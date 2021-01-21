package com.pro.bityard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class CurrencyDetailEntity {


    /**
     * code : 200
     * data : [{"chain":"OMNI","chainShow":"true","code":"BYD","currency":"USDT","id":"3","recharge":true,"rechargeDesc":"","rechargeMin":15,"sort":1,"transfer":true,"transferMax":1000,"transferMin":10,"valid":true,"withdraw":true,"withdrawDay":1500,"withdrawDesc":"","withdrawFee":6.63,"withdrawKyc":100000,"withdrawKyc0":1500,"withdrawKyc1":1,"withdrawKyc2":100000,"withdrawMax":30000,"withdrawMin":50,"withdrawTag":false},{"chain":"ERC20","chainShow":"true","code":"BYD","currency":"USDT","id":"613","recharge":true,"rechargeDesc":"","rechargeMin":15,"sort":1,"transfer":true,"transferMax":1500,"transferMin":10,"valid":true,"withdraw":true,"withdrawDay":1500,"withdrawDesc":"","withdrawFee":3,"withdrawKyc":100000,"withdrawKyc0":1500,"withdrawKyc1":0,"withdrawKyc2":100000,"withdrawMax":30000,"withdrawMin":50,"withdrawTag":false},{"chain":"TRC20","chainShow":"true","code":"BYD","currency":"USDT","id":"2","recharge":true,"rechargeDesc":"","rechargeMin":10,"sort":2,"transfer":true,"transferMax":1500,"transferMin":10,"valid":true,"withdraw":true,"withdrawDay":1500,"withdrawDesc":"","withdrawFee":0,"withdrawKyc":100000,"withdrawKyc0":1500,"withdrawKyc1":0,"withdrawKyc2":100000,"withdrawMax":1500,"withdrawMin":50,"withdrawTag":false}]
     * message :
     */

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<DataBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public static class DataBean implements Serializable {
        /**
         * chain : OMNI
         * chainShow : true
         * code : BYD
         * currency : USDT
         * id : 3
         * recharge : true
         * rechargeDesc :
         * rechargeMin : 15.0
         * sort : 1
         * transfer : true
         * transferMax : 1000.0
         * transferMin : 10.0
         * valid : true
         * withdraw : true
         * withdrawDay : 1500.0
         * withdrawDesc :
         * withdrawFee : 6.63
         * withdrawKyc : 100000.0
         * withdrawKyc0 : 1500.0
         * withdrawKyc1 : 1.0
         * withdrawKyc2 : 100000.0
         * withdrawMax : 30000.0
         * withdrawMin : 50.0
         * withdrawTag : false
         */

        @SerializedName("chain")
        private String chain;
        @SerializedName("chainShow")
        private String chainShow;
        @SerializedName("code")
        private String code;
        @SerializedName("currency")
        private String currency;
        @SerializedName("id")
        private String id;
        @SerializedName("recharge")
        private Boolean recharge;
        @SerializedName("rechargeDesc")
        private String rechargeDesc;
        @SerializedName("rechargeMin")
        private Double rechargeMin;
        @SerializedName("sort")
        private Integer sort;
        @SerializedName("transfer")
        private Boolean transfer;
        @SerializedName("transferMax")
        private Double transferMax;
        @SerializedName("transferMin")
        private Double transferMin;
        @SerializedName("valid")
        private Boolean valid;
        @SerializedName("withdraw")
        private Boolean withdraw;
        @SerializedName("withdrawDay")
        private Double withdrawDay;
        @SerializedName("withdrawDesc")
        private String withdrawDesc;
        @SerializedName("withdrawFee")
        private Double withdrawFee;
        @SerializedName("withdrawKyc")
        private Double withdrawKyc;
        @SerializedName("withdrawKyc0")
        private Double withdrawKyc0;
        @SerializedName("withdrawKyc1")
        private Double withdrawKyc1;
        @SerializedName("withdrawKyc2")
        private Double withdrawKyc2;
        @SerializedName("withdrawMax")
        private Double withdrawMax;
        @SerializedName("withdrawMin")
        private Double withdrawMin;
        @SerializedName("withdrawTag")
        private Boolean withdrawTag;

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getChainShow() {
            return chainShow;
        }

        public void setChainShow(String chainShow) {
            this.chainShow = chainShow;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean isRecharge() {
            return recharge;
        }

        public void setRecharge(Boolean recharge) {
            this.recharge = recharge;
        }

        public String getRechargeDesc() {
            return rechargeDesc;
        }

        public void setRechargeDesc(String rechargeDesc) {
            this.rechargeDesc = rechargeDesc;
        }

        public Double getRechargeMin() {
            return rechargeMin;
        }

        public void setRechargeMin(Double rechargeMin) {
            this.rechargeMin = rechargeMin;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public Boolean isTransfer() {
            return transfer;
        }

        public void setTransfer(Boolean transfer) {
            this.transfer = transfer;
        }

        public Double getTransferMax() {
            return transferMax;
        }

        public void setTransferMax(Double transferMax) {
            this.transferMax = transferMax;
        }

        public Double getTransferMin() {
            return transferMin;
        }

        public void setTransferMin(Double transferMin) {
            this.transferMin = transferMin;
        }

        public Boolean isValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

        public Boolean isWithdraw() {
            return withdraw;
        }

        public void setWithdraw(Boolean withdraw) {
            this.withdraw = withdraw;
        }

        public Double getWithdrawDay() {
            return withdrawDay;
        }

        public void setWithdrawDay(Double withdrawDay) {
            this.withdrawDay = withdrawDay;
        }

        public String getWithdrawDesc() {
            return withdrawDesc;
        }

        public void setWithdrawDesc(String withdrawDesc) {
            this.withdrawDesc = withdrawDesc;
        }

        public Double getWithdrawFee() {
            return withdrawFee;
        }

        public void setWithdrawFee(Double withdrawFee) {
            this.withdrawFee = withdrawFee;
        }

        public Double getWithdrawKyc() {
            return withdrawKyc;
        }

        public void setWithdrawKyc(Double withdrawKyc) {
            this.withdrawKyc = withdrawKyc;
        }

        public Double getWithdrawKyc0() {
            return withdrawKyc0;
        }

        public void setWithdrawKyc0(Double withdrawKyc0) {
            this.withdrawKyc0 = withdrawKyc0;
        }

        public Double getWithdrawKyc1() {
            return withdrawKyc1;
        }

        public void setWithdrawKyc1(Double withdrawKyc1) {
            this.withdrawKyc1 = withdrawKyc1;
        }

        public Double getWithdrawKyc2() {
            return withdrawKyc2;
        }

        public void setWithdrawKyc2(Double withdrawKyc2) {
            this.withdrawKyc2 = withdrawKyc2;
        }

        public Double getWithdrawMax() {
            return withdrawMax;
        }

        public void setWithdrawMax(Double withdrawMax) {
            this.withdrawMax = withdrawMax;
        }

        public Double getWithdrawMin() {
            return withdrawMin;
        }

        public void setWithdrawMin(Double withdrawMin) {
            this.withdrawMin = withdrawMin;
        }

        public Boolean isWithdrawTag() {
            return withdrawTag;
        }

        public void setWithdrawTag(Boolean withdrawTag) {
            this.withdrawTag = withdrawTag;
        }
    }
}
