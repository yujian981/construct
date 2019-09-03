package org.cn.modules.activiti.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.cn.config.activiti.ActivitiExtendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cty
 */
@Slf4j
@RestController
@Api(description = "流程实例管理接口")
@RequestMapping("/actProcess")
@Transactional
public class ActProcessInsController {

    @Autowired
    private ActivitiExtendProperties properties;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;








}
