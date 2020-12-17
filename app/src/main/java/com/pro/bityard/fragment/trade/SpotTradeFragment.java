package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.ProportionSelectAdapter;
import com.pro.bityard.adapter.SellBuyListAdapter;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.BuySellEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.QuoteCurrentManger;
import com.pro.bityard.manger.QuoteSpotManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;

public class SpotTradeFragment extends BaseFragment implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;

    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.text_currency)
    TextView text_currency;
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;
    private String quote_code = null;


    private Set<String> optionalList;//自选列表
    @BindView(R.id.img_star)
    ImageView img_star;

    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    /*头部视图id*/
    RelativeLayout layout_cancel;
    View view_line_two;
    private SpotPositionAdapter spotPositionAdapter;
    private ProportionSelectAdapter proportionLimitAdapter, proportionMarketAdapter;
    private List<Integer> proportionList;


    private SellBuyListAdapter sellAdapter, buyAdapter;
    private RecyclerView recyclerView_sell;
    private RecyclerView recyclerView_buy;
    private List<BuySellEntity> buyList;
    private List<BuySellEntity> sellList;
    private String quote;
    private TextView text_price;
    private TextView text_currency_price;
    private String value_rate;
    private TextView text_scale;
    private RelativeLayout layout_switch_limit_price;
    private TextView text_limit_market;
    private TextView text_currency_head;
    private DecimalEditText edit_amount_limit;

    private String isBuy = "true";
    private String type = "0";
    private LinearLayout layout_spot_limit;
    private LinearLayout layout_spot_market;
    private TextView text_balance;
    private DecimalEditText edit_price_limit;
    private double price;
    private TradeListEntity tradeDetail;
    private TextView text_add_price_limit, text_sub_price_limit, text_add_amount_limit, text_sub_amount_limit;
    private RadioButton radioButton_buy;
    private RadioButton radioButton_sell;
    private RadioGroup radioGroup;
    private TextView text_buy_what;
    private RelativeLayout layout_buy_what;

    private String tradeName = null;
    private DecimalEditText edit_trade_amount_limit;
    private int volumeDigit;
    private TextWatcher watcher_price;
    private TextWatcher watcher_amount;
    private TextWatcher watcher_trade;
    private String value_price_limit;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_tab;
    }

    public SpotTradeFragment newInstance(String type, String value) {
        SpotTradeFragment fragment = new SpotTradeFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        BalanceManger.getInstance().getBalance("USDT");

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        BalanceManger.getInstance().addObserver(this);


        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_spot_layout, null);
        //限价
        edit_price_limit = headView.findViewById(R.id.edit_price_limit);
        //限价数量
        edit_amount_limit = headView.findViewById(R.id.edit_amount_limit);
        //限价成交金额
        edit_trade_amount_limit = headView.findViewById(R.id.edit_trade_amount_limit);


        text_add_price_limit = headView.findViewById(R.id.text_add_price_limit);
        text_sub_price_limit = headView.findViewById(R.id.text_sub_price_limit);
        text_add_amount_limit = headView.findViewById(R.id.text_add_amount_limit);
        text_sub_amount_limit = headView.findViewById(R.id.text_sub_amount_limit);

        radioGroup = headView.findViewById(R.id.radioGroup);

        radioButton_buy = headView.findViewById(R.id.radio_buy);
        radioButton_sell = headView.findViewById(R.id.radio_sell);

        text_balance = headView.findViewById(R.id.text_balance);

        text_buy_what = headView.findViewById(R.id.text_buy_what);
        layout_buy_what = headView.findViewById(R.id.layout_buy_what);

        layout_spot_limit = headView.findViewById(R.id.layout_spot_limit);
        layout_spot_market = headView.findViewById(R.id.layout_spot_market);
        layout_spot_market.setVisibility(View.GONE);


        text_currency_head = headView.findViewById(R.id.text_currency_head);
        /*限价*/
        RecyclerView recyclerView_proportion_limit = headView.findViewById(R.id.recyclerView_proportion_limit);

        proportionList = new ArrayList<>();
        proportionList.add(20);
        proportionList.add(50);
        proportionList.add(75);
        proportionList.add(100);
        proportionLimitAdapter = new ProportionSelectAdapter(getActivity());
        recyclerView_proportion_limit.setLayoutManager(new GridLayoutManager(getActivity(), proportionList.size()));
        recyclerView_proportion_limit.setAdapter(proportionLimitAdapter);






        /*市价*/
        RecyclerView recyclerView_proportion_market = headView.findViewById(R.id.recyclerView_proportion_market);
        proportionMarketAdapter = new ProportionSelectAdapter(getActivity());
        recyclerView_proportion_market.setLayoutManager(new GridLayoutManager(getActivity(), proportionList.size()));
        recyclerView_proportion_market.setAdapter(proportionMarketAdapter);


        view.findViewById(R.id.layout_product).setOnClickListener(this);
        //自选监听
        view.findViewById(R.id.layout_optional).setOnClickListener(this);
        BalanceManger.getInstance().addObserver(this);
        QuoteSpotManger.getInstance().addObserver(this);
        QuoteCurrentManger.getInstance().addObserver(this);//1min 实时


        spotPositionAdapter = new SpotPositionAdapter(getActivity());
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_spot.setAdapter(spotPositionAdapter);

        recyclerView_spot.addHeaderView(headView);
        layout_cancel = headView.findViewById(R.id.layout_cancel);
        view_line_two = headView.findViewById(R.id.view_line_two);
        layout_switch_limit_price = headView.findViewById(R.id.layout_switch_limit_price);
        text_limit_market = headView.findViewById(R.id.text_limit_market);




        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosition();
        });

        text_scale = headView.findViewById(R.id.text_scale);
        text_price = headView.findViewById(R.id.text_price);
        text_currency_price = headView.findViewById(R.id.text_currency_price);


        headView.findViewById(R.id.layout_buy_sell_switch).setOnClickListener(this);
        layout_switch_limit_price.setOnClickListener(this);


        sellAdapter = new SellBuyListAdapter(getActivity());
        recyclerView_sell = headView.findViewById(R.id.recyclerView_sell);
        recyclerView_sell.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_sell.setAdapter(sellAdapter);

        buyAdapter = new SellBuyListAdapter(getActivity());
        recyclerView_buy = headView.findViewById(R.id.recyclerView_buy);
        recyclerView_buy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_buy.setAdapter(buyAdapter);


    }


    @Override
    protected void intPresenter() {

    }


    boolean flag_price = true;
    boolean flag_amount = true;
    boolean flag_trade = true;

    @Override
    protected void initData() {


        Handler handler = new Handler();
        handler.postDelayed(() -> startScheduleJob(mHandler, ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND), 50);


        tradeType = getArguments().getString(TYPE);
        itemData = getArguments().getString(VALUE);
        tradeName = TradeUtil.name(itemData);


        quote_code = itemQuoteContCode(itemData);


        String string = SPUtils.getString(AppConfig.QUOTE_DETAIL, null);
        List<TradeListEntity> tradeListEntityList = Util.SPDealEntityResult(string);
        tradeDetail = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
        Log.d("print", "initData: 309: " + tradeDetail);


        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));
        text_currency_head.setText("(" + tradeName + ")");
        edit_amount_limit.setHint(getResources().getString(R.string.text_amount_withdrawal) + " (" + tradeName + ")");
        //根据当前价格的小数位确定输入框的小数位
        int priceDigit = tradeDetail.getPriceDigit();
        edit_price_limit.setDecimalEndNumber(priceDigit);
        String volumeMin = tradeDetail.getVolumeMin();
        if (volumeMin == null) {
            volumeDigit = 0;
        } else {
            volumeDigit = TradeUtil.decimalPoint(volumeMin);
        }
        edit_amount_limit.setDecimalEndNumber(volumeDigit);
        edit_trade_amount_limit.setDecimalEndNumber(priceDigit);


        watcher_price = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_trade_amount_limit.removeTextChangedListener(watcher_trade);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    TradeUtil.setTradeAmount(edit_price_limit, edit_amount_limit, edit_trade_amount_limit, priceDigit);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_trade_amount_limit.addTextChangedListener(watcher_trade);

            }
        };

        edit_price_limit.addTextChangedListener(watcher_price);


        watcher_amount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_trade_amount_limit.removeTextChangedListener(watcher_trade);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    TradeUtil.setTradeAmount(edit_amount_limit, edit_price_limit, edit_trade_amount_limit, priceDigit);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_trade_amount_limit.addTextChangedListener(watcher_trade);

            }
        };
        edit_amount_limit.addTextChangedListener(watcher_amount);


        watcher_trade = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_amount_limit.removeTextChangedListener(watcher_amount);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value_price_limit = edit_price_limit.getText().toString();
                if (s.length() != 0) {
                    if (value_price_limit.length() != 0) {
                        String value_amount = TradeUtil.divBig(Double.parseDouble(edit_trade_amount_limit.getText().toString()), Double.parseDouble(value_price_limit), volumeDigit);
                        edit_amount_limit.setText(value_amount);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_amount_limit.addTextChangedListener(watcher_amount);
            }
        };

        edit_trade_amount_limit.addTextChangedListener(watcher_trade);


        //TradeUtil.setEditTrade(edit_price_limit, edit_amount_limit, edit_trade_amount_limit, priceDigit);

        //TradeUtil.setEditTrade(edit_amount_limit, edit_price_limit, edit_trade_amount_limit, priceDigit);
        //限价加号
        text_add_price_limit.setOnClickListener(v ->
        {
            TradeUtil.addMyself(edit_price_limit, tradeDetail.getPriceChange());
            TradeUtil.setTradeAmount(edit_price_limit, edit_amount_limit, edit_trade_amount_limit, priceDigit);
        });
        //限价减号
        text_sub_price_limit.setOnClickListener(v ->
        {
            TradeUtil.subMyself(edit_price_limit, tradeDetail.getPriceChange());
            TradeUtil.setTradeAmount(edit_price_limit, edit_amount_limit, edit_trade_amount_limit, priceDigit);
        });

        String minVolume = volumeMin;
        //限价加号
        text_add_amount_limit.setOnClickListener(v ->
        {
            TradeUtil.addMyself(edit_amount_limit, Double.parseDouble(minVolume));
            TradeUtil.setTradeAmount(edit_amount_limit, edit_price_limit, edit_trade_amount_limit, priceDigit);

        });
        //限价减号
        text_sub_amount_limit.setOnClickListener(v ->

        {
            TradeUtil.subMyself(edit_amount_limit, Double.parseDouble(minVolume));
            TradeUtil.setTradeAmount(edit_amount_limit, edit_price_limit, edit_trade_amount_limit, priceDigit);
        });


        optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));

        Util.setOptional(
                getActivity(), optionalList, quote_code, img_star, response ->
                {
                    if (!(boolean) response) {
                        optionalList = new HashSet<>();
                    }
                });

        getPosition();

        value_rate = SPUtils.getString(AppConfig.USD_RATE, null);
        text_buy_what.setText(
                getResources().

                        getText(R.string.text_buy) + tradeName);
        radioGroup.setOnCheckedChangeListener((group, checkedId) ->
        {
            switch (checkedId) {
                case R.id.radio_buy:
                    count = 0;
                    radioButton_buy.setBackground(getActivity().getResources().getDrawable(R.mipmap.bg_spot_buy));
                    radioButton_sell.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_color_left));
                    layout_buy_what.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_shape_green));
                    text_buy_what.setText(getResources().getText(R.string.text_buy) + tradeName);
                    isBuy = "true";
                    break;
                case R.id.radio_sell:
                    count = 0;
                    radioButton_buy.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_color_left));
                    radioButton_sell.setBackground(getActivity().getResources().getDrawable(R.mipmap.bg_spot_sell));
                    layout_buy_what.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_shape_red));
                    text_buy_what.setText(getResources().getText(R.string.text_sell) + tradeName);
                    isBuy = "false";
                    break;
            }
        });
        /*限价*/
        proportionLimitAdapter.setDatas(proportionList);
        proportionLimitAdapter.select(5);
        proportionLimitAdapter.setOnItemClick((position, data) -> {
            proportionLimitAdapter.select(position);
            double balanceReal = BalanceManger.getInstance().getBalanceReal();
            String s = TradeUtil.mulBig(balanceReal, Double.parseDouble(TradeUtil.divBig(data, 100, 2)));
            String numberFormat = TradeUtil.getNumberFormat(Double.parseDouble(s), priceDigit);
            edit_trade_amount_limit.setText(numberFormat);
        });

        /*市价*/
        proportionMarketAdapter.setDatas(proportionList);
        proportionMarketAdapter.select(5);
        proportionMarketAdapter.setOnItemClick((position, data) -> {
            proportionMarketAdapter.select(position);
        });
        /*买入*/
        layout_buy_what.setOnClickListener(v -> {


            String value_amount_limit = edit_amount_limit.getText().toString();
            if (value_amount_limit.equals("")) {
                return;
            }

            if (type.equals("0")) {
                value_price_limit = edit_price_limit.getText().toString();
                if (value_price_limit.equals("")) {
                    return;
                }
            } else {
                value_price_limit = null;
            }

            NetManger.getInstance().spotOpen(Util.filterNumber(quote_code), isBuy, type, "USDT", tradeName, value_price_limit,
                    value_amount_limit, "0", (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

        });


    }

    private void getPosition() {
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().userSpotPosition(loginEntity.getUser().getUserId(), (state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                SpotPositionEntity spotPositionEntity = (SpotPositionEntity) response;
                if (spotPositionEntity.getData().size() == 0) {
                    layout_null.setVisibility(View.VISIBLE);
                    layout_cancel.setVisibility(View.GONE);
                    view_line_two.setVisibility(View.GONE);
                } else {
                    layout_null.setVisibility(View.GONE);
                    layout_cancel.setVisibility(View.VISIBLE);
                    view_line_two.setVisibility(View.VISIBLE);
                }
                spotPositionAdapter.setDatas(spotPositionEntity.getData());
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_optional:
                optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
                Log.d("print", "onClick:自选目前: " + optionalList);
                //判断当前是否存在自选
                if (optionalList.size() != 0) {
                    Util.isOptional(quote_code, optionalList, response -> {
                        boolean isOptional = (boolean) response;
                        if (isOptional) {
                            img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                            optionalList.remove(quote_code);
                        } else {
                            img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                            optionalList.add(quote_code);
                        }
                    });
                } else {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                    optionalList.add(quote_code);
                }
                SPUtils.putString(AppConfig.KEY_OPTIONAL, Util.SPDeal(optionalList));

                break;
            /*买卖展示切换*/
            case R.id.layout_buy_sell_switch:
                if (quote != null) {
                    Util.lightOff(getActivity());
                    showBuySellSwitch(getActivity(), layout_view);
                }

                break;
            case R.id.layout_switch_limit_price:
                showLimitPriceWindow();
                break;

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            //发送行情包
            if (quote_code != null) {
                WebSocketManager.getInstance().send("5001", quote_code);
                WebSocketManager.getInstance().send("4001", quote_code);

            }

        }
    };


    private int length = 5;
    private int count = 0;//控制限价价格显示 手动切换才变数据

    @SuppressLint("SetTextI18n")
    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteSpotManger.getInstance()) {
            if (!isAdded()) {
                return;
            }

            quote = (String) arg;


            runOnUiThread(() -> {
                buyList = Util.getBuyList(quote);
                buyAdapter.isSell(false);
                buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));


                sellList = Util.getSellList(quote);
                sellAdapter.isSell(true);
                sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));
            });


        } else if (o == QuoteCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteMinEntity quoteMinEntity = (QuoteMinEntity) arg;
            if (quoteMinEntity != null) {
                runOnUiThread(() -> {
                    if (text_price != null) {
                        int isUp = quoteMinEntity.getIsUp();
                        price = quoteMinEntity.getPrice();
                        text_price.setText(String.valueOf(price));

                        if (count == 0) {
                            Log.d("print", "initView:221:  " + TradeUtil.decimalPoint(String.valueOf(price)));
                            edit_price_limit.setText(String.valueOf(price));
                            count++;
                        }
                        text_scale.setText(TradeUtil.scaleString(TradeUtil.decimalPoint(String.valueOf(price))));
                        if (value_rate != null) {
                            text_currency_price.setText("≈" + TradeUtil.numberHalfUp(TradeUtil.mul(price, Double.parseDouble(value_rate)), 2));
                        } else {
                            text_currency_price.setText("≈" + price);
                        }
                        switch (isUp) {
                            case -1:
                                text_price.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_red));
                                break;
                            case 1:
                                text_price.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_green));
                                break;
                            case 0:
                                text_price.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_main_color));
                                break;
                        }


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

            text_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }

    /*限价 市价的切换*/
    private void showLimitPriceWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_switch_limit_layout, null);
        PopupWindow popupWindowLimitMarket = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        view.findViewById(R.id.text_limit_order).setOnClickListener(v -> {

            popupWindowLimitMarket.dismiss();
            text_limit_market.setText(getResources().getText(R.string.text_limit_order));
            layout_spot_limit.setVisibility(View.VISIBLE);
            layout_spot_market.setVisibility(View.GONE);
            type = "0";

        });


        view.findViewById(R.id.text_market_order).setOnClickListener(v -> {
            popupWindowLimitMarket.dismiss();
            text_limit_market.setText(getResources().getText(R.string.text_market_order));
            layout_spot_limit.setVisibility(View.GONE);
            layout_spot_market.setVisibility(View.VISIBLE);
            type = "1";

        });


        Util.dismiss(getActivity(), popupWindowLimitMarket);
        popupWindowLimitMarket.setFocusable(true);
        popupWindowLimitMarket.setOutsideTouchable(true);
        popupWindowLimitMarket.setContentView(view);
        popupWindowLimitMarket.showAsDropDown(layout_switch_limit_price, Gravity.CENTER, 0, 0);
    }

    /*买卖单显示切换*/
    public void showBuySellSwitch(Activity activity, View layout_view) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_buy_sell_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.text_default).setOnClickListener(view1 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.VISIBLE);
            recyclerView_sell.setVisibility(View.VISIBLE);
            length = 5;

            buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));
            sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));

        });


        view.findViewById(R.id.text_show_sell).setOnClickListener(view12 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.GONE);
            recyclerView_sell.setVisibility(View.VISIBLE);
            length = 10;
            if (sellList != null) {
                sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));
            }

        });
        view.findViewById(R.id.text_show_buy).setOnClickListener(view12 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.VISIBLE);
            recyclerView_sell.setVisibility(View.GONE);
            length = 10;
            if (buyList != null) {
                buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));
            }

        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);


        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);

    }


}
