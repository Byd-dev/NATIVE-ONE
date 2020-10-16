package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.FollowersListEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FollowerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FollowersListEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public FollowerListAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<FollowersListEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<FollowersListEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_follower_list_layout, parent, false);
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

            ((MyViewHolder) holder).text_id.setText(datas.get(position).getUsername());

            ((MyViewHolder) holder).text_copy_trade_amount_item.setText(String.valueOf(datas.get(position).getSumMargin()));


            double income = datas.get(position).getSumIncome();

            ((MyViewHolder) holder).text_income_rate.setText(TradeUtil.getNumberFormat(income, 2) + "%");
            if (income > 0) {
                ((MyViewHolder) holder).text_income_rate.setTextColor(context.getResources().getColor(R.color.text_quote_green));

            } else {
                ((MyViewHolder) holder).text_income_rate.setTextColor(context.getResources().getColor(R.color.text_quote_red));

            }


            Glide.with(context.getApplicationContext())
                    .load(datas.get(position).getAvatar())
                    .error(context.getResources().getDrawable(R.mipmap.icon_my_bityard))
                    .into(((MyViewHolder) holder).img_head);

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
        TextView text_id, text_copy_trade_amount_item,
                text_income_rate;
        CircleImageView img_head;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            text_id = itemView.findViewById(R.id.text_id);
            text_copy_trade_amount_item = itemView.findViewById(R.id.text_copy_trade_amount_item);
            text_income_rate = itemView.findViewById(R.id.text_income_rate);


        }
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(FollowersListEntity.DataBean data);


    }
}
