package com.pro.bityard.entity;

public class AddAddressItemEntity {


    /**
     * code : 200
     * data : {"address":"0xf7fcaf9826128f82e2feb7970a6ea4cc782fcb4b","authed":false,"brand":"BYD","chain":"ERC20","count":0,"createTime":null,"currency":"USDT","id":"464179956683898880","order":null,"remark":"火币","type":2,"userId":"422134795078729728","withdraw":null}
     * message :
     */

    private int code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "AddAddressItemEntity{" +
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
                    ", order=" + order +
                    ", remark='" + remark + '\'' +
                    ", type=" + type +
                    ", userId='" + userId + '\'' +
                    ", withdraw=" + withdraw +
                    '}';
        }

        /**
         * address : 0xf7fcaf9826128f82e2feb7970a6ea4cc782fcb4b
         * authed : false
         * brand : BYD
         * chain : ERC20
         * count : 0
         * createTime : null
         * currency : USDT
         * id : 464179956683898880
         * order : null
         * remark : 火币
         * type : 2
         * userId : 422134795078729728
         * withdraw : null
         */


        private String address;
        private boolean authed;
        private String brand;
        private String chain;
        private int count;
        private Object createTime;
        private String currency;
        private String id;
        private Object order;
        private String remark;
        private int type;
        private String userId;
        private Object withdraw;

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

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
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

        public Object getOrder() {
            return order;
        }

        public void setOrder(Object order) {
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

        public Object getWithdraw() {
            return withdraw;
        }

        public void setWithdraw(Object withdraw) {
            this.withdraw = withdraw;
        }
    }
}
