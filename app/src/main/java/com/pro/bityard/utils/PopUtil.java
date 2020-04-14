package com.pro.bityard.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.manger.QuoteListManger;

public class PopUtil {
    private static PopUtil popUtil;


    public static PopUtil getInstance() {
        if (popUtil == null) {
            synchronized (QuoteListManger.class) {
                if (popUtil == null) {
                    popUtil = new PopUtil();
                }
            }

        }
        return popUtil;

    }

    public void showTip(Activity activity, View layout_view, boolean showCancel, String content, OnPopResult onPopResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_tip_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_tip = view.findViewById(R.id.text_content);
        text_tip.setLineSpacing(1.1f, 1.5f);

        text_tip.setText(content);


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            onPopResult.setPopResult(true);
            popupWindow.dismiss();
            backgroundAlpha(activity);
        });

        TextView text_cancel = view.findViewById(R.id.text_cancel);
        if (showCancel) {
            text_cancel.setVisibility(View.VISIBLE);
        } else {
            text_cancel.setVisibility(View.GONE);

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            onPopResult.setPopResult(false);
            popupWindow.dismiss();
            backgroundAlpha(activity);
        });
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.6f;
        activity.getWindow().setAttributes(params);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
       // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    private void backgroundAlpha(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = (float) 1.0;
        activity.getWindow().setAttributes(lp);


    }

}
