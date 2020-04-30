package com.pro.bityard.entity;

public class InviteEntity {


    /**
     * code : 200
     * data : {"commission":39.24225,"currency":"USDT","subCount":3,"subCountNew":0,"subTrade":1,"tradeAmount":529700,"tradeCount":13}
     * message :
     */

    private int code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "InviteEntity{" +
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
                    "commission=" + commission +
                    ", currency='" + currency + '\'' +
                    ", subCount=" + subCount +
                    ", subCountNew=" + subCountNew +
                    ", subTrade=" + subTrade +
                    ", tradeAmount=" + tradeAmount +
                    ", tradeCount=" + tradeCount +
                    '}';
        }

        /**
         * commission : 39.24225
         * currency : USDT
         * subCount : 3
         * subCountNew : 0
         * subTrade : 1
         * tradeAmount : 529700
         * tradeCount : 13
         */



        private double commission;
        private String currency;
        private int subCount;
        private int subCountNew;
        private int subTrade;
        private int tradeAmount;
        private int tradeCount;

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getSubCount() {
            return subCount;
        }

        public void setSubCount(int subCount) {
            this.subCount = subCount;
        }

        public int getSubCountNew() {
            return subCountNew;
        }

        public void setSubCountNew(int subCountNew) {
            this.subCountNew = subCountNew;
        }

        public int getSubTrade() {
            return subTrade;
        }

        public void setSubTrade(int subTrade) {
            this.subTrade = subTrade;
        }

        public int getTradeAmount() {
            return tradeAmount;
        }

        public void setTradeAmount(int tradeAmount) {
            this.tradeAmount = tradeAmount;
        }

        public int getTradeCount() {
            return tradeCount;
        }

        public void setTradeCount(int tradeCount) {
            this.tradeCount = tradeCount;
        }
    }
}
