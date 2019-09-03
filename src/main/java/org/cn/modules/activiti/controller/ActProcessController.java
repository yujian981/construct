package org.cn.modules.activiti.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cty
 */
@Slf4j
@RestController
@Api(description = "流程定义管理接口")
@RequestMapping("/actProcess")
@Transactional
public class ActProcessController {


    @Autowired
    private RepositoryService repositoryService;


}
