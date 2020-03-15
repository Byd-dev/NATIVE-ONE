package com.pro.bityard.entity;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

import java.util.List;

public class BannerEntity {


    /**
     * notices : []
     * code : 200
     * alertFlag : register
     * carousels : [{"brand":"BYD","createTime":1550764800000,"expireTime":1592150400000,"id":"346675240975204352","key":"/","language":"zh_CN","name":"测试天然气banner","publishTime":1577808000000,"url":"/src/banner/banner04.png"},{"brand":"BYD","createTime":1559318400000,"expireTime":1592064000000,"id":"346343691113201664","key":"/invest","language":"zh_CN","name":"越投资 越有钱","publishTime":1577808000000,"url":"/src/banner/invest.png"}]
     * message :
     */

    private int code;
    private String alertFlag;
    private String message;
    private List<String> notices;
    private List<CarouselsBean> carousels;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAlertFlag() {
        return alertFlag;
    }

    public void setAlertFlag(String alertFlag) {
        this.alertFlag = alertFlag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public static class CarouselsBean extends SimpleBannerInfo {
        /**
         * brand : BYD
         * createTime : 1550764800000
         * expireTime : 1592150400000
         * id : 346675240975204352
         * key : /
         * language : zh_CN
         * name : 测试天然气banner
         * publishTime : 1577808000000
         * url : /src/banner/banner04.png
         */

        private String brand;
        private long createTime;
        private long expireTime;
        private String id;
        private String key;
        private String language;
        private String name;
        private long publishTime;
        private String url;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public Object getXBannerUrl() {
            return url;
        }
    }
}
