package com.pro.bityard.circleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FollowerDetailEntity.DataBean> datas;


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public boolean isLoadMore = false;


    public FollowAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<FollowerDetailEntity.DataBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void addDatas(List<FollowerDetailEntity.DataBean> datas) {
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
            String value_type = null;
            int type = datas.get(position).getType();
            switch (type) {
                case 1:
                    value_type = context.getString(R.string.text_normal_user);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_user), null, null, null);

                    break;
                case 2:
                    value_type = context.getString(R.string.text_normal_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_trader), null, null, null);
                    break;
                case 3:
                    value_type = context.getString(R.string.text_pro_trader);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);
                    break;
                case 4:
                    value_type = context.getString(R.string.text_investors);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);
                    break;
                case 5:
                    value_type = context.getString(R.string.text_internal_account);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);
                    break;
                case 6:
                    value_type = context.getString(R.string.text_copy_account);
                    ((MyViewHolder) holder).text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);
                    break;
            }

            ((MyViewHolder) holder).text_type.setText(value_type);


            double mul = TradeUtil.mul(datas.get(position).getIncomeRate(),1);
            if (mul >= 0) {
                ((MyViewHolder) holder).text_trader_total_rate.setTextColor(context.getResources().getColor(R.color.text_quote_green));

            } else {
                ((MyViewHolder) holder).text_trader_total_rate.setTextColor(context.getResources().getColor(R.color.text_quote_red));

            }
            ((MyViewHolder) holder).text_trader_30_days_income.setText(datas.get(position).getTrader30DaysRate() + "%");

            double mul1 = TradeUtil.mul(datas.get(position).getTraderIncome(), 1);
            ((MyViewHolder) holder).text_trader_30_days_defeat.setText(datas.get(position).getTrader30DaysDefeat() + "%");

            ((MyViewHolder) holder).text_trader_total_rate.setText(TradeUtil.getNumberFormat(datas.get(position).getIncomeRate(),2)+"%");

            ((MyViewHolder) holder).text_bet_days.setText(datas.get(position).getBetDays());

            ((MyViewHolder) holder).text_trader_30_days_count.setText(String.valueOf(datas.get(position).getTrader30DaysCount()));

            ((MyViewHolder) holder).text_follower.setText(datas.get(position).getFollower());

            ((MyViewHolder) holder).text_follow.setOnClickListener(v -> {
                if (onFollowClick != null) {
                    onFollowClick.onClickListener(datas.get(position));
                }
            });


            ((MyViewHolder) holder).layout_name.setOnClickListener(v -> {
                if (onDetailClick != null) {
                    onDetailClick.onClickListener(datas.get(position));
                }
            });

            ((MyViewHolder) holder).img_head.setOnClickListener(v -> {
                if (onDetailClick != null) {
                    onDetailClick.onClickListener(datas.get(position));
                }
            });

            if (datas.get(position).isFollow()){
                ((MyViewHolder) holder).text_follow.setText(context.getResources().getString(R.string.text_edit));
                ((MyViewHolder) holder).text_follow.setBackground(context.getDrawable(R.drawable.gradient_bg_copy_main));
                ((MyViewHolder) holder).text_follow.setTextColor(context.getResources().getColor(R.color.text_main_color_black));

            }else {
                ((MyViewHolder) holder).text_follow.setText(context.getResources().getString(R.string.text_copy_trade));
                ((MyViewHolder) holder).text_follow.setBackground(context.getDrawable(R.drawable.gradient_bg_blue));
                ((MyViewHolder) holder).text_follow.setTextColor(context.getResources().getColor(R.color.text_main_color));

            }
            if (position>=3){
                ((MyViewHolder) holder).img_fire.setVisibility(View.GONE);
            }else {
                ((MyViewHolder) holder).img_fire.setVisibility(View.VISIBLE);

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
            bar = itemView.findViewById(R.id.progress);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_name, text_trader_30_days_income, text_trader_30_days_defeat, text_trader_total_rate,
                text_bet_days, text_trader_30_days_count, text_follower, text_type, text_follow;
        CircleImageView img_head;
        ImageView img_fire;
        LinearLayout layout_tags,layout_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_userName);
            text_trader_30_days_income = itemView.findViewById(R.id.text_trader_30_days_income);
            text_trader_30_days_defeat = itemView.findViewById(R.id.text_trader_30_days_defeat);
            text_trader_total_rate = itemView.findViewById(R.id.text_trader_total_rate);
            text_bet_days = itemView.findViewById(R.id.text_bet_days);
            text_trader_30_days_count = itemView.findViewById(R.id.text_trader_30_days_count);
            text_follower = itemView.findViewById(R.id.text_follower);
            img_head = itemView.findViewById(R.id.img_head);
            layout_tags = itemView.findViewById(R.id.layout_tags);
            layout_name = itemView.findViewById(R.id.layout_name);
            img_fire=itemView.findViewById(R.id.img_fire);

            text_type = itemView.findViewById(R.id.text_type);
            text_follow = itemView.findViewById(R.id.text_follow);

            itemView.findViewById(R.id.layout_warning).setOnClickListener(v -> {
                if (warningClick != null) {
                    warningClick.setWarningClick();
                }
            });


        }
    }

    //查看详情的监听
    private OnFollowClick onFollowClick;

    public void setOnFollowClick(OnFollowClick onFollowClick) {
        this.onFollowClick = onFollowClick;
    }

    public interface OnFollowClick {
        void onClickListener(FollowerDetailEntity.DataBean dataBean);


    }


    //查看详情的监听
    private OnDetailClick onDetailClick;

    public void setOnDetailClick(OnDetailClick onDetailClick) {
        this.onDetailClick = onDetailClick;
    }

    public interface OnDetailClick {
        void onClickListener(FollowerDetailEntity.DataBean dataBean);


    }

    //平仓的监听
    private WarningClick warningClick;

    public void setWarningClick(WarningClick warningClick) {
        this.warningClick = warningClick;
    }

    public interface WarningClick {
        void setWarningClick();
    }
}
