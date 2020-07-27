package com.pro.bityard.manger;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
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

public class QuoteCustomizeListManger extends Observable {


    private static QuoteCustomizeListManger quoteListManger;


    public static QuoteCustomizeListManger getInstance() {
        if (quoteListManger == null) {
            synchronized (QuoteCustomizeListManger.class) {
                if (quoteListManger == null) {
                    quoteListManger = new QuoteCustomizeListManger();
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
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
            if (quote_host == null && quote_code == null) {
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

        if (quote_host == null && quote_code == null) {
            NetManger.getInstance().initQuote();
        } else {
            NetManger.getInstance().getCustomizeQuote(quote_host, "/customize.jsp", quote_code, (state, response) -> {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {
                    String jsonReplace = Util.jsonReplace(response.toString());
                    QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                    String data = quoteEntity.getData();
                    List<String> strings = Util.quoteResult(data);
                    List<String> innovationQuoteList = TradeUtil.innovationQuoteList(strings);


                    //价格从高到低
                    List<String> stringList = TradeUtil.priceHighToLow(strings);
                    //价格从低到高
                    List<String> stringList1 = TradeUtil.priceLowToHigh(strings);
                    //涨跌幅从高到低
                    List<String> stringList2 = TradeUtil.rangeHighToLow(strings);
                    //涨跌幅从低到高
                    List<String> stringList3 = TradeUtil.rangeLowToHigh(strings);
                    //BTC BCH ETH
                    List<String> stringList4 = TradeUtil.homeHot(strings);
                    //除去 BTC BCH ETH
                    List<String> stringList5 = TradeUtil.homeList(strings);
                    //字母a-z
                    List<String> stringList6 = TradeUtil.nameLowToHigh(strings);
                    //字母z-a
                    List<String> stringList7 = TradeUtil.nameHighToLow(strings);


                    //价格从高到低
                    List<String> stringList8 = TradeUtil.priceHighToLow(innovationQuoteList);
                    //价格从低到高
                    List<String> stringList9 = TradeUtil.priceLowToHigh(innovationQuoteList);
                    //涨跌幅从高到低
                    List<String> stringList10 = TradeUtil.rangeHighToLow(innovationQuoteList);
                    //涨跌幅从低到高
                    List<String> stringList11 = TradeUtil.rangeLowToHigh(innovationQuoteList);
                    //字母a-z
                    List<String> stringList12 = TradeUtil.nameLowToHigh(innovationQuoteList);
                    //字母z-a
                    List<String> stringList13 = TradeUtil.nameHighToLow(innovationQuoteList);
                    arrayMap.put("all",strings);
                    arrayMap.put("0", strings);
                    arrayMap.put("1", stringList);
                    arrayMap.put("2", stringList1);
                    arrayMap.put("3", stringList2);
                    arrayMap.put("4", stringList3);
                    arrayMap.put("5", stringList4);
                    arrayMap.put("6", stringList5);
                    arrayMap.put("7", stringList6);
                    arrayMap.put("8", stringList7);


                    arrayMap.put("9", innovationQuoteList);
                    arrayMap.put("10", stringList8);
                    arrayMap.put("11", stringList9);
                    arrayMap.put("12", stringList10);
                    arrayMap.put("13", stringList11);
                    arrayMap.put("14", stringList12);
                    arrayMap.put("15", stringList13);
                    postQuote(arrayMap);

                } else if (state.equals(FAILURE)) {

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
