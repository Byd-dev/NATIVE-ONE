package com.pro.bityard.entity;

import java.util.List;

public class TradeListEntity {
    private String amClearingTime;
    private String amCloseTime;
    private String amOpenTime;
    private String amTradeTime;
    private String amWarningTime;
    private String code;
    private boolean coins;
    private String contractCode;
    private String currency;
    private boolean defer;
    private double deferFee;
    private int deferType;
    private String exchange;
    private double exgRate;
    private boolean foreign;
    private String holiday;
    private String name;
    private String niteClearingTime;
    private String niteCloseTime;
    private String niteOpenTime;
    private String niteTradeTime;
    private String niteWarningTime;
    private Object offset;
    private String pmClearingTime;
    private String pmCloseTime;
    private String pmOpenTime;
    private String pmTradeTime;
    private String pmWarningTime;
    private double priceChange;
    private int priceDigit;
    private double priceOriginal;
    private double priceUnit;
    private boolean range;
    private String remark;
    private int spread;
    private String type;
    private boolean valid;

    private double maxHoldAll;
    private double maxHoldOne;
    private double maxHoldWay;
    private double volumeMin;



    private List<Long> closeTime;
    private List<Integer> depositList;
    private List<Integer> leverList;
    private List<Integer> leverShowList;
    private List<Integer> moneyTypeList;
    private List<Double> priceRateList;
    private List<Double> stopLossList;
    private List<Double> stopProfitList;
    private List<Double> volumeList;

    @Override
    public String toString() {
        return "TradeListEntity{" +
                "amClearingTime='" + amClearingTime + '\'' +
                ", amCloseTime='" + amCloseTime + '\'' +
                ", amOpenTime='" + amOpenTime + '\'' +
                ", amTradeTime='" + amTradeTime + '\'' +
                ", amWarningTime='" + amWarningTime + '\'' +
                ", code='" + code + '\'' +
                ", coins=" + coins +
                ", contractCode='" + contractCode + '\'' +
                ", currency='" + currency + '\'' +
                ", defer=" + defer +
                ", deferFee=" + deferFee +
                ", deferType=" + deferType +
                ", exchange='" + exchange + '\'' +
                ", exgRate=" + exgRate +
                ", foreign=" + foreign +
                ", holiday='" + holiday + '\'' +
                ", name='" + name + '\'' +
                ", niteClearingTime='" + niteClearingTime + '\'' +
                ", niteCloseTime='" + niteCloseTime + '\'' +
                ", niteOpenTime='" + niteOpenTime + '\'' +
                ", niteTradeTime='" + niteTradeTime + '\'' +
                ", niteWarningTime='" + niteWarningTime + '\'' +
                ", offset=" + offset +
                ", pmClearingTime='" + pmClearingTime + '\'' +
                ", pmCloseTime='" + pmCloseTime + '\'' +
                ", pmOpenTime='" + pmOpenTime + '\'' +
                ", pmTradeTime='" + pmTradeTime + '\'' +
                ", pmWarningTime='" + pmWarningTime + '\'' +
                ", priceChange=" + priceChange +
                ", priceDigit=" + priceDigit +
                ", priceOriginal=" + priceOriginal +
                ", priceUnit=" + priceUnit +
                ", range=" + range +
                ", remark='" + remark + '\'' +
                ", spread=" + spread +
                ", type='" + type + '\'' +
                ", valid=" + valid +
                ", maxHoldAll=" + maxHoldAll +
                ", maxHoldOne=" + maxHoldOne +
                ", maxHoldWay=" + maxHoldWay +
                ", volumeMin=" + volumeMin +
                ", closeTime=" + closeTime +
                ", depositList=" + depositList +
                ", leverList=" + leverList +
                ", leverShowList=" + leverShowList +
                ", moneyTypeList=" + moneyTypeList +
                ", priceRateList=" + priceRateList +
                ", stopLossList=" + stopLossList +
                ", stopProfitList=" + stopProfitList +
                ", volumeList=" + volumeList +
                '}';
    }

    public double getMaxHoldAll() {
        return maxHoldAll;
    }

    public void setMaxHoldAll(double maxHoldAll) {
        this.maxHoldAll = maxHoldAll;
    }

    public double getMaxHoldOne() {
        return maxHoldOne;
    }

    public void setMaxHoldOne(double maxHoldOne) {
        this.maxHoldOne = maxHoldOne;
    }

    public double getMaxHoldWay() {
        return maxHoldWay;
    }

    public void setMaxHoldWay(double maxHoldWay) {
        this.maxHoldWay = maxHoldWay;
    }


    public double getVolumeMin() {
        return volumeMin;
    }

    public void setVolumeMin(double volumeMin) {
        this.volumeMin = volumeMin;
    }

    public String getAmClearingTime() {
        return amClearingTime;
    }

    public void setAmClearingTime(String amClearingTime) {
        this.amClearingTime = amClearingTime;
    }

    public String getAmCloseTime() {
        return amCloseTime;
    }

    public void setAmCloseTime(String amCloseTime) {
        this.amCloseTime = amCloseTime;
    }

    public String getAmOpenTime() {
        return amOpenTime;
    }

    public void setAmOpenTime(String amOpenTime) {
        this.amOpenTime = amOpenTime;
    }

    public String getAmTradeTime() {
        return amTradeTime;
    }

    public void setAmTradeTime(String amTradeTime) {
        this.amTradeTime = amTradeTime;
    }

    public String getAmWarningTime() {
        return amWarningTime;
    }

    public void setAmWarningTime(String amWarningTime) {
        this.amWarningTime = amWarningTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCoins() {
        return coins;
    }

    public void setCoins(boolean coins) {
        this.coins = coins;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isDefer() {
        return defer;
    }

    public void setDefer(boolean defer) {
        this.defer = defer;
    }

    public double getDeferFee() {
        return deferFee;
    }

    public void setDeferFee(double deferFee) {
        this.deferFee = deferFee;
    }

    public int getDeferType() {
        return deferType;
    }

    public void setDeferType(int deferType) {
        this.deferType = deferType;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getExgRate() {
        return exgRate;
    }

    public void setExgRate(double exgRate) {
        this.exgRate = exgRate;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNiteClearingTime() {
        return niteClearingTime;
    }

    public void setNiteClearingTime(String niteClearingTime) {
        this.niteClearingTime = niteClearingTime;
    }

    public String getNiteCloseTime() {
        return niteCloseTime;
    }

    public void setNiteCloseTime(String niteCloseTime) {
        this.niteCloseTime = niteCloseTime;
    }

    public String getNiteOpenTime() {
        return niteOpenTime;
    }

    public void setNiteOpenTime(String niteOpenTime) {
        this.niteOpenTime = niteOpenTime;
    }

    public String getNiteTradeTime() {
        return niteTradeTime;
    }

    public void setNiteTradeTime(String niteTradeTime) {
        this.niteTradeTime = niteTradeTime;
    }

    public String getNiteWarningTime() {
        return niteWarningTime;
    }

    public void setNiteWarningTime(String niteWarningTime) {
        this.niteWarningTime = niteWarningTime;
    }

    public Object getOffset() {
        return offset;
    }

    public void setOffset(Object offset) {
        this.offset = offset;
    }

    public String getPmClearingTime() {
        return pmClearingTime;
    }

    public void setPmClearingTime(String pmClearingTime) {
        this.pmClearingTime = pmClearingTime;
    }

    public String getPmCloseTime() {
        return pmCloseTime;
    }

    public void setPmCloseTime(String pmCloseTime) {
        this.pmCloseTime = pmCloseTime;
    }

    public String getPmOpenTime() {
        return pmOpenTime;
    }

    public void setPmOpenTime(String pmOpenTime) {
        this.pmOpenTime = pmOpenTime;
    }

    public String getPmTradeTime() {
        return pmTradeTime;
    }

    public void setPmTradeTime(String pmTradeTime) {
        this.pmTradeTime = pmTradeTime;
    }

    public String getPmWarningTime() {
        return pmWarningTime;
    }

    public void setPmWarningTime(String pmWarningTime) {
        this.pmWarningTime = pmWarningTime;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public int getPriceDigit() {
        return priceDigit;
    }

    public void setPriceDigit(int priceDigit) {
        this.priceDigit = priceDigit;
    }

    public double getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(double priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSpread() {
        return spread;
    }

    public void setSpread(int spread) {
        this.spread = spread;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<Long> getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(List<Long> closeTime) {
        this.closeTime = closeTime;
    }

    public List<Integer> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<Integer> depositList) {
        this.depositList = depositList;
    }

    public List<Integer> getLeverList() {
        return leverList;
    }

    public void setLeverList(List<Integer> leverList) {
        this.leverList = leverList;
    }

    public List<Integer> getLeverShowList() {
        return leverShowList;
    }

    public void setLeverShowList(List<Integer> leverShowList) {
        this.leverShowList = leverShowList;
    }

    public List<Integer> getMoneyTypeList() {
        return moneyTypeList;
    }

    public void setMoneyTypeList(List<Integer> moneyTypeList) {
        this.moneyTypeList = moneyTypeList;
    }

    public List<Double> getPriceRateList() {
        return priceRateList;
    }

    public void setPriceRateList(List<Double> priceRateList) {
        this.priceRateList = priceRateList;
    }

    public List<Double> getStopLossList() {
        return stopLossList;
    }

    public void setStopLossList(List<Double> stopLossList) {
        this.stopLossList = stopLossList;
    }

    public List<Double> getStopProfitList() {
        return stopProfitList;
    }

    public void setStopProfitList(List<Double> stopProfitList) {
        this.stopProfitList = stopProfitList;
    }

     public List<Double> getVolumeList() {
        return volumeList;
    }

    public void setVolumeList(List<Double> volumeList) {
        this.volumeList = volumeList;
    }
}
