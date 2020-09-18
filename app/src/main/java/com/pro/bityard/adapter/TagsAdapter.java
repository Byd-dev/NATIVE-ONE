package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.pro.bityard.R;
import com.pro.bityard.entity.TagEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TagEntity> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;

    private int type;
    private Map<Integer, Boolean> map = new HashMap<>();
    private int checkedPosition = -1;
    private boolean onBind;

    private String select;

    public TagsAdapter(Context context, int type) {
        this.type = type;
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<TagEntity> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void select(String select){
        this.select=select;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_tags_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHoler(view);
        return holder;


    }

    //得到当前选中的位置
    public int getCheckedPosition() {
        return checkedPosition;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {


            ((MyViewHolder) holder).checkbox_tags.setText(datas.get(position).getContent());


            switch (type) {
                case 1:
                    ((MyViewHolder) holder).checkbox_tags.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_rate_bg));
                    break;
                case 2:
                    ((MyViewHolder) holder).checkbox_tags.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_draw_bg));
                    break;
                case 3:
                    ((MyViewHolder) holder).checkbox_tags.setBackground(context.getResources().getDrawable(R.drawable.sel_switcher_tag_days_bg));
                    break;

            }


           /* if (!datas.get(position).isChecked) {
                ((MyViewHolder) holder).checkbox_tags.setChecked(false);
                map.remove(position);
                if (map.size() == 0) {
                    checkedPosition = -1;
                }
            } else {
                ((MyViewHolder) holder).checkbox_tags.setChecked(true);
                map.clear();
                map.put(position, true);
            }*/

            ((MyViewHolder) holder).checkbox_tags.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onItemChaneClick != null) {
                    onItemChaneClick.onSuccessListener(isChecked, datas.get(position));
                }


                if (isChecked) {
                    map.clear();
                    map.put(position, true);

                    checkedPosition = position;
                } else {
                    map.remove(position);
                    if (map.size() == 0) {
                        checkedPosition = -1;
                    }
                }
                if (!onBind) {
                    notifyDataSetChanged();
                }
            });
            onBind = true;
            if (map != null && map.containsKey(position)) {
                ((MyViewHolder) holder).checkbox_tags.setChecked(true);
            } else {
                ((MyViewHolder) holder).checkbox_tags.setChecked(false);
            }
            onBind = false;

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
        CheckBox checkbox_tags;


        public MyViewHolder(View itemView) {
            super(itemView);
            checkbox_tags = itemView.findViewById(R.id.checkbox_tags);


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
