package com.pro.bityard.entity;

import java.util.List;

public class UserAssetEntity {


    /**
     * code : 200
     * data : {"brand":"BYD","comm":0,"commApply":0,"commExchange":0,"currency":"USDT","dataMD5":"f227355c082a568ae2966abf8997863d","discount":"","eagle":0,"fundIn":100100,"fundOut":502,"game":86885.48026523,"lucky":0,"modify":false,"modifyFields":[],"money":29735.36839241,"prize":0,"signString":"422134795078729728`29,735.3683`0.0000`0.0000","userId":"422134795078729728"}
     * message :
     */

    private int code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "UserAssetEntity{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "brand='" + brand + '\'' +
                    ", comm=" + comm +
                    ", commApply=" + commApply +
                    ", commExchange=" + commExchange +
                    ", currency='" + currency + '\'' +
                    ", dataMD5='" + dataMD5 + '\'' +
                    ", discount='" + discount + '\'' +
                    ", eagle=" + eagle +
                    ", fundIn=" + fundIn +
                    ", fundOut=" + fundOut +
                    ", game=" + game +
                    ", lucky=" + lucky +
                    ", modify=" + modify +
                    ", money=" + money +
                    ", prize=" + prize +
                    ", signString='" + signString + '\'' +
                    ", userId='" + userId + '\'' +
                    ", modifyFields=" + modifyFields +
                    '}';
        }

        /**
         * brand : BYD
         * comm : 0.0
         * commApply : 0.0
         * commExchange : 0.0
         * currency : USDT
         * dataMD5 : f227355c082a568ae2966abf8997863d
         * discount :
         * eagle : 0.0
         * fundIn : 100100.0
         * fundOut : 502.0
         * game : 86885.48026523
         * lucky : 0.0
         * modify : false
         * modifyFields : []
         * money : 29735.36839241
         * prize : 0.0
         * signString : 422134795078729728`29,735.3683`0.0000`0.0000
         * userId : 422134795078729728
         */



        private String brand;
        private double comm;
        private double commApply;
        private double commExchange;
        private String currency;
        private String dataMD5;
        private String discount;
        private double eagle;
        private double fundIn;
        private double fundOut;
        private double game;
        private double lucky;
        private boolean modify;
        private double money;
        private double prize;
        private String signString;
        private String userId;
        private List<Object> modifyFields;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public double getComm() {
            return comm;
        }

        public void setComm(double comm) {
            this.comm = comm;
        }

        public double getCommApply() {
            return commApply;
        }

        public void setCommApply(double commApply) {
            this.commApply = commApply;
        }

        public double getCommExchange() {
            return commExchange;
        }

        public void setCommExchange(double commExchange) {
            this.commExchange = commExchange;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDataMD5() {
            return dataMD5;
        }

        public void setDataMD5(String dataMD5) {
            this.dataMD5 = dataMD5;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public double getEagle() {
            return eagle;
        }

        public void setEagle(double eagle) {
            this.eagle = eagle;
        }

        public double getFundIn() {
            return fundIn;
        }

        public void setFundIn(double fundIn) {
            this.fundIn = fundIn;
        }

        public double getFundOut() {
            return fundOut;
        }

        public void setFundOut(double fundOut) {
            this.fundOut = fundOut;
        }

        public double getGame() {
            return game;
        }

        public void setGame(double game) {
            this.game = game;
        }

        public double getLucky() {
            return lucky;
        }

        public void setLucky(double lucky) {
            this.lucky = lucky;
        }

        public boolean isModify() {
            return modify;
        }

        public void setModify(boolean modify) {
            this.modify = modify;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getPrize() {
            return prize;
        }

        public void setPrize(double prize) {
            this.prize = prize;
        }

        public String getSignString() {
            return signString;
        }

        public void setSignString(String signString) {
            this.signString = signString;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<Object> getModifyFields() {
            return modifyFields;
        }

        public void setModifyFields(List<Object> modifyFields) {
            this.modifyFields = modifyFields;
        }
    }
}
