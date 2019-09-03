package org.cn.modules.activiti.controller;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.cn.common.constant.Result;
import org.cn.common.exception.PublicException;
import org.cn.common.utils.ResultUtil;
import org.cn.modules.activiti.service.ActModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author Cty
 */
@Slf4j
@RestController
@Api(description = "模型管理接口")
@RequestMapping("/actModel")
@Transactional
public class ActModelController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ActModelService actModelService;

    @ApiOperation("创建新模型(在线)")
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

    @RequestMapping(value = "/deploy/{id}",method = RequestMethod.GET)
    @ApiOperation("部署发布模型")
    public Result<Object> deploy(@PathVariable String id){

        //获取模型
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
        if (bytes == null){
            return new ResultUtil<Object>().setErrorMsg("模型数据为空,请先成功设计流程图并保存");
        }

        try {
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if(model.getProcesses().size()==0){
                return new ResultUtil<Object>().setErrorMsg("模型不符要求，请至少设计一条主线流程");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            // 部署发布模型流程
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();

            // 设置流程分类 保存扩展流程至数据库
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
//            ActModel actModel = actModelService.get(id);
            System.out.println(list);

        } catch (IOException e) {
            log.error(e.toString());
            return new ResultUtil<Object>().setErrorMsg("部署失败");
        }
        return new ResultUtil<Object>().setSuccessMsg("部署成功");
    }


    @RequestMapping(value = "/deployByFile", method = RequestMethod.POST)
    @ApiOperation(value = "通过文件部署模型")
    public Result<Object> deployByFile(@RequestParam MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (StrUtil.isBlank(fileName)) {
            return new ResultUtil<Object>().setErrorMsg("请先选择文件");
        }
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment;
            String extension = FilenameUtils.getExtension(fileName);
            String baseName = FilenameUtils.getBaseName(fileName);
            if ("zip".equals(extension) || "bar".equals(extension)) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().name(baseName)
                        .addZipInputStream(zip).deploy();
            } else if ("png".equals(extension)) {
                deployment = repositoryService.createDeployment().name(baseName)
                        .addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.indexOf("bpmn20.xml") != -1) {
                deployment = repositoryService.createDeployment().name(baseName)
                        .addInputStream(fileName, fileInputStream).deploy();
            } else if ("bpmn".equals(extension)) {
                deployment = repositoryService.createDeployment().name(baseName)
                        .addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                return new ResultUtil<Object>().setErrorMsg("不支持的文件格式");
            }

            // 保存扩展流程至数据库
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

        } catch (Exception e) {
            log.error(e.toString());
            return new ResultUtil<Object>().setErrorMsg("部署失败");
        }

        return new ResultUtil<Object>().setSuccessMsg("部署成功");
    }


    @RequestMapping(value = "/export/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "导出模型XML")
    public void export(@PathVariable String id, HttpServletResponse response) {

        try {
            Model modelData = repositoryService.getModel(id);
            // 获取节点信息
            byte[] nodeBytes = repositoryService.getModelEditorSource(modelData.getId());
            if (nodeBytes == null) {
                throw new PublicException("导出失败，模型数据为空");
            }
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(nodeBytes);
            // 将节点信息转换为xml
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);

            String filename = modelData.getName() + ".bpmn20.xml";
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));

            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e){
            log.error(e.toString());
            throw new PublicException("导出模型出错");
        }
    }


    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "通过id批量删除")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id :ids){
            repositoryService.deleteModel(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("删除成功");
    }

}
