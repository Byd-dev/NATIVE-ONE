package com.pro.bityard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
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
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.adapter.RadioGroupAdapter;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.chart.KData;
import com.pro.bityard.chart.NoVolumeView;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.AddScoreEntity;
import com.pro.bityard.entity.ChargeUnitEntity;
import com.pro.bityard.entity.QuoteChartEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.Quote15MinCurrentManger;
import com.pro.bityard.manger.Quote15MinHistoryManger;
import com.pro.bityard.manger.Quote1MinCurrentManger;
import com.pro.bityard.manger.Quote1MinHistoryManger;
import com.pro.bityard.manger.Quote3MinCurrentManger;
import com.pro.bityard.manger.Quote3MinHistoryManger;
import com.pro.bityard.manger.Quote5MinCurrentManger;
import com.pro.bityard.manger.Quote5MinHistoryManger;
import com.pro.bityard.manger.Quote60MinCurrentManger;
import com.pro.bityard.manger.Quote60MinHistoryManger;
import com.pro.bityard.manger.QuoteDayCurrentManger;
import com.pro.bityard.manger.QuoteDayHistoryManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.manger.QuoteMonthCurrentManger;
import com.pro.bityard.manger.QuoteMonthHistoryManger;
import com.pro.bityard.manger.QuoteWeekCurrentManger;
import com.pro.bityard.manger.QuoteWeekHistoryManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.manger.TradeListManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.itemQuotePrice;
import static com.pro.bityard.utils.TradeUtil.itemQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.listQuoteName;
import static com.pro.bityard.utils.TradeUtil.listQuotePrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteUSD;
import static com.pro.bityard.utils.TradeUtil.marginMax;
import static com.pro.bityard.utils.TradeUtil.marginOrder;
import static java.lang.Double.parseDouble;

public class QuoteDetailActivity extends BaseActivity implements View.OnClickListener, Observer, RadioGroup.OnCheckedChangeListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "1";
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

    @BindView(R.id.img_right)
    ImageView img_right;

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
    private String quote;
    private List<TradeListEntity> tradeListEntityList;
    private List<ChargeUnitEntity> chargeUnitEntityList;
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


        titles.add("Line");
        titles.add("1min");
        titles.add("3min");
        titles.add("5min");
        titles.add("15min");
        titles.add(getResources().getString(R.string.text_more));


        QuoteListManger.getInstance().addObserver(this);
        //QuoteItemManger.getInstance().addObserver(this);

        Quote1MinCurrentManger.getInstance().addObserver(this);//1min 实时
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

        BalanceManger.getInstance().getBalance("USDT");

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

        //更多的监听
        findViewById(R.id.text_one_hour).setOnClickListener(this);
        findViewById(R.id.text_one_day).setOnClickListener(this);
        findViewById(R.id.text_one_week).setOnClickListener(this);
        findViewById(R.id.text_one_month).setOnClickListener(this);
        findViewById(R.id.text_rule).setOnClickListener(this);


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


    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        if (itemData.equals("")) {
            return;
        }

        Quote1MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -1);
        Quote3MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        Quote5MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        Quote15MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        Quote60MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        QuoteDayHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        QuoteWeekHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(itemData), -2);
        //开启单个刷新
        //  QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        //开启单个行情图
        Quote1MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));

        //合约号
        tradeListEntityList = TradeListManger.getInstance().getTradeListEntityList();
        //手续费
        chargeUnitEntityList = ChargeUnitManger.getInstance().getChargeUnitEntityList();
        if (tradeListEntityList == null) {
            startHandler(handler, 0, QUOTE_SECOND, QUOTE_SECOND);
        } else {
            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            // Log.d("print", "initData:258:合约号:  " + tradeListEntity);
            setContent(tradeListEntity);
        }

        if (chargeUnitEntityList == null) {
            startHandler(handler, 1, QUOTE_SECOND, QUOTE_SECOND);
        } else {
            chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
            // Log.d("print", "initData:259:手续费: " + chargeUnitEntity);
        }


        String[] split1 = Util.quoteList(itemQuoteContCode(itemData)).split(",");
        text_name.setText(split1[0]);
        text_name_usdt.setText(split1[1]);
        text_lastPrice.setText(listQuotePrice(itemData));
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
            text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
            text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
        } else {
            text_market_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
            text_limit_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    TradeListManger.getInstance().tradeList((state, response) -> {
                        if (state.equals(SUCCESS)) {
                            cancelTimer();
                        }
                    });
                    break;
                case 1:
                    ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                        if (state.equals(SUCCESS)) {
                            cancelTimer();
                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }


        }
    };


    /*设置 保证金和杠杆*/
    public void setContent(TradeListEntity tradeListEntity) {
        if (tradeListEntity != null) {
            edit_market_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));
            edit_limit_margin.setHint(TradeUtil.deposit(tradeListEntity.getDepositList()));

            List<Integer> leverShowList = tradeListEntity.getLeverShowList();
            lever = leverShowList.get(0);

           /* recyclerView_market.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView_limit.setLayoutManager(new GridLayoutManager(this, 3));

            radioGroupAdapter.setDatas(leverShowList);
            radioGroupAdapter.select(0);
            lever = leverShowList.get(0);

            radioGroupAdapter.setOnItemClick((position, data) -> {
                lever = data;
                radioGroupAdapter.select(position);
                recyclerView_market.setAdapter(radioGroupAdapter);
                recyclerView_limit.setAdapter(radioGroupAdapter);
                radioGroupAdapter.notifyDataSetChanged();
                text_lever_market.setText(lever + "X");
                text_lever_limit.setText(lever + "X");
                if (layout_market.isShown()) {
                    layout_market.setVisibility(View.GONE);
                    layout_lever_market.setVisibility(View.VISIBLE);
                } else {
                    layout_market.setVisibility(View.VISIBLE);
                    layout_lever_market.setVisibility(View.GONE);
                }

                if (layout_limit.isShown()) {
                    layout_limit.setVisibility(View.GONE);
                    layout_lever_limit.setVisibility(View.VISIBLE);
                } else {
                    layout_limit.setVisibility(View.VISIBLE);
                    layout_lever_limit.setVisibility(View.GONE);
                }

            });*/

            edit_market_margin.addTextChangedListener(new TextWatcher() {
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
                            } /*else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_market_margin.postDelayed(() -> edit_market_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }*/
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
                            }/* else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.postDelayed(() -> edit_limit_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }*/
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onClick(View v) {
        TabLayout.Tab tabAt = tabLayout.getTabAt(5);
        View view = tabAt.getCustomView();

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_product:
                img_right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_market_open));
                showProductWindow(quoteList);
                break;


            case R.id.img_setting:
                if (isLogin()){
                    UserActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_TRADE_SETTINGS);
                }else {
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
                if (text_market_all.getText().toString().equals(getResources().getString(R.string.text_default))) {
                    return;
                } else {
                    Util.lightOff(QuoteDetailActivity.this);
                    PopUtil.getInstance().showTip(QuoteDetailActivity.this, layout_view, false,
                            new StringBuilder().append(getString(R.string.text_service_tip)).append(getString(R.string.text_trade_fees)).append(text_market_all.getText().toString()).append("USDT").toString(), state -> {
                            }
                    );
                }
                break;
            case R.id.text_limit_all:
                if (text_limit_all.getText().toString().equals(getResources().getString(R.string.text_default))) {
                    return;
                } else {
                    Util.lightOff(QuoteDetailActivity.this);
                    PopUtil.getInstance().showTip(QuoteDetailActivity.this, layout_view, false,
                            new StringBuilder().append(getString(R.string.text_service_tip)).append(getString(R.string.text_trade_fees)).append(text_limit_all.getText().toString()).append("USDT").toString(), state -> {
                            }
                    );
                }
                break;
            case R.id.text_position:
                /*TabManger.getInstance().jump(MainOneActivity.TAB_TYPE.TAB_POSITION);
                finish();*/
                UserActivity.enter(QuoteDetailActivity.this, IntentConfig.Keys.KEY_HOLD);
                break;
            case R.id.text_one_hour:

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
                UserActivity.enter(QuoteDetailActivity.this,IntentConfig.Keys.RULE,TradeUtil.itemQuoteContCode(quote));
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


            });
        }
        Util.dismiss(QuoteDetailActivity.this, popupWindow);
        Util.dismiss(QuoteDetailActivity.this, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_pop_market, Gravity.CENTER, 0, 0);
    }

    /*下单提示*/
    private void showTip(String isBuy, String margin, String service) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_much_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_margin = view.findViewById(R.id.text_margin_pop);
        text_margin.setText(margin);
        TextView text_service = view.findViewById(R.id.text_service_pop);
        text_service.setText(service);
        TextView text_all = view.findViewById(R.id.text_all_pop);
        text_all.setText(String.valueOf(TradeUtil.add(Double.parseDouble(margin), Double.parseDouble(service))));

        view.findViewById(R.id.text_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceMuch = text_buy_much.getText().toString();
                fastOpen(isBuy, priceMuch);
                popupWindow.dismiss();
            }
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

        ChargeUnitEntity chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
        if (chargeUnitEntity == null) {
            Toast.makeText(QuoteDetailActivity.this, R.string.text_tip_login, Toast.LENGTH_SHORT).show();
            return;
        }
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);
        Util.lightOff(QuoteDetailActivity.this);
        showTip(isBuy, margin, serviceCharge);

    }

    /*下单*/
    private void fastOpen(String isBuy, String priceMuchOrEmpty) {
        isDefer = SPUtils.getBoolean(AppConfig.KEY_DEFER, false);
        Log.d("print", "initView:是否递延:  " + isDefer);

        TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(quote), tradeListEntityList);
        if (priceMuchOrEmpty.equals(getResources().getString(R.string.text_default))) {
            return;
        }

        String marginMarket = Objects.requireNonNull(edit_market_margin.getText()).toString();
        String marginLimit = Objects.requireNonNull(edit_limit_margin.getText()).toString();

        String margin = marginOrder(orderType, marginMarket, marginLimit);
        String priceOrder = TradeUtil.priceOrder(orderType, Objects.requireNonNull(edit_limit_price.getText()).toString());
        String defer = TradeUtil.defer(tradeType, isDefer);
        Log.d("print", "onClick: 保证金:" + margin + "  是否递延:" + defer + "  价格:" + priceOrder);

        if (margin == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_margin_input), Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceOrder == null) {
            Toast.makeText(QuoteDetailActivity.this, getResources().getText(R.string.text_limit_price_input), Toast.LENGTH_SHORT).show();
            return;
        }

        ChargeUnitEntity chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
        if (chargeUnitEntity == null) {
            Toast.makeText(QuoteDetailActivity.this, R.string.text_tip_login, Toast.LENGTH_SHORT).show();
            return;
        }
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);


        String stopProfit = SPUtils.getString(AppConfig.VALUE_PROFIT, "3");
        String stopLoss = SPUtils.getString(AppConfig.VALUE_LOSS, "-0.9");
        NetManger.getInstance().order(tradeType, "2", tradeListEntity.getCode(),
                tradeListEntity.getContractCode(), isBuy, margin, String.valueOf(lever), priceOrder, defer,
                TradeUtil.deferFee(defer, tradeListEntity.getDeferFee(), margin, lever), stopProfit, stopLoss, serviceCharge,
                "0", TradeUtil.volume(lever, margin, parseDouble(priceMuchOrEmpty)), "0", "USDT", (state, response) -> {
                    if (state.equals(BUSY)) {
                         showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        Toast.makeText(QuoteDetailActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                        PositionRealManger.getInstance().getHold();
                        PositionSimulationManger.getInstance().getHold();
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
        if (quote != null) {
            quotePopAdapter.select(itemQuoteContCode(quote));

        }

        popupWindow.setOnDismissListener(() -> {
            img_right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_market_right));

        });
        quotePopAdapter.setOnItemClick(data -> {
            text_name.setText(listQuoteName(data));
            text_name_usdt.setText(listQuoteUSD(data));
            // QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, itemQuoteContCode(data));

            Quote1MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(data));
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
            img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_right));
            popupWindow.dismiss();


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
            img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_right));


        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteListManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(quoteType);
            if (quotePopAdapter != null && quoteList != null) {
                quotePopAdapter.setDatas(quoteList);
            }

        } /*else if (o == QuoteItemManger.getInstance()) {
            quote = (String) arg;

            if (quote != null) {
                //仓位实时更新 服务费
                if (Objects.requireNonNull(edit_market_margin.getText()).length() != 0) {
                    text_market_volume.setText(TradeUtil.volume(lever, edit_market_margin.getText().toString(), parseDouble(itemQuotePrice(quote))));
                    text_market_all.setText(TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_market_margin.getText().toString(), lever));

                }
                if (Objects.requireNonNull(edit_limit_margin.getText()).length() != 0) {
                    text_limit_volume.setText(TradeUtil.volume(lever, edit_limit_margin.getText().toString(), parseDouble(itemQuotePrice(quote))));
                    text_limit_all.setText(TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_limit_margin.getText().toString(), lever));

                }


                if (quotePopAdapter != null) {
                    quotePopAdapter.select(itemQuoteContCode(quote));
                }


                text_lastPrice.setText(itemQuotePrice(quote));
                text_change.setText(TradeUtil.quoteChange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));
                text_range.setText(TradeUtil.quoteRange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));

                if (itemQuoteIsRange(quote).equals("-1")) {
                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));

                    img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_down));

                } else if (itemQuoteIsRange(quote).equals("1")) {
                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_up));

                } else if (itemQuoteIsRange(quote).equals("0")) {

                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));

                }

                text_max.setText(TradeUtil.itemQuoteMaxPrice(quote));
                text_min.setText(TradeUtil.itemQuoteMinPrice(quote));
                text_volume.setText(TradeUtil.itemQuoteVolume(quote));


                String spread = TradeUtil.spread(TradeUtil.itemQuoteContCode(quote), tradeListEntityList);

                if (spread != null) {
                    text_buy_much.setText(TradeUtil.itemQuoteBuyMuchPrice(quote, Integer.parseInt(spread)));
                    text_buy_empty.setText(TradeUtil.itemQuoteBuyEmptyPrice(quote, Integer.parseInt(spread)));
                }

            }

        }*/ else if (o == TradeListManger.getInstance()) {
            tradeListEntityList = (List<TradeListEntity>) arg;
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            //  Log.d("print", "initData:510:  " + tradeListEntity);
            setContent(tradeListEntity);
        } else if (o == ChargeUnitManger.getInstance()) {
            chargeUnitEntityList = (List<ChargeUnitEntity>) arg;
            chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
            //   Log.d("print", "update:596: " + chargeUnitEntity);


        } else if (o == TagManger.getInstance()) {
            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {

            });
        } else if (o == Quote1MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;

            quote = data.getQuote();
            if (quote != null) {
                //仓位实时更新 服务费
                if (Objects.requireNonNull(edit_market_margin.getText()).length() != 0) {
                    text_market_volume.setText(TradeUtil.volume(lever, edit_market_margin.getText().toString(), parseDouble(itemQuotePrice(quote))));
                    text_market_all.setText(TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_market_margin.getText().toString(), lever));

                }
                if (Objects.requireNonNull(edit_limit_margin.getText()).length() != 0) {
                    text_limit_volume.setText(TradeUtil.volume(lever, edit_limit_margin.getText().toString(), parseDouble(itemQuotePrice(quote))));
                    text_limit_all.setText(TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_limit_margin.getText().toString(), lever));

                }


                if (quotePopAdapter != null) {
                    quotePopAdapter.select(itemQuoteContCode(quote));
                }


                text_lastPrice.setText(itemQuotePrice(quote));
                text_change.setText(TradeUtil.quoteChange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));
                text_range.setText(TradeUtil.quoteRange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));

                if (itemQuoteIsRange(this.quote).equals("-1")) {
                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));

                    img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_down));

                } else if (itemQuoteIsRange(this.quote).equals("1")) {
                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                    img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_up));

                } else if (itemQuoteIsRange(this.quote).equals("0")) {

                    text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                    text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));
                    text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_main_color));

                }

                text_max.setText(TradeUtil.itemQuoteMaxPrice(quote));
                text_min.setText(TradeUtil.itemQuoteMinPrice(quote));
                text_volume.setText(TradeUtil.itemQuoteVolume(quote));


                String spread = TradeUtil.spread(TradeUtil.itemQuoteContCode(quote), tradeListEntityList);

                if (spread != null) {
                    text_buy_much.setText(TradeUtil.itemQuoteBuyMuchPrice(quote, Integer.parseInt(spread)));
                    text_buy_empty.setText(TradeUtil.itemQuoteBuyEmptyPrice(quote, Integer.parseInt(spread)));
                }

                List<KData> kData = ChartUtil.klineList(data);
                if (kData1MinHistory != null) {
                    // Log.d("print", "update:1453: " + kData.get(kData.size() - 2).getTime());
                    myKLineView_1Min.addSingleData(kData.get(kData.size() - 1));
                    kline_1min_time.addSingleData(kData.get(kData.size() - 1));


                } else {
                    Quote1MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(this.quote), -2);
                }

            }


        } else if (o == Quote3MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData3MinHistory != null) {
                kData.get(kData.size() - 1).setClosePrice(Double.parseDouble(itemQuotePrice(data.getQuote())));
                myKLineView_3Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                Quote3MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == Quote5MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);

            if (kData5MinHistory != null) {
                kData.get(kData.size() - 1).setClosePrice(Double.parseDouble(itemQuotePrice(data.getQuote())));
                myKLineView_5Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                Quote5MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == Quote15MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData15MinHistory != null) {
                kData.get(kData.size() - 1).setClosePrice(Double.parseDouble(itemQuotePrice(data.getQuote())));
                myKLineView_15Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                Quote15MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == Quote60MinCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData60MinHistory != null) {
                kData.get(kData.size() - 1).setClosePrice(Double.parseDouble(itemQuotePrice(data.getQuote())));
                myKLineView_1H.addSingleData(kData.get(kData.size() - 1));
            } else {
                Quote60MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == QuoteDayCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataDayHistory != null) {
                myKLineView_1D.addSingleData(kData.get(kData.size() - 1));
            } else {
                QuoteDayHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == QuoteWeekCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataWeekHistory != null) {

                kData.get(kData.size() - 1).setTime(ChartUtil.setWeekTime(kData));
                myKLineView_1_week.addSingleData(kData.get(kData.size() - 1));


            } else {
                QuoteWeekHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }
        } else if (o == QuoteMonthCurrentManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataMonthHistory != null) {

                kData.get(kData.size() - 1).setTime(ChartUtil.setMonthTime(kData));
                myKLineView_1_month.addSingleData(kData.get(kData.size() - 1));

            } else {
                QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

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
                Quote1MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == Quote3MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData3MinHistory = ChartUtil.klineList(data);

            if (kData3MinHistory != null) {

                myKLineView_3Min.initKDataList(kData3MinHistory);
            } else {
                Quote3MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == Quote5MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData5MinHistory = ChartUtil.klineList(data);
            if (kData5MinHistory != null) {
                myKLineView_5Min.initKDataList(kData5MinHistory);
            } else {
                Quote5MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == Quote15MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData15MinHistory = ChartUtil.klineList(data);

            if (kData15MinHistory != null) {

                myKLineView_15Min.initKDataList(kData15MinHistory);
            } else {
                Quote15MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == Quote60MinHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kData60MinHistory = ChartUtil.klineList(data);

            if (kData60MinHistory != null) {

                myKLineView_1H.initKDataList(kData60MinHistory);
            } else {
                Quote60MinHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == QuoteDayHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataDayHistory = ChartUtil.klineList(data);
            if (kDataDayHistory != null) {
                myKLineView_1D.initKDataList(kDataDayHistory);

            } else {
                QuoteDayHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == QuoteWeekHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataWeekHistory = ChartUtil.klineList(data);
            if (kDataWeekHistory != null) {

                List<KData> weekList = ChartUtil.KlineWeekData(ChartUtil.getWeekKDataList(kDataWeekHistory));
                myKLineView_1_week.initKDataList(weekList);


            } else {
                QuoteWeekHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        } else if (o == QuoteMonthHistoryManger.getInstance()) {
            QuoteChartEntity data = (QuoteChartEntity) arg;
            kDataMonthHistory = ChartUtil.klineList(data);
            if (kDataMonthHistory != null) {

                List<KData> monthList = ChartUtil.KlineMonthData(ChartUtil.getMonthKDataList(kDataMonthHistory));
                myKLineView_1_month.initKDataList(monthList);

            } else {
                QuoteMonthHistoryManger.getInstance().quote(TradeUtil.itemQuoteContCode(quote), -2);

            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

       /* QuoteItemManger.getInstance().clear();
        QuoteItemManger.getInstance().cancelTimer();*/

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
