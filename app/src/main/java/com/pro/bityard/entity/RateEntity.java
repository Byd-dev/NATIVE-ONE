package com.pro.bityard.entity;

public class RateEntity {


    /**
     * code : 200
     * rate : 129.37
     * message :
     */

    private int code;
    private double rate;
    private String message;

    @Override
    public String toString() {
        return "RateEntity{" +
                "code=" + code +
                ", rate=" + rate +
                ", message='" + message + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
