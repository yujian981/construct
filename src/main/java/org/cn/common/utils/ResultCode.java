package org.cn.common.utils;


public enum ResultCode {

    SUCCESS(200,"成功"),
    ERROR(404,"错误");

    private Integer code;
    private String  message;


    ResultCode(Integer code,String  message){
       this.code=code;
       this.message=message;
    }
    public Integer code(){ return this.code; }
    public String message(){
       return this.message;
    }


}
