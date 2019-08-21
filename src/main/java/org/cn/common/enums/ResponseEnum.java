package org.cn.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResponseEnum {

    // 系统模块
    SUCCESS(0, "操作成功"),
    ERROR(1, "操作失败"),
    SERVER_ERROR(500, "服务器异常"),

    // 通用模块 1xxxx
    ILLEGAL_ARGUMENT(10000, "参数不合法"),
    REPETITIVE_OPERATION(10001, "请勿重复操作"),
    ACCESS_LIMIT(10002, "请求太频繁, 请稍后再试"),
    MAIL_SEND_SUCCESS(10003, "邮件发送成功"),

    // 用户模块 2xxxx
    NEED_LOGIN(20001, "登录失效"),
    USERNAME_OR_PASSWORD_EMPTY(20002, "用户名或密码不能为空"),
    USERNAME_OR_PASSWORD_WRONG(20003, "用户名或密码错误"),
    USER_NOT_EXISTS(20004, "用户不存在"),
    WRONG_PASSWORD(20005, "密码错误"),
    DOCUMENT_IS_OK(2001, "部署成功！"),
    DOCUMENT_FILE_YEAS(1, "文件上传成功！");

    private int code;

    private String msg;

    private ResponseEnum() {

    }

    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", this.getCode());
        map.put("msg", this.getMsg());
        return map;
    }

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
