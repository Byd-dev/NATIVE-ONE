package com.pro.bityard.manger;

import java.util.Observable;

public class FollowManger extends Observable {

    private FollowManger() {

    }

    private static FollowManger followManger = null;

    public static FollowManger getInstance() {
        if (followManger == null) {
            synchronized (FollowManger.class) {
                if (followManger == null) {
                    followManger = new FollowManger();
                }
            }

        }
        return followManger;

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
        followManger = null;


    }

}
