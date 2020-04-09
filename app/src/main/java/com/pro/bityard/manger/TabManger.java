package com.pro.bityard.manger;

import java.util.Observable;

public class TabManger extends Observable {

    private TabManger() {

    }

    private static TabManger tabManger = null;

    public static TabManger getInstance() {
        if (tabManger == null) {
            synchronized (TabManger.class) {
                if (tabManger == null) {
                    tabManger = new TabManger();
                }
            }

        }
        return tabManger;

    }

    public void post(Integer position) {

        setChanged();
        notifyObservers(position);

    }


    public void jump(Integer position) {
        post(position);

    }

    /**
     * 清理消息监听
     */
    public void clear() {

        tabManger = null;
    }

}
