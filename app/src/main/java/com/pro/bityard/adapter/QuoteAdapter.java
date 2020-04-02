package com.pro.bityard.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.itemQuotePrice;
import static com.pro.bityard.utils.TradeUtil.itemQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;

    public QuoteAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_quote_layout, parent, false);
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


            String name = TradeUtil.listQuoteName(datas.get(position));

            Drawable drawable;

            if (name.equals("BTC")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_btc);
            } else if (name.equals("ETC")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_etc);
            } else if (name.equals("BCH")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_bch);
            } else if (name.equals("ETH")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_eth);
            } else if (name.equals("DASH")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_dash);
            } else if (name.equals("LTC")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_ltc);
            } else if (name.equals("ETC")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_etc);
            } else if (name.equals("BNB")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_bnb);
            } else if (name.equals("EOS")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_eos);
            } else if (name.equals("TRX")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_trx);
            } else if (name.equals("XRP")) {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_xrp);
            } else {
                drawable = context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_usdt);
            }


            ((MyViewHolder) holder).img_icon.setImageDrawable(drawable);


            ((MyViewHolder) holder).text_name.setText(name);
            ((MyViewHolder) holder).text_name_usdt.setText("/" + TradeUtil.listQuoteUSD(datas.get(position)));

            String price = TradeUtil.listQuotePrice(datas.get(position));

            ((MyViewHolder) holder).text_price.setText(price);

            ((MyViewHolder) holder).text_change.setText(TradeUtil.quoteRange(price, listQuoteTodayPrice(datas.get(position))));


            String tag = TradeUtil.listQuoteIsRange(datas.get(position));
            if (Integer.parseInt(tag) == 1) {

                ((MyViewHolder) holder).text_change.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_quote_green));
                ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_green));
                ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_up));
            } else if (Integer.parseInt(tag) == -1) {

                ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_red));
                ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_down));

            } else if (Integer.parseInt(tag) == 0) {

                ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_maincolor));
                ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_normal));

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
            bar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_name_usdt, text_change, text_price;
        RelativeLayout layout_bg;
        ImageView img_up_down, img_icon;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_name_usdt = itemView.findViewById(R.id.text_name_usdt);

            text_change = itemView.findViewById(R.id.text_change);
            text_price = itemView.findViewById(R.id.text_lastPrice);
            layout_bg = itemView.findViewById(R.id.layout_bg);
            img_up_down = itemView.findViewById(R.id.img_up_down);
            img_icon = itemView.findViewById(R.id.img_icon);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
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

    public interface OnItemClick {
        void onSuccessListener(String data);

    }
}
