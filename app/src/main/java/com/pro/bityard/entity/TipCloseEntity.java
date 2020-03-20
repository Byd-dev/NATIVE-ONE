package com.pro.bityard.entity;

public class TipCloseEntity {

    /**
     * code : 200
     * successNumber : 1
     * failNumber : 0
     * message : 卖出委托已提交
     */

    private int code;
    private int successNumber;
    private int failNumber;
    private String message;

    @Override
    public String toString() {
        return "TipCloseEntity{" +
                "code=" + code +
                ", successNumber=" + successNumber +
                ", failNumber=" + failNumber +
                ", message='" + message + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(int successNumber) {
        this.successNumber = successNumber;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
