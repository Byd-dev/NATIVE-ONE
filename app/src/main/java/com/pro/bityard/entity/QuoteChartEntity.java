package com.pro.bityard.entity;

import java.util.List;

public class QuoteChartEntity {


    /**
     * s : ok
     * t : [1586750220,1586750280,1586750340,1586750400,1586750460,1586750520]
     * c : [6672.82,6675.73,6678.57,6675.91,6663.74,6662.36]
     * o : [6675.4,6672.67,6675.73,6678.56,6675.91,6664.22]
     * h : [6675.57,6676.27,6679.68,6678.83,6675.94,6667.63]
     * l : [6672.51,6667.28,6673.29,6675.32,6655,6660.83]
     * v : [14196926,102059738,76995848,40086775,284662694,46886784]
     * quote : BTCUSDT1808,6664.06,1.0,6662.36,5.0,7018.68,7018.68,7178.63,6580.69,6662.36,0.00,0.00,70512.0,0,0.0,0.0,7018.68,0.00,0.00,1586750481000,0.00,0.00,1586750481000,6662.36,5.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,6664.06,1.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0,0.00,0.0
     */

    private String s;
    private String quote;
    private List<Long> t;
    private List<Double> c;
    private List<Double> o;
    private List<Double> h;
    private List<Double> l;
    private List<Double> v;


    @Override
    public String toString() {
        return "QuoteChartEntity{" +
                "s='" + s + '\'' +
                ", quote='" + quote + '\'' +
                ", t=" + t +
                ", c=" + c +
                ", o=" + o +
                ", h=" + h +
                ", l=" + l +
                ", v=" + v +
                '}';
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public List<Long> getT() {
        return t;
    }

    public void setT(List<Long> t) {
        this.t = t;
    }

    public List<Double> getC() {
        return c;
    }

    public void setC(List<Double> c) {
        this.c = c;
    }

    public List<Double> getO() {
        return o;
    }

    public void setO(List<Double> o) {
        this.o = o;
    }

    public List<Double> getH() {
        return h;
    }

    public void setH(List<Double> h) {
        this.h = h;
    }

    public List<Double> getL() {
        return l;
    }

    public void setL(List<Double> l) {
        this.l = l;
    }

    public List<Double> getV() {
        return v;
    }

    public void setV(List<Double> v) {
        this.v = v;
    }
}
