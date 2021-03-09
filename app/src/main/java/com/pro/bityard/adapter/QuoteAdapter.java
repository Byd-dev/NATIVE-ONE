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

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    public QuoteAdapter(Context context) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_quote_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHoler(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Iterator<String> iterator = changeData.keySet().iterator();
            String contractCode = datas.get(position).split(",")[0];
            while (iterator.hasNext()) {
                String next = iterator.next();
                Log.d("print", "onBindViewHolder: " + TradeUtil.removeDigital(contractCode) + "      " + next);
                if (TradeUtil.removeDigital(contractCode).equals(next)) {
                    String price = changeData.get(next);
                    ((MyViewHolder) holder).text_price.setText(price);
                    ((MyViewHolder) holder).text_change.setText(TradeUtil.quoteRange(price, listQuoteTodayPrice(datas.get(position))));
                    ((MyViewHolder) holder).text_volume.setText(context.getResources().getText(R.string.kline_volume_detail) + ":" + TradeUtil.justDisplay(Double.parseDouble(TradeUtil.listQuoteBuyVolume(datas.get(position)))));
                    String tag = TradeUtil.listQuoteIsRange(datas.get(position));
                    if (Integer.parseInt(tag) == 1) {

                        // ((MyViewHolder) holder).text_change.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_quote_green));
                        ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_green));
                        //  ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_up));
                    } else if (Integer.parseInt(tag) == -1) {

                        //  ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                        ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_red));
                        //  ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_down));

                    } else if (Integer.parseInt(tag) == 0) {

                        ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_main_color));
                        ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_normal));

                    }
                    String string = SPUtils.getString(AppConfig.USD_RATE, null);
                    ((MyViewHolder) holder).text_price_currency.setText(TradeUtil.numberHalfUp(TradeUtil.mul(Double.parseDouble(price), Double.parseDouble(string)), 2));
                }
            }
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            String name, currency;
            name = TradeUtil.name(datas.get(position));
            currency = TradeUtil.currency(datas.get(position));
            //根据后台直接显示
           /* if (currency == null) {
                ((MyViewHolder) holder).text_name_usdt.setText("");
            } else {
                ((MyViewHolder) holder).text_name_usdt.setText("/" + currency);
            }*/

            if (isShow) {
                ChartUtil.setIcon(name, ((MyViewHolder) holder).img_icon);
            } else {
                ((MyViewHolder) holder).img_icon.setVisibility(View.GONE);
            }

            if (isShowVolume) {
                ((MyViewHolder) holder).text_volume.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).text_price_currency.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).text_volume.setVisibility(View.GONE);
                ((MyViewHolder) holder).text_price_currency.setVisibility(View.GONE);
            }
            String tag = TradeUtil.listQuoteIsRange(datas.get(position));

            if (isStar) {
                ((MyViewHolder) holder).layout_bg.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.background_main_color));
                ((MyViewHolder) holder).img_star.setVisibility(View.VISIBLE);
                if (Integer.parseInt(tag) == 1) {

                    ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                } else if (Integer.parseInt(tag) == -1) {

                    ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_quote_red));

                } else if (Integer.parseInt(tag) == 0) {

                    ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_main_color));

                }

            } else {
                ((MyViewHolder) holder).img_star.setVisibility(View.GONE);
                if (Integer.parseInt(tag) == 1) {

                    // ((MyViewHolder) holder).text_change.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_green));
                    //  ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_up));
                } else if (Integer.parseInt(tag) == -1) {

                    //  ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_red));
                    //  ((MyViewHolder) holder).img_up_down.setImageDrawable(context.getApplicationContext().getResources().getDrawable(R.mipmap.icon_down));

                } else if (Integer.parseInt(tag) == 0) {

                    ((MyViewHolder) holder).text_change.setTextColor(context.getResources().getColor(R.color.text_main_color));
                    ((MyViewHolder) holder).layout_bg.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.bg_shape_normal));

                }
            }
            ((MyViewHolder) holder).text_name.setText(name);


            String price = TradeUtil.listQuotePrice(datas.get(position));

            ((MyViewHolder) holder).text_price.setText(price);

            ((MyViewHolder) holder).text_change.setText(TradeUtil.quoteRange(price, listQuoteTodayPrice(datas.get(position))));





            ((MyViewHolder) holder).text_volume.setText(context.getResources().getText(R.string.kline_volume_detail) + ":" + TradeUtil.listQuoteBuyVolume(datas.get(position)));

            String string = SPUtils.getString(AppConfig.USD_RATE, null);
            if (string != null) {
                ((MyViewHolder) holder).text_price_currency.setText(TradeUtil.numberHalfUp(TradeUtil.mul(Double.parseDouble(price), Double.parseDouble(string)), 2));
            }


            if (optionalList.size() != 0) {
                //判断当前是否存在自选
                Util.isOptional(itemQuoteContCode(datas.get(position)), optionalList, response -> {
                    boolean isOptional = (boolean) response;
                    if (isOptional) {
                        ((MyViewHolder) holder).img_star.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star));
                    } else {
                        ((MyViewHolder) holder).img_star.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star_normal));

                    }
                });
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
        TextView text_name, text_name_usdt, text_change, text_price, text_price_currency, text_volume;
        RelativeLayout layout_bg;
        ImageView img_up_down, img_icon, img_star;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_name_usdt = itemView.findViewById(R.id.text_name_usdt);

            text_change = itemView.findViewById(R.id.text_change);
            text_price = itemView.findViewById(R.id.text_lastPrice);
            layout_bg = itemView.findViewById(R.id.layout_bg);
            img_up_down = itemView.findViewById(R.id.img_up_down);
            img_star = itemView.findViewById(R.id.img_star);

            img_icon = itemView.findViewById(R.id.img_icon);
            text_price_currency = itemView.findViewById(R.id.text_price_currency);
            text_volume = itemView.findViewById(R.id.text_volume);


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
