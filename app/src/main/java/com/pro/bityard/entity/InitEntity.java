package com.pro.bityard.entity;

import java.util.List;

public class InitEntity {


    /**
     * names : ["TRXUSDT:波场币","EOSUSDT:柚子币","LTCUSDT:莱特币","ETHUSDT:以太坊","XRPUSDT:瑞波币","BTCUSDT:比特币","DASHUSDT:达世币","CL:美原油","LINKUSDT:Link币","BCHUSDT:比特币现金","ETCUSDT:以太经典"]
     * code : 200
     * quoteDomain : 39DE33DDE9FA12F1B9D37C631D6308B3D9C5C8605F7BB088DE537B3058EFE38C
     * androidUrl : https://download.wanzhuan7h.com/app.apk
     * time : 1590475730
     * iosUrl : https://itunes.apple.com/cn/app/id1360236743
     * message :
     * brand : {"activityDays":1,"androidUrl":"https://download.wanzhuan7h.com/app.apk","code":"BYD","defaultCurrency":"USDT","defer":true,"eagleDefer":0,"eagleTrade":1,"exchangeRatio":0,"geetest":true,"iosUrl":"https://itunes.apple.com/cn/app/id1360236743","logoUrl":"https://www.bityard.com/src/byd_logo.png","nameAuth":null,"supportCurrency":"USDT,BTC,ETH,XRP,HT,TRX,LINK","valid":true,"withdrawHour1":9,"withdrawHour2":24,"withdrawInterval":10,"withdrawWeek1":1,"withdrawWeek2":7}
     * group : [{"name":"数字货币","list":"TRXUSDT;LTCUSDT;EOSUSDT;XRPUSDT;ETHUSDT;DASHUSDT;BTCUSDT;CL;LINKUSDT;BCHUSDT;ETCUSDT"}]
     */



    private int code;
    private String quoteDomain;
    private String androidUrl;
    private String time;
    private String iosUrl;
    private String message;
    private BrandBean brand;
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
                ", brand=" + brand +
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

    public BrandBean getBrand() {
        return brand;
    }

    public void setBrand(BrandBean brand) {
        this.brand = brand;
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

    public static class BrandBean {
        @Override
        public String toString() {
            return "BrandBean{" +
                    "activityDays=" + activityDays +
                    ", androidUrl='" + androidUrl + '\'' +
                    ", code='" + code + '\'' +
                    ", defaultCurrency='" + defaultCurrency + '\'' +
                    ", defer=" + defer +
                    ", eagleDefer=" + eagleDefer +
                    ", eagleTrade=" + eagleTrade +
                    ", exchangeRatio=" + exchangeRatio +
                    ", geetest=" + geetest +
                    ", iosUrl='" + iosUrl + '\'' +
                    ", logoUrl='" + logoUrl + '\'' +
                    ", nameAuth=" + nameAuth +
                    ", supportCurrency='" + supportCurrency + '\'' +
                    ", valid=" + valid +
                    ", withdrawHour1=" + withdrawHour1 +
                    ", withdrawHour2=" + withdrawHour2 +
                    ", withdrawInterval=" + withdrawInterval +
                    ", withdrawWeek1=" + withdrawWeek1 +
                    ", withdrawWeek2=" + withdrawWeek2 +
                    '}';
        }

        /**
         * activityDays : 1
         * androidUrl : https://download.wanzhuan7h.com/app.apk
         * code : BYD
         * defaultCurrency : USDT
         * defer : true
         * eagleDefer : 0
         * eagleTrade : 1
         * exchangeRatio : 0
         * geetest : true
         * iosUrl : https://itunes.apple.com/cn/app/id1360236743
         * logoUrl : https://www.bityard.com/src/byd_logo.png
         * nameAuth : null
         * supportCurrency : USDT,BTC,ETH,XRP,HT,TRX,LINK
         * valid : true
         * withdrawHour1 : 9
         * withdrawHour2 : 24
         * withdrawInterval : 10
         * withdrawWeek1 : 1
         * withdrawWeek2 : 7
         */



        private int activityDays;
        private String androidUrl;
        private String code;
        private String defaultCurrency;
        private boolean defer;
        private int eagleDefer;
        private int eagleTrade;
        private double exchangeRatio;
        private boolean geetest;
        private String iosUrl;
        private String logoUrl;
        private Object nameAuth;
        private String prizeTrade;
        private String supportCurrency;
        private boolean valid;
        private int withdrawHour1;
        private int withdrawHour2;
        private int withdrawInterval;
        private int withdrawWeek1;
        private int withdrawWeek2;

        public int getActivityDays() {
            return activityDays;
        }

        public void setActivityDays(int activityDays) {
            this.activityDays = activityDays;
        }

        public String getAndroidUrl() {
            return androidUrl;
        }

        public void setAndroidUrl(String androidUrl) {
            this.androidUrl = androidUrl;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDefaultCurrency() {
            return defaultCurrency;
        }

        public void setDefaultCurrency(String defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
        }

        public boolean isDefer() {
            return defer;
        }

        public void setDefer(boolean defer) {
            this.defer = defer;
        }

        public int getEagleDefer() {
            return eagleDefer;
        }

        public void setEagleDefer(int eagleDefer) {
            this.eagleDefer = eagleDefer;
        }

        public int getEagleTrade() {
            return eagleTrade;
        }

        public void setEagleTrade(int eagleTrade) {
            this.eagleTrade = eagleTrade;
        }

        public double getExchangeRatio() {
            return exchangeRatio;
        }

        public void setExchangeRatio(double exchangeRatio) {
            this.exchangeRatio = exchangeRatio;
        }

        public boolean isGeetest() {
            return geetest;
        }

        public void setGeetest(boolean geetest) {
            this.geetest = geetest;
        }

        public String getIosUrl() {
            return iosUrl;
        }

        public void setIosUrl(String iosUrl) {
            this.iosUrl = iosUrl;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getPrizeTrade() {
            return prizeTrade;
        }

        public void setPrizeTrade(String prizeTrade) {
            this.prizeTrade = prizeTrade;
        }

        public Object getNameAuth() {
            return nameAuth;
        }

        public void setNameAuth(Object nameAuth) {
            this.nameAuth = nameAuth;
        }

        public String getSupportCurrency() {
            return supportCurrency;
        }

        public void setSupportCurrency(String supportCurrency) {
            this.supportCurrency = supportCurrency;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public int getWithdrawHour1() {
            return withdrawHour1;
        }

        public void setWithdrawHour1(int withdrawHour1) {
            this.withdrawHour1 = withdrawHour1;
        }

        public int getWithdrawHour2() {
            return withdrawHour2;
        }

        public void setWithdrawHour2(int withdrawHour2) {
            this.withdrawHour2 = withdrawHour2;
        }

        public int getWithdrawInterval() {
            return withdrawInterval;
        }

        public void setWithdrawInterval(int withdrawInterval) {
            this.withdrawInterval = withdrawInterval;
        }

        public int getWithdrawWeek1() {
            return withdrawWeek1;
        }

        public void setWithdrawWeek1(int withdrawWeek1) {
            this.withdrawWeek1 = withdrawWeek1;
        }

        public int getWithdrawWeek2() {
            return withdrawWeek2;
        }

        public void setWithdrawWeek2(int withdrawWeek2) {
            this.withdrawWeek2 = withdrawWeek2;
        }
    }

    public static class GroupBean {
        @Override
        public String toString() {
            return "GroupBean{" +
                    "name='" + name + '\'' +
                    ", list='" + list + '\'' +
                    '}';
        }

        /**
         * name : 数字货币
         * list : TRXUSDT;LTCUSDT;EOSUSDT;XRPUSDT;ETHUSDT;DASHUSDT;BTCUSDT;CL;LINKUSDT;BCHUSDT;ETCUSDT
         */



        private String name;
        private String list;

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
