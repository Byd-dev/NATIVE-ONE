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




                /*现货defi*/
                List<String> spotDEFIQuoteList = TradeUtil.spotDEFIQuoteList(spotQuoteList);
                //价格从高到低
                List<String> spotDeFiQuoteList_price_high2low = TradeUtil.priceHighToLow(spotDEFIQuoteList);
                //价格从低到高
                List<String> spotDeFiQuoteList_price_low2high = TradeUtil.priceLowToHigh(spotDEFIQuoteList);
                //涨跌幅从高到低
                List<String> spotDeFiQuoteList_rate_high2low = TradeUtil.rangeHighToLow(spotDEFIQuoteList);
                //涨跌幅从低到高
                List<String> spotDeFiQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(spotDEFIQuoteList);
                //字母a-z
                List<String> spotDeFiQuoteList_name_a2z = TradeUtil.nameLowToHigh(spotDEFIQuoteList);
                //字母z-a
                List<String> spotDeFiQuoteList_name_z2a = TradeUtil.nameHighToLow(spotDEFIQuoteList);


                /*现货defi*/
                List<String> spotPOSQuoteList = TradeUtil.spotPOSQuoteList(spotQuoteList);
                //价格从高到低
                List<String> spotPOSQuoteList_price_high2low = TradeUtil.priceHighToLow(spotPOSQuoteList);
                //价格从低到高
                List<String> spotPOSQuoteList_price_low2high = TradeUtil.priceLowToHigh(spotPOSQuoteList);
                //涨跌幅从高到低
                List<String> spotPOSQuoteList_rate_high2low = TradeUtil.rangeHighToLow(spotPOSQuoteList);
                //涨跌幅从低到高
                List<String> spotPOSQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(spotPOSQuoteList);
                //字母a-z
                List<String> spotPOSQuoteList_name_a2z = TradeUtil.nameLowToHigh(spotPOSQuoteList);
                //字母z-a
                List<String> spotPOSQuoteList_name_z2a = TradeUtil.nameHighToLow(spotPOSQuoteList);

                //所有合约 包括衍生品
                List<String> contractQuoteList = TradeUtil.contractQuoteList(strings);


                //价格从高到低
                List<String> contractQuoteList_price_high2low = TradeUtil.priceHighToLow(contractQuoteList);
                //价格从低到高
                List<String> contractQuoteList_price_low2high = TradeUtil.priceLowToHigh(contractQuoteList);
                //涨跌幅从高到低
                List<String> contractQuoteList_rate_high2low = TradeUtil.rangeHighToLow(contractQuoteList);
                //涨跌幅从低到高
                List<String> contractQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(contractQuoteList);
                //字母a-z:
                List<String> contractQuoteList_name_a2z = TradeUtil.nameLowToHigh(contractQuoteList);
                //字母z-a
                List<String> contractQuoteList_name_z2a = TradeUtil.nameHighToLow(contractQuoteList);


                //合约中的合约
                List<String> contractMainQuoteList = TradeUtil.contractMainQuoteList(contractQuoteList);
                //价格从高到低
                List<String> contractMainQuoteList_price_high2low = TradeUtil.priceHighToLow(contractMainQuoteList);
                //价格从低到高
                List<String> contractMainQuoteList_price_low2high = TradeUtil.priceLowToHigh(contractMainQuoteList);
                //涨跌幅从高到低
                List<String> contractMainQuoteList_rate_high2low = TradeUtil.rangeHighToLow(contractMainQuoteList);
                //涨跌幅从低到高
                List<String> contractMainQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(contractMainQuoteList);
                //字母a-z:
                List<String> contractMainQuoteList_name_a2z = TradeUtil.nameLowToHigh(contractMainQuoteList);
                //字母z-a
                List<String> contractMainQuoteList_name_z2a = TradeUtil.nameHighToLow(contractMainQuoteList);


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

                //外汇
                List<String> feQuoteList = TradeUtil.foreignExchangeQuoteList(strings);
                //价格从高到低
                List<String> feQuoteList_price_high2low = TradeUtil.priceHighToLow(feQuoteList);
                //价格从低到高
                List<String> feQuoteList_price_low2high = TradeUtil.priceLowToHigh(feQuoteList);
                //涨跌幅从高到低
                List<String> feQuoteList_rate_high2low = TradeUtil.rangeHighToLow(feQuoteList);
                //涨跌幅从低到高
                List<String> feQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(feQuoteList);
                //字母a-z
                List<String> feQuoteList_name_a2z = TradeUtil.nameLowToHigh(feQuoteList);
                //字母z-a
                List<String> feQuoteList_name_z2a = TradeUtil.nameHighToLow(feQuoteList);
                /*-------------------------------------------------------------------------*/
                //主区
                List<String> mainQuoteList = TradeUtil.mainQuoteList(strings);
                //创新区
                List<String> innovationQuoteList = TradeUtil.innovationQuoteList(strings);
                //自选
                List<String> optionalQuoteList = TradeUtil.optionalQuoteList(strings);

                //历史记录
                List<String> historyQuoteList = TradeUtil.historyQuoteList(strings);

                List<String> optionalSpotQuoteList = TradeUtil.optionalQuoteList(spotQuoteList);
                List<String> optionalContractQuoteList = TradeUtil.optionalQuoteList(contractQuoteList);
                List<String> optionalDerivedQuoteList = TradeUtil.optionalQuoteList(derivedQuoteList);
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


                //现货自选
                if (optionalSpotQuoteList != null) {
                    //价格从高到低
                    List<String> optionalSpotQuoteList_price_high2low = TradeUtil.priceHighToLow(optionalSpotQuoteList);
                    //价格从低到高
                    List<String> optionalSpotQuoteList_price_low2high = TradeUtil.priceLowToHigh(optionalSpotQuoteList);
                    //涨跌幅从高到低
                    List<String> optionalSpotQuoteList_rate_high2low = TradeUtil.rangeHighToLow(optionalSpotQuoteList);
                    //涨跌幅从低到高
                    List<String> optionalSpotQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(optionalSpotQuoteList);
                    //字母a-z
                    List<String> optionalSpotQuoteList_name_a2z = TradeUtil.nameLowToHigh(optionalSpotQuoteList);
                    //字母z-a
                    List<String> optionalSpotQuoteList_name_z2a = TradeUtil.nameHighToLow(optionalSpotQuoteList);

                    arrayMap.put(AppConfig.OPTIONAL_SPOT_ALL, optionalSpotQuoteList);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_PRICE_HIGH2LOW, optionalSpotQuoteList_price_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_PRICE_LOW2HIGH, optionalSpotQuoteList_price_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_RATE_HIGH2LOW, optionalSpotQuoteList_rate_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_RATE_LOW2HIGH, optionalSpotQuoteList_rate_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_NAME_A2Z, optionalSpotQuoteList_name_a2z);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_NAME_Z2A, optionalSpotQuoteList_name_z2a);
                } else {
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_ALL, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_PRICE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_PRICE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_RATE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_RATE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_NAME_A2Z, null);
                    arrayMap.put(AppConfig.OPTIONAL_SPOT_NAME_Z2A, null);
                }


                //合约自选
                if (optionalContractQuoteList != null) {
                    //价格从高到低
                    List<String> optionalContractQuoteList_price_high2low = TradeUtil.priceHighToLow(optionalContractQuoteList);
                    //价格从低到高
                    List<String> optionalContractQuoteList_price_low2high = TradeUtil.priceLowToHigh(optionalContractQuoteList);
                    //涨跌幅从高到低
                    List<String> optionalContractQuoteList_rate_high2low = TradeUtil.rangeHighToLow(optionalContractQuoteList);
                    //涨跌幅从低到高
                    List<String> optionalContractQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(optionalContractQuoteList);
                    //字母a-z
                    List<String> optionalContractQuoteList_name_a2z = TradeUtil.nameLowToHigh(optionalContractQuoteList);
                    //字母z-a
                    List<String> optionalContractQuoteList_name_z2a = TradeUtil.nameHighToLow(optionalContractQuoteList);

                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_ALL, optionalContractQuoteList);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_PRICE_HIGH2LOW, optionalContractQuoteList_price_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_PRICE_LOW2HIGH, optionalContractQuoteList_price_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_RATE_HIGH2LOW, optionalContractQuoteList_rate_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_RATE_LOW2HIGH, optionalContractQuoteList_rate_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_NAME_A2Z, optionalContractQuoteList_name_a2z);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_NAME_Z2A, optionalContractQuoteList_name_z2a);
                } else {
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_ALL, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_PRICE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_PRICE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_RATE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_RATE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_NAME_A2Z, null);
                    arrayMap.put(AppConfig.OPTIONAL_CONTRACT_NAME_Z2A, null);
                }


                //衍生品自选
                if (optionalDerivedQuoteList != null) {
                    //价格从高到低
                    List<String> optionalDerivedQuoteList_price_high2low = TradeUtil.priceHighToLow(optionalDerivedQuoteList);
                    //价格从低到高
                    List<String> optionalDerivedQuoteList_price_low2high = TradeUtil.priceLowToHigh(optionalDerivedQuoteList);
                    //涨跌幅从高到低
                    List<String> optionalDerivedQuoteList_rate_high2low = TradeUtil.rangeHighToLow(optionalDerivedQuoteList);
                    //涨跌幅从低到高
                    List<String> optionalDerivedQuoteList_rate_low2high = TradeUtil.rangeLowToHigh(optionalDerivedQuoteList);
                    //字母a-z
                    List<String> optionalDerivedQuoteList_name_a2z = TradeUtil.nameLowToHigh(optionalDerivedQuoteList);
                    //字母z-a
                    List<String> optionalDerivedQuoteList_name_z2a = TradeUtil.nameHighToLow(optionalDerivedQuoteList);

                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_ALL, optionalDerivedQuoteList);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_PRICE_HIGH2LOW, optionalDerivedQuoteList_price_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_PRICE_LOW2HIGH, optionalDerivedQuoteList_price_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_RATE_HIGH2LOW, optionalDerivedQuoteList_rate_high2low);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_RATE_LOW2HIGH, optionalDerivedQuoteList_rate_low2high);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_NAME_A2Z, optionalDerivedQuoteList_name_a2z);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_NAME_Z2A, optionalDerivedQuoteList_name_z2a);
                } else {
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_ALL, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_PRICE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_PRICE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_RATE_HIGH2LOW, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_RATE_LOW2HIGH, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_NAME_A2Z, null);
                    arrayMap.put(AppConfig.OPTIONAL_DERIVATIVES_NAME_Z2A, null);
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

                //现货DeFi
                arrayMap.put(AppConfig.SPOT_DEFI_ALL, spotDEFIQuoteList);
                arrayMap.put(AppConfig.SPOT_DEFI_PRICE_HIGH2LOW, spotDeFiQuoteList_price_high2low);
                arrayMap.put(AppConfig.SPOT_DEFI_PRICE_LOW2HIGH, spotDeFiQuoteList_price_low2high);
                arrayMap.put(AppConfig.SPOT_DEFI_RATE_HIGH2LOW, spotDeFiQuoteList_rate_high2low);
                arrayMap.put(AppConfig.SPOT_DEFI_RATE_LOW2HIGH, spotDeFiQuoteList_rate_low2high);
                arrayMap.put(AppConfig.SPOT_DEFI_NAME_A2Z, spotDeFiQuoteList_name_a2z);
                arrayMap.put(AppConfig.SPOT_DEFI_NAME_Z2A, spotDeFiQuoteList_name_z2a);

                //现货POS
                arrayMap.put(AppConfig.SPOT_POS_ALL, spotPOSQuoteList);
                arrayMap.put(AppConfig.SPOT_POS_PRICE_HIGH2LOW, spotPOSQuoteList_price_high2low);
                arrayMap.put(AppConfig.SPOT_POS_PRICE_LOW2HIGH, spotPOSQuoteList_price_low2high);
                arrayMap.put(AppConfig.SPOT_POS_RATE_HIGH2LOW, spotPOSQuoteList_rate_high2low);
                arrayMap.put(AppConfig.SPOT_POS_RATE_LOW2HIGH, spotPOSQuoteList_rate_low2high);
                arrayMap.put(AppConfig.SPOT_POS_NAME_A2Z, spotPOSQuoteList_name_a2z);
                arrayMap.put(AppConfig.SPOT_POS_NAME_Z2A, spotPOSQuoteList_name_z2a);

                //合约包括衍生品
                arrayMap.put(AppConfig.CONTRACT_ALL, contractQuoteList);
                arrayMap.put(AppConfig.CONTRACT_PRICE_HIGH2LOW, contractQuoteList_price_high2low);
                arrayMap.put(AppConfig.CONTRACT_PRICE_LOW2HIGH, contractQuoteList_price_low2high);
                arrayMap.put(AppConfig.CONTRACT_RATE_HIGH2LOW, contractQuoteList_rate_high2low);
                arrayMap.put(AppConfig.CONTRACT_RATE_LOW2HIGH, contractQuoteList_rate_low2high);
                arrayMap.put(AppConfig.CONTRACT_NAME_A2Z, contractQuoteList_name_a2z);
                arrayMap.put(AppConfig.CONTRACT_NAME_Z2A, contractQuoteList_name_z2a);


                //合约中合约
                arrayMap.put(AppConfig.CONTRACT_IN_ALL, contractMainQuoteList);
                arrayMap.put(AppConfig.CONTRACT_IN_PRICE_HIGH2LOW, contractMainQuoteList_price_high2low);
                arrayMap.put(AppConfig.CONTRACT_IN_PRICE_LOW2HIGH, contractMainQuoteList_price_low2high);
                arrayMap.put(AppConfig.CONTRACT_IN_RATE_HIGH2LOW, contractMainQuoteList_rate_high2low);
                arrayMap.put(AppConfig.CONTRACT_IN_RATE_LOW2HIGH, contractMainQuoteList_rate_low2high);
                arrayMap.put(AppConfig.CONTRACT_IN_NAME_A2Z, contractMainQuoteList_name_a2z);
                arrayMap.put(AppConfig.CONTRACT_IN_NAME_Z2A, contractMainQuoteList_name_z2a);
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

                //外汇
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_ALL, feQuoteList);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_PRICE_HIGH2LOW, feQuoteList_price_high2low);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_PRICE_LOW2HIGH, feQuoteList_price_low2high);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_RATE_HIGH2LOW, feQuoteList_rate_high2low);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_RATE_LOW2HIGH, feQuoteList_rate_low2high);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_NAME_A2Z, feQuoteList_name_a2z);
                arrayMap.put(AppConfig.FOREIGN_EXCHANGE_NAME_Z2A, feQuoteList_name_z2a);

                //历史记录
                arrayMap.put(AppConfig.HISTORY_ALL, historyQuoteList);

                Log.d("webSocket", "onMessage:384:  " + quoteEntity.getData().length());
                postListQuote(arrayMap);

                break;
            case "4001":
                QuoteMinEntity quoteMinEntity = new Gson().fromJson(quoteEntity.getData(), QuoteMinEntity.class);
                //Log.d("webSocket", "onMessage:400:  " + quoteEntity.getData());
                QuoteCurrentManger.getInstance().postQuote(quoteMinEntity);
                break;
            case "5001":
                //Log.d("webSocket", "onMessage:收到3:  " + quoteEntity.getData());
                QuoteSpotManger.getInstance().postQuote(quoteEntity.getData());
                break;

        }


    }




}
