package com.pro.bityard.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SmsTimeUtils {
    /*倒计时时长  单位：秒*/
    private final static int COUNT = 90;
    /*当前做*/
    private static int CURR_COUNT = 0;
    /*设置提现账户  标识
      发送验证码*/
    public final static int EMAIL_REGISTER = 1;
    public final static int MOBILE_REGISTER = 2;

    public final static int EMAIL_FORGET = 3;
    public final static int MOBILE_FORGET = 4;

    public final static int EMAIL_BIND = 5;
    public final static int MOBILE_BIND = 6;

    public final static int FUND_PASS_CHANGE = 7;
    public final static int FUND_PASS_FORGET = 8;

    public final static int INVITE = 9;

    public final static int LOGIN_PASS_CHANGE = 10;
    public final static int LOGIN_PASS_FORGET = 11;

    public final static int WITHDRAWAL = 12;

    public final static int MOBILE_CHANGE = 13;
    public final static int EMAIL_CHANGE = 14;
    public final static int TRANSFER = 15;


    /*设置提现账户
      预计结束的时间*/
    /*注册*/
    private static long EMAIL_REGISTER_TIME_END = 0;
    private static long MOBILE_REGISTER_TIME_END = 0;

    /*邮箱忘记密码*/
    private static long EMAIL_FORGET_TIME_END = 0;
    private static long MOBILE_FORGET_TIME_END = 0;

    private static long EMAIL_BIND_TIME_END = 0;
    private static long MOBILE_BIND_TIME_END = 0;

    private static long FUND_PASS_CHANGE_TIME_END = 0;
    private static long FUND_PASS_FORGET_TIME_END = 0;

    private static long INVITE_TIME_END = 0;

    private static long LOGIN_PASS_CHANGE_TIME_END = 0;
    private static long LOGIN_PASS_FORGET_TIME_END = 0;

    private static long WITHDRAWAL_TIME_END = 0;

    private static Timer countdownTimer;
    private static TextView tvSendCode;
    private static long MOBILE_CHANGE_TIME_END = 0;
    private static long EMAIL_CHANGE_TIME_END = 0;
    private static long TRANSFER_TIME_END = 0;

    /**
     * 检查是否超过60秒
     * 给当前要从多少开始倒数赋值
     * http://blog.csdn.net/qq_16965811
     *
     * @param type  1，设置提现账户 2，注册3，忘记密码
     * @param first true 表示第一次   false不是
     * @return 是否需要调用startCountdown(TextView textView)，主要用于判断在重新打开页，需不需要继续倒计时
     */
    public static boolean check(int type, boolean first) {
        long data = System.currentTimeMillis();
        long time = 0;
        switch (type) {
            case EMAIL_REGISTER:
                time = EMAIL_REGISTER_TIME_END;
                break;
            case MOBILE_REGISTER:
                time = MOBILE_REGISTER_TIME_END;
                break;
            case EMAIL_FORGET:
                time = EMAIL_FORGET_TIME_END;
                break;
            case MOBILE_FORGET:
                time = MOBILE_FORGET_TIME_END;
                break;
            case EMAIL_BIND:
                time = EMAIL_BIND_TIME_END;
                break;
            case MOBILE_BIND:
                time = MOBILE_BIND_TIME_END;
                break;
            case FUND_PASS_CHANGE:
                time = FUND_PASS_CHANGE_TIME_END;
                break;
            case FUND_PASS_FORGET:
                time = FUND_PASS_FORGET_TIME_END;
                break;
            case INVITE:
                time = INVITE_TIME_END;
                break;
            case LOGIN_PASS_CHANGE:
                time = LOGIN_PASS_CHANGE_TIME_END;
                break;
            case LOGIN_PASS_FORGET:
                time = LOGIN_PASS_FORGET_TIME_END;
                break;
            case WITHDRAWAL:
                time = WITHDRAWAL_TIME_END;
                break;
            case MOBILE_CHANGE:
                time = MOBILE_CHANGE_TIME_END;
                break;
            case EMAIL_CHANGE:
                time = EMAIL_CHANGE_TIME_END;
                break;
            case TRANSFER:
                time=TRANSFER_TIME_END;
                break;
        }
        if (data > time) {
            /*主要是区别于是否是第一次进入。第一次进入不需要赋值*/
            if (!first) {
                CURR_COUNT = COUNT;
                time = data + COUNT * 1000;
                switch (type) {
                    case EMAIL_REGISTER:
                        EMAIL_REGISTER_TIME_END = time;
                        break;
                    case MOBILE_REGISTER:
                        MOBILE_REGISTER_TIME_END = time;
                        break;
                    case EMAIL_FORGET:
                        EMAIL_FORGET_TIME_END = time;
                        break;
                    case MOBILE_FORGET:
                        MOBILE_FORGET_TIME_END = time;
                        break;
                    case EMAIL_BIND:
                        EMAIL_BIND_TIME_END = time;
                        break;
                    case MOBILE_BIND:
                        MOBILE_BIND_TIME_END = time;
                        break;
                    case FUND_PASS_CHANGE:
                        FUND_PASS_CHANGE_TIME_END = time;
                        break;
                    case FUND_PASS_FORGET:
                        FUND_PASS_FORGET_TIME_END = time;
                        break;
                    case INVITE:
                        INVITE_TIME_END = time;
                        break;
                    case LOGIN_PASS_CHANGE:
                        LOGIN_PASS_CHANGE_TIME_END = time;
                        break;
                    case LOGIN_PASS_FORGET:
                        LOGIN_PASS_FORGET_TIME_END = time;
                        break;
                    case WITHDRAWAL:
                        WITHDRAWAL_TIME_END = time;
                        break;
                    case MOBILE_CHANGE:
                        MOBILE_CHANGE_TIME_END = time;
                        break;
                    case EMAIL_CHANGE:
                        EMAIL_CHANGE_TIME_END = time;
                        break;
                    case TRANSFER:
                        TRANSFER_TIME_END=time;
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
    public static void startCountdown(TextView textView) {
        tvSendCode = textView;
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
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer = null;
                }
                tvSendCode.setText("立即获取");
                tvSendCode.setEnabled(true);
            } else {
                tvSendCode.setText(msg.what + "s重新获取");
                tvSendCode.setEnabled(false);

            }
            super.handleMessage(msg);
        }
    };

}
