package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.FollowHistoryAdapter;
import com.pro.bityard.adapter.FollowerListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowHistoryEntity;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.entity.FollowersListEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.view.RadarView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.FIRST;
import static com.pro.bityard.config.AppConfig.FOLLOW;
import static com.pro.bityard.config.AppConfig.LOAD;
import static com.pro.bityard.config.AppConfig.TRADE;

public class FollowDetailActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Observer {
    private static final String DATA_VALUE = "DATA_VALUE";
    private static final String TYPE = "TYPE";


    private FollowerDetailEntity.DataBean followerUser;

    @BindView(R.id.layout_view)
    RelativeLayout layout_view;

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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_tags)
    LinearLayout layout_tags;
    @BindView(R.id.view_line)
    View view_line;

    @BindView(R.id.radarView)
    RadarView radarView;
    @BindView(R.id.text_profit_share_ratio)
    TextView text_profit_share_ratio;
    private double mul_ratio;
    private int page;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private FollowHistoryAdapter followHistoryAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.text_location)
    TextView text_location;


    @BindView(R.id.layout_one)
    LinearLayout layout_one;
    @BindView(R.id.layout_two)
    RelativeLayout layout_two;

    @BindView(R.id.text_share_tip)
    TextView text_share_tip;
    /*------------------------------------*/


    private int page_follower;
    private int lastVisibleItem_follower;
    private LinearLayoutManager linearLayoutManager_follower;
    private FollowerListAdapter followerListAdapter;


    @BindView(R.id.radioGroup_hold)
    RadioGroup radioGroup;
    private String type_self;
    private List<String> quoteList;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_follow_detail;
    }

    public static void enter(Context context, String type, Object value) {
        Intent intent = new Intent(context, FollowDetailActivity.class);
        intent.putExtra(DATA_VALUE, (Serializable) value);
        intent.putExtra(TYPE, type);
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
        btn_submit.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        followHistoryAdapter = new FollowHistoryAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(followHistoryAdapter);


        Util.colorSwipe(this, swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            page_follower = 1;
            getHistoryData(FIRST, page);
            getFollowerData(FIRST, page_follower);
        });

        //防止冲突
        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i >= 0) {
                swipeRefreshLayout.setEnabled(true); //当滑动到顶部的时候开启
            } else {
                swipeRefreshLayout.setEnabled(false); //否则关闭
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == followHistoryAdapter.getItemCount() - 1) {
                    followHistoryAdapter.startLoad();
                    page = page + 1;
                    getHistoryData(AppConfig.LOAD, page);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        /*----------------------------------------------------*/

        followerListAdapter = new FollowerListAdapter(this);
        linearLayoutManager_follower = new LinearLayoutManager(this);


        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        followerUser = (FollowerDetailEntity.DataBean) intent.getSerializableExtra(DATA_VALUE);


        type_self = intent.getStringExtra(TYPE);

        SocketQuoteManger.getInstance().addObserver(this);


        Log.d("print", "initData: " + followerUser);

        mul_ratio = TradeUtil.mul(followerUser.getTraderRatio(), 100);
        String profit_share_ratio = getString(R.string.text_profit_share_ratio);
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String format = String.format(profit_share_ratio, (mul_ratio + "%"));


        text_profit_share_ratio.setText(format);

        text_profit_share_ratio.setOnClickListener(this);

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


        double trader30DaysCount = followerUser.getTrader30DaysCount();//30天交易笔数
        double trader30DaysDefeat = followerUser.getTrader30DaysDefeat();//30天交易胜率
        String follower = followerUser.getFollower();//跟随者
        double trader30DaysRate = followerUser.getTrader30DaysRate();//30天带单收益率
        double traderTotalIncome = followerUser.getIncomeRate();//带单总收益


        Log.d("print", "initData:169:  " + trader30DaysCount + "   " + trader30DaysRate + "  " + trader30DaysDefeat + "  " + traderTotalIncome + "  " + follower);
        List<String> datas = new ArrayList<>();
        datas.add(String.valueOf(trader30DaysCount));//0~10000
        datas.add(TradeUtil.getNumberFormat(trader30DaysDefeat, 2) + "%");//0~10
        datas.add(follower);//0~1000
        datas.add(TradeUtil.getNumberFormat(trader30DaysRate, 2) + "%");//0~1
        datas.add(TradeUtil.getNumberFormat(traderTotalIncome, 2)+"%");//0~5000


        List<Double> scaleData = new ArrayList<>();
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysCount, 500, 10), 100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysDefeat, 100, 10), 100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(Double.parseDouble(follower), 200, 10), 100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(trader30DaysRate, 500, 10), 100));
        scaleData.add(TradeUtil.mul(TradeUtil.div(traderTotalIncome, 500, 10), 100));


       /* scaleData.add(20.0);
        scaleData.add(60.0);
        scaleData.add(80.0);
        scaleData.add(35.0);
        scaleData.add(19.0);*/


        radarView.setData(datas, scaleData);
        page = 1;
        getHistoryData(FIRST, page);
        page_follower = 1;
        getFollowerData(FIRST, page);


        UserDetailEntity userDetailEntity = SPUtils.getData(AppConfig.DETAIL, UserDetailEntity.class);
        Log.d("print ", "initData:352: " + userDetailEntity.getUser().getUserId() + "  " + followerUser.getUserId());
        if (userDetailEntity != null) {
            UserDetailEntity.UserBean user = userDetailEntity.getUser();
            if (user.getUserId().equals(followerUser.getUserId())) {
                if (user.getType() != 2) {
                    layout_one.setVisibility(View.GONE);
                    layout_two.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    text_share_tip.setVisibility(View.VISIBLE);
                    btn_submit.setText(R.string.text_open_copy_trade);
                    type_self="OPEN";

                } else {
                    layout_one.setVisibility(View.VISIBLE);
                    layout_two.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    text_share_tip.setVisibility(View.GONE);
                    btn_submit.setText(R.string.text_go_trade);
                    type_self=TRADE;

                }
            } else {
                if (type_self.equals(TRADE)) {
                    btn_submit.setText(R.string.text_go_trade);
                } else {
                    btn_submit.setText(getString(R.string.text_copy));
                }
            }
        }


    }

    private void getHistoryData(String type, int page) {
        NetManger.getInstance().followHistory(followerUser.getUserId(), String.valueOf(page), "10", (state, response) -> {
            if (state.equals(BUSY)) {

            } else if (state.equals(SUCCESS)) {
                FollowHistoryEntity followHistoryEntity = (FollowHistoryEntity) response;
                if (type.equals(LOAD)) {
                    followHistoryAdapter.addDatas(followHistoryEntity.getData());
                } else {
                    followHistoryAdapter.setDatas(followHistoryEntity.getData());
                }
            }
        });
    }


    private void getFollowerData(String type, int page) {
        NetManger.getInstance().followerList(followerUser.getUserId(), String.valueOf(page), "10", (state, response) -> {
            if (state.equals(BUSY)) {

            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                FollowersListEntity followersListEntity = (FollowersListEntity) response;
                if (type.equals(LOAD)) {
                    followerListAdapter.addDatas(followersListEntity.getData());
                } else {
                    followerListAdapter.setDatas(followersListEntity.getData());
                }
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (type_self.equals(TRADE)) {
                    TradeTabActivity.enter(this, "1", quoteList.get(0));

                } else if (type_self.equals(FOLLOW)){
                    UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_SETTINGS_FOLLOW, followerUser);
                }else {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FOLLOWER_MANGER);
                }

                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.text_profit_share_ratio:
                Util.lightOff(this);
                String text_ratio_tip = getString(R.string.text_ratio_tip);
                @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String format = String.format(text_ratio_tip, (mul_ratio + "%"));
                PopUtil.getInstance().showTip(this, layout_view, false, format,
                        state -> {

                        });
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_hold:


                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(followHistoryAdapter);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == followHistoryAdapter.getItemCount() - 1) {
                            followHistoryAdapter.startLoad();
                            page = page + 1;
                            getHistoryData(AppConfig.LOAD, page);

                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    }
                });

                break;
            case R.id.radio_follower:


                recyclerView.setLayoutManager(linearLayoutManager_follower);
                recyclerView.setAdapter(followerListAdapter);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem_follower == followerListAdapter.getItemCount() - 1) {
                            followerListAdapter.startLoad();
                            page_follower = page_follower + 1;
                            getFollowerData(AppConfig.LOAD, page_follower);

                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        lastVisibleItem_follower = linearLayoutManager_follower.findLastVisibleItemPosition();
                    }
                });

                break;

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get("0");


        }
    }
}
