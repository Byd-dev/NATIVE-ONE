package com.pro.bityard.entity;

import java.util.List;

public class MarginHistoryEntity {


    /**
     * code : 200
     * data : [{"bettingId":"468384337126375424","brand":"BYD","createTime":1589601128000,"id":"468384337134764032","lever":100,"margin":5,"stopLoss":-2.5,"stopProfit":25},{"bettingId":"468384337126375424","brand":"BYD","createTime":1589601440000,"id":"468385644218712064","lever":99.8,"margin":0.01,"stopLoss":null,"stopProfit":null},{"bettingId":"468384337126375424","brand":"BYD","createTime":1589601447000,"id":"468385672492515328","lever":99.4,"margin":0.02,"stopLoss":null,"stopProfit":null},{"bettingId":"468384337126375424","brand":"BYD","createTime":1589601450000,"id":"468385687222501376","lever":99.01,"margin":0.02,"stopLoss":null,"stopProfit":null},{"bettingId":"468384337126375424","brand":"BYD","createTime":1589613045000,"id":"468434319229878272","lever":98.62,"margin":0.02,"stopLoss":null,"stopProfit":null}]
     * message :
     */

    private int code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "MarginHistoryEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "bettingId='" + bettingId + '\'' +
                    ", brand='" + brand + '\'' +
                    ", createTime=" + createTime +
                    ", id='" + id + '\'' +
                    ", lever=" + lever +
                    ", margin=" + margin +
                    ", stopLoss=" + stopLoss +
                    ", stopProfit=" + stopProfit +
                    '}';
        }

        /**
         * bettingId : 468384337126375424
         * brand : BYD
         * createTime : 1589601128000
         * id : 468384337134764032
         * lever : 100.0
         * margin : 5.0
         * stopLoss : -2.5
         * stopProfit : 25.0
         */



        private String bettingId;
        private String brand;
        private long createTime;
        private String id;
        private double lever;
        private double margin;
        private double stopLoss;
        private double stopProfit;

        public String getBettingId() {
            return bettingId;
        }

        public void setBettingId(String bettingId) {
            this.bettingId = bettingId;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getLever() {
            return lever;
        }

        public void setLever(double lever) {
            this.lever = lever;
        }

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public double getStopLoss() {
            return stopLoss;
        }

        public void setStopLoss(double stopLoss) {
            this.stopLoss = stopLoss;
        }

        public double getStopProfit() {
            return stopProfit;
        }

        public void setStopProfit(double stopProfit) {
            this.stopProfit = stopProfit;
        }
    }
}
