package com.pro.bityard.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;

public class SpotSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isShow = false;
    private boolean isShowVolume = false;
    private boolean isStar = false;
    private Set<String>optionalList;

    private ArrayMap<String, String> changeData;

    public boolean isLoadMore = false;
    private String contCode = null;

    public SpotSearchAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
        optionalList=new HashSet<>();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<String> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
        this.notifyDataSetChanged();
    }

    public void isShowIcon(boolean isShow) {
        this.isShow = isShow;
        this.notifyDataSetChanged();
    }

    public void isShowVolume(boolean isShowVolume) {
        this.isShowVolume = isShowVolume;
        this.notifyDataSetChanged();
    }

    public void isShowStar(Set<String> optionalList,boolean isStar) {
        this.optionalList=optionalList;
        this.isStar = isStar;
        this.notifyDataSetChanged();
    }

    public void refreshPartItem(int position, ArrayMap<String, String> map) {
        // 局部刷新的主要api，参数一：更新的item位置，参数二：item中被标记的某个数据
        this.changeData = map;
        notifyItemChanged(position, map);  // changePos 为0：数据1   为1：数据2

    }

    public void sortPrice(boolean isHigh) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_spot_search_layout, parent, false);
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

            String name, currency;
            name = TradeUtil.name(datas.get(position));
            currency = TradeUtil.currency(datas.get(position));

            if (currency == null) {
                ((MyViewHolder) holder).text_name_usdt.setText("");
            } else {
                ((MyViewHolder) holder).text_name_usdt.setText("/" + currency);
            }
            ((MyViewHolder) holder).text_name.setText(name);



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
            bar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_name_usdt;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_name_usdt = itemView.findViewById(R.id.text_name_usdt);




            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onSuccessListener(datas.get(getPosition()));
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
