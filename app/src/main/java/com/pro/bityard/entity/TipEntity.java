package com.pro.bityard.entity;

public class TipEntity {

    /**
     * code : 200
     * message :
     * timeout : 600
     */

    private int code;
    private String message;
    private int timeout;

    @Override
    public String toString() {
        return "TipEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", timeout=" + timeout +
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
