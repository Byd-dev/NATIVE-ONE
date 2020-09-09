package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.ExchangeRecordEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ExchangeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ExchangeRecordEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public ExchangeRecordAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<ExchangeRecordEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<ExchangeRecordEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_exchange_record_layout, parent, false);
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

            String srcCurrency = datas.get(position).getSrcCurrency();
            String desCurrency = datas.get(position).getDesCurrency();


            ((MyViewHolder) holder).text_coin_in.setText(srcCurrency);


            ((MyViewHolder) holder).text_coin_out.setText(desCurrency);

            ChartUtil.setLeftImage(srcCurrency,((MyViewHolder) holder).text_coin_in);
            ChartUtil.setLeftImage(desCurrency,((MyViewHolder) holder).text_coin_out);

            ((MyViewHolder) holder).text_price.setText(TradeUtil.numberHalfUp(datas.get(position).getPrice(),4));

            ((MyViewHolder) holder).text_exchange_fee.setText(TradeUtil.numberHalfUp(datas.get(position).getFee(),2));

            ((MyViewHolder) holder).text_in_amount.setText(datas.get(position).getSrcMoney()+srcCurrency);

            ((MyViewHolder) holder).text_out_amount.setText(TradeUtil.numberHalfUp(datas.get(position).getDesMoney(),2)+desCurrency);

            ((MyViewHolder) holder).text_time.setText(ChartUtil.getDate(datas.get(position).getCreateTime()));

            ((MyViewHolder) holder).text_id.setText(datas.get(position).getId());


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
        TextView text_time, text_coin_in, text_in_amount, text_price,
                text_coin_out, text_out_amount, text_exchange_fee, text_id;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_time = itemView.findViewById(R.id.text_time);
            text_coin_in = itemView.findViewById(R.id.text_coin_in);
            text_in_amount = itemView.findViewById(R.id.text_in_amount);
            text_price = itemView.findViewById(R.id.text_price);
            text_coin_out = itemView.findViewById(R.id.text_coin_out);
            text_out_amount = itemView.findViewById(R.id.text_out_amount);
            text_exchange_fee = itemView.findViewById(R.id.text_exchange_fee);
            text_id = itemView.findViewById(R.id.text_id);


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
        void onClickListener(ExchangeRecordEntity.DataBean data);


    }
}
