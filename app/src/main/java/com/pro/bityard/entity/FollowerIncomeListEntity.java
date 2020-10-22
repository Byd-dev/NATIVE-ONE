package com.pro.bityard.entity;

import java.util.List;

public class FollowerIncomeListEntity {


    /**
     * total : 3
     * code : 200
     * data : [{"currency":"USDT","date":"2020-10-15","money":0.00109989,"userId":"475609350069960704"},{"currency":"USDT","date":"2020-10-12","money":0.66928704,"userId":"475609350069960704"},{"currency":"USDT","date":"2020-10-10","money":1.2,"userId":"475609350069960704"}]
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
        return "FollowerIncomeList{" +
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
        /**
         * currency : USDT
         * date : 2020-10-15
         * money : 0.00109989
         * userId : 475609350069960704
         */

        private String currency;
        private String date;
        private double money;
        private String userId;

        @Override
        public String toString() {
            return "DataBean{" +
                    "currency='" + currency + '\'' +
                    ", date='" + date + '\'' +
                    ", money=" + money +
                    ", userId='" + userId + '\'' +
                    '}';
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
