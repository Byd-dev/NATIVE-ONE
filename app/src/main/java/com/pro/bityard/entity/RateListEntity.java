package com.pro.bityard.entity;

import java.util.List;

public class RateListEntity {


    /**
     * code : 200
     * data : {"BTC":6500.1234,"ETH":215.59,"BCH":280.0019}
     * list : [{"name":"BTC","value":6500.1234},{"name":"ETH","value":215.59},{"name":"BCH","value":280.0019}]
     */

    private int code;
    private DataBean data;
    private String message;
    private List<ListBean> list;

    @Override
    public String toString() {
        return "RateListEntity{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", list=" + list +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class DataBean {
        /**
         * BTC : 6500.1234
         * ETH : 215.59
         * BCH : 280.0019
         */

        private double BTC;
        private double ETH;
        private double BCH;

        public double getBTC() {
            return BTC;
        }

        public void setBTC(double BTC) {
            this.BTC = BTC;
        }

        public double getETH() {
            return ETH;
        }

        public void setETH(double ETH) {
            this.ETH = ETH;
        }

        public double getBCH() {
            return BCH;
        }

        public void setBCH(double BCH) {
            this.BCH = BCH;
        }
    }

    public static class ListBean {
        /**
         * name : BTC
         * value : 6500.1234
         */

        private String name;
        private double value;

        @Override
        public String toString() {
            return "ListBean{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
