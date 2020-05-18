package com.pro.bityard.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.manger.QuoteListManger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
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
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
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
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }


    private List<String> topList = new ArrayList<>();
    private List<String> midList = new ArrayList<>();
    private List<String> lowList = new ArrayList<>();


    /*显示详情*/
    public void showShare(Activity activity, View layout_view, HistoryEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_share_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        double opPrice = dataBean.getOpPrice();
        double cpPrice = dataBean.getCpPrice();
        //服务费
        double lever = dataBean.getLever();
        double margin = dataBean.getMargin();
        double income = dataBean.getIncome();


        TextView text_name = view.findViewById(R.id.text_name);
        String[] split = Util.quoteList(dataBean.getContractCode()).split(",");
        text_name.setText(split[0]);
        TextView text_currency = view.findViewById(R.id.text_currency);
        text_currency.setText("/" + dataBean.getCurrency());
        //开仓价
        TextView text_open_price = view.findViewById(R.id.text_open_price);
        text_open_price.setText(String.valueOf(opPrice));
        //平仓价
        TextView text_close_price = view.findViewById(R.id.text_close_price);
        text_close_price.setText(String.valueOf(cpPrice));
        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(lever + "×");
        TextView text_rate = view.findViewById(R.id.text_rate);
        text_rate.setText(TradeUtil.ratio(income, margin));

        if (income > 0) {
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_green));

        } else {
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_red));

        }

        topList.add(activity.getString(R.string.text_wonderful_unrivalled));
        topList.add(activity.getString(R.string.text_king));
        topList.add(activity.getString(R.string.text_awesome));

        midList.add(activity.getString(R.string.text_set_goal));
        midList.add(activity.getString(R.string.text_one_step));
        midList.add(activity.getString(R.string.text_get_less));

        lowList.add(activity.getString(R.string.text_stop_loss));
        lowList.add(activity.getString(R.string.text_do_hesitate));
        lowList.add(activity.getString(R.string.text_just_case));


        Random random = new Random();
        int top = random.nextInt(topList.size());
        int mid = random.nextInt(midList.size());
        int low = random.nextInt(lowList.size());


        TextView text_title = view.findViewById(R.id.text_title);

        ImageView img_person=view.findViewById(R.id.img_person);
        double rate = TradeUtil.ratioDouble(income, margin);
        if (rate >= 0 && rate < 100) {
            text_title.setText(midList.get(mid));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_win));
        } else if (rate >= 100) {
            text_title.setText(topList.get(top));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_win));

        } else if (rate < 0) {
            text_title.setText(lowList.get(low));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_loss));

        }


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }

}
