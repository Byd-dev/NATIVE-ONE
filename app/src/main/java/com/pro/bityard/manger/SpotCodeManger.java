package com.pro.bityard.manger;

import java.util.Observable;

public class SpotCodeManger extends Observable {

    private SpotCodeManger() {

    }

    private static SpotCodeManger controlManger = null;

    public static SpotCodeManger getInstance() {
        if (controlManger == null) {
            synchronized (SpotCodeManger.class) {
                if (controlManger == null) {
                    controlManger = new SpotCodeManger();
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
