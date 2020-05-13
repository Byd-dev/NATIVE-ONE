package com.pro.bityard.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.switchlibrary.SPUtils;

import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

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
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    public void showEdit(Activity activity, View layout_view, boolean showCancel, OnResult onResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_edit_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText edit_username = view.findViewById(R.id.edit_username);


        if (edit_username.getText().toString().equals("")) {

        }


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            NetManger.getInstance().updateNickName(edit_username.getText().toString(), (state, response) -> {
                if (state.equals(SUCCESS)) {
                    onResult.setResult(edit_username.getText().toString());
                    popupWindow.dismiss();


                } else if (state.equals(FAILURE)) {
                    Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        TextView text_cancel = view.findViewById(R.id.text_cancel);
        if (showCancel) {
            text_cancel.setVisibility(View.VISIBLE);
        } else {
            text_cancel.setVisibility(View.GONE);

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            onResult.setResult(null);
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

}
