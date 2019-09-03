package org.cn.modules.activiti.service.impl;

import org.cn.base.BaseDao;
import org.cn.base.BaseService;
import org.cn.modules.activiti.entity.ActModel;
import org.cn.modules.activiti.service.ActModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cty
 */
@Service
public class ActModelServiceImpl  implements ActModelService  {

    @Override
    public BaseDao<ActModel, String> getRepository() {
        return null;
    }
}
