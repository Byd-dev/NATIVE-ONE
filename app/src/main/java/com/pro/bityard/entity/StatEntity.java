package com.pro.bityard.entity;

public class StatEntity {


    @Override
    public String toString() {
        return "StatEntity{" +
                "code=" + code +
                ", sumMargin=" + sumMargin +
                ", sumIncome=" + sumIncome +
                ", currency='" + currency + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * code : 200
     * sumMargin : 50.0
     * sumIncome : -1.359104
     * currency : USDT
     * message :
     */



    private int code;
    private double sumMargin;
    private double sumIncome;
    private String currency;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getSumMargin() {
        return sumMargin;
    }

    public void setSumMargin(double sumMargin) {
        this.sumMargin = sumMargin;
    }

    public double getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(double sumIncome) {
        this.sumIncome = sumIncome;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
