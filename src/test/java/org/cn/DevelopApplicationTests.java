package org.cn;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Temporal;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevelopApplicationTests {

    @Autowired
    private ProcessEngine processEngine;


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
        String processDefiKey = "ceshi";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assignee", "唐僧");
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefiKey, params);

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


    @Test
    @ApiOperation(value = "完成当前任务", notes = "多实例任务全部完成后才能进行转交 https://www.jianshu.com/p/dfad80be1dbf")
    public void compileTask() {
        String taskId = "35031";
        Map<String, Object> map = new HashMap<>();
//        map.put("limit","3");
//        String[]v={"蜘蛛侠","青蜂侠","黑寡妇","钢铁侠"};
//        map.put("assigneeList", Arrays.asList(v));
        //taskId：localScope=true 任务结束后不会存在实例
        processEngine.getTaskService().complete(taskId, map, true);
        System.out.println("当前任务执行完毕");
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
        String deploymentId = "8";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }

    @Test
    @ApiOperation("删除已经启动的任务")
    public void delPrcInById() {
        //通过部署id来删除流程定义
        String proc_inst_id = "5001";
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
    @ApiOperation("查询历史流程变量(act_hi_varinst表)")
    public void findHistoryProcessVariables() {
        String processInstanceId = "32501";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()                                      //创建一个历史的流程变量查询对象
                .processInstanceId(processInstanceId)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance hvi : list) {
                System.out.println(hvi.getId() + "   " + hvi.getProcessInstanceId() + "   " + hvi.getVariableName() + "   " + hvi.getVariableTypeName() + "    " + hvi.getValue());
                System.out.println("###############################################");
            }
        }
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
    public void getHistryUser() {
        // 获取当前流程的任务信息
        List<Task> list = processEngine.getTaskService().createTaskQuery().processInstanceId("32501").list();
        System.out.println(list);
        // 已签收和未签收同时查询
        List<Task> list1 = processEngine.getTaskService().createTaskQuery().taskCandidateOrAssigned("张翠山").list();
        System.out.println(list1);
        //只查询未签收的任务
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().taskCandidateUser("张翠山").list();
        System.out.println(taskList);

    }


    //设置流程变量值
    @Test
    public void setVariable() {
        String taskId = "35024";//任务id
        //采用TaskService来设置流程变量

        //1. 第一次设置流程变量
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariable(taskId, "cost", 1000);//设置单一的变量，作用域在整个流程实例
        taskService.setVariable(taskId, "申请时间", new Date());
        taskService.setVariableLocal(taskId, "申请人", "蜘蛛侠");//该变量只有在本任务中是有效的


        //2. 在不同的任务中设置变量
//		TaskService taskService = processEngine.getTaskService();
//		taskService.setVariable(taskId, "cost", 5000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());
//		taskService.setVariableLocal(taskId, "申请人", "李某某");//该变量只有在本任务中是有效的

        /**
         * 3. 变量支持的类型
         * - 简单的类型 ：String 、boolean、Integer、double、date
         * - 自定义对象bean
         */
//        TaskService taskService = processEngine.getTaskService();
//        //传递的一个自定义bean对象
//        AppayBillBean appayBillBean=new AppayBillBean();
//        appayBillBean.setId(1);
//        appayBillBean.setCost(300);
//        appayBillBean.setDate(new Date());
//        appayBillBean.setAppayPerson("何某某");
//        taskService.setVariable(taskId, "appayBillBean", appayBillBean);


        System.out.println("设置成功！");

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


    @Test
    @ApiOperation("查询流程变量")
    public void getVariable() {
        String taskId = "45005";//任务id
        TaskService taskService = processEngine.getTaskService();
        Integer cost = (Integer) taskService.getVariable(taskId, "cost");//取变量
        Date date = (Date) taskService.getVariable(taskId, "申请时间");//取本任务中的变量
//		Date date=(Date) taskService.getVariableLocal(taskId, "申请时间");//取本任务中的变量
        String appayPerson = (String) taskService.getVariableLocal(taskId, "申请人");//取本任务中的变量
//		String appayPerson=(String) taskService.getVariable(taskId, "申请人");//取本任务中的变量

        System.out.println("金额:" + cost);
        System.out.println("申请时间:" + date);
        System.out.println("申请人:" + appayPerson);


        //读取实现序列化的对象变量数据
//        TaskService taskService = processEngine.getTaskService();
//        AppayBillBean appayBillBean=(AppayBillBean) taskService.getVariable(taskId, "appayBillBean");
//        System.out.println(appayBillBean.getCost());
//        System.out.println(appayBillBean.getAppayPerson());

    }

    @Data
    public class AppayBillBean {
        private Integer id;
        private Integer cost;//金额
        private String appayPerson;//申请人
        private Date date;//申请日期

    }

}
