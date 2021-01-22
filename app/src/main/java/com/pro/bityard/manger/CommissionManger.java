package com.pro.bityard.manger;

import java.util.Observable;

public class CommissionManger extends Observable {

    private CommissionManger() {

    }

    private static CommissionManger controlManger = null;

    public static CommissionManger getInstance() {
        if (controlManger == null) {
            synchronized (CommissionManger.class) {
                if (controlManger == null) {
                    controlManger = new CommissionManger();
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
