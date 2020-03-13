package com.pro.bityard.quote;

public interface Observable {

    void add(Observer observer);//添加观察者

    void remove(Observer observer);//删除观察者

    void notifyQuote(String quote);//通知观察者


}
