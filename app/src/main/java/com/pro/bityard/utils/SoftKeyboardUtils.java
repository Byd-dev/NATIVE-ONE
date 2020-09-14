package com.pro.bityard.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtils {
    /**
     * 关闭软件盘
     */
    public static void closeInoutDecorView(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

