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

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        BalanceManger.getInstance().clear();
        //持仓初始化
        PositionRealManger.getInstance().clear();
        PositionSimulationManger.getInstance().clear();
        deleteObservers();
        balanceManger = null;
    }

}
