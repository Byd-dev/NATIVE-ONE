package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuoteHomeAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.BannerEntity;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.fragment.hold.HistoryFragment;
import com.pro.bityard.fragment.hold.PendingFragment;
import com.pro.bityard.fragment.hold.PositionFragment;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.InitManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TabManger;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.ListUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.view.StatusBarHeightView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;
import com.stx.xhb.xbanner.XBanner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class MainFollowActivity extends BaseActivity implements Observer, View.OnClickListener {

    public static boolean isForeground = false;


    @BindView(R.id.layout_view)
    RelativeLayout layout_view;
    @BindView(R.id.layout_home)
    LinearLayout layout_home;
    @BindView(R.id.layout_market)
    LinearLayout layout_market;
    @BindView(R.id.layout_circle)
    LinearLayout layout_circle;
    @BindView(R.id.layout_my)
    LinearLayout layout_my;
    @BindView(R.id.layout_status)
    StatusBarHeightView layout_status;
    @BindView(R.id.layout_login_register)
    RelativeLayout layout_login_register;
    @BindView(R.id.radioGroup)
    LinearLayout radioGroup;

    @BindView(R.id.radio_0)
    RadioButton radioButton_0;
    @BindView(R.id.radio_1)
    RadioButton radioButton_1;
    @BindView(R.id.radio_2)
    RadioButton radioButton_2;
    @BindView(R.id.radio_3)
    RadioButton radioButton_3;
    @BindView(R.id.radio_4)
    RadioButton radioButton_4;


    /*首页-------------------------------------------------------------*/
    @BindView(R.id.recyclerView_list)
    RecyclerView recyclerView_list;
    @BindView(R.id.banner)
    XBanner xBanner;
    @BindView(R.id.img_ad)
    ImageView img_ad;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private QuoteHomeAdapter quoteHomeAdapter;
    private QuoteAdapter quoteAdapter;
    @BindView(R.id.ts_news)
    TextSwitcher textSwitcher;
    private List<BannerEntity.NoticesBean> notices;
    private int mNewsIndex;
    @BindView(R.id.recyclerView_hot)
    RecyclerView recyclerView_hot;


    /*market---------------------------------------------------*/
    private List<String> titleList;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    @BindView(R.id.swipeRefreshLayout_market)
    SwipeRefreshLayout swipeRefreshLayout_market;

    @BindView(R.id.tabLayout_market)
    TabLayout tabLayout_market;

    @BindView(R.id.recyclerView_market)
    HeaderRecyclerView recyclerView_market;

    private QuoteAdapter quoteAdapter_market;

    private boolean flag_new_price = false;
    private boolean flag_up_down = false;
    private boolean flag_name = false;

    @BindView(R.id.img_price_triangle)
    ImageView img_price_triangle;

    @BindView(R.id.img_rate_triangle)
    ImageView img_rate_triangle;

    @BindView(R.id.img_name_triangle)
    ImageView img_name_triangle;
    private String type = "0";
    private int zone_type = 1;//-1是自选 1是主区 0是创新区 2是衍生品
    private ArrayMap<String, List<String>> arrayMap;


    /*社区  ---------------------------------------------------*/
    @BindView(R.id.swipeRefreshLayout_circle)
    SwipeRefreshLayout swipeRefreshLayout_circle;

    @BindView(R.id.img_head_circle)
    CircleImageView img_head_circle;

    @BindView(R.id.recyclerView_circle)
    HeaderRecyclerView recyclerView_circle;

    private FollowAdapter followAdapter;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;


    /*持仓 实盘 ---------------------------------------------------*/

    private String tradeType = "1";
    private BalanceEntity balanceEntity;


    /*我的  ---------------------------------------------------*/
    @BindView(R.id.text_userName)
    TextView text_userName;
    @BindView(R.id.img_service_my)
    ImageView img_service_my;
    @BindView(R.id.img_head)
    CircleImageView img_head;
    @BindView(R.id.text_uid)
    TextView text_uid;

    @BindView(R.id.text_currency)
    TextView text_currency;

    @BindView(R.id.text_balance)
    TextView text_balance;
    @BindView(R.id.text_balance_currency)
    TextView text_balance_currency;

    @BindView(R.id.img_eye_switch)
    ImageView img_eye_switch;
    @BindView(R.id.text_commissionRate)
    TextView text_commissionRate;

    @BindView(R.id.view_four)
    View view_four;
    @BindView(R.id.text_register)
    TextView text_register;
    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.text_byd_balance)
    TextView text_byd_balance;
    @BindView(R.id.text_fiat)
    TextView text_fiat;

    private boolean isEyeOpen = true;
    private String netIncomeResult;
    private List<PositionEntity.DataBean> positionRealList;
    private List<PositionEntity.DataBean> positionSimulationList;
    private LoginEntity loginEntity;
    private UserDetailEntity userDetailEntity;
    private FollowerDetailEntity followDetailEntity = null;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main_follow;

    }

    private List<String> quoteList;


    @Override
    public void update(Observable o, Object arg) {

        if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);


            if (quoteList != null) {
                runOnUiThread(() -> {
                    layout_null.setVisibility(View.GONE);
                    recyclerView_market.setVisibility(View.VISIBLE);
                    quoteHomeAdapter.setDatas(arrayMap.get("5"));
                    quoteAdapter.setDatas(arrayMap.get("6"));
                    quoteAdapter_market.setDatas(quoteList);
                    if (quoteList.size() >= 3) {
                        if (isLogin()) {
                            if (tradeType.equals("1")) {
                                TradeUtil.setNetIncome(tradeType, positionRealList, quoteList);
                            } else {
                                TradeUtil.setNetIncome(tradeType, positionSimulationList, quoteList);
                            }
                        }
                    }


                    if (quoteAdapter_market_pop != null) {
                        //搜索框
                        if (edit_search.getText().toString().equals("")) {
                            quoteAdapter_market_pop.setDatas(quoteList);
                        } else {
                            List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), quoteList);
                            quoteAdapter_market_pop.setDatas(searchQuoteList);
                        }
                    }


                });
            } else {
                layout_null.setVisibility(View.VISIBLE);
                recyclerView_market.setVisibility(View.GONE);
            }
        } else if (o == BalanceManger.getInstance()) {

            balanceEntity = (BalanceEntity) arg;
        } else if (o == PositionRealManger.getInstance()) {
            positionRealList = (List<PositionEntity.DataBean>) arg;


        } else if (o == PositionSimulationManger.getInstance()) {
            positionSimulationList = (List<PositionEntity.DataBean>) arg;


        } else if (o == NetIncomeManger.getInstance()) {
            netIncomeResult = (String) arg;

            //Log.d("netIncome", "update:273: " + isLogin() + "  --   " + netIncomeResult);

            String[] NetIncome = netIncomeResult.split(",");
            runOnUiThread(() -> {
                // 1,2.5,5  类型 整体净盈亏  整体  保证金
                String netIncome = NetIncome[1];
                String margin = NetIncome[2];


                if (NetIncome[0].equals("1") && tradeType.equals("1")) {
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            setMyNetIncome(balanceEntity, netIncome, margin);
                        }
                    } else {

                        if (isEyeOpen) {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }

                    }
                }
            });


            runOnUiThread(() -> {
                if (NetIncome[0].equals("2") && tradeType.equals("2")) {
                    // 1,2.5,5
                    String netIncome = NetIncome[1];
                    String margin = NetIncome[2];
                    Log.d("print", "update:模拟: " + netIncome + "  --  " + margin);
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                                if (data.getCurrency().equals("USDT")) {
                                    double game = data.getGame();
                                    //   Log.d("print", "update:337:模拟:  " + game);
                                    double add1 = TradeUtil.add(game, Double.parseDouble(margin));
                                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));
                                }
                            }
                        }
                    } else {

                        if (isEyeOpen) {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }

                    }


                }
            });
        } else if (o == TabManger.getInstance()) {
            Integer a = (Integer) arg;
            if (a == TAB_TYPE.TAB_POSITION) {
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_circle.setVisibility(View.VISIBLE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);

                runOnUiThread(() -> {
                    // Log.d("print", "update:337:  " + a + "  --   " + radioButton_2);
                    radioButton_2.setChecked(true);
                });
            }
        } else if (o == UserDetailManger.getInstance()) {
            userDetailEntity = (UserDetailEntity) arg;
            Log.d("print", "update:437:  " + userDetailEntity);
            runOnUiThread(() -> {
                if (userDetailEntity.getUser() != null) {
                    text_userName.setText(userDetailEntity.getUser().getUsername());
                    text_uid.setVisibility(View.VISIBLE);
                    if (loginEntity != null) {
                        text_uid.setText("UID:" + loginEntity.getUser().getUserId());
                    }
                    text_register.setVisibility(View.GONE);
                    img_edit.setVisibility(View.VISIBLE);
                    if (isEyeOpen) {
                        text_byd_balance.setText(String.valueOf(userDetailEntity.getUser().getEagle()));
                        text_commissionRate.setText(TradeUtil.mul(userDetailEntity.getUser().getCommRatio(), 100) + "%");
                    }

                } else {

                    text_userName.setText(getResources().getText(R.string.text_unlogin));
                    text_uid.setVisibility(View.GONE);
                    text_register.setVisibility(View.VISIBLE);
                    img_edit.setVisibility(View.GONE);
                    text_byd_balance.setText(getResources().getString(R.string.text_default));
                    text_commissionRate.setText(getResources().getString(R.string.text_default));
                    text_balance.setText(getResources().getString(R.string.text_default));
                    text_balance_currency.setText(getResources().getString(R.string.text_default));
                }
            });
        }
    }

    private void onSuccessListener(String data) {
        QuoteDetailActivity.enter(this, "1", data);
    }


    /**
     * 首页Tab索引
     */
    public static final class TAB_TYPE {
        public static final int COUNT = 4;
        public static final int TAB_HOME = 0;
        public static final int TAB_HALL = 1;
        public static final int TAB_POSITION = 2;
        public static final int TAB_MY = 3;


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetManger.getInstance().updateCheck(this, layout_view);
        //初始化
        InitManger.getInstance().init();
    }


    public static void enter(Context context, int tabIndex) {

        Intent intent = new Intent(context, MainFollowActivity.class);
        intent.putExtra(IntentConfig.Keys.POSITION, tabIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();

    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();


        if (quoteList == null) {
            layout_null.setVisibility(View.VISIBLE);
            recyclerView_market.setVisibility(View.GONE);
        } else {
            layout_null.setVisibility(View.GONE);
            recyclerView_market.setVisibility(View.VISIBLE);
        }

        //跟单列表
        getFollowList();
        if (isLogin()) {
            loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            text_userName.setText(loginEntity.getUser().getUserName());
            text_uid.setVisibility(View.VISIBLE);
            text_uid.setText("UID:" + loginEntity.getUser().getUserId());
            text_register.setVisibility(View.GONE);
            img_edit.setVisibility(View.VISIBLE);
            layout_login_register.setVisibility(View.GONE);
            img_service_my.setVisibility(View.VISIBLE);
            Glide.with(this).load(loginEntity.getUser().getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head_circle);
            Glide.with(this).load(loginEntity.getUser().getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);


        } else {


            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setVisibility(View.GONE);
            text_register.setVisibility(View.VISIBLE);
            img_edit.setVisibility(View.GONE);
            layout_login_register.setVisibility(View.VISIBLE);
            text_byd_balance.setText(getResources().getString(R.string.text_default));
            text_commissionRate.setText(getResources().getString(R.string.text_default));
            img_service_my.setVisibility(View.GONE);
            Glide.with(this).load(R.mipmap.icon_my_bityard).into(img_head_circle);
            Glide.with(this).load(R.mipmap.icon_my_bityard).into(img_head);

        }


        //我的页面 火币结算单位
        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        text_currency.setText("(" + cny + ")");

        String register_success = SPUtils.getString(AppConfig.POP_LOGIN, null);
        if (register_success != null) {
            SPUtils.remove(AppConfig.POP_LOGIN);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Util.lightOff(MainFollowActivity.this);
                PopUtil.getInstance().showSuccessTip(MainFollowActivity.this, layout_view, state -> {
                    if (state) {
                    }
                });
            }, 1000);

        }

        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        if (language.equals(AppConfig.ZH_SIMPLE) || language.equals(AppConfig.VI_VN) || language.equals(AppConfig.IN_ID)) {
            text_fiat.setVisibility(View.VISIBLE);
        } else {
            text_fiat.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        Toast.makeText(this, "执行了initView", Toast.LENGTH_SHORT).show();




       /* HashMap<String, String> map = new HashMap<>();
        map.put("cmid","3000");
        map.put("t", String.valueOf(System.currentTimeMillis()));
        map.put("symbols", "BYDUSDT_CC");*/


        //首页监听
        radioButton_0.setOnClickListener(this);
        radioButton_1.setOnClickListener(this);
        radioButton_2.setOnClickListener(this);
        radioButton_3.setOnClickListener(this);
        radioButton_4.setOnClickListener(this);


        //主题是深色的标题
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        //打开沉浸式状态栏
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //行情初始化


        //  SocketQuoteManger.getInstance().startScheduleJob(QUOTE_SECOND, QUOTE_SECOND);
        SocketQuoteManger.getInstance().addObserver(this);
        TabManger.getInstance().addObserver(this);
        //个人信息初始化
        UserDetailManger.getInstance().addObserver(this);
        //初始化 交易设置
        radioButton_2.setOnClickListener(this);





        /*首页 分割线-----------------------------------------------------------------------------*/


        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setLineSpacing(1.1f, 1.1f);
            textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.text_main_color));
            textView.setTextSize(13);
            textView.setSingleLine();
            return textView;
        });
        startScheduleJob(mHandler, 3000, 3000);
        //首页三个行情
        quoteHomeAdapter = new QuoteHomeAdapter(this);
        recyclerView_hot.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView_hot.setAdapter(quoteHomeAdapter);
        quoteHomeAdapter.setOnItemClick(data -> QuoteDetailActivity.enter(this, "1", data));
        img_head.setOnClickListener(this);
        findViewById(R.id.img_service).setOnClickListener(this);
        findViewById(R.id.layout_announcement).setOnClickListener(this);
        findViewById(R.id.text_login_register).setOnClickListener(this);
        findViewById(R.id.layout_mining).setOnClickListener(this);

        quoteAdapter = new QuoteAdapter(this);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_list.setAdapter(quoteAdapter);
        quoteAdapter.isShowIcon(false);
        quoteAdapter.isShowVolume(true);


        Util.colorSwipe(this, swipeRefreshLayout);
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);


        quoteAdapter.setOnItemClick(data -> QuoteDetailActivity.enter(this, "1", data));
        findViewById(R.id.layout_simulation_home).setOnClickListener(this);
        findViewById(R.id.layout_activity).setOnClickListener(this);


        /*行情 分割线-----------------------------------------------------------------------------*/

        quoteAdapter_market = new QuoteAdapter(this);
        recyclerView_market.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market.setAdapter(quoteAdapter_market);


        findViewById(R.id.img_search).setOnClickListener(this);

        layout_null.setVisibility(View.GONE);

        titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.text_optional));
        titleList.add(getResources().getString(R.string.text_main_zone));
        titleList.add(getResources().getString(R.string.text_innovate));
        titleList.add(getString(R.string.text_derived));
        for (String market_name : titleList) {
            tabLayout_market.addTab(tabLayout_market.newTab().setText(market_name));
        }
        tabLayout_market.getTabAt(1).select();


        tabLayout_market.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换都重置为默认
                flag_new_price = false;
                flag_up_down = false;
                flag_name = false;
                if (arrayMap == null) {
                    return;
                }

                if (tab.getPosition() == 0) {
                    type = "16";

                    quoteList = arrayMap.get(type);
                    Log.d("print", "onTabSelected:684:  " + quoteList + "  " + type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = -1;
                } else if (tab.getPosition() == 1) {
                    type = "0";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 1;
                } else if (tab.getPosition() == 2) {
                    type = "9";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 0;
                } else if (tab.getPosition() == 3) {
                    type = "23";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 2;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        quoteAdapter_market.isShowIcon(false);
        quoteAdapter_market.isShowVolume(true);
        @SuppressLint("InflateParams") View footView = LayoutInflater.from(this).inflate(R.layout.foot_tab_view, null, false);

        recyclerView_market.addFooterView(footView);


        findViewById(R.id.layout_new_price).setOnClickListener(this);
        findViewById(R.id.layout_up_down).setOnClickListener(this);
        findViewById(R.id.layout_name).setOnClickListener(this);
        swipeRefreshLayout_market.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout_market.setOnRefreshListener(() -> {
            swipeRefreshLayout_market.setRefreshing(false);
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
            if (quote_host == null && quote_code == null) {
                NetManger.getInstance().initQuote();
                WebSocketManager.getInstance().reconnect();
            } else {
                assert quote_host != null;
                // SocketQuoteManger.getInstance().quote(quote_host, quote_code);
            }
        });


        quoteAdapter_market.setOnItemClick(this::onSuccessListener);

        /*持仓 分割线-----------------------------------------------------------------------------*/
        //余额注册
        BalanceManger.getInstance().addObserver(this);
        //净值注册
        NetIncomeManger.getInstance().addObserver(this);




        /*持仓 实盘 分割线-----------------------------------------------------------------------------*/
        //持仓注册
        PositionRealManger.getInstance().addObserver(this);


        /*持仓 模拟 分割线-----------------------------------------------------------------------------*/
        PositionSimulationManger.getInstance().addObserver(this);
        /*社区  分割线-----------------------------------------------------------------------------*/

        swipeRefreshLayout_circle.setOnRefreshListener(this::getFollowList);
        Util.colorSwipe(this, swipeRefreshLayout_circle);

        @SuppressLint("InflateParams") View head_circle = LayoutInflater.from(this).inflate(R.layout.layout_head_circle, null, false);
        @SuppressLint("InflateParams") View footView_circle = LayoutInflater.from(this).inflate(R.layout.foot_circle_view, null, false);

        followAdapter = new FollowAdapter(this);
        recyclerView_circle.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_circle.addHeaderView(head_circle);
        recyclerView_circle.addFooterView(footView_circle);

        head_circle.findViewById(R.id.text_follow_settings).setOnClickListener(this);
        img_head_circle.setOnClickListener(this);

        TextView text_update = head_circle.findViewById(R.id.text_update_two);
        String strMsg = getString(R.string.text_two_update);
        String format = String.format(strMsg, "2");
        text_update.setText(Html.fromHtml(format));

        recyclerView_circle.setAdapter(followAdapter);

        head_circle.findViewById(R.id.layout_search).setOnClickListener(v -> {
            UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_SEARCH_NICKNAME);
        });
        head_circle.findViewById(R.id.layout_filter).setOnClickListener(v -> {
            FilterSettingsActivity.enter(this);
        });
        //查看优质投资者
        footView_circle.setOnClickListener(v -> {
            UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_FOLLOWER_LIST);

        });

        followAdapter.setWarningClick(() -> {
            Util.lightOff(this);
            PopUtil.getInstance().showTip(this, layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });

        followAdapter.setOnFollowClick(dataBean -> {
            FollowDetailActivity.enter(this, AppConfig.FOLLOW, dataBean);
        });



        /*我的 分割线-----------------------------------------------------------------------------*/
        findViewById(R.id.layout_balance).setOnClickListener(this);
        img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
        findViewById(R.id.layout_one).setOnClickListener(this);//安全中心监听
        findViewById(R.id.layout_two).setOnClickListener(this);
        findViewById(R.id.layout_three).setOnClickListener(this);
        findViewById(R.id.layout_four).setOnClickListener(this);
        findViewById(R.id.layout_five).setOnClickListener(this);
        findViewById(R.id.layout_six).setOnClickListener(this);
        findViewById(R.id.layout_seven).setOnClickListener(this);
        findViewById(R.id.layout_eight).setOnClickListener(this);//系统设置
        findViewById(R.id.layout_nine).setOnClickListener(this);
        findViewById(R.id.layout_login).setOnClickListener(this);
        text_register.findViewById(R.id.text_register).setOnClickListener(this);
        findViewById(R.id.text_account).setOnClickListener(this);
        findViewById(R.id.text_deposit).setOnClickListener(this);
        findViewById(R.id.text_withdrawal).setOnClickListener(this);
        findViewById(R.id.text_quick_exchange).setOnClickListener(this);
        findViewById(R.id.text_fiat).setOnClickListener(this);
        findViewById(R.id.img_edit).setOnClickListener(this);
        findViewById(R.id.text_mining).setOnClickListener(this);
        img_service_my.setOnClickListener(this);


    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            updateNews();

        }
    };


    private void updateNews() {
        if (notices != null) {
            mNewsIndex++;
            if (notices.size() > 0) {
                if (mNewsIndex >= notices.size()) mNewsIndex = 0;
                if (ListUtil.isNotEmpty(notices)) {
                    textSwitcher.setText(notices.get(mNewsIndex).getTitle());
                }
            }
        }
    }

    private void upBanner(List<BannerEntity.CarouselsBean> data) {
        if (data == null) {
            return;
        }

        if (xBanner != null) {

            xBanner.setBannerData(R.layout.item_banner_layout, data);
            xBanner.loadImage((banner, model, view, position) -> {

                ImageView imageView = view.findViewById(R.id.img_banner);
                TextView text_title = view.findViewById(R.id.text_title);

                text_title.setText(data.get(position).getName());

                Glide.with(Objects.requireNonNull(this)).load(data.get(position).getXBannerUrl()).into(imageView);
            });

            xBanner.setOnItemClickListener((banner, model, view, position) -> {


            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {

        //首页 -------------------------------------------------------------------------------------
        getBanner();


        //个人详情
        if (isLogin()) {
            UserDetailManger.getInstance().detail();

        }


    }

    private void getFollowList() {
        NetManger.getInstance().followList(null, null,
                null, null, null, null, null, null,
                null, null, null, "1", "20", (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);

                        FollowEntity followEntity = (FollowEntity) response;
                        List<FollowerDetailEntity.DataBean> followEntityData = followEntity.getData();
                        if (followEntityData.size() == 0) {
                            layout_circle_null.setVisibility(View.VISIBLE);
                            recyclerView_circle.setVisibility(View.GONE);
                        } else {
                            layout_circle_null.setVisibility(View.GONE);
                            recyclerView_circle.setVisibility(View.VISIBLE);
                            followAdapter.setDatas(followEntityData);

                        }
                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.VISIBLE);
                        recyclerView_circle.setVisibility(View.GONE);
                    }
                });

        if (loginEntity != null) {
            NetManger.getInstance().followerDetail(loginEntity.getUser().getUserId(), "USDT", (state, response) -> {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    followDetailEntity = (FollowerDetailEntity) response;
                } else if (state.equals(FAILURE)) {
                }
            });
        }


    }

    /*首页轮播图*/
    private void getBanner() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("action", "carousel");
        NetManger.getInstance().getRequest("/api/index.htm", map, (state, response) -> {
            if (state.equals(BUSY)) {
                if (swipeRefreshLayout != null) {

                    swipeRefreshLayout.setRefreshing(true);
                }
            } else if (state.equals(SUCCESS)) {
                if (swipeRefreshLayout != null) {

                    swipeRefreshLayout.setRefreshing(false);
                }
                String s = response.toString().replaceAll(" ", "");
                if (s.startsWith("error")) {
                    return;
                }

                BannerEntity bannerEntity = new Gson().fromJson(response.toString(), BannerEntity.class);
                if (bannerEntity == null) {
                    return;
                }
                if (bannerEntity.getCarousels() == null) {
                    return;
                }

                Iterator<BannerEntity.CarouselsBean> iterator = bannerEntity.getCarousels().iterator();
                while (iterator.hasNext()) {
                    BannerEntity.CarouselsBean value = iterator.next();
                    if (value.toString().contains(".")) {
                        String[] split = value.getName().split("\\.");
                        if (split[1].equals("false")) {
                            Glide.with(Objects.requireNonNull(this)).load(value.getUrl()).into(img_ad);
                            iterator.remove();

                        }
                    }
                }
                upBanner(bannerEntity.getCarousels());

                notices = bannerEntity.getNotices();


            } else if (state.equals(FAILURE)) {
                if (swipeRefreshLayout != null) {

                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    @Override
    protected void initEvent() {


    }


    private void initViewPager(ViewPager viewPager, String tradeType) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new PendingFragment().newInstance(tradeType), getString(R.string.text_order));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));

        viewPager.setAdapter(myPagerAdapter);
    }

    private void initSimulationViewPager(ViewPager viewPager, String tradeType) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));
        viewPager.setAdapter(myPagerAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("print", "onDestroy:912:  " + "执行了Ondestory");
        SocketQuoteManger.getInstance().clear();
        //QuoteCustomizeListManger.getInstance().cancelTimer();
        //QuoteCustomizeListManger.getInstance().clear();
        SPUtils.remove(AppConfig.RATE_LIST);
        UserDetailManger.getInstance().clear();
        cancelTimer();
        //  UserDetailManger.getInstance().cancelTimer();


    }


    private QuoteAdapter quoteAdapter_market_pop;
    // 声明平移动画
    private TranslateAnimation animation;
    private EditText edit_search;

    /*显示行情选择的按钮*/
    private void showQuotePopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.quote_market, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        TabLayout tabLayout_market_search = view.findViewById(R.id.tabLayout_market);

        LinearLayout layout_null = view.findViewById(R.id.layout_null);

        RecyclerView recyclerView_market = view.findViewById(R.id.recyclerView_market);

        quoteAdapter_market_pop = new QuoteAdapter(this);
        recyclerView_market.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market.setAdapter(quoteAdapter_market_pop);

        quoteAdapter_market_pop.setDatas(quoteList);
        quoteAdapter_market_pop.isShowIcon(false);
        ImageView img_price_triangle = view.findViewById(R.id.img_price_triangle);

        ImageView img_rate_triangle = view.findViewById(R.id.img_rate_triangle);

        ImageView img_name_triangle = view.findViewById(R.id.img_name_triangle);


        for (String market_name : titleList) {
            tabLayout_market_search.addTab(tabLayout_market_search.newTab().setText(market_name));
        }
        tabLayout_market_search.getTabAt(1).select();
        view.findViewById(R.id.layout_new_price).setOnClickListener(v -> {
            if (arrayMap == null) {
                return;
            }

            if (flag_new_price) {
                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                flag_new_price = false;
                if (zone_type == 1) {
                    type = "1";
                } else if (zone_type == 0) {
                    type = "10";
                } else if (zone_type == -1) {
                    type = "17";
                } else if (zone_type == 2) {
                    type = "24";
                }
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market.setDatas(quoteList);


            } else {

                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_new_price = true;
                if (zone_type == 1) {
                    type = "2";
                } else if (zone_type == 0) {
                    type = "11";

                } else if (zone_type == -1) {
                    type = "18";
                } else if (zone_type == 2) {
                    type = "25";
                }
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market.setDatas(quoteList);

            }
            img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
            img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

        });
        view.findViewById(R.id.layout_up_down).setOnClickListener(v -> {
            if (arrayMap == null) {
                return;
            }
            if (flag_up_down) {
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                flag_up_down = false;
                if (zone_type == 1) {
                    type = "3";
                } else if (zone_type == 0) {
                    type = "12";
                } else if (zone_type == -1) {
                    type = "19";
                } else if (zone_type == 2) {
                    type = "26";
                }

                List<String> quoteList = arrayMap.get(type);

                quoteAdapter_market.setDatas(quoteList);

            } else {
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_up_down = true;
                if (zone_type == 1) {
                    type = "4";
                } else if (zone_type == 0) {
                    type = "13";

                } else if (zone_type == -1) {
                    type = "20";
                } else if (zone_type == 2) {
                    type = "27";
                }
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market.setDatas(quoteList);

            }
            img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
            img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
        });
        view.findViewById(R.id.layout_name).setOnClickListener(v -> {
            if (arrayMap == null) {
                return;
            }
            if (flag_name) {
                img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                flag_name = false;
                if (zone_type == 1) {
                    type = "8";
                } else if (zone_type == 0) {
                    type = "15";

                } else if (zone_type == -1) {
                    type = "21";
                } else if (zone_type == 2) {
                    type = "28";
                }
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market.setDatas(quoteList);

            } else {
                img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_name = true;
                if (zone_type == 1) {
                    type = "7";
                } else if (zone_type == 0) {
                    type = "14";

                } else if (zone_type == -1) {
                    type = "22";
                } else if (zone_type == 2) {
                    type = "29";
                }
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market.setDatas(quoteList);

            }
            img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
            img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
        });

        tabLayout_market_search.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换都重置为默认
                flag_new_price = false;
                flag_up_down = false;
                flag_name = false;
                if (arrayMap == null) {
                    return;
                }

                if (tab.getPosition() == 0) {
                    type = "16";

                    quoteList = arrayMap.get(type);
                    Log.d("print", "onTabSelected:684:  " + quoteList + "  " + type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = -1;
                } else if (tab.getPosition() == 1) {
                    type = "0";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 1;
                } else if (tab.getPosition() == 2) {
                    type = "9";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 0;
                } else if (tab.getPosition() == 3) {
                    type = "23";

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_market.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    zone_type = 2;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SwipeRefreshLayout swipeRefreshLayout_market = view.findViewById(R.id.swipeRefreshLayout_market);
        swipeRefreshLayout_market.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout_market.setOnRefreshListener(() -> {
            swipeRefreshLayout_market.setRefreshing(false);
            quoteAdapter_market_pop.setDatas(quoteList);
          /*  String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
            if (quote_host == null && quote_code == null) {
                NetManger.getInstance().initQuote();
                WebSocketManager.getInstance().reconnect();
            } else {
                assert quote_host != null;
                // SocketQuoteManger.getInstance().quote(quote_host, quote_code);
            }*/
        });

        quoteAdapter_market_pop.setOnItemClick(data -> {
            popupWindow.dismiss();
            type = "1";
            QuoteDetailActivity.enter(this, "1", data);

        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            type = "1";
            tabLayout_market.getTabAt(1).select();
            popupWindow.dismiss();
        });

        RelativeLayout layout_bar = view.findViewById(R.id.layout_bar);

        edit_search = view.findViewById(R.id.edit_search);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    layout_bar.setVisibility(View.GONE);
                    tabLayout_market.setVisibility(View.GONE);
                    tabLayout_market.getTabAt(1).select();
                    type = "all";
                    List<String> strings = arrayMap.get(type);
                    List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                    quoteAdapter_market_pop.setDatas(searchQuoteList);
                } else {
                    layout_bar.setVisibility(View.VISIBLE);
                    tabLayout_market.setVisibility(View.VISIBLE);
                    type = "1";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market_pop.setDatas(quoteList);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Util.dismiss(this, popupWindow);
        Util.isShowing(this, popupWindow);


        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(layout_view);
        //   popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);

    }

    @Override
    public void onClick(View v) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, AppConfig.ZH_SIMPLE);

        switch (v.getId()) {
            case R.id.radio_0:
                layout_home.setVisibility(View.VISIBLE);
                layout_market.setVisibility(View.GONE);
                layout_circle.setVisibility(View.GONE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);

                radioButton_0.setChecked(true);
                radioButton_1.setChecked(false);
                radioButton_2.setChecked(false);
                radioButton_3.setChecked(false);
                radioButton_4.setChecked(false);

                break;
            case R.id.radio_1:
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.VISIBLE);
                layout_circle.setVisibility(View.GONE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);

                radioButton_0.setChecked(false);
                radioButton_1.setChecked(true);
                radioButton_2.setChecked(false);
                radioButton_3.setChecked(false);
                radioButton_4.setChecked(false);
                break;
            case R.id.radio_2:
                if (quoteList == null) {
                    return;
                }
                QuoteDetailActivity.enter(MainFollowActivity.this, "1", quoteList.get(0));

                break;

            case R.id.radio_3:
                radioButton_3.setChecked(false);
                radioButton_0.setChecked(false);
                radioButton_1.setChecked(false);
                radioButton_2.setChecked(false);
                radioButton_3.setChecked(true);
                radioButton_4.setChecked(false);
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_circle.setVisibility(View.VISIBLE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);

                break;
            case R.id.radio_4:
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_circle.setVisibility(View.GONE);
                layout_my.setVisibility(View.VISIBLE);
                layout_status.setVisibility(View.GONE);

                radioButton_0.setChecked(false);
                radioButton_1.setChecked(false);
                radioButton_2.setChecked(false);
                radioButton_3.setChecked(false);
                radioButton_4.setChecked(true);

                break;

            case R.id.text_login_register:
                LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);

                break;


            /*首页 -----------------------------------------------------------------------------------*/
            case R.id.layout_announcement:
                /*最新公告*/
            case R.id.layout_nine:
                UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_ANNOUNCEMENT);
                break;
            case R.id.layout_activity:
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(MainFollowActivity.this,
                            NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/activity"),
                            getResources().getString(R.string.text_trade_bonus));
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;

            case R.id.layout_simulation_home:
                if (quoteList == null) {
                    return;
                }
                QuoteDetailActivity.enter(MainFollowActivity.this, "2", quoteList.get(0));
                break;

            /*行情 -----------------------------------------------------------------------------------*/
            case R.id.img_search:
                Util.lightOff(this);
                type = "1";
                showQuotePopWindow();
                break;
            case R.id.layout_new_price:
                if (arrayMap == null) {
                    return;
                }

                if (flag_new_price) {
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_new_price = false;
                    if (zone_type == 1) {
                        type = "1";
                    } else if (zone_type == 0) {
                        type = "10";
                    } else if (zone_type == -1) {
                        type = "17";
                    } else if (zone_type == 2) {
                        type = "24";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);


                } else {

                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_new_price = true;
                    if (zone_type == 1) {
                        type = "2";
                    } else if (zone_type == 0) {
                        type = "11";

                    } else if (zone_type == -1) {
                        type = "18";
                    } else if (zone_type == 2) {
                        type = "25";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            case R.id.layout_up_down:
                if (arrayMap == null) {
                    return;
                }
                if (flag_up_down) {
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_up_down = false;
                    if (zone_type == 1) {
                        type = "3";
                    } else if (zone_type == 0) {
                        type = "12";

                    } else if (zone_type == -1) {
                        type = "19";
                    } else if (zone_type == 2) {
                        type = "26";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                } else {
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_up_down = true;
                    if (zone_type == 1) {
                        type = "4";
                    } else if (zone_type == 0) {
                        type = "13";

                    } else if (zone_type == -1) {
                        type = "20";
                    } else if (zone_type == 2) {
                        type = "27";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            case R.id.layout_name:
                if (arrayMap == null) {
                    return;
                }
                if (flag_name) {
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_name = false;
                    if (zone_type == 1) {
                        type = "8";
                    } else if (zone_type == 0) {
                        type = "15";

                    } else if (zone_type == -1) {
                        type = "21";
                    } else if (zone_type == 2) {
                        type = "28";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                } else {
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_name = true;
                    if (zone_type == 1) {
                        type = "7";
                    } else if (zone_type == 0) {
                        type = "14";

                    } else if (zone_type == -1) {
                        type = "22";
                    } else if (zone_type == 2) {
                        type = "29";
                    }
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            //跟单管理
            case R.id.text_follow_settings:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FOLLOW_SETTINGS);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.img_head_circle:
                if (isLogin()) {
                    if (followDetailEntity == null) {
                        NetManger.getInstance().followerDetail(loginEntity.getUser().getUserId(), "USDT", (state, response) -> {
                            if (state.equals(BUSY)) {
                            } else if (state.equals(SUCCESS)) {
                                followDetailEntity = (FollowerDetailEntity) response;
                            } else if (state.equals(FAILURE)) {
                            }
                        });
                    } else {
                        FollowDetailActivity.enter(this, AppConfig.TRADE, followDetailEntity.getData());
                    }
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }

                break;
            /*我的 -----------------------------------------------------------------------------------*/
            case R.id.img_head:
            case R.id.layout_login:
                if (isLogin()) {
                    PersonActivity.enter(MainFollowActivity.this);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;

            case R.id.text_register:
                RegisterActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_REGISTER);
                break;

            case R.id.layout_eight:
                UserActivity.enter(this, IntentConfig.Keys.KEY_SET_UP);

                break;
            case R.id.layout_balance:

                if (isEyeOpen) {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    text_balance.setText("***");
                    text_balance_currency.setText("***");
                    text_commissionRate.setText("***");
                    text_byd_balance.setText("***");

                    isEyeOpen = false;
                } else {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    isEyeOpen = true;
                    if (isLogin()) {

                        if (userDetailEntity == null) {
                            return;
                        }
                        if (userDetailEntity.getUser() == null) {
                            return;
                        }
                        text_commissionRate.setText(TradeUtil.mul(userDetailEntity.getUser().getCommRatio(), 100) + "%");
                        text_byd_balance.setText(String.valueOf(userDetailEntity.getUser().getEagle()));

                        if (netIncomeResult != null) {
                            String[] NetIncome = netIncomeResult.split(",");
                            if (NetIncome[0].equals("1")) {
                                if (balanceEntity != null) {
                                    String netIncome = NetIncome[1];
                                    String margin = NetIncome[2];
                                    setMyNetIncome(balanceEntity, netIncome, margin);
                                }
                            }
                        } else {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                            text_commissionRate.setText(getResources().getString(R.string.text_default));
                            text_byd_balance.setText(getResources().getString(R.string.text_default));
                        }
                    } else {
                        text_balance.setText(getResources().getString(R.string.text_default));
                        text_balance_currency.setText(getResources().getString(R.string.text_default));
                        text_commissionRate.setText(getResources().getString(R.string.text_default));
                        text_byd_balance.setText(getResources().getString(R.string.text_default));
                    }


                }

                break;
            //安全中心
            case R.id.layout_one:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_SAFE_CENTER);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            //资金记录
            case R.id.layout_two:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*交易记录*/
            case R.id.layout_three:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_TRADE_HISTORY);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*邀请记录*/
            case R.id.layout_four:

                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_INVITE_HISTORY);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*交易设置*/
            case R.id.layout_five:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_TRADE_SETTINGS);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*提币地址管理*/
            case R.id.layout_six:

                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_WITHDRAWAL_ADDRESS);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*客服系统*/

            case R.id.img_service:
            case R.id.layout_seven:
            case R.id.img_service_my:

                String url;
                if (isLogin()) {
                    url = String.format(NetManger.SERVICE_URL, language, loginEntity.getUser().getUserId(), loginEntity.getUser().getAccount());
                } else {
                    url = String.format(NetManger.SERVICE_URL, language, "", "游客");
                }
                WebActivity.getInstance().openUrl(MainFollowActivity.this, url, getResources().getString(R.string.text_my_service));

                break;
            /*前往挖矿*/
            case R.id.layout_mining:
            case R.id.text_mining:
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/mining"), getString(R.string.text_mining_title));

                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*资金账户*/
            case R.id.text_account:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_ACCOUNT);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*充币*/
            case R.id.text_deposit:
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(MainFollowActivity.this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/deposit"), getResources().getString(R.string.text_recharge));
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*提币*/
            case R.id.text_withdrawal:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_WITHDRAWAL);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*币币闪兑*/
            case R.id.text_quick_exchange:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_QUICK_EXCHANGE);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*法币充值*/
            case R.id.text_fiat:
                String url_api = null;
                switch (language) {
                    case AppConfig.ZH_SIMPLE:
                        url_api = "/cnRecharge";
                        break;
                    case AppConfig.VI_VN:
                        url_api = "/viRecharge";
                        break;
                    case AppConfig.IN_ID:
                        url_api = "/idRecharge";
                        break;
                }
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, url_api), getResources().getString(R.string.text_fabi_trade));
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*修改昵称*/
            case R.id.img_edit:
                if (isLogin()) {
                    Util.lightOff(MainFollowActivity.this);
                    PopUtil.getInstance().showEdit(MainFollowActivity.this, layout_view, true, result -> {
                        loginEntity.getUser().setUserName(result.toString());
                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                        onResume();
                    });
                }

                break;
        }
    }


    /*设置我的页面的总净资产*/
    private void setMyNetIncome(BalanceEntity balanceEntity, String netIncome, String margin) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                //汇率是实时的
                TradeUtil.getRateList(balanceEntity, "1", response -> {
                    double money1 = data.getMoney();//可用余额
                    double add2 = TradeUtil.add(money1, Double.parseDouble(margin));//+保证金
                    double ad3 = TradeUtil.add(add2, Double.parseDouble(netIncome));//+浮动盈亏
                    //  Log.d("print", "setMyNetIncome:1205: " + money1 + "  " + add2 + "   " + ad3);
                    //账户净值=可用余额+占用保证金+浮动盈亏
                    double money = Double.parseDouble(response.toString());//所有钱包的和
                    double add1 = TradeUtil.add(money, Double.parseDouble(margin));//+保证金
                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));//+浮动盈亏
                    if (isEyeOpen) {
                        text_balance.setText(TradeUtil.getNumberFormat(add, 2));
                        String string = SPUtils.getString(AppConfig.USD_RATE, null);
                        text_balance_currency.setText(TradeUtil.numberHalfUp(TradeUtil.mul(add, Double.parseDouble(string)), 2));
                    }
                });

            }
        }
    }

   /* public void setNetIncome(String tradeType, List<PositionEntity.DataBean> positionList, List<String> quoteList) {
        Log.d("hold", "setNetIncome:204:  " + positionList);
        if (positionList == null) {
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder append = stringBuilder.append(tradeType).append(",").append(0.0)
                    .append(",").append(0.0);
            //总净值=可用余额-冻结资金+总净盈亏+其他钱包换算成USDT额
            //账户净值=可用余额+占用保证金+浮动盈亏
            Log.d("hold", "setNetIncome:发送的数据 210:  " + append.toString());
            NetIncomeManger.getInstance().postNetIncome(append.toString());
        } else if (positionList.size() == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder append = stringBuilder.append(tradeType).append(",").append(0.0)
                    .append(",").append(0.0);
            //总净值=可用余额-冻结资金+总净盈亏+其他钱包换算成USDT额
            //账户净值=可用余额+占用保证金+浮动盈亏
            Log.d("hold", "setNetIncome:发送的数据 219:  " + append.toString());
            NetIncomeManger.getInstance().postNetIncome(append.toString());
        } else {
            TradeUtil.getNetIncome(quoteList, positionList, response1 -> TradeUtil.getMargin(positionList, response2 -> {
                double margin;
                double income;
                //    Log.d("print", "setNetIncome: 207: "+positionList+"    "+response1+"    "+response2);
                if (positionList == null) {
                    margin = 0.0;
                    income = 0.0;
                } else {
                    margin = Double.parseDouble(response2.toString());
                    income = Double.parseDouble(response1.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder append = stringBuilder.append(tradeType).append(",").append(income)
                        .append(",").append(margin);
                //总净值=可用余额-冻结资金+总净盈亏+其他钱包换算成USDT额
                //账户净值=可用余额+占用保证金+浮动盈亏


                Log.d("hold", "setNetIncome:发送的数据 220:  " + append.toString());
                NetIncomeManger.getInstance().postNetIncome(append.toString());
            }));
        }


    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}