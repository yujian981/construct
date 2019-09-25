package org.cn.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "t_act_form")
@TableName("t_act_form")
@ApiModel(value = "扩展表单信息")
public class ActContentEntity extends ActFormEntity {

    @ApiModelProperty("主键")
    private Integer ID_;
    @ApiModelProperty("新增内容")
    private String TEXT_;
    @ApiModelProperty("标记")
    private String PID_;



}
