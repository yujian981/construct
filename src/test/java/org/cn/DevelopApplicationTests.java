package org.cn;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevelopApplicationTests {

    @Autowired
    private ProcessEngine processEngine;

    //资料 https://juejin.im/post/5aafa3eef265da23784015b9
    @Test
    @ApiOperation("启动流程")
    public void startProcess(){
//        //取运行时服务
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        //取得流程实例
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefiKey);//通过流程定义的key 来执行流程
//        System.out.println("流程实例id:"+pi.getId());//流程实例id
//        System.out.println("流程定义id:"+pi.getProcessDefinitionId());//输出流程定义的id

       //指定执行我们刚才部署的工作流程
        String processDefiKey="process";
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userID", "王某某");
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefiKey, params);

        System.out.println("流程执行对象的id：" + pi.getId());// Execution 对象
        System.out.println("流程实例的id：" + pi.getProcessInstanceId());// ProcessInstance
        // 对象
        System.out.println("流程定义的id：" + pi.getProcessDefinitionId());// 默认执行的是最新版本的流程定义


    }



    @Test
    @ApiOperation("查询任务")
    public void queryTask(){
        //任务的办理人
        String assignee="";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();
        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());
            }
        }
    }


    @Test
    @ApiOperation("完成当前任务")
    public void compileTask(){
        String taskId="17504";
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }


    @Test
    @ApiOperation("查看bpmn 资源图片")
    public void viewImage() throws Exception{
        String deploymentId="5001";
        String imageName=null;
        //取得某个部署的资源的名称  deploymentId
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        // buybill.bpmn  buybill.png
        if(resourceNames!=null&&resourceNames.size()>0){
            for(String temp :resourceNames){
                if(temp.indexOf(".png")>0){
                    imageName=temp;
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
        File file=new File("d:/"+imageName);
        FileUtils.copyInputStreamToFile(resourceAsStream, file);
    }


    @Test
    @ApiOperation("查看流程定义")
    public void queryProcessDefination(){
        String processDefiKey="process";//流程定义key
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
        if(list!=null&&list.size()>0){
            for(ProcessDefinition temp:list){
                System.out.print("流程定义的id: "+temp.getId());
                System.out.print("流程定义的key: "+temp.getKey());
                System.out.print("流程定义的版本: "+temp.getVersion());
                System.out.print("流程定义部署的id: "+temp.getDeploymentId());
                System.out.println("流程定义的名称: "+temp.getName());
            }
        }
    }


    @Test
    @ApiOperation("删除流程定义")
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="101";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }


    @Test
    @ApiOperation("开始流程")
    public void start(){
        String processDefiKey="leaveActiviti";//bpmn 的 process id属性
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefiKey);
        System.out.println("流程执行对象的id："+pi.getId());//Execution 对象
        System.out.println("流程实例的id："+pi.getProcessInstanceId());//ProcessInstance 对象
        System.out.println("流程定义的id："+pi.getProcessDefinitionId());//默认执行的是最新版本的流程定义
    }


    @Test
    @ApiOperation("查询正在运行任务")
    public void queryTasks(){
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.list();
        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());

            }
        }
    }


    @Test
    @ApiOperation("获取流程实例的状态")
    public void getProcessInstanceState(){
        String processInstanceId="17501";
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();//返回的数据要么是单行，要么是空 ，其他情况报错
        //判断流程实例的状态
        if(pi!=null){
            System.out.println("该流程实例"+processInstanceId+"正在运行...  "+"当前活动的任务:"+pi.getActivityId());
        }else{
            System.out.println("当前的流程实例"+processInstanceId+" 已经结束！");
        }
    }

    @Test
    @ApiOperation("查看历史实例执行任务信息")
    public void queryHistoryTask(){
        String processInstanceId="17501";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if(list!=null&&list.size()>0){
            for(HistoricTaskInstance temp:list){
                System.out.print("历史流程实例任务id:"+temp.getId());
                System.out.print("历史流程定义的id:"+temp.getProcessDefinitionId());
                System.out.print("历史流程实例任务名称: "+temp.getName());
                System.out.println("历史流程实例任务处理人: "+temp.getAssignee());
            }
        }
    }


    @Test
    @ApiOperation("模拟流程变量设置")
    public void  getAndSetProcessVariable(){
        //有两种服务可以设置流程变量
//		TaskService taskService = processEngine.getTaskService();
//		RuntimeService runtimeService = processEngine.getRuntimeService();

        /**1.通过 runtimeService 来设置流程变量
         * executionId: 执行对象
         * variableName：变量名
         * values：变量值
         */
//		runtimeService.setVariable(executionId, variableName, values);
//		runtimeService.setVariableLocal(executionId, variableName, values);
        //设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		runtimeService.setVariables(executionId, variables);
        //可以设置多个变量  放在 Map<key,value>  Map<String,Object>

        /**2. 通过TaskService来设置流程变量
         * taskId：任务id
         */
//		taskService.setVariable(taskId, variableName, values);
//		taskService.setVariableLocal(taskId, variableName, values);
////		设置本执行对象的变量 ，该变量的作用域只在当前的execution对象
//		taskService.setVariables(taskId, variables); //设置的是Map<key,values>

        /**3. 当流程开始执行的时候，设置变量参数
         * processDefiKey: 流程定义的key
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getRuntimeService()
//		.startProcessInstanceByKey(processDefiKey, variables)

        /**4. 当任务完成时候，可以设置流程变量
         * taskId:任务id
         * variables： 设置多个变量  Map<key,values>
         */
//		processEngine.getTaskService().complete(taskId, variables);


        /** 5. 通过RuntimeService取变量值
         * exxcutionId： 执行对象
         *
         */
//		runtimeService.getVariable(executionId, variableName);//取变量
//		runtimeService.getVariableLocal(executionId, variableName);//取本执行对象的某个变量
//		runtimeService.getVariables(variablesName);//取当前执行对象的所有变量
        /** 6. 通过TaskService取变量值
         * TaskId： 执行对象
         *
         */
//		taskService.getVariable(taskId, variableName);//取变量
//		taskService.getVariableLocal(taskId, variableName);//取本执行对象的某个变量
//		taskService.getVariables(taskId);//取当前执行对象的所有变量
    }


    //设置流程变量值
    @Test
    public void setVariable(){
        String taskId="20006";//任务id
        //采用TaskService来设置流程变量

        //1. 第一次设置流程变量
//		TaskService taskService = processEngine.getTaskService();
//		taskService.setVariable(taskId, "cost", 1000);//设置单一的变量，作用域在整个流程实例
//		taskService.setVariable(taskId, "申请时间", new Date());
//		taskService.setVariableLocal(taskId, "申请人", "何某某");//该变量只有在本任务中是有效的


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
        TaskService taskService = processEngine.getTaskService();
        //传递的一个自定义bean对象
        AppayBillBean appayBillBean=new AppayBillBean();
        appayBillBean.setId(1);
        appayBillBean.setCost(300);
        appayBillBean.setDate(new Date());
        appayBillBean.setAppayPerson("何某某");
        taskService.setVariable(taskId, "appayBillBean", appayBillBean);


        System.out.println("设置成功！");

    }

    @Data
    public class AppayBillBean{
        private Integer id;
        private Integer cost;//金额
        private String appayPerson;//申请人
        private Date date;//申请日期

    }


    @Test
    @ApiOperation("查询流程变量")
    public void getVariable(){
        String taskId="1804";//任务id
//		TaskService taskService = processEngine.getTaskService();
//		Integer cost=(Integer) taskService.getVariable(taskId, "cost");//取变量
//		Date date=(Date) taskService.getVariable(taskId, "申请时间");//取本任务中的变量
////		Date date=(Date) taskService.getVariableLocal(taskId, "申请时间");//取本任务中的变量
//		String appayPerson=(String) taskService.getVariableLocal(taskId, "申请人");//取本任务中的变量
////		String appayPerson=(String) taskService.getVariable(taskId, "申请人");//取本任务中的变量
//
//		System.out.println("金额:"+cost);
//		System.out.println("申请时间:"+date);
//		System.out.println("申请人:"+appayPerson);


        //读取实现序列化的对象变量数据
        TaskService taskService = processEngine.getTaskService();
        AppayBillBean appayBillBean=(AppayBillBean) taskService.getVariable(taskId, "appayBillBean");
        System.out.println(appayBillBean.getCost());
        System.out.println(appayBillBean.getAppayPerson());

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

}
