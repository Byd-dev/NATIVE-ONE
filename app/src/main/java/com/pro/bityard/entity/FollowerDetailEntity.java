package com.pro.bityard.entity;

import java.io.Serializable;
import java.util.List;

public class FollowerDetailEntity implements Serializable{


    /**
     * code : 200
     * data : {"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20201007104925_679.jpg?Expires=1917398965&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=1wA8mMV9XqSi17AAqdBoBu7pCow%3D","betDays":137,"brand":null,"currency":"USDT","follower":2,"idearTag":null,"idearTags":[],"registerTime":1591323705000,"selectedRegion":null,"sex":1,"styleTag":null,"styleTags":[],"trader30DaysCost":1434,"trader30DaysCount":102,"trader30DaysDefeat":93.1034,"trader30DaysDraw":1.4,"trader30DaysIncome":-63.96862535,"trader30DaysRate":0,"traderRatio":0.08,"traderTotalIncome":1.87038693,"type":2,"userId":"475609350069960704","username":"揍谁"}
     * message :
     */

    private int code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "FollowerDetailEntity{" +
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

    public static class DataBean implements Serializable {

        /**
         * avatar :
         * betDays : 62
         * brand : BYD
         * currency : USDT
         * follower : 4
         * idearTag : idear_3
         * idearTags : ["团队在做事,不要看价格,拿住!"]
         * registerTime : 1594265436000
         * selectedRegion : null
         * sex : 0
         * styleTag : risk_low,risk_high,pos_light
         * styleTags : ["稳健","高风险","轻仓"]
         * trader30DaysCost : 385.0
         * trader30DaysCount : 0
         * trader30DaysDefeat : 0.0
         * trader30DaysDraw : 0.258
         * trader30DaysIncome : 39.182153
         * trader30DaysRate : 0.10177183
         * type : 2
         * userId : 487947864136630272
         * username : 带单人02
         */




        private String avatar;
        private String betDays;
        private String brand;
        private String currency;
        private String follower;
        private String idearTag;
        private long registerTime;
        private Object selectedRegion;
        private int sex;
        private String styleTag;
        private String trader30DaysMargin;
        private double traderProfitRate;
        private double trader30DaysCount;
        private double trader30DaysDefeat;
        private double trader30DaysDraw;
        private double trader30DaysIncome;
        private double trader30DaysRate;
        private double traderRatio;
        private double incomeRate;
        private double traderIncome;
        private int type;
        private String userId;
        private String username;
        private List<String> idearTags;
        private List<String> styleTags;
        private boolean follow;



        private boolean active;
        private long createTime;
        private double followVal;
        private String followMax;
        private int followWay;
        private String id;
        private Object lastCode;
        private Object lastData;
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "avatar='" + avatar + '\'' +
                    ", betDays='" + betDays + '\'' +
                    ", brand='" + brand + '\'' +
                    ", currency='" + currency + '\'' +
                    ", follower='" + follower + '\'' +
                    ", idearTag='" + idearTag + '\'' +
                    ", registerTime=" + registerTime +
                    ", selectedRegion=" + selectedRegion +
                    ", sex=" + sex +
                    ", styleTag='" + styleTag + '\'' +
                    ", trader30DaysMargin='" + trader30DaysMargin + '\'' +
                    ", traderProfitRate=" + traderProfitRate +
                    ", trader30DaysCount=" + trader30DaysCount +
                    ", trader30DaysDefeat=" + trader30DaysDefeat +
                    ", trader30DaysDraw=" + trader30DaysDraw +
                    ", trader30DaysIncome=" + trader30DaysIncome +
                    ", trader30DaysRate=" + trader30DaysRate +
                    ", traderRatio=" + traderRatio +
                    ", incomeRate=" + incomeRate +
                    ", traderIncome=" + traderIncome +
                    ", type=" + type +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", idearTags=" + idearTags +
                    ", styleTags=" + styleTags +
                    ", follow=" + follow +
                    ", active=" + active +
                    ", createTime=" + createTime +
                    ", followVal=" + followVal +
                    ", followMax=" + followMax +
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
                    '}';
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
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

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public double getSlRatio() {
            return slRatio;
        }

        public void setSlRatio(double slRatio) {
            this.slRatio = slRatio;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public double getFollowVal() {
            return followVal;
        }

        public void setFollowVal(double followVal) {
            this.followVal = followVal;
        }

        public String getFollowMax() {
            return followMax;
        }

        public void setFollowMax(String followMax) {
            this.followMax = followMax;
        }

        public int getFollowWay() {
            return followWay;
        }

        public void setFollowWay(int followWay) {
            this.followWay = followWay;
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

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public String getTrader30DaysMargin() {
            return trader30DaysMargin;
        }

        public void setTrader30DaysMargin(String trader30DaysMargin) {
            this.trader30DaysMargin = trader30DaysMargin;
        }

        public double getTraderProfitRate() {
            return traderProfitRate;
        }

        public void setTraderProfitRate(double traderProfitRate) {
            this.traderProfitRate = traderProfitRate;
        }

        public double getTraderIncome() {
            return traderIncome;
        }

        public void setTraderIncome(double traderIncome) {
            this.traderIncome = traderIncome;
        }

        public double getIncomeRate() {
            return incomeRate;
        }

        public void setIncomeRate(double incomeRate) {
            this.incomeRate = incomeRate;
        }




        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBetDays() {
            return betDays;
        }

        public void setBetDays(String betDays) {
            this.betDays = betDays;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getFollower() {
            return follower;
        }

        public void setFollower(String follower) {
            this.follower = follower;
        }

        public String getIdearTag() {
            return idearTag;
        }

        public void setIdearTag(String idearTag) {
            this.idearTag = idearTag;
        }

        public long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
        }

        public Object getSelectedRegion() {
            return selectedRegion;
        }

        public void setSelectedRegion(Object selectedRegion) {
            this.selectedRegion = selectedRegion;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getStyleTag() {
            return styleTag;
        }

        public void setStyleTag(String styleTag) {
            this.styleTag = styleTag;
        }



        public double getTrader30DaysCount() {
            return trader30DaysCount;
        }

        public void setTrader30DaysCount(double trader30DaysCount) {
            this.trader30DaysCount = trader30DaysCount;
        }

        public double getTrader30DaysDefeat() {
            return trader30DaysDefeat;
        }

        public void setTrader30DaysDefeat(double trader30DaysDefeat) {
            this.trader30DaysDefeat = trader30DaysDefeat;
        }

        public double getTrader30DaysDraw() {
            return trader30DaysDraw;
        }

        public void setTrader30DaysDraw(double trader30DaysDraw) {
            this.trader30DaysDraw = trader30DaysDraw;
        }

        public double getTrader30DaysIncome() {
            return trader30DaysIncome;
        }

        public void setTrader30DaysIncome(double trader30DaysIncome) {
            this.trader30DaysIncome = trader30DaysIncome;
        }

        public double getTrader30DaysRate() {
            return trader30DaysRate;
        }

        public void setTrader30DaysRate(double trader30DaysRate) {
            this.trader30DaysRate = trader30DaysRate;
        }

        public double getTraderRatio() {
            return traderRatio;
        }

        public void setTraderRatio(double traderRatio) {
            this.traderRatio = traderRatio;
        }



        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public List<String> getIdearTags() {
            return idearTags;
        }

        public void setIdearTags(List<String> idearTags) {
            this.idearTags = idearTags;
        }

        public List<String> getStyleTags() {
            return styleTags;
        }

        public void setStyleTags(List<String> styleTags) {
            this.styleTags = styleTags;
        }
    }
}
