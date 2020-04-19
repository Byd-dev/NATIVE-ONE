package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuoteHomeAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.BannerEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.fragment.hold.HistoryFragment;
import com.pro.bityard.fragment.hold.PendingFragment;
import com.pro.bityard.fragment.hold.PositionFragment;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.InitManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.manger.TabManger;
import com.pro.bityard.utils.ListUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.view.StatusBarHeightView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;
import com.stx.xhb.xbanner.XBanner;

import org.jetbrains.annotations.NotNull;

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
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;

public class MainOneActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, Observer, View.OnClickListener {

    @BindView(R.id.layout_home)
    LinearLayout layout_home;
    @BindView(R.id.layout_market)
    LinearLayout layout_market;
    @BindView(R.id.layout_hold)
    LinearLayout layout_hold;
    @BindView(R.id.layout_my)
    LinearLayout layout_my;
    @BindView(R.id.layout_status)
    StatusBarHeightView layout_status;

    @BindView(R.id.radio_2)
    RadioButton radioButton_2;


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
    @BindView(R.id.swipeRefreshLayout_market)
    SwipeRefreshLayout swipeRefreshLayout_market;

    @BindView(R.id.recyclerView_market)
    HeaderRecyclerView recyclerView_market;

    private QuoteAdapter quoteAdapter_market;

    private int flag_new_price = 0;
    private int flag_up_down = 0;

    @BindView(R.id.img_new_price)
    ImageView img_new_price;

    @BindView(R.id.img_up_down)
    ImageView img_up_down;
    private String type = "1";
    private ArrayMap<String, List<String>> arrayMap;


    /*持仓  ---------------------------------------------------*/
    @BindView(R.id.radioGroup_hold)
    RadioGroup radioGroup_hold;
    @BindView(R.id.layout_real)
    LinearLayout layout_real;
    @BindView(R.id.layout_simulation)
    LinearLayout layout_simulation;


    /*持仓 实盘 ---------------------------------------------------*/
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.text_available)
    TextView text_available;
    @BindView(R.id.text_freeze)
    TextView text_freeze;
    @BindView(R.id.text_worth)
    TextView text_worth;
    private String tradeType = "1";
    private BalanceEntity balanceEntity;

    /*持仓 模拟 ---------------------------------------------------*/
    @BindView(R.id.tabLayout_simulation)
    TabLayout tabLayout_simulation;
    @BindView(R.id.viewPager_simulation)
    ViewPager viewPager_simulation;
    @BindView(R.id.text_available_simulation)
    TextView text_available_simulation;
    @BindView(R.id.text_freeze_simulation)
    TextView text_freeze_simulation;
    @BindView(R.id.text_worth_simulation)
    TextView text_worth_simulation;

    /*我的  ---------------------------------------------------*/
    @BindView(R.id.text_userName)
    TextView text_userName;

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

    private boolean isEyeOpen = true;
    private String netIncomeResult;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main_one;

    }

    private List<String> quoteList;

    @Override
    public void update(Observable o, Object arg) {

        if (o == QuoteListManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);
            runOnUiThread(() -> {
                assert quoteList != null;
                if (quoteList.size() >= 3) {
                    quoteHomeAdapter.setDatas(quoteList.subList(0, 3));
                    quoteAdapter.setDatas(quoteList);
                    quoteAdapter_market.setDatas(quoteList);
                }
            });


        } else if (o == BalanceManger.getInstance()) {

            balanceEntity = (BalanceEntity) arg;

            // TradeUtil.getRate(balanceEntity, "1", response -> Log.d("print", "setResult:137实盘:  " + response.toString()));
            Log.d("print", "setResult:137实盘:  " + tradeType + "  " + balanceEntity);
            runOnUiThread(() -> {
                if (tradeType.equals("1") && text_available != null) {
                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals("USDT")) {
                            text_available.setText(TradeUtil.getNumberFormat(data.getMoney(), 2));
                        }
                    }
                }
            });
            runOnUiThread(() -> {
                if (tradeType.equals("2") && text_available_simulation != null) {
                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals("USDT")) {
                            text_available_simulation.setText(TradeUtil.getNumberFormat(data.getGame(), 2));
                        }
                    }

                }
            });

        } else if (o == PositionRealManger.getInstance()) {

            List<PositionEntity.DataBean> positionList = (List<PositionEntity.DataBean>) arg;
            runOnUiThread(() -> {
                if (text_freeze != null) {
                    TradeUtil.getMargin(positionList, response -> {
                        if (response == null) {
                            text_freeze.setText(getResources().getString(R.string.text_default));
                        } else {
                            text_freeze.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()), 2));
                        }
                    });
                }
            });


        } else if (o == PositionSimulationManger.getInstance()) {
            List<PositionEntity.DataBean> positionList = (List<PositionEntity.DataBean>) arg;

            runOnUiThread(() -> {
                if (text_freeze_simulation != null) {
                    TradeUtil.getMargin(positionList, response -> {
                        if (response == null) {
                            text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                        } else {
                            text_freeze_simulation.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()), 2));

                        }

                    });
                }
            });
        } else if (o == NetIncomeManger.getInstance()) {
            Log.d("print", "update:273: " + isLogin());
            netIncomeResult = (String) arg;
            String[] NetIncome = netIncomeResult.split(",");
            runOnUiThread(() -> {
                if (text_worth != null && NetIncome[0].equals("1") && tradeType.equals("1")) {
                    // 1,2.5,5  类型 整体净盈亏  整体  保证金
                    String netIncome = NetIncome[1];
                    String margin = NetIncome[2];
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                                if (data.getCurrency().equals("USDT")) {
                                    double money1 = data.getMoney();//可用余额
                                    double add2 = TradeUtil.add(money1, Double.parseDouble(margin));//+保证金
                                    double ad3 = TradeUtil.add(add2, Double.parseDouble(netIncome));//+浮动盈亏
                                    text_worth.setText(TradeUtil.getNumberFormat(ad3, 2));
                                    //汇率是实时的
                                    TradeUtil.getRateList(balanceEntity, "1", response -> {
                                        //账户净值=可用余额+占用保证金+浮动盈亏
                                        double money = Double.parseDouble(response.toString());//所有钱包的和
                                        double add1 = TradeUtil.add(money, Double.parseDouble(margin));//+保证金
                                        double add = TradeUtil.add(add1, Double.parseDouble(netIncome));//+浮动盈亏
                                        if (isEyeOpen) {
                                            text_balance.setText(TradeUtil.getNumberFormat(add, 2));
                                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                                            text_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(add, Double.parseDouble(string)), 2));
                                        }
                                    });

                                }
                            }
                        }
                    } else {
                        text_worth.setText(getResources().getString(R.string.text_default));
                        text_worth_simulation.setText(getResources().getString(R.string.text_default));
                        if (isEyeOpen) {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }
                        text_available.setText(getResources().getString(R.string.text_default));
                        text_available_simulation.setText(getResources().getString(R.string.text_default));
                        text_freeze.setText(getResources().getString(R.string.text_default));
                        text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                    }
                }
            });


            runOnUiThread(() -> {
                if (text_worth_simulation != null && NetIncome[0].equals("2") && tradeType.equals("2")) {
                    // 1,2.5,5
                    String netIncome = NetIncome[1];
                    String margin = NetIncome[2];
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                                if (data.getCurrency().equals("USDT")) {
                                    double game = data.getGame();
                                    double add1 = TradeUtil.add(game, Double.parseDouble(margin));
                                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));
                                    text_worth_simulation.setText(TradeUtil.getNumberFormat(add, 2));
                               /* TradeUtil.getRate(balanceEntity, "2", response -> {
                                    // double money = Double.parseDouble(response.toString());
                                    double game = data.getGame();
                                    double add1 = TradeUtil.add(game, Double.parseDouble(margin));
                                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));
                                    text_worth_simulation.setText(TradeUtil.getNumberFormat(add, 2));
                                });*/

                                }
                            }
                        }
                    } else {
                        text_worth.setText(getResources().getString(R.string.text_default));
                        text_worth_simulation.setText(getResources().getString(R.string.text_default));
                        if (isEyeOpen) {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }
                        text_available.setText(getResources().getString(R.string.text_default));
                        text_available_simulation.setText(getResources().getString(R.string.text_default));
                        text_freeze.setText(getResources().getString(R.string.text_default));
                        text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                    }


                }
            });
        } else if (o == TabManger.getInstance()) {
            Integer a = (Integer) arg;
            if (a == TAB_TYPE.TAB_POSITION) {
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_hold.setVisibility(View.VISIBLE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);
                layout_real.setVisibility(View.VISIBLE);
                layout_simulation.setVisibility(View.GONE);
                runOnUiThread(() -> {
                    Log.d("print", "update:337:  " + a + "  --   " + radioButton_2);
                    radioButton_2.setChecked(true);
                });
            }
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


    }


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    public static void enter(Context context, int tabIndex) {

        Intent intent = new Intent(context, MainOneActivity.class);
        intent.putExtra(IntentConfig.Keys.POSITION, tabIndex);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            Log.d("print", "onResume:30:  " + data);
            text_userName.setText(data.getUser().getAccount());
            text_uid.setText(data.getUser().getUserId());
        } else {
            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setText("--");

        }

        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        text_currency.setText("(" + cny + ")");


    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        //主题是深色的标题
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        //打开沉浸式状态栏
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //行情初始化
        QuoteListManger.getInstance().startScheduleJob(QUOTE_SECOND, QUOTE_SECOND);
        TabManger.getInstance().addObserver(this);
        //初始化 交易设置
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();


        /*首页 分割线-----------------------------------------------------------------------------*/
        QuoteListManger.getInstance().addObserver(this);
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setLineSpacing(1.1f, 1.1f);
            textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this), R.color.text_maincolor));
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
        view.findViewById(R.id.img_icon1).setOnClickListener(this);
        view.findViewById(R.id.img_icon2).setOnClickListener(this);
        view.findViewById(R.id.img_head).setOnClickListener(this);
        quoteAdapter = new QuoteAdapter(this);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_list.setAdapter(quoteAdapter);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::getBanner);

        quoteAdapter.setOnItemClick(data -> QuoteDetailActivity.enter(this, "1", data));
        findViewById(R.id.layout_simulation_home).setOnClickListener(this);


        /*行情 分割线-----------------------------------------------------------------------------*/
        quoteAdapter_market = new QuoteAdapter(this);
        recyclerView_market.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market.setAdapter(quoteAdapter_market);
        @SuppressLint("InflateParams") View footView = LayoutInflater.from(this).inflate(R.layout.tab_foot_view, null);

        recyclerView_market.addFooterView(footView);


        findViewById(R.id.layout_new_price).setOnClickListener(this);
        findViewById(R.id.layout_up_down).setOnClickListener(this);

        swipeRefreshLayout_market.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout_market.setOnRefreshListener(() -> {
            swipeRefreshLayout_market.setRefreshing(false);
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
            if (quote_host == null && quote_code == null) {
                NetManger.getInstance().initQuote();
            } else {
                assert quote_host != null;
                QuoteListManger.getInstance().quote(quote_host, quote_code);
            }
        });


        quoteAdapter_market.setOnItemClick(this::onSuccessListener);

        /*持仓 分割线-----------------------------------------------------------------------------*/
        //余额注册
        BalanceManger.getInstance().addObserver(this);
        //净值注册
        NetIncomeManger.getInstance().addObserver(this);

        radioGroup_hold.getChildAt(0).performClick();
        radioGroup_hold.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_hold_0:
                    layout_real.setVisibility(View.VISIBLE);
                    layout_simulation.setVisibility(View.GONE);
                    tradeType = "1";
                    break;
                case R.id.radio_hold_1:
                    layout_real.setVisibility(View.GONE);
                    layout_simulation.setVisibility(View.VISIBLE);
                    tradeType = "2";
                    break;
            }
        });

        /*持仓 实盘 分割线-----------------------------------------------------------------------------*/
        //持仓注册
        PositionRealManger.getInstance().addObserver(this);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager, "1");

        /*持仓 模拟 分割线-----------------------------------------------------------------------------*/
        PositionSimulationManger.getInstance().addObserver(this);
        viewPager_simulation.setOffscreenPageLimit(3);
        tabLayout_simulation.setupWithViewPager(viewPager_simulation);
        initSimulationViewPager(viewPager_simulation, "2");

        /*我的 分割线-----------------------------------------------------------------------------*/
        findViewById(R.id.layout_six).setOnClickListener(this);
        findViewById(R.id.layout_balance).setOnClickListener(this);
        img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye));

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

    @Override
    protected void initData() {

        QuoteListManger.getInstance().addObserver(this);

        InitManger.getInstance().init();

        radioButton_2.setOnClickListener(this);


        //首页 -------------------------------------------------------------------------------------
        getBanner();
        if (balanceEntity != null) {
            for (BalanceEntity.DataBean data1 : balanceEntity.getData()) {
                if (data1.getCurrency().equals("USDT")) {
                    String string = SPUtils.getString(AppConfig.CURRENCY, null);
                    text_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data1.getMoney(), Double.parseDouble(string)), 2));


                }
            }
        }
        //持仓
        //hhh
       /* NetManger.getInstance().getHold(tradeType, (state, response1, response2) -> {
            if (state.equals(BUSY)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            } else if (state.equals(SUCCESS)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                PositionEntity positionEntity = (PositionEntity) response1;
                TradeUtil.myNetIncome("1", positionEntity.getData(), quoteList, response -> {

                    String[] NetIncome = response.toString().split(",");

                    //账户净值=可用余额+占用保证金+浮动盈亏
                    double money = Double.parseDouble(response.toString());//所有钱包的和
                    double add1 = TradeUtil.add(money, Double.parseDouble(NetIncome[1]));//+保证金
                    double add = TradeUtil.add(add1, Double.parseDouble(NetIncome[2]));//+浮动盈亏

                    if (isEyeOpen) {
                        text_balance.setText(TradeUtil.getNumberFormat(add, 2));
                        String string = SPUtils.getString(AppConfig.USD_RATE, null);
                        text_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(add, Double.parseDouble(string)), 2));
                    }
                });


            } else if (state.equals(FAILURE)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });*/


        if (balanceEntity != null) {
            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                if (data.getCurrency().equals("USDT")) {
                    text_available.setText(TradeUtil.getNumberFormat(data.getMoney(), 2));
                    double money1 = data.getMoney();//可用余额
                    if (netIncomeResult != null) {
                        String[] netIncome = netIncomeResult.split(",");
                        double add2 = TradeUtil.add(money1, Double.parseDouble(netIncome[1]));//+保证金
                        double ad3 = TradeUtil.add(add2, Double.parseDouble(netIncome[2]));//+浮动盈亏
                        text_worth.setText(TradeUtil.getNumberFormat(ad3, 2));
                    }


                }
            }


        }


        //我的


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
        Log.d("print", "initViewPager:HoldRealFragment: " + tradeType);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                layout_home.setVisibility(View.VISIBLE);
                layout_market.setVisibility(View.GONE);
                layout_hold.setVisibility(View.GONE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);
                layout_real.setVisibility(View.GONE);
                layout_simulation.setVisibility(View.GONE);

                break;
            case R.id.radio_1:
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.VISIBLE);
                layout_hold.setVisibility(View.GONE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);
                layout_real.setVisibility(View.GONE);
                layout_simulation.setVisibility(View.GONE);

                break;


            case R.id.radio_3:
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_hold.setVisibility(View.VISIBLE);
                layout_my.setVisibility(View.GONE);
                layout_status.setVisibility(View.VISIBLE);
                if (tradeType.equals("1")) {
                    layout_real.setVisibility(View.VISIBLE);
                    layout_simulation.setVisibility(View.GONE);
                } else {
                    layout_real.setVisibility(View.GONE);
                    layout_simulation.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.radio_4:
                layout_home.setVisibility(View.GONE);
                layout_market.setVisibility(View.GONE);
                layout_hold.setVisibility(View.GONE);
                layout_my.setVisibility(View.VISIBLE);
                layout_status.setVisibility(View.GONE);
                layout_real.setVisibility(View.GONE);
                layout_simulation.setVisibility(View.GONE);


                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QuoteListManger.getInstance().cancelTimer();
        QuoteListManger.getInstance().clear();
        SPUtils.remove(AppConfig.RATE_LIST);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.radio_2:
                if (quoteList == null) {
                    return;
                }
                QuoteDetailActivity.enter(MainOneActivity.this, "1", quoteList.get(0));
                break;
            /*首页 -----------------------------------------------------------------------------------*/
            case R.id.layout_simulation_home:
                if (quoteList == null) {
                    return;
                }
                QuoteDetailActivity.enter(MainOneActivity.this, "2", quoteList.get(0));
                break;

            /*行情 -----------------------------------------------------------------------------------*/
            case R.id.layout_new_price:
                if (arrayMap == null) {
                    return;
                }

                if (flag_new_price == 0) {
                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_new_price = 1;
                    type = "1";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);


                } else if (flag_new_price == 1) {

                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_new_price = 0;
                    type = "2";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            case R.id.layout_up_down:
                if (arrayMap == null) {
                    return;
                }
                if (flag_up_down == 0) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_up_down = 1;
                    type = "3";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                } else if (flag_up_down == 1) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_up_down = 0;
                    type = "4";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            /*我的 -----------------------------------------------------------------------------------*/
            case R.id.layout_six:
                UserActivity.enter(this, IntentConfig.Keys.KEY_SET_UP);

                break;
            case R.id.layout_balance:

                if (isEyeOpen) {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    text_balance.setText("***");
                    text_balance_currency.setText("**");
                    isEyeOpen = false;
                } else {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye));
                    isEyeOpen = true;
                    if (isLogin()) {

                    } else {
                        text_balance.setText(getResources().getString(R.string.text_default));
                        text_balance_currency.setText(getResources().getString(R.string.text_default));
                    }


                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}