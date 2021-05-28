package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.SpotTradeActivity;
import com.pro.bityard.activity.TradeTabActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.activity.WebActivity;
import com.pro.bityard.adapter.OptionalSelectAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.adapter.RadioGroupAdapter;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.chart.KData;
import com.pro.bityard.chart.NoVolumeView;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.AddScoreEntity;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.ChargeUnitEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.QuoteChartEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.NoticeManger;
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
import com.pro.bityard.manger.QuoteCodeManger;
import com.pro.bityard.manger.QuoteContractCurrentManger;
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
import com.pro.bityard.utils.SocketUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.listQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.listQuotePrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.marginMax;
import static com.pro.bityard.utils.TradeUtil.marginMin;
import static com.pro.bityard.utils.TradeUtil.marginOrder;
import static java.lang.Double.parseDouble;

public class ContractTradeFragment extends BaseFragment implements Observer, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "all";
    private int lever;
    private DecimalEditText edit_lever;
    private SeekBar seekBar_lever;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_contract;
    }

    public ContractTradeFragment newInstance(String type, String value) {
        ContractTradeFragment fragment = new ContractTradeFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }



    @BindView(R.id.layout_switch)
    LinearLayout layout_switch;
    @BindView(R.id.img_star_contract)
    ImageView img_star_contract;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    private String orderType = "0"; //市价=0 限价=1
    private boolean isDefer; //是否递延
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;

    @BindView(R.id.text_name)
    TextView text_name;

    @BindView(R.id.text_currency)
    TextView text_currency;


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

    private Set<String> optionalList;


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
    private String prizeTrade = null, luckyTrade = null;

    @BindView(R.id.text_market_currency)
    TextView text_market_currency;

    @BindView(R.id.text_limit_currency)
    TextView text_limit_currency;

    //当前行情号
    private String quote_code = null, quote_code_old = null;

    private QuoteMinEntity quoteMinEntity;


    // 声明平移动画
    private TranslateAnimation animation;
    private EditText edit_search;
    private JSONObject chargeUnitEntityJson;

    private List<String> titleList, titleListContract, optionalTitleList;
    private OptionalSelectAdapter optionalSelectAdapter;
    private boolean flag_new_price = false;
    private boolean flag_up_down = false;
    private boolean flag_name = false;
    private String type = AppConfig.CONTRACT_ALL;
    private String zone_type = AppConfig.VIEW_CONTRACT;//-1是自选 1是主区 0是创新区 2是衍生品
    private ArrayMap<String, List<String>> arrayMap;

    private QuoteAdapter quoteAdapter_market_pop;

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    /*显示行情选择的按钮*/
    private void showQuotePopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.quote_market, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        titleList = new ArrayList<>();
        titleList.add(getString(R.string.text_optional));
        titleList.add(getString(R.string.text_contract));
        // titleList.add(getString(R.string.text_derived));
        titleList.add(getString(R.string.text_spot));
        LinearLayout layout_optional_select_pop = view.findViewById(R.id.layout_optional_select_pop);

        RecyclerView recyclerView_optional_select_pop = view.findViewById(R.id.recyclerView_optional_pop);

        LinearLayout layout_null_pop = view.findViewById(R.id.layout_null);

        optionalSelectAdapter = new OptionalSelectAdapter(getActivity());
        recyclerView_optional_select_pop.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView_optional_select_pop.setAdapter(optionalSelectAdapter);
        optionalTitleList = new ArrayList<>();
        optionalTitleList.add(getString(R.string.text_contract));
        optionalTitleList.add(getString(R.string.text_spot));
        //optionalTitleList.add(getString(R.string.text_derived));3
        optionalSelectAdapter.setDatas(optionalTitleList);
        optionalSelectAdapter.select(getString(R.string.text_contract));
        optionalSelectAdapter.setEnable(true);


        TabLayout tabLayout_market_search = view.findViewById(R.id.tabLayout_market_search);

        LinearLayout layout_null = view.findViewById(R.id.layout_null);

        RecyclerView recyclerView_market = view.findViewById(R.id.recyclerView_market);

        quoteAdapter_market_pop = new QuoteAdapter(getActivity());
        recyclerView_market.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_market.setAdapter(quoteAdapter_market_pop);
        String quoteJson = SPUtils.getString(AppConfig.QUOTE_LIST, null);
        List<String> quote_list = Util.SPDealStringResult(quoteJson);
        if (quoteList == null) {
            quoteAdapter_market_pop.setDatas(quote_list);
        } else {
            quoteAdapter_market_pop.setDatas(quoteList);
        }
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

                    type = AppConfig.CONTRACT_ALL;
                    zone_type = AppConfig.VIEW_CONTRACT;

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
                /*else if (tab.getPosition() == 2) {
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
                }*///现货
                else if (tab.getPosition() == 2) {
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


        quote_code_old = quote_code;
        quoteAdapter_market_pop.setOnItemClick(data -> {
            showProgressDialog();
            SocketUtil.switchQuotesList("3002");
            quote_code = TradeUtil.itemQuoteContCode(data);
            Log.d("print", "showQuotePopWindow:644:  " + quote_code_old + "   " + quote_code);
            if (TradeUtil.type(data).equals(AppConfig.TYPE_FT)) {
                if (!quote_code_old.equals(quote_code)) {
                    WebSocketManager.getInstance().cancelQuotes("4002", quote_code_old);
                }
                QuoteCodeManger.getInstance().postTag(data);
                WebSocketManager.getInstance().sendQuotes("4001", quote_code, "1");
                type = AppConfig.CONTRACT_ALL;
                TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                Log.d("print", "showQuotePopWindow:1201:  " + itemQuoteCode(quote_code) + "                 " + chargeUnitEntity);
                //判断当前是否存在自选
                Util.isOptional(quote_code, optionalList, response -> {
                    boolean isOptional = (boolean) response;
                    if (isOptional) {
                        img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                    } else {
                        img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                    }
                });


                text_market_currency.setText(TradeUtil.currency(data));
                text_limit_currency.setText(TradeUtil.currency(data));

                text_name.setText(TradeUtil.name(data));
                text_currency.setText(TradeUtil.currency(data));

                edit_limit_price.setDecimalEndNumber(TradeUtil.decimalPoint(listQuotePrice(data)));//根据不同的小数位限制
                edit_limit_price.setText(listQuotePrice(data));


                Quote1MinHistoryManger.getInstance().quote(quote_code, -2);
                Quote3MinHistoryManger.getInstance().quote(quote_code, -2);
                Quote5MinHistoryManger.getInstance().quote(quote_code, -2);
                Quote15MinHistoryManger.getInstance().quote(quote_code, -2);
                Quote60MinHistoryManger.getInstance().quote(quote_code, -2);
                QuoteDayHistoryManger.getInstance().quote(quote_code, -2);
                QuoteWeekHistoryManger.getInstance().quote(quote_code, -2);
                QuoteMonthHistoryManger.getInstance().quote(quote_code, -2);

                recyclerView_market.postDelayed(() -> resetChart(), 0);


                //相应选择
                tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(data), tradeListEntityList);

                setContent(tradeListEntity);

            } else if (TradeUtil.type(data).equals(AppConfig.TYPE_CH)) {
                //SpotCodeManger.getInstance().postTag(data);
                WebSocketManager.getInstance().cancelQuotes("4002", quote_code_old);
                SpotTradeActivity.enter(getActivity(), "1", data);
                getActivity().finish();

            }

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


        Util.dismiss(getActivity(), popupWindow);
        Util.isShowing(getActivity(), popupWindow);


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
    public void onResume() {
        super.onResume();
        Log.d("progress", "onResume: "+"Contract onResume");

        BalanceManger.getInstance().getBalance("USDT");
        //WebSocketManager.getInstance().sendQuotes("4001", quote_code,"1");

        if (isLogin()) {
            layout_trade.setVisibility(View.VISIBLE);
            //stay_view.setVisibility(View.VISIBLE);
            //获取持仓数

        } else {
            layout_trade.setVisibility(View.GONE);
            //stay_view.setVisibility(View.GONE);

        }


    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        //showProgressDialog();
        initTabView(view);
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        tradeType = getArguments().getString(TYPE);
        itemData = getArguments().getString(VALUE);
        if (tradeType.equals("1")) {
            text_switch.setText(getResources().getText(R.string.text_real_trade));

        } else if (tradeType.equals("2")) {
            text_switch.setText(getResources().getText(R.string.text_simulation_trade));

        }

        //礼金抵扣比例
        prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);
        //红包抵扣比例
        luckyTrade = SPUtils.getString(AppConfig.LUCKY_TRADE, null);
        if (itemData.equals("")) {
            return;
        }
        //根据合约还是现货跳转相应的页面
        String isChOrFt = TradeUtil.type(itemData);
        String zone = TradeUtil.zone(itemData);


        quote_code = itemQuoteContCode(itemData);

        //自选的图标
        optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
        Util.setOptional(getActivity(), optionalList, quote_code, img_star_contract, response -> {
            if (!(boolean) response) {
                optionalList = new HashSet<>();
            }
        });

        Log.d("print", "initData:合约进来的值:  " + itemQuoteContCode(itemData) + "  " + itemData + "  " + TradeUtil.name(itemData));
        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));


        text_market_currency.setText(TradeUtil.currency(itemData));
        text_limit_currency.setText(TradeUtil.currency(itemData));

        startScheduleJob(mHandler, QUOTE_SECOND, QUOTE_SECOND);

        Handler handler = new Handler();
        handler.postDelayed(() -> {


            Quote1MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote5MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote15MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote3MinHistoryManger.getInstance().quote(quote_code, -2);
            Quote60MinHistoryManger.getInstance().quote(quote_code, -2);
            QuoteDayHistoryManger.getInstance().quote(quote_code, -2);
            QuoteWeekHistoryManger.getInstance().quote(quote_code, -2);
            QuoteMonthHistoryManger.getInstance().quote(quote_code, -2);


            String string = SPUtils.getString(AppConfig.QUOTE_DETAIL, null);
            tradeListEntityList = Util.SPDealEntityResult(string);

            tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            Log.d("print", "initData:864:  " + tradeListEntity);

            setContent(tradeListEntity);

            //获取输入框的范围保证金
            /*TradeListManger.getInstance().tradeList((state, response) -> {
                if (state.equals(SUCCESS)) {
                    tradeListEntityList = (List<TradeListEntity>) response;
                    tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
                    setContent(tradeListEntity);
                }
            });*/
            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    chargeUnitEntityJson = (JSONObject) response;
                    TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                }
            });


        }, 50);


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
                text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
                text_change.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
                text_range.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
              //  img_up_down.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_market_down));

                break;
            case "1":
                text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
                text_change.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
                text_range.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
             //   img_up_down.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_market_up));

                break;
            case "0":
                text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));
                text_change.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));
                text_range.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + listQuoteIsRange(itemData));
        }
        //可用余额
        if (tradeType.equals("1")) {
            text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()) + " " + getResources().getString(R.string.text_usdt));
            text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()) + " " + getResources().getString(R.string.text_usdt));
        } else {
            text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()) + " " + getResources().getString(R.string.text_usdt));
            text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()) + " " + getResources().getString(R.string.text_usdt));
        }


    }


    /*private void getPositionSize() {
        NetManger.getInstance().getHold(tradeType, (state, response1, response2) -> {
            if (state.equals(SUCCESS)) {
                PositionEntity positionEntity = (PositionEntity) response1;
                int size = positionEntity.getData().size();
                if (size == 0) {
                    if (text_position_size != null) {
                        text_position_size.setVisibility(View.GONE);
                        text_position.setTextColor(activity.getResources().getColor(R.color.text_second_color));
                        text_position.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_position), null, null);

                    }
                } else {
                    text_position_size.setVisibility(View.VISIBLE);
                    text_position_size.setText(size + "");
                    text_position.setTextColor(activity.getResources().getColor(R.color.maincolor));
                   // text_position.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_position_yellow), null, null);
                    text_position.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_position), null, null);

                }
            }
        });
    }*/

    private void initTabView(View view) {
        titles.add("Line");
        titles.add("1min");
        titles.add("3min");
        titles.add("5min");
        titles.add("15min");
        titles.add(getResources().getString(R.string.text_more));
        //QuoteCodeManger.getInstance().addObserver(this);


        SocketQuoteManger.getInstance().addObserver(this);
        BalanceManger.getInstance().addObserver(this);
        //QuoteItemManger.getInstance().addObserver(this);

        QuoteContractCurrentManger.getInstance().addObserver(this);//1min 实时
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


        view.findViewById(R.id.img_setting).setOnClickListener(this);
        view.findViewById(R.id.layout_product).setOnClickListener(this);
        //加币 持仓
        view.findViewById(R.id.text_position).setOnClickListener(this);
        view.findViewById(R.id.text_charge).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);

        view.findViewById(R.id.layout_much).setOnClickListener(this);
        view.findViewById(R.id.layout_empty).setOnClickListener(this);
        view.findViewById(R.id.layout_switch).setOnClickListener(this);


        //更多的监听
        view.findViewById(R.id.text_one_hour).setOnClickListener(this);
        view.findViewById(R.id.text_one_day).setOnClickListener(this);
        view.findViewById(R.id.text_one_week).setOnClickListener(this);
        view.findViewById(R.id.text_one_month).setOnClickListener(this);
        view.findViewById(R.id.text_rule).setOnClickListener(this);
        //自选监听
        view.findViewById(R.id.layout_optional).setOnClickListener(this);


        radioGroupAdapter = new RadioGroupAdapter(getActivity());
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

        tabLayout.getTabAt(1).select();

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

                        if (kData1MinHistory != null) {
                            kline_1min_time.initKDataList(kData1MinHistory);
                        } else {
                            if (quoteMinEntity != null) {
                                Quote1MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                            }
                        }

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
                        Quote1MinHistoryManger.getInstance().quote(quote_code, -2);

                        if (kData1MinHistory != null) {
                            myKLineView_1Min.initKDataList(kData1MinHistory);
                        } else {
                            if (quoteMinEntity != null) {
                                Quote1MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                            }
                        }

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
                        Quote3MinHistoryManger.getInstance().quote(quote_code, -2);

                        if (kData3MinHistory != null) {
                            myKLineView_3Min.initKDataList(kData3MinHistory);
                        } else {
                            if (quoteMinEntity != null) {

                                Quote3MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                            }
                        }

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

                        Quote5MinHistoryManger.getInstance().quote(quote_code, -2);

                        if (kData5MinHistory != null) {
                            myKLineView_5Min.initKDataList(kData5MinHistory);
                        } else {
                            if (quoteMinEntity != null) {

                                Quote5MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                            }
                        }

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
                        Quote15MinHistoryManger.getInstance().quote(quote_code, -2);

                        if (kData15MinHistory != null) {
                            myKLineView_15Min.initKDataList(kData15MinHistory);
                        } else {
                            if (quoteMinEntity != null) {

                                Quote15MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                            }
                        }

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
        view.findViewById(R.id.layout_market_lever_select).setOnClickListener(this);
        view.findViewById(R.id.layout_limit_lever_select).setOnClickListener(this);
    }

    private void setTabStyle() {
        for (int i = 0; i < titles.size(); i++) {//根据Tab数量循环来设置
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_title_layout, null);
                TextView text_title_one = view.findViewById(R.id.text_title);
                text_title_one.setText(titles.get(i));
                ImageView img_subscript = view.findViewById(R.id.img_subscript);

                if (i == 1) {
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

    /*设置 保证金和杠杆*/
    public void setContent(TradeListEntity tradeListEntity) {
        //   Log.d("print", "setContent:659:  " + tradeListEntity);
        if (tradeListEntity != null) {
            List<String> leverShowList = tradeListEntity.getLeverShowList();
            if (leverShowList.size() == 0) {
                return;
            }
            lever = Integer.parseInt(leverShowList.get(oldSelect));
            text_lever_market.setText(lever + "");
            text_lever_limit.setText(lever + "");


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
                Log.d("print", "handleMessage:合约fragment订阅: " + quote_code);

                String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
                Quote3MinCurrentManger.getInstance().quote(quote_host, quote_code);
                Quote5MinCurrentManger.getInstance().quote(quote_host, quote_code);
                Quote15MinCurrentManger.getInstance().quote(quote_host, quote_code);
                Quote60MinCurrentManger.getInstance().quote(quote_host, quote_code);
                QuoteDayCurrentManger.getInstance().quote(quote_host, quote_code);
                QuoteWeekCurrentManger.getInstance().quote(quote_host, quote_code);
                QuoteMonthCurrentManger.getInstance().quote(quote_host, quote_code);
            }

        }
    };

    private int oldSelect = 0;

    //选择杠杆
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showLeverWindow(TradeListEntity tradeListEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_lever_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_market);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(radioGroupAdapter);
        LinearLayout layout_lever_slide = view.findViewById(R.id.layout_lever_slide);
        edit_lever = view.findViewById(R.id.edit_lever);
        seekBar_lever = view.findViewById(R.id.bar_lever);

        TextView text_lever_min = view.findViewById(R.id.text_lever_min);
        TextView text_lever_max = view.findViewById(R.id.text_lever_max);

        TextView text_sub_lever = view.findViewById(R.id.text_sub_lever);

        TextView text_add_lever = view.findViewById(R.id.text_add_lever);


        lever = Integer.parseInt(text_lever_limit.getText().toString());

        seekBar_lever.post(() -> {
            seekBar_lever.setProgress(lever);
        });

        edit_lever.setText(String.valueOf(lever));


        RadioGroup radioGroup_lever = view.findViewById(R.id.radioGroup_lever);
        radioGroup_lever.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_quick:
                    recyclerView.setVisibility(View.VISIBLE);
                    layout_lever_slide.setVisibility(View.GONE);
                    break;
                case R.id.radio_slide:
                    recyclerView.setVisibility(View.GONE);
                    layout_lever_slide.setVisibility(View.VISIBLE);
                    break;
            }
        });


        if (tradeListEntity != null) {
            List<String> leverShowList = tradeListEntity.getLeverShowList();
            List<String> leverList = tradeListEntity.getLeverList();
            seekBar_lever.getThumb().setColorFilter(Color.parseColor("#FFB628"), PorterDuff.Mode.SRC_ATOP);

            seekBar_lever.setMin(Integer.parseInt(leverList.get(0)));
            seekBar_lever.setMax(Integer.parseInt(leverList.get(1)));
            text_lever_min.setText(String.valueOf(leverList.get(0)));
            text_lever_max.setText(String.valueOf(leverList.get(1)));

            text_sub_lever.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TradeUtil.subLeverMyself(edit_lever, 1);
                    lever = Integer.parseInt(edit_lever.getText().toString());
                    setSlide(false, lever);

                }
            });

            text_add_lever.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TradeUtil.addLeverMyself(edit_lever, 1);
                    lever = Integer.parseInt(edit_lever.getText().toString());
                    setSlide(false, lever);
                }
            });
            seekBar_lever.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        edit_lever.setText(progress + "");
                        setSlide(true, progress);


                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            radioGroupAdapter.setDatas(leverShowList);
            //自动选择相应的
            radioGroupAdapter.selectLever(String.valueOf(lever));


            radioGroupAdapter.setOnItemClick((position, data) -> {
                lever = data;
                oldSelect = position;
                radioGroupAdapter.select(position);
                recyclerView.setAdapter(radioGroupAdapter);
                radioGroupAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
                setLeverContent(tradeListEntity);


            });
        }
        Util.dismiss(getActivity(), popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_pop_market, Gravity.CENTER, 0, 0);
    }


    public void setSlide(boolean isSeekBar, int progress) {
        text_lever_limit.setText(String.valueOf(progress));
        text_lever_market.setText(String.valueOf(progress));
        lever = progress;
        double maxHoldOne = tradeListEntity.getMaxHoldOne();
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
        if (!isSeekBar) {
            seekBar_lever.post(() -> {
                seekBar_lever.setProgress(lever);
            });
        }

        radioGroupAdapter.selectLever(String.valueOf(lever));
    }

    /*设置 保证金和杠杆*/
    public void setLeverContent(TradeListEntity tradeListEntity) {
        if (tradeListEntity != null) {
            List<String> leverShowList = tradeListEntity.getLeverShowList();
            lever = Integer.parseInt(leverShowList.get(oldSelect));
            text_lever_market.setText(lever + "");
            text_lever_limit.setText(lever + "");
            edit_limit_margin.setText("");
            edit_market_margin.setText("");
            text_market_volume.setText(getString(R.string.text_default));
            text_limit_volume.setText(getString(R.string.text_default));
            text_market_all.setText(getString(R.string.text_default));
            text_limit_all.setText(getString(R.string.text_default));
            double maxHoldOne = tradeListEntity.getMaxHoldOne();
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

    /*下单提示*/
    @SuppressLint("SetTextI18n")
    private void showTip(String isBuy, String margin, String service) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_much_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_margin = view.findViewById(R.id.text_margin_pop);
        text_margin.setText(margin + " " + getString(R.string.text_usdt));
        RelativeLayout layout_deduction = view.findViewById(R.id.layout_deduction);

        TextView text_service = view.findViewById(R.id.text_service_pop);
        text_service.setText(TradeUtil.numberHalfUp(Double.parseDouble(service), 2) + " " + getString(R.string.text_usdt));
        TextView text_all = view.findViewById(R.id.text_all_pop);
        text_deduction_amount_pop = view.findViewById(R.id.text_deduction_amount_pop);
        if (Double.parseDouble(TradeUtil.deductionResult(service, margin, prizeTrade, luckyTrade)) == 0) {
            layout_deduction.setVisibility(View.GONE);
        }


        text_deduction_amount_pop.setText(TradeUtil.deductionResult(service, margin, prizeTrade, luckyTrade) + " " + getString(R.string.text_usdt));

        text_all.setText(TradeUtil.total(margin, service, TradeUtil.deductionResult(service, margin, prizeTrade, luckyTrade)) + getString(R.string.text_usdt));

        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            String priceMuch = text_buy_much.getText().toString();
            fastOpen(isBuy, priceMuch);
            popupWindow.dismiss();
        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> popupWindow.dismiss());

        Util.dismiss(getActivity(), popupWindow);
        Util.isShowing(getActivity(), popupWindow);

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
            Toast.makeText(getActivity(), getResources().getText(R.string.text_margin_input), Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceOrder == null) {
            Toast.makeText(getActivity(), getResources().getText(R.string.text_limit_price_input), Toast.LENGTH_SHORT).show();
            return;
        }

        TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);

       /* TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityJson, new OnResult() {
            @Override
            public void setResult(Object response) {
                chargeUnitEntity= (ChargeUnitEntity) response;

            }
        });*/


        if (chargeUnitEntity == null) {
            Toast.makeText(getActivity(), R.string.text_failure, Toast.LENGTH_SHORT).show();
            return;
        } else {
            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    chargeUnitEntityJson = (JSONObject) response;
                    TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                }
            });
        }


        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);
        Util.lightOff(getActivity());
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
            Toast.makeText(getActivity(), getResources().getText(R.string.text_margin_input), Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceOrder == null) {
            Toast.makeText(getActivity(), getResources().getText(R.string.text_limit_price_input), Toast.LENGTH_SHORT).show();
            return;
        }
        TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);

        if (chargeUnitEntity == null) {
            Toast.makeText(getActivity(), R.string.text_failure, Toast.LENGTH_SHORT).show();
            return;
        } else {
            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    chargeUnitEntityJson = (JSONObject) response;
                    TradeUtil.chargeDetail(itemQuoteCode(quote_code), chargeUnitEntityJson, response1 -> chargeUnitEntity = (ChargeUnitEntity) response1);
                }
            });
        }
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);


        String stopProfit = SPUtils.getString(AppConfig.VALUE_PROFIT, "3");
        String stopLoss = SPUtils.getString(AppConfig.VALUE_LOSS, "-0.9");
        NetManger.getInstance().contractOpen(tradeType, "2", tradeListEntity.getCode(),
                tradeListEntity.getContractCode(), isBuy, margin, String.valueOf(lever), priceOrder, defer,
                TradeUtil.deferFee(priceDigit, defer, tradeListEntity.getDeferFee(), margin, lever), stopProfit, stopLoss, serviceCharge,
                "0", TradeUtil.volume(lever, margin, parseDouble(priceMuchOrEmpty)), "0", "USDT", (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                        PositionRealManger.getInstance().getHold();
                        PositionSimulationManger.getInstance().getHold();
                        BalanceManger.getInstance().getBalance("USDT");
                        //getPositionSize();
                        NoticeManger.getInstance().notice();

                    } else if (state.equals(FAILURE)) dismissProgressDialog();
                }

        );
    }


    private void setItemQuote(QuoteMinEntity quoteMinEntity) {
        //    Toast.makeText(QuoteDetailActivity.this, quoteMinEntity.getSymbol() + "    " + quote_code, Toast.LENGTH_SHORT).show();
        //仓位实时更新 服务费
        if (edit_market_margin == null) {
            return;
        }
        if (Objects.requireNonNull(edit_market_margin.getText()).length() != 0) {
            text_market_volume.setText(TradeUtil.volume(lever, edit_market_margin.getText().toString(), quoteMinEntity.getPrice()));
            String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, edit_market_margin.getText().toString(), lever);
            // Log.d("print", "update:服务费:  " +chargeUnitEntity +"            "+service);
            if (prizeTrade != null && service != null) {
                text_market_all.setText(TradeUtil.total(edit_market_margin.getText().toString(),
                        service,
                        TradeUtil.deductionResult(service, edit_market_margin.getText().toString(), prizeTrade, luckyTrade)) + " " + getResources().getString(R.string.text_usdt));
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
                        TradeUtil.deductionResult(service, edit_limit_margin.getText().toString(), prizeTrade, luckyTrade)) + " " + getResources().getString(R.string.text_usdt));
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
            text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
            text_change.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
            text_range.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));

            img_up_down.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_market_down));

        } else if (quoteMinEntity.getIsUp() == 1) {
            text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
            text_change.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
            text_range.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
            img_up_down.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.icon_market_up));

        } else if (quoteMinEntity.getIsUp() == 0) {

            text_lastPrice.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));
            text_change.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));
            text_range.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));

        }
        text_max.setText(String.valueOf(quoteMinEntity.getMax()));
        text_min.setText(String.valueOf(quoteMinEntity.getMin()));
        text_volume.setText(TradeUtil.justDisplay(quoteMinEntity.getVolume()));


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


    @Override
    public void update(Observable o, Object arg) {
       /* if (o == QuoteCodeManger.getInstance()) {
            itemData = (String) arg;
            Log.d("print", "update:1631: " + itemData);
            runOnUiThread(() -> {
                text_name.setText(TradeUtil.name(itemData));
                text_currency.setText(TradeUtil.currency(itemData));
                text_market_currency.setText(TradeUtil.currency(itemData));
                text_limit_currency.setText(TradeUtil.currency(itemData));
            });

        } else*/
        if (o == SocketQuoteManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
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
            if (!isAdded()) {
                return;
            }

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
                text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()) + " " + getResources().getString(R.string.text_usdt));
                text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()) + " " + getResources().getString(R.string.text_usdt));
            } else {
                text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()) + " " + getResources().getString(R.string.text_usdt));
                text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()) + " " + getResources().getString(R.string.text_usdt));
            }

        } /*else if (o == TradeListManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            tradeListEntityList = (List<TradeListEntity>) arg;
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            setContent(tradeListEntity);
        }*/ else if (o == QuoteContractCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            quoteMinEntity = (QuoteMinEntity) arg;
            if (quoteMinEntity != null) {
                Log.d("print", "onReceive:1549:QuoteContractCurrentManger 合约:  " + quoteMinEntity);
                runOnUiThread(() -> {
                    if (quoteMinEntity.getSymbol().equals(quote_code)) {
                        dismissProgressDialog();
                        setItemQuote(quoteMinEntity);
                    }
                });


            }


        } else if (o == Quote3MinCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }

            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData3MinHistory != null && quoteMinEntity != null && myKLineView_3Min != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_3Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {
                    Quote3MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        } else if (o == Quote5MinCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);

            if (kData5MinHistory != null && quoteMinEntity != null && myKLineView_5Min != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_5Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    Quote5MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == Quote15MinCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData15MinHistory != null && quoteMinEntity != null && myKLineView_15Min != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_15Min.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    Quote15MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == Quote60MinCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kData60MinHistory != null && quoteMinEntity != null && myKLineView_1H != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1H.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {
                    Quote60MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }

            }
        } else if (o == QuoteDayCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataDayHistory != null && quoteMinEntity != null && myKLineView_1D != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1D.addSingleData(kData.get(kData.size() - 1));
            } else {
                if (quoteMinEntity != null) {

                    QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        } else if (o == QuoteWeekCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataWeekHistory != null && quoteMinEntity != null && myKLineView_1_week != null) {
                kData.get(kData.size() - 1).setClosePrice(quoteMinEntity.getPrice());
                myKLineView_1_week.addSingleData(kData.get(kData.size() - 1));


            } else {
                if (quoteMinEntity != null) {

                    QuoteWeekHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }
        } else if (o == QuoteMonthCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteChartEntity data = (QuoteChartEntity) arg;
            List<KData> kData = ChartUtil.klineList(data);
            if (kDataMonthHistory != null && quoteMinEntity != null && myKLineView_1_month != null) {
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
            Log.d("print", "klineList:1846: " + data.getT().size() + " " + data.getO().size() + " " + data.getC().size() + " " + data.getH().size() + " " + data.getL().size() + " " + data.getV().size());

            if (!isAdded()) {
                return;
            }


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
            if (!isAdded()) {
                return;
            }
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
            if (!isAdded()) {
                return;
            }
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
            if (!isAdded() && data == null) {
                return;
            }
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
            if (!isAdded()) {
                return;
            }
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
            if (!isAdded() && data == null) {
                return;
            }
            kDataDayHistory = ChartUtil.klineList(data);

            if (kDataDayHistory != null) {
                myKLineView_1D.initKDataList(kDataDayHistory);

            } else {
                if (quoteMinEntity != null) {

                    QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                }
            }

        } else if (o == QuoteWeekHistoryManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
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
            if (!isAdded()) {
                return;
            }
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                layout_market_price.setVisibility(View.VISIBLE);
                layout_limit_price.setVisibility(View.GONE);
                text_buy_much.setVisibility(View.VISIBLE);
                text_buy_empty.setVisibility(View.VISIBLE);
                radio_btn0.setTextSize(15);
                radio_btn1.setTextSize(13);
                orderType = "0";

                break;
            case R.id.radio_1:
                layout_market_price.setVisibility(View.GONE);
                layout_limit_price.setVisibility(View.VISIBLE);
                text_buy_much.setVisibility(View.GONE);
                text_buy_empty.setVisibility(View.GONE);
                radio_btn0.setTextSize(13);
                radio_btn1.setTextSize(15);
                orderType = "1";
                break;


        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("progress", "onStart: "+"Contract onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("progress", "onStop: "+"Contract onStop");

    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("progress", "onPause: "+"Contract onPause");

        SocketUtil.switchQuotesList("3002");
        WebSocketManager.getInstance().cancelQuotes("4002", quote_code);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("progress", "onDestroy: "+"Contract onDestroy");



        cancelTimer();
       /* QuoteContractCurrentManger.getInstance().clear();
        SocketQuoteManger.getInstance().deleteObserver(this);*/
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
        if (myKLineView_1Min != null) {
            myKLineView_1Min.cancelQuotaThread();
            myKLineView_5Min.cancelQuotaThread();
            myKLineView_15Min.cancelQuotaThread();
            myKLineView_3Min.cancelQuotaThread();
            myKLineView_1H.cancelQuotaThread();
            myKLineView_1D.cancelQuotaThread();
            myKLineView_1_week.cancelQuotaThread();
            myKLineView_1_month.cancelQuotaThread();
        }


       // WebSocketManager.getInstance().cancelQuotes("4002", quote_code);
        quote_code = null;

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        TabLayout.Tab tabAt = tabLayout.getTabAt(5);
        View view = tabAt.getCustomView();
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);

        switch (v.getId()) {
            case R.id.layout_product:
                Util.lightOff(getActivity());
                SocketUtil.switchQuotesList("3001");
                showQuotePopWindow();
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
                    text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()));
                    text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceReal()));
                } else {
                    text_market_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()));
                    text_limit_balance.setText(TradeUtil.justDisplay(BalanceManger.getInstance().getBalanceSim()));
                }
                NoticeManger.getInstance().notice();
               // getPositionSize();
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
                                img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                                optionalList.remove(quoteMinEntity.getSymbol());
                            } else {
                                img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                                optionalList.add(quoteMinEntity.getSymbol());
                            }
                        });
                    } else {
                        img_star_contract.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
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


            case R.id.img_setting:
                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_TRADE_SETTINGS);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
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
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);

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
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;


            case R.id.text_market_all:
                String value_margin = edit_market_margin.getText().toString();
                if (value_margin.equals("")) {
                    value_margin = "0.0";
                }
                Util.lightOff(getActivity());
                if (prizeTrade != null) {
                    String service = TradeUtil.serviceCharge(chargeUnitEntity, 3, value_margin, lever);
                    String prizeDub = TradeUtil.deductionResult(service, value_margin, prizeTrade, luckyTrade);
                    PopUtil.getInstance().showLongTip(getActivity(),
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
                    String prizeDub = TradeUtil.deductionResult(service, value_margin_limit, prizeTrade, luckyTrade);
                    Util.lightOff(getActivity());
                    PopUtil.getInstance().showLongTip(getActivity(),
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
                    WebSocketManager.getInstance().cancelQuotes("4002", quote_code);
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_HOLD);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.text_one_hour:

                Quote60MinHistoryManger.getInstance().quote(quote_code, -2);

                if (kData60MinHistory != null) {

                    myKLineView_1H.initKDataList(kData60MinHistory);
                } else {
                    if (quoteMinEntity != null) {

                        Quote60MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                    }
                }


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


                if (kDataDayHistory != null) {
                    myKLineView_1D.initKDataList(kDataDayHistory);

                } else {
                    if (quoteMinEntity != null) {

                        QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                    }
                }

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

                if (kDataWeekHistory != null) {
                    List<KData> weekList = ChartUtil.KlineWeekData(ChartUtil.getWeekKDataList(kDataWeekHistory));
                    myKLineView_1_week.initKDataList(weekList);
                } else {
                    if (quoteMinEntity != null) {
                        QuoteWeekHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                    }
                }


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

                if (kDataMonthHistory != null) {
                    List<KData> monthList = ChartUtil.KlineMonthData(ChartUtil.getMonthKDataList(kDataMonthHistory));
                    myKLineView_1_month.initKDataList(monthList);
                } else {
                    if (quoteMinEntity != null) {
                        QuoteMonthHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
                    }
                }

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
                Util.lightOff(getActivity());
                showLeverWindow(tradeListEntity);
                break;
            case R.id.text_charge:
                if (isLogin()) {
                    if (tradeType.equals("1")) {
                        WebActivity.getInstance().openUrl(getActivity(), NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/deposit"), getResources().getString(R.string.text_recharge));
                    } else {
                        NetManger.getInstance().addScore((state, response) -> {
                            if (state.equals(SUCCESS)) {
                                AddScoreEntity addScoreEntity = (AddScoreEntity) response;
                                Toast.makeText(getActivity(), addScoreEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*规则*/
            case R.id.text_rule:
                String s = itemQuoteCode(quote_code);
                if (!isLogin()) {
                    WebActivity.getInstance().openUrl(getActivity(), NetManger.getInstance().h5Url(null, s, "/rule"), getResources().getString(R.string.text_rule));
                } else {
                    WebActivity.getInstance().openUrl(getActivity(), NetManger.getInstance().h5Url(loginEntity.getAccess_token(), s, "/rule"), getResources().getString(R.string.text_rule));
                }

                break;
        }
    }


    private void resetChart() {
        kline_1min_time.resetView();
        myKLineView_1Min.resetView();
        myKLineView_3Min.resetView();
        myKLineView_5Min.resetView();
        myKLineView_15Min.resetView();
        myKLineView_1H.resetView();
        myKLineView_1D.resetView();
        myKLineView_1_week.resetView();
        myKLineView_1_month.resetView();
        if (kData1MinHistory != null) {
            kline_1min_time.resetDataList(kData1MinHistory);
            myKLineView_1Min.resetDataList(kData1MinHistory);
        } else {
            if (quoteMinEntity != null) {
                Quote1MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }

        if (kData3MinHistory != null) {
            myKLineView_3Min.resetDataList(kData3MinHistory);
        } else {
            if (quoteMinEntity != null) {

                Quote3MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }
        if (kData5MinHistory != null) {
            myKLineView_5Min.resetDataList(kData5MinHistory);
        } else {
            if (quoteMinEntity != null) {

                Quote5MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }

        if (kData15MinHistory != null) {
            myKLineView_15Min.resetDataList(kData15MinHistory);
        } else {
            if (quoteMinEntity != null) {

                Quote15MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }
        if (kData60MinHistory != null) {

            myKLineView_1H.resetDataList(kData60MinHistory);
        } else {
            if (quoteMinEntity != null) {

                Quote60MinHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }

        if (kDataDayHistory != null) {
            myKLineView_1D.resetDataList(kDataDayHistory);

        } else {
            if (quoteMinEntity != null) {

                QuoteDayHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }

        if (kDataWeekHistory != null) {
            List<KData> weekList = ChartUtil.KlineWeekData(ChartUtil.getWeekKDataList(kDataWeekHistory));
            myKLineView_1_week.resetDataList(weekList);
        } else {
            if (quoteMinEntity != null) {
                QuoteWeekHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }
        if (kDataMonthHistory != null) {
            List<KData> monthList = ChartUtil.KlineMonthData(ChartUtil.getMonthKDataList(kDataMonthHistory));
            myKLineView_1_month.resetDataList(monthList);
        } else {
            if (quoteMinEntity != null) {
                QuoteMonthHistoryManger.getInstance().quote(quoteMinEntity.getSymbol(), -2);
            }
        }

    }
}
