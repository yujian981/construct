package org.cn;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowTest {
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;

    @Test
    public  void Test() {

        Map<String, Object> vars = new HashMap<>();
        //设置流程发起用户信息
        identityService.setAuthenticatedUserId("worker");
//        vars.put("assigneeList", Arrays.asList());
        String[]v={"张三","李四","王五","马六"};
        vars.put("assigneeList", Arrays.asList(v));
//        vars.put("flow",1);
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("process:7:2543", vars);

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("taskName1:" + task.getName() + "|assignee:" + task.getAssignee());
//        taskService.complete(task.getId());
//        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult(); //.taskAssignee()
    }

    @Test
    public  void Test1() {
        Map<String, Object> vars = new HashMap<>();
        String[]v={"蜘蛛侠","青蜂侠","黑寡妇","钢铁侠","大黄蜂","杜爸爸"};
        vars.put("assignee", Arrays.asList(v));
//        vars.put("assignee", Arrays.asList("张三","李四","王五","马六"));
        vars.put("flow",1);
        taskService.complete("47510", vars);
    }


}
