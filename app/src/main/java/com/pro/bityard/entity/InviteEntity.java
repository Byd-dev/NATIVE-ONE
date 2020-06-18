package com.pro.bityard.entity;

public class InviteEntity {


    /**
     * code : 200
     * data : {"commission":550.176915,"currency":"USDT","salaryAll":15,"salaryDay":0,"subCount":16,"subCountNew":0,"subTrade":13,"tradeAmount":6122920,"tradeCount":50695}
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
                    ", salaryAll=" + salaryAll +
                    ", salaryDay=" + salaryDay +
                    ", subCount=" + subCount +
                    ", subCountNew=" + subCountNew +
                    ", subTrade=" + subTrade +
                    ", tradeAmount=" + tradeAmount +
                    ", tradeCount=" + tradeCount +
                    '}';
        }

        /**
         * commission : 550.176915
         * currency : USDT
         * salaryAll : 15.0
         * salaryDay : 0
         * subCount : 16
         * subCountNew : 0
         * subTrade : 13
         * tradeAmount : 6122920.0
         * tradeCount : 50695
         */



        private double commission;
        private String currency;
        private double salaryAll;
        private int salaryDay;
        private int subCount;
        private int subCountNew;
        private int subTrade;
        private double tradeAmount;
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

        public double getSalaryAll() {
            return salaryAll;
        }

        public void setSalaryAll(double salaryAll) {
            this.salaryAll = salaryAll;
        }

        public int getSalaryDay() {
            return salaryDay;
        }

        public void setSalaryDay(int salaryDay) {
            this.salaryDay = salaryDay;
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

        public double getTradeAmount() {
            return tradeAmount;
        }

        public void setTradeAmount(double tradeAmount) {
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
