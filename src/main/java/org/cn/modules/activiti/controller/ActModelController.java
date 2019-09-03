package org.cn.modules.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.cn.common.constant.Result;
import org.cn.common.utils.ResultUtil;
import org.cn.modules.activiti.entity.ActModel;
import org.cn.modules.activiti.service.ActModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Cty
 */
@Slf4j
@RestController
@Api(description = "模型管理接口")
@RequestMapping("/model")
@Transactional
public class ActModelController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ActModelService actModelService;

    @RequestMapping("create")
    public void createModel(HttpServletRequest request, HttpServletResponse response){
        try{
            String modelName = "modelName";
            String modelKey = "modelKey";
            String description = "description";

            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            RepositoryService repositoryService = processEngine.getRepositoryService();

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(modelName);
            modelData.setKey(modelKey);

            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        }catch (Exception e){
        }
    }


//    @RequestMapping(value = "/add", method = RequestMethod.GET)
//    @ApiOperation(value = "创建新模型")
////    @ModelAttribute ActModel actModel
//    public Result<Object> addModel() throws IOException {
//        ActModel actModel = new ActModel();
//        // 初始化一个空模型
//        Model model = repositoryService.newModel();
//
//        ObjectNode modelNode = objectMapper.createObjectNode();
//        modelNode.put(ModelDataJsonConstants.MODEL_NAME, actModel.getName());
//        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, actModel.getDescription());
//        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion());
//
//        model.setName("ce1");//actModel.getName()
//        model.setKey("ce2");//actModel.getModelKey()
//        model.setMetaInfo("ces");//modelNode.toString()
//
//        // 保存模型
//        repositoryService.saveModel(model);
//        String id = model.getId();
//
//        // 完善ModelEditorSource
//        ObjectNode editorNode = objectMapper.createObjectNode();
//        ObjectNode stencilSetNode = objectMapper.createObjectNode();
//        ObjectNode properties = objectMapper.createObjectNode();
//        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
//        editorNode.replace("stencilset", stencilSetNode);
//        properties.put("process_id", actModel.getModelKey());
//        editorNode.replace("properties", properties);
//        try {
//            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
//            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + id);
//        } catch (UnsupportedEncodingException e) {
//            log.error(e.toString());
//            return new ResultUtil<Object>().setErrorMsg("添加模型失败");
//        }
//        // 保存扩展模型至数据库
//        actModel.setId(id);
//        actModel.setVersion(model.getVersion());
//        actModelService.save(actModel);
//        return new ResultUtil<Object>().setSuccessMsg("添加模型成功");
//    }

}
