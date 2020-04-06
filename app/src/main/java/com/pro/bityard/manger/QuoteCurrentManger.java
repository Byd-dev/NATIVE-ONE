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

public class QuoteCurrentManger extends Observable {


    private static QuoteCurrentManger quoteCurrentManger;

    private String code;
    private String resolution;


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

    private Timer mTimer;

    public void startScheduleJob(long delay, long interval, String code,String resolution) {
        this.code = code;
        this.resolution=resolution;
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
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            if (quote_host == null) {
                NetManger.getInstance().initQuote();
                return;
            } else {
                quote(quote_host, code,resolution);
            }
        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void quote(String quote_host, String quote_code,String resolution) {


        if (quote_host == null && quote_code == null) {
            NetManger.getInstance().initQuote();
        } else {
            NetManger.getInstance().getQuoteChart(quote_host, "/quota.jsp", quote_code,resolution, new OnNetResult() {
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
        quoteCurrentManger = null;
    }


}
