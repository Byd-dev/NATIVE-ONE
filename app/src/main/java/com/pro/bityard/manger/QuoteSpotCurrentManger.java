package com.pro.bityard.manger;

import com.pro.bityard.entity.QuoteMinEntity;

import java.util.Observable;
import java.util.Timer;

public class QuoteSpotCurrentManger extends Observable {


    private static QuoteSpotCurrentManger quoteCurrentManger;


    public static QuoteSpotCurrentManger getInstance() {
        if (quoteCurrentManger == null) {
            synchronized (QuoteSpotCurrentManger.class) {
                if (quoteCurrentManger == null) {
                    quoteCurrentManger = new QuoteSpotCurrentManger();
                }
            }

        }
        return quoteCurrentManger;

    }


    public void postQuote(QuoteMinEntity data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteCurrentManger = null;
    }


    private Timer mTimer;
    private String quote_code;


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
