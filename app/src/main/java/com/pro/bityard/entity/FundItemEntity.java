package com.pro.bityard.entity;

import java.util.List;

public class FundItemEntity {


    /**
     * code : 200
     * data : [{"code":"EOS","coinTender":true,"contract":"EOSUSDT1808","id":"117","legalTender":false,"name":"EOS","scale":8,"sort":0,"type":1,"unit":"个"},{"code":"LTC","coinTender":true,"contract":"LTCUSDT1808","id":"118","legalTender":false,"name":"LTC","scale":8,"sort":0,"type":1,"unit":"个"},{"code":"BCH","coinTender":true,"contract":"BCHUSDT1808","id":"120","legalTender":false,"name":"BCH","scale":8,"sort":0,"type":1,"unit":"个"},{"code":"USDT","coinTender":true,"contract":null,"id":"99","legalTender":false,"name":"USDT","scale":2,"sort":1,"type":1,"unit":"个"},{"code":"BTC","coinTender":true,"contract":"BTCUSDT1808","id":"115","legalTender":false,"name":"BTC","scale":8,"sort":2,"type":1,"unit":"个"},{"code":"ETH","coinTender":true,"contract":"ETHUSDT1808","id":"104","legalTender":false,"name":"ETH","scale":8,"sort":3,"type":1,"unit":"个"},{"code":"XRP","coinTender":true,"contract":"XRPUSDT1808","id":"119","legalTender":false,"name":"XRP","scale":8,"sort":4,"type":1,"unit":"个"},{"code":"TRX","coinTender":true,"contract":"TRXUSDT1808","id":"116","legalTender":false,"name":"TRX","scale":8,"sort":5,"type":1,"unit":"个"},{"code":"HT","coinTender":true,"contract":"HTUSDT1808","id":"122","legalTender":false,"name":"HT","scale":4,"sort":6,"type":1,"unit":"个"},{"code":"LINK","coinTender":true,"contract":"LINKUSDT1808","id":"123","legalTender":false,"name":"LINK","scale":4,"sort":7,"type":1,"unit":"个"}]
     * message :
     * ratio : 0.0
     */

    private int code;
    private String message;
    private double ratio;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "FundItemEntity{" +
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

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "code='" + code + '\'' +
                    ", coinTender=" + coinTender +
                    ", contract='" + contract + '\'' +
                    ", id='" + id + '\'' +
                    ", legalTender=" + legalTender +
                    ", name='" + name + '\'' +
                    ", scale=" + scale +
                    ", sort=" + sort +
                    ", type=" + type +
                    ", unit='" + unit + '\'' +
                    '}';
        }

        public DataBean() {
        }

        public DataBean(String code, boolean coinTender, String contract, String id, boolean legalTender, String name, int scale, int sort, int type, String unit) {
            this.code = code;
            this.coinTender = coinTender;
            this.contract = contract;
            this.id = id;
            this.legalTender = legalTender;
            this.name = name;
            this.scale = scale;
            this.sort = sort;
            this.type = type;
            this.unit = unit;
        }

        /**
         * code : EOS
         * coinTender : true
         * contract : EOSUSDT1808
         * id : 117
         * legalTender : false
         * name : EOS
         * scale : 8
         * sort : 0
         * type : 1
         * unit : 个
         */





        private String code;
        private boolean coinTender;
        private String contract;
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

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
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
