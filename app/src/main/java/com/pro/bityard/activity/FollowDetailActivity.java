package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pro.bityard.R;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.view.RadarView;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class FollowDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String DATA_VALUE = "DATA_VALUE";
    private FollowEntity.DataBean followerUser;


    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.img_head)
    CircleImageView img_head;
    @BindView(R.id.text_userName)
    TextView text_userName;
    @BindView(R.id.text_type)
    TextView text_type;
    @BindView(R.id.text_profile)
    TextView text_profile;
    @BindView(R.id.text_registerTime)
    TextView text_registerTime;

    @BindView(R.id.recyclerView_history)
    RecyclerView recyclerView_history;
    @BindView(R.id.layout_tags)
    LinearLayout layout_tags;
    @BindView(R.id.layout_view)
    View view_line;

    @BindView(R.id.radarView)
    RadarView radarView;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_follow_detail;
    }

    public static void enter(Context context, Object value) {
        Intent intent = new Intent(context, FollowDetailActivity.class);
        intent.putExtra(DATA_VALUE, (Serializable) value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);


    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();

        followerUser = (FollowEntity.DataBean) intent.getSerializableExtra(DATA_VALUE);
        Log.d("print", "initData: " + followerUser);

        Glide.with(this).load(followerUser.getAvatar()).error(getResources().getDrawable(R.mipmap.icon_my_bityard)).apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3))).into(img_bg);

        Glide.with(this).load(followerUser.getAvatar()).error(getResources().getDrawable(R.mipmap.icon_my_bityard)).into(img_head);

        List<String> ideaTags = followerUser.getIdearTags();
        if (ideaTags.size() != 0) {
            text_profile.setText(ideaTags.get(0));
        }
        text_userName.setText(followerUser.getUsername());
        text_registerTime.setText(TradeUtil.dateToStampWithout(followerUser.getRegisterTime()) + " " + getString(R.string.text_join));

        /*动态添加tag*/
        List<String> styleTags = followerUser.getStyleTags();
        if (styleTags.size() != 0) {
            layout_tags.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
            layout_tags.removeAllViews();
            for (int i = 0; i < styleTags.size(); i++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setBackground(getResources().getDrawable(R.drawable.bg_shape_circle_green));
                textView.setTextColor(getResources().getColor(R.color.text_main_color));
                textView.setPadding(10, 0, 10, 0);
                textView.setTextSize(14);
                textView.setText(styleTags.get(i));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
                lp.setMargins(0, 0, 10, 0);
                textView.setLayoutParams(lp);
                layout_tags.addView(textView);
            }
            layout_tags.setPadding(0, 0, 10, 0);
        } else {
            layout_tags.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
        }

        String value_type = null;
        int type = followerUser.getType();
        switch (type) {
            case 1:
                value_type = getString(R.string.text_normal_user);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_user), null, null, null);

                break;
            case 2:
                value_type = getString(R.string.text_normal_trader);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_normal_trader), null, null, null);
                break;
            case 3:
                value_type = getString(R.string.text_pro_trader);
                text_type.setCompoundDrawablesWithIntrinsicBounds(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_pro_trader), null, null, null);

                break;
        }

        text_type.setText(value_type);


        double trader30DaysIncome = followerUser.getTrader30DaysIncome();//30天带单收益
        double trader30DaysDefeat = followerUser.getTrader30DaysDefeat();//30天交易胜率
        String follower = followerUser.getFollower();//跟随者
        double trader30DaysDraw = followerUser.getTrader30DaysDraw();//30天最大回撤率
        double trader30DaysRate = followerUser.getTrader30DaysRate();//30天带单收益率


        Log.d("print", "initData:169:  "+trader30DaysIncome+"   "+trader30DaysDefeat+"  "+follower+"  "+trader30DaysDraw+"  "+trader30DaysRate);
        List<String> datas = new ArrayList<>();
        datas.add(String.valueOf(trader30DaysIncome));//0~10000
        datas.add(TradeUtil.getNumberFormat(trader30DaysDefeat,2)+"%");//0~10
        datas.add(follower);//0~1000
        datas.add(TradeUtil.getNumberFormat(trader30DaysDraw,2)+"%");//0~1
        datas.add(TradeUtil.getNumberFormat(trader30DaysRate,2)+"%");//0~1


        List<Double> scaleData=new ArrayList<>();
       /* scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysIncome,10000,10),100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysDefeat,1000,10),100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(Double.parseDouble(follower),1000,10),100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysDraw,100,10),100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysRate,100,10),100));*/


        scaleData.add(20.0);
        scaleData.add(60.0);
        scaleData.add(80.0);
        scaleData.add(35.0);
        scaleData.add(19.0);


        radarView.setData(datas,scaleData);






    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_SETTINGS_FOLLOW, followerUser);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }
}
