package org.cn.modules.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Cty
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 157883924364585035L;

    private Long id;
    private String name;
    private Integer age;
    private String email;

}
