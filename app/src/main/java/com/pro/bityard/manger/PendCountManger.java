package com.pro.bityard.manger;

import java.util.Observable;

public class PendCountManger extends Observable {

    private PendCountManger() {

    }

    private static PendCountManger tabManger = null;

    public static PendCountManger getInstance() {
        if (tabManger == null) {
            synchronized (PendCountManger.class) {
                if (tabManger == null) {
                    tabManger = new PendCountManger();
                }
            }

        }
        return tabManger;

    }

    public void post(String position) {

        setChanged();
        notifyObservers(position);

    }




    /**
     * 清理消息监听
     */
    public void clear() {

        tabManger = null;
    }

}
