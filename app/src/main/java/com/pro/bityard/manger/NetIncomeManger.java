package com.pro.bityard.manger;

import java.util.Observable;

public class NetIncomeManger extends Observable {

    private NetIncomeManger() {

    }

    private static NetIncomeManger netIncomeManger = null;

    public static NetIncomeManger getInstance() {
        if (netIncomeManger == null) {
            synchronized (NetIncomeManger.class) {
                if (netIncomeManger == null) {
                    netIncomeManger = new NetIncomeManger();
                }
            }

        }
        return netIncomeManger;

    }

    public void postNetIncome(Object data) {

        setChanged();
        notifyObservers(data);

    }


    /**
     * 清理消息监听
     */
    public void clear() {

        deleteObservers();
        netIncomeManger = null;
    }

}
