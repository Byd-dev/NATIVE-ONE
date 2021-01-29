package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.BuySellEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TradeNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isSell = false;

    public boolean isLoadMore = false;

    private double max;


    private List<Double> incomeList;


    public TradeNewAdapter(Context context) {
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

    public void isSell(boolean isSell) {
        this.isSell = isSell;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_trade_new_layout, parent, false);
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


            String price = datas.get(position).split(",")[1];
            String amount = datas.get(position).split(",")[2];

            ((MyViewHolder) holder).text_price.setText(price);
            ((MyViewHolder) holder).text_amount.setText(amount);
            String up = datas.get(position).split(",")[0];
            if (up.equals("1")){
                ((MyViewHolder) holder).text_price.setTextColor(context.getResources().getColor(R.color.text_quote_green));
            }else {
                ((MyViewHolder) holder).text_price.setTextColor(context.getResources().getColor(R.color.text_quote_red));

            }








        }

    }

    private double calculateAmountViewWidth(RelativeLayout layout_bg, double volume, double totalVolume) {
        if (totalVolume == 0) return 0;
        int width = layout_bg.getMeasuredWidth();
        double div = TradeUtil.div(volume, totalVolume, 1);
        double widthPx = TradeUtil.mul(div, width);
        return widthPx;
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
        TextView text_time,text_amount, text_price;



        public MyViewHolder(View itemView) {
            super(itemView);

            text_time = itemView.findViewById(R.id.text_time);
            text_price = itemView.findViewById(R.id.text_price);
            text_amount = itemView.findViewById(R.id.text_amount);



        }
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(String data);


    }
}
