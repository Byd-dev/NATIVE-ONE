package com.pro.bityard.entity;

public class QuoteMinEntity {


    @Override
    public String toString() {
        return "QuoteMinEntity{" +
                "symbol='" + symbol + '\'' +
                ", s='" + s + '\'' +
                ", t=" + t +
                ", c=" + c +
                ", o=" + o +
                ", h=" + h +
                ", l=" + l +
                ", v=" + v +
                ", isUp=" + isUp +
                ", price=" + price +
                ", prev=" + prev +
                ", buyPrice=" + buyPrice +
                ", buyVolume=" + buyVolume +
                ", sellPrice=" + sellPrice +
                ", sellVolume=" + sellVolume +
                ", max=" + max +
                ", min=" + min +
                ", volume=" + volume +
                ", open=" + open +
                ", close=" + close +
                ", settle_price_yes=" + settle_price_yes +
                ", high_limit=" + high_limit +
                ", low_limit=" + low_limit +
                '}';
    }

    /**
     * symbol : XRPUSDT1808
     * s : ok
     * t : 1599126480
     * c : 0.2718
     * o : 0.2717
     * h : 0.2718
     * l : 0.2717
     * v : 201918.15833324
     * isUp : 1
     * price : 0.2718
     * prev : 0.2709
     * buyPrice : 0.2718
     * buyVolume : 114.74
     * sellPrice : 0.2719
     * sellVolume : 114.74
     * max : 0.278
     * min : 0.265
     * volume : 1.90745769E8
     * open : 0.2709
     * close : 0.2709
     * settle_price_yes : 0.0
     * high_limit : 0.0
     * low_limit : 0.0
     */



    private String symbol;
    private String s;
    private long t;
    private double c;
    private double o;
    private double h;
    private double l;
    private double v;
    private int isUp;
    private double price;
    private double prev;
    private double buyPrice;
    private double buyVolume;
    private double sellPrice;
    private double sellVolume;
    private double max;
    private double min;
    private double volume;
    private double open;
    private double close;
    private double settle_price_yes;
    private double high_limit;
    private double low_limit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public int getIsUp() {
        return isUp;
    }

    public void setIsUp(int isUp) {
        this.isUp = isUp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrev() {
        return prev;
    }

    public void setPrev(double prev) {
        this.prev = prev;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(double buyVolume) {
        this.buyVolume = buyVolume;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(double sellVolume) {
        this.sellVolume = sellVolume;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getSettle_price_yes() {
        return settle_price_yes;
    }

    public void setSettle_price_yes(double settle_price_yes) {
        this.settle_price_yes = settle_price_yes;
    }

    public double getHigh_limit() {
        return high_limit;
    }

    public void setHigh_limit(double high_limit) {
        this.high_limit = high_limit;
    }

    public double getLow_limit() {
        return low_limit;
    }

    public void setLow_limit(double low_limit) {
        this.low_limit = low_limit;
    }
}
