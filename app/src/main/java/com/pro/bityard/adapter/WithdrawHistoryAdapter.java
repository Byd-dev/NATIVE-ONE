package com.pro.bityard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.RecyclerView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class WithdrawHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DepositWithdrawEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean isHigh = false;

    public boolean isLoadMore = false;


    private List<Double> incomeList;


    public WithdrawHistoryAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
        startTime();
    }

    public void setDatas(List<DepositWithdrawEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<DepositWithdrawEntity.DataBean> datas) {
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_history_withdraw_layout, parent, false);
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


            int status = datas.get(position).getStatus();
            switch (status) {
                case 0:
                case -1:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_pending);
                    break;
                case 1:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_tip_success);
                    break;
                case 2:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_failure);
                    break;
                case 3:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_tip_cancel);
                    break;
                case 4:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_processing);
                    break;
                case 5:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_in_progress);
                    break;
                case 6:
                    ((MyViewHolder) holder).text_status.setText(R.string.text_refunded);
                    break;
            }


            long createTime = datas.get(position).getCreateTime();
            long time = System.currentTimeMillis();
            long l = time - createTime;
            Log.d("print", "onBindViewHolder:136:  " + createTime + "    " + time + "    差值:   " + l);

            if (l < 10 * 60 * 1000) {
                ((MyViewHolder) holder).text_status.setText(R.string.text_cancel_position);
                ((MyViewHolder) holder).layout_bg.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_main_color));
                ((MyViewHolder) holder).text_status.setTextColor(context.getResources().getColor(R.color.text_main_color_black));
                ((MyViewHolder) holder).text_time_min.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_red));
                ((MyViewHolder) holder).text_time_second.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_red));
                setTimeShow(datas.get(position).getUseTime(), ((MyViewHolder) holder));


            } else {
                ((MyViewHolder) holder).text_time_min.setText("00");
                ((MyViewHolder) holder).text_time_second.setText("00");
                ((MyViewHolder) holder).layout_bg.setBackgroundColor(context.getResources().getColor(R.color.color_bg_left));
                ((MyViewHolder) holder).text_status.setTextColor(context.getResources().getColor(R.color.text_main_color));
                ((MyViewHolder) holder).text_time_min.setBackgroundColor(context.getResources().getColor(R.color.color_bg_left));
                ((MyViewHolder) holder).text_time_second.setBackgroundColor(context.getResources().getColor(R.color.color_bg_left));
            }


            ((MyViewHolder) holder).text_address.setText(datas.get(position).getBankCard());
            ((MyViewHolder) holder).text_amount.setText(datas.get(position).getMoney() + " " + context.getResources().getString(R.string.text_usdt));
            ((MyViewHolder) holder).text_time.setText(ChartUtil.getDate(createTime));


        }
    }


    /**
     * 列表倒计时
     */
    private void startTime() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    for (int i = 0; i < datas.size(); i++) {
                        long useTime = datas.get(i).getUseTime();
                        if (useTime > 1000) {
                            useTime -= 1000;
                            datas.get(i).setUseTime(useTime);
                            long createTime = datas.get(i).getCreateTime();
                            long time = System.currentTimeMillis();
                            long l = time - createTime;
                            if (l < 10 * 60 * 1000) {
                                datas.get(i).setTimeFlag(true);
                            } else {
                                datas.get(i).setTimeFlag(false);
                            }
                            if (useTime <= 1000 && !datas.get(i).isTimeFlag()) {
                                datas.get(i).setTimeFlag(false);
                                WithdrawHistoryAdapter.this.notifyItemChanged(i);
                            } else {
                                datas.get(i).setTimeFlag(true);
                                WithdrawHistoryAdapter.this.notifyItemChanged(i);
                            }

                        }

                    }

                });
            }
        }, 0, 1000);
    }


    private void setTimeShow(long useTime, MyViewHolder holder) {
        int hour = (int) (useTime / 3600 );
        int min = (int) (useTime/1000 / 60 % 60);
        int second = (int) (useTime/1000 % 60);
        int day = (int) (useTime / 3600 / 24);
        String mDay, mHour, mMin, mSecond;//天，小时，分钟，秒
        second--;
        if (second < 0) {
            min--;
            second = 59;
            if (min < 0) {
                min = 59;
                hour--;
            }
        }
        if (hour < 10) {
            mHour = "0" + hour;
        } else {
            mHour = "" + hour;
        }
        if (min < 10) {
            mMin = "0" + min;
        } else {
            mMin = "" + min;
        }
        if (second < 10) {
            mSecond = "0" + second;
        } else {
            mSecond = "" + second;
        }
        String strTime = mMin + ":" + mSecond + "";
        Log.d("print", "setTimeShow:233:  "+strTime);
        holder.text_time_min.setText(mMin);
        holder.text_time_second.setText(mSecond);

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
        TextView text_address, text_time, text_time_min, text_time_second, text_amount,
                text_status;
        RelativeLayout layout_bg;


        public MyViewHolder(View itemView) {
            super(itemView);
            text_address = itemView.findViewById(R.id.text_address);
            text_amount = itemView.findViewById(R.id.text_amount);
            text_time = itemView.findViewById(R.id.text_time);
            text_status = itemView.findViewById(R.id.text_status);
            text_time_min = itemView.findViewById(R.id.text_time_min);
            text_time_second = itemView.findViewById(R.id.text_time_second);

            layout_bg = itemView.findViewById(R.id.layout_bg);
            itemView.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onClickListener(datas.get(getPosition()));
                }
            });


        }
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onClickListener(DepositWithdrawEntity.DataBean data);


    }
}
