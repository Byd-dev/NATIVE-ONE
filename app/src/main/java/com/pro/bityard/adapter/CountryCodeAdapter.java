package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CountryCodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CountryCodeEntity.DataBean> datas;

    public CountryCodeAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CountryCodeEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_layout, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, Util.ZH_SIMPLE);
            if (language.contains("zh")) {
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getNameCn() + "(" + datas.get(position).getNameEn() + ")");
            } else {
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getNameEn());
            }


            // TODO: 2020/3/7   英文切换
            ((MyViewHolder) holder).text_code.setText("+" + datas.get(position).getCountryCode());

        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_code;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_code = itemView.findViewById(R.id.text_code);

            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onSuccessListener(datas.get(getPosition() - 1));
                }
            });


        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onSuccessListener(CountryCodeEntity.DataBean dataBean);
    }



}
