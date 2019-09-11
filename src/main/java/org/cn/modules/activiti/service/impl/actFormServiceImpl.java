package org.cn.modules.activiti.service.impl;

import org.cn.modules.activiti.dao.ActFormDao;
import org.cn.modules.activiti.entity.ActFormEntity;
import org.cn.modules.activiti.service.ActFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class actFormServiceImpl implements ActFormService {

    @Autowired
    ActFormDao actFormDao;

    @Override
    public Integer setVariable(ActFormEntity entity) {

        Integer ret = this.actFormDao.setVariable(entity);

        return ret;
    }

    @Override
    public List<ActFormEntity> getVariable(String id) {
        return  this.actFormDao.getVariable(id);
    }
}
