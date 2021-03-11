package com.pro.bityard.manger;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuoteItemManger extends Observable {


    private static QuoteItemManger quoteItemManger;


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


    public void quote(String quote_code, OnResult onResult) {
        String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
        if (quote_host == null && quote_code == null) {
            NetManger.getInstance().initQuote();
        } else {
            NetManger.getInstance().getItemQuote(quote_host, "/quote.jsp", quote_code, (state, response) -> {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    String jsonReplace = Util.jsonReplace(response.toString());
                    QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                    String data = quoteEntity.getData();
                    onResult.setResult(data);
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
