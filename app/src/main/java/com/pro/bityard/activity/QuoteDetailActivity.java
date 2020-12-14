package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.adapter.RadioGroupAdapter;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.chart.KData;
import com.pro.bityard.chart.NoVolumeView;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.AddScoreEntity;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.ChargeUnitEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.QuoteChartEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.Quote15MinCurrentManger;
import com.pro.bityard.manger.Quote15MinHistoryManger;
import com.pro.bityard.manger.Quote1MinHistoryManger;
import com.pro.bityard.manger.Quote3MinCurrentManger;
import com.pro.bityard.manger.Quote3MinHistoryManger;
import com.pro.bityard.manger.Quote5MinCurrentManger;
import com.pro.bityard.manger.Quote5MinHistoryManger;
import com.pro.bityard.manger.Quote60MinCurrentManger;
import com.pro.bityard.manger.Quote60MinHistoryManger;
import com.pro.bityard.manger.QuoteCurrentManger;
import com.pro.bityard.manger.QuoteDayCurrentManger;
import com.pro.bityard.manger.QuoteDayHistoryManger;
import com.pro.bityard.manger.QuoteMonthCurrentManger;
import com.pro.bityard.manger.QuoteMonthHistoryManger;
import com.pro.bityard.manger.QuoteWeekCurrentManger;
import com.pro.bityard.manger.QuoteWeekHistoryManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.manger.TradeListManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.listQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.listQuoteName;
import static com.pro.bityard.utils.TradeUtil.listQuotePrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteUSD;
import static com.pro.bityard.utils.TradeUtil.marginMax;
import static com.pro.bityard.utils.TradeUtil.marginMin;
import static com.pro.bityard.utils.TradeUtil.marginOrder;
import static java.lang.Double.parseDouble;

public class QuoteDetailActivity extends BaseActivity implements View.OnClickListener, Observer, RadioGroup.OnCheckedChangeListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "all";
    private int lever;

    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.layout_bar)
    RelativeLayout layout_bar;
    private String orderType = "0"; //市价=0 限价=1
    private boolean isDefer; //是否递延

    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;

    @BindView(R.id.text_name)
    TextView text_name;

    @BindView(R.id.text_usdt)
    TextView text_name_usdt;

    @BindView(R.id.text_switch)
    TextView text_switch;


    @BindView(R.id.text_lastPrice)
    TextView text_lastPrice;

    @BindView(R.id.img_up_down)
    ImageView img_up_down;

    @BindView(R.id.text_change)
    TextView text_change;

    @BindView(R.id.text_range)
    TextView text_range;

    @BindView(R.id.text_max)
    TextView text_max;
    @BindView(R.id.text_min)
    TextView text_min;
    @BindView(R.id.text_volume)
    TextView text_volume;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radio_0)
    RadioButton radio_btn0;
    @BindView(R.id.radio_1)
    RadioButton radio_btn1;

    @BindView(R.id.text_buy_much)
    TextView text_buy_much;
    @BindView(R.id.text_buy_empty)
    TextView text_buy_empty;

    @BindView(R.id.layout_market_price)
    LinearLayout layout_market_price;
    @BindView(R.id.layout_limit_price)
    LinearLayout layout_limit_price;

    @BindView(R.id.text_market_price)
    TextView text_market_price;
    @BindView(R.id.text_market_balance)
    TextView text_market_balance;
    @BindView(R.id.text_limit_balance)
    TextView text_limit_balance;

    @BindView(R.id.edit_market_margin)
    DecimalEditText edit_market_margin;

    @BindView(R.id.edit_limit_margin)
    DecimalEditText edit_limit_margin;


  /*  @BindView(R.id.recyclerView_market)
    RecyclerView recyclerView_market;

    @BindView(R.id.recyclerView_limit)
    RecyclerView recyclerView_limit;*/


    @BindView(R.id.text_market_volume)
    TextView text_market_volume;

    @BindView(R.id.text_limit_volume)
    TextView text_limit_volume;

    @BindView(R.id.edit_limit_price)
    DecimalEditText edit_limit_price;
    @BindView(R.id.text_market_all)
    TextView text_market_all;

    @BindView(R.id.text_limit_all)
    TextView text_limit_all;


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.kline_1min_time)
    NoVolumeView kline_1min_time;
    @BindView(R.id.kline_1min)
    NoVolumeView myKLineView_1Min;
    @BindView(R.id.kline_5min)
    NoVolumeView myKLineView_5Min;
    @BindView(R.id.kline_15min)
    NoVolumeView myKLineView_15Min;
    @BindView(R.id.kline_3min)
    NoVolumeView myKLineView_3Min;
    @BindView(R.id.kline_1h)
    NoVolumeView myKLineView_1H;
    @BindView(R.id.kline_1d)
    NoVolumeView myKLineView_1D;
    @BindView(R.id.kline_1week)
    NoVolumeView myKLineView_1_week;
    @BindView(R.id.kline_1month)
    NoVolumeView myKLineView_1_month;


    private RadioGroupAdapter radioGroupAdapter;//杠杆适配器
    private RadioRateAdapter radioRateProfitAdapter, radioRateLossAdapter;

    private List<String> quoteList;

    private QuotePopAdapter quotePopAdapter;
    //private String quote;
    private List<TradeListEntity> tradeListEntityList;
    private ChargeUnitEntity chargeUnitEntity;


    @BindView(R.id.layout_more)
    LinearLayout layout_more;
    @BindView(R.id.text_one_hour)
    TextView text_one_hour;
    @BindView(R.id.text_one_day)
    TextView text_one_day;
    @BindView(R.id.text_one_week)
    TextView text_one_week;
    @BindView(R.id.text_one_month)
    TextView text_one_month;

    @BindView(R.id.layout_trade)
    LinearLayout layout_trade;

    @BindView(R.id.stay_view)
    View stay_view;
    private List<String> titles = new ArrayList<>();
    private List<KData> kData1MinHistory, kData5MinHistory, kData15MinHistory, kData3MinHistory, kData60MinHistory, kDataDayHistory, kDataWeekHistory, kDataMonthHistory;


    @BindView(R.id.layout_lever_market)
    LinearLayout layout_lever_market;
    @BindView(R.id.layout_market)
    LinearLayout layout_market;
    @BindView(R.id.text_lever_market)
    TextView text_lever_market;

    @BindView(R.id.layout_lever_limit)
    LinearLayout layout_lever_limit;
    @BindView(R.id.layout_limit)
    LinearLayout layout_limit;
    @BindView(R.id.text_lever_limit)
    TextView text_lever_limit;

    @BindView(R.id.layout_pop_market)
    LinearLayout layout_pop_market;

    private TradeListEntity tradeListEntity;
    private TextView text_deduction_amount_pop;
    private String prizeTrade = null;

    @BindView(R.id.text_market_currency)
    TextView text_market_currency;

    @BindView(R.id.text_limit_currency)
    TextView text_limit_currency;
    @BindView(R.id.img_star)
    ImageView img_star;
    private boolean flag_optional = false;
    //当前行情号
    private String quote_code = null;
    private QuoteMinEntity quoteMinEntity;


    // 声明平移动画
    private TranslateAnimation animation;
    private EditText edit_search;
    private JSONObject chargeUnitEntityJson;

    public static void enter(Context context, String tradeType, String data) {
        Intent intent = new Intent(context, QuoteDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, data);
        bundle.putString(TYPE, tradeType);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BalanceManger.getInstance().getBalance("USDT");

        if (isLogin()) {
            layout_trade.setVisibility(View.VISIBLE);
            stay_view.setVisibility(View.VISIBLE);

        } else {
            layout_trade.setVisibility(View.GONE);
            stay_view.setVisibility(View.GONE);

        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_quote_detail;
    }

    @Override
    protected void initPresenter() {

    }


    private void setTabStyle() {
        for (int i = 0; i < titles.size(); i++) {//根据Tab数量循环来设置
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(this).inflate(R.layout.tab_title_layout, null);
                TextView text_title_one = view.findViewById(R.id.text_title);
                text_title_one.setText(titles.get(i));
                ImageView img_subscript = view.findViewById(R.id.img_subscript);

                if (i == 0) {
                    text_title_one.setTextColor(getResources().getColor(R.color.maincolor));//设置一下文字颜色
                } else {
                    text_title_one.setTextColor(getResources().getColor(R.color.text_second_color));//设置一下文字颜色
                }

                if (i == 5) {
                    img_subscript.setVisibility(View.VISIBLE);
                } else {
                    img_subscript.setVisibility(View.GONE);
                }
                tab.setCustomView(view);//最后添加view到Tab上面
            }
        }
    }


    @Override
    protected void initView(View view) {
        initTabView();
        /*Handler handler = new Handler();
        handler.postDelayed(() -> initTabView(), 100);*/


    }

    private void initTabView() {
        titles.add("Line");
        titles.add("1min");
        titles.add("3min");
        titles.add("5min");
        titles.add("15min");
        titles.add(getResources().getString(R.string.text_more));


        SocketQuoteManger.getInstance().addObserver(this);
        BalanceManger.getInstance().addObserver(this);
        //QuoteItemManger.getInstance().addObserver(this);

        QuoteCurrentManger.getInstance().addObserver(this);//1min 实时
        Quote5MinCurrentManger.getInstance().addObserver(this);//5min 实时
        Quote15MinCurrentManger.getInstance().addObserver(this);//15min 实时
        Quote3MinCurrentManger.getInstance().addObserver(this);//30min 实时
        Quote60MinCurrentManger.getInstance().addObserver(this);//60min 实时
        QuoteDayCurrentManger.getInstance().addObserver(this);//1day 实时
        QuoteWeekCurrentManger.getInstance().addObserver(this);
        QuoteMonthCurrentManger.getInstance().addObserver(this);


        Quote1MinHistoryManger.getInstance().addObserver(this);
        Quote5MinHistoryManger.getInstance().addObserver(this);
        Quote15MinHistoryManger.getInstance().addObserver(this);
        Quote3MinHistoryManger.getInstance().addObserver(this);
        Quote60MinHistoryManger.getInstance().addObserver(this);
        QuoteDayHistoryManger.getInstance().addObserver(this);
        QuoteWeekHistoryManger.getInstance().addObserver(this);
        QuoteMonthHistoryManger.getInstance().addObserver(this);


        TradeListManger.getInstance().addObserver(this);

        ChargeUnitManger.getInstance().addObserver(this);
        TagManger.getInstance().addObserver(this);


        findViewById(R.id.img_back).setOnClickListener(this);

        findViewById(R.id.img_setting).setOnClickListener(this);
        findViewById(R.id.layout_product).setOnClickListener(this);
        //加币 持仓
        findViewById(R.id.text_position).setOnClickListener(this);
        findViewById(R.id.text_charge).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);

        findViewById(R.id.layout_much).setOnClickListener(this);
        findViewById(R.id.layout_empty).setOnClickListener(this);
        findViewById(R.id.layout_switch).setOnClickListener(this);


        //更多的监听
        findViewById(R.id.text_one_hour).setOnClickListener(this);
        findViewById(R.id.text_one_day).setOnClickListener(this);
        findViewById(R.id.text_one_week).setOnClickListener(this);
        findViewById(R.id.text_one_month).setOnClickListener(this);
        findViewById(R.id.text_rule).setOnClickListener(this);
        //自选监听
        findViewById(R.id.layout_optional).setOnClickListener(this);


        radioGroupAdapter = new RadioGroupAdapter(this);
        /*recyclerView_market.setAdapter(radioGroupAdapter);
        recyclerView_limit.setAdapter(radioGroupAdapter);*/


        text_market_all.setOnClickListener(this);
        text_limit_all.setOnClickListener(this);

        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.getTabAt(i).setText(titles.get(i));
        }
        setTabStyle();

        kline_1min_time.setShowInstant(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text_title)).setTextColor(getResources().getColor(R.color.maincolor));//设置一下文字颜色
                tab.setCustomView(view);


                TabLayout.Tab tabAt = tabLayout.getTabAt(5);
                View view2 = tabAt.getCustomView();
                ((TextView) view2.findViewById(R.id.text_title)).setText(titles.get(5));//设置一下文字
                tabAt.setCustomView(view2);

                switch (tab.getPosition()) {
                    case 0:

                        Quote1MinHistoryManger.getInstance().quote(quote_code, -1);

                        kline_1min_time.setVisibility(View.VISIBLE);
                        myKLineView_1Min.setVisibility(View.GONE);
                        myKLineView_3Min.setVisibility(View.GONE);
                        myKLineView_5Min.setVisibility(View.GONE);
                        myKLineView_15Min.setVisibility(View.GONE);
                        myKLineView_1H.setVisibility(View.GONE);
                        myKLineView_1D.setVisibility(View.GONE);
                        layout_more.setVisibility(View.GONE);
                        myKLineView_1_week.setVisibility(View.GONE);
                        myKLineView_1_month.setVisibility(View.GONE);


                        break;
                    case 1:
                        Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
                        Quote3MinHistoryManger.getInstance().quote(quote_code, -2);

                        kline_1min_time.setVisibility(View.GONE);
                        myKLineView_1Min.setVisibility(View.VISIBLE);
                        myKLineView_3Min.setVisibility(View.GONE);
                        myKLineView_5Min.setVisibility(View.GONE);
                        myKLineView_15Min.setVisibility(View.GONE);
                        myKLineView_1H.setVisibility(View.GONE);
                        myKLineView_1D.setVisibility(View.GONE);
                        layout_more.setVisibility(View.GONE);
                        myKLineView_1_week.setVisibility(View.GONE);
                        myKLineView_1_month.setVisibility(View.GONE);
                        break;
                    case 2:
                        Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);

                        Quote5MinHistoryManger.getInstance().quote(quote_code, -2);


                        kline_1min_time.setVisibility(View.GONE);
                        myKLineView_1Min.setVisibility(View.GONE);
                        myKLineView_3Min.setVisibility(View.VISIBLE);
                        myKLineView_5Min.setVisibility(View.GONE);
                        myKLineView_15Min.setVisibility(View.GONE);
                        myKLineView_1H.setVisibility(View.GONE);
                        myKLineView_1D.setVisibility(View.GONE);
                        layout_more.setVisibility(View.GONE);
                        myKLineView_1_week.setVisibility(View.GONE);
                        myKLineView_1_month.setVisibility(View.GONE);
                        break;
                    case 3:
                        Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);

                        Quote15MinHistoryManger.getInstance().quote(quote_code, -2);

                        layout_more.setVisibility(View.GONE);
                        kline_1min_time.setVisibility(View.GONE);
                        myKLineView_1Min.setVisibility(View.GONE);
                        myKLineView_3Min.setVisibility(View.GONE);
                        myKLineView_5Min.setVisibility(View.VISIBLE);
                        myKLineView_15Min.setVisibility(View.GONE);
                        myKLineView_1H.setVisibility(View.GONE);
                        myKLineView_1D.setVisibility(View.GONE);
                        myKLineView_1_week.setVisibility(View.GONE);
                        myKLineView_1_month.setVisibility(View.GONE);
                        break;
                    case 4:
                        layout_more.setVisibility(View.GONE);
                        kline_1min_time.setVisibility(View.GONE);
                        myKLineView_1Min.setVisibility(View.GONE);
                        myKLineView_5Min.setVisibility(View.GONE);
                        myKLineView_15Min.setVisibility(View.VISIBLE);
                        myKLineView_3Min.setVisibility(View.GONE);
                        myKLineView_1H.setVisibility(View.GONE);
                        myKLineView_1D.setVisibility(View.GONE);
                        myKLineView_1_week.setVisibility(View.GONE);
                        myKLineView_1_month.setVisibility(View.GONE);

                        break;

                    case 5:
                        layout_more.setVisibility(View.VISIBLE);
                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text_title)).setTextColor(getResources().getColor(R.color.text_second_color));//设置一下文字颜色
                tab.setCustomView(view);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 5) {
                    if (layout_more.isShown()) {
                        layout_more.setVisibility(View.GONE);
                    } else {
                        layout_more.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //杠杆选择
        findViewById(R.id.layout_market_lever_select).setOnClickListener(this);
        findViewById(R.id.layout_limit_lever_select).setOnClickListener(this);
    }


    private Set<String> setList;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        Log.d("print", "initData:590:  " + itemData);
        if (tradeType.equals("1")) {
            text_switch.setText(getResources().getText(R.string.text_real_trade));

        } else if (tradeType.equals("2")) {
            text_switch.setText(getResources().getText(R.string.text_simulation_trade));

        }

        //礼金抵扣比例
        prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);

        if (itemData.equals("")) {
            return;
        }

        quote_code = itemQuoteContCode(itemData);
        Log.d("print", "initData:进来的值:  " + itemQuoteContCode(itemData));
        //自选的图标
        String optional = SPUtils.getString(AppConfig.KEY_OPTIONAL, null);
        Log.d("print", "onClick:569:  " + optional);
        setList = new HashSet<>();
        if (optional != null) {
            String[] split = optional.split(",");
            for (String a : split) {
                setList.add(a);
            }
            if (setList.contains(itemQuoteContCode(itemData))) {
                img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
            } else {
                img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
            }
        }


        text_name.setText(TradeUtil.name(itemData));
        text_name_usdt.setText(TradeUtil.currency(itemData));

        text_market_currency.setText(TradeUtil.currency(itemData));
        text_limit_currency.setText(TradeUtil.currency(itemData));
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Quote1MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -1);
            /*Quote3MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            Quote5MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            Quote15MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            Quote60MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            QuoteDayHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            QuoteWeekHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
            QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);*/
            //开启单个刷新
            //  QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            //开启单个行情图
            // Quote1MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            startScheduleJob(mHandler, QUOTE_SECOND, QUOTE_SECOND);
           /* Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));*/

            //获取输入框的范围保证金
           /* TradeListManger.getInstance().tradeList((state, response) -> {
                if (state.equals(SUCCESS)) {
                    tradeListEntityList = (List<TradeListEntity>) response;
                    tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
                    setContent(tradeListEntity);
                }
            });*/

            String string = SPUtils.getString(AppConfig.QUOTE_DETAIL, null);
            tradeListEntityList = Util.SPDealEntityResult(string);
            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            setContent(tradeListEntity);

            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    chargeUnitEntityJson = (JSONObject) response;
                    TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                    Log.d("print", "initData:673:  " + chargeUnitEntity);
                }
            });


        }, 500);






        text_lastPrice.setText(listQuotePrice(itemData));
        edit_limit_price.setDecimalEndNumber(TradeUtil.decimalPoint(listQuotePrice(itemData)));//根据不同的小数位限制
        edit_limit_price.setText(listQuotePrice(itemData));
        if (tradeType.equals("1")) {
            text_switch.setText(getResources().getText(R.string.text_real_trade));
        } else {
            text_switch.setText(getResources().getText(R.string.text_simulation_trade));

        }

        text_change.setText(TradeUtil.quoteChange(listQuotePrice(itemData), listQuoteTodayPrice(itemData)));
        text_range.setText(TradeUtil.quoteRange(listQuotePrice(itemData), listQuoteTodayPrice(itemData)));


        switch (listQuoteIsRange(itemData)) {
            case "-1":
                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_down));

                break;
            case "1":
                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_up));

                break;
            case "0":
                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + listQuoteIsRange(itemData));
        }
        //可用余额
        if (tradeType.equals("1")) {
            text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));
            text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));
        } else {
            text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2) + " " + getResources().getString(R.string.text_usdt));
            text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2) + " " + getResources().getString(R.string.text_usdt));
        }

    }


    /*设置 保证金和杠杆*/
    public void setContent(TradeListEntity tradeListEntity) {
     //   Log.d("print", "setContent:659:  " + tradeListEntity);
        if (tradeListEntity != null) {
            List<Integer> leverShowList = tradeListEntity.getLeverShowList();

            lever = leverShowList.get(oldSelect);
            text_lever_market.setText(lever + "X");
            text_lever_limit.setText(lever + "X");
            double maxHoldOne = tradeListEntity.getMaxHoldOne();
            if (maxHoldOne > 0) {
                String min_margin = TradeUtil.depositMin(tradeListEntity.getDepositList());
                double max_margin = TradeUtil.div(maxHoldOne, lever, 0);

                edit_market_margin.setHint(min_margin +
                        "~" + max_margin);
                edit_limit_margin.setHint(min_margin +
                        "~" + max_margin);
                edit_market_margin.setOnFocusChangeListener((v, hasFocus) -> {
                    String value_margin = edit_market_margin.getText().toString();
                    // Log.d("print", "setContent:669:  " + value_margin + "        " + max_margin);

                    if (!hasFocus) {
                        if (value_margin.length() != 0) {
                            if (Double.parseDouble(value_margin) < Double.parseDouble(min_margin)) {
                                edit_market_margin.setText(min_margin);
                            } else if (Double.parseDouble(value_margin) > max_margin) {
                                edit_market_margin.setText(String.valueOf(max_margin));

                            }
                        }
                    }
                });


                edit_limit_margin.setOnFocusChangeListener((v, hasFocus) -> {
                    String value_margin = edit_limit_margin.getText().toString();
                    if (!hasFocus) {
                        if (value_margin.length() != 0) {
                            if (Double.parseDouble(value_margin) < Double.parseDouble(min_margin)) {
                                edit_limit_margin.setText(min_margin);
                            } else if (Double.parseDouble(value_margin) > max_margin) {
                                edit_limit_margin.setText(String.valueOf(max_margin));
                            }
                        }
                    }
                });
            } else {
                edit_market_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));
                edit_limit_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));
                edit_market_margin.setOnFocusChangeListener((v, hasFocus) -> {
                    String value_margin = edit_market_margin.getText().toString();
                    if (!hasFocus) {
                        if (value_margin.length() != 0) {
                            if (Double.parseDouble(value_margin) < Double.parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_market_margin.setText(marginMin(tradeListEntity.getDepositList()));
                            } else if (Double.parseDouble(value_margin) > Double.parseDouble(marginMax(tradeListEntity.getDepositList()))) {
                                edit_market_margin.setText(marginMax(tradeListEntity.getDepositList()));
                            }
                        }
                    }
                });
                edit_limit_margin.setOnFocusChangeListener((v, hasFocus) -> {
                    String value_margin = edit_limit_margin.getText().toString();
                    if (!hasFocus) {
                        if (value_margin.length() != 0) {
                            if (Double.parseDouble(value_margin) < Double.parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.setText(marginMin(tradeListEntity.getDepositList()));
                            } else if (Double.parseDouble(value_margin) > Double.parseDouble(marginMax(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.setText(marginMax(tradeListEntity.getDepositList()));
                            }
                        }
                    }
                });
            }




            /*edit_market_margin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (0 == s.length()) {
                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (parseDouble(s.toString()) > parseDouble(marginMax(tradeListEntity.getDepositList()))) {
                                edit_market_margin.setText(marginMax(tradeListEntity.getDepositList()));
                            } *//*else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_market_margin.postDelayed(() -> edit_market_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }*//*
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edit_limit_margin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (0 == s.length()) {
                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (parseDouble(s.toString()) > parseDouble(marginMax(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.setText(marginMax(tradeListEntity.getDepositList()));
                            }*//* else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.postDelayed(() -> edit_limit_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }*//*
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });*/
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            //发送行情包
            if (quote_code != null) {
             //   Log.d("print", "handleMessage:845:  " + quote_code);
                WebSocketManager.getInstance().send("4001", quote_code);
            }

        }
    };

    /*设置 保证金和杠杆*/
    public void setLeverContent(TradeListEntity tradeListEntity) {
        if (tradeListEntity != null) {
            List<Integer> leverShowList = tradeListEntity.getLeverShowList();
            lever = leverShowList.get(oldSelect);
            text_lever_market.setText(lever + "X");
            text_lever_limit.setText(lever + "X");
            double maxHoldOne = tradeListEntity.getMaxHoldOne();
            edit_limit_margin.setText("");
            edit_market_margin.setText("");
            text_market_volume.setText(getString(R.string.text_default));
            text_limit_volume.setText(getString(R.string.text_default));
            text_market_all.setText(getString(R.string.text_default));
            text_limit_all.setText(getString(R.string.text_default));
            if (maxHoldOne > 0) {
                String min_margin = TradeUtil.depositMin(tradeListEntity.getDepositList());
                double max_margin = TradeUtil.div(maxHoldOne, lever, 0);
                edit_market_margin.setHint(min_margin +
                        "~" + max_margin);
                edit_limit_margin.setHint(min_margin +
                        "~" + max_margin);

            } else {
                edit_market_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));
                edit_limit_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));


            }

        }
    }

    @Override
    protected void initEvent() {

    }


    private List<String> titleList;

    private boolean flag_new_price = false;
    private boolean flag_up_down = false;
    private boolean flag_name = false;
    private String type = "0";
    private int zone_type = 1;//-1是自选 1是主区 0是创新区 2是衍生品
    private ArrayMap<String, List<String>> arrayMap;

    private QuoteAdapter quoteAdapter_market;

    /*显示行情选择的按钮*/
    private void showQuotePopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.quote_market, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        TabLayout tabLayout_market = view.findViewById(R.id.tabLayout_market_search);
        titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.text_optional));
        titleList.add(getResources().getString(R.string.text_main_zone));
        titleList.add(getResources().getString(R.string.text_innovate));
        titleList.add(getString(R.string.text_derived));

        LinearLayout layout_null = view.findViewById(R.id.layout_null);

        RecyclerView recyclerView_market = view.findViewById(R.id.recyclerView_market);

        quoteAdapter_market = new QuoteAdapter(this);
        recyclerView_market.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_market.setAdapter(quoteAdapter_market);

        quoteAdapter_market.setDatas(quoteList);
        quoteAdapter_market.isShowIcon(false);
        ImageView img_price_triangle = view.findViewById(R.id.img_price_triangle);

        ImageView img_rate_triangle = view.findViewById(R.id.img_rate_triangle);

        ImageView img_name_triangle = view.findViewById(R.id.img_name_triangle);


        for (String market_name : titleList) {
            tabLayout_market.addTab(tabLayout_market.newTab().setText(market_name));
        }
        tabLayout_market.getTabAt(1).select();
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

        SwipeRefreshLayout swipeRefreshLayout_market = view.findViewById(R.id.swipeRefreshLayout_market);
        Util.colorSwipe(this, swipeRefreshLayout_market);
        /*刷新监听*/
        swipeRefreshLayout_market.setOnRefreshListener(() -> {
            swipeRefreshLayout_market.setRefreshing(false);
            quoteAdapter_market.setDatas(quoteList);
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

        quoteAdapter_market.setOnItemClick(data -> {
            quote_code = TradeUtil.itemQuoteContCode(data);
            type = "1";

            TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
            Log.d("print", "showQuotePopWindow:1201:  " + itemQuoteCode(quote_code) + "                 " + chargeUnitEntity);
            //自选的图标
            String optional = SPUtils.getString(AppConfig.KEY_OPTIONAL, null);
            setList = new HashSet<>();
            if (optional != null) {
                String[] split = optional.split(",");
                for (String a : split) {
                    setList.add(a);
                }
                if (setList.contains(itemQuoteContCode(data))) {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                } else {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                }
            }


            text_market_currency.setText(TradeUtil.listQuoteName(data));
            text_limit_currency.setText(TradeUtil.listQuoteName(data));

            text_name.setText(TradeUtil.name(itemData));
            if (listQuoteUSD(data) == null) {
                text_name_usdt.setText("");
            } else {
                text_name_usdt.setText(listQuoteUSD(data));
            }
            // QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, itemQuoteContCode(data));

            edit_limit_price.setDecimalEndNumber(TradeUtil.decimalPoint(listQuotePrice(data)));//根据不同的小数位限制
            edit_limit_price.setText(listQuotePrice(data));

            // Quote1MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
            QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);


            Quote1MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote5MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote15MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote3MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote60MinHistoryManger.getInstance().quote(quote_code, -2);
            QuoteDayHistoryManger.getInstance().quote(quote_code, -2);
            QuoteWeekHistoryManger.getInstance().quote(quote_code, -2);
            QuoteMonthHistoryManger.getInstance().quote(quote_code, -2);

            //相应选择
            popupWindow.dismiss();
            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(data), tradeListEntityList);


            setContent(tradeListEntity);


        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
            type = "1";
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
                    quoteAdapter_market.setDatas(searchQuoteList);
                } else {
                    layout_bar.setVisibility(View.VISIBLE);
                    tabLayout_market.setVisibility(View.VISIBLE);
                    type = "1";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter_market.setDatas(quoteList);

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
        TabLayout.Tab tabAt = tabLayout.getTabAt(5);
        View view = tabAt.getCustomView();
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_product:
                Util.lightOff(this);
                showQuotePopWindow();
                //showProductWindow(quoteList);
                break;
            case R.id.layout_switch:

                if (tradeType.equals("1")) {

                    tradeType = "2";
                    text_switch.setText(getResources().getText(R.string.text_simulation_trade));
                    radio_btn1.setVisibility(View.GONE);


                } else if (tradeType.equals("2")) {

                    tradeType = "1";
                    text_switch.setText(getResources().getText(R.string.text_real_trade));
                    radio_btn1.setVisibility(View.VISIBLE);


                }
                radio_btn0.setChecked(true);
                if (radio_btn0.isChecked()) {
                    layout_market_price.setVisibility(View.VISIBLE);
                    layout_limit_price.setVisibility(View.GONE);


                }
                //可用余额
                if (tradeType.equals("1")) {
                    text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
                    text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
                } else {
                    text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
                    text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
                }
                break;
            //自选的监听
            case R.id.layout_optional:

                String optional = SPUtils.getString(AppConfig.KEY_OPTIONAL, null);
                if (quoteMinEntity != null) {
                    if (setList.contains(quoteMinEntity.getSymbol())) {
                        img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                        setList.remove(quoteMinEntity.getSymbol());
                    } else {
                        img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                        setList.add(quoteMinEntity.getSymbol());
                    }
                    SPUtils.putString(AppConfig.KEY_OPTIONAL, Util.deal(setList.toString()));
                }


                break;


            case R.id.img_setting:
                if (isLogin()) {
                    UserActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_TRADE_SETTINGS);
                } else {
                    LoginActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }


                break;

            case R.id.layout_much:

                if (isLogin()) {
                    String priceMuch = text_buy_much.getText().toString();
                    boolean isOpenSure = SPUtils.getBoolean(AppConfig.KEY_OPEN_SURE, true);
                    if (isOpenSure) {
                        slowOpen("true", priceMuch);
                    } else {
                        fastOpen("true", priceMuch);
                    }
                } else {
                    LoginActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_LOGIN);

                }

                break;
            case R.id.layout_empty:
                if (isLogin()) {
                    String priceEmpty = text_buy_empty.getText().toString();
                    boolean isOpenSure = SPUtils.getBoolean(AppConfig.KEY_OPEN_SURE, true);
                    if (isOpenSure) {
                        slowOpen("false", priceEmpty);
                    } else {
                        fastOpen("false", priceEmpty);
                    }
                } else {
                    LoginActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;


            case R.id.text_market_all:
                String value_margin = edit_market_margin.getText().toString();
                if (value_margin.equals("")) {
                    value_margin = "0.0";
                }
                Util.lightOff(QuoteDetailActivity.this);
                if (prizeTrade != null) {
                    String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, value_margin, lever);
                    String prizeDub = TradeUtil.deductionResult(service, value_margin, prizeTrade);
                    PopUtil.getInstance().showLongTip(QuoteDetailActivity.this,
                            layout_view, false,
                            getString(R.string.text_service_tip),
                            getString(R.string.text_trade_fees),
                            getString(R.string.text_deduction_amount),
                            service,
                            prizeDub, state -> {

                            });
                }
                break;
            case R.id.text_limit_all:
                String value_margin_limit = edit_limit_margin.getText().toString();
                if (value_margin_limit.equals("")) {
                    value_margin_limit = "0.0";
                }
                if (prizeTrade != null) {
                    String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, value_margin_limit, lever);
                    String prizeDub = TradeUtil.deductionResult(service, value_margin_limit, prizeTrade);
                    Util.lightOff(QuoteDetailActivity.this);
                    PopUtil.getInstance().showLongTip(QuoteDetailActivity.this,
                            layout_view, false,
                            getString(R.string.text_service_tip),
                            getString(R.string.text_trade_fees),
                            getString(R.string.text_deduction_amount),
                            service,
                            prizeDub, state -> {

                            });
                }
                break;
            case R.id.text_position:
                if (isLogin()) {
                    UserActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_HOLD);
                } else {
                    LoginActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.text_one_hour:

                Quote60MinHistoryManger.getInstance().quote(quote_code, -2);
                Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);


                ((TextView) view.findViewById(R.id.text_title)).setText(text_one_hour.getText().toString());//设置一下文字颜色
                tabAt.setCustomView(view);


                layout_more.setVisibility(View.GONE);
                kline_1min_time.setVisibility(View.GONE);
                myKLineView_1Min.setVisibility(View.GONE);
                myKLineView_5Min.setVisibility(View.GONE);
                myKLineView_15Min.setVisibility(View.GONE);
                myKLineView_3Min.setVisibility(View.GONE);
                myKLineView_1H.setVisibility(View.VISIBLE);
                myKLineView_1D.setVisibility(View.GONE);
                myKLineView_1_week.setVisibility(View.GONE);
                myKLineView_1_month.setVisibility(View.GONE);
                break;
            case R.id.text_one_day:
                QuoteDayHistoryManger.getInstance().quote(quote_code, -2);
                QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);


                ((TextView) view.findViewById(R.id.text_title)).setText(text_one_day.getText().toString());//设置一下文字颜色
                tabAt.setCustomView(view);

                layout_more.setVisibility(View.GONE);
                kline_1min_time.setVisibility(View.GONE);
                myKLineView_1Min.setVisibility(View.GONE);
                myKLineView_5Min.setVisibility(View.GONE);
                myKLineView_15Min.setVisibility(View.GONE);
                myKLineView_3Min.setVisibility(View.GONE);
                myKLineView_1H.setVisibility(View.GONE);
                myKLineView_1D.setVisibility(View.VISIBLE);
                myKLineView_1_week.setVisibility(View.GONE);
                myKLineView_1_month.setVisibility(View.GONE);
                break;
            case R.id.text_one_week:
                QuoteWeekHistoryManger.getInstance().quote(quote_code, -2);
                QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, quote_code);
                ((TextView) view.findViewById(R.id.text_title)).setText(text_one_week.getText().toString());//设置一下文字颜色
                tabAt.setCustomView(view);
                layout_more.setVisibility(View.GONE);
                kline_1min_time.setVisibility(View.GONE);
                myKLineView_1Min.setVisibility(View.GONE);
                myKLineView_5Min.setVisibility(View.GONE);
                myKLineView_15Min.setVisibility(View.GONE);
                myKLineView_3Min.setVisibility(View.GONE);
                myKLineView_1H.setVisibility(View.GONE);
                myKLineView_1D.setVisibility(View.GONE);
                myKLineView_1_week.setVisibility(View.VISIBLE);
                myKLineView_1_month.setVisibility(View.GONE);

                break;
            case R.id.text_one_month:
                QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote_code), -2);
                QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(quote_code));

                ((TextView) view.findViewById(R.id.text_title)).setText(text_one_month.getText().toString());//设置一下文字颜色
                tabAt.setCustomView(view);
                layout_more.setVisibility(View.GONE);
                kline_1min_time.setVisibility(View.GONE);
                myKLineView_1Min.setVisibility(View.GONE);
                myKLineView_5Min.setVisibility(View.GONE);
                myKLineView_15Min.setVisibility(View.GONE);
                myKLineView_3Min.setVisibility(View.GONE);
                myKLineView_1H.setVisibility(View.GONE);
                myKLineView_1D.setVisibility(View.GONE);
                myKLineView_1_week.setVisibility(View.GONE);
                myKLineView_1_month.setVisibility(View.VISIBLE);

                break;

            case R.id.layout_market_lever_select:
            case R.id.layout_limit_lever_select:
                Util.lightOff(QuoteDetailActivity.this);
                showLeverWindow(tradeListEntity);


                break;

            case R.id.text_charge:
                if (isLogin()) {
                    if (tradeType.equals("1")) {
                        WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/deposit"), getResources().getString(R.string.text_recharge));
                    } else {
                        NetManger.getInstance().addScore((state, response) -> {
                            if (state.equals(SUCCESS)) {
                                AddScoreEntity addScoreEntity = (AddScoreEntity) response;
                                Toast.makeText(QuoteDetailActivity.this, addScoreEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    LoginActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*规则*/
            case R.id.text_rule:
                String s = itemQuoteCode(quote_code);
                if (!isLogin()) {
                    WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(null, s, "/rule"), getResources().getString(R.string.text_rule));
                } else {
                    WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), s, "/rule"), getResources().getString(R.string.text_rule));
                }

                break;
        }
    }


    private int oldSelect = 0;

    //选择杠杆
    private void showLeverWindow(TradeListEntity tradeListEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_lever_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_market);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(radioGroupAdapter);
        if (tradeListEntity != null) {
            List<Integer> leverShowList = tradeListEntity.getLeverShowList();
            radioGroupAdapter.setDatas(leverShowList);

            radioGroupAdapter.select(oldSelect);
            lever = leverShowList.get(oldSelect);

            radioGroupAdapter.setOnItemClick((position, data) -> {
                lever = data;
                oldSelect = position;
                radioGroupAdapter.select(position);
                recyclerView.setAdapter(radioGroupAdapter);
                radioGroupAdapter.notifyDataSetChanged();
                text_lever_market.setText(lever + "X");
                text_lever_limit.setText(lever + "X");
                popupWindow.dismiss();

                setLeverContent(tradeListEntity);


            });
        }
        Util.dismiss(QuoteDetailActivity.this, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_pop_market, Gravity.CENTER, 0, 0);
    }

    /*下单提示*/
    @SuppressLint("SetTextI18n")
    private void showTip(String isBuy, String margin, String service) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_much_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_margin = view.findViewById(R.id.text_margin_pop);
        text_margin.setText(margin + " " + getString(R.string.text_usdt));
        RelativeLayout layout_deduction = view.findViewById(R.id.layout_deduction);

        TextView text_service = view.findViewById(R.id.text_service_pop);
        text_service.setText(TradeUtil.numberHalfUp(Double.parseDouble(service), 2) + " " + getString(R.string.text_usdt));
        TextView text_all = view.findViewById(R.id.text_all_pop);
        text_deduction_amount_pop = view.findViewById(R.id.text_deduction_amount_pop);
        if (Double.parseDouble(TradeUtil.deductionResult(service, margin, prizeTrade)) == 0) {
            layout_deduction.setVisibility(View.GONE);
        }


        text_deduction_amount_pop.setText(TradeUtil.deductionResult(service, margin, prizeTrade) + " " + getString(R.string.text_usdt));

        text_all.setText(TradeUtil.total(margin, service, TradeUtil.deductionResult(service, margin, prizeTrade)) + getString(R.string.text_usdt));

        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            String priceMuch = text_buy_much.getText().toString();
            fastOpen(isBuy, priceMuch);
            popupWindow.dismiss();
        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> popupWindow.dismiss());

        Util.dismiss(QuoteDetailActivity.this, popupWindow);
        Util.isShowing(QuoteDetailActivity.this, popupWindow);

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }


    private void slowOpen(String isBuy, String priceMuchOrEmpty) {
        if (priceMuchOrEmpty.equals(getResources().getString(R.string.text_default))) {
            return;
        }

        String marginMarket = Objects.requireNonNull(edit_market_margin.getText()).toString();
        String marginLimit = Objects.requireNonNull(edit_limit_margin.getText()).toString();

        String margin = marginOrder(orderType, marginMarket, marginLimit);
        String priceOrder = TradeUtil.priceOrder(orderType, Objects.requireNonNull(edit_limit_price.getText()).toString());

        if (margin == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_margin_input), Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceOrder == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_limit_price_input), Toast.LENGTH_SHORT).show();
            return;
        }

        TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity= (ChargeUnitEntity) response1);

       /* TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityJson, new OnResult() {
            @Override
            public void setResult(Object response) {
                chargeUnitEntity= (ChargeUnitEntity) response;

            }
        });*/


        if (chargeUnitEntity == null) {
            Toast.makeText(QuoteDetailActivity.this, R.string.text_failure, Toast.LENGTH_SHORT).show();
            return;
        }else {
             ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
            if (state.equals(SUCCESS)) {
                chargeUnitEntityJson = (JSONObject) response;
                TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity= (ChargeUnitEntity) response1);
            }
        });
        }


        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);
        Util.lightOff(QuoteDetailActivity.this);
        showTip(isBuy, margin, serviceCharge);

    }

    /*下单*/
    private void fastOpen(String isBuy, String priceMuchOrEmpty) {
        isDefer = SPUtils.getBoolean(AppConfig.KEY_DEFER, false);

        TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(quoteMinEntity.getSymbol(), tradeListEntityList);
        if (priceMuchOrEmpty.equals(getResources().getString(R.string.text_default))) {
            return;
        }

        String marginMarket = Objects.requireNonNull(edit_market_margin.getText()).toString();
        String marginLimit = Objects.requireNonNull(edit_limit_margin.getText()).toString();

        int priceDigit = tradeListEntity.getPriceDigit();
        String margin = marginOrder(orderType, marginMarket, marginLimit);
        String priceOrder = TradeUtil.priceOrder(orderType, Objects.requireNonNull(edit_limit_price.getText()).toString());
        String defer = TradeUtil.defer(tradeType, isDefer);
        //Log.d("print", "onClick: 保证金:" + margin + "  是否递延:" + defer + "  价格:" + priceOrder);

        if (margin == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_margin_input), Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceOrder == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_limit_price_input), Toast.LENGTH_SHORT).show();
            return;
        }
        TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity= (ChargeUnitEntity) response1);

        if (chargeUnitEntity == null) {
            Toast.makeText(QuoteDetailActivity.this, R.string.text_failure, Toast.LENGTH_SHORT).show();
            return;
        }else {
             ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
            if (state.equals(SUCCESS)) {
                chargeUnitEntityJson = (JSONObject) response;
                TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity= (ChargeUnitEntity) response1);
            }
        });
        }
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);


        String stopProfit = SPUtils.getString(AppConfig.VALUE_PROFIT, "3");
        String stopLoss = SPUtils.getString(AppConfig.VALUE_LOSS, "-0.9");
        NetManger.getInstance().order(tradeType, "2", tradeListEntity.getCode(),
                tradeListEntity.getContractCode(), isBuy, margin, String.valueOf(lever), priceOrder, defer,
                TradeUtil.deferFee(priceDigit, defer, tradeListEntity.getDeferFee(), margin, lever), stopProfit, stopLoss, serviceCharge,
                "0", TradeUtil.volume(lever, margin, parseDouble(priceMuchOrEmpty)), "0", "USDT", (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        Toast.makeText(QuoteDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                        PositionRealManger.getInstance().getHold();
                        PositionSimulationManger.getInstance().getHold();
                        BalanceManger.getInstance().getBalance("USDT");

                    } else if (state.equals(FAILURE)) dismissProgressDialog();
                }

        );
    }


    /*行情选择*/
    private void showProductWindow(List<String> quoteList) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_product_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_pop);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        quotePopAdapter = new QuotePopAdapter(this);
        recyclerView.setAdapter(quotePopAdapter);
        if (quoteList != null) {
            quotePopAdapter.setDatas(quoteList);
        }
        if (quoteMinEntity != null) {
            quotePopAdapter.select(quoteMinEntity.getSymbol());

        }


        quotePopAdapter.setOnItemClick(data -> {
            quote_code = TradeUtil.itemQuoteContCode(data);

            //自选的图标
            String optional = SPUtils.getString(AppConfig.KEY_OPTIONAL, null);
            setList = new HashSet<>();
            if (optional != null) {
                String[] split = optional.split(",");
                for (String a : split) {
                    setList.add(a);
                }
                if (setList.contains(itemQuoteContCode(data))) {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                } else {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                }
            }


            text_market_currency.setText(TradeUtil.currency(data));
            text_limit_currency.setText(TradeUtil.currency(data));

            text_name.setText(TradeUtil.name(data));
            text_name_usdt.setText(TradeUtil.currency(data));
            // QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, itemQuoteContCode(data));

            edit_limit_price.setDecimalEndNumber(TradeUtil.decimalPoint(listQuotePrice(data)));//根据不同的小数位限制
            edit_limit_price.setText(listQuotePrice(data));

            // Quote1MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
            QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));


            Quote1MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            Quote5MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            Quote15MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            Quote3MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            Quote60MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            QuoteDayHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            QuoteWeekHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);
            QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(data), -2);

            //相应选择
            quotePopAdapter.select(itemQuoteContCode(data));
            popupWindow.dismiss();
            Log.d("print", "showProductWindow:1360:  " + tradeListEntityList);
            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(data), tradeListEntityList);


            setContent(tradeListEntity);


        });


        Button btn_switch = view.findViewById(R.id.btn_switch);

        if (tradeType.equals("1")) {
            btn_switch.setText(getResources().getText(R.string.text_simulation_btn));

        } else if (tradeType.equals("2")) {
            btn_switch.setText(getResources().getText(R.string.text_real_btn));

        }

        btn_switch.setOnClickListener(v -> {
            if (tradeType.equals("1")) {

                tradeType = "2";
                text_switch.setText(getResources().getText(R.string.text_simulation_trade));
                radio_btn1.setVisibility(View.GONE);


            } else if (tradeType.equals("2")) {

                tradeType = "1";
                text_switch.setText(getResources().getText(R.string.text_real_trade));
                radio_btn1.setVisibility(View.VISIBLE);


            }
            radio_btn0.setChecked(true);
            if (radio_btn0.isChecked()) {
                layout_market_price.setVisibility(View.VISIBLE);
                layout_limit_price.setVisibility(View.GONE);


            }
            //可用余额
            if (tradeType.equals("1")) {
                text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
                text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
            } else {
                text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
                text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
            }

            popupWindow.dismiss();


        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);

            if (quoteList != null && quoteAdapter_market != null) {
                runOnUiThread(() -> {
                    //搜索框
                    if (edit_search.getText().toString().equals("")) {
                        quoteAdapter_market.setDatas(quoteList);
                    } else {
                        List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), quoteList);
                        quoteAdapter_market.setDatas(searchQuoteList);
                    }


                });
            }


        } else if (o == BalanceManger.getInstance()) {

            BalanceEntity balanceEntity = (BalanceEntity) arg;
            if (balanceEntity == null) {
                return;
            }
            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                if (data.getCurrency().equals(tradeType)) {
                    BalanceManger.getInstance().setBalanceReal(data.getMoney());
                    BalanceManger.getInstance().setBalanceSim(data.getGame());
                    BalanceManger.getInstance().setPrize(data.getPrize());
                    BalanceManger.getInstance().setLucky(data.getLucky());
                }
            }
            //可用余额
            if (tradeType.equals("1")) {
                text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));
                text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));
            } else {
                text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2) + " " + getResources().getString(R.string.text_usdt));
                text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2) + " " + getResources().getString(R.string.text_usdt));
            }

        } else if (o == TradeListManger.getInstance()) {
            tradeListEntityList = (List<TradeListEntity>) arg;
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            setContent(tradeListEntity);
        } else if (o == QuoteCurrentManger.getInstance()) {
            quoteMinEntity = (QuoteMinEntity) arg;
            if (quoteMinEntity != null) {
                //Log.d("print", "update:1549:  " + quoteMinEntity);
                runOnUiThread(() -> {
                    if (quoteMinEntity.getSymbol().equals(quote_code)) {
                        //    Toast.makeText(QuoteDetailActivity.this, quoteMinEntity.getSymbol() + "    " + quote_code, Toast.LENGTH_SHORT).show();
                        //仓位实时更新 服务费
                        if (Objects.requireNonNull(edit_market_margin.getText()).length() != 0) {
                            text_market_volume.setText(TradeUtil.volume(lever, edit_market_margin.getText().toString(), quoteMinEntity.getPrice()));
                            String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_market_margin.getText().toString(), lever);
                           // Log.d("print", "update:服务费:  " +chargeUnitEntity +"            "+service);

                            if (prizeTrade != null && service != null) {
                                text_market_all.setText(TradeUtil.total(edit_market_margin.getText().toString(),
                                        service,
                                        TradeUtil.deductionResult(service, edit_market_margin.getText().toString(), prizeTrade)) + " " + getResources().getString(R.string.text_usdt));
                            }

                        } else {
                            text_market_volume.setText(getResources().getText(R.string.text_default));
                            text_market_all.setText(getResources().getText(R.string.text_default));

                        }
                        if (Objects.requireNonNull(edit_limit_margin.getText()).length() != 0) {
                            text_limit_volume.setText(TradeUtil.volume(lever, edit_limit_margin.getText().toString(), quoteMinEntity.getPrice()));
                            String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_limit_margin.getText().toString(), lever);
                            if (prizeTrade != null && service != null) {
                                text_limit_all.setText(TradeUtil.total(edit_limit_margin.getText().toString(),
                                        service,
                                        TradeUtil.deductionResult(service, edit_limit_margin.getText().toString(), prizeTrade)) + " " + getResources().getString(R.string.text_usdt));
                            }

                        } else {
                            text_limit_volume.setText(getResources().getText(R.string.text_default));
                            text_limit_all.setText(getResources().getText(R.string.text_default));

                        }


                        if (quotePopAdapter != null) {
                            quotePopAdapter.select(quoteMinEntity.getSymbol());
                        }


                        text_lastPrice.setText(String.valueOf(quoteMinEntity.getPrice()));
                        text_change.setText(TradeUtil.quoteChange(String.valueOf(quoteMinEntity.getPrice()), String.valueOf(quoteMinEntity.getOpen())));
                        text_range.setText(TradeUtil.quoteRange(String.valueOf(quoteMinEntity.getPrice()), String.valueOf(quoteMinEntity.getOpen())));

                        if (quoteMinEntity.getIsUp() == -1) {
                            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));

                            img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_down));

                        } else if (quoteMinEntity.getIsUp() == 1) {
                            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                            img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_up));

                        } else if (quoteMinEntity.getIsUp() == 0) {

                            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));

                        }
                        text_max.setText(String.valueOf(quoteMinEntity.getMax()));
                        text_min.setText(String.valueOf(quoteMinEntity.getMin()));
                        text_volume.setText(quoteMinEntity.getVolume());


                        String spread = TradeUtil.spread(quoteMinEntity.getSymbol(), tradeListEntityList);

                        if (spread != null) {
                            text_buy_much.setText(String.valueOf(quoteMinEntity.getBuyPrice()));
                            text_buy_empty.setText(String.valueOf(quoteMinEntity.getSellPrice()));
                        }

                        // List<KData> kData = ChartUtil.klineList(data);
                        if (kData1MinHistory != null) {
                            KData kData = new KData(quoteMinEntity.getT() * 1000, quoteMinEntity.getO(), quoteMinEntity.getPrice(), quoteMinEntity.getH(), quoteMinEntity.getL(), quoteMinEntity.getV());
                            // Log.d("print", "update:1453: " + kData.get(kData.size() - 2).getTime());
                            myKLineView_1Min.addSingleData(kData);
                            //kline_1min_time.addSingleData(kData.get(kData.size() - 1));
                            kline_1min_time.addSingleData(kData);

                        } else {
                            Quote1MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                        }
                    }


                });


            }


        } else if (o == Quote3MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData3MinHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_3Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {
                    Quote3MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == Quote5MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);

            if (kData5MinHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_5Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    Quote5MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == Quote15MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData15MinHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_15Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    Quote15MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == Quote60MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData60MinHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1H.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {
                    Quote60MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == QuoteDayCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataDayHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1D.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        } else if (o == QuoteWeekCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataWeekHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1_week.addSingleData(kData.get(kData.size() - 1));


            } else {
                if (quoteMinEntity != null) {

                    QuoteWeekHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        } else if (o == QuoteMonthCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataMonthHistory != null && quoteMinEntity != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1_month.addSingleData(kData.get(kData.size() - 1));

            } else {
                if (quoteMinEntity != null) {

                    QuoteMonthHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        }
        /*历史分割线-----------------------------------------------------------------------------------*/
        else if (o == Quote1MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData1MinHistory = ChartUtil.klineList(data);
            if (kData1MinHistory != null) {
                myKLineView_1Min.initKDataList(kData1MinHistory);
                kline_1min_time.initKDataList(kData1MinHistory);


            } else {
                if (quoteMinEntity != null) {
                    Quote1MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == Quote3MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData3MinHistory = ChartUtil.klineList(data);

            if (kData3MinHistory != null) {

                myKLineView_3Min.initKDataList(kData3MinHistory);
            } else {
                if (quoteMinEntity != null) {

                    Quote3MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == Quote5MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData5MinHistory = ChartUtil.klineList(data);
            if (kData5MinHistory != null) {
                myKLineView_5Min.initKDataList(kData5MinHistory);
            } else {
                if (quoteMinEntity != null) {

                    Quote5MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == Quote15MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData15MinHistory = ChartUtil.klineList(data);

            if (kData15MinHistory != null) {

                myKLineView_15Min.initKDataList(kData15MinHistory);
            } else {
                if (quoteMinEntity != null) {

                    Quote15MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == Quote60MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData60MinHistory = ChartUtil.klineList(data);

            if (kData60MinHistory != null) {

                myKLineView_1H.initKDataList(kData60MinHistory);
            } else {
                if (quoteMinEntity != null) {

                    Quote60MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == QuoteDayHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataDayHistory = ChartUtil.klineList(data);
            if (kDataDayHistory != null) {
                myKLineView_1D.initKDataList(kDataDayHistory);

            } else {
                if (quoteMinEntity != null) {

                    QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == QuoteWeekHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataWeekHistory = ChartUtil.klineList(data);
            if (kDataWeekHistory != null) {

                List<KData> weekList = ChartUtil.KlineWeekData(ChartUtil.getWeekKDataList(kDataWeekHistory));
                myKLineView_1_week.initKDataList(weekList);


            } else {
                if (quoteMinEntity != null) {

                    QuoteWeekHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == QuoteMonthHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataMonthHistory = ChartUtil.klineList(data);
            if (kDataMonthHistory != null) {

                List<KData> monthList = ChartUtil.KlineMonthData(ChartUtil.getMonthKDataList(kDataMonthHistory));
                myKLineView_1_month.initKDataList(monthList);

            } else {
                if (quoteMinEntity != null) {

                    QuoteMonthHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);

                }
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(QuoteDetailActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
        //要取消计时 防止内存溢出
        cancelTimer();
        QuoteCurrentManger.getInstance().clear();

        Quote1MinHistoryManger.getInstance().clear();
        Quote1MinHistoryManger.getInstance().cancelTimer();
        Quote5MinHistoryManger.getInstance().clear();
        Quote5MinHistoryManger.getInstance().cancelTimer();
        Quote15MinHistoryManger.getInstance().clear();
        Quote15MinHistoryManger.getInstance().cancelTimer();
        Quote3MinHistoryManger.getInstance().clear();
        Quote3MinHistoryManger.getInstance().cancelTimer();
        Quote60MinHistoryManger.getInstance().clear();
        Quote60MinHistoryManger.getInstance().cancelTimer();
        QuoteDayHistoryManger.getInstance().clear();
        QuoteDayHistoryManger.getInstance().cancelTimer();
        QuoteWeekHistoryManger.getInstance().clear();
        QuoteWeekHistoryManger.getInstance().cancelTimer();
        QuoteMonthHistoryManger.getInstance().clear();
        QuoteMonthHistoryManger.getInstance().cancelTimer();

        myKLineView_1Min.cancelQuotaThread();
        myKLineView_5Min.cancelQuotaThread();
        myKLineView_15Min.cancelQuotaThread();
        myKLineView_3Min.cancelQuotaThread();
        myKLineView_1H.cancelQuotaThread();
        myKLineView_1D.cancelQuotaThread();
        myKLineView_1_week.cancelQuotaThread();
        myKLineView_1_month.cancelQuotaThread();


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                layout_market_price.setVisibility(View.VISIBLE);
                layout_limit_price.setVisibility(View.GONE);
                text_buy_much.setVisibility(View.VISIBLE);
                text_buy_empty.setVisibility(View.VISIBLE);
                radio_btn0.setTextSize(14);
                radio_btn1.setTextSize(13);
                orderType = "0";

                break;
            case R.id.radio_1:
                layout_market_price.setVisibility(View.GONE);
                layout_limit_price.setVisibility(View.VISIBLE);
                text_buy_much.setVisibility(View.GONE);
                text_buy_empty.setVisibility(View.GONE);
                radio_btn0.setTextSize(13);
                radio_btn1.setTextSize(14);
                orderType = "1";
                break;


        }
    }


}
