package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.pro.bityard.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;
    private String tags="";
    private int type;
    private boolean isEnable = true;

    public TagsAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void select(int type,String tags) {
        this.type=type;
        this.tags = tags;
        this.notifyDataSetChanged();

    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_tags_layout, parent, false);
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

            ((MyViewHolder) holder).radioButton_tag.setText(datas.get(position));
            if (isEnable){
                ((MyViewHolder) holder).radioButton_tag.setEnabled(true);
            }else {
                ((MyViewHolder) holder).radioButton_tag.setEnabled(false);
            }

            ((MyViewHolder) holder).radioButton_tag.setText(datas.get(position));
            if (tags.equals(datas.get(position))) {
                ((MyViewHolder) holder).radioButton_tag.setChecked(true);
            } else {
                ((MyViewHolder) holder).radioButton_tag.setChecked(false);
            }
            switch (type){
                case 1:
                    ((MyViewHolder) holder).radioButton_tag.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_rate_bg));
                    break;
                case 2:
                    ((MyViewHolder) holder).radioButton_tag.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_draw_bg));

                    break;

                case 3:
                    ((MyViewHolder) holder).radioButton_tag.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_days_bg));

                    break;
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
        RadioButton radioButton_tag;


        public MyViewHolder(View itemView) {
            super(itemView);
            radioButton_tag = itemView.findViewById(R.id.radio_tags);


            radioButton_tag.setOnClickListener(v -> {
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
