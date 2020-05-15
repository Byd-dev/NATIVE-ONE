package com.pro.bityard.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
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
import com.pro.bityard.adapter.PositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.manger.QuoteListManger;

import java.util.List;

import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.utils.TradeUtil.income;
import static com.pro.bityard.utils.TradeUtil.netIncome;
import static com.pro.bityard.utils.TradeUtil.price;

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


    /*显示详情*/
    public void showDetailPopWindow(Activity activity, View layout_view, PositionEntity.DataBean dataBean, List<String> quoteList) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_position_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_positions);

        TextView text_name = view.findViewById(R.id.text_name);

        String[] split = Util.quoteList(dataBean.getContractCode()).split(",");
        text_name.setText(split[0]);
        TextView text_currency = view.findViewById(R.id.text_currency);
        text_currency.setText(dataBean.getCurrency());
        //盈利
        TextView text_income=view.findViewById(R.id.text_income);
        //净收入
        TextView text_worth=view.findViewById(R.id.text_worth);
        //当前价
        TextView text_price=view.findViewById(R.id.text_price);
        //开盘价
        double opPrice = dataBean.getOpPrice();
        TextView text_opPrice = view.findViewById(R.id.text_open_price);
        text_opPrice.setText(String.valueOf(opPrice));
        //成交量
        double volume = dataBean.getVolume();
        //服务费
        double serviceCharge = dataBean.getServiceCharge();

        boolean isBuy = dataBean.isIsBuy();
        ImageView img_bg = view.findViewById(R.id.img_bg);

        if (isBuy) {
            img_bg.setBackgroundResource(R.mipmap.icon_up);
        } else {
            img_bg.setBackgroundResource(R.mipmap.icon_down);

        }
        //现价和盈亏
        price(quoteList, dataBean.getContractCode(), response -> {
            text_price.setText(response.toString());
            String income = income(isBuy, Double.parseDouble(response.toString()), opPrice, volume);
            text_income.setText(income);
            double incomeDouble = Double.parseDouble(income);
            String netIncome = netIncome(incomeDouble,serviceCharge);
            double netIncomeDouble = Double.parseDouble(netIncome);

           text_worth.setText(netIncome);
            if (incomeDouble > 0) {
                text_income.setTextColor(activity.getResources().getColor(R.color.text_quote_green));
            } else {
                text_income.setTextColor(activity.getResources().getColor(R.color.text_quote_red));
            }

            if (netIncomeDouble > 0) {
                text_worth.setTextColor(activity.getResources().getColor(R.color.text_quote_green));
            } else {
                text_worth.setTextColor(activity.getResources().getColor(R.color.text_quote_red));
            }

        });





        TextView text_profit = view.findViewById(R.id.text_profit);




        TextView text_side = view.findViewById(R.id.text_side);
        if (isBuy) {
            text_side.setText(R.string.text_buy_much);
        } else {
            text_side.setText(R.string.text_buy_empty);
        }

        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(dataBean.getMargin()));

        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(dataBean.getLever()));
        TextView text_orders = view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));

        TextView text_clPrice = view.findViewById(R.id.text_close_price);
        text_clPrice.setText(String.valueOf(dataBean.getCpPrice()));
        TextView text_fee = view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge));
        TextView text_o_n = view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));
        TextView text_o_n_fee = view.findViewById(R.id.text_o_n_fee);
        text_o_n_fee.setText(String.valueOf(dataBean.getDeferFee()));
        TextView text_open_time = view.findViewById(R.id.text_open_time);
        text_open_time.setText(ChartUtil.getDate(dataBean.getTime()));
        TextView text_close_time = view.findViewById(R.id.text_close_time);
        text_close_time.setText(ChartUtil.getDate(dataBean.getTradeTime()));
        TextView text_order_id = view.findViewById(R.id.text_order_id);
        text_order_id.setText(dataBean.getId());


        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }

}
