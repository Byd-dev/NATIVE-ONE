package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TradeSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;
    private Integer index = 0;

    public TradeSelectAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void select(Integer index) {
        this.index = index;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_radio_fund_layout, parent, false);
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
            if (position != 0) {
                if (datas.get(position).length()>4)
                {
                    String substring = datas.get(position).substring(0, datas.get(position).length() - 4);
                    ((MyViewHolder) holder).text_name.setText(substring);
                    ChartUtil.setIcon(substring, ((MyViewHolder) holder).img_bg);
                }else{
                    String substring = datas.get(position);
                    ((MyViewHolder) holder).text_name.setText(substring);
                    ChartUtil.setIcon(substring, ((MyViewHolder) holder).img_bg);
                }
            }


            if (index == position) {
                ((MyViewHolder) holder).layout_usd.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_fund_bg));
            } else {
                ((MyViewHolder) holder).layout_usd.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_fund_normal_bg));

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
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_usd;
        TextView text_name;
        ImageView img_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            layout_usd = itemView.findViewById(R.id.layout_usd);
            text_name = itemView.findViewById(R.id.text_name);
            img_bg = itemView.findViewById(R.id.img_bg);

            layout_usd.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onSuccessListener(getAdapterPosition(), datas.get(getPosition()));

                }
            });
        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onSuccessListener(Integer position, String data);

    }
}
