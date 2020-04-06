package com.pro.bityard.entity;

import java.util.List;

public class QuoteChartEntity {


    /**
     * s : ok
     * t : [1586158080,1586158140,1586158200,1586158260,1586158320,1586158380,1586158440,1586158380,1586158440,1586158500]
     * c : [7035.57,7024.66,7015.18,7017.35,7022.28,7022.97,7023.13,7023.01,7019.56,7019.67]
     * o : [7035.57,7034.36,7024.78,7015.26,7016.63,7022.52,7023.13,7023.01,7023.13,7019.64]
     * h : [7035.57,7036.38,7024.82,7018.79,7024.52,7023.25,7023.13,7023.01,7023.37,7024.58]
     * l : [7035.57,7023.43,7012.75,7014.28,7016.63,7018.98,7023.13,7023.01,7019.14,7019.64]
     * v : [60501,30150998,31707387,34308905,24943571,29795773,61074,61083,25469518,12155711]
     */

    private String s;
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
