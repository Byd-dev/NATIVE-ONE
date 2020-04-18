package com.pro.bityard.manger;

import java.util.Observable;

public class TagManger extends Observable {

    private TagManger() {

    }

    private static TagManger balanceManger = null;

    public static TagManger getInstance() {
        if (balanceManger == null) {
            synchronized (TagManger.class) {
                if (balanceManger == null) {
                    balanceManger = new TagManger();
                }
            }

        }
        return balanceManger;

    }

    public void postTag(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void tag() {
        postTag(true);
        BalanceManger.getInstance().getBalance("USDT");


    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        balanceManger = null;
    }

}
