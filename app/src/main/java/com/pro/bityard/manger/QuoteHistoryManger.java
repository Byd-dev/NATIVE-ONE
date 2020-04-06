package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteChartEntity;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuoteHistoryManger extends Observable {


    private static QuoteHistoryManger quoteHistoryManger;

    private String code;
    private String resolution;


    public static QuoteHistoryManger getInstance() {
        if (quoteHistoryManger == null) {
            synchronized (QuoteHistoryManger.class) {
                if (quoteHistoryManger == null) {
                    quoteHistoryManger = new QuoteHistoryManger();
                }
            }

        }
        return quoteHistoryManger;

    }

    private Timer mTimer;

    public void startScheduleJob(long delay, long interval, String code, String resolution) {
        this.code = code;
        this.resolution = resolution;
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
            quote(code, resolution);

        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void quote(String quote_code, String resolution) {


        NetManger.getInstance().getQuoteHistory("http://app.bityard.com", "/api/tv/tradingView/history", quote_code, resolution, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {

                    QuoteChartEntity quoteChartEntity = new Gson().fromJson(response.toString(), QuoteChartEntity.class);
                    if (quoteChartEntity.getS().equals("ok")) {

                        postQuote(quoteChartEntity);


                    }

                } else if (state.equals(FAILURE)) {
                }
            }
        });


    }

    public void postQuote(QuoteChartEntity data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteHistoryManger = null;
    }


}
