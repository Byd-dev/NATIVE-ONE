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
import com.pro.bityard.entity.OpenPositionEntity;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.StopLossPrice;
import static com.pro.bityard.utils.TradeUtil.StopProfitPrice;
import static com.pro.bityard.utils.TradeUtil.income;
import static com.pro.bityard.utils.TradeUtil.netIncome;
import static com.pro.bityard.utils.TradeUtil.price;

public class PositionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<OpenPositionEntity.DataBean> datas;

    private List<String> quoteList;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public PositionAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<OpenPositionEntity.DataBean> datas, List<String> quoteList) {
        this.datas = datas;
        this.quoteList = quoteList;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<OpenPositionEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_position_layout, parent, false);
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

            double opPrice = datas.get(position).getOpPrice();

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

            //开仓价
            ((MyViewHolder) holder).text_buy_price.setText(String.valueOf(opPrice));
            //止损价格
            ((MyViewHolder) holder).text_loss_price.setText(StopLossPrice(isBuy, opPrice, priceDigit, lever, margin, Math.abs(stopLoss)));
            //止盈价格
            ((MyViewHolder) holder).text_profit_price.setText(StopProfitPrice(isBuy, opPrice, priceDigit, lever, margin, stopProfit));
            //现价和盈亏
            price(quoteList, datas.get(position).getContractCode(), new TradeResult() {
                @Override
                public void setResult(Object response) {
                    ((MyViewHolder) holder).text_price.setText(response.toString());
                    String income = income(isBuy, Double.parseDouble(response.toString()), opPrice, datas.get(position).getVolume());
                    ((MyViewHolder) holder).text_income.setText(income);
                    double incomeDouble = Double.parseDouble(income);


                    String netIncome = netIncome(incomeDouble, datas.get(position).getServiceCharge());
                    double netIncomeDouble = Double.parseDouble(netIncome);

                    ((MyViewHolder) holder).text_worth.setText(netIncome);
                    if (incomeDouble > 0) {
                        ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                    } else {
                        ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                    }

                    if (netIncomeDouble > 0) {
                        ((MyViewHolder) holder).text_worth.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                    } else {
                        ((MyViewHolder) holder).text_worth.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                    }

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
                text_income, text_worth, text_close_out,
                text_profit_loss;
        ImageView img_buy;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_volume = itemView.findViewById(R.id.text_volume);
            text_buy_price = itemView.findViewById(R.id.text_buy_price);
            text_loss_price = itemView.findViewById(R.id.text_loss_price);
            text_price = itemView.findViewById(R.id.text_price);
            text_profit_price = itemView.findViewById(R.id.text_profit_price);
            text_income = itemView.findViewById(R.id.text_income);
            text_worth = itemView.findViewById(R.id.text_worth);
            text_close_out = itemView.findViewById(R.id.text_close_out);
            img_buy = itemView.findViewById(R.id.img_buy);
            text_profit_loss=itemView.findViewById(R.id.text_profit_loss);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onClickListener(datas.get(getPosition() - 1));
                    }
                }
            });

            text_close_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onCloseListener(datas.get(getPosition() - 1).getId());
                    }
                }
            });

            text_profit_loss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onProfitLossListener(datas.get(getPosition() - 1));
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
        void onClickListener(OpenPositionEntity.DataBean data);

        void onCloseListener(String id);

        void onProfitLossListener(OpenPositionEntity.DataBean data);

    }
}
