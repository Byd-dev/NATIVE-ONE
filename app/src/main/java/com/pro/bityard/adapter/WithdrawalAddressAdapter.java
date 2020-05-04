package com.pro.bityard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class WithdrawalAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<WithdrawalAdressEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public WithdrawalAddressAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<WithdrawalAdressEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<WithdrawalAdressEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_withdrawal_address_layout, parent, false);
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


            ((MyViewHolder) holder).text_name.setText(datas.get(position).getCurrency());

            ((MyViewHolder) holder).text_chain.setText(datas.get(position).getChain());

            ((MyViewHolder) holder).text_userName.setText(datas.get(position).getRemark());
            ((MyViewHolder) holder).text_address.setText(datas.get(position).getAddress());
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
        TextView text_name, text_userName, text_chain, text_address;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_userName = itemView.findViewById(R.id.text_userName);
            text_chain = itemView.findViewById(R.id.text_chain);
            text_address = itemView.findViewById(R.id.text_address);


            itemView.findViewById(R.id.layout_address).setOnClickListener(v -> {
                if (onCopyClick != null) {
                    onCopyClick.onClickListener(datas.get(getAdapterPosition()));
                }
            });

            itemView.findViewById(R.id.img_edit).setOnClickListener(v -> {
                if (onEditClick != null) {
                    onEditClick.onClickListener(datas.get(getAdapterPosition()));
                }
            });


        }
    }

    private OnCopyClick onCopyClick;


    public void setCopyClick(OnCopyClick onCopyClick) {
        this.onCopyClick = onCopyClick;
    }

    public interface OnCopyClick {
        void onClickListener(WithdrawalAdressEntity.DataBean data);
    }

    private OnEditClick onEditClick;

    public void setOnEditClick(OnEditClick onEditClick) {
        this.onEditClick = onEditClick;
    }
    public interface OnEditClick {
        void onClickListener(WithdrawalAdressEntity.DataBean data);

    }
}
