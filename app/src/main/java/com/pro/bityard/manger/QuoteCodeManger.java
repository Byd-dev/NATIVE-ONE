package com.pro.bityard.manger;

import java.util.Observable;

public class QuoteCodeManger extends Observable {

    private QuoteCodeManger() {

    }

    private static QuoteCodeManger controlManger = null;

    public static QuoteCodeManger getInstance() {
        if (controlManger == null) {
            synchronized (QuoteCodeManger.class) {
                if (controlManger == null) {
                    controlManger = new QuoteCodeManger();
                }
            }

        }
        return controlManger;

    }

    public void postTag(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void tag() {
        postTag(true);


    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        controlManger = null;


    }

}
