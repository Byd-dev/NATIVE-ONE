package com.pro.bityard.entity;

import java.util.List;

public class FollowHistoryEntity {


    @Override
    public String toString() {
        return "FollowHistoryEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * total : 53
     * code : 200
     * data : [{"closeSource":"自主平仓","commodity":"ZRX币","commodityCode":"ZRXUSDT","commodityType":"FT","contCode":"ZRXUSDT1808","contract":"ZRXUSDT1808","contractCode":"ZRXUSDT1808","contractQuote":"ZRXUSDT1808","contractType":"DAY","cpPrice":0.4059,"cpVolume":500.6258,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.09,"eagleDeduction":0,"followId":"0","id":"523803017297149952","income":3.20400512,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":10,"marginPrize":0,"moneyType":0,"opPrice":0.3995,"opVolume":500.6258,"openSource":"自主下单","orVolume":0,"price":0.3995,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.2,"shared":true,"stopLoss":-5,"stopLossBegin":-9,"stopProfit":30,"stopProfitBegin":30,"time":1602813971000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602817855000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":500.6258},{"closeSource":"自主平仓","commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","contractQuote":"BTCUSDT1808","contractType":"DAY","cpPrice":11331.25,"cpVolume":0.0441,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.225,"eagleDeduction":0,"followId":"0","id":"523561536242204672","income":-0.328545,"investUserId":"0","investUsername":null,"isBuy":true,"lever":5,"luckyDeduction":0,"margin":100,"marginPrize":0,"moneyType":0,"opPrice":11338.7,"opVolume":0.0441,"openSource":"自主下单","orVolume":0,"price":11338.7,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.5,"shared":true,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1602756398000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602756494000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":0.0441},{"closeSource":"自主平仓","commodity":"DOT币","commodityCode":"DOTUSDT","commodityType":"FT","contCode":"DOTUSDT1808","contract":"DOTUSDT1808","contractCode":"DOTUSDT1808","contractQuote":"DOTUSDT1808","contractType":"DAY","cpPrice":4.1102,"cpVolume":24.1354,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523535546132611072","income":-0.79888174,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":4.1433,"opVolume":24.1354,"openSource":"自主下单","orVolume":0,"price":4.1433,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602750201000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602751591000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":24.1354},{"closeSource":"自主平仓","commodity":"DOT币","commodityCode":"DOTUSDT","commodityType":"FT","contCode":"DOTUSDT1808","contract":"DOTUSDT1808","contractCode":"DOTUSDT1808","contractQuote":"DOTUSDT1808","contractType":"DAY","cpPrice":4.1102,"cpVolume":24.1441,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523535399847870464","income":-0.76295356,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":4.1418,"opVolume":24.1441,"openSource":"自主下单","orVolume":0,"price":4.1418,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602750166000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602751591000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":24.1441},{"closeSource":"自主平仓","commodity":"DOT币","commodityCode":"DOTUSDT","commodityType":"FT","contCode":"DOTUSDT1808","contract":"DOTUSDT1808","contractCode":"DOTUSDT1808","contractQuote":"DOTUSDT1808","contractType":"DAY","cpPrice":4.1431,"cpVolume":24.1121,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534971332608000","income":-0.10127082,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":4.1473,"opVolume":24.1121,"openSource":"自主下单","orVolume":0,"price":4.1473,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602750064000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":24.1121},{"closeSource":"自主平仓","commodity":"DOT币","commodityCode":"DOTUSDT","commodityType":"FT","contCode":"DOTUSDT1808","contract":"DOTUSDT1808","contractCode":"DOTUSDT1808","contractQuote":"DOTUSDT1808","contractType":"DAY","cpPrice":4.143,"cpVolume":24.09,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534748870918144","income":-0.195129,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":4.1511,"opVolume":24.09,"openSource":"自主下单","orVolume":0,"price":4.1511,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602750011000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":24.09},{"closeSource":"自主平仓","commodity":"DOT币","commodityCode":"DOTUSDT","commodityType":"FT","contCode":"DOTUSDT1808","contract":"DOTUSDT1808","contractCode":"DOTUSDT1808","contractQuote":"DOTUSDT1808","contractType":"DAY","cpPrice":4.1431,"cpVolume":24.0987,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534603366318080","income":-0.15664155,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":4.1496,"opVolume":24.0987,"openSource":"自主下单","orVolume":0,"price":4.1496,"priceDigit":4,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602749976000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":24.0987},{"closeSource":"自主平仓","commodity":"柚子币","commodityCode":"EOSUSDT","commodityType":"FT","contCode":"EOSUSDT1808","contract":"EOSUSDT1808","contractCode":"EOSUSDT1808","contractQuote":"EOSUSDT1808","contractType":"DAY","cpPrice":2.605,"cpVolume":38.2702,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534481702141952","income":-0.3061616,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":2.613,"opVolume":38.2702,"openSource":"自主下单","orVolume":0,"price":2.613,"priceDigit":3,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602749947000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":38.2702},{"closeSource":"自主平仓","commodity":"柚子币","commodityCode":"EOSUSDT","commodityType":"FT","contCode":"EOSUSDT1808","contract":"EOSUSDT1808","contractCode":"EOSUSDT1808","contractQuote":"EOSUSDT1808","contractType":"DAY","cpPrice":2.605,"cpVolume":38.2555,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534256207970304","income":-0.3442995,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":2.614,"opVolume":38.2555,"openSource":"自主下单","orVolume":0,"price":2.614,"priceDigit":3,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602749894000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":38.2555},{"closeSource":"自主平仓","commodity":"柚子币","commodityCode":"EOSUSDT","commodityType":"FT","contCode":"EOSUSDT1808","contract":"EOSUSDT1808","contractCode":"EOSUSDT1808","contractQuote":"EOSUSDT1808","contractType":"DAY","cpPrice":2.605,"cpVolume":38.2409,"currency":"USDT","deferDays":0,"deferEagle":0,"deferFee":0.04,"eagleDeduction":0,"followId":"0","id":"523534109352804352","income":-0.382409,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"luckyDeduction":0,"margin":5,"marginPrize":0,"moneyType":0,"opPrice":2.615,"opVolume":38.2409,"openSource":"自主下单","orVolume":0,"price":2.615,"priceDigit":3,"priceRate":100,"priceVolume":0,"serviceCharge":0.1,"shared":true,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1602749859000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1602750129000,"traderId":"0","traderMoney":0,"traderRatio":0,"traderUsername":null,"volume":38.2409}]
     * size : 10
     * page : 1
     * message :
     */

    private String total;
    private double code;
    private double size;
    private double page;
    private String message;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
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
                    "closeSource='" + closeSource + '\'' +
                    ", commodity='" + commodity + '\'' +
                    ", commodityCode='" + commodityCode + '\'' +
                    ", commodityType='" + commodityType + '\'' +
                    ", contCode='" + contCode + '\'' +
                    ", contract='" + contract + '\'' +
                    ", contractCode='" + contractCode + '\'' +
                    ", contractQuote='" + contractQuote + '\'' +
                    ", contractType='" + contractType + '\'' +
                    ", cpPrice=" + cpPrice +
                    ", cpVolume=" + cpVolume +
                    ", currency='" + currency + '\'' +
                    ", deferDays=" + deferDays +
                    ", deferEagle=" + deferEagle +
                    ", deferFee=" + deferFee +
                    ", eagleDeduction=" + eagleDeduction +
                    ", followId='" + followId + '\'' +
                    ", id='" + id + '\'' +
                    ", income=" + income +
                    ", investUserId='" + investUserId + '\'' +
                    ", investUsername=" + investUsername +
                    ", isBuy=" + isBuy +
                    ", lever=" + lever +
                    ", luckyDeduction=" + luckyDeduction +
                    ", margin=" + margin +
                    ", marginPrize=" + marginPrize +
                    ", moneyType=" + moneyType +
                    ", opPrice=" + opPrice +
                    ", opVolume=" + opVolume +
                    ", openSource='" + openSource + '\'' +
                    ", orVolume=" + orVolume +
                    ", price=" + price +
                    ", priceDigit=" + priceDigit +
                    ", priceRate=" + priceRate +
                    ", priceVolume=" + priceVolume +
                    ", serviceCharge=" + serviceCharge +
                    ", shared=" + shared +
                    ", stopLoss=" + stopLoss +
                    ", stopLossBegin=" + stopLossBegin +
                    ", stopProfit=" + stopProfit +
                    ", stopProfitBegin=" + stopProfitBegin +
                    ", time=" + time +
                    ", tradeMode='" + tradeMode + '\'' +
                    ", tradeMsg='" + tradeMsg + '\'' +
                    ", tradeStatus=" + tradeStatus +
                    ", tradeTime=" + tradeTime +
                    ", traderId='" + traderId + '\'' +
                    ", traderMoney=" + traderMoney +
                    ", traderRatio=" + traderRatio +
                    ", traderUsername=" + traderUsername +
                    ", volume=" + volume +
                    '}';
        }

        /**
         * closeSource : 自主平仓
         * commodity : ZRX币
         * commodityCode : ZRXUSDT
         * commodityType : FT
         * contCode : ZRXUSDT1808
         * contract : ZRXUSDT1808
         * contractCode : ZRXUSDT1808
         * contractQuote : ZRXUSDT1808
         * contractType : DAY
         * cpPrice : 0.4059
         * cpVolume : 500.6258
         * currency : USDT
         * deferDays : 0
         * deferEagle : 0
         * deferFee : 0.09
         * eagleDeduction : 0
         * followId : 0
         * id : 523803017297149952
         * income : 3.20400512
         * investUserId : 0
         * investUsername : null
         * isBuy : true
         * lever : 20
         * luckyDeduction : 0
         * margin : 10
         * marginPrize : 0
         * moneyType : 0
         * opPrice : 0.3995
         * opVolume : 500.6258
         * openSource : 自主下单
         * orVolume : 0
         * price : 0.3995
         * priceDigit : 4
         * priceRate : 100
         * priceVolume : 0
         * serviceCharge : 0.2
         * shared : true
         * stopLoss : -5
         * stopLossBegin : -9
         * stopProfit : 30
         * stopProfitBegin : 30
         * time : 1602813971000
         * tradeMode : T0D
         * tradeMsg :
         * tradeStatus : 14
         * tradeTime : 1602817855000
         * traderId : 0
         * traderMoney : 0
         * traderRatio : 0
         * traderUsername : null
         * volume : 500.6258
         */


        private String closeSource;
        private String commodity;
        private String commodityCode;
        private String commodityType;
        private String contCode;
        private String contract;
        private String contractCode;
        private String contractQuote;
        private String contractType;
        private double cpPrice;
        private double cpVolume;
        private String currency;
        private double deferDays;
        private double deferEagle;
        private double deferFee;
        private double eagleDeduction;
        private String followId;
        private String id;
        private double income;
        private String investUserId;
        private Object investUsername;
        private boolean isBuy;
        private double lever;
        private double luckyDeduction;
        private double margin;
        private double marginPrize;
        private double moneyType;
        private double opPrice;
        private double opVolume;
        private String openSource;
        private double orVolume;
        private double price;
        private double priceDigit;
        private double priceRate;
        private double priceVolume;
        private double serviceCharge;
        private boolean shared;
        private double stopLoss;
        private double stopLossBegin;
        private double stopProfit;
        private double stopProfitBegin;
        private long time;
        private String tradeMode;
        private String tradeMsg;
        private double tradeStatus;
        private long tradeTime;
        private String traderId;
        private double traderMoney;
        private double traderRatio;
        private Object traderUsername;
        private double volume;

        public String getCloseSource() {
            return closeSource;
        }

        public void setCloseSource(String closeSource) {
            this.closeSource = closeSource;
        }

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public String getCommodityCode() {
            return commodityCode;
        }

        public void setCommodityCode(String commodityCode) {
            this.commodityCode = commodityCode;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public String getContCode() {
            return contCode;
        }

        public void setContCode(String contCode) {
            this.contCode = contCode;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public String getContractQuote() {
            return contractQuote;
        }

        public void setContractQuote(String contractQuote) {
            this.contractQuote = contractQuote;
        }

        public String getContractType() {
            return contractType;
        }

        public void setContractType(String contractType) {
            this.contractType = contractType;
        }

        public double getCpPrice() {
            return cpPrice;
        }

        public void setCpPrice(double cpPrice) {
            this.cpPrice = cpPrice;
        }

        public double getCpVolume() {
            return cpVolume;
        }

        public void setCpVolume(double cpVolume) {
            this.cpVolume = cpVolume;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getDeferDays() {
            return deferDays;
        }

        public void setDeferDays(double deferDays) {
            this.deferDays = deferDays;
        }

        public double getDeferEagle() {
            return deferEagle;
        }

        public void setDeferEagle(double deferEagle) {
            this.deferEagle = deferEagle;
        }

        public double getDeferFee() {
            return deferFee;
        }

        public void setDeferFee(double deferFee) {
            this.deferFee = deferFee;
        }

        public double getEagleDeduction() {
            return eagleDeduction;
        }

        public void setEagleDeduction(double eagleDeduction) {
            this.eagleDeduction = eagleDeduction;
        }

        public String getFollowId() {
            return followId;
        }

        public void setFollowId(String followId) {
            this.followId = followId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public String getInvestUserId() {
            return investUserId;
        }

        public void setInvestUserId(String investUserId) {
            this.investUserId = investUserId;
        }

        public Object getInvestUsername() {
            return investUsername;
        }

        public void setInvestUsername(Object investUsername) {
            this.investUsername = investUsername;
        }

        public boolean isIsBuy() {
            return isBuy;
        }

        public void setIsBuy(boolean isBuy) {
            this.isBuy = isBuy;
        }

        public double getLever() {
            return lever;
        }

        public void setLever(double lever) {
            this.lever = lever;
        }

        public double getLuckyDeduction() {
            return luckyDeduction;
        }

        public void setLuckyDeduction(double luckyDeduction) {
            this.luckyDeduction = luckyDeduction;
        }

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public double getMarginPrize() {
            return marginPrize;
        }

        public void setMarginPrize(double marginPrize) {
            this.marginPrize = marginPrize;
        }

        public double getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(double moneyType) {
            this.moneyType = moneyType;
        }

        public double getOpPrice() {
            return opPrice;
        }

        public void setOpPrice(double opPrice) {
            this.opPrice = opPrice;
        }

        public double getOpVolume() {
            return opVolume;
        }

        public void setOpVolume(double opVolume) {
            this.opVolume = opVolume;
        }

        public String getOpenSource() {
            return openSource;
        }

        public void setOpenSource(String openSource) {
            this.openSource = openSource;
        }

        public double getOrVolume() {
            return orVolume;
        }

        public void setOrVolume(double orVolume) {
            this.orVolume = orVolume;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPriceDigit() {
            return priceDigit;
        }

        public void setPriceDigit(double priceDigit) {
            this.priceDigit = priceDigit;
        }

        public double getPriceRate() {
            return priceRate;
        }

        public void setPriceRate(double priceRate) {
            this.priceRate = priceRate;
        }

        public double getPriceVolume() {
            return priceVolume;
        }

        public void setPriceVolume(double priceVolume) {
            this.priceVolume = priceVolume;
        }

        public double getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(double serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public boolean isShared() {
            return shared;
        }

        public void setShared(boolean shared) {
            this.shared = shared;
        }

        public double getStopLoss() {
            return stopLoss;
        }

        public void setStopLoss(double stopLoss) {
            this.stopLoss = stopLoss;
        }

        public double getStopLossBegin() {
            return stopLossBegin;
        }

        public void setStopLossBegin(double stopLossBegin) {
            this.stopLossBegin = stopLossBegin;
        }

        public double getStopProfit() {
            return stopProfit;
        }

        public void setStopProfit(double stopProfit) {
            this.stopProfit = stopProfit;
        }

        public double getStopProfitBegin() {
            return stopProfitBegin;
        }

        public void setStopProfitBegin(double stopProfitBegin) {
            this.stopProfitBegin = stopProfitBegin;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTradeMode() {
            return tradeMode;
        }

        public void setTradeMode(String tradeMode) {
            this.tradeMode = tradeMode;
        }

        public String getTradeMsg() {
            return tradeMsg;
        }

        public void setTradeMsg(String tradeMsg) {
            this.tradeMsg = tradeMsg;
        }

        public double getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(double tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getTraderId() {
            return traderId;
        }

        public void setTraderId(String traderId) {
            this.traderId = traderId;
        }

        public double getTraderMoney() {
            return traderMoney;
        }

        public void setTraderMoney(double traderMoney) {
            this.traderMoney = traderMoney;
        }

        public double getTraderRatio() {
            return traderRatio;
        }

        public void setTraderRatio(double traderRatio) {
            this.traderRatio = traderRatio;
        }

        public Object getTraderUsername() {
            return traderUsername;
        }

        public void setTraderUsername(Object traderUsername) {
            this.traderUsername = traderUsername;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }
    }
}
