package com.pro.bityard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.entity.AnnouncementEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<AnnouncementEntity.NoticesBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;




    public AnnouncementAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<AnnouncementEntity.NoticesBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<AnnouncementEntity.NoticesBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_announcement_layout, parent, false);
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


            ((MyViewHolder) holder).text_title.setText(datas.get(position).getTitle());

            ((MyViewHolder) holder).text_time.setText(TradeUtil.dateToStamp(datas.get(position).getTime()));


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

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
        TextView text_title, text_time;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_title);
            text_time = itemView.findViewById(R.id.text_time);


            itemView.findViewById(R.id.text_detail).setOnClickListener(v -> {
                if (detailClick != null) {
                    detailClick.onClickListener(datas.get(getAdapterPosition()));
                }
            });


        }
    }

    private OnDetailClick detailClick;


    public void setDetailClick(OnDetailClick detailClick) {
        this.detailClick = detailClick;
    }

    public interface OnDetailClick {
        void onClickListener(AnnouncementEntity.NoticesBean data);
    }


}
