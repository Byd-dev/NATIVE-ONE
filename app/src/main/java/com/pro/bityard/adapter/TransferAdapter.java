package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TransferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DepositWithdrawEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public TransferAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<DepositWithdrawEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<DepositWithdrawEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_layout, parent, false);
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
            String explain = datas.get(position).getExplain();


            int status = datas.get(position).getStatus();


            if (explain.contains("取出")){
                ((MyViewHolder) holder).text_explain.setText(R.string.text_out);
            }else if (explain.contains("入")){
                ((MyViewHolder) holder).text_explain.setText(R.string.text_in);
            }

            if (status==1){
                ((MyViewHolder) holder).text_money.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                ((MyViewHolder) holder).text_currency.setTextColor(context.getResources().getColor(R.color.text_quote_green));
            }else {
                ((MyViewHolder) holder).text_money.setTextColor(context.getResources().getColor(R.color.text_main_color));
                ((MyViewHolder) holder).text_currency.setTextColor(context.getResources().getColor(R.color.text_main_color));
            }




            ((MyViewHolder) holder).text_time.setText(ChartUtil.getDate(datas.get(position).getCreateTime()));
            ((MyViewHolder) holder).text_money.setText(String.valueOf(datas.get(position).getMoney()));
            ((MyViewHolder) holder).text_currency.setText(datas.get(position).getCurrency());
            ((MyViewHolder) holder).text_address.setText(datas.get(position).getBankCard());


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
        TextView text_explain, text_time, text_address,
                text_money, text_currency;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_explain = itemView.findViewById(R.id.text_explain);
            text_address = itemView.findViewById(R.id.text_address);
            text_time = itemView.findViewById(R.id.text_time);
            text_money = itemView.findViewById(R.id.text_money);
            text_currency = itemView.findViewById(R.id.text_currency);


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
        void onClickListener(DepositWithdrawEntity.DataBean data);


    }
}
