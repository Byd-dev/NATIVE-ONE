package com.pro.bityard.entity;

import java.util.List;

public class SpotPositionEntity {


    @Override
    public String toString() {
        return "SpotPositionEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    private int code;
    private String message;


    private List<DataBean> data;

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
                    "amount=" + amount +
                    ", brand=" + brand +
                    ", buy=" + buy +
                    ", charge=" + charge +
                    ", chargeEagle=" + chargeEagle +
                    ", chargeLucky=" + chargeLucky +
                    ", commodity='" + commodity + '\'' +
                    ", commodityName='" + commodityName + '\'' +
                    ", createTime=" + createTime +
                    ", desCurrency='" + desCurrency + '\'' +
                    ", id='" + id + '\'' +
                    ", identity='" + identity + '\'' +
                    ", message=" + message +
                    ", opAmount=" + opAmount +
                    ", opPrice=" + opPrice +
                    ", opVolume=" + opVolume +
                    ", orAmount=" + orAmount +
                    ", orVolume=" + orVolume +
                    ", platform='" + platform + '\'' +
                    ", price=" + price +
                    ", server=" + server +
                    ", srcCurrency='" + srcCurrency + '\'' +
                    ", status=" + status +
                    ", type=" + type +
                    ", unionMoney=" + unionMoney +
                    ", unionRatio=" + unionRatio +
                    ", unionUser=" + unionUser +
                    ", updateTime=" + updateTime +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", version='" + version + '\'' +
                    ", volume=" + volume +
                    '}';
        }

        private double amount;
        private Object brand;
        private boolean buy;
        private double charge;
        private double chargeEagle;
        private double chargeLucky;
        private String commodity;
        private String commodityName;
        private long createTime;
        private String desCurrency;
        private String id;
        private String identity;
        private Object message;
        private double opAmount;
        private double opPrice;
        private double opVolume;
        private double orAmount;
        private double orVolume;
        private String platform;
        private double price;
        private Object server;
        private String srcCurrency;
        private int status;
        private int type;
        private Object unionMoney;
        private Object unionRatio;
        private Object unionUser;
        private long updateTime;
        private String userId;
        private String username;
        private String version;
        private double volume;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Object getBrand() {
            return brand;
        }

        public void setBrand(Object brand) {
            this.brand = brand;
        }

        public boolean isBuy() {
            return buy;
        }

        public void setBuy(boolean buy) {
            this.buy = buy;
        }

        public double getCharge() {
            return charge;
        }

        public void setCharge(double charge) {
            this.charge = charge;
        }

        public double getChargeEagle() {
            return chargeEagle;
        }

        public void setChargeEagle(double chargeEagle) {
            this.chargeEagle = chargeEagle;
        }

        public double getChargeLucky() {
            return chargeLucky;
        }

        public void setChargeLucky(double chargeLucky) {
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDesCurrency() {
            return desCurrency;
        }

        public void setDesCurrency(String desCurrency) {
            this.desCurrency = desCurrency;
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

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public double getOpAmount() {
            return opAmount;
        }

        public void setOpAmount(double opAmount) {
            this.opAmount = opAmount;
        }

        public double getOpPrice() {
            return opPrice;
        }

        public void setOpPrice(double opPrice) {
            this.opPrice = opPrice;
        }

        public double getOpVolume() {
            return opVolume;
        }

        public void setOpVolume(double opVolume) {
            this.opVolume = opVolume;
        }

        public double getOrAmount() {
            return orAmount;
        }

        public void setOrAmount(double orAmount) {
            this.orAmount = orAmount;
        }

        public double getOrVolume() {
            return orVolume;
        }

        public void setOrVolume(double orVolume) {
            this.orVolume = orVolume;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Object getServer() {
            return server;
        }

        public void setServer(Object server) {
            this.server = server;
        }

        public String getSrcCurrency() {
            return srcCurrency;
        }

        public void setSrcCurrency(String srcCurrency) {
            this.srcCurrency = srcCurrency;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getUnionMoney() {
            return unionMoney;
        }

        public void setUnionMoney(Object unionMoney) {
            this.unionMoney = unionMoney;
        }

        public Object getUnionRatio() {
            return unionRatio;
        }

        public void setUnionRatio(Object unionRatio) {
            this.unionRatio = unionRatio;
        }

        public Object getUnionUser() {
            return unionUser;
        }

        public void setUnionUser(Object unionUser) {
            this.unionUser = unionUser;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
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

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }
    }
}
