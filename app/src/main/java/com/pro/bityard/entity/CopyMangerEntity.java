package com.pro.bityard.entity;

import java.util.List;

public class CopyMangerEntity {


    /**
     * total : 3
     * code : 200
     * data : [{"active":true,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200923150130_151.jpg?Expires=1916204490&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=P6Byg%2BueJ%2FACUeuJCnE47UMvO%2BU%3D","brand":null,"createTime":1601889471000,"currency":"USDT","followVal":5,"followWay":1,"id":"519925383215595520","lastCode":null,"lastData":null,"lastTime":null,"maxDay":10000,"maxHold":10000,"slRatio":-1,"sumIncome":-1.6901786,"sumMargin":30,"traderAvatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200930094654_374.jpg?Expires=1916790414&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=DRM0%2B3Gj%2F6WywITZMmPjuMmh6Lw%3D","traderId":"517989308389801984","traderName":"暴躁的火女","traderType":"2","updateTime":1601934938000,"userId":"475609350069960704","username":"揍谁"},{"active":true,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200923150130_151.jpg?Expires=1916204490&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=P6Byg%2BueJ%2FACUeuJCnE47UMvO%2BU%3D","brand":null,"createTime":1601631121000,"currency":"USDT","followVal":5,"followWay":1,"id":"518841785498615808","lastCode":null,"lastData":null,"lastTime":null,"maxDay":10000,"maxHold":10000,"slRatio":-1,"sumIncome":0,"sumMargin":0,"traderAvatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200828154005_432.jpg?Expires=1913960405&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=tAE0%2FeJ1wgO1lpltq1%2FQxP4qCV8%3D","traderId":"452096359483310080","traderName":"Apple","traderType":"2","updateTime":null,"userId":"475609350069960704","username":"揍谁"},{"active":true,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200923150130_151.jpg?Expires=1916204490&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=P6Byg%2BueJ%2FACUeuJCnE47UMvO%2BU%3D","brand":null,"createTime":1600843940000,"currency":"USDT","followVal":5,"followWay":1,"id":"515540108444057600","lastCode":"","lastData":"","lastTime":null,"maxDay":10000,"maxHold":10000,"slRatio":60,"sumIncome":-1.359104,"sumMargin":50,"traderAvatar":"","traderId":"487947864136630272","traderName":"带单人02","traderType":"2","updateTime":1601890919000,"userId":"475609350069960704","username":"揍谁"}]
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

    @Override
    public String toString() {
        return "CopyMangerEntity{" +
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
                    "active=" + active +
                    ", avatar='" + avatar + '\'' +
                    ", brand=" + brand +
                    ", createTime=" + createTime +
                    ", currency='" + currency + '\'' +
                    ", followVal=" + followVal +
                    ", followWay=" + followWay +
                    ", id='" + id + '\'' +
                    ", lastCode=" + lastCode +
                    ", lastData=" + lastData +
                    ", lastTime=" + lastTime +
                    ", maxDay=" + maxDay +
                    ", maxHold=" + maxHold +
                    ", slRatio=" + slRatio +
                    ", sumIncome=" + sumIncome +
                    ", sumMargin=" + sumMargin +
                    ", traderAvatar='" + traderAvatar + '\'' +
                    ", traderId='" + traderId + '\'' +
                    ", traderName='" + traderName + '\'' +
                    ", traderType='" + traderType + '\'' +
                    ", updateTime=" + updateTime +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }

        /**
         * active : true
         * avatar : http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200923150130_151.jpg?Expires=1916204490&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=P6Byg%2BueJ%2FACUeuJCnE47UMvO%2BU%3D
         * brand : null
         * createTime : 1601889471000
         * currency : USDT
         * followVal : 5.0
         * followWay : 1
         * id : 519925383215595520
         * lastCode : null
         * lastData : null
         * lastTime : null
         * maxDay : 10000.0
         * maxHold : 10000.0
         * slRatio : -1.0
         * sumIncome : -1.6901786
         * sumMargin : 30.0
         * traderAvatar : http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200930094654_374.jpg?Expires=1916790414&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=DRM0%2B3Gj%2F6WywITZMmPjuMmh6Lw%3D
         * traderId : 517989308389801984
         * traderName : 暴躁的火女
         * traderType : 2
         * updateTime : 1601934938000
         * userId : 475609350069960704
         * username : 揍谁
         */



        private boolean active;
        private String avatar;
        private Object brand;
        private long createTime;
        private String currency;
        private double followVal;
        private int followWay;
        private String id;
        private Object lastCode;
        private Object lastData;
        private Object lastTime;
        private double maxDay;
        private double maxHold;
        private double slRatio;
        private double sumIncome;
        private double sumMargin;
        private String traderAvatar;
        private String traderId;
        private String traderName;
        private String traderType;
        private long updateTime;
        private String userId;
        private String username;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Object getBrand() {
            return brand;
        }

        public void setBrand(Object brand) {
            this.brand = brand;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getFollowVal() {
            return followVal;
        }

        public void setFollowVal(double followVal) {
            this.followVal = followVal;
        }

        public int getFollowWay() {
            return followWay;
        }

        public void setFollowWay(int followWay) {
            this.followWay = followWay;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getLastCode() {
            return lastCode;
        }

        public void setLastCode(Object lastCode) {
            this.lastCode = lastCode;
        }

        public Object getLastData() {
            return lastData;
        }

        public void setLastData(Object lastData) {
            this.lastData = lastData;
        }

        public Object getLastTime() {
            return lastTime;
        }

        public void setLastTime(Object lastTime) {
            this.lastTime = lastTime;
        }

        public double getMaxDay() {
            return maxDay;
        }

        public void setMaxDay(double maxDay) {
            this.maxDay = maxDay;
        }

        public double getMaxHold() {
            return maxHold;
        }

        public void setMaxHold(double maxHold) {
            this.maxHold = maxHold;
        }

        public double getSlRatio() {
            return slRatio;
        }

        public void setSlRatio(double slRatio) {
            this.slRatio = slRatio;
        }

        public double getSumIncome() {
            return sumIncome;
        }

        public void setSumIncome(double sumIncome) {
            this.sumIncome = sumIncome;
        }

        public double getSumMargin() {
            return sumMargin;
        }

        public void setSumMargin(double sumMargin) {
            this.sumMargin = sumMargin;
        }

        public String getTraderAvatar() {
            return traderAvatar;
        }

        public void setTraderAvatar(String traderAvatar) {
            this.traderAvatar = traderAvatar;
        }

        public String getTraderId() {
            return traderId;
        }

        public void setTraderId(String traderId) {
            this.traderId = traderId;
        }

        public String getTraderName() {
            return traderName;
        }

        public void setTraderName(String traderName) {
            this.traderName = traderName;
        }

        public String getTraderType() {
            return traderType;
        }

        public void setTraderType(String traderType) {
            this.traderType = traderType;
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
    }
}
