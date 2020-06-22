package com.pro.bityard.entity;

import java.util.List;

public class ExchangeRecordEntity {


    /**
     * total : 7
     * code : 200
     * data : [{"brand":"BYD","createTime":1592470925000,"desCurrency":"USDT","desMoney":2333.3,"descr":"1ETH ≈ 233.3300USDT","fee":0,"id":"480421136472358912","price":233.33,"srcCurrency":"ETH","srcMoney":10,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1590568133000,"desCurrency":"USDT","desMoney":8935.08,"descr":"1BTC ≈ 8,935.0800USDT","fee":0,"id":"472440249642680320","price":8935.08,"srcCurrency":"BTC","srcMoney":1,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1590137855000,"desCurrency":"USDT","desMoney":0.80636,"descr":"1ETH ≈ 201.5900USDT","fee":0,"id":"470635534470234112","price":201.59,"srcCurrency":"ETH","srcMoney":0.004,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1586524729000,"desCurrency":"USDT","desMoney":3.0517,"descr":"1LINK ≈ 3.0517USDT","fee":0,"id":"455480984024907776","price":3.0517,"srcCurrency":"LINK","srcMoney":1,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1586524719000,"desCurrency":"USDT","desMoney":0.024926,"descr":"1USDT ≈ 80.2375TRX","fee":0,"id":"455480940127322112","price":0.012463,"srcCurrency":"TRX","srcMoney":2,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1586524707000,"desCurrency":"USDT","desMoney":1.08414,"descr":"1HT ≈ 3.6138USDT","fee":0,"id":"455480890395459584","price":3.6138,"srcCurrency":"HT","srcMoney":0.3,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"},{"brand":"BYD","createTime":1586524694000,"desCurrency":"USDT","desMoney":0.37556,"descr":"1USDT ≈ 5.3254XRP","fee":0,"id":"455480836049862656","price":0.18778,"srcCurrency":"XRP","srcMoney":2,"userId":"452096359483310080","userType":2,"username":"嘻嘻哈哈"}]
     * page : 0
     * rows : 10
     * message :
     */

    private String total;
    private int code;
    private int page;
    private int rows;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "ExchangeRecordEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", page=" + page +
                ", rows=" + rows +
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
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
                    "brand='" + brand + '\'' +
                    ", createTime=" + createTime +
                    ", desCurrency='" + desCurrency + '\'' +
                    ", desMoney=" + desMoney +
                    ", descr='" + descr + '\'' +
                    ", fee=" + fee +
                    ", id='" + id + '\'' +
                    ", price=" + price +
                    ", srcCurrency='" + srcCurrency + '\'' +
                    ", srcMoney=" + srcMoney +
                    ", userId='" + userId + '\'' +
                    ", userType=" + userType +
                    ", username='" + username + '\'' +
                    '}';
        }

        /**
         * brand : BYD
         * createTime : 1592470925000
         * desCurrency : USDT
         * desMoney : 2333.3
         * descr : 1ETH ≈ 233.3300USDT
         * fee : 0.0
         * id : 480421136472358912
         * price : 233.33
         * srcCurrency : ETH
         * srcMoney : 10.0
         * userId : 452096359483310080
         * userType : 2
         * username : 嘻嘻哈哈
         */



        private String brand;
        private long createTime;
        private String desCurrency;
        private double desMoney;
        private String descr;
        private double fee;
        private String id;
        private double price;
        private String srcCurrency;
        private double srcMoney;
        private String userId;
        private int userType;
        private String username;

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

        public String getDesCurrency() {
            return desCurrency;
        }

        public void setDesCurrency(String desCurrency) {
            this.desCurrency = desCurrency;
        }

        public double getDesMoney() {
            return desMoney;
        }

        public void setDesMoney(double desMoney) {
            this.desMoney = desMoney;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getSrcCurrency() {
            return srcCurrency;
        }

        public void setSrcCurrency(String srcCurrency) {
            this.srcCurrency = srcCurrency;
        }

        public double getSrcMoney() {
            return srcMoney;
        }

        public void setSrcMoney(double srcMoney) {
            this.srcMoney = srcMoney;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
