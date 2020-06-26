package com.pro.bityard.entity;

import com.pro.bityard.utils.TradeUtil;

import java.util.List;

public class HistoryEntity {


    private double code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "HistoryEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
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
        /**
         * commodity : 达世币
         * commodityCode : DASHUSDT
         * commodityType : FT
         * contCode : DASHUSDT1808
         * contract : DASHUSDT1808
         * contractCode : DASHUSDT1808
         * cpPrice : 67.4
         * cpVolume : 1.4846
         * currency : USDT
         * deferDays : 0
         * deferFee : 0
         * eagleDeduction : 0
         * followId : 0
         * id : 447865080147607552
         * income : -0.059384
         * investUserId : 0
         * investUsername : null
         * isBuy : false
         * lever : 20
         * margin : 5
         * moneyType : 0
         * opPrice : 67.36
         * opVolume : 1.4846
         * orVolume : 0
         * price : 67.36
         * priceDigit : 2
         * priceRate : 100
         * priceVolume : 0
         * serviceCharge : 0.1425
         * shared : false
         * stopLoss : -4.5
         * stopLossBegin : -4.5
         * stopProfit : 15
         * stopProfitBegin : 15
         * time : 1584708956000
         * tradeMode : T0
         * tradeMsg :
         * tradeStatus : 14
         * tradeTime : 1584708962000
         * volume : 1.4846
         */

        private String commodity;
        private String commodityCode;
        private String commodityType;
        private String contCode;
        private String contract;
        private String contractCode;
        private double cpPrice;
        private double cpVolume;
        private String currency;
        private String deferDays;
        private String deferFee;
        private double eagleDeduction;
        private String followId;
        private String id;
        private double income;
        private String investUserId;
        private Object investUsername;
        private boolean isBuy;
        private double lever;
        private double margin;
        private double moneyType;
        private double opPrice;
        private double opVolume;
        private double orVolume;
        private double price;
        private int priceDigit;
        private double priceRate;
        private double priceVolume;
        private double luckyDeduction;
        private double marginPrize;
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
        private double volume;


        @Override
        public String toString() {
            return "DataBean{" +
                    "commodity='" + commodity + '\'' +
                    ", commodityCode='" + commodityCode + '\'' +
                    ", commodityType='" + commodityType + '\'' +
                    ", contCode='" + contCode + '\'' +
                    ", contract='" + contract + '\'' +
                    ", contractCode='" + contractCode + '\'' +
                    ", cpPrice=" + cpPrice +
                    ", cpVolume=" + cpVolume +
                    ", currency='" + currency + '\'' +
                    ", deferDays='" + deferDays + '\'' +
                    ", deferFee='" + deferFee + '\'' +
                    ", eagleDeduction=" + eagleDeduction +
                    ", followId='" + followId + '\'' +
                    ", id='" + id + '\'' +
                    ", income=" + income +
                    ", investUserId='" + investUserId + '\'' +
                    ", investUsername=" + investUsername +
                    ", isBuy=" + isBuy +
                    ", lever=" + lever +
                    ", margin=" + margin +
                    ", moneyType=" + moneyType +
                    ", opPrice=" + opPrice +
                    ", opVolume=" + opVolume +
                    ", orVolume=" + orVolume +
                    ", price=" + price +
                    ", priceDigit=" + priceDigit +
                    ", priceRate=" + priceRate +
                    ", priceVolume=" + priceVolume +
                    ", luckyDeduction=" + luckyDeduction +
                    ", marginPrize=" + marginPrize +
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
                    ", volume=" + volume +
                    '}';
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public double getLuckyDeduction() {
            return luckyDeduction;
        }

        public void setLuckyDeduction(double luckyDeduction) {
            this.luckyDeduction = luckyDeduction;
        }

        public double getMarginPrize() {
            return marginPrize;
        }

        public void setMarginPrize(double marginPrize) {
            this.marginPrize = marginPrize;
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

        public String getDeferDays() {

                return deferDays;

        }

        public void setDeferDays(String deferDays) {
            this.deferDays = deferDays;
        }

        public String getDeferFee() {
            if (Double.parseDouble(deferFee) == 0) {
                return "N/A";
            } else {
                return TradeUtil.getNumberFormat(Double.parseDouble(deferFee),2);

            }
        }

        public void setDeferFee(String deferFee) {
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

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
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

        public int getPriceDigit() {
            return priceDigit;
        }

        public void setPriceDigit(int priceDigit) {
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

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }
    }
}
