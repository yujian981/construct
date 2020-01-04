package org.cn.common.utils;

import lombok.Data;

import javax.crypto.interfaces.PBEKey;
import java.io.Serializable;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private Object data;


    //返回成功
    public static Result success(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code());
        result.setMessage(ResultCode.SUCCESS.message());
        return result;
    }
    //返回成功
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.code());
        result.setMessage(ResultCode.SUCCESS.message());
        result.setData(data);
        return result;
    }

    //返回失败
    public static Result failure(ResultCode resultCode){
        Result result = new Result();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message());
        return result;
    }
    //返回失败
    public static Result failure(ResultCode resultCode,Object data){
        Result result = new Result();
        result.setCode(resultCode.code());
        result.setMessage(resultCode.message());
        result.setData(data);
        return result;
    }
}

