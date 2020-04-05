package com.pro.bityard.entity;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

import java.util.List;

public class BannerEntity {


    /**
     * notices : [{"brand":"BYD","content":"公告测试公告测试测试测试","expire":1711641600000,"id":"451136540236578816","language":"zh_CN","time":1585483200000,"title":"公告测试","top":true,"url":""},{"brand":"BYD","content":"&nbsp; 尊敬的用户：<div>&nbsp;快来注册，注册送1024BYD啦！<div>&nbsp; &nbsp; 交易即送2USDT，先到先得。<\/div><\/div>","expire":1617379200000,"id":"452913996924715008","language":"zh_CN","time":1585843200000,"title":"注册送1024BYD","top":false,"url":""},{"brand":"BYD","content":"<div><div>&nbsp; &nbsp;尊敬的用户：<\/div><div>　　根据大连商品交易所、郑州商品交易所的合约交易规则，<font color=\"#f83a22\">PP聚丙烯（PP1909）、白糖（SR909）<\/font>合约到期不再交易，交易合约更换为<font color=\"#f83a22\">PP2001、SR001<\/font><\/div><div>　　<font color=\"#fad165\">请以最新合约成交价为准。<\/font><\/div><div><font color=\"#fad165\">　&nbsp; &nbsp; 感谢您的支持与信任。<\/font><\/div><\/div><div><br><\/div><div><br><\/div><div><br><\/div><div><br><\/div>","expire":1664467200000,"id":"375587739308916736","language":"zh_CN","time":1567476727000,"title":"PP聚丙烯、白糖合约更换提醒","top":false,"url":""}]
     * code : 200
     * alertFlag : register
     * carousels : [{"brand":"BYD","createTime":1585842500000,"expireTime":1609257600000,"id":"452618559663636480","key":"regesit","language":"zh_CN","name":"注册送byd","publishTime":1585842354000,"url":"regesit"},{"brand":"BYD","createTime":1585842555000,"expireTime":1608739200000,"id":"376306873885786112","key":"/activity/rechargeActivity","language":"zh_CN","name":"充值豪礼天天送","publishTime":1577808000000,"url":"/activity/rechargeActivity"},{"brand":"BYD","createTime":1550764800000,"expireTime":1592150400000,"id":"346675240975204352","key":"/","language":"zh_CN","name":"测试天然气banner","publishTime":1577808000000,"url":"/src/banner/banner04.png"},{"brand":"BYD","createTime":1559318400000,"expireTime":1592064000000,"id":"346343691113201664","key":"/invest","language":"zh_CN","name":"越投资 越有钱","publishTime":1577808000000,"url":"/src/banner/invest.png"}]
     * message :
     */

    private int code;
    private String alertFlag;
    private String message;
    private List<NoticesBean> notices;
    private List<CarouselsBean> carousels;

    @Override
    public String toString() {
        return "BannerEntity{" +
                "code=" + code +
                ", alertFlag='" + alertFlag + '\'' +
                ", message='" + message + '\'' +
                ", notices=" + notices +
                ", carousels=" + carousels +
                '}';
    }

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

    public List<NoticesBean> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticesBean> notices) {
        this.notices = notices;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public static class NoticesBean {
        /**
         * brand : BYD
         * content : 公告测试公告测试测试测试
         * expire : 1711641600000
         * id : 451136540236578816
         * language : zh_CN
         * time : 1585483200000
         * title : 公告测试
         * top : true
         * url :
         */

        private String brand;
        private String content;
        private long expire;
        private String id;
        private String language;
        private long time;
        private String title;
        private boolean top;
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

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
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

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isTop() {
            return top;
        }

        public void setTop(boolean top) {
            this.top = top;
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
