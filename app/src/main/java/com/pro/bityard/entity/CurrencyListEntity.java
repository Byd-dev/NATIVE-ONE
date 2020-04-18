package com.pro.bityard.entity;

import java.io.Serializable;
import java.util.List;

public class CurrencyListEntity implements Serializable {


    /**
     * code : 200
     * data : [{"code":"USD","coinTender":false,"contract":null,"id":"2","legalTender":true,"name":"美元","scale":2,"sort":1,"type":0,"unit":"美元"},{"code":"CNY","coinTender":false,"contract":null,"id":"1","legalTender":true,"name":"人民币","scale":2,"sort":2,"type":0,"unit":"元"},{"code":"VND","coinTender":false,"contract":null,"id":"121","legalTender":true,"name":"越南盾","scale":0,"sort":2,"type":0,"unit":"盾"},{"code":"EUR","coinTender":false,"contract":null,"id":"4","legalTender":true,"name":"欧元","scale":2,"sort":4,"type":0,"unit":"元"},{"code":"HKD","coinTender":false,"contract":null,"id":"3","legalTender":true,"name":"港元","scale":2,"sort":5,"type":0,"unit":"元"},{"code":"JPY","coinTender":false,"contract":null,"id":"5","legalTender":true,"name":"日元","scale":2,"sort":6,"type":0,"unit":"日元"}]
     * message :
     * ratio : 0.0
     */

    private int code;
    private String message;
    private double ratio;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "CurrencyListEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", ratio=" + ratio +
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        @Override
        public String toString() {
            return "DataBean{" +
                    "code='" + code + '\'' +
                    ", coinTender=" + coinTender +
                    ", contract=" + contract +
                    ", id='" + id + '\'' +
                    ", legalTender=" + legalTender +
                    ", name='" + name + '\'' +
                    ", scale=" + scale +
                    ", sort=" + sort +
                    ", type=" + type +
                    ", unit='" + unit + '\'' +
                    '}';
        }

        /**
         * code : USD
         * coinTender : false
         * contract : null
         * id : 2
         * legalTender : true
         * name : 美元
         * scale : 2
         * sort : 1
         * type : 0
         * unit : 美元
         */



        private String code;
        private boolean coinTender;
        private Object contract;
        private String id;
        private boolean legalTender;
        private String name;
        private int scale;
        private int sort;
        private int type;
        private String unit;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isCoinTender() {
            return coinTender;
        }

        public void setCoinTender(boolean coinTender) {
            this.coinTender = coinTender;
        }

        public Object getContract() {
            return contract;
        }

        public void setContract(Object contract) {
            this.contract = contract;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isLegalTender() {
            return legalTender;
        }

        public void setLegalTender(boolean legalTender) {
            this.legalTender = legalTender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
