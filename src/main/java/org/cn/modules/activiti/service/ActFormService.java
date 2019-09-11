package org.cn.modules.activiti.service;


import org.cn.modules.activiti.entity.ActFormEntity;

import java.util.List;

public interface ActFormService {

    public Integer setVariable(ActFormEntity entity);


    public List<ActFormEntity> getVariable(String id);
}
