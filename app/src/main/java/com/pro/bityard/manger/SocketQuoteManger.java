package com.pro.bityard.manger;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;

import java.util.List;
import java.util.Observable;
import java.util.Timer;

public class SocketQuoteManger extends Observable implements IReceiveMessage {


    private static SocketQuoteManger socketQuoteManger;


    public static SocketQuoteManger getInstance() {
        if (socketQuoteManger == null) {
            synchronized (SocketQuoteManger.class) {
                if (socketQuoteManger == null) {
                    socketQuoteManger = new SocketQuoteManger();
                }
            }

        }
        return socketQuoteManger;

    }

    private Timer mTimer;


    public void initSocket() {
        WebSocketManager.getInstance().init(NetManger.QUOTE_SOCKET, this);
    }




    public void postListQuote(ArrayMap<String, List<String>> arrayMap) {
        setChanged();
        notifyObservers(arrayMap);
    }


    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        socketQuoteManger = null;
        WebSocketManager.getInstance().close();
    }


    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectFailed() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onMessage(String text) {

        ArrayMap<String, List<String>> arrayMap = new ArrayMap<>();
        QuoteEntity quoteEntity = new Gson().fromJson(text, QuoteEntity.class);

        switch (quoteEntity.getCmid()) {
            //行情列表
            case "3001":
                String data = quoteEntity.getData();

                List<String> strings = Util.quoteResultAdd(Util.quoteResult(data));


                //现货
                List<String> spotQuoteList = TradeUtil.spotQuoteList(strings);
                //价格从高到低
                List<String> spotQuoteList_price_high2low = TradeUtil.priceHighToLow(spotQuoteList);
                //价格从低到高
                List<String> spotQuoteList_price_low2high = TradeUtil.priceLowToHigh(spotQuoteList);
                //涨跌幅从高到低
                List<String> spotQuoteList_rate_high2low = TradeUtil.rangeHighToLow(spotQuoteList);
                //涨跌幅从低到高
                List<String> spotQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(spotQuoteList);
                //字母a-z
                List<String> spotQuoteList_name_a2z = TradeUtil.nameLowToHigh(spotQuoteList);
                //字母z-a
                List<String> spotQuoteList_name_z2a = TradeUtil.nameHighToLow(spotQuoteList);





                //合约
                List<String> contractQuoteList = TradeUtil.contractQuoteList(strings);

                //价格从高到低
                List<String> contractQuoteList_price_high2low = TradeUtil.priceHighToLow(contractQuoteList);
                //价格从低到高
                List<String> contractQuoteList_price_low2high = TradeUtil.priceLowToHigh(contractQuoteList);
                //涨跌幅从高到低
                List<String> contractQuoteList_rate_high2low = TradeUtil.rangeHighToLow(contractQuoteList);
                //涨跌幅从低到高
                List<String> contractQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(contractQuoteList);
                //字母a-z
                List<String> contractQuoteList_name_a2z = TradeUtil.nameLowToHigh(contractQuoteList);
                //字母z-a
                List<String> contractQuoteList_name_z2a = TradeUtil.nameHighToLow(contractQuoteList);


                //衍生区
                List<String> derivedQuoteList = TradeUtil.derivedQuoteList(strings);

                //价格从高到低
                List<String> derivedQuoteList_price_high2low = TradeUtil.priceHighToLow(derivedQuoteList);
                //价格从低到高
                List<String> derivedQuoteList_price_low2high = TradeUtil.priceLowToHigh(derivedQuoteList);
                //涨跌幅从高到低
                List<String> derivedQuoteList_rate_high2low = TradeUtil.rangeHighToLow(derivedQuoteList);
                //涨跌幅从低到高
                List<String> derivedQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(derivedQuoteList);
                //字母a-z
                List<String> derivedQuoteList_name_a2z = TradeUtil.nameLowToHigh(derivedQuoteList);
                //字母z-a
                List<String> derivedQuoteList_name_z2a = TradeUtil.nameHighToLow(derivedQuoteList);


                /*-------------------------------------------------------------------------*/
                //主区
                List<String> mainQuoteList = TradeUtil.mainQuoteList(strings);
                //创新区
                List<String> innovationQuoteList = TradeUtil.innovationQuoteList(strings);
                //自选
                Log.d("print", "onMessage:151: "+"执行了这里");
                List<String> optionalQuoteList = TradeUtil.optionalQuoteList(strings);



                //BTC BCH ETH
                List<String> hotQuoteList = TradeUtil.homeHot(contractQuoteList);
                //除去 BTC BCH ETH
                List<String> homeList = TradeUtil.homeList(mainQuoteList);


                //价格从高到低
                List<String> stringList = TradeUtil.priceHighToLow(mainQuoteList);
                //价格从低到高
                List<String> stringList1 = TradeUtil.priceLowToHigh(mainQuoteList);
                //涨跌幅从高到低
                List<String> stringList2 = TradeUtil.rangeHighToLow(mainQuoteList);
                //涨跌幅从低到高
                List<String> stringList3 = TradeUtil.rangeLowToHigh(mainQuoteList);
                //字母a-z
                List<String> stringList6 = TradeUtil.nameLowToHigh(mainQuoteList);
                //字母z-a
                List<String> stringList7 = TradeUtil.nameHighToLow(mainQuoteList);


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

                //自选
                if (optionalQuoteList != null) {
                    //价格从高到低
                    List<String> optionalQuoteList_price_high2low = TradeUtil.priceHighToLow(optionalQuoteList);
                    //价格从低到高
                    List<String> optionalQuoteList_price_low2high = TradeUtil.priceLowToHigh(optionalQuoteList);
                    //涨跌幅从高到低
                    List<String> optionalQuoteList_rate_high2low = TradeUtil.rangeHighToLow(optionalQuoteList);
                    //涨跌幅从低到高
                    List<String> optionalQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(optionalQuoteList);
                    //字母a-z
                    List<String> optionalQuoteList_name_a2z = TradeUtil.nameLowToHigh(optionalQuoteList);
                    //字母z-a
                    List<String> optionalQuoteList_name_z2a = TradeUtil.nameHighToLow(optionalQuoteList);

                    arrayMap.put(AppConfig.OPTIONAL_ALL, optionalQuoteList);
                    arrayMap.put(AppConfig.OPTIONAL_PRICE_HIGH2LOW, optionalQuoteList_price_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_PRICE_LOW2HIGH, optionalQuoteList_price_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_RATE_HIGH2LOW, optionalQuoteList_rate_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_RATE_LOW2HIGH, optionalQuoteList_rate_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_NAME_A2Z, optionalQuoteList_name_a2z);
                    arrayMap.put(AppConfig.OPTIONAL_NAME_Z2A, optionalQuoteList_name_z2a);
                } else {
                    arrayMap.put(AppConfig.OPTIONAL_ALL, null);
                    arrayMap.put(AppConfig.OPTIONAL_PRICE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_PRICE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_RATE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_RATE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_NAME_A2Z, null);
                    arrayMap.put(AppConfig.OPTIONAL_NAME_Z2A, null);
                }


                //价格从高到低
                List<String> stringList20 = TradeUtil.priceHighToLow(derivedQuoteList);
                //价格从低到高
                List<String> stringList21 = TradeUtil.priceLowToHigh(derivedQuoteList);
                //涨跌幅从高到低
                List<String> stringList22 = TradeUtil.rangeHighToLow(derivedQuoteList);
                //涨跌幅从低到高
                List<String> stringList23 = TradeUtil.rangeLowToHigh(derivedQuoteList);
                //字母a-z
                List<String> stringList24 = TradeUtil.nameLowToHigh(derivedQuoteList);
                //字母z-a
                List<String> stringList25 = TradeUtil.nameHighToLow(derivedQuoteList);


                arrayMap.put("all", strings);

                arrayMap.put("0", mainQuoteList);
                arrayMap.put("1", stringList);
                arrayMap.put("2", stringList1);
                arrayMap.put("3", stringList2);
                arrayMap.put("4", stringList3);
                arrayMap.put("7", stringList6);
                arrayMap.put("8", stringList7);

                arrayMap.put("9", innovationQuoteList);
                arrayMap.put("10", stringList8);
                arrayMap.put("11", stringList9);
                arrayMap.put("12", stringList10);
                arrayMap.put("13", stringList11);
                arrayMap.put("14", stringList12);
                arrayMap.put("15", stringList13);

                arrayMap.put("23", derivedQuoteList);
                arrayMap.put("24", stringList20);
                arrayMap.put("25", stringList21);
                arrayMap.put("26", stringList22);
                arrayMap.put("27", stringList23);
                arrayMap.put("28", stringList24);
                arrayMap.put("29", stringList25);

                //现货
                arrayMap.put(AppConfig.SPOT_ALL, spotQuoteList);
                arrayMap.put(AppConfig.SPOT_PRICE_HIGH2LOW, spotQuoteList_price_high2low);
                arrayMap.put(AppConfig.SPOT_PRICE_LOW2HIGH, spotQuoteList_price_low2high);
                arrayMap.put(AppConfig.SPOT_RATE_HIGH2LOW, spotQuoteList_rate_high2low);
                arrayMap.put(AppConfig.SPOT_RATE_LOW2HIGH, spotQuoteList_rate_low2high);
                arrayMap.put(AppConfig.SPOT_NAME_A2Z, spotQuoteList_name_a2z);
                arrayMap.put(AppConfig.SPOT_NAME_Z2A, spotQuoteList_name_z2a);

                //合约
                arrayMap.put(AppConfig.CONTRACT_ALL, contractQuoteList);
                arrayMap.put(AppConfig.CONTRACT_PRICE_HIGH2LOW, contractQuoteList_price_high2low);
                arrayMap.put(AppConfig.CONTRACT_PRICE_LOW2HIGH, contractQuoteList_price_low2high);
                arrayMap.put(AppConfig.CONTRACT_RATE_HIGH2LOW, contractQuoteList_rate_high2low);
                arrayMap.put(AppConfig.CONTRACT_RATE_LOW2HIGH, contractQuoteList_rate_low2high);
                arrayMap.put(AppConfig.CONTRACT_NAME_A2Z, contractQuoteList_name_a2z);
                arrayMap.put(AppConfig.CONTRACT_NAME_Z2A, contractQuoteList_name_z2a);
                //首页热门和列表
                arrayMap.put(AppConfig.HOME_HOT, hotQuoteList);
                arrayMap.put(AppConfig.HOME_LIST, homeList);


                //衍生品
                arrayMap.put(AppConfig.DERIVATIVES_ALL, derivedQuoteList);
                arrayMap.put(AppConfig.DERIVATIVES_PRICE_HIGH2LOW, derivedQuoteList_price_high2low);
                arrayMap.put(AppConfig.DERIVATIVES_PRICE_LOW2HIGH, derivedQuoteList_price_low2high);
                arrayMap.put(AppConfig.DERIVATIVES_RATE_HIGH2LOW, derivedQuoteList_rate_high2low);
                arrayMap.put(AppConfig.DERIVATIVES_RATE_LOW2HIGH, derivedQuoteList_rate_low2high);
                arrayMap.put(AppConfig.DERIVATIVES_NAME_A2Z, derivedQuoteList_name_a2z);
                arrayMap.put(AppConfig.DERIVATIVES_NAME_Z2A, derivedQuoteList_name_z2a);

                Log.d("webSocket", "onMessage:384:  " + quoteEntity.getData());
                postListQuote(arrayMap);

                break;
            case "4001":
                QuoteMinEntity quoteMinEntity = new Gson().fromJson(quoteEntity.getData(), QuoteMinEntity.class);
                Log.d("webSocket", "onMessage:400:  " + quoteEntity.getData());
                QuoteCurrentManger.getInstance().postQuote(quoteMinEntity);
                break;
            case "6001":
                //  Log.d("webSocket", "onMessage:收到3:  " + quoteEntity.getData());
                break;

        }


    }

   /*public void startScheduleJob(long delay, long interval) {
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


        if (quote_code == null) {
            NetManger.getInstance().initQuote();
        } else {
             ArrayMap<String, List<String>> arrayMap = new ArrayMap<>();

            NetManger.getInstance().getQuote(quote_host, "/quote.jsp", quote_code, (state, response) -> {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {
                    String jsonReplace = Util.jsonReplace(response.toString());
                    QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                    String data = quoteEntity.getData();
                    List<String> strings = Util.quoteResult(data);
                    //主区
                    List<String> mainQuoteList = TradeUtil.mainQuoteList(strings);
                    //创新区
                    List<String> innovationQuoteList = TradeUtil.innovationQuoteList(strings);
                    //自选
                    List<String> optionalQuoteList = TradeUtil.optionalQuoteList(strings);
                    //衍生区
                    List<String> derivedQuoteList = TradeUtil.derivedQuoteList(strings);


                    //BTC BCH ETH
                    List<String> stringList4 = TradeUtil.homeHot(mainQuoteList);
                    //除去 BTC BCH ETH
                    List<String> stringList5 = TradeUtil.homeList(mainQuoteList);


                    //价格从高到低
                    List<String> stringList = TradeUtil.priceHighToLow(mainQuoteList);
                    //价格从低到高
                    List<String> stringList1 = TradeUtil.priceLowToHigh(mainQuoteList);
                    //涨跌幅从高到低
                    List<String> stringList2 = TradeUtil.rangeHighToLow(mainQuoteList);
                    //涨跌幅从低到高
                    List<String> stringList3 = TradeUtil.rangeLowToHigh(mainQuoteList);
                    //字母a-z
                    List<String> stringList6 = TradeUtil.nameLowToHigh(mainQuoteList);
                    //字母z-a
                    List<String> stringList7 = TradeUtil.nameHighToLow(mainQuoteList);


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


                    if (optionalQuoteList != null) {
                        //价格从高到低
                        List<String> stringList14 = TradeUtil.priceHighToLow(optionalQuoteList);
                        //价格从低到高
                        List<String> stringList15 = TradeUtil.priceLowToHigh(optionalQuoteList);
                        //涨跌幅从高到低
                        List<String> stringList16 = TradeUtil.rangeHighToLow(optionalQuoteList);
                        //涨跌幅从低到高
                        List<String> stringList17 = TradeUtil.rangeLowToHigh(optionalQuoteList);
                        //字母a-z
                        List<String> stringList18 = TradeUtil.nameLowToHigh(optionalQuoteList);
                        //字母z-a
                        List<String> stringList19 = TradeUtil.nameHighToLow(optionalQuoteList);
                        arrayMap.put("16", optionalQuoteList);
                        arrayMap.put("17", stringList14);
                        arrayMap.put("18", stringList15);
                        arrayMap.put("19", stringList16);
                        arrayMap.put("20", stringList17);
                        arrayMap.put("21", stringList18);
                        arrayMap.put("22", stringList19);
                    } else {
                        arrayMap.put("16", null);
                        arrayMap.put("17", null);
                        arrayMap.put("18", null);
                        arrayMap.put("19", null);
                        arrayMap.put("20", null);
                        arrayMap.put("21", null);
                        arrayMap.put("22", null);
                    }


                    //价格从高到低
                    List<String> stringList20 = TradeUtil.priceHighToLow(derivedQuoteList);
                    //价格从低到高
                    List<String> stringList21 = TradeUtil.priceLowToHigh(derivedQuoteList);
                    //涨跌幅从高到低
                    List<String> stringList22 = TradeUtil.rangeHighToLow(derivedQuoteList);
                    //涨跌幅从低到高
                    List<String> stringList23 = TradeUtil.rangeLowToHigh(derivedQuoteList);
                    //字母a-z
                    List<String> stringList24 = TradeUtil.nameLowToHigh(derivedQuoteList);
                    //字母z-a
                    List<String> stringList25 = TradeUtil.nameHighToLow(derivedQuoteList);


                    arrayMap.put("all", strings);

                    arrayMap.put("0", mainQuoteList);
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

                    arrayMap.put("23", derivedQuoteList);
                    arrayMap.put("24", stringList20);
                    arrayMap.put("25", stringList21);
                    arrayMap.put("26", stringList22);
                    arrayMap.put("27", stringList23);
                    arrayMap.put("28", stringList24);
                    arrayMap.put("29", stringList25);

                    postQuote(arrayMap);

                } else if (state.equals(FAILURE)) {

                }
            });
        }


    }*/
}
