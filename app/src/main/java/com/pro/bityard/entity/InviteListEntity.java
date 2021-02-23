package com.pro.bityard.entity;

import java.util.List;

public class InviteListEntity {


    @Override
    public String toString() {
        return "InviteListEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * total : 3
     * code : 200
     * data : [{"commission":39.24225,"commissionDay":0.007125,"currency":null,"loginTime":1588231988000,"registerRegion":"Malaysia","registerTime":1587990680000,"source":null,"tradeAmount":529700,"tradeAmountDay":100,"tradeCharge":784.845,"tradeChargeDay":0.1425,"tradeCost":784.845,"tradeCostDay":0.1425,"tradeCount":13,"tradeCountDay":1,"tradeVolume":184804.2515,"tradeVolumeDay":0.013,"userId":"461629630122557440","username":"大剋"},{"commission":0,"commissionDay":0,"currency":null,"loginTime":1586079648000,"registerRegion":"Malaysia","registerTime":1586079648000,"source":null,"tradeAmount":0,"tradeAmountDay":0,"tradeCharge":0,"tradeChargeDay":0,"tradeCost":0,"tradeCostDay":0,"tradeCount":0,"tradeCountDay":0,"tradeVolume":0,"tradeVolumeDay":0,"userId":"453614180260380672","username":"B0342290"},{"commission":0,"commissionDay":0,"currency":null,"loginTime":1587221131000,"registerRegion":"Malaysia","registerTime":1585230709000,"source":"9999","tradeAmount":0,"tradeAmountDay":0,"tradeCharge":0,"tradeChargeDay":0,"tradeCost":0,"tradeCostDay":0,"tradeCount":0,"tradeCountDay":0,"tradeVolume":0,"tradeVolumeDay":0,"userId":"450053469684891648","username":"B8914860"}]
     * size : 10
     * page : 1
     * message :
     */



    private String total;
    private int code;
    private int size;
    private int page;
    private String message;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
                    "commission=" + commission +
                    ", commissionDay=" + commissionDay +
                    ", currency=" + currency +
                    ", loginTime=" + loginTime +
                    ", registerRegion='" + registerRegion + '\'' +
                    ", registerTime=" + registerTime +
                    ", source=" + source +
                    ", tradeAmount=" + tradeAmount +
                    ", tradeAmountDay=" + tradeAmountDay +
                    ", tradeCharge=" + tradeCharge +
                    ", tradeChargeDay=" + tradeChargeDay +
                    ", tradeCost=" + tradeCost +
                    ", tradeCostDay=" + tradeCostDay +
                    ", tradeCount=" + tradeCount +
                    ", tradeCountDay=" + tradeCountDay +
                    ", tradeVolume=" + tradeVolume +
                    ", tradeVolumeDay=" + tradeVolumeDay +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }

        /**
         * commission : 39.24225
         * commissionDay : 0.007125
         * currency : null
         * loginTime : 1588231988000
         * registerRegion : Malaysia
         * registerTime : 1587990680000
         * source : null
         * tradeAmount : 529700
         * tradeAmountDay : 100
         * tradeCharge : 784.845
         * tradeChargeDay : 0.1425
         * tradeCost : 784.845
         * tradeCostDay : 0.1425
         * tradeCount : 13
         * tradeCountDay : 1
         * tradeVolume : 184804.2515
         * tradeVolumeDay : 0.013
         * userId : 461629630122557440
         * username : 大剋
         */



        private double commission;
        private double commissionDay;
        private String currency;
        private long loginTime;
        private String registerRegion;
        private long registerTime;
        private Object source;
        private double tradeAmount;
        private int tradeAmountDay;
        private double tradeCharge;
        private double tradeChargeDay;
        private double tradeCost;
        private double tradeCostDay;
        private int tradeCount;
        private int tradeCountDay;
        private double tradeVolume;
        private double tradeVolumeDay;
        private String userId;
        private String username;

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public double getCommissionDay() {
            return commissionDay;
        }

        public void setCommissionDay(double commissionDay) {
            this.commissionDay = commissionDay;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(long loginTime) {
            this.loginTime = loginTime;
        }

        public String getRegisterRegion() {
            return registerRegion;
        }

        public void setRegisterRegion(String registerRegion) {
            this.registerRegion = registerRegion;
        }

        public long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
        }

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public double getTradeAmount() {
            return tradeAmount;
        }

        public void setTradeAmount(double tradeAmount) {
            this.tradeAmount = tradeAmount;
        }

        public int getTradeAmountDay() {
            return tradeAmountDay;
        }

        public void setTradeAmountDay(int tradeAmountDay) {
            this.tradeAmountDay = tradeAmountDay;
        }

        public double getTradeCharge() {
            return tradeCharge;
        }

        public void setTradeCharge(double tradeCharge) {
            this.tradeCharge = tradeCharge;
        }

        public double getTradeChargeDay() {
            return tradeChargeDay;
        }

        public void setTradeChargeDay(double tradeChargeDay) {
            this.tradeChargeDay = tradeChargeDay;
        }

        public double getTradeCost() {
            return tradeCost;
        }

        public void setTradeCost(double tradeCost) {
            this.tradeCost = tradeCost;
        }

        public double getTradeCostDay() {
            return tradeCostDay;
        }

        public void setTradeCostDay(double tradeCostDay) {
            this.tradeCostDay = tradeCostDay;
        }

        public int getTradeCount() {
            return tradeCount;
        }

        public void setTradeCount(int tradeCount) {
            this.tradeCount = tradeCount;
        }

        public int getTradeCountDay() {
            return tradeCountDay;
        }

        public void setTradeCountDay(int tradeCountDay) {
            this.tradeCountDay = tradeCountDay;
        }

        public double getTradeVolume() {
            return tradeVolume;
        }

        public void setTradeVolume(double tradeVolume) {
            this.tradeVolume = tradeVolume;
        }

        public double getTradeVolumeDay() {
            return tradeVolumeDay;
        }

        public void setTradeVolumeDay(double tradeVolumeDay) {
            this.tradeVolumeDay = tradeVolumeDay;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
