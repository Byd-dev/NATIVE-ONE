package com.pro.bityard.entity;

import java.util.List;

public class WithdrawCurrencyEntity {


    /**
     * code : 200
     * data : ["USDT","BTC","XLM","ETH","DASH","XRP","BCH","LRC","TRX","EOS","DOT","YFI","MKR","COMP","LINK","LTC","BAT","KNC","ZRX","OMG","REN","ANT","ENJ","MANA","ATOM","STORJ","XTZ","SAND","ONT","FIL","UNI","ALGO","BAND"]
     * message :
     */

    private Integer code;
    private String message;
    private List<String> data;

    @Override
    public String toString() {
        return "WithdrawCurrencyEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
