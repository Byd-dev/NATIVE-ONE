package com.pro.bityard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FollowEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;


    public FollowAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<FollowEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<FollowEntity.DataBean> datas) {
        this.datas.addAll(datas);
        isLoadMore = false;
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

            View view = LayoutInflater.from(context).inflate(R.layout.item_follow_layout, parent, false);
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

            Glide.with(context).load(datas.get(position).getAvatar()).error(R.mipmap.icon_my_bityard).into(((MyViewHolder) holder).img_head);
            ((MyViewHolder) holder).text_name.setText(datas.get(position).getUsername());
            /*动态添加tag*/
            List<String> styleTags = datas.get(position).getStyleTags();
            if (styleTags.size() != 0) {
                ((MyViewHolder) holder).layout_tags.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).layout_tags.removeAllViews();
                for (int i = 0; i < styleTags.size(); i++) {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setBackground(context.getResources().getDrawable(R.drawable.bg_shape_circle_green));
                    textView.setTextColor(context.getResources().getColor(R.color.text_main_color));
                    textView.setPadding(10, 0, 10, 0);
                    textView.setTextSize(14);
                    textView.setText(styleTags.get(i));
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    lp.setMargins(0, 0, 10, 0);
                    textView.setLayoutParams(lp);
                    ((MyViewHolder) holder).layout_tags.addView(textView);
                }
                ((MyViewHolder) holder).layout_tags.setPadding(0, 0, 10, 0);
            } else {
                ((MyViewHolder) holder).layout_tags.setVisibility(View.GONE);
            }
            String value_type=null;
            int type = datas.get(position).getType();
            switch (type){
                case 1:
                    value_type=context.getString(R.string.text_normal_user);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_user),null,null,null);

                    break;
                case 2:
                    value_type=context.getString(R.string.text_normal_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_trader),null,null,null);
                    break;
                case 3:
                    value_type=context.getString(R.string.text_pro_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader),null,null,null);

                    break;
            }

            ((MyViewHolder) holder).text_type.setText(value_type);


            double mul = TradeUtil.mul(datas.get(position).getTrader30DaysRate(), 100);
            ((MyViewHolder) holder).text_trader_30_days_rate.setText(TradeUtil.getNumberFormat(mul, 2) + "%");

            double mul1 = TradeUtil.mul(datas.get(position).getTrader30DaysDefeat(), 100);
            ((MyViewHolder) holder).text_trader_30_days_defeat.setText(TradeUtil.getNumberFormat(mul1, 2) + "%");

            double mul2 = TradeUtil.mul(datas.get(position).getTrader30DaysDraw(), 100);
            ((MyViewHolder) holder).text_trader_30_days_draw.setText(TradeUtil.getNumberFormat(mul2, 2) + "%");

            ((MyViewHolder) holder).text_bet_days.setText(datas.get(position).getBetDays());

            ((MyViewHolder) holder).text_trader_30_days_count.setText(datas.get(position).getTrader30DaysCount());

            ((MyViewHolder) holder).text_follower.setText(datas.get(position).getFollower());


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
        TextView text_name, text_trader_30_days_rate, text_trader_30_days_defeat, text_trader_30_days_draw,
                text_bet_days, text_trader_30_days_count, text_follower,text_type;
        CircleImageView img_head;
        LinearLayout layout_tags;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_userName);
            text_trader_30_days_rate = itemView.findViewById(R.id.text_trader_30_days_rate);
            text_trader_30_days_defeat = itemView.findViewById(R.id.text_trader_30_days_defeat);
            text_trader_30_days_draw = itemView.findViewById(R.id.text_trader_30_days_draw);
            text_bet_days = itemView.findViewById(R.id.text_bet_days);
            text_trader_30_days_count = itemView.findViewById(R.id.text_trader_30_days_count);
            text_follower = itemView.findViewById(R.id.text_follower);
            img_head = itemView.findViewById(R.id.img_head);
            layout_tags = itemView.findViewById(R.id.layout_tags);
            text_type=itemView.findViewById(R.id.text_type);
            itemView.findViewById(R.id.text_follow).setOnClickListener(v -> {
                if (onDetailClick != null) {
                    onDetailClick.onClickListener(datas.get(getPosition() - 1));
                }
            });


        }
    }

    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(FollowEntity.DataBean data);


    }


    //追加保证金监听
    private AddMarginClick addMarginClick;

    public void setAddMarginClick(AddMarginClick addMarginClick) {

        this.addMarginClick = addMarginClick;
    }

    public interface AddMarginClick {
        void onAddMarginClick(FollowEntity.DataBean data);
    }


    //止盈止损监听
    private ProfitLossClick profitLossClick;

    public void setProfitLossClick(ProfitLossClick profitLossClick) {
        this.profitLossClick = profitLossClick;
    }

    public interface ProfitLossClick {
        void onProfitLossListener(FollowEntity.DataBean data);
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
