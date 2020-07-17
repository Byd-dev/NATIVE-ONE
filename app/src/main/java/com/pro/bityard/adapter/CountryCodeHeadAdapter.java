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

public class CountryCodeHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CountryCodeEntity.DataBean> datas;

    public CountryCodeHeadAdapter(Context context) {
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

            String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, AppConfig.ZH_SIMPLE);
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



            itemView.setOnClickListener(v -> {
                if (onItemHeadClick != null) {
                    onItemHeadClick.onSuccessListener(datas.get(getPosition()));
                }
            });
        }
    }



    private OnItemHeadClick onItemHeadClick;

    public void setOnItemHeadClick(OnItemHeadClick onItemHeadClick) {
        this.onItemHeadClick = onItemHeadClick;
    }

    public interface OnItemHeadClick {
        void onSuccessListener(CountryCodeEntity.DataBean dataBean);
    }

}
