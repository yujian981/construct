package org.cn.modules.activiti.listener;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cty
 */
public class MyExeListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String eventName = delegateExecution.getEventName();
        if ("start".equals(eventName)) {
            System.out.println("start=========");



        } else if ("end".equals(eventName)) {
            System.out.println(delegateExecution.getCurrentActivityName()+"-------"+"end=========");
        }
    }
}