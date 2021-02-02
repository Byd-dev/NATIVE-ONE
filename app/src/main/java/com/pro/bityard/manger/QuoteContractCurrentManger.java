package com.pro.bityard.manger;

import com.pro.bityard.entity.QuoteMinEntity;

import java.util.Observable;
import java.util.Timer;

public class QuoteContractCurrentManger extends Observable {


    private static QuoteContractCurrentManger quoteContractCurrentManger;


    public static QuoteContractCurrentManger getInstance() {
        if (quoteContractCurrentManger == null) {
            synchronized (QuoteContractCurrentManger.class) {
                if (quoteContractCurrentManger == null) {
                    quoteContractCurrentManger = new QuoteContractCurrentManger();
                }
            }

        }
        return quoteContractCurrentManger;

    }


    public void postQuote(QuoteMinEntity data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        quoteContractCurrentManger = null;
    }


    private Timer mTimer;
    private String quote_code;


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
