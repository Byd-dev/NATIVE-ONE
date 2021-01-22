package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.FollowHistoryEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FollowHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FollowHistoryEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public FollowHistoryAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<FollowHistoryEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<FollowHistoryEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_follow_history_layout, parent, false);
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
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getCommodityCode());
            ((MyViewHolder) holder).text_margin.setText("×" + datas.get(position).getMargin());

            double opPrice = datas.get(position).getOpPrice();

            double cpPrice = datas.get(position).getCpPrice();

            boolean isBuy = datas.get(position).isIsBuy();







            if (isBuy) {
                ((MyViewHolder) holder).text_buy.setText(context.getResources().getString(R.string.text_buy_much));
            } else {
                ((MyViewHolder) holder).text_buy.setText(context.getResources().getString(R.string.text_buy_empty));


            }

            //开仓价
            ((MyViewHolder) holder).text_open_price.setText(String.valueOf(opPrice));
            //平仓价
            ((MyViewHolder) holder).text_close_price.setText(String.valueOf(cpPrice));


            ((MyViewHolder) holder).text_open_time.setText(TradeUtil.dateToStampHour(datas.get(position).getTradeTime()));

            ((MyViewHolder) holder).text_close_time.setText(TradeUtil.dateToStampHour(datas.get(position).getTime()));

            ((MyViewHolder) holder).text_order_id.setText(datas.get(position).getId());

            ((MyViewHolder) holder).text_order_type.setText(datas.get(position).getOpenSource());

            double income = datas.get(position).getIncome();

            ((MyViewHolder) holder).text_income_rate.setText(TradeUtil.getNumberFormat(income,2)+"%");
            if (income > 0) {
                ((MyViewHolder) holder).text_income_rate.setTextColor(context.getResources().getColor(R.color.text_quote_green));

            } else {
                ((MyViewHolder) holder).text_income_rate.setTextColor(context.getResources().getColor(R.color.text_quote_red));

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
        TextView text_name, text_margin, text_open_price,
                text_buy, text_close_price, text_income_rate,
                text_open_time, text_close_time, text_order_id, text_order_type,text_rate_tip;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_margin = itemView.findViewById(R.id.text_margin);
            text_open_price = itemView.findViewById(R.id.text_open_price);
            text_close_price = itemView.findViewById(R.id.text_close_price);
            text_buy = itemView.findViewById(R.id.text_buy);
            text_rate_tip=itemView.findViewById(R.id.text_rate_tip);


            text_income_rate = itemView.findViewById(R.id.text_income_rate);
            text_open_time = itemView.findViewById(R.id.text_open_time);
            text_close_time = itemView.findViewById(R.id.text_close_time);
            text_order_id = itemView.findViewById(R.id.text_order_id);
            text_order_type = itemView.findViewById(R.id.text_order_type);




        }
    }

    private OnShareClick onShareClick;

    public void setOnShareClick(OnShareClick onShareClick) {
        this.onShareClick = onShareClick;
    }

    public interface OnShareClick {
        void onClickListener(FollowHistoryEntity.DataBean data);

    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(FollowHistoryEntity.DataBean data);


    }
}
