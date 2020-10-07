package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.entity.CopyMangerEntity;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CopyMangerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CopyMangerEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;


    public CopyMangerAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CopyMangerEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<CopyMangerEntity.DataBean> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;


        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_copy_manger_layout, parent, false);
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

            Glide.with(context).load(datas.get(position).getTraderAvatar()).error(R.mipmap.icon_my_bityard).into(((MyViewHolder) holder).img_head);
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getTraderName());
            String value_type = null;
            String type = datas.get(position).getTraderType();
            switch (type) {
                case "1":
                    value_type = context.getString(R.string.text_normal_user);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_user), null, null, null);

                    break;
                case "2":
                    value_type = context.getString(R.string.text_normal_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_trader), null, null, null);
                    break;
                case "3":
                    value_type = context.getString(R.string.text_pro_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);

                    break;
            }

            ((MyViewHolder) holder).text_type.setText(value_type);



            ((MyViewHolder) holder).text_copy_total_amount.setText(String.valueOf(datas.get(position).getSumMargin()));

            ((MyViewHolder) holder).text_copy_trade_profit.setText(TradeUtil.getNumberFormat(datas.get(position).getSumIncome(), 2));

            ((MyViewHolder) holder).text_fixed_margin.setText(String.valueOf(datas.get(position).getFollowVal()));

            ((MyViewHolder) holder).text_day_copy_amount.setText(String.valueOf(datas.get(position).getMaxDay()));

            ((MyViewHolder) holder).text_max_position_amount.setText(String.valueOf(datas.get(position).getMaxHold()));

            ((MyViewHolder) holder).stay_copy_total_amount.setText(context.getResources().getString(R.string.text_copy_total_amount)+"(USDT)");

            ((MyViewHolder) holder).stay_copy_trade_profit.setText(context.getResources().getString(R.string.text_copy_trade_profit)+"(USDT)");

            ((MyViewHolder) holder).text_edit.setOnClickListener(v -> {
                if (onFollowClick != null) {
                    onFollowClick.onClickListener(datas.get(position));
                }
            });
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
        TextView text_name, text_copy_total_amount, stay_copy_total_amount,text_copy_trade_profit,stay_copy_trade_profit, text_fixed_margin,
                text_day_copy_amount, text_max_position_amount,  text_type, text_edit;
        CircleImageView img_head;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_userName);
            text_copy_total_amount = itemView.findViewById(R.id.text_copy_total_amount);
            stay_copy_total_amount = itemView.findViewById(R.id.stay_copy_total_amount);
            stay_copy_trade_profit=itemView.findViewById(R.id.stay_copy_trade_profit);
            text_copy_trade_profit = itemView.findViewById(R.id.text_copy_trade_profit);
            text_fixed_margin = itemView.findViewById(R.id.text_fixed_margin);
            text_day_copy_amount = itemView.findViewById(R.id.text_day_copy_amount);
            text_max_position_amount = itemView.findViewById(R.id.text_max_position_amount);
            img_head = itemView.findViewById(R.id.img_head);
            text_type = itemView.findViewById(R.id.text_type);
            text_edit = itemView.findViewById(R.id.text_edit);




        }
    }

    //查看详情的监听
    private OnFollowClick onFollowClick;

    public void setOnFollowClick(OnFollowClick onFollowClick) {
        this.onFollowClick = onFollowClick;
    }

    public interface OnFollowClick {
        void onClickListener(CopyMangerEntity.DataBean dataBean);


    }



}
