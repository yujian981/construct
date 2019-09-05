package org.cn.modules.activiti.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cty
 */
public class Xs {


    @Autowired
    ProcessEngine processEngine;

    public static void SayHello(){

        Map<Object,String> map=new HashMap<>();
        map.put("limit","1");
        map.put("assignee","猪八戒");
        //ProcessEngines.getDefaultProcessEngine().getTaskService().complete(taskId,params);
        System.out.println("当前任务执行完毕");
    }

}
