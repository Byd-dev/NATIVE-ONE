package com.pro.bityard.entity;

import java.util.List;


public class SpotHistoryEntity {


    /**
     * total : 1
     * code : 200
     * data : [{"amount":39.84,"brand":null,"buy":true,"charge":0,"chargeEagle":0,"chargeLucky":0,"commodity":"ZRXUSDT_CC","commodityName":"ZRXUSDT","createTime":1609730421000,"desCurrency":"ZRX","id":"552812711458406400","identity":"MqD9LN47lma4hEBw","message":null,"opAmount":0,"opPrice":0,"opVolume":0,"orAmount":39.84,"orVolume":100,"platform":"Android","price":0.3984,"server":null,"srcCurrency":"USDT","status":6,"type":0,"unionMoney":null,"unionRatio":null,"unionUser":null,"updateTime":1609730422000,"userId":"501120432234446848","username":"交割合约不递延01","version":"0","volume":100}]
     * size : 10
     * page : 0
     * message :
     */



    private String total;
    private Integer code;
    private Integer size;
    private Integer page;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "SpotHistoryEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

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

        /**
         * amount : 39.84
         * brand : null
         * buy : true
         * charge : 0.0
         * chargeEagle : 0.0
         * chargeLucky : 0.0
         * commodity : ZRXUSDT_CC
         * commodityName : ZRXUSDT
         * createTime : 1609730421000
         * desCurrency : ZRX
         * id : 552812711458406400
         * identity : MqD9LN47lma4hEBw
         * message : null
         * opAmount : 0.0
         * opPrice : 0.0
         * opVolume : 0.0
         * orAmount : 39.84
         * orVolume : 100.0
         * platform : Android
         * price : 0.3984
         * server : null
         * srcCurrency : USDT
         * status : 6
         * type : 0
         * unionMoney : null
         * unionRatio : null
         * unionUser : null
         * updateTime : 1609730422000
         * userId : 501120432234446848
         * username : 交割合约不递延01
         * version : 0
         * volume : 100.0
         */



        private Double amount;
        private Object brand;
        private Boolean buy;
        private Double charge;
        private Double chargeEagle;
        private Double chargeLucky;
        private String commodity;
        private String commodityName;
        private Long createTime;
        private String desCurrency;
        private String id;
        private String identity;
        private Object message;
        private Double opAmount;
        private Double opPrice;
        private Double opVolume;
        private Double orAmount;
        private Double orVolume;
        private String platform;
        private Double price;
        private Object server;
        private String srcCurrency;
        private Integer status;
        private Integer type;
        private Object unionMoney;
        private Object unionRatio;
        private Object unionUser;
        private Long updateTime;
        private String userId;
        private String username;
        private String version;
        private Double volume;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Object getBrand() {
            return brand;
        }

        public void setBrand(Object brand) {
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

        public Double getOrAmount() {
            return orAmount;
        }

        public void setOrAmount(Double orAmount) {
            this.orAmount = orAmount;
        }

        public Double getOrVolume() {
            return orVolume;
        }

        public void setOrVolume(Double orVolume) {
            this.orVolume = orVolume;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
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

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
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

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }
    }
}
