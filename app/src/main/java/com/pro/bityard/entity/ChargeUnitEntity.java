package com.pro.bityard.entity;

import java.util.List;

public class ChargeUnitEntity {


    /**
     * chargeCoinList : [-3,1.5]
     * chargeOriginal : -3
     * chargeUnit : -3
     * chargeUnitList : [-3,1.5]
     * coins : true
     * discountType : 0
     * discountVal : 0
     */
    private String code;
    private int chargeOriginal;
    private int chargeUnit;
    private boolean coins;
    private int discountType;
    private int discountVal;
    private List<Double> chargeCoinList;
    private List<Double> chargeUnitList;


    @Override
    public String toString() {
        return "ChargeUnitEntity{" +
                "name='" + code + '\'' +
                ", chargeOriginal=" + chargeOriginal +
                ", chargeUnit=" + chargeUnit +
                ", coins=" + coins +
                ", discountType=" + discountType +
                ", discountVal=" + discountVal +
                ", chargeCoinList=" + chargeCoinList +
                ", chargeUnitList=" + chargeUnitList +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public int getChargeOriginal() {
        return chargeOriginal;
    }

    public void setChargeOriginal(int chargeOriginal) {
        this.chargeOriginal = chargeOriginal;
    }

    public int getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(int chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public boolean isCoins() {
        return coins;
    }

    public void setCoins(boolean coins) {
        this.coins = coins;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getDiscountVal() {
        return discountVal;
    }

    public void setDiscountVal(int discountVal) {
        this.discountVal = discountVal;
    }

    public List<Double> getChargeCoinList() {
        return chargeCoinList;
    }

    public void setChargeCoinList(List<Double> chargeCoinList) {
        this.chargeCoinList = chargeCoinList;
    }

    public List<Double> getChargeUnitList() {
        return chargeUnitList;
    }

    public void setChargeUnitList(List<Double> chargeUnitList) {
        this.chargeUnitList = chargeUnitList;
    }
}
