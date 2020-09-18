package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.pro.bityard.R;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class StyleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TagEntity> datas;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;
    private String chain;
    private boolean isEnable = true;

    public StyleListAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<TagEntity> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void select(String chain) {
        this.chain = chain;
        this.notifyDataSetChanged();

    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<TagEntity> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_style_adapter_layout, parent, false);
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

            ((MyViewHolder) holder).checkBox_style.setText(datas.get(position).getContent());


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
        CheckBox checkBox_style;


        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox_style = itemView.findViewById(R.id.radio_style);


            checkBox_style.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onItemChaneClick != null) {
                    onItemChaneClick.onSuccessListener(isChecked, datas.get(getPosition()));
                }
            });


        }
    }

    private OnItemChaneClick onItemChaneClick;

    public void setOnItemChaneClick(OnItemChaneClick onItemChaneClick) {
        this.onItemChaneClick = onItemChaneClick;
    }

    public interface OnItemChaneClick {
        void onSuccessListener(boolean isChecked, TagEntity data);
    }
}
