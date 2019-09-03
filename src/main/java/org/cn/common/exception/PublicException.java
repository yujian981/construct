package org.cn.common.exception;

import lombok.Data;

/**
 * @author Cty
 */
@Data
public class PublicException extends RuntimeException {

    private String msg;

    public PublicException(String msg){
        super(msg);
        this.msg = msg;
    }

}
