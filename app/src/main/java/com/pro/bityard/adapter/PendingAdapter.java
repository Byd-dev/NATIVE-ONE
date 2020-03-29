package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.PendingEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.StopLossPrice;
import static com.pro.bityard.utils.TradeUtil.StopProfitPrice;
import static com.pro.bityard.utils.TradeUtil.getNumberFormat;
import static com.pro.bityard.utils.TradeUtil.price;

public class PendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PositionEntity.DataBean> datas;

    private List<String> quoteList;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public PendingAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<PositionEntity.DataBean> datas, List<String> quoteList) {
        this.datas = datas;
        this.quoteList = quoteList;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<PositionEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_pending_layout, parent, false);
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
            String[] split = Util.quoteList(datas.get(position).getContractCode()).split(",");
            ((MyViewHolder) holder).text_name.setText(split[0]);
            ((MyViewHolder) holder).text_volume.setText("×"+String.valueOf(datas.get(position).getVolume()));

            ((MyViewHolder) holder).text_time.setText(TradeUtil.dateToStamp(datas.get(position).getTime()));

            double price = datas.get(position).getPrice();

            boolean isBuy = datas.get(position).isIsBuy();

            double lever = datas.get(position).getLever();

            double margin = datas.get(position).getMargin();

            double stopLoss = datas.get(position).getStopLoss();

            double stopProfit = datas.get(position).getStopProfit();

            int priceDigit = datas.get(position).getPriceDigit();


            if (isBuy) {
                ((MyViewHolder) holder).img_buy.setBackgroundResource(R.mipmap.icon_up);
            } else {
                ((MyViewHolder) holder).img_buy.setBackgroundResource(R.mipmap.icon_down);
            }

            //挂单价
            ((MyViewHolder) holder).text_buy_price.setText(getNumberFormat(price, priceDigit));
            //止损价格
            ((MyViewHolder) holder).text_loss_price.setText(StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
            //止盈价格
            ((MyViewHolder) holder).text_profit_price.setText(StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
            //现价和盈亏
            price(quoteList, datas.get(position).getContractCode(), new TradeResult() {
                @Override
                public void setResult(Object response) {
                    ((MyViewHolder) holder).text_price.setText(response.toString());
                }
            });


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
        TextView text_name, text_volume, text_buy_price,
                text_loss_price, text_price, text_profit_price,
                text_cancel, text_time;
        ImageView img_buy;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_volume = itemView.findViewById(R.id.text_volume);
            text_buy_price = itemView.findViewById(R.id.text_buy_price);
            text_loss_price = itemView.findViewById(R.id.text_loss_price);
            text_price = itemView.findViewById(R.id.text_price);
            text_profit_price = itemView.findViewById(R.id.text_profit_price);
            text_cancel = itemView.findViewById(R.id.text_cancel);
            img_buy = itemView.findViewById(R.id.img_buy);
            text_time = itemView.findViewById(R.id.text_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onClickListener(datas.get(getPosition()));
                    }
                }
            });

            text_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onCancelListener(datas.get(getPosition()).getId());
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
        void onClickListener(PositionEntity.DataBean data);

        void onCancelListener(String id);

        void onProfitLossListener();

    }
}
