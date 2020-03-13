package com.pro.bityard.quote;

import java.util.List;

public class QuoteManger {

    private static QuoteManger instance;


    public static QuoteManger getInstance() {
        if (instance == null) {
            instance = new QuoteManger();
        }
        return instance;
    }

    //行情的
    private List<String> quoteList;


    public List<String> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<String> quoteList) {
        this.quoteList = quoteList;
    }
}
