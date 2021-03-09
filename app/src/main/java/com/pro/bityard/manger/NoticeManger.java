package com.pro.bityard.manger;

import com.pro.bityard.config.AppConfig;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;

public class NoticeManger extends Observable {

    private NoticeManger() {

    }

    private static NoticeManger noticeManger = null;

    public static NoticeManger getInstance() {
        if (noticeManger == null) {
            synchronized (NoticeManger.class) {
                if (noticeManger == null) {
                    noticeManger = new NoticeManger();
                }
            }

        }
        return noticeManger;

    }

    public void postTag(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void notice() {
        postTag(true);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        noticeManger = null;


    }

}
