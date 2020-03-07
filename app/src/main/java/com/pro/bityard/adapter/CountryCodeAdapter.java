package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.entity.CountryCodeEntity;

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
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getNameCn());

            // TODO: 2020/3/7   英文切换
            ((MyViewHolder) holder).text_code.setText("+"+datas.get(position).getCountryCode());

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
            text_name =  itemView.findViewById(R.id.text_name);
            text_code = (TextView) itemView.findViewById(R.id.text_code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick!=null){
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

    public  interface OnItemClick{
        void onSuccessListener(CountryCodeEntity.DataBean dataBean);

    }
}
