package org.cn;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestActiviti {

    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void test1(){

//        Task task = this.processEngine.getTaskService().createTaskQuery().processInstanceId("135001").singleResult();
//        Map<String, Object> variables = this.processEngine.getTaskService().getVariables(task.getId());
//
//        System.out.println(variables);

        List<HistoricDetail> list = this.processEngine.getHistoryService().createHistoricDetailQuery().
                processInstanceId("147501").list();
        HistoricVariableUpdate variable = null;
        for (HistoricDetail historicDetail : list) {
            variable = (HistoricVariableUpdate) historicDetail;
//            System.out.println(variable);
            System.out.println(            ((HistoricVariableUpdate) historicDetail).getValue());
        }
    }


    @Test
    public void setValue(){
        TaskService taskService=processEngine.getTaskService(); // 任务Service
        String taskId="160040";
        taskService.setVariableLocal(taskId, "申请人", "黑寡妇");
        taskService.setVariableLocal(taskId, "申请意见", "第四次");
        taskService.setVariableLocal(taskId, "申请时间", new Date());
    }


}
