package com.pro.bityard.manger;

import java.util.Observable;

public class TabCountManger extends Observable {

    private TabCountManger() {

    }

    private static TabCountManger tabManger = null;

    public static TabCountManger getInstance() {
        if (tabManger == null) {
            synchronized (TabCountManger.class) {
                if (tabManger == null) {
                    tabManger = new TabCountManger();
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
