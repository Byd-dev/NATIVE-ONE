package com.pro.bityard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TradeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TradeHistoryEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public TradeRecordAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<TradeHistoryEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<TradeHistoryEntity.DataBean> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
        this.notifyDataSetChanged();
    }

    public void sortPrice(boolean isHigh) {
        this.isHigh = isHigh;
        this.notifyDataSetChanged();
    }

    public void startLoad() {
        isLoadMore = true;
        this.notifyDataSetChanged();
    }

    public void stopLoad() {
        isLoadMore = false;
        this.notifyDataSetChanged();
    }


    public void getIncome(TradeResult tradeResult) {
        tradeResult.setResult(incomeList);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;


        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_trade_history_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHoler(view);
        return holder;


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            int length = datas.get(position).getCurrency().length();

            if (datas.get(position).getCommodityCode().length()>4){
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getCommodityCode().substring(0, datas.get(position).getCommodityCode().length() - length));
            }else {
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getCommodityCode());

            }


            ((MyViewHolder) holder).text_currency.setText(datas.get(position).getCurrency());

            boolean isBuy = datas.get(position).isIsBuy();
            if (isBuy) {
                ((MyViewHolder) holder).text_buy_tip.setText(R.string.text_buy_much);
                ((MyViewHolder) holder).text_buy_tip.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_green_record));
            } else {
                ((MyViewHolder) holder).text_buy_tip.setText(R.string.text_buy_empty);
                ((MyViewHolder) holder).text_buy_tip.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_red_record));

            }


            ((MyViewHolder) holder).text_time.setText(context.getString(R.string.text_close_time) + ChartUtil.getDate(datas.get(position).getTradeTime()));
            ((MyViewHolder) holder).text_amount.setText(String.valueOf(datas.get(position).getOpVolume()));
            ((MyViewHolder) holder).text_currency.setText(datas.get(position).getCurrency());
            double income = datas.get(position).getIncome();
            if (income > 0) {
                ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_green));
            } else {
                ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_red));
            }
            ((MyViewHolder) holder).text_income.setText(TradeUtil.getNumberFormat(income, 2) + "(" + context.getString(R.string.text_p_l) + ")");


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

    public static class ProgressViewHoler extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHoler(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_currency, text_buy_tip, text_time,
                text_income, text_amount;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_buy_tip = itemView.findViewById(R.id.text_buy_tip);
            text_time = itemView.findViewById(R.id.text_time);
            text_income = itemView.findViewById(R.id.text_income);
            text_amount = itemView.findViewById(R.id.text_amount);


            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onClickListener(datas.get(getPosition()));
                }
            });


        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onClickListener(TradeHistoryEntity.DataBean data);


    }
}
