package com.pro.bityard.entity;

import java.util.List;

public class InitEntity {


    /**
     * names : ["TRXUSDT:波场币","EOSUSDT:柚子币","LTCUSDT:莱特币","ETHUSDT:以太坊","XRPUSDT:瑞波币","BTCUSDT:比特币","DASHUSDT:达世币","BCHUSDT:比特币现金","BNBUSDT:币安币","ETCUSDT:以太经典"]
     * code : 200
     * quoteDomain : 39DE33DDE9FA12F1B9D37C631D6308B3D9C5C8605F7BB088DE537B3058EFE38C
     * androidUrl : https://download.wanzhuan7h.com/app.apk
     * time : 1583997332
     * iosUrl : https://itunes.apple.com/cn/app/id1360236743
     * message :
     * group : [{"name":"数字货币","list":"TRXUSDT;EOSUSDT;LTCUSDT;ETHUSDT;XRPUSDT;DASHUSDT;BTCUSDT;BNBUSDT;BCHUSDT;ETCUSDT"}]
     */

    private int code;
    private String quoteDomain;
    private String androidUrl;
    private String time;
    private String iosUrl;
    private String message;
    private List<String> names;
    private List<GroupBean> group;

    @Override
    public String toString() {
        return "InitEntity{" +
                "code=" + code +
                ", quoteDomain='" + quoteDomain + '\'' +
                ", androidUrl='" + androidUrl + '\'' +
                ", time='" + time + '\'' +
                ", iosUrl='" + iosUrl + '\'' +
                ", message='" + message + '\'' +
                ", names=" + names +
                ", group=" + group +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getQuoteDomain() {
        return quoteDomain;
    }

    public void setQuoteDomain(String quoteDomain) {
        this.quoteDomain = quoteDomain;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<GroupBean> getGroup() {
        return group;
    }

    public void setGroup(List<GroupBean> group) {
        this.group = group;
    }

    public static class GroupBean {
        /**
         * name : 数字货币
         * list : TRXUSDT;EOSUSDT;LTCUSDT;ETHUSDT;XRPUSDT;DASHUSDT;BTCUSDT;BNBUSDT;BCHUSDT;ETCUSDT
         */

        private String name;
        private String list;

        @Override
        public String toString() {
            return "GroupBean{" +
                    "name='" + name + '\'' +
                    ", list='" + list + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getList() {
            return list;
        }

        public void setList(String list) {
            this.list = list;
        }
    }
}
