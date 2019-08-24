package org.cn.common.utils;

import org.cn.common.enums.ResponseEnum;

import java.io.Serializable;
/**
 * @Author Cty
 * @Description //TODO 封装json数据 统一返回
 **/
public class ServerResponse implements Serializable {

    private static final long serialVersionUID = 8095809201605680157L;
    private int code;

    private String msg;

    private Object data;

    public static ServerResponse success() {
        return new ServerResponse(200, "ok");
    }

    public static ServerResponse success(Object data) {
        return new ServerResponse(200, "ok", data);
    }

    public static ServerResponse success(ResponseEnum responseEnum, Object data) {
        return new ServerResponse(responseEnum.getCode(), responseEnum.getMsg(), data);
    }

    public static ServerResponse error() {
        return new ServerResponse(-1, "error");
    }

    public static ServerResponse error(int code, String msg) {
        return new ServerResponse(code, msg);
    }

    public static ServerResponse error(ResponseEnum responseEnum) {
        return new ServerResponse(responseEnum.getCode(), responseEnum.getMsg());
    }


    private ServerResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
