package org.cn.modules.activiti.dao;


import org.cn.modules.activiti.entity.ActFormEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActFormDao {

    Integer setVariable(ActFormEntity entity);

    List<ActFormEntity> getVariable(String id);
}
