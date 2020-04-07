package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuoteItemManger extends Observable {


    private static QuoteItemManger quoteItemManger;

    private String code;


    public static QuoteItemManger getInstance() {
        if (quoteItemManger == null) {
            synchronized (QuoteItemManger.class) {
                if (quoteItemManger == null) {
                    quoteItemManger = new QuoteItemManger();
                }
            }

        }
        return quoteItemManger;

    }

    private Timer mTimer;

    public void startScheduleJob(long delay, long interval, String code) {
        this.code = code;
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
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST,null);
            if (quote_host==null) {
                NetManger.getInstance().initQuote();
                return;
            } else {
                quote(quote_host, code);
            }
        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void quote(String quote_host, String quote_code) {


        if (quote_host==null&& quote_code==null) {
            NetManger.getInstance().initQuote();
        } else {
            NetManger.getInstance().getItemQuote(quote_host, "/quote.jsp", quote_code, (state, response) -> {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {

                    String jsonReplace = Util.jsonReplace(response.toString());
                    QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                    String data = quoteEntity.getData();
                    postQuote(data);

                } else if (state.equals(FAILURE)) {
                }
            });

        }

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
        quoteItemManger = null;
    }


}
