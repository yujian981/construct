package org.cn.modules.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.cn.modules.activiti.controller.Xs;


/**
 * 监听器测试
 * @author Cty
 */
public class ActListener implements ExecutionListener {


    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Xs.SayHello();
        //org.cn.modules.activiti.listener.ActListener
    }
}
