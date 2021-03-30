package com.pro.bityard.entity;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

import java.util.List;

public class BannerEntity {


    /**
     * notices : [{"brand":"BYD","content":"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈","expire":1619712000000,"id":"460867231144738816","language":"zh_CN","time":1587744000000,"title":"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈","top":true,"type":1,"url":""},{"brand":"BYD","content":"&nbsp; 尊敬的用户：<div>&nbsp;快来注册，注册送1024BYD啦！<div>&nbsp; &nbsp; 交易即送2USDT，先到先得。<\/div><\/div>","expire":1617379200000,"id":"452913996924715008","language":"zh_CN","time":1585843200000,"title":"测试测试测试测试测试测试测试试测试测试测试测试","top":false,"type":1,"url":""}]
     * code : 200
     * carousels : []
     * message :
     */

    private List<NoticesBean> notices;
    private Integer code;
    private List<CarouselsBean> carousels;
    private String message;


    public List<NoticesBean> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticesBean> notices) {
        this.notices = notices;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BannerXEntity{" +
                "notices=" + notices +
                ", code=" + code +
                ", carousels=" + carousels +
                ", message='" + message + '\'' +
                '}';
    }

    public static class NoticesBean {
        /**
         * brand : BYD
         * content : 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈
         * expire : 1619712000000
         * id : 460867231144738816
         * language : zh_CN
         * time : 1587744000000
         * title : 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈
         * top : true
         * type : 1
         * url :
         */

        private String brand;
        private String content;
        private Long expire;
        private String id;
        private String language;
        private Long time;
        private String title;
        private Boolean top;
        private Integer type;
        private String url;

        @Override
        public String toString() {
            return "NoticesBean{" +
                    "brand='" + brand + '\'' +
                    ", content='" + content + '\'' +
                    ", expire=" + expire +
                    ", id='" + id + '\'' +
                    ", language='" + language + '\'' +
                    ", time=" + time +
                    ", title='" + title + '\'' +
                    ", top=" + top +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getExpire() {
            return expire;
        }

        public void setExpire(Long expire) {
            this.expire = expire;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Boolean getTop() {
            return top;
        }

        public void setTop(Boolean top) {
            this.top = top;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public static class CarouselsBean extends SimpleBannerInfo {
        /**
         * brand : BYD
         * createTime : 1585842500000
         * expireTime : 1609257600000
         * id : 452618559663636480
         * key : regesit
         * language : zh_CN
         * name : 注册送byd
         * publishTime : 1585842354000
         * url : regesit
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


        @Override
        public String toString() {
            return "CarouselsBean{" +
                    "brand='" + brand + '\'' +
                    ", createTime=" + createTime +
                    ", expireTime=" + expireTime +
                    ", id='" + id + '\'' +
                    ", key='" + key + '\'' +
                    ", language='" + language + '\'' +
                    ", name='" + name + '\'' +
                    ", publishTime=" + publishTime +
                    ", url='" + url + '\'' +
                    '}';
        }

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
