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
import com.pro.bityard.entity.MarginHistoryEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MarginHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MarginHistoryEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;

    private boolean isHide = true;


    public MarginHistoryAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<MarginHistoryEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<MarginHistoryEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_margin_history_layout, parent, false);
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
        ((MyViewHolder) holder).text_time.setText(ChartUtil.getDate(datas.get(position).getCreateTime()));

        if (position==0){
            ((MyViewHolder) holder).text_tip.setText(R.string.text_open_tip);
            ((MyViewHolder) holder).text_margin_tip.setText(R.string.text_margin_start);
            ((MyViewHolder) holder).text_lever_tip.setText(R.string.text_initial_leverage);
            ((MyViewHolder) holder).text_tip.setTextColor(context.getResources().getColor(R.color.text_maincolor));
        }else {
            ((MyViewHolder) holder).text_tip.setText(String.valueOf(position));
            ((MyViewHolder) holder).text_margin_tip.setText(R.string.text_additional_margin);
            ((MyViewHolder) holder).text_lever_tip.setText(R.string.text_lever);
            ((MyViewHolder) holder).text_tip.setTextColor(context.getResources().getColor(R.color.maincolor));

        }


        ((MyViewHolder) holder).text_margin.setText(String.valueOf(datas.get(position).getMargin()));

        ((MyViewHolder) holder).text_lever.setText(String.valueOf(datas.get(position).getLever()));





        }
    }

    private void getRate(String currency, double money, OnResult onResult) {
        RateListEntity rateListEntity = SPUtils.getData(AppConfig.RATE_LIST, RateListEntity.class);
        if (rateListEntity!=null){
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
        TextView text_tip, text_time,text_margin_tip,text_margin,text_lever_tip,text_lever;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_tip = itemView.findViewById(R.id.text_tip);
            text_time = itemView.findViewById(R.id.text_time);
            text_margin_tip = itemView.findViewById(R.id.text_margin_tip);
            text_margin = itemView.findViewById(R.id.text_margin);
            text_lever_tip = itemView.findViewById(R.id.text_lever_tip);
            text_lever = itemView.findViewById(R.id.text_lever);


        }
    }

    private OnDetailClick detailClick;


    public void setDetailClick(OnDetailClick detailClick) {
        this.detailClick = detailClick;
    }

    public interface OnDetailClick {
        void onClickListener(MarginHistoryEntity.DataBean data);
    }


}
