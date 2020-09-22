package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.TagEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class SelectResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TagEntity> selectList;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public boolean isLoadMore = false;

    private int type;
    private Map<Integer, Boolean> map = new HashMap<>();
    private int checkedPosition = -1;
    private boolean onBind;

    public SelectResultAdapter(Context context) {
        this.context = context;
        selectList = new ArrayList<>();
    }

    public void setDatas(List<TagEntity> selectList) {
        this.selectList = selectList;
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_select_result_layout, parent, false);
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

            String type = selectList.get(position).getType();
            switch (type) {
                case AppConfig.type_style:
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getResources().getDrawable(R.drawable.gradient_bg_green));
                    break;
                case AppConfig.type_rate:
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getResources().getDrawable(R.drawable.gradient_bg_blue));
                    break;
                case AppConfig.type_draw:
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getResources().getDrawable(R.drawable.gradient_btn_main));
                    break;
                case AppConfig.type_day:
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getResources().getDrawable(R.drawable.gradient_bg_pink));
                    break;
            }
            ((MyViewHolder) holder).text_content.setText(selectList.get(position).getContent());


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
            return selectList.size() + 1;
        }
        return selectList.size();
    }

    public static class ProgressViewHoler extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHoler(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_content;
        RelativeLayout layout_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_content = itemView.findViewById(R.id.text_content);
            layout_bg = itemView.findViewById(R.id.layout_bg);

            layout_bg.setOnClickListener(v -> {
                if (onItemDeleteClick != null) {
                    onItemDeleteClick.onSuccessListener(getPosition(), selectList.get(getPosition()));
                }
            });

        }
    }

    private OnItemDeleteClick onItemDeleteClick;


    public void setOnItemDeleteClick(OnItemDeleteClick onItemDeleteClick) {
        this.onItemDeleteClick = onItemDeleteClick;
    }

    public interface OnItemDeleteClick {
        void onSuccessListener(int position, TagEntity data);
    }
}
