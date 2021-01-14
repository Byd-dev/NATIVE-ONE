package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SpotPositionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SpotPositionEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public SpotPositionAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SpotPositionEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<SpotPositionEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_spot_position_layout, parent, false);
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
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getDesCurrency() + "/" + datas.get(position).getSrcCurrency());
            ((MyViewHolder) holder).text_time.setText(TradeUtil.dateToStamp(datas.get(position).getCreateTime()));


            ((MyViewHolder) holder).text_price.setText(TradeUtil.getNumberFormat(datas.get(position).getPrice(), 2));


            ((MyViewHolder) holder).text_amount.setText(datas.get(position).getVolume() + "/"
                    + datas.get(position).getAmount());



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
        TextView text_name, text_currency, text_time, text_amount, text_price;

        ImageView img_buy;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_amount = itemView.findViewById(R.id.text_amount);
            text_price = itemView.findViewById(R.id.text_price);
            text_time = itemView.findViewById(R.id.text_time);

            itemView.findViewById(R.id.text_cancel).setOnClickListener(v -> {
                if (onDetailClick!=null){
                    onDetailClick.onClickListener(datas.get(getAdapterPosition()));
                }
            });




        }
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(SpotPositionEntity.DataBean data);


    }
}
