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
import com.pro.bityard.api.OnResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.entity.WithdrawCurrencyEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class WithdrawCurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;

    private boolean isHide = true;
    private boolean hideSmall=false;
    private int scale;


    public WithdrawCurrencyAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<String> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
        this.notifyDataSetChanged();
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }

    public void hideSmallCoin(boolean hideSmall){
        this.hideSmall=hideSmall;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_withdraw_currency_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHolder(view);
        return holder;


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {



            String currency = datas.get(position);

            ((MyViewHolder) holder).text_currency.setText(currency);
            ChartUtil.setIcon(currency, ((MyViewHolder) holder).img_bg);










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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_currency;
        ImageView img_bg;
        LinearLayout layout_item;
        View line;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_currency = itemView.findViewById(R.id.text_currency);


            img_bg = itemView.findViewById(R.id.img_bg);
            layout_item = itemView.findViewById(R.id.layout_item);
            line = itemView.findViewById(R.id.line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailClick!=null){
                        detailClick.onClickListener(datas.get(getAdapterPosition()));
                    }
                }
            });


        }



    }

    private OnDetailClick detailClick;


    public void setDetailClick(OnDetailClick detailClick) {
        this.detailClick = detailClick;
    }

    public interface OnDetailClick {
        void onClickListener(String data);
    }


}
