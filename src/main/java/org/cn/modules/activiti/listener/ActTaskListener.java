package org.cn.modules.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;


/**
 * 任务节点监听器  关于用户
 * @author Cty
 * ExecutionListener
 */
public class ActTaskListener implements TaskListener {



    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {
//        List<String> list = new ArrayList<>();
//        list.add("孙悟空");
//        list.add("猪八戒");
//        delegateTask.addCandidateUsers(list);

        String eventName = delegateTask.getEventName();
        if ("assignment".endsWith(eventName)){
            System.out.println(delegateTask.getAssignee());
            System.out.println("assignment========");

        }




    }

}
