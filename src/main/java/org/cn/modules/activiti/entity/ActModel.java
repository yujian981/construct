package org.cn.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.cn.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author Cty
 */
@Data
@Entity
@Table(name = "t_act_model")
@TableName("t_act_model")
@ApiModel(value = "模型")
public class ActModel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模型名称")
    private String name;

    @ApiModelProperty(value = "标识")
    private String modelKey;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "描述/备注")
    private String description;
}