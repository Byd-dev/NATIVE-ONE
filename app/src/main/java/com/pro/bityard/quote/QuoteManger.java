package com.pro.bityard.quote;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.MainActivity;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.fragment.tab.HomeFragment;
import com.pro.bityard.fragment.tab.MarketFragment;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuoteManger implements Observable {


    private static QuoteManger instance;


    public static QuoteManger getInstance() {
        if (instance == null) {
            instance = new QuoteManger();
        }
        return instance;
    }

    private List<String> quoteList;

    public List<String> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<String> quoteList) {
        this.quoteList = quoteList;
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

        Observable observable = new QuoteManger();
        Observer observer = new MarketFragment();
        Observer observer2 = new HomeFragment();
        observable.add(observer);
        observable.add(observer2);

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
                        observable.notifyQuote(data);
                        List<String> strings = Util.quoteResult(data);
                        setQuoteList(strings);

                    } else if (state.equals(FAILURE)) {

                    }
                }
            });

        }

    }

    private List<Observer> personList = new ArrayList<Observer>();

    @Override
    public void add(Observer observer) {
        personList.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        personList.remove(observer);

    }

    @Override
    public void notifyQuote(String message) {
        for (Observer observer : personList) {
            observer.update(message);
        }
    }
}
