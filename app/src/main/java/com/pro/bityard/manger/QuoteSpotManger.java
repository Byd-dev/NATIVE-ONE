package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.pro.bityard.entity.QuoteMinEntity;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class QuoteSpotManger extends Observable {


    private static QuoteSpotManger quoteSpotManger;



    public static QuoteSpotManger getInstance() {
        if (quoteSpotManger == null) {
            synchronized (QuoteSpotManger.class) {
                if (quoteSpotManger == null) {
                    quoteSpotManger = new QuoteSpotManger();
                }
            }

        }
        return quoteSpotManger;

    }



    public void postQuote(String data) {
        setChanged();
        notifyObservers(data);
    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteSpotManger = null;
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
