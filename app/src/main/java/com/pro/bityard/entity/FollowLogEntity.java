package com.pro.bityard.entity;

import java.util.List;

public class FollowLogEntity {


    /**
     * total : 3
     * code : 200
     * data : [{"bettingId":"520899449602850816","brand":"BYD","createTime":1602121707000,"id":"520899450227802112","logData":"手续费错误，请刷新后重试","tagCode":"follow_error","tagContent":"follow_error","traderId":"520540090876182528","traderName":"牛老师","userId":"475609350069960704","username":"揍谁"},{"bettingId":"515811969694318592","brand":"BYD","createTime":1600908760000,"id":"515811980838584320","logData":"您已被禁止交易，请联系客服","tagCode":"follow_error","tagContent":"follow_error","traderId":"487947864136630272","traderName":"带单人02","userId":"475609350069960704","username":"揍谁"},{"bettingId":"515583971208609792","brand":"BYD","createTime":1600854401000,"id":"515583985318248448","logData":"您已被禁止交易，请联系客服","tagCode":"follow_error","tagContent":"follow_error","traderId":"487947864136630272","traderName":"带单人02","userId":"475609350069960704","username":"揍谁"}]
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
        return "FollowLogEntity{" +
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
                    "bettingId='" + bettingId + '\'' +
                    ", brand='" + brand + '\'' +
                    ", createTime=" + createTime +
                    ", id='" + id + '\'' +
                    ", logData='" + logData + '\'' +
                    ", tagCode='" + tagCode + '\'' +
                    ", tagContent='" + tagContent + '\'' +
                    ", traderId='" + traderId + '\'' +
                    ", traderName='" + traderName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }

        /**
         * bettingId : 520899449602850816
         * brand : BYD
         * createTime : 1602121707000
         * id : 520899450227802112
         * logData : 手续费错误，请刷新后重试
         * tagCode : follow_error
         * tagContent : follow_error
         * traderId : 520540090876182528
         * traderName : 牛老师
         * userId : 475609350069960704
         * username : 揍谁
         */



        private String bettingId;
        private String brand;
        private long createTime;
        private String id;
        private String logData;
        private String tagCode;
        private String tagContent;
        private String traderId;
        private String traderName;
        private String userId;
        private String username;

        public String getBettingId() {
            return bettingId;
        }

        public void setBettingId(String bettingId) {
            this.bettingId = bettingId;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogData() {
            return logData;
        }

        public void setLogData(String logData) {
            this.logData = logData;
        }

        public String getTagCode() {
            return tagCode;
        }

        public void setTagCode(String tagCode) {
            this.tagCode = tagCode;
        }

        public String getTagContent() {
            return tagContent;
        }

        public void setTagContent(String tagContent) {
            this.tagContent = tagContent;
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
