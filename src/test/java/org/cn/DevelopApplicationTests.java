package org.cn;

import io.swagger.annotations.ApiOperation;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevelopApplicationTests {

    @Autowired
    private ProcessEngine processEngine;

    //资料 https://juejin.im/post/5aafa3eef265da23784015b9
    @Test
    @ApiOperation("启动流程")
    public void startProcess(){

        //指定执行我们刚才部署的工作流程
        String processDefiKey="process";
        //取运行时服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //取得流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefiKey);//通过流程定义的key 来执行流程
        System.out.println("流程实例id:"+pi.getId());//流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());//输出流程定义的id
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




}
