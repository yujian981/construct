package org.cn.modules.activiti.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cty
 */
@Slf4j
@RestController
@Api(description = "业务申请管理接口")
@RequestMapping("/actBusiness")
@Transactional
public class ActBusinessController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;







}
