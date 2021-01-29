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
    private String quote_code;

    public void startScheduleJob(long delay, long interval, String quote_code) {
        this.quote_code = quote_code;

        if (mTimer != null) cancelTimer();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }, delay, interval);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WebSocketManager.getInstance().send("6001", quote_code);
        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
