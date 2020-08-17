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
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.entity.InviteListEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class InviteRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<InviteListEntity.DataBean> datas;
    private double rate;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public InviteRecordAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<InviteListEntity.DataBean> datas, double rate) {
        this.datas = datas;
        this.rate = rate;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<InviteListEntity.DataBean> datas, double rate) {
        this.rate = rate;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_invite_history_layout, parent, false);
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


            ((MyViewHolder) holder).text_name.setText(datas.get(position).getUsername());


           /* boolean isBuy = datas.get(position).isIsBuy();
            if (isBuy) {
                ((MyViewHolder) holder).text_buy_tip.setText(R.string.text_buy_much);
                ((MyViewHolder) holder).text_buy_tip.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_green_record));
            } else {
                ((MyViewHolder) holder).text_buy_tip.setText(R.string.text_buy_empty);
                ((MyViewHolder) holder).text_buy_tip.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_red_record));

            }*/

            //内部转账所有用户都可以
           /* if (rate >= 5) {
                ((MyViewHolder) holder).text_buy_tip.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).text_buy_tip.setVisibility(View.GONE);
            }*/


            ((MyViewHolder) holder).text_time.setText(context.getString(R.string.text_last_login) + ChartUtil.getDate(datas.get(position).getLoginTime()));
            ((MyViewHolder) holder).text_income.setText(TradeUtil.getNumberFormat(datas.get(position).getCommission(), 2));


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
        TextView text_name, text_buy_tip, text_time,
                text_income, text_amount;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_buy_tip = itemView.findViewById(R.id.text_buy_tip);
            text_time = itemView.findViewById(R.id.text_time);
            text_income = itemView.findViewById(R.id.text_income);
            text_amount = itemView.findViewById(R.id.text_amount);


            itemView.findViewById(R.id.layout_transfer).setOnClickListener(view -> {
                if (onTransferClick != null) {
                    onTransferClick.onClickListener(datas.get(getPosition()));
                }
            });
            itemView.findViewById(R.id.layout_detail).setOnClickListener(view -> {
                if (onDetailClick != null) {
                    onDetailClick.onClickListener(datas.get(getPosition()));
                }
            });

        }
    }

    private OnTransferClick onTransferClick;

    public void setOnTransferClick(OnTransferClick onTransferClick) {
        this.onTransferClick = onTransferClick;
    }

    public interface OnTransferClick {
        void onClickListener(InviteListEntity.DataBean data);

    }

    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(InviteListEntity.DataBean data);
    }
}
