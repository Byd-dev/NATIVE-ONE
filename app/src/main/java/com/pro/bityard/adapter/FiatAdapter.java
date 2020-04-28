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

public class FiatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DepositWithdrawEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public FiatAdapter(Context context) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_deposit_withdraw_layout, parent, false);
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
            switch (explain){
                case "提款取出":
                    ((MyViewHolder) holder).text_explain.setText(R.string.text_withdrawal);
                    break;
                case "充值存入":
                    ((MyViewHolder) holder).text_explain.setText(R.string.text_recharge);
                    break;
            }

            int status = datas.get(position).getStatus();
            switch (status) {
                case 0:
                case -1:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_pending);
                    break;
                case 1:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_tip_success);
                    break;
                case 2:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_failure);
                    break;
                case 3:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_tip_cancel);
                    break;
                case 4:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_processing);
                    break;
                case 5:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_in_progress);
                    break;
                case 6:
                    ((MyViewHolder) holder).text_state.setText(R.string.text_refunded);
                    break;
            }

            ((MyViewHolder) holder).text_time.setText(ChartUtil.getDate(datas.get(position).getCreateTime()));
            ((MyViewHolder) holder).text_money.setText(String.valueOf(datas.get(position).getSrcMoney()));
            ((MyViewHolder) holder).text_currency.setText(datas.get(position).getSrcCurrency());


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
        TextView text_explain, text_time, text_state,
                text_money, text_currency;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_explain = itemView.findViewById(R.id.text_explain);
            text_state = itemView.findViewById(R.id.text_state);
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
