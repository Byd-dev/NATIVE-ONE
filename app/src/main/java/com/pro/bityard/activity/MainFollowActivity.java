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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.AccountAdapter;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.adapter.MarketSearchHotAdapter;
import com.pro.bityard.adapter.OptionalSelectAdapter;
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
import com.pro.bityard.entity.FollowerIncomeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.InitManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.NoticeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TabManger;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.ListUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.SocketUtil;
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
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;

public class MainFollowActivity extends BaseActivity implements Observer, View.OnClickListener {
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "${applicationId}.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


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


    private List<String> titleList, titlePopList, optionalTitleList, contractTitleList, spotTitleList;

    @BindView(R.id.layout_optional_select)
    LinearLayout layout_optional_select;
    @BindView(R.id.layout_contract_select)
    LinearLayout layout_contract_select;
    @BindView(R.id.layout_spot_select)
    LinearLayout layout_spot_select;
    @BindView(R.id.recyclerView_optional)
    RecyclerView recyclerView_optional;
    @BindView(R.id.recyclerView_contract)
    RecyclerView recyclerView_contract;
    @BindView(R.id.recyclerView_spot)
    RecyclerView recyclerView_spot;
    private OptionalSelectAdapter optionalSelectAdapter, optionalSelectAdapterPop, contractSelectAdapter, spotSelectAdapter;
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    @BindView(R.id.text_add)
    TextView text_add;

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
    private String type = AppConfig.CONTRACT_IN_ALL;
    private String zone_type = AppConfig.VIEW_CONTRACT_IN;


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
    @BindView(R.id.tabLayout_my)
    TabLayout tabLayout_my;
    @BindView(R.id.img_record)
    ImageView img_record;
    @BindView(R.id.layout_assets_my)
    LinearLayout layout_assets_my;
    @BindView(R.id.layout_account_my)
    ScrollView layout_account_my;
    @BindView(R.id.text_all_profit)
    TextView text_all_profit;

    @BindView(R.id.layout_commissionRate)
    LinearLayout layout_commissionRate;

    private List<String> myTitleList;

    @BindView(R.id.text_userName)
    TextView text_userName;

    @BindView(R.id.img_head)
    CircleImageView img_head;
    @BindView(R.id.text_uid)
    TextView text_uid;

    /*@BindView(R.id.text_currency)
    TextView text_currency;*/
    private String prizeTradeValue;

    //礼金余额
    private TextView text_balance, text_currency, text_bonus_balance, text_bonus_balance_currency, text_bonus_currency;
    //红包余额
    private TextView text_balance_currency, text_gift_balance, text_gift_balance_currency, text_gift_currency;
    private AccountAdapter accountAdapter;
    @BindView(R.id.recyclerView_assets)
    HeaderRecyclerView recyclerView_assets;
    @BindView(R.id.swipeRefreshLayout_assets)
    SwipeRefreshLayout swipeRefreshLayout_assets;
    ImageView img_eye_switch;
    @BindView(R.id.text_commissionRate)
    TextView text_commissionRate;


    @BindView(R.id.text_register)
    TextView text_register;
    @BindView(R.id.img_edit)
    ImageView img_edit;
    @BindView(R.id.text_byd_balance)
    TextView text_byd_balance;
    TextView text_fiat;

    private boolean isEyeOpen = true;
    private String netIncomeResult;
    private List<PositionEntity.DataBean> positionRealList;
    private List<PositionEntity.DataBean> positionSimulationList;
    private LoginEntity loginEntity;
    private UserDetailEntity userDetailEntity;
    private FollowerDetailEntity followDetailEntity = null;
    private LinearLayout layout_history_content;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main_follow;

    }

    private List<String> quoteList;

    ArrayMap<String, String> map;

    @Override
    public void update(Observable o, Object arg) {

        if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);


            if (quoteList != null) {
                runOnUiThread(() -> {
                    // layout_null.setVisibility(View.GONE);
                    recyclerView_market.setVisibility(View.VISIBLE);
                    quoteHomeAdapter.setDatas(arrayMap.get(AppConfig.HOME_HOT));
                    quoteAdapter.setDatas(arrayMap.get(AppConfig.HOME_LIST));
                    quoteAdapter_market.setDatas(quoteList);


                   /* map = new ArrayMap<>();
                    for (int i = 0; i < quoteList.size(); i++) {
                        String[] split1 = quoteList.get(i).split(",");
                        map.put(removeDigital(split1[0]), split1[4]);
                        quoteAdapter_market.refreshPartItem(i, map);
                        continue;
                    }*/


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

                    if (quoteAdapter_history != null) {
                        List<String> historyQuoteList = arrayMap.get(AppConfig.HISTORY_ALL);
                        if (historyQuoteList != null) {
                            if (layout_history_content != null) {
                                layout_history_content.setVisibility(View.VISIBLE);
                            }
                            quoteAdapter_history.setDatas(historyQuoteList);
                        }

                    }
                });
            } else {
                //layout_null.setVisibility(View.VISIBLE);
                //recyclerView_market.setVisibility(View.GONE);
            }
        } else if (o == BalanceManger.getInstance()) {

            balanceEntity = (BalanceEntity) arg;
            if (accountAdapter != null) {
                accountAdapter.setDatas(balanceEntity.getData());
            }

            setBonus(balanceEntity);
            setGift(balanceEntity);
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
        } else if (o == NoticeManger.getInstance()) {
            Log.d("print", "update:478:  " + "收到");
            getFollowList();
        }
    }

    private void onSuccessListener(String data) {
        Toast.makeText(this, itemQuoteContCode(data), Toast.LENGTH_LONG).show();
        WebSocketManager.getInstance().cancelQuotes("4002", "");
        SocketUtil.switchQuotesList("3002");

        if (TradeUtil.type(data).equals(AppConfig.TYPE_FT)) {
            TradeTabActivity.enter(this, "1", data);
        } else {
            SpotTradeActivity.enter(this, tradeType, data);
        }


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

    private void setBonus(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_bonus_balance.setText(TradeUtil.getNumberFormat(data.getPrize(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_bonus_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getPrize(), Double.parseDouble(string)), 2));


                //汇率是实时的
              /*  TradeUtil.getRateList(balanceEntity, "2", response -> {
                    double money = Double.parseDouble(response.toString());//所有钱包的和

                    if (isEyeOpen) {
                        if (isAdded()) {
                            text_bonus_balance.setText(TradeUtil.getNumberFormat(money, 2));
                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                            text_bonus_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(money, Double.parseDouble(string)), 2));
                        }
                    }
                });*/

            }
        }
    }

    private void setGift(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_gift_balance.setText(TradeUtil.getNumberFormat(data.getLucky(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_gift_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getLucky(), Double.parseDouble(string)), 2));

              /*  //汇率是实时的
                TradeUtil.getRateList(balanceEntity, "3", response -> {
                    double money = Double.parseDouble(response.toString());//所有钱包的和

                    if (isEyeOpen) {
                        if (isAdded()) {
                            text_gift_balance.setText(TradeUtil.getNumberFormat(money, 2));
                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                            text_gift_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(money, Double.parseDouble(string)), 2));
                        }
                    }
                });*/

            }
        }
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
        SocketUtil.switchQuotesList("3001");


        //跟单列表
        if (isLogin()) {
            loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            text_userName.setText(loginEntity.getUser().getUserName());
            text_uid.setVisibility(View.VISIBLE);
            text_uid.setText("UID:" + loginEntity.getUser().getUserId());
            text_register.setVisibility(View.GONE);
            img_edit.setVisibility(View.VISIBLE);
            layout_login_register.setVisibility(View.GONE);
            Glide.with(this).load(loginEntity.getUser().getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head_circle);
            Glide.with(this).load(loginEntity.getUser().getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);
            layout_commissionRate.setVisibility(View.VISIBLE);
            getFollowIncome();
            accountAdapter.setZero(false);

        } else {


            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setVisibility(View.GONE);
            text_register.setVisibility(View.VISIBLE);
            img_edit.setVisibility(View.GONE);
            layout_login_register.setVisibility(View.VISIBLE);
            text_byd_balance.setText(getResources().getString(R.string.text_default));
            text_commissionRate.setText(getResources().getString(R.string.text_default));
            Glide.with(this).load(R.mipmap.icon_my_bityard).into(img_head_circle);
            Glide.with(this).load(R.mipmap.icon_my_bityard).into(img_head);
            layout_commissionRate.setVisibility(View.GONE);

            text_all_profit.setText(getResources().getString(R.string.text_all_profit) + ": 0.0");
            //资产
            text_balance.setText(getResources().getString(R.string.text_default));
            text_balance_currency.setText(getResources().getString(R.string.text_default));
            text_bonus_balance.setText(getResources().getString(R.string.text_default));
            text_gift_balance.setText(getResources().getString(R.string.text_default));
            accountAdapter.setZero(true);
        }


        //我的页面 火币结算单位
        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        text_currency.setText("(" + cny + ")");
        text_bonus_currency.setText("(" + cny + ")");
        text_gift_currency.setText("(" + cny + ")");


        String register_success = SPUtils.getString(AppConfig.POP_LOGIN, null);
        if (register_success != null) {
            SPUtils.remove(AppConfig.POP_LOGIN);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Util.lightOff(MainFollowActivity.this);
                PopUtil.getInstance().showSuccessTip(MainFollowActivity.this, layout_view, state -> {
                    if (state) {
                        if (isLogin()) {
                            WebActivity.getInstance().openUrl(this,
                                    NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/activity"),
                                    getResources().getString(R.string.text_trade_bonus));
                        } else {
                            LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                        }
                    }
                });
            }, 1000);

        }
        //暂时永久关闭法币充值入口
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        if (language.equals(AppConfig.ZH_SIMPLE) || language.equals(AppConfig.VI_VN) || language.equals(AppConfig.IN_ID)) {
            text_fiat.setVisibility(View.GONE);
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
        //跟单的消息提醒
        NoticeManger.getInstance().addObserver(this);





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
        quoteHomeAdapter.setOnItemClick(this::onSuccessListener);
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


        quoteAdapter.setOnItemClick(this::onSuccessListener);
        findViewById(R.id.layout_simulation_home).setOnClickListener(this);
        findViewById(R.id.layout_activity).setOnClickListener(this);


        /*行情 分割线-----------------------------------------------------------------------------*/

        quoteAdapter_market = new QuoteAdapter(this);
        recyclerView_market.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market.setAdapter(quoteAdapter_market);
        /*自选*/
        optionalSelectAdapter = new OptionalSelectAdapter(this);
        recyclerView_optional.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView_optional.setAdapter(optionalSelectAdapter);
        optionalTitleList = new ArrayList<>();
        optionalTitleList.add(getString(R.string.text_contract));
        optionalTitleList.add(getString(R.string.text_spot));
        optionalSelectAdapter.setDatas(optionalTitleList);
        optionalSelectAdapter.setEnable(true);
        optionalSelectAdapter.select(getString(R.string.text_contract));
        /*自选的监听*/
        optionalSelectAdapter.setOnItemClick((position, data) -> {
            optionalSelectAdapter.select(data);
            switch (position) {
                case 0:
                    type = AppConfig.OPTIONAL_CONTRACT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_CONTRACT;

                    quoteList = arrayMap.get(type);
                    Log.d("print", "initView:779:  " + quoteList);

                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }

                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));


                    break;
                case 1:
                    type = AppConfig.OPTIONAL_SPOT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_SPOT;

                    quoteList = arrayMap.get(type);
                    Log.d("print", "initView:798:  " + quoteList);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }

                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));


                    break;
                case 2:
                    type = AppConfig.FOREIGN_EXCHANGE_ALL;
                    zone_type = AppConfig.VIEW_FOREIGN_EXCHANGE;
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
                    break;

            }
        });

        /*合约*/
        contractSelectAdapter = new OptionalSelectAdapter(this);
        recyclerView_contract.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView_contract.setAdapter(contractSelectAdapter);
        contractTitleList = new ArrayList<>();
        contractTitleList.add(getString(R.string.text_add_pass_coin));
        contractTitleList.add(getString(R.string.text_derived));
        contractTitleList.add(getString(R.string.text_foreign_exchange));

        contractSelectAdapter.setDatas(contractTitleList);
        contractSelectAdapter.select(getString(R.string.text_add_pass_coin));
        contractSelectAdapter.setEnable(true);
        /*合约的监听*/
        contractSelectAdapter.setOnItemClick((position, data) -> {
            contractSelectAdapter.select(data);
            switch (position) {
                case 0:
                    type = AppConfig.CONTRACT_IN_ALL;
                    zone_type = AppConfig.VIEW_CONTRACT_IN;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 1:
                    type = AppConfig.DERIVATIVES_ALL;
                    zone_type = AppConfig.VIEW_DERIVATIVES;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 2:
                    type = AppConfig.FOREIGN_EXCHANGE_ALL;
                    zone_type = AppConfig.VIEW_FOREIGN_EXCHANGE;
                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;

            }
        });


        /*现货*/
        spotSelectAdapter = new OptionalSelectAdapter(this);
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView_spot.setAdapter(spotSelectAdapter);
        spotTitleList = new ArrayList<>();
        spotTitleList.add(getString(R.string.text_all_country_code));
        spotTitleList.add("DeFi");
        spotTitleList.add("Pos");
        spotTitleList.add("Gray");
        spotTitleList.add("Bsc");

        spotSelectAdapter.setDatas(spotTitleList);
        spotSelectAdapter.select(getString(R.string.text_all_country_code));
        spotSelectAdapter.setEnable(true);
        /*现货的监听*/
        spotSelectAdapter.setOnItemClick((position, data) -> {
            spotSelectAdapter.select(data);
            switch (position) {
                case 0:
                    type = AppConfig.SPOT_ALL;
                    zone_type = AppConfig.VIEW_SPOT;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 1:
                    type = AppConfig.SPOT_DEFI_ALL;
                    zone_type = AppConfig.VIEW_SPOT_DEFI;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 2:
                    type = AppConfig.SPOT_POS_ALL;
                    zone_type = AppConfig.VIEW_SPOT_POS;
                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 3:
                    type = AppConfig.SPOT_GRAY_ALL;
                    zone_type = AppConfig.VIEW_SPOT_GRAY;
                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 4:
                    type = AppConfig.SPOT_BSC_ALL;
                    zone_type = AppConfig.VIEW_SPOT_BSC;
                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;

            }
        });

        text_add.setOnClickListener(v -> {
            if (zone_type == AppConfig.VIEW_OPTIONAL_CONTRACT) {
                tabLayout_market.getTabAt(1).select();
            } else if (zone_type == AppConfig.VIEW_OPTIONAL_SPOT) {
                tabLayout_market.getTabAt(2).select();

            }
        });
        findViewById(R.id.img_search).setOnClickListener(this);

        layout_null.setVisibility(View.GONE);

        titleList = new ArrayList<>();
        titleList.add(getString(R.string.text_optional));
        titleList.add(getString(R.string.text_contract));
        titleList.add(getString(R.string.text_spot));

        titlePopList = new ArrayList<>();
        titlePopList.add(getString(R.string.text_contract));
        titlePopList.add(getString(R.string.text_spot));
        for (String market_name : titleList) {
            tabLayout_market.addTab(tabLayout_market.newTab().setText(market_name));
        }
        tabLayout_market.getTabAt(AppConfig.selectPosition).select();

        /*自选 合约 现货监听*/
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

                    text_add.setText(R.string.text_add_optional);
                    text_add.setTextColor(getResources().getColor(R.color.maincolor));
                    text_add.setBackground(getResources().getDrawable(R.drawable.bg_kuang_main_color));
                    optionalSelectAdapter.select(getString(R.string.text_contract));//按到自选自动跳到现货
                    layout_optional_select.setVisibility(View.VISIBLE);
                    layout_contract_select.setVisibility(View.GONE);
                    layout_spot_select.setVisibility(View.GONE);

                    type = AppConfig.OPTIONAL_CONTRACT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_CONTRACT;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));


                } else if (tab.getPosition() == 1) {
                    text_add.setText(R.string.text_no_data);
                    text_add.setTextColor(getResources().getColor(R.color.text_second_color));
                    contractSelectAdapter.select(getString(R.string.text_add_pass_coin));//按到自选自动跳到现货
                    layout_optional_select.setVisibility(View.GONE);
                    layout_contract_select.setVisibility(View.VISIBLE);
                    layout_spot_select.setVisibility(View.GONE);

                    type = AppConfig.CONTRACT_IN_ALL;
                    zone_type = AppConfig.VIEW_CONTRACT_IN;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                } else if (tab.getPosition() == 2) {
                    text_add.setText(R.string.text_no_data);
                    text_add.setTextColor(getResources().getColor(R.color.text_second_color));

                    spotSelectAdapter.select(getString(R.string.text_all_country_code));
                    layout_optional_select.setVisibility(View.GONE);
                    layout_contract_select.setVisibility(View.GONE);
                    layout_spot_select.setVisibility(View.VISIBLE);

                    type = AppConfig.SPOT_ALL;
                    zone_type = AppConfig.VIEW_SPOT;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));


                } else if (tab.getPosition() == 3) {
                    text_add.setText(R.string.text_no_data);

                    layout_optional_select.setVisibility(View.GONE);

                    type = AppConfig.DERIVATIVES_ALL;
                    zone_type = AppConfig.VIEW_DERIVATIVES;

                    quoteList = arrayMap.get(type);
                    if (quoteList != null) {
                        if (quoteList.size() == 0) {
                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        } else {
                            layout_null.setVisibility(View.GONE);
                            recyclerView_market.setVisibility(View.VISIBLE);
                            quoteAdapter_market.setDatas(quoteList);
                        }
                    } else {

                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_market.setVisibility(View.GONE);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
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
                WebSocketManager.getInstance().sendQuotes("3001", quote_code, null);

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
        findViewById(R.id.img_log).setOnClickListener(this);

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
            if (isLogin()) {
                if (dataBean.isFollow()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_EDIT_FOLLOW, dataBean);
                } else {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_CIRCLE_SETTINGS_FOLLOW, dataBean);
                }
            } else {
                LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
            }
        });


        followAdapter.setOnDetailClick(dataBean -> {
            if (isLogin()) {
                FollowDetailActivity.enter(MainFollowActivity.this, AppConfig.FOLLOW, dataBean);
            } else {
                LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
            }
        });



        /*我的 分割线-----------------------------------------------------------------------------*/
        findViewById(R.id.layout_hold).setOnClickListener(this);//我的持仓
        findViewById(R.id.layout_one).setOnClickListener(this);//安全中心监听
        findViewById(R.id.layout_two).setOnClickListener(this);
        findViewById(R.id.layout_three).setOnClickListener(this);
        findViewById(R.id.layout_four).setOnClickListener(this);
        findViewById(R.id.layout_spot_record).setOnClickListener(this);
        //带单管理
        findViewById(R.id.layout_copied_manger).setOnClickListener(this);
        findViewById(R.id.layout_copy_manger).setOnClickListener(this);


        findViewById(R.id.layout_five).setOnClickListener(this);
        findViewById(R.id.layout_six).setOnClickListener(this);
        findViewById(R.id.layout_seven).setOnClickListener(this);
        findViewById(R.id.layout_help).setOnClickListener(this);//系统设置

        findViewById(R.id.layout_eight).setOnClickListener(this);//系统设置
        findViewById(R.id.layout_nine).setOnClickListener(this);
        findViewById(R.id.layout_login).setOnClickListener(this);
        text_register.findViewById(R.id.text_register).setOnClickListener(this);
       /* findViewById(R.id.text_account).setOnClickListener(this);
        findViewById(R.id.text_deposit).setOnClickListener(this);
        findViewById(R.id.text_withdrawal).setOnClickListener(this);
        findViewById(R.id.text_quick_exchange).setOnClickListener(this);
        findViewById(R.id.text_fiat).setOnClickListener(this);*/
        findViewById(R.id.img_edit).setOnClickListener(this);
        findViewById(R.id.text_mining).setOnClickListener(this);

        myTitleList = new ArrayList<>();
        myTitleList.add(getString(R.string.text_assets));
        myTitleList.add(getString(R.string.text_account_my));
        for (String market_name : myTitleList) {
            tabLayout_my.addTab(tabLayout_my.newTab().setText(market_name));
        }
        tabLayout_my.getTabAt(1).select();


        tabLayout_my.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tab.getPosition() == 0) {
                    layout_assets_my.setVisibility(View.VISIBLE);
                    layout_account_my.setVisibility(View.GONE);
                    img_record.setVisibility(View.VISIBLE);

                } else if (tab.getPosition() == 1) {
                    layout_assets_my.setVisibility(View.GONE);
                    layout_account_my.setVisibility(View.VISIBLE);
                    img_record.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //我的 资产页面
        img_record.setVisibility(View.GONE);
        img_record.setOnClickListener(this);

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_account_head, null);
        text_balance = headView.findViewById(R.id.text_balance);
        text_balance_currency = headView.findViewById(R.id.text_balance_currency);
        text_fiat = headView.findViewById(R.id.text_fiat);
        img_eye_switch = headView.findViewById(R.id.img_eye_switch);
        text_currency = headView.findViewById(R.id.text_currency);
        img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));


        text_bonus_balance = headView.findViewById(R.id.text_bonus_balance);
        text_bonus_balance_currency = headView.findViewById(R.id.text_bonus_balance_currency);
        text_bonus_currency = headView.findViewById(R.id.text_bonus_currency);

        text_gift_balance = headView.findViewById(R.id.text_gift_balance);
        text_gift_balance_currency = headView.findViewById(R.id.text_gift_balance_currency);
        text_gift_currency = headView.findViewById(R.id.text_gift_currency);

        img_eye_switch.setOnClickListener(this);
        headView.findViewById(R.id.text_deposit).setOnClickListener(this);
        headView.findViewById(R.id.text_withdrawal).setOnClickListener(this);
        headView.findViewById(R.id.text_quick_exchange).setOnClickListener(this);
        headView.findViewById(R.id.text_fiat).setOnClickListener(this);
        accountAdapter = new AccountAdapter(this);
        recyclerView_assets.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_assets.addHeaderView(headView);
        recyclerView_assets.setAdapter(accountAdapter);
        Util.colorSwipe(this, swipeRefreshLayout_assets);
        CheckBox checkBox_hide = headView.findViewById(R.id.checkbox_hide);
        checkBox_hide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                accountAdapter.hideSmallCoin(true);
            } else {
                accountAdapter.hideSmallCoin(false);

            }
        });

        swipeRefreshLayout_assets.setOnRefreshListener(() -> {
            BalanceManger.getInstance().getBalance("USDT");
            swipeRefreshLayout_assets.setRefreshing(false);
        });

        headView.findViewById(R.id.stay_bonus).setOnClickListener(this);
        headView.findViewById(R.id.stay_gift).setOnClickListener(this);
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
                String key = data.get(position).getKey();
                switch (key) {
                    case IntentConfig.Keys.KEY_KOL:
                        break;
                    case IntentConfig.Keys.KEY_TRADE_LIVE:
                        TradeTabActivity.enter(this, "1", quoteList.get(0));
                        break;
                    case IntentConfig.Keys.KEY_MINING:
                        if (isLogin()) {
                            WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/mining"), getString(R.string.text_mining_title));
                        } else {
                            LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                        }
                        break;
                    case IntentConfig.Keys.KEY_REGISTER:
                        LoginActivity.enter(this, IntentConfig.Keys.KEY_REGISTER);

                        break;
                    case IntentConfig.Keys.KEY_LOGIN:
                        LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                        break;
                }

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
            //带单总收益
        }

        getFollowList();
    }

    private void getFollowIncome() {
        NetManger.getInstance().followerIncome((state, response) -> {
            if (state.equals(SUCCESS)) {
                FollowerIncomeEntity followerIncomeEntity = (FollowerIncomeEntity) response;
                text_all_profit.setText(getResources().getString(R.string.text_all_profit)
                        + ": " + TradeUtil.getNumberFormat(followerIncomeEntity.getIncomeAll(), 2));

            }
        });
    }


    public void getFollowList() {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("print", "onDestroy:912:  " + "执行了Ondestory");
        SocketQuoteManger.getInstance().clear();
        //QuoteCustomizeListManger.getInstance().cancelTimer();
        //QuoteCustomizeListManger.getInstance().clear();
        SPUtils.remove(AppConfig.RATE_LIST);
        UserDetailManger.getInstance().clear();
        NoticeManger.getInstance().clear();
        cancelTimer();
        //  UserDetailManger.getInstance().cancelTimer();


    }


    private QuoteAdapter quoteAdapter_market_pop, quoteAdapter_history;
    // 声明平移动画
    private TranslateAnimation animation;
    private EditText edit_search;


    private MarketSearchHotAdapter marketSearchHotAdapter;
    private List<String> hotSearchList;
    private Set<String> historyList;


    /*显示行情选择的按钮*/
    private void showQuotePopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.quote_market_search, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        /*热门搜索*/
        RecyclerView recyclerView_hot_search = view.findViewById(R.id.recyclerView_hot_search);
        LinearLayout layout_search_hot = view.findViewById(R.id.layout_search_hot);

        LinearLayout layout_quote_content = view.findViewById(R.id.layout_quote_content);
        layout_history_content = view.findViewById(R.id.layout_history_content);

        marketSearchHotAdapter = new MarketSearchHotAdapter(this);
        recyclerView_hot_search.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        recyclerView_hot_search.setAdapter(marketSearchHotAdapter);
        hotSearchList = new ArrayList<>();
        hotSearchList.add("DOT/USDT,DOTUSDT1808");
        hotSearchList.add("ATOM/USDT,ATOMUSDT1808");
        hotSearchList.add("UNI/USDT,UNIUSDT1808");
        hotSearchList.add("FIL/USDT,FILUSDT1808");
        hotSearchList.add("ETH/USDT,ETHUSDT1808");
        hotSearchList.add("LINK/USDT,LINKUSDT1808");
        marketSearchHotAdapter.setDatas(hotSearchList);

        marketSearchHotAdapter.setOnItemClick((position, data) -> {
            List<String> list = arrayMap.get(AppConfig.CONTRACT_IN_ALL);
            String[] split1 = data.split(",");
            for (int i = 0; i < list.size(); i++) {
                String[] split = list.get(i).split(",");
                if (split[0].equals(split1[1])) {
                    TradeTabActivity.enter(MainFollowActivity.this, "1", list.get(i));
                }
            }
            historyList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_HISTORY, null));
            if (historyList.size() != 0) {
                Util.isOptional(split1[1], historyList, response -> {
                    boolean isOptional = (boolean) response;
                    if (!isOptional) {
                        historyList.add(itemQuoteContCode(split1[1]));
                    }
                });
            } else {
                historyList.add(itemQuoteContCode(split1[1]));
            }
            SPUtils.putString(AppConfig.KEY_HISTORY, Util.SPDeal(historyList));

        });

        TabLayout tabLayout_market_search = view.findViewById(R.id.tabLayout_market_search);

        RecyclerView recyclerView_market_pop = view.findViewById(R.id.recyclerView_market_pop);

        quoteAdapter_market_pop = new QuoteAdapter(this);
        recyclerView_market_pop.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market_pop.setAdapter(quoteAdapter_market_pop);


        quoteAdapter_market_pop.setDatas(quoteList);
        quoteAdapter_market_pop.isShowIcon(false);
        quoteAdapter_market_pop.isShowVolume(true);

        /*历史记录*/
        RecyclerView recyclerView_history_search = view.findViewById(R.id.recyclerView_history_search);

        quoteAdapter_history = new QuoteAdapter(this);
        recyclerView_history_search.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_history_search.setAdapter(quoteAdapter_history);

        Set<String> optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
        Log.d("print", "showQuotePopWindow:1398: " + historyList);
        quoteAdapter_history.isShowIcon(false);
        quoteAdapter_history.isShowVolume(false);
        quoteAdapter_history.isShowStar(optionalList, true);


        if (arrayMap != null) {
            List<String> historyQuoteList = arrayMap.get(AppConfig.HISTORY_ALL);
            if (historyQuoteList != null) {
                quoteAdapter_history.setDatas(historyQuoteList);
            } else {
                layout_history_content.setVisibility(View.GONE);
            }
        }


        quoteAdapter_history.setOnItemClick(data -> TradeTabActivity.enter(MainFollowActivity.this, "1", data));


        view.findViewById(R.id.img_clear_history).setOnClickListener(v -> {
            SPUtils.remove(AppConfig.KEY_HISTORY);
            layout_history_content.setVisibility(View.GONE);
        });

        ImageView img_price_triangle = view.findViewById(R.id.img_price_triangle);

        ImageView img_rate_triangle = view.findViewById(R.id.img_rate_triangle);

        ImageView img_name_triangle = view.findViewById(R.id.img_name_triangle);


        for (String market_name : titlePopList) {
            tabLayout_market_search.addTab(tabLayout_market_search.newTab().setText(market_name));
        }
        tabLayout_market_search.getTabAt(0).select();

        view.findViewById(R.id.layout_new_price).setOnClickListener(v -> {
            if (arrayMap == null) {
                return;
            }
            if (flag_new_price) {
                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                flag_new_price = false;
                Util.priceTypeHigh2Low(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);
            } else {

                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_new_price = true;
                Util.priceTypeLow2High(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);

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
                Util.rateTypeHigh2Low(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);

            } else {
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_up_down = true;
                Util.rateTypeLow2High(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);

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
                Util.nameTypeA2Z(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);

            } else {
                img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                flag_name = true;
                Util.nameTypeZ2A(zone_type, response -> type = (String) response);
                List<String> quoteList = arrayMap.get(type);
                quoteAdapter_market_pop.setDatas(quoteList);

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
                    zone_type = AppConfig.VIEW_POP_CONTRACT;
                    if (edit_search.getText().toString().equals("")) {
                        type = AppConfig.CONTRACT_ALL;
                        quoteList = arrayMap.get(type);
                        Log.d("print", "onTabSelected:684:  " + quoteList + "  " + type);
                        if (quoteList != null) {
                            if (quoteList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                recyclerView_market.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                recyclerView_market.setVisibility(View.VISIBLE);
                                quoteAdapter_market.setDatas(quoteList);
                            }
                        } else {

                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        }
                        img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                        img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                        img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    } else {
                        if (zone_type.equals(AppConfig.VIEW_POP_CONTRACT)) {
                            type = AppConfig.CONTRACT_ALL;
                        } else {
                            type = AppConfig.SPOT_ALL;
                        }
                        List<String> strings = arrayMap.get(type);
                        List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                        quoteAdapter_market_pop.setDatas(searchQuoteList);
                    }


                } else if (tab.getPosition() == 1) {


                    zone_type = AppConfig.VIEW_POP_SPOT;
                    if (edit_search.getText().toString().equals("")) {
                        type = AppConfig.SPOT_ALL;

                        quoteList = arrayMap.get(type);
                        if (quoteList != null) {
                            if (quoteList.size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                recyclerView_market.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                recyclerView_market.setVisibility(View.VISIBLE);
                                quoteAdapter_market.setDatas(quoteList);
                            }
                        } else {

                            layout_null.setVisibility(View.VISIBLE);
                            recyclerView_market.setVisibility(View.GONE);
                        }
                        img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                        img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                        img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    } else {
                        if (zone_type.equals(AppConfig.VIEW_POP_CONTRACT)) {
                            type = AppConfig.CONTRACT_ALL;
                        } else {
                            type = AppConfig.SPOT_ALL;
                        }
                        List<String> strings = arrayMap.get(type);
                        Log.d("search", "onTabSelected:1781:  " + strings);
                        List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                        quoteAdapter_market_pop.setDatas(searchQuoteList);
                    }
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
            // popupWindow.dismiss();
            type = AppConfig.CONTRACT_IN_ALL;
            zone_type = AppConfig.VIEW_CONTRACT_IN;
            TradeTabActivity.enter(this, "1", data);

            historyList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_HISTORY, null));


            if (historyList.size() != 0) {
                Util.isOptional(itemQuoteContCode(data), historyList, response -> {
                    boolean isOptional = (boolean) response;
                    if (!isOptional) {
                        historyList.add(itemQuoteContCode(data));
                    }
                });
            } else {
                historyList.add(itemQuoteContCode(data));
            }
            SPUtils.putString(AppConfig.KEY_HISTORY, Util.SPDeal(historyList));


        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            type = AppConfig.CONTRACT_IN_ALL;
            zone_type = AppConfig.VIEW_CONTRACT_IN;
            tabLayout_market.setVisibility(View.VISIBLE);
            tabLayout_market.getTabAt(AppConfig.selectPosition).select();
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
                    layout_quote_content.setVisibility(View.VISIBLE);
                    layout_history_content.setVisibility(View.GONE);

                    layout_bar.setVisibility(View.GONE);
                    layout_search_hot.setVisibility(View.GONE);
                    if (zone_type.equals(AppConfig.VIEW_POP_CONTRACT)) {
                        type = AppConfig.CONTRACT_ALL;
                    } else {
                        type = AppConfig.SPOT_ALL;
                    }
                    List<String> strings = arrayMap.get(type);
                    List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                    quoteAdapter_market_pop.setDatas(searchQuoteList);
                } else {
                    layout_quote_content.setVisibility(View.GONE);
                    layout_history_content.setVisibility(View.VISIBLE);

                    layout_bar.setVisibility(View.VISIBLE);
                    layout_search_hot.setVisibility(View.VISIBLE);
                    type = AppConfig.CONTRACT_ALL;
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
                TradeTabActivity.enter(MainFollowActivity.this, "1", quoteList.get(0));

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
                //getFollowList();

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
                UserActivity.enter(this, IntentConfig.Keys.KEY_ANNOUNCEMENT);
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
                TradeTabActivity.enter(MainFollowActivity.this, "2", quoteList.get(0));
                break;

            /*行情 -----------------------------------------------------------------------------------*/
            case R.id.img_search:
                Util.lightOff(this);
                type = AppConfig.CONTRACT_ALL;
                zone_type = AppConfig.VIEW_POP_CONTRACT;
                showQuotePopWindow();
                break;
            case R.id.layout_new_price:
                if (arrayMap == null) {
                    return;
                }

                if (flag_new_price) {
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_new_price = false;
                    Util.priceTypeHigh2Low(zone_type, response -> type = (String) response);
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);


                } else {

                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_new_price = true;
                    Util.priceTypeLow2High(zone_type, response -> type = (String) response);
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
                    Util.rateTypeHigh2Low(zone_type, response -> type = (String) response);
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                } else {
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_up_down = true;

                    Util.rateTypeLow2High(zone_type, response -> type = (String) response);
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

                    Util.nameTypeA2Z(zone_type, response -> type = (String) response);
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                } else {
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_name = true;

                    Util.nameTypeZ2A(zone_type, response -> type = (String) response);
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

                }
                img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            //
            case R.id.img_log:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FOLLOW_LOG);

                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            //跟单管理
            case R.id.text_follow_settings:
            case R.id.layout_copy_manger:
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
            case R.id.stay_bonus:
                String prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);
                if (prizeTrade != null) {
                    prizeTradeValue = TradeUtil.mul(Double.parseDouble(prizeTrade), 100) + "%";
                } else {
                    prizeTradeValue = null;
                }
                String format = String.format(getString(R.string.text_used_to_deduct), prizeTradeValue);

                Util.lightOff(this);
                PopUtil.getInstance().showTip(this, layout_view, false, format,
                        state -> {

                        });
                break;
            case R.id.stay_gift:
                Util.lightOff(this);
                PopUtil.getInstance().showTip(this, layout_view, false, getString(R.string.text_trading_fees),
                        state -> {

                        });
                break;

            case R.id.text_register:
                RegisterActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_REGISTER);
                break;

            //帮助中心
            case R.id.layout_help:
                WebActivity.getInstance().openUrl(this, NetManger.H5_HELP_CENTER, getString(R.string.text_help_center));


                break;

            case R.id.layout_eight:
                UserActivity.enter(this, IntentConfig.Keys.KEY_SET_UP);

                break;
            case R.id.img_eye_switch:

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
            case R.id.layout_hold:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_HOLD);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
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
            case R.id.img_record:
            case R.id.layout_two:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.layout_spot_record:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_SPOT_RECORD);

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

            /*带单管理*/

            case R.id.layout_copied_manger:

                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_FOLLOWER_MANGER);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*跟单管理*/

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
           /* case R.id.text_account:
                if (isLogin()) {
                    UserActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_ACCOUNT);
                } else {
                    LoginActivity.enter(MainFollowActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;*/
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