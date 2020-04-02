package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;

public class QuotePopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;
    private int type = 0;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;
    private String contCode=null;

    public QuotePopAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void select(String contCode) {
        this.contCode = contCode;
        this.notifyDataSetChanged();

    }

    public void addDatas(List<String> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_quote_pop_layout, parent, false);
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
            ((MyViewHolder) holder).text_price.setText(TradeUtil.listQuotePrice(datas.get(position)));
            ((MyViewHolder) holder).text_name.setText(TradeUtil.listQuoteName(datas.get(position)));
            ((MyViewHolder) holder).text_name_usdt.setText(TradeUtil.listQuoteUSD(datas.get(position)));

            String Code = TradeUtil.itemQuoteContCode(datas.get(position));
            if (contCode==null){
                return;
            }
            if (contCode.equals(Code)) {
                ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_pop_item_select));
                ((MyViewHolder) holder).text_name.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_main_color_black));
                ((MyViewHolder) holder).text_price.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_second_color_black));
                ((MyViewHolder) holder).text_name_usdt.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_second_color_black));

            } else {
                ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_pop_item));
                ((MyViewHolder) holder).text_name.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_maincolor));
                ((MyViewHolder) holder).text_price.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_second_color));
                ((MyViewHolder) holder).text_name_usdt.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_second_color));
            }


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
            bar =  itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_price, text_name_usdt;
        LinearLayout layout_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_price = itemView.findViewById(R.id.text_lastPrice);
            text_name_usdt = itemView.findViewById(R.id.text_name_usdt);
            layout_bg = itemView.findViewById(R.id.layout_bg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onSuccessListener(datas.get(getPosition()));
                    }
                }
            });
        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onSuccessListener(String data);

    }
}
