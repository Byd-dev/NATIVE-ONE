package com.pro.bityard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class QuickAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BalanceEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;

    private boolean isHide = true;
    private int scale;


    public QuickAccountAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<BalanceEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<BalanceEntity.DataBean> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
        this.notifyDataSetChanged();
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }


    public void startLoad() {
        isLoadMore = true;
        this.notifyDataSetChanged();
    }

    public void stopLoad() {
        isLoadMore = false;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;


        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_quick_account_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHolder(view);
        return holder;


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            double money = datas.get(position).getMoney();

            String currency = datas.get(position).getCurrency();

            ((MyViewHolder) holder).text_currency.setText(currency);
            ChartUtil.setIcon(currency, ((MyViewHolder) holder).img_bg);

            TradeUtil.getScale(datas.get(position).getCurrency(), response -> {
                scale = (int) response;
            });

            if (isHide) {
                if (currency.equals("USDT")) {
                    ((MyViewHolder) holder).text_balance.setText(TradeUtil.numberHalfUp(money, 2) + currency);
                } else {
                    getRate(currency, money, response -> {
                        ((MyViewHolder) holder).text_balance.setText(TradeUtil.numberHalfUp(money, scale) + currency + "≈"
                                + response.toString() + "USDT");
                    });
                }

            } else {
                ((MyViewHolder) holder).text_balance.setText("***≈***");
            }

        }
    }

    private void getRate(String currency, double money, OnResult onResult) {
        RateListEntity rateListEntity = SPUtils.getData(AppConfig.RATE_LIST, RateListEntity.class);
        if (rateListEntity != null) {
            for (RateListEntity.ListBean rateList : rateListEntity.getList()) {
                if (currency.equals(rateList.getName())) {

                    double mul = TradeUtil.mul(money, rateList.getValue());
                    onResult.setResult(TradeUtil.getNumberFormat(mul, 2));
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && isLoadMore) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;

    }

    @Override
    public int getItemCount() {
        if (isLoadMore) {
            return datas.size() + 1;
        }
        return datas.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_currency, text_balance;
        ImageView img_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_balance = itemView.findViewById(R.id.text_balance);
            img_bg = itemView.findViewById(R.id.img_bg);


        }
    }

    private OnDetailClick detailClick;


    public void setDetailClick(OnDetailClick detailClick) {
        this.detailClick = detailClick;
    }

    public interface OnDetailClick {
        void onClickListener(BalanceEntity.DataBean data);
    }


}
