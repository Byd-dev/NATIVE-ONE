package com.pro.bityard.entity;

import java.util.List;

public class FollowEntity {


    /**
     * total : 21
     * code : 200
     * data : [{"avatar":"","betDays":62,"brand":"BYD","currency":"USDT","follower":4,"idearTag":"idear_3","idearTags":["团队在做事,不要看价格,拿住!"],"registerTime":1594265436000,"selectedRegion":null,"sex":0,"styleTag":"risk_low,risk_high,pos_light","styleTags":["稳健","高风险","轻仓"],"trader30DaysCost":385,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0.258,"trader30DaysIncome":39.182153,"trader30DaysRate":0.10177183,"type":2,"userId":"487947864136630272","username":"带单人02"},{"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200828154005_432.jpg?Expires=1913960405&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=tAE0%2FeJ1wgO1lpltq1%2FQxP4qCV8%3D","betDays":161,"brand":"BYD","currency":"USDT","follower":3,"idearTag":"idear_5","idearTags":["一币一嫩模,一币一别墅"],"registerTime":1585717772000,"selectedRegion":null,"sex":0,"styleTag":"term_short","styleTags":["短线"],"trader30DaysCost":0,"trader30DaysCount":2,"trader30DaysDefeat":12,"trader30DaysDraw":38.7795,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"452096359483310080","username":"Apple"},{"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200814191510_177.png?Expires=1912763710&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=BceF%2FPW%2BsBcbO6V3p6nWaCBUdgc%3D","betDays":244,"brand":"BYD","currency":"USDT","follower":3,"idearTag":"idear_1","idearTags":["一切有为法,如梦亦如露"],"registerTime":1578574378000,"selectedRegion":null,"sex":1,"styleTag":"term_long,pos_heavy,term_short","styleTags":["长线","重仓","短线"],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"422134795078729728","username":"投资鬼才"},{"avatar":"","betDays":120,"brand":"BYD","currency":"USDT","follower":1,"idearTag":null,"idearTags":[],"registerTime":1589263461000,"selectedRegion":null,"sex":1,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"466968059912683520","username":"wasim测试11"},{"avatar":"","betDays":118,"brand":"BYD","currency":"USDT","follower":0,"idearTag":"idear_7","idearTags":["耐得住寂寞,才能守得住芳华!"],"registerTime":1589423012000,"selectedRegion":null,"sex":0,"styleTag":"risk_high","styleTags":["高风险"],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"467637263133343744","username":"跟单人A01废弃"},{"avatar":"","betDays":117,"brand":"BYD","currency":"USDT","follower":0,"idearTag":null,"idearTags":[],"registerTime":1589543214000,"selectedRegion":null,"sex":0,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"468141429110489088","username":"prayer测试头头"},{"avatar":"","betDays":113,"brand":"BYD","currency":"USDT","follower":0,"idearTag":null,"idearTags":[],"registerTime":1589854479000,"selectedRegion":null,"sex":0,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"469446966125559808","username":"日工资1号下级8"}]
     */

    private int total;
    private int code;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "FollowEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                    "avatar='" + avatar + '\'' +
                    ", betDays=" + betDays +
                    ", brand='" + brand + '\'' +
                    ", currency='" + currency + '\'' +
                    ", follower=" + follower +
                    ", idearTag='" + idearTag + '\'' +
                    ", registerTime=" + registerTime +
                    ", selectedRegion=" + selectedRegion +
                    ", sex=" + sex +
                    ", styleTag='" + styleTag + '\'' +
                    ", trader30DaysCost=" + trader30DaysCost +
                    ", trader30DaysCount=" + trader30DaysCount +
                    ", trader30DaysDefeat=" + trader30DaysDefeat +
                    ", trader30DaysDraw=" + trader30DaysDraw +
                    ", trader30DaysIncome=" + trader30DaysIncome +
                    ", trader30DaysRate=" + trader30DaysRate +
                    ", type=" + type +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", idearTags=" + idearTags +
                    ", styleTags=" + styleTags +
                    '}';
        }

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
        private String trader30DaysCost;
        private String trader30DaysCount;
        private double trader30DaysDefeat;
        private double trader30DaysDraw;
        private String trader30DaysIncome;
        private double trader30DaysRate;
        private int type;
        private String userId;
        private String username;
        private List<String> idearTags;
        private List<String> styleTags;

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

        public String getTrader30DaysCost() {
            return trader30DaysCost;
        }

        public void setTrader30DaysCost(String trader30DaysCost) {
            this.trader30DaysCost = trader30DaysCost;
        }

        public String getTrader30DaysCount() {
            return trader30DaysCount;
        }

        public void setTrader30DaysCount(String trader30DaysCount) {
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

        public String getTrader30DaysIncome() {
            return trader30DaysIncome;
        }

        public void setTrader30DaysIncome(String trader30DaysIncome) {
            this.trader30DaysIncome = trader30DaysIncome;
        }

        public double getTrader30DaysRate() {
            return trader30DaysRate;
        }

        public void setTrader30DaysRate(double trader30DaysRate) {
            this.trader30DaysRate = trader30DaysRate;
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
