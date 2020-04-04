package com.pro.bityard.entity;

public class OrderEntity {


    /**
     * code : 200
     * message : 买入委托已提交
     */

    private int code;
    private String message;

    @Override
    public String toString() {
        return "OrderEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
