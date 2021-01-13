package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.SpotTradeHistoryEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SpotTradeHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SpotTradeHistoryEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public SpotTradeHistoryAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SpotTradeHistoryEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<SpotTradeHistoryEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_spot_trade_history_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHoler(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getDesCurrency() + "/" + datas.get(position).getSrcCurrency());
            ((MyViewHolder) holder).text_time.setText(TradeUtil.dateToStamp(datas.get(position).getCreateTime()));


            ((MyViewHolder) holder).text_price.setText(String.valueOf(datas.get(position).getOpPrice()));


            ((MyViewHolder) holder).text_service.setText(TradeUtil.justDisplay(datas.get(position).getCharge()));


            ((MyViewHolder) holder).text_amount.setText(String.valueOf(datas.get(position).getOpAmount()));
            ((MyViewHolder) holder).text_amount_all.setText(String.valueOf(datas.get(position).getOpVolume()));
            Boolean buy = datas.get(position).getBuy();
            if (buy) {
                ((MyViewHolder) holder).text_buy.setText(context.getApplicationContext().getText(R.string.text_buy_tip));
                ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.text_quote_green));
            } else {
                ((MyViewHolder) holder).text_buy.setText(R.string.text_sell_tip);
                ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.text_quote_red));

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

    public static class ProgressViewHoler extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHoler(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_buy, text_name, text_currency, text_time, text_amount, text_price, text_service, text_amount_all;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_buy = itemView.findViewById(R.id.text_buy);
            text_name = itemView.findViewById(R.id.text_name);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_time = itemView.findViewById(R.id.text_time);
            text_price = itemView.findViewById(R.id.text_price);
            text_service = itemView.findViewById(R.id.text_service);
            text_amount = itemView.findViewById(R.id.text_amount);
            text_amount_all = itemView.findViewById(R.id.text_amount_all);


        }
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(SpotTradeHistoryEntity.DataBean data);


    }
}
