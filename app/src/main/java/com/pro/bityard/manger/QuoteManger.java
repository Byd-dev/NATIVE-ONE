package com.pro.bityard.manger;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuoteManger extends Observable {


    private static QuoteManger quoteManger;


    public static QuoteManger getInstance() {
        if (quoteManger == null) {
            synchronized (QuoteManger.class) {
                if (quoteManger == null) {
                    quoteManger = new QuoteManger();
                }
            }

        }
        return quoteManger;

    }

    private Timer mTimer;

    public void startScheduleJob(long delay, long interval) {
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE);
            if (quote_host.equals("") && quote_code.equals("")) {
                NetManger.getInstance().initQuote();
                return;
            } else {
                quote(quote_host, quote_code);
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

        if (quote_host.equals("") && quote_code.equals("")) {
            NetManger.getInstance().initQuote();
        } else {
            NetManger.getInstance().getQuote(quote_host, "/quote.jsp", quote_code, new OnNetResult() {
                @Override
                public void onNetResult(String state, Object response) {
                    if (state.equals(BUSY)) {

                    } else if (state.equals(SUCCESS)) {
                        String jsonReplace = Util.jsonReplace(response.toString());
                        QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                        String data = quoteEntity.getData();
                        List<String> strings = Util.quoteResult(data);
                        postMessage(strings);

                    } else if (state.equals(FAILURE)) {

                    }
                }
            });

        }

    }

    public void postMessage(Object data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteManger = null;
    }


}
