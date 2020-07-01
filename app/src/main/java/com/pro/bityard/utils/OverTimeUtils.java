package com.pro.bityard.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class OverTimeUtils {
    /*倒计时时长  单位：秒*/
    private final static int COUNT = 90;
    /*当前做*/
    private static int CURR_COUNT = 0;
    /*设置提现账户  标识
      发送验证码*/
    private static Timer countdownTimer;
    private static TextView textMIn;
    private static TextView textSecond;


    /*设置提现账户
      预计结束的时间*/
    /*注册*/


    private final static int POSITION_ONE = 0;
    private final static int POSITION_TWO = 1;
    private final static int POSITION_THREE = 2;
    private final static int POSITION_FOUR = 3;
    private final static int POSITION_FIVE = 4;
    private final static int POSITION_SIX = 5;
    private final static int POSITION_SEVEN = 6;
    private final static int POSITION_EIGHT = 7;
    private final static int POSITION_NINE = 8;
    private final static int POSITION_TEN = 9;

    private static long POSITION_ONE_END = 0;
    private static long POSITION_TWO_END = 0;
    private static long POSITION_THREE_END = 0;
    private static long POSITION_FOUR_END = 0;
    private static long POSITION_FIVE_END = 0;
    private static long POSITION_SIX_END = 0;
    private static long POSITION_SEVEN_END = 0;
    private static long POSITION_EIGHT_END = 0;
    private static long POSITION_NINE_END = 0;
    private static long POSITION_TEN_END = 0;

    /**
     * 检查是否超过60秒
     * 给当前要从多少开始倒数赋值
     * http://blog.csdn.net/qq_16965811
     *
     * @param type  1，设置提现账户 2，注册3，忘记密码
     * @param first true 表示第一次   false不是
     * @return 是否需要调用startCountdown(TextView textView)，主要用于判断在重新打开页，需不需要继续倒计时
     */
    public static boolean check(long data,int type, boolean first) {
       // long data = System.currentTimeMillis();
        long time = 0;
        switch (type) {

            case 0:
                time = POSITION_ONE;
                break;
            case 1:
                time = POSITION_TWO;
                break;
            case 2:
                time = POSITION_THREE;
                break;
            case 3:
                time = POSITION_FOUR;
                break;
            case 4:
                time = POSITION_FIVE;
                break;
            case 5:
                time = POSITION_SIX;
                break;
            case 6:
                time = POSITION_SEVEN;
                break;
            case 7:
                time = POSITION_EIGHT;
                break;
            case 8:
                time = POSITION_NINE;
                break;
            case 9:
                time = POSITION_TEN;
                break;

        }
        if (data > time) {
            /*主要是区别于是否是第一次进入。第一次进入不需要赋值*/
            if (!first) {
                CURR_COUNT = COUNT;
                time = data + 10 * 60 * 1000;
                switch (type) {
                    case POSITION_ONE:
                        POSITION_ONE_END = time;
                        break;
                    case POSITION_TWO:
                        POSITION_TWO_END = time;
                        break;
                    case POSITION_THREE:
                        POSITION_THREE_END = time;
                        break;
                    case POSITION_FOUR:
                        POSITION_FOUR_END = time;
                        break;
                    case POSITION_FIVE:
                        POSITION_FIVE_END = time;
                        break;
                    case POSITION_SIX:
                        POSITION_SIX_END = time;
                        break;
                    case POSITION_SEVEN:
                        POSITION_SEVEN_END = time;
                        break;
                    case POSITION_EIGHT:
                        POSITION_EIGHT_END = time;
                        break;
                    case POSITION_NINE:
                        POSITION_NINE_END = time;
                        break;
                    case POSITION_TEN:
                        POSITION_TEN_END = time;
                        break;
                }
            }
            return false;
        } else {
            int the_difference = ((int) (time - data)) / 1000;
            CURR_COUNT = the_difference;
            return true;
        }
    }

    /**
     * 开始倒计时
     *
     * @param textView 控制倒计时的view
     */
    public static void startCountdown(TextView textView,TextView textView2) {
        textMIn = textView;
        textSecond=textView2;
        if (countdownTimer == null) {
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = CURR_COUNT--;
                    handler.sendMessage(msg);
                }
            }, 0, 1000);
        }
    }


    private static Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer = null;
                }
                textMIn.setText("00");
                textSecond.setText("00");
            } else {
                String min = Util.getMins(msg.what);
                String seconds = Util.getSeconds(msg.what);
                textMIn.setText(min);
                textSecond.setText(seconds);

            }
            super.handleMessage(msg);
        }
    };

}
