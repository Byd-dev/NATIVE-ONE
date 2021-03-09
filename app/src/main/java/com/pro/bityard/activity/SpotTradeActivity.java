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
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.OptionalSelectAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.adapter.RadioGroupAdapter;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.adapter.SellBuyListAdapter;
import com.pro.bityard.adapter.TradeNewAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.chart.KData;
import com.pro.bityard.chart.NoVolumeView;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.AddScoreEntity;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.BuySellEntity;
import com.pro.bityard.entity.ChargeUnitEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.MyQueue;
import com.pro.bityard.entity.QuoteChartEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.Quote15MinCurrentManger;
import com.pro.bityard.manger.Quote15MinHistoryManger;
import com.pro.bityard.manger.Quote1MinHistoryManger;
import com.pro.bityard.manger.Quote3MinCurrentManger;
import com.pro.bityard.manger.Quote3MinHistoryManger;
import com.pro.bityard.manger.Quote5MinCurrentManger;
import com.pro.bityard.manger.Quote5MinHistoryManger;
import com.pro.bityard.manger.Quote60MinCurrentManger;
import com.pro.bityard.manger.Quote60MinHistoryManger;
import com.pro.bityard.manger.SpotCodeManger;
import com.pro.bityard.manger.QuoteCurrentManger;
import com.pro.bityard.manger.QuoteDayCurrentManger;
import com.pro.bityard.manger.QuoteDayHistoryManger;
import com.pro.bityard.manger.QuoteMonthCurrentManger;
import com.pro.bityard.manger.QuoteMonthHistoryManger;
import com.pro.bityard.manger.QuoteSpotManger;
import com.pro.bityard.manger.QuoteWeekCurrentManger;
import com.pro.bityard.manger.QuoteWeekHistoryManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.manger.TradeListManger;
import com.pro.bityard.manger.TradeSpotManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.listQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.listQuotePrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;

public class SpotTradeActivity extends BaseActivity implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "all";
    private int lever;


    @BindView(R.id.img_star_spot)
    ImageView img_star_spot;

    @BindView(R.id.text_lever)
    TextView text_lever;


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


    @BindView(R.id.text_currency)
    TextView text_currency;


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

    @BindView(R.id.tabLayout_trade)
    TabLayout tabLayout_trade;
    @BindView(R.id.recyclerView_sell)
    RecyclerView recyclerView_sell;
    @BindView(R.id.recyclerView_buy)
    RecyclerView recyclerView_buy;

    @BindView(R.id.layout_commission_record)
    LinearLayout layout_commission_record;
    @BindView(R.id.recyclerView_trade)
    RecyclerView recyclerView_trade;
    @BindView(R.id.stay_view)
    View stay_view;
    private List<String> titles = new ArrayList<>();
    private List<String> titleTrade;
    private List<KData> kData1MinHistory, kData5MinHistory, kData15MinHistory, kData3MinHistory, kData60MinHistory, kDataDayHistory, kDataWeekHistory, kDataMonthHistory;


    private TradeListEntity tradeListEntity;
    private TextView text_deduction_amount_pop;
    private String prizeTrade = null;


    private boolean flag_optional = false;
    //当前行情号
    private String quote_code = null;
    private QuoteMinEntity quoteMinEntity;


    private SellBuyListAdapter sellAdapter, buyAdapter;


    // 声明平移动画
    private TranslateAnimation animation;
    private EditText edit_search;
    private JSONObject chargeUnitEntityJson;

    public static void enter(Context context, String tradeType, String data) {
        Intent intent = new Intent(context, SpotTradeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, data);
        bundle.putString(TYPE, tradeType);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }


    @Override
    protected int setContentLayout() {
        return R.layout.activity_trade_spot;
    }


    @Override
    protected void onResume() {
        super.onResume();
        BalanceManger.getInstance().getBalance("USDT");


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

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


        initTabViewTitle();


    }

    /*头部*/
    private void initTabViewTitle() {


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
        SpotCodeManger.getInstance().addObserver(this);


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
        QuoteSpotManger.getInstance().addObserver(this);
        TradeSpotManger.getInstance().addObserver(this);

        ChargeUnitManger.getInstance().addObserver(this);
        TagManger.getInstance().addObserver(this);


        findViewById(R.id.img_back).setOnClickListener(this);

        findViewById(R.id.layout_product).setOnClickListener(this);
        //加币 持仓
        findViewById(R.id.text_charge).setOnClickListener(this);


        findViewById(R.id.layout_buy).setOnClickListener(this);
        findViewById(R.id.layout_sell).setOnClickListener(this);


        //更多的监听
        findViewById(R.id.text_one_hour).setOnClickListener(this);
        findViewById(R.id.text_one_day).setOnClickListener(this);
        findViewById(R.id.text_one_week).setOnClickListener(this);
        findViewById(R.id.text_one_month).setOnClickListener(this);
        //自选监听
        findViewById(R.id.layout_optional).setOnClickListener(this);


        radioGroupAdapter = new RadioGroupAdapter(this);
        /*recyclerView_market.setAdapter(radioGroupAdapter);
        recyclerView_limit.setAdapter(radioGroupAdapter);*/


        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.getTabAt(i).setText(titles.get(i));
        }
        setTabStyle();

        setTabTrade();

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
                    //更多监听
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
    }

    private void setTabTrade() {
        titleTrade = new ArrayList<>();
        titleTrade.add(getResources().getString(R.string.text_record_commission));
        titleTrade.add(getResources().getString(R.string.text_new_trade));

        for (String market_name : titleTrade) {
            tabLayout_trade.addTab(tabLayout_trade.newTab().setText(market_name));
        }
        tabLayout_trade.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    layout_commission_record.setVisibility(View.VISIBLE);
                    recyclerView_trade.setVisibility(View.GONE);
                } else {
                    layout_commission_record.setVisibility(View.GONE);
                    recyclerView_trade.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sellAdapter = new SellBuyListAdapter(this);
        recyclerView_sell.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_sell.setAdapter(sellAdapter);

        buyAdapter = new SellBuyListAdapter(this);
        recyclerView_buy.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_buy.setAdapter(buyAdapter);


        tradeNewAdapter = new TradeNewAdapter(this);
        recyclerView_trade.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_trade.setAdapter(tradeNewAdapter);
    }


    private Set<String> optionalList;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        Log.d("print", "initData:590:  " + itemData);


        //礼金抵扣比例
        prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);

        if (itemData.equals("")) {
            return;
        }
        //根据合约还是现货跳转相应的页面
        String isChOrFt = TradeUtil.type(itemData);
        String zone = TradeUtil.zone(itemData);


        quote_code = itemQuoteContCode(itemData);
        Log.d("print", "initData:进来的值:  " + itemQuoteContCode(itemData));
        //自选的图标
        optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
        Log.d("print", "initData:707:  " + optionalList);
        if (optionalList.size() != 0) {
            //判断当前是否存在自选
            Util.isOptional(itemQuoteContCode(itemData), optionalList, response -> {
                boolean isOptional = (boolean) response;
                Toast.makeText(SpotTradeActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if (isOptional) {
                    img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                } else {
                    img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                }
            });
        } else {
            optionalList = new HashSet<>();
        }


        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));
        startScheduleJob(mHandler, QUOTE_SECOND, QUOTE_SECOND);

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
           /* Quote3MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote5MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote15MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            Quote60MinCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteDayCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteWeekCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
            QuoteMonthCurrentManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));*/

            //获取输入框的范围保证金
          /*  TradeListManger.getInstance().tradeList((state, response) -> {
                if (state.equals(SUCCESS)) {
                    tradeListEntityList = (List<TradeListEntity>) response;
                    tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
                    setContent(tradeListEntity);
                }
            });*/
            String string = SPUtils.getString(AppConfig.QUOTE_DETAIL, null);
            tradeListEntityList = Util.SPDealEntityResult(string);
            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);

            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    chargeUnitEntityJson = (JSONObject) response;
                    TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                    Log.d("print", "initData:673:  " + chargeUnitEntity);
                }
            });


        }, 50);


        text_lastPrice.setText(listQuotePrice(itemData));
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


    }


    private String old_code = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            //发送行情包
            if (quote_code != null) {
                Log.d("print", "handleMessage:activity订阅:  " + quote_code);
                old_code = quote_code;
                WebSocketManager.getInstance().send("6001", quote_code);


            }

        }
    };


    @Override
    protected void initEvent() {

    }

    private void setContent(String itemData) {
        Log.d("print", "setContent:313:  " + itemData);
        quote_code = itemQuoteContCode(itemData);


        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));

    }


    private List<String> titleList, titleListContract, optionalTitleList;
    private OptionalSelectAdapter optionalSelectAdapter;
    private boolean flag_new_price = false;
    private boolean flag_up_down = false;
    private boolean flag_name = false;
    private String type = AppConfig.CONTRACT_IN_ALL;
    private String zone_type = AppConfig.VIEW_CONTRACT_IN;//-1是自选 1是主区 0是创新区 2是衍生品
    private ArrayMap<String, List<String>> arrayMap;

    private QuoteAdapter quoteAdapter_market_pop;


    /*显示行情选择的按钮*/
    private void showQuotePopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.quote_market, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        titleList = new ArrayList<>();
        titleList.add(getString(R.string.text_optional));
        titleList.add(getString(R.string.text_contract));
        titleList.add(getString(R.string.text_derived));
        titleList.add(getString(R.string.text_spot));
        LinearLayout layout_optional_select_pop = view.findViewById(R.id.layout_optional_select_pop);

        RecyclerView recyclerView_optional_select_pop = view.findViewById(R.id.recyclerView_optional_pop);

        LinearLayout layout_null_pop = view.findViewById(R.id.layout_null);

        optionalSelectAdapter = new OptionalSelectAdapter(this);
        recyclerView_optional_select_pop.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView_optional_select_pop.setAdapter(optionalSelectAdapter);
        optionalTitleList = new ArrayList<>();
        optionalTitleList.add(getString(R.string.text_contract));
        optionalTitleList.add(getString(R.string.text_spot));
        //optionalTitleList.add(getString(R.string.text_derived));
        optionalSelectAdapter.setDatas(optionalTitleList);
        optionalSelectAdapter.select(getString(R.string.text_contract));
        optionalSelectAdapter.setEnable(true);


        TabLayout tabLayout_market_search = view.findViewById(R.id.tabLayout_market_search);

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

        /*自选的监听*/
        optionalSelectAdapter.setOnItemClick((position, data) -> {
            optionalSelectAdapter.select(data);
            switch (position) {
                case 0:
                    type = AppConfig.OPTIONAL_CONTRACT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_CONTRACT;

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null_pop.setVisibility(View.VISIBLE);
                        recyclerView_optional_select_pop.setVisibility(View.GONE);
                    } else {
                        layout_null_pop.setVisibility(View.GONE);
                        recyclerView_optional_select_pop.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 1:
                    type = AppConfig.OPTIONAL_SPOT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_SPOT;

                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null_pop.setVisibility(View.VISIBLE);
                        recyclerView_optional_select_pop.setVisibility(View.GONE);
                    } else {
                        layout_null_pop.setVisibility(View.GONE);
                        recyclerView_optional_select_pop.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
                case 2:
                    type = AppConfig.OPTIONAL_DERIVATIVES_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_DERIVATIVES;
                    quoteList = arrayMap.get(type);
                    if (quoteList == null) {
                        layout_null_pop.setVisibility(View.VISIBLE);
                        recyclerView_optional_select_pop.setVisibility(View.GONE);
                    } else {
                        layout_null_pop.setVisibility(View.GONE);
                        recyclerView_optional_select_pop.setVisibility(View.VISIBLE);
                        quoteAdapter_market_pop.setDatas(quoteList);
                    }
                    img_rate_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_name_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    img_price_triangle.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));
                    break;
            }
        });


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
                //自选
                if (tab.getPosition() == 0) {
                    layout_optional_select_pop.setVisibility(View.VISIBLE);
                    type = AppConfig.OPTIONAL_CONTRACT_ALL;
                    zone_type = AppConfig.VIEW_OPTIONAL_CONTRACT;
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
                }//合约
                else if (tab.getPosition() == 1) {
                    layout_optional_select_pop.setVisibility(View.GONE);

                    type = AppConfig.CONTRACT_IN_ALL;
                    zone_type = AppConfig.VIEW_CONTRACT_IN;

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
                }//衍生品
                else if (tab.getPosition() == 2) {
                    layout_optional_select_pop.setVisibility(View.GONE);

                    type = AppConfig.DERIVATIVES_ALL;
                    zone_type = AppConfig.VIEW_DERIVATIVES;

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
                }//现货
                else if (tab.getPosition() == 3) {
                    layout_optional_select_pop.setVisibility(View.GONE);

                    type = AppConfig.SPOT_ALL;
                    zone_type = AppConfig.VIEW_SPOT;

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

        });

        quoteAdapter_market_pop.setOnItemClick(data -> {

            Log.d("print", "showQuotePopWindow:1231:  " + data + "   " + old_code);
            setContent(data);
            itemData = data;
            quote_code = TradeUtil.itemQuoteContCode(data);
            WebSocketManager.getInstance().send("4002", old_code);
            SpotCodeManger.getInstance().postTag(data);

            type = AppConfig.CONTRACT_IN_ALL;


            //判断当前是否存在自选
            Util.isOptional(quote_code, optionalList, response -> {
                boolean isOptional = (boolean) response;
                if (isOptional) {
                    img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                } else {
                    img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));

                }
            });


            text_name.setText(TradeUtil.name(data));
            text_currency.setText(TradeUtil.currency(data));

            //相应选择
            popupWindow.dismiss();


        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            type = AppConfig.SPOT_ALL;
            tabLayout_market_search.getTabAt(AppConfig.selectPosition).select();
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
                    tabLayout_market_search.setVisibility(View.GONE);
                    tabLayout_market_search.getTabAt(AppConfig.selectPosition).select();
                    type = AppConfig.SPOT_ALL;
                    List<String> strings = arrayMap.get(type);
                    List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                    quoteAdapter_market_pop.setDatas(searchQuoteList);
                } else {
                    layout_bar.setVisibility(View.VISIBLE);
                    tabLayout_market_search.setVisibility(View.VISIBLE);
                    type = AppConfig.SPOT_ALL;
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

    @SuppressLint("NewApi")
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

            //自选的监听
            case R.id.layout_optional:
                optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
                Log.d("print", "onClick:自选目前: " + optionalList);
                //判断当前是否存在自选
                if (quoteMinEntity != null) {
                    if (optionalList.size() != 0) {
                        Util.isOptional(quoteMinEntity.getSymbol(), optionalList, response -> {
                            boolean isOptional = (boolean) response;
                            if (isOptional) {
                                img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                                optionalList.remove(quoteMinEntity.getSymbol());
                            } else {
                                img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                                optionalList.add(quoteMinEntity.getSymbol());
                            }
                        });
                    } else {
                        img_star_spot.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                        optionalList.add(quoteMinEntity.getSymbol());
                    }
                    SPUtils.putString(AppConfig.KEY_OPTIONAL, Util.SPDeal(optionalList));

                }
               /* if (quoteMinEntity != null) {
                    if (setList.contains(quoteMinEntity.getSymbol())) {
                        img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                        setList.remove(quoteMinEntity.getSymbol());
                    } else {
                        img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                        setList.add(quoteMinEntity.getSymbol());
                    }
                    SPUtils.putString(AppConfig.KEY_OPTIONAL, Util.SPDeal(setList));
                }*/


                break;


            case R.id.layout_buy:
            case R.id.layout_sell:
                if (isLogin()) {
                    finish();
                    SpotCodeManger.getInstance().postTag(itemData);
                } else {
                    LoginActivity.enter(SpotTradeActivity.this, IntentConfig.Keys.KEY_LOGIN);

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


            case R.id.text_charge:
                if (isLogin()) {
                    if (tradeType.equals("1")) {
                        WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/deposit"), getResources().getString(R.string.text_recharge));
                    } else {
                        NetManger.getInstance().addScore((state, response) -> {
                            if (state.equals(SUCCESS)) {
                                AddScoreEntity addScoreEntity = (AddScoreEntity) response;
                                Toast.makeText(SpotTradeActivity.this, addScoreEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    LoginActivity.enter(SpotTradeActivity.this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;

        }
    }


    private int oldSelect = 0;
    private int length = 0;
    private int count = 0;//控制限价价格显示 手动切换才变数据
    private String quote;
    private List<BuySellEntity> buyList;
    private List<BuySellEntity> sellList;

    private TradeNewAdapter tradeNewAdapter;
    private List<String> tradeList = new ArrayList<>();
    MyQueue myQueue = new MyQueue();
    private LinkedList list = new LinkedList();

    @Override
    public void update(Observable o, Object arg) {
        //买卖列表
        if (o == QuoteSpotManger.getInstance()) {

            quote = (String) arg;
            //Log.d("print", "update:现货activity买卖列表获取:  "+quote);

            runOnUiThread(() -> {
                buyList = Util.getBuyList(quote);
                buyAdapter.isSell(false);
                sellList = Util.getSellList(quote);
                sellAdapter.isSell(true);

                Collections.reverse(sellList);
                if (length == 0) {
                    buyAdapter.setDatas(buyList, Util.buyMax(quote));
                    sellAdapter.setDatas(sellList, Util.sellMax(quote));

                } else {
                    buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));
                    sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));
                }


            });

            //最新成交
        } else if (o == SpotCodeManger.getInstance()) {
            itemData = (String) arg;
            setContent(itemData);
            quote_code = itemQuoteContCode(itemData);

        } else if (o == TradeSpotManger.getInstance()) {
            String trade = (String) arg;

            runOnUiThread(() -> {
                list.addFirst(trade);
                tradeNewAdapter.setDatas(list);
            });




        } else if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);
            if (quoteList != null && quoteAdapter_market_pop != null) {
                runOnUiThread(() -> {
                    //搜索框
                    if (edit_search.getText().toString().equals("")) {
                        quoteAdapter_market_pop.setDatas(quoteList);
                    } else {
                        List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), quoteList);
                        quoteAdapter_market_pop.setDatas(searchQuoteList);
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

        } else if (o == TradeListManger.getInstance()) {
            tradeListEntityList = (List<TradeListEntity>) arg;
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
        } else if (o == QuoteCurrentManger.getInstance()) {
            quoteMinEntity = (QuoteMinEntity) arg;
            if (quoteMinEntity != null) {
                Log.d("print", "update:1549:现货activity行情:" + quoteMinEntity);
                runOnUiThread(() -> {
                    if (quoteMinEntity.getSymbol().equals(quote_code)) {
                        //    Toast.makeText(QuoteDetailActivity.this, quoteMinEntity.getSymbol() + "    " + quote_code, Toast.LENGTH_SHORT).show();
                        //仓位实时更新 服务费


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
                        text_volume.setText(TradeUtil.justDisplay(quoteMinEntity.getVolume()));


                        String spread = TradeUtil.spread(quoteMinEntity.getSymbol(), tradeListEntityList);


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
        Toast.makeText(SpotTradeActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
        //要取消计时 防止内存溢出
        cancelTimer();
      //  QuoteCurrentManger.getInstance().clear();
        SocketQuoteManger.getInstance().deleteObserver(this);
        TradeSpotManger.getInstance().clear();
        TradeSpotManger.getInstance().deleteObserver(this);
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


        quote_code = null;


    }


}
