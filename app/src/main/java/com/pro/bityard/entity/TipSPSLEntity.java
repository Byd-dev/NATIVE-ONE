package com.pro.bityard.entity;

public class TipSPSLEntity {


    /**
     * code : 200
     * message :
     */

    private int code;
    private String message;

    @Override
    public String toString() {
        return "TipSPSLEntity{" +
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
