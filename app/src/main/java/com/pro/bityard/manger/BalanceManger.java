package com.pro.bityard.manger;

import java.util.Observable;

public class BalanceManger extends Observable {

    private BalanceManger() {

    }

    private static BalanceManger balanceManger = null;

    public static BalanceManger getInstance() {
        if (balanceManger == null) {
            synchronized (BalanceManger.class) {
                if (balanceManger == null) {
                    balanceManger = new BalanceManger();
                }
            }

        }
        return balanceManger;

    }

    public void postMessage(String eventType) {

        setChanged();
        notifyObservers(eventType);

    }

    /**
     * 清理消息监听
     */
    public void clear(){

        deleteObservers();
        balanceManger = null;
    }

}
