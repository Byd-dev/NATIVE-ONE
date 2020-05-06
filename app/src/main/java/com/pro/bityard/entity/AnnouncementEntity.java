package com.pro.bityard.entity;

import java.util.List;

public class AnnouncementEntity {


    /**
     * notices : [{"brand":"BYD","content":"<div>尊敬的用户 :<\/div><div><br><\/div><div><br><\/div><div>我们很高兴地宣布，Bityard的手机版正式线上，同时，APP应用程序已在Google Play和Apple Store上发布。<\/div><div><br><\/div><div>手机版访问地址：<a href=\"https://m.bityard.com\" style=\"\">https://m.bityard.com<\/a><\/div><div><br><\/div><div>APP下载二维码（Google Play &amp; Apple Store）：<\/div><div><img src=\"https://static.bityard.com/notice/%E8%8B%B9%E6%9E%9C%E5%AE%89%E5%8D%93%E4%BA%8C%E7%BB%B4%E7%A0%81.jpg\"><br><\/div><div><br><\/div><div>今后，不管您身处何处，您可以随时通过手机在Bityard上交易。<\/div><div><br><\/div><div>感谢您一直以来对我们平台的支持和使用！<\/div><div><br><\/div><div><br><\/div><div><br><\/div><div>Bityard团队<\/div><div>2020年4月20日<\/div>","expire":1618848000000,"id":"458960886493609984","language":"zh_CN","time":1587354373000,"title":"关于Bityard 手机版及APP上线的公告","top":false,"url":""}]
     * code : 200
     * message :
     */

    private int code;
    private String message;
    private List<NoticesBean> notices;

    @Override
    public String toString() {
        return "AnnouncementEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", notices=" + notices +
                '}';
    }

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

    public List<NoticesBean> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticesBean> notices) {
        this.notices = notices;
    }

    public static class NoticesBean {
        /**
         * brand : BYD
         * content : <div>尊敬的用户 :</div><div><br></div><div><br></div><div>我们很高兴地宣布，Bityard的手机版正式线上，同时，APP应用程序已在Google Play和Apple Store上发布。</div><div><br></div><div>手机版访问地址：<a href="https://m.bityard.com" style="">https://m.bityard.com</a></div><div><br></div><div>APP下载二维码（Google Play &amp; Apple Store）：</div><div><img src="https://static.bityard.com/notice/%E8%8B%B9%E6%9E%9C%E5%AE%89%E5%8D%93%E4%BA%8C%E7%BB%B4%E7%A0%81.jpg"><br></div><div><br></div><div>今后，不管您身处何处，您可以随时通过手机在Bityard上交易。</div><div><br></div><div>感谢您一直以来对我们平台的支持和使用！</div><div><br></div><div><br></div><div><br></div><div>Bityard团队</div><div>2020年4月20日</div>
         * expire : 1618848000000
         * id : 458960886493609984
         * language : zh_CN
         * time : 1587354373000
         * title : 关于Bityard 手机版及APP上线的公告
         * top : false
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
}
