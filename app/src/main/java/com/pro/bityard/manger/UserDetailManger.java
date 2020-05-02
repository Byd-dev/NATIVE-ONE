package com.pro.bityard.manger;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.pro.bityard.api.NetManger;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class UserDetailManger extends Observable {


    private static UserDetailManger userDetailManger;

    private String code;


    public static UserDetailManger getInstance() {
        if (userDetailManger == null) {
            synchronized (UserDetailManger.class) {
                if (userDetailManger == null) {
                    userDetailManger = new UserDetailManger();
                }
            }

        }
        return userDetailManger;

    }

    private Timer mTimer;

    public void startScheduleJob(long delay, long interval) {
        if (mTimer != null) cancelTimer();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }, delay, interval);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            detail();
        }
    };


    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    public void detail() {
        NetManger.getInstance().userDetail((state, response) -> {
            if (state.equals(SUCCESS)) {
                postDetail(response);
            }
        });


    }

    public void postDetail(Object data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        userDetailManger = null;
    }


}
