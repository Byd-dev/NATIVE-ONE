package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.pro.bityard.entity.QuoteMinEntity;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class QuoteCurrentManger extends Observable {


    private static QuoteCurrentManger quoteCurrentManger;



    public static QuoteCurrentManger getInstance() {
        if (quoteCurrentManger == null) {
            synchronized (QuoteCurrentManger.class) {
                if (quoteCurrentManger == null) {
                    quoteCurrentManger = new QuoteCurrentManger();
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
            WebSocketManager.getInstance().send("4001", quote_code);
        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
