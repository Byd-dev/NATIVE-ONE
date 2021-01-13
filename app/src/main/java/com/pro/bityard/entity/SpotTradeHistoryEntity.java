package com.pro.bityard.entity;

import java.util.List;


public class SpotTradeHistoryEntity {


    /**
     * total : 3
     * code : 200
     * data : [{"brand":"BYD","buy":false,"charge":0.04034016,"chargeEagle":0,"chargeLucky":0,"commodity":"ADAUSDT_CC","commodityName":"ADAUSDT","createTime":1610506189000,"dealWay":0,"desCurrency":"USDT","discount":0.96,"id":"556066518720086016","identity":"56626112","opAmount":14.007,"opPrice":0.28014,"opVolume":50,"orderId":"556066444699009024","srcCurrency":"ADA","tradeTime":1610506172000,"type":0,"unionMoney":0.0254143,"unionRatio":0,"unionUser":"551003597899710464","userId":"554356010455023616"},{"brand":"BYD","buy":true,"charge":0.0537,"chargeEagle":0,"chargeLucky":0,"commodity":"ADAUSDT_CC","commodityName":"ADAUSDT","createTime":1610505890000,"dealWay":1,"desCurrency":"ADA","discount":1,"id":"556065263511699456","identity":"56623711","opAmount":9.993928,"opPrice":0.27916,"opVolume":35.8,"orderId":"556062840005099520","srcCurrency":"USDT","tradeTime":1610505312000,"type":1,"unionMoney":0.033831,"unionRatio":0,"unionUser":"551003597899710464","userId":"554356010455023616"},{"brand":"BYD","buy":false,"charge":0.02007288,"chargeEagle":0,"chargeLucky":0,"commodity":"ADAUSDT_CC","commodityName":"ADAUSDT","createTime":1610505281000,"dealWay":1,"desCurrency":"USDT","discount":0.96,"id":"556062710858285056","identity":"56623599","opAmount":13.9395,"opPrice":0.27879,"opVolume":50,"orderId":"556062701211385856","srcCurrency":"ADA","tradeTime":1610505279000,"type":1,"unionMoney":0.01264591,"unionRatio":0,"unionUser":"551003597899710464","userId":"554356010455023616"}]
     * size : 10
     * page : 1
     * message :
     */

    private String total;
    private Integer code;
    private Integer size;
    private Integer page;
    private String message;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
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

    @Override
    public String toString() {
        return "SpotTradeHistoryEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "brand='" + brand + '\'' +
                    ", buy=" + buy +
                    ", charge=" + charge +
                    ", chargeEagle=" + chargeEagle +
                    ", chargeLucky=" + chargeLucky +
                    ", commodity='" + commodity + '\'' +
                    ", commodityName='" + commodityName + '\'' +
                    ", createTime=" + createTime +
                    ", dealWay=" + dealWay +
                    ", desCurrency='" + desCurrency + '\'' +
                    ", discount=" + discount +
                    ", id='" + id + '\'' +
                    ", identity='" + identity + '\'' +
                    ", opAmount=" + opAmount +
                    ", opPrice=" + opPrice +
                    ", opVolume=" + opVolume +
                    ", orderId='" + orderId + '\'' +
                    ", srcCurrency='" + srcCurrency + '\'' +
                    ", tradeTime=" + tradeTime +
                    ", type=" + type +
                    ", unionMoney=" + unionMoney +
                    ", unionRatio=" + unionRatio +
                    ", unionUser='" + unionUser + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Boolean getBuy() {
            return buy;
        }

        public void setBuy(Boolean buy) {
            this.buy = buy;
        }

        public Double getCharge() {
            return charge;
        }

        public void setCharge(Double charge) {
            this.charge = charge;
        }

        public Double getChargeEagle() {
            return chargeEagle;
        }

        public void setChargeEagle(Double chargeEagle) {
            this.chargeEagle = chargeEagle;
        }

        public Double getChargeLucky() {
            return chargeLucky;
        }

        public void setChargeLucky(Double chargeLucky) {
            this.chargeLucky = chargeLucky;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Integer getDealWay() {
            return dealWay;
        }

        public void setDealWay(Integer dealWay) {
            this.dealWay = dealWay;
        }

        public String getDesCurrency() {
            return desCurrency;
        }

        public void setDesCurrency(String desCurrency) {
            this.desCurrency = desCurrency;
        }

        public Double getDiscount() {
            return discount;
        }

        public void setDiscount(Double discount) {
            this.discount = discount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public Double getOpAmount() {
            return opAmount;
        }

        public void setOpAmount(Double opAmount) {
            this.opAmount = opAmount;
        }

        public Double getOpPrice() {
            return opPrice;
        }

        public void setOpPrice(Double opPrice) {
            this.opPrice = opPrice;
        }

        public Double getOpVolume() {
            return opVolume;
        }

        public void setOpVolume(Double opVolume) {
            this.opVolume = opVolume;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSrcCurrency() {
            return srcCurrency;
        }

        public void setSrcCurrency(String srcCurrency) {
            this.srcCurrency = srcCurrency;
        }

        public Long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(Long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Double getUnionMoney() {
            return unionMoney;
        }

        public void setUnionMoney(Double unionMoney) {
            this.unionMoney = unionMoney;
        }

        public Double getUnionRatio() {
            return unionRatio;
        }

        public void setUnionRatio(Double unionRatio) {
            this.unionRatio = unionRatio;
        }

        public String getUnionUser() {
            return unionUser;
        }

        public void setUnionUser(String unionUser) {
            this.unionUser = unionUser;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        /**
         * brand : BYD
         * buy : false
         * charge : 0.04034016
         * chargeEagle : 0.0
         * chargeLucky : 0.0
         * commodity : ADAUSDT_CC
         * commodityName : ADAUSDT
         * createTime : 1610506189000
         * dealWay : 0
         * desCurrency : USDT
         * discount : 0.96
         * id : 556066518720086016
         * identity : 56626112
         * opAmount : 14.007
         * opPrice : 0.28014
         * opVolume : 50.0
         * orderId : 556066444699009024
         * srcCurrency : ADA
         * tradeTime : 1610506172000
         * type : 0
         * unionMoney : 0.0254143
         * unionRatio : 0.0
         * unionUser : 551003597899710464
         * userId : 554356010455023616
         */





        private String brand;
        private Boolean buy;
        private Double charge;
        private Double chargeEagle;
        private Double chargeLucky;
        private String commodity;
        private String commodityName;
        private Long createTime;
        private Integer dealWay;
        private String desCurrency;
        private Double discount;
        private String id;
        private String identity;
        private Double opAmount;
        private Double opPrice;
        private Double opVolume;
        private String orderId;
        private String srcCurrency;
        private Long tradeTime;
        private Integer type;
        private Double unionMoney;
        private Double unionRatio;
        private String unionUser;
        private String userId;
    }
}
