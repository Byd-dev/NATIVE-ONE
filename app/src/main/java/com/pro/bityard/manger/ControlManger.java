package com.pro.bityard.manger;

import com.pro.bityard.config.AppConfig;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;

public class ControlManger extends Observable {

    private ControlManger() {

    }

    private static ControlManger controlManger = null;

    public static ControlManger getInstance() {
        if (controlManger == null) {
            synchronized (ControlManger.class) {
                if (controlManger == null) {
                    controlManger = new ControlManger();
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
