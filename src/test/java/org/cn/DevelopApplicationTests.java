package org.cn;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevelopApplicationTests {

    @Autowired
    private ProcessEngine processEngine;
    @Resource
    private TaskService taskService;

    //modeler.html?modelId=22501

    //资料 https://juejin.im/post/5aafa3eef265da23784015b9
    //RepositoryService----操作静态的资源（流程定义，bpmn、png）
    //RuntimeService-----操作流程实例（启动流程实例、查询流程实例、结束流程实例）
    //TaskService-----操作任务（查询任务、办理任务）
    //HistoryService----操作历史数据
    @Test
    @ApiOperation("启动流程")
    public void startProcess() {
        //指定执行我们刚才部署的工作流程
        String processDefiKey = "process:5:12511";
        Map<String, Object> params = new HashMap<String, Object>();
//        identityService.setAuthenticatedUserId("worker");
//        taskService.setAssignee();
//        taskService.claim();

//        params.put("leader", "唐僧");
        String[]v={"蜘蛛侠","青蜂侠","黑寡妇","钢铁侠","大黄蜂","杜爸爸"};//,
//        map.put("assignee", v);
        params.put("assigneeList", Arrays.asList(v));
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceById(processDefiKey, params);
        System.out.println("流程执行对象的id：" + pi.getId());// Execution 对象
        System.out.println("流程实例的id：" + pi.getProcessInstanceId());// ProcessInstance
        // 对象
        System.out.println("流程定义的id：" + pi.getProcessDefinitionId());// 默认执行的是最新版本的流程定义
    }


    @Test
    @ApiOperation("查询任务")
    public void queryTask() {

        //任务的办理人
        String assignee = "唐僧";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("");

        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();
        //遍历任务列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("任务的id：" + task.getId());
                System.out.println("任务的名称：" + task.getName());
            }
        }
    }


    public boolean isMultiInstance(String taskId){

            boolean flag = false;
            Task task=processEngine.getTaskService().createTaskQuery() // 创建任务查询
                    .taskId(taskId) // 根据任务id查询
                    .singleResult();
            if(task != null){
                // 获取流程定义id
                String processDefinitionId=task.getProcessDefinitionId();

                ProcessDefinitionEntity processDefinitionEntity=(ProcessDefinitionEntity) processEngine.getRepositoryService()
                        .getProcessDefinition(processDefinitionId);

                // 根据活动id获取活动实例
                ActivityImpl activityImpl=processDefinitionEntity.findActivity(task.getTaskDefinitionKey());

                if(((ActivityImpl) activityImpl).getActivityBehavior() instanceof ParallelMultiInstanceBehavior){
                    ParallelMultiInstanceBehavior behavior = (ParallelMultiInstanceBehavior)activityImpl.getActivityBehavior();
                    if(behavior != null && behavior.getCollectionExpression() != null){
                        flag = true;
                    }
                }
            }

            return flag;

    }

    //TODO  问题待处理
    @Test //查询当前任务是否是会签任务
    public void isMultiInstance(){
        boolean flag = false;
        Task task=processEngine.getTaskService().createTaskQuery() // 创建任务查询
                .taskId("57545") // 根据任务id查询
                .singleResult();
        if(task != null){
            // 获取流程定义id
            String processDefinitionId=task.getProcessDefinitionId();

            ProcessDefinitionEntity processDefinitionEntity=(ProcessDefinitionEntity) processEngine.getRepositoryService()
                    .getProcessDefinition(processDefinitionId);

            // 根据活动id获取活动实例
            ActivityImpl activityImpl=processDefinitionEntity.findActivity(task.getTaskDefinitionKey());

            if(((ActivityImpl) activityImpl).getActivityBehavior() instanceof ParallelMultiInstanceBehavior){
                ParallelMultiInstanceBehavior behavior = (ParallelMultiInstanceBehavior)activityImpl.getActivityBehavior();
                if(behavior != null && behavior.getCollectionExpression() != null){
                    flag = true;
                }
            }
        }
        System.out.println(flag);
    }


    @Test
    @ApiOperation(" 代办 查询")
    public void todoList(){

        TaskQuery query = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned("张三");
        List<Task> list = query.list();
        System.out.println(list);

        List<IdentityLink> identityLinksForProcessInstance = processEngine.getRuntimeService().getIdentityLinksForProcessInstance(list.get(0).getProcessInstanceId());

        System.out.println(identityLinksForProcessInstance);
    }


    @Test
    @ApiOperation("")
    public void test1(){

        //查询当前任务ID
//        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId("52501").taskAssignee("黑寡妇").singleResult();
//        System.out.println(task);


        processEngine.getTaskService().addComment("57538", "52501", "测试转交");
        processEngine.getTaskService().delegateTask("57538", "旺旺");
        processEngine.getTaskService().setOwner("57538", "黑寡妇");

    }

    //TODO 有疑问
    @Test
    @ApiOperation("已办理")
    public void doneList(){
        HistoricTaskInstanceQuery query = processEngine.getHistoryService().
                createHistoricTaskInstanceQuery().or().taskCandidateUser("小A").
                taskAssignee("蜘蛛侠").endOr().finished();
        List<HistoricTaskInstance> list = query.list();
        System.out.println(list);
    }

    @Test
    @ApiOperation(value = "完成当前任务", notes = "多实例任务全部完成后才能进行转交 https://www.jianshu.com/p/dfad80be1dbf")
    public void compileTask() {
        String taskId = "20008";
        Map<String, Object> map = new HashMap<>();
        List<String> maps = new ArrayList<String>();
//         map.put("limit","2");
        String[]v={"蜘蛛侠","青蜂侠","黑寡妇","钢铁侠","大黄蜂","杜爸爸"};//,
//        map.put("assignee", v);
       map.put("assigneeList", Arrays.asList(v));
//        maps.add("小A");
//        maps.add("小B");
//        maps.add("小C");
//        map.put("assignee",maps);


        //流程变量
        //taskId：localScope=true 任务结束后不会存在实例 多人部署和 localScope 冲突
        processEngine.getTaskService().resolveTask(taskId);
        processEngine.getTaskService().complete(taskId, map);

        System.out.println("当前任务执行完毕");
    }




    @Test
    public void getTaskList() {


//        ProcessDefinitionEntity processDefinitionEntity = null;
//
//        String id = null;
//
//        TaskDefinition task = null;
//
//        //获取流程实例Id信息
//        String processInstanceId = processEngine.getTaskService().createTaskQuery().taskId("147506").singleResult().getProcessInstanceId();
//
//        //获取流程发布Id信息
//        String definitionId = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
//
//        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())
//                .getDeployedProcessDefinition(definitionId);
//
//        ExecutionEntity execution = (ExecutionEntity) processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//
//        //当前流程节点Id信息
//        String activitiId = execution.getActivityId();
//
//        //获取流程所有节点信息
//        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
//
//        //遍历所有节点信息
//        for(ActivityImpl activityImpl : activitiList){
//            String instance = (String) activityImpl.getProperties().get("multiInstance");
//            System.out.println(instance);
//            // 找到当前节点信息
//            if (activitiId.equals(id)) {
//
//                //获取下一个节点信息
////                task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
//
//                break;
//            }
//        }

    }

    @Test
    @ApiOperation("获取多实例")
    public void getAct(){
        Boolean activity = getActivity("147501");
        System.out.println(activity);
    }


    private Boolean getActivity(String id){
        ProcessDefinitionEntity processDefinitionEntity = null;
//        String id = null;
        TaskDefinition task = null;
        //获取流程发布Id信息
        String definitionId = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(id).singleResult().getProcessDefinitionId();

        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())
                .getDeployedProcessDefinition(definitionId);

        ExecutionEntity execution = (ExecutionEntity) processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(id).singleResult();

        //当前流程节点Id信息(暂未需要)
        String activitiId = execution.getActivityId();

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        for (int i = 0; i < activitiList.size(); i++) {

            //验证节点参数
            if (!("sid-E4515070-B23B-4FA9-ADE9-D1D09A60F1F7").equals(activitiList.get(i).getId())){
                continue;
            }
            //验证是否是多实例任务
            if (StrUtil.isNotBlank((String) activitiList.get(i).getProperties().get("multiInstance"))
                    && ("parallel").equals((String) activitiList.get(i).getProperties().get("multiInstance"))){
                return true;
            }
        }
        return false;

    }


    @Test
    public void getForm(){
        TaskFormData taskFormData = processEngine.getFormService().getTaskFormData("57559");
        String key = taskFormData.getFormKey();
        System.out.println(key);
    }

    @Test
    public void getActivityName(){

        List<HistoricActivityInstance> list = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId("52501").unfinished() //未完成的活动(任务)
                .list();
        for (HistoricActivityInstance instance : list) {
            System.out.println(instance);
        }

        //HistoricActivityInstance hai=historyService.createHistoricActivityInstanceQuery()//
        //    .processInstanceId(hvi.getProcessInstanceId())//
        //    .unfinished()
        //    .singleResult();
        //if(hai!=null){
        //    map.put("piState", hai.getActivityName());// 流程状态
        //}else{
        //    map.put("piState", "完结");// 流程状态
        //}
    }

    @Test
    @ApiOperation("查看bpmn 资源图片")
    public void viewImage() throws Exception {
        String deploymentId = "5001";
        String imageName = null;
        //取得某个部署的资源的名称  deploymentId
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        // buybill.bpmn  buybill.png
        if (resourceNames != null && resourceNames.size() > 0) {
            for (String temp : resourceNames) {
                if (temp.indexOf(".png") > 0) {
                    imageName = temp;
                }
            }
        }
        /**
         * 读取资源
         * deploymentId:部署的id
         * resourceName：资源的文件名
         */
        InputStream resourceAsStream = processEngine.getRepositoryService()
                .getResourceAsStream(deploymentId, imageName);
        //把文件输入流写入到文件中
        File file = new File("d:/" + imageName);
        FileUtils.copyInputStreamToFile(resourceAsStream, file);
    }


    @Test
    @ApiOperation("查看流程定义")
    public void queryProcessDefination() {
        String processDefiKey = "process";//流程定义key
        //获取流程定义列表
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                //查询 ，好比where
//		.processDefinitionId(proDefiId) //流程定义id
                // 流程定义id  ： buyBill:2:704   组成 ： proDefikey（流程定义key）+version(版本)+自动生成id
                .processDefinitionKey(processDefiKey)//流程定义key 由bpmn 的 process 的  id属性决定
//		.processDefinitionName(name)//流程定义名称  由bpmn 的 process 的  name属性决定
//		.processDefinitionVersion(version)//流程定义的版本
                .latestVersion()//最新版本

                //排序
                .orderByProcessDefinitionVersion().desc()//按版本的降序排序

                //结果
//		.count()//统计结果
//		.listPage(arg0, arg1)//分页查询
                .list();
        //遍历结果
        if (list != null && list.size() > 0) {
            for (ProcessDefinition temp : list) {
                System.out.print("流程定义的id: " + temp.getId());
                System.out.print("流程定义的key: " + temp.getKey());
                System.out.print("流程定义的版本: " + temp.getVersion());
                System.out.print("流程定义部署的id: " + temp.getDeploymentId());
                System.out.println("流程定义的名称: " + temp.getName());
            }
        }
    }


    @Test
    @ApiOperation("删除流程定义")
    public void deleteProcessDefi() {
        //通过部署id来删除流程定义 如果有启动中的流程无法删除 先执行 delPrcInById方法
        String deploymentId = "4";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

    @Test
    @ApiOperation("删除已经启动的任务")
    public void delPrcInById() {
        //通过部署id来删除流程定义
        String proc_inst_id = "2501";
        processEngine.getRuntimeService().deleteProcessInstance(proc_inst_id, "原因");
    }
    //HistoryService删除历史操作
    //deleteHistoricProcessInstance
    //deleteHistoricTaskInstance
    @Test
    @ApiOperation("删除历史操作")
    public void delHistory(){
         String proInstId="2501";
        processEngine.getHistoryService().deleteHistoricProcessInstance(proInstId);
        System.out.println("执行完成！");
    }


    @Test
    @ApiOperation("任务附件")
    public void startFile() {
    // 添加附件(地址位于/url/test.png)到task中  附件存在表:act_hi_attachment
            String taskId="35038";
            String processInstanceId="32501";
        processEngine.getTaskService().createAttachment("url", taskId,
                processInstanceId,
                "name",
                "desc",
                "/url/test12.png");

    // 查询附件
        List<Attachment> attachmentList = processEngine.getTaskService().getTaskAttachments(taskId);
        for (Attachment attachment : attachmentList) {
            System.out.println(attachment);
    //            logger.info("attach={}",attachment);
        }

    }


    @Test
    @ApiOperation("任务意见")
    public void start(){
        String taskId="35038";
        String processInstanceId="32501";
        //新增意见
        processEngine.getTaskService().addComment(taskId,processInstanceId,"record23334");
        processEngine.getTaskService().addComment(taskId,processInstanceId,"record ndfsdote 2");

        // 查询意见
        List<Comment> commentList =  processEngine.getTaskService().getTaskComments(taskId);
        for(Comment comment : commentList){
            System.out.println(ToStringBuilder.reflectionToString(comment, ToStringStyle.MULTI_LINE_STYLE));
//            logger.info("comment={}", ToStringBuilder.reflectionToString(comment, ToStringStyle.MULTI_LINE_STYLE));
        }
        //删除意见
        processEngine.getTaskService().deleteComments("35038","32501");
    }




    @Test
    @ApiOperation("查询正在运行任务")
    public void queryTasks() {
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.list();
        //遍历任务列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("任务的id：" + task.getId());
                System.out.println("任务的名称：" + task.getName());

            }
        }
    }


    @Test
    @ApiOperation("获取流程实例的状态")
    public void getProcessInstanceState() {
        String processInstanceId = "37501";
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();//返回的数据要么是单行，要么是空 ，其他情况报错
        //判断流程实例的状态
        if (pi != null) {
            System.out.println("该流程实例" + processInstanceId + "正在运行...  " + "当前活动的任务:" + pi.getActivityId());
        } else {
            System.out.println("当前的流程实例" + processInstanceId + " 已经结束！");
        }
    }

    @Test
    @ApiOperation("查看历史实例执行任务信息")
    public void queryHistoryTask() {
        String processInstanceId = "37501";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance temp : list) {
                System.out.print("历史流程实例任务id:   " + temp.getId());
                System.out.print("历史流程定义的id:   " + temp.getProcessDefinitionId());
                System.out.print("历史流程实例任务名称:   " + temp.getName());
                System.out.println("历史流程实例任务处理人:   " + temp.getAssignee());
            }
        }
    }


    @Test
    @ApiOperation(value = "转交任务", tags = "可以分配个人任务从一个人到另一个人（认领任务）")
    public void setAssigneeTask() {
        //任务ID
        String taskId = "35038";

        //指定的办理人
        String userId = "张翠山";
        processEngine.getTaskService()//
                .setAssignee(taskId, userId);

        //委托
        processEngine.getTaskService().delegateTask("35038", "cc");

    }

    //
    @Test
    @ApiOperation(value = "任务签收状态",tags = "https://blog.csdn.net/Code_shadow/article/details/82777959")
    public void getHistryUser() {
        // 获取当前流程的任务信息
        List<Task> list = processEngine.getTaskService().createTaskQuery().processInstanceId("25001").list();
        System.out.println(list);
        // 已签收和未签收同时查询
        List<Task> list1 = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned("杜爸爸").list();
        System.out.println(list1);
        //已经签收过的任务列表，某种意义上我理解为真正的办理人
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee("杜爸爸").list();
        System.out.println(tasks);
        //只查询未签收的任务
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().taskCandidateUser("杜爸爸").list();
        System.out.println(taskList);
        //用户组待签收/待办理
        List<Task> tasks1 = processEngine.getTaskService().createTaskQuery().taskCandidateGroup("杜爸爸").list();
        System.out.println(tasks1);

    }


    @Test
    @ApiOperation(value = "流程运行节点信息",tags = "https://blog.csdn.net/Code_shadow/article/details/82777959")
    public void findHisto(){

        //查询流程运行的节点
        List<HistoricTaskInstance> list1 = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId("25001")
                .orderByHistoricTaskInstanceEndTime()
                .asc()
                .list();
        System.out.println(list1);
    }





    @Test
    @ApiOperation("根据后缀.bpmn部署流程")
    public void deployProcessDefi() {
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment().name("用户任务指定流程")
                .addClasspathResource("AppayBill.bpmn")
                .deploy();

        System.out.println("部署名称:" + deploy.getName());
        System.out.println("部署id:" + deploy.getId());
    }

    @ApiOperation("表单扩展--设置流程变量值")
    @Test
    public void setVariable() {
//        ActContentEntity act=new ActContentEntity();
//        act.setCREATE_BY_("小张");
//        act.setCREATE_TIME_(new Date());
//        act.setPROC_INST_ID_("1111");
//        act.setSING_("个人");
//
//        Object o = actFormService.setVariable(act);
//        System.out.println(o);
//        System.out.println("设置成功！");
    }

    @Test
    @ApiOperation("表单扩展--查询流程变量")
    public void getVariable(){

//       List<ActFormEntity> act= actFormService.getVariable("1111");

//        System.out.println(act);
    }




    @Test
    @ApiOperation("设置流程变量数据")
    public void setVariableValues(){
        TaskService taskService=processEngine.getTaskService(); // 任务Service
        String taskId="147506";
        //方法一： 变量名称相同每一次 替换前一次的变量信息
//        taskService.setVariable(taskId, "days", 2);
//        //方法二：变量名称相同 taskId不同 不会替换前一次的变量信息
//        taskService.setVariableLocal(taskId, "days", 2);
//        taskService.setVariableLocal(taskId, "date", new Date());
//        taskService.setVariableLocal(taskId, "reason", "发烧");

        //1. 第一次设置流程变量
//		taskService.setVariable(taskId, "cost", 1000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());

		taskService.setVariableLocal(taskId, "申请人", "何某某");//该变量只有在本任务中是有效的
		taskService.setVariableLocal(taskId, "申请人", "何某某");//该变量只有在本任务中是有效的
		taskService.setVariableLocal(taskId, "申请人", "何某某");//该变量只有在本任务中是有效的


        //2. 在不同的任务中设置变量
//		taskService.setVariable(taskId, "cost", 5000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());
//		taskService.setVariableLocal(taskId, "申请人", "李某某");//该变量只有在本任务中是有效的
        System.out.println("设置变量完成!");
    }

    @Data
    class Student{

        private int Id;
        private String Name;
    }

    @Test
    @ApiOperation("获取流程变量数据")
    public void getVariableValues(){
        TaskService taskService=processEngine.getTaskService(); // 任务Service
        String taskId="57538";
        Integer days=(Integer) taskService.getVariable(taskId, "days");
        Date date=(Date) taskService.getVariable(taskId, "date");
        String reason=(String) taskService.getVariable(taskId, "reason");
//        Student student=(Student) taskService.getVariable(taskId, "student");
        System.out.println("请假天数："+days);
        System.out.println("请假日期："+date);
        System.out.println("请假原因："+reason);
//        System.out.println("请假对象："+student.getId()+","+student.getName());
    }

    @Test
    @ApiOperation("")
    public void getVaribleValues(){

        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId("52501").list();
        for (Task task : list) {
            System.out.println(task.getId());
            TaskFormData taskFormData = processEngine.getFormService().getTaskFormData(task.getId());
            System.out.println(taskFormData);
        }


    }


    /**
     * 4.查询历史流程变量(act_hi_varinst表)
     */
    @Test
    public void findHistoryProcessVariables(){
        String processInstanceId = "52501";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()                                      //创建一个历史的流程变量查询对象
                .processInstanceId(processInstanceId)
                .list();
        if(list !=null && list.size()>0){
            for(HistoricVariableInstance hvi:list){
                System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());
                System.out.println("###############################################");
            }
        }

    }

}
