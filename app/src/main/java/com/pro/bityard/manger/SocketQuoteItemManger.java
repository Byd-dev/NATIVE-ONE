package com.pro.bityard.manger;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.entity.QuoteMinEntity;

import java.util.List;
import java.util.Observable;
import java.util.Timer;

public class SocketQuoteItemManger extends Observable implements IReceiveMessage {

    private static SocketQuoteItemManger socketQuoteManger;


    public static SocketQuoteItemManger getInstance() {
        if (socketQuoteManger == null) {
            synchronized (SocketQuoteItemManger.class) {
                if (socketQuoteManger == null) {
                    socketQuoteManger = new SocketQuoteItemManger();
                }
            }

        }
        return socketQuoteManger;

    }

    private Timer mTimer;


    public void initSocket(String url) {
        WebSocketManager.getInstance().init(url, this);
    }


    public void postListQuote(ArrayMap<String, List<String>> arrayMap) {
        setChanged();
        notifyObservers(arrayMap);
    }


    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        socketQuoteManger = null;
        WebSocketManager.getInstance().close();
    }


    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectFailed() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onMessage(String text) {

        ArrayMap<String, List<String>> arrayMap = new ArrayMap<>();
        QuoteEntity quoteEntity = new Gson().fromJson(text, QuoteEntity.class);
        switch (quoteEntity.getCmid()) {
            case "4001":
                QuoteMinEntity quoteMinEntity = new Gson().fromJson(quoteEntity.getData(), QuoteMinEntity.class);
                Log.d("send", "getQuote 行情4001: " + quoteMinEntity);
                if (quoteMinEntity.getSymbol().contains("CC")) {
                    QuoteSpotCurrentManger.getInstance().postQuote(quoteMinEntity);
                } else {
                    QuoteContractCurrentManger.getInstance().postQuote(quoteMinEntity);
                }
                break;
            case "5001":
                //Log.d("send", "getQuote 现货买卖5001:  " + quoteEntity.getData().length());
                QuoteSpotManger.getInstance().postQuote(quoteEntity.getData());
                break;
            case "6001":
                //Log.d("send", "getQuote 最新成交6001:  " + quoteEntity.getData());
                TradeSpotManger.getInstance().postQuote(quoteEntity.getData());
                break;

        }


    }


}
