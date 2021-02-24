package com.pro.bityard.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.pro.bityard.utils.TradeUtil.StopLossPrice;
import static com.pro.bityard.utils.TradeUtil.StopProfitPrice;
import static com.pro.bityard.utils.TradeUtil.getNumberFormat;
import static com.pro.bityard.utils.TradeUtil.income;
import static com.pro.bityard.utils.TradeUtil.netIncome;

public class PositionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PositionEntity.DataBean> datas;

    private List<String> quoteList;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    private ArrayMap<String, String> changeData;
    private int changePosition;

    private static final String DATA_ONE = "dataOne";
    private static final String DATA_TWO = "dataTwo";


    public PositionAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
        quoteList = new ArrayList<>();
    }

    //设置数据
    public void setDatas(List<PositionEntity.DataBean> datas, List<String> quoteList) {
        this.datas = datas;
        this.quoteList = quoteList;
        this.notifyDataSetChanged();
    }


    public void refreshPartItem(int position, ArrayMap<String, String> map) {
        // 局部刷新的主要api，参数一：更新的item位置，参数二：item中被标记的某个数据
        this.changeData = map;
        notifyItemChanged(position, map);  // changePos 为0：数据1   为1：数据2

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

            View view = LayoutInflater.from(context).inflate(R.layout.item_position_layout, parent, false);
            holder = new MyViewHolder(view);
            return holder;
        }


        View view = LayoutInflater.from(context).inflate(R.layout.item_foot_layout, parent, false);
        holder = new ProgressViewHoler(view);
        return holder;


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Log.d("PayloadAdapter", "onBindViewHolder payload PositionAdapter" + payloads);
        if (holder instanceof MyViewHolder) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);

            } else {
                Log.d("print", "onBindViewHolder:146:  " + changeData);

                double opPrice = datas.get(position).getOpPrice();
                double margin = datas.get(position).getMargin();
                boolean isBuy = datas.get(position).isIsBuy();
                double volume = datas.get(position).getVolume();
                String contractCode = datas.get(position).getContractCode();
                Iterator<String> iterator = changeData.keySet().iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (contractCode.equals(next)) {
                        String price = changeData.get(next);
                        Log.d("print", "onBindViewHolder:157:  " + price);
                        ((MyViewHolder) holder).text_price.setText(price);
                        String income = income(isBuy, Double.parseDouble(price), opPrice, volume, 4);
                        ((MyViewHolder) holder).text_income.setText(getNumberFormat(Double.parseDouble(income), 2));
                        double incomeDouble = Double.parseDouble(income);
                        //盈亏比
                        ((MyViewHolder) holder).text_rate.setText(TradeUtil.ratio(incomeDouble, margin));
                    }
                }
            }
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("PayloadAdapter", "onBindViewHolder position PositionAdapter " + position);
        if (holder instanceof MyViewHolder) {


            String[] split = Util.quoteList(datas.get(position).getContractCode()).split(",");
            ((MyViewHolder) holder).text_name.setText(split[0]);
            ((MyViewHolder) holder).text_volume.setText("×" + datas.get(position).getVolume());
            ((MyViewHolder) holder).text_currency.setText("/" + datas.get(position).getCurrency());

            double opPrice = datas.get(position).getOpPrice();

            boolean isBuy = datas.get(position).isIsBuy();

            double lever = datas.get(position).getLever();

            double margin = datas.get(position).getMargin();

            double stopLoss = datas.get(position).getStopLoss();

            double stopProfit = datas.get(position).getStopProfit();

            int priceDigit = datas.get(position).getPriceDigit();

            String traderUsername = datas.get(position).getTraderUsername();

            if (traderUsername == null) {
                ((MyViewHolder) holder).layout_traderUsername.setVisibility(View.GONE);
            } else {
                ((MyViewHolder) holder).layout_traderUsername.setVisibility(View.VISIBLE);
            }


            ((MyViewHolder) holder).text_traderUsername.setText(datas.get(position).getTraderUsername());

            boolean addMargin = TradeUtil.isAddMargin(datas.get(position).getContractCode(), lever, margin);
            if (addMargin) {
                ((MyViewHolder) holder).img_add.setBackgroundResource(R.mipmap.icon_add);
                ((MyViewHolder) holder).layout_add.setEnabled(true);
                ((MyViewHolder) holder).text_add.setTextColor(context.getResources().getColor(R.color.maincolor));
            } else {
                ((MyViewHolder) holder).img_add.setBackgroundResource(R.mipmap.icon_add_normal);
                ((MyViewHolder) holder).layout_add.setEnabled(false);
                ((MyViewHolder) holder).text_add.setTextColor(context.getResources().getColor(R.color.text_second_color));

            }


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
            TradeUtil.positionPrice(isBuy, quoteList, datas.get(position).getContractCode(), response -> {
                ((MyViewHolder) holder).text_price.setText(response.toString());
                String income = income(isBuy, Double.parseDouble(response.toString()), opPrice, datas.get(position).getVolume(), 4);
                ((MyViewHolder) holder).text_income.setText(getNumberFormat(Double.parseDouble(income), 2));
                double incomeDouble = Double.parseDouble(income);
                //盈亏比
                ((MyViewHolder) holder).text_rate.setText(TradeUtil.ratio(incomeDouble, margin));

                String netIncome = netIncome(incomeDouble, datas.get(position).getServiceCharge());
                double netIncomeDouble = Double.parseDouble(netIncome);

                ((MyViewHolder) holder).text_worth.setText(TradeUtil.numberHalfUp(incomeDouble, 2));
                if (incomeDouble > 0) {
                    ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                    ((MyViewHolder) holder).text_rate.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                } else {
                    ((MyViewHolder) holder).text_income.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                    ((MyViewHolder) holder).text_rate.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                }

                if (incomeDouble > 0) {
                    ((MyViewHolder) holder).text_worth.setTextColor(context.getResources().getColor(R.color.text_quote_green));
                } else {
                    ((MyViewHolder) holder).text_worth.setTextColor(context.getResources().getColor(R.color.text_quote_red));
                }

            });


            ((MyViewHolder) holder).img_market.setOnClickListener(v -> {
                if (onJumpQuoteClick != null) {
                    onJumpQuoteClick.onClickListener(datas.get(position));
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
        TextView text_name, text_currency, text_volume, text_buy_price,
                text_loss_price, text_price, text_profit_price, text_rate,
                text_income, text_worth, text_close_out,
                text_profit_loss, text_add, text_traderUsername;
        ImageView img_buy, img_add, img_market;
        LinearLayout layout_add, layout_traderUsername;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_currency = itemView.findViewById(R.id.text_currency);
            text_volume = itemView.findViewById(R.id.text_volume);
            text_buy_price = itemView.findViewById(R.id.text_buy_price);
            text_loss_price = itemView.findViewById(R.id.text_loss_price);
            text_price = itemView.findViewById(R.id.text_price);
            text_profit_price = itemView.findViewById(R.id.text_profit_price);
            text_income = itemView.findViewById(R.id.text_income);
            text_worth = itemView.findViewById(R.id.text_worth);
            text_close_out = itemView.findViewById(R.id.text_close_out);
            text_profit_loss = itemView.findViewById(R.id.text_profit_loss);
            img_buy = itemView.findViewById(R.id.img_buy);
            text_add = itemView.findViewById(R.id.text_add);
            img_add = itemView.findViewById(R.id.img_add);
            layout_add = itemView.findViewById(R.id.layout_add);
            text_rate = itemView.findViewById(R.id.text_rate);
            text_traderUsername = itemView.findViewById(R.id.text_traderUsername);
            layout_traderUsername = itemView.findViewById(R.id.layout_traderUsername);
            img_market = itemView.findViewById(R.id.img_market);


            itemView.findViewById(R.id.text_detail).setOnClickListener(v -> {
                if (onDetailClick != null) {
                    onDetailClick.onClickListener(datas.get(getPosition()));
                }
            });


            text_close_out.setOnClickListener(v -> {
                if (closeClick != null) {
                    closeClick.onCloseListener(datas.get(getPosition()).getId());
                }
            });

            text_profit_loss.setOnClickListener(v -> {
                if (profitLossClick != null) {
                    profitLossClick.onProfitLossListener(datas.get(getPosition()));
                }
            });

            layout_add.setOnClickListener(v -> {
                if (addMarginClick != null) {
                    addMarginClick.onAddMarginClick(datas.get(getPosition()));
                }
            });


        }
    }
    //跳到行情的监听

    private OnJumpQuoteClick onJumpQuoteClick;

    public void setOnJumpQuoteClick(OnJumpQuoteClick onJumpQuoteClick) {
        this.onJumpQuoteClick = onJumpQuoteClick;
    }

    public interface OnJumpQuoteClick {
        void onClickListener(PositionEntity.DataBean data);
    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(PositionEntity.DataBean data);


    }


    //追加保证金监听
    private AddMarginClick addMarginClick;

    public void setAddMarginClick(AddMarginClick addMarginClick) {

        this.addMarginClick = addMarginClick;
    }

    public interface AddMarginClick {
        void onAddMarginClick(PositionEntity.DataBean data);
    }


    //止盈止损监听
    private ProfitLossClick profitLossClick;

    public void setProfitLossClick(ProfitLossClick profitLossClick) {
        this.profitLossClick = profitLossClick;
    }

    public interface ProfitLossClick {
        void onProfitLossListener(PositionEntity.DataBean data);
    }


    //平仓的监听
    private CloseClick closeClick;

    public void setCloseClick(CloseClick closeClick) {
        this.closeClick = closeClick;
    }

    public interface CloseClick {
        void onCloseListener(String id);
    }
}
