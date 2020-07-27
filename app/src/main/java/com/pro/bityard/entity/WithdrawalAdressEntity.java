package com.pro.bityard.entity;

import java.util.List;

public class WithdrawalAdressEntity {

    /**
     * code : 200
     * data : [{"address":"100000000098","authed":false,"brand":null,"chain":"OMNI","count":0,"createTime":1587220396000,"currency":"USDT","id":"458398723290955776","order":null,"remark":"uuu888","type":2,"userId":null,"withdraw":"1-2"},{"address":"1uwiieuryyqioqwiuyuuwuuwiiq","authed":false,"brand":null,"chain":"OMNI","count":0,"createTime":1583571924000,"currency":"USDT","id":"443096024098013184","order":null,"remark":"1121","type":2,"userId":null,"withdraw":"1-2"},{"address":"jhuiuehhgkjsiwuyeiioeoueyio","authed":false,"brand":null,"chain":"OMNI","count":0,"createTime":null,"currency":"USDT","id":"441194471242072064","order":null,"remark":"ceshi","type":2,"userId":null,"withdraw":"1-2"}]
     * message :
     */

    private int code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "WithdrawalAdressEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
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
                    "address='" + address + '\'' +
                    ", authed=" + authed +
                    ", brand='" + brand + '\'' +
                    ", chain='" + chain + '\'' +
                    ", count=" + count +
                    ", createTime=" + createTime +
                    ", currency='" + currency + '\'' +
                    ", id='" + id + '\'' +
                    ", order='" + order + '\'' +
                    ", remark='" + remark + '\'' +
                    ", type=" + type +
                    ", userId='" + userId + '\'' +
                    ", withdraw='" + withdraw + '\'' +
                    ", withdrawChain='" + withdrawChain + '\'' +
                    '}';
        }

        /**
         * address : 100000000098
         * authed : false
         * brand : null
         * chain : OMNI
         * count : 0
         * createTime : 1587220396000
         * currency : USDT
         * id : 458398723290955776
         * order : null
         * remark : uuu888
         * type : 2
         * userId : null
         * withdraw : 1-2
         */


        private String address;
        private boolean authed;
        private String brand;
        private String chain;
        private int count;
        private long createTime;
        private String currency;
        private String id;
        private String order;
        private String remark;
        private int type;
        private String userId;
        private String withdraw;
        private String withdrawChain;

        public String getWithdrawChain() {
            return withdrawChain;
        }

        public void setWithdrawChain(String withdrawChain) {
            this.withdrawChain = withdrawChain;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isAuthed() {
            return authed;
        }

        public void setAuthed(boolean authed) {
            this.authed = authed;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getWithdraw() {
            return withdraw;
        }

        public void setWithdraw(String withdraw) {
            this.withdraw = withdraw;
        }
    }
}
