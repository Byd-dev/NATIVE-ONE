package com.pro.bityard.entity;

import java.util.List;

public class FollowersListEntity {


    /**
     * total : 3
     * code : 200
     * data : [{"active":true,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105303_644.jpg?Expires=1916880783&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=horzUU%2BzYRlhVqxhW2%2BoZTy3%2Fpo%3D","brand":null,"createTime":1602748535000,"currency":"USDT","followMax":100,"followVal":2,"followWay":2,"id":"523528558317289472","lastCode":"follow_max_hold","lastData":"5.00000000","lastTime":1602750201000,"maxDay":1000,"maxHold":1000,"slRatio":-1,"sumIncome":3.78290311,"sumMargin":55,"traderAvatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105238_654.jpg?Expires=1916880758&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=iqtlH%2Bof%2FJ907tq9AioWc12KDQE%3D","traderId":"518375173016403968","traderName":"E测试带单人","traderType":"2","updateTime":1602817845000,"userId":"518376338080808960","username":"E测试跟单人"},{"active":false,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201007102928_950.jpg?Expires=1917397768&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=t7BJ2wbzjqRR%2Bk96tSYpkvcIxfk%3D","brand":null,"createTime":1602745863000,"currency":"USDT","followMax":0,"followVal":5,"followWay":1,"id":"523517351485128704","lastCode":"follow_self_off","lastData":"","lastTime":1602749961000,"maxDay":10000,"maxHold":10000,"slRatio":-1,"sumIncome":-2.758608,"sumMargin":45,"traderAvatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105238_654.jpg?Expires=1916880758&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=iqtlH%2Bof%2FJ907tq9AioWc12KDQE%3D","traderId":"518375173016403968","traderName":"E测试带单人","traderType":"2","updateTime":1602750119000,"userId":"520540090876182528","username":"牛老师"},{"active":true,"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201007104925_679.jpg?Expires=1917398965&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=1wA8mMV9XqSi17AAqdBoBu7pCow%3D","brand":null,"createTime":1602744259000,"currency":"USDT","followMax":0,"followVal":5,"followWay":1,"id":"523510622714216448","lastCode":null,"lastData":null,"lastTime":null,"maxDay":10000,"maxHold":10000,"slRatio":-1,"sumIncome":-3.31603231,"sumMargin":80,"traderAvatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105238_654.jpg?Expires=1916880758&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=iqtlH%2Bof%2FJ907tq9AioWc12KDQE%3D","traderId":"518375173016403968","traderName":"E测试带单人","traderType":"2","updateTime":1602817845000,"userId":"475609350069960704","username":"揍谁"}]
     * size : 100
     * page : 1
     * message : 
     */

    private String total;
    private double code;
    private double size;
    private double page;
    private String message;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
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
        /**
         * active : true
         * avatar : http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105303_644.jpg?Expires=1916880783&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=horzUU%2BzYRlhVqxhW2%2BoZTy3%2Fpo%3D
         * brand : null
         * createTime : 1602748535000
         * currency : USDT
         * followMax : 100
         * followVal : 2
         * followWay : 2
         * id : 523528558317289472
         * lastCode : follow_max_hold
         * lastData : 5.00000000
         * lastTime : 1602750201000
         * maxDay : 1000
         * maxHold : 1000
         * slRatio : -1
         * sumIncome : 3.78290311
         * sumMargin : 55
         * traderAvatar : http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201001105238_654.jpg?Expires=1916880758&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=iqtlH%2Bof%2FJ907tq9AioWc12KDQE%3D
         * traderId : 518375173016403968
         * traderName : E测试带单人
         * traderType : 2
         * updateTime : 1602817845000
         * userId : 518376338080808960
         * username : E测试跟单人
         */

        private boolean active;
        private String avatar;
        private Object brand;
        private long createTime;
        private String currency;
        private double followMax;
        private double followVal;
        private double followWay;
        private String id;
        private String lastCode;
        private String lastData;
        private long lastTime;
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

        public double getFollowMax() {
            return followMax;
        }

        public void setFollowMax(double followMax) {
            this.followMax = followMax;
        }

        public double getFollowVal() {
            return followVal;
        }

        public void setFollowVal(double followVal) {
            this.followVal = followVal;
        }

        public double getFollowWay() {
            return followWay;
        }

        public void setFollowWay(double followWay) {
            this.followWay = followWay;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLastCode() {
            return lastCode;
        }

        public void setLastCode(String lastCode) {
            this.lastCode = lastCode;
        }

        public String getLastData() {
            return lastData;
        }

        public void setLastData(String lastData) {
            this.lastData = lastData;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
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
