package org.cn.modules.activiti.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "t_act_form")
@TableName("t_act_form")
@ApiModel(value = "扩展表单")
public class ActFormEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer ID_;

    @ApiModelProperty("创建用户")
    private String CREATE_BY_;

    @ApiModelProperty("创建时间")
    private Date CREATE_TIME_;

    @ApiModelProperty("流程ID")
    private String PROC_INST_ID_;

    @ApiModelProperty("标记")
    private String SING_;

    @ApiModelProperty("外键")
    private String PID_;

}
