package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class TradeSpotManger extends Observable {


    private static TradeSpotManger tradeSpotManger;



    public static TradeSpotManger getInstance() {
        if (tradeSpotManger == null) {
            synchronized (TradeSpotManger.class) {
                if (tradeSpotManger == null) {
                    tradeSpotManger = new TradeSpotManger();
                }
            }

        }
        return tradeSpotManger;

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
        tradeSpotManger = null;
    }


    private Timer mTimer;





    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
