package com.pro.bityard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.SpotHistoryEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SpotHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SpotHistoryEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;
    private String value_circle;
    private String div_market;
    private String div_limit;


    public SpotHistoryAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SpotHistoryEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<SpotHistoryEntity.DataBean> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
        this.notifyDataSetChanged();
    }

    public void sortPrice(boolean isHigh) {
        this.isHigh = isHigh;
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


    public void getIncome(TradeResult tradeResult) {
        tradeResult.setResult(incomeList);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;


        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_spot_history_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).text_time.setText(TradeUtil.dateToStamp(datas.get(position).getCreateTime()));
            int type = datas.get(position).getType();
            //0 限价   1市价
            boolean buy = datas.get(position).getBuy();
            Integer status = datas.get(position).getStatus();

            if (status == 6) {
                ((MyViewHolder) holder).text_name.setTextColor(context.getResources().getColor(R.color.text_second_color));
                ((MyViewHolder) holder).text_price.setTextColor(context.getResources().getColor(R.color.text_second_color));

            } else {
                ((MyViewHolder) holder).text_name.setTextColor(context.getResources().getColor(R.color.text_main_color));
                ((MyViewHolder) holder).text_price.setTextColor(context.getResources().getColor(R.color.text_main_color));

            }

            String value_price = null;
            double opVolume_double = datas.get(position).getOpVolume();
            double volume_double = datas.get(position).getVolume();

            String OpVolume = TradeUtil.justDisplay(opVolume_double);
            String volume = TradeUtil.justDisplay(volume_double);


            if (type == 0) {
                ((MyViewHolder) holder).text_amount.setText(OpVolume + "/"
                        + volume);
                if (volume_double!=0){
                    div_limit = TradeUtil.divBig(opVolume_double, volume_double, 0);
                }else {
                    div_market="0";

                }
                Log.d("print", "onBindViewHolder:135: "+div_limit);

                value_circle = TradeUtil.mulBig(Double.parseDouble(div_limit), 100);

            } else {
                ((MyViewHolder) holder).text_amount.setText(OpVolume + "/"
                        + OpVolume);
                if (opVolume_double!=0){
                    div_market = TradeUtil.divBig(opVolume_double, opVolume_double, 0);
                }else {
                    div_market="0";
                }
                Log.d("print", "onBindViewHolder:146: "+div_market);
                value_circle = TradeUtil.mulBig(Double.parseDouble(div_market), 100);

            }
            Log.d("print", "onBindViewHolder: "+value_circle);


            ((MyViewHolder) holder).circleView.setProgress(Integer.parseInt(value_circle));

            if (buy) {
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getDesCurrency() + "/" + datas.get(position).getSrcCurrency());

                if (type == 0) {
                    ((MyViewHolder) holder).text_buy.setText(R.string.text_limit_buy);
                    value_price = TradeUtil.getNumberFormat(datas.get(position).getPrice(), 2);
                } else {
                    ((MyViewHolder) holder).text_buy.setText(R.string.text_market_buy);
                    value_price = context.getResources().getString(R.string.text_market_price);
                }
                if (status == 6) {
                    ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.color_bg_green));
                } else {
                    ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                }

                ((MyViewHolder) holder).circleView.setCricleProgressColor(context.getResources().getColor(R.color.text_quote_green));
                ((MyViewHolder) holder).circleView.setTextColor(context.getResources().getColor(R.color.text_quote_green));


            } else {
                ((MyViewHolder) holder).text_name.setText(datas.get(position).getSrcCurrency() + "/" + datas.get(position).getDesCurrency());

                if (type == 0) {
                    ((MyViewHolder) holder).text_buy.setText(R.string.text_limit_sell);
                    value_price = TradeUtil.getNumberFormat(datas.get(position).getPrice(), 2);

                } else {
                    ((MyViewHolder) holder).text_buy.setText(R.string.text_market_sell);
                    value_price = context.getResources().getString(R.string.text_market_price);


                }
                if (status == 6) {
                    ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.color_bg_red));

                } else {
                    ((MyViewHolder) holder).text_buy.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                }
                ((MyViewHolder) holder).circleView.setCricleProgressColor(context.getResources().getColor(R.color.text_quote_red));
                ((MyViewHolder) holder).circleView.setTextColor(context.getResources().getColor(R.color.text_quote_red));

            }

            ((MyViewHolder) holder).text_price.setText(value_price);


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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar bar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_buy, text_currency, text_time, text_amount, text_price;

        ImageView img_buy;
        CircleView circleView;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_amount = itemView.findViewById(R.id.text_amount);
            text_price = itemView.findViewById(R.id.text_price);
            text_time = itemView.findViewById(R.id.text_time);
            text_buy = itemView.findViewById(R.id.text_buy);
            circleView = itemView.findViewById(R.id.circle);


        }
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(SpotHistoryEntity.DataBean data);


    }
}
