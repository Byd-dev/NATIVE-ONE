package com.pro.bityard.manger;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;

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

public class QuoteListManger extends Observable {


    private static QuoteListManger quoteListManger;


    public static QuoteListManger getInstance() {
        if (quoteListManger == null) {
            synchronized (QuoteListManger.class) {
                if (quoteListManger == null) {
                    quoteListManger = new QuoteListManger();
                }
            }

        }
        return quoteListManger;

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
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST,null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE,null);
            if (quote_host==null && quote_code==null) {
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

        ArrayMap<String, List<String>> arrayMap = new ArrayMap<>();

        if (quote_host==null && quote_code==null) {
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
                        //价格从高到低
                        List<String> stringList = TradeUtil.priceHighToLow(strings);
                        //价格从低到高
                        List<String> stringList1 = TradeUtil.priceLowToHigh(strings);
                        //涨跌幅从高到低
                        List<String> stringList2 = TradeUtil.rangeHighToLow(strings);
                        //涨跌幅从低到高
                        List<String> stringList3 = TradeUtil.rangeLowToHigh(strings);
                        arrayMap.put("0",strings);
                        arrayMap.put("1", stringList);
                        arrayMap.put("2", stringList1);
                        arrayMap.put("3", stringList2);
                        arrayMap.put("4", stringList3);
                        postQuote(arrayMap);

                    } else if (state.equals(FAILURE)) {

                    }
                }
            });

        }

    }

    public void postQuote(ArrayMap<String, List<String>> arrayMap) {
        setChanged();
        notifyObservers(arrayMap);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteListManger = null;
    }


}
