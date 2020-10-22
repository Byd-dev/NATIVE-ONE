package com.pro.bityard.entity;

public class FollowerIncomeEntity {


    /**
     * volume : 1
     * follower : 2
     * code : 200
     * incomeDay : 0.0
     * holder : 1
     * incomeWeek : 0.00109989
     * currency : USDT
     * incomeAll : 1.87038693
     * message :
     * ratio : 0.08
     */

    private String volume;
    private String follower;
    private int code;
    private double incomeDay;
    private String holder;
    private double incomeWeek;
    private String currency;
    private double incomeAll;
    private String message;
    private double ratio;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getIncomeDay() {
        return incomeDay;
    }

    public void setIncomeDay(double incomeDay) {
        this.incomeDay = incomeDay;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public double getIncomeWeek() {
        return incomeWeek;
    }

    public void setIncomeWeek(double incomeWeek) {
        this.incomeWeek = incomeWeek;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(double incomeAll) {
        this.incomeAll = incomeAll;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
