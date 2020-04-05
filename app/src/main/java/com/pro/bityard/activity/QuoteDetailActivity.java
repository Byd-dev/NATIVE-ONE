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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.adapter.RadioGroupAdapter;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.ChargeUnitEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.QuoteItemManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.manger.TradeListManger;
import com.pro.bityard.utils.OnPopResult;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
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
import static com.pro.bityard.utils.TradeUtil.marginMin;
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
    private String tradeType = "1";//实盘=1 模拟=2
    private String orderType = "0"; //市价=0 限价=1
    private boolean isDefer; //是否递延

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


    @BindView(R.id.recyclerView_market)
    RecyclerView recyclerView_market;

    @BindView(R.id.recyclerView_limit)
    RecyclerView recyclerView_limit;

    @BindView(R.id.recyclerView_profit)
    RecyclerView recyclerView_profit;

    @BindView(R.id.recyclerView_loss)
    RecyclerView recyclerView_loss;

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

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.img_one)
    ImageView img_one;
    @BindView(R.id.img_two)
    ImageView img_two;
    @BindView(R.id.img_three)
    ImageView img_three;


    private RadioGroupAdapter radioGroupAdapter;//杠杆适配器
    private RadioRateAdapter radioRateProfitAdapter, radioRateLossAdapter;

    private List<String> quoteList;

    private QuotePopAdapter quotePopAdapter;
    private PopupWindow popupWindow;
    private String quote;
    private List<TradeListEntity> tradeListEntityList;
    private List<ChargeUnitEntity> chargeUnitEntityList;
    private ChargeUnitEntity chargeUnitEntity;
    private double stopProfit = 3;
    private double stopLoss = -0.9;
    private boolean isOpenSure;
    private boolean isCloseSure;


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

    @Override
    protected void initView(View view) {

        QuoteListManger.getInstance().addObserver(this);

        QuoteItemManger.getInstance().addObserver(this);

        BalanceManger.getInstance().getBalance("USDT");

        TradeListManger.getInstance().addObserver(this);

        ChargeUnitManger.getInstance().addObserver(this);
        TagManger.getInstance().addObserver(this);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_back_two).setOnClickListener(this);

        findViewById(R.id.img_setting).setOnClickListener(this);

        findViewById(R.id.layout_more).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        findViewById(R.id.layout_much).setOnClickListener(this);
        findViewById(R.id.layout_empty).setOnClickListener(this);

        radioGroupAdapter = new RadioGroupAdapter(this);
        recyclerView_market.setAdapter(radioGroupAdapter);
        recyclerView_limit.setAdapter(radioGroupAdapter);

        isDefer = SPUtils.getBoolean(AppConfig.KEY_DEFER, false);
        if (isDefer) {
            img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
        } else {
            img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
        }

        isOpenSure = SPUtils.getBoolean(AppConfig.KEY_OPEN_SURE, true);
        if (isOpenSure) {
            img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
        } else {
            img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
        }
        isCloseSure = SPUtils.getBoolean(AppConfig.KEY_CLOSE_SURE, false);
        if (isCloseSure) {
            img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));

        } else {
            img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));

        }

        List<Integer> stopProfitList = new ArrayList<>();
        stopProfitList.add(300);
        stopProfitList.add(350);
        stopProfitList.add(400);
        stopProfitList.add(450);
        stopProfitList.add(500);
        radioRateProfitAdapter = new RadioRateAdapter(this);
        recyclerView_profit.setAdapter(radioRateProfitAdapter);
        recyclerView_profit.setLayoutManager(new GridLayoutManager(this, stopProfitList.size()));
        radioRateProfitAdapter.setDatas(stopProfitList);
        int index_profit = SPUtils.getInt(AppConfig.INDEX_PROFIT, 0);
        radioRateProfitAdapter.select(index_profit);
        radioRateProfitAdapter.setOnItemClick((position, data) -> {
            radioRateProfitAdapter.select(position);
            recyclerView_profit.setAdapter(radioRateProfitAdapter);
            stopProfit = TradeUtil.div(data, 100, 2);
            SPUtils.putInt(AppConfig.INDEX_PROFIT, position);

        });

        List<Integer> stopLossList = new ArrayList<>();
        stopLossList.add(-10);
        stopLossList.add(-30);
        stopLossList.add(-50);
        stopLossList.add(-70);
        stopLossList.add(-90);
        radioRateLossAdapter = new RadioRateAdapter(this);
        recyclerView_loss.setAdapter(radioRateLossAdapter);
        recyclerView_loss.setLayoutManager(new GridLayoutManager(this, stopLossList.size()));
        radioRateLossAdapter.setDatas(stopLossList);
        int index_loss = SPUtils.getInt(AppConfig.INDEX_LOSS, 4);
        radioRateLossAdapter.select(index_loss);
        radioRateLossAdapter.setOnItemClick((position, data) -> {
            radioRateLossAdapter.select(position);
            recyclerView_loss.setAdapter(radioRateLossAdapter);
            stopLoss = TradeUtil.div(data, 100, 2);
            SPUtils.putInt(AppConfig.INDEX_LOSS, position);

        });


        findViewById(R.id.layout_one).setOnClickListener(this);
        findViewById(R.id.layout_two).setOnClickListener(this);
        findViewById(R.id.layout_three).setOnClickListener(this);
        findViewById(R.id.btn_sure).setOnClickListener(this);

        text_market_all.setOnClickListener(this);
        text_limit_all.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);

        //开启单个刷新
        QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));
        //合约号
        tradeListEntityList = TradeListManger.getInstance().getTradeListEntityList();
        //手续费
        chargeUnitEntityList = ChargeUnitManger.getInstance().getChargeUnitEntityList();
        if (tradeListEntityList == null) {
            startHandler(handler, 0, ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND);
        } else {
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            Log.d("print", "initData:258:合约号:  " + tradeListEntity);
            setContent(tradeListEntity);
        }

        if (chargeUnitEntityList == null) {
            startHandler(handler, 1, ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND);
        } else {
            chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
            Log.d("print", "initData:259:手续费: " + chargeUnitEntity);
        }


        String[] split1 = Util.quoteList(itemQuoteContCode(itemData)).split(",");
        text_name.setText(split1[0]);
        text_name_usdt.setText(split1[1]);
        text_lastPrice.setText(listQuotePrice(itemData));
        text_market_price.setText(listQuotePrice(itemData));
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

                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));

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
            recyclerView_market.setLayoutManager(new GridLayoutManager(this, leverShowList.size()));
            recyclerView_limit.setLayoutManager(new GridLayoutManager(this, leverShowList.size()));

            radioGroupAdapter.setDatas(leverShowList);
            radioGroupAdapter.select(0);
            lever = leverShowList.get(0);

            radioGroupAdapter.setOnItemClick((position, data) -> {
                lever = data;
                radioGroupAdapter.select(position);
                recyclerView_market.setAdapter(radioGroupAdapter);
                recyclerView_limit.setAdapter(radioGroupAdapter);
                radioGroupAdapter.notifyDataSetChanged();


            });

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
                            } else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_market_margin.postDelayed(() -> edit_market_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }
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
                            } else if (parseDouble(s.toString()) < parseDouble(marginMin(tradeListEntity.getDepositList()))) {
                                edit_limit_margin.postDelayed(() -> edit_limit_margin.setText(marginMin(tradeListEntity.getDepositList())), 1000);
                            }
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

    @SuppressLint("RtlHardcoded")
    private void showDrawerLayout() {
        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_more:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_right));
                    backgroundAlpha(1f);
                } else {
                    showMoreWindow(quoteList);
                    img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_open));
                }
                break;
            case R.id.btn_sure:
            case R.id.img_back_two:
                drawerLayout.closeDrawers();
                break;

            case R.id.img_setting:
                showDrawerLayout();
                break;

            case R.id.layout_much:

                String priceMuch = text_buy_much.getText().toString();
                if (isOpenSure) {
                    slowOpen("true", priceMuch);
                } else {
                    fastOpen("true", priceMuch);
                }


                break;
            case R.id.layout_empty:
                String priceEmpty = text_buy_empty.getText().toString();
                if (isOpenSure) {
                    slowOpen("false", priceEmpty);
                } else {
                    fastOpen("false", priceEmpty);

                }

                break;
            case R.id.layout_one:
                if (isDefer) {
                    img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isDefer = false;
                    SPUtils.putBoolean(AppConfig.KEY_DEFER, false);
                } else {
                    img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isDefer = true;
                    SPUtils.putBoolean(AppConfig.KEY_DEFER, true);
                }
                break;
            case R.id.layout_two:
                if (isOpenSure) {
                    img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isOpenSure = false;
                    SPUtils.putBoolean(AppConfig.KEY_OPEN_SURE, false);
                } else {
                    img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isOpenSure = true;
                    SPUtils.putBoolean(AppConfig.KEY_OPEN_SURE, true);
                }
                break;
            case R.id.layout_three:
                if (isCloseSure) {
                    img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isCloseSure = false;
                    SPUtils.putBoolean(AppConfig.KEY_CLOSE_SURE, false);
                } else {
                    img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isCloseSure = true;
                    SPUtils.putBoolean(AppConfig.KEY_CLOSE_SURE, true);
                }
                break;

            case R.id.text_market_all:
                if (text_market_all.getText().toString().equals(getResources().getString(R.string.text_default))) {
                    return;
                } else {
                    PopUtil.getInstance().showTip(QuoteDetailActivity.this, layout_view, false,
                            new StringBuilder().append(getString(R.string.text_service_tip)).append(text_market_all.getText().toString()).append("USDT").toString(), state -> {
                            }
                    );
                }
                break;
            case R.id.text_limit_all:
                if (text_limit_all.getText().toString().equals(getResources().getString(R.string.text_default))) {
                    return;
                } else {
                    PopUtil.getInstance().showTip(QuoteDetailActivity.this, layout_view, false,
                            new StringBuilder().append(getString(R.string.text_service_tip)).append(text_limit_all.getText().toString()).append("USDT").toString(), state -> {
                            }
                    );
                }
                break;

        }
    }

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
                backgroundAlpha(1f);
            }
        });
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.6f;
        getWindow().setAttributes(params);

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.pop_anim_quote);
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
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);
        showTip(isBuy, margin, serviceCharge);

    }

    /*下单*/
    private void fastOpen(String isBuy, String priceMuchOrEmpty) {
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
        String serviceCharge = TradeUtil.serviceCharge(chargeUnitEntity, 3, margin, lever);


        NetManger.getInstance().order(tradeType, "2", tradeListEntity.getCode(),
                tradeListEntity.getContractCode(), isBuy, margin, String.valueOf(lever), priceOrder, defer,
                TradeUtil.deferFee(defer, tradeListEntity.getDeferFee(), margin, lever), String.valueOf(stopProfit), String.valueOf(stopLoss), serviceCharge,
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

    private void showMoreWindow(List<String> quoteList) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.item_more_layout, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_pop);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        quotePopAdapter = new QuotePopAdapter(this);
        if (quoteList != null) {
            quotePopAdapter.setDatas(quoteList);
        }
        if (quote != null) {
            quotePopAdapter.select(itemQuoteContCode(quote));

        }


        recyclerView.setAdapter(quotePopAdapter);
        quotePopAdapter.setOnItemClick(data -> {
            text_name.setText(listQuoteName(data));
            text_name_usdt.setText(listQuoteUSD(data));
            QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, itemQuoteContCode(data));
            //相应选择
            quotePopAdapter.select(itemQuoteContCode(data));
            backgroundAlpha(1f);
            img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_right));
            popupWindow.dismiss();


            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(data), tradeListEntityList);

            setContent(tradeListEntity);


        });


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.6f;
        getWindow().setAttributes(params);

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

            backgroundAlpha(1f);
            popupWindow.dismiss();
            img_right.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_market_right));


        });

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.pop_anim_quote);
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

        } else if (o == QuoteItemManger.getInstance()) {
            quote = (String) arg;

            Log.d("print", "update:720: 单个行情:  " + quote);
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
                text_market_price.setText(itemQuotePrice(quote));
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

        } else if (o == TradeListManger.getInstance()) {
            tradeListEntityList = (List<TradeListEntity>) arg;
            TradeListEntity tradeListEntity = (TradeListEntity) TradeUtil.tradeDetail(itemQuoteContCode(itemData), tradeListEntityList);
            Log.d("print", "initData:510:  " + tradeListEntity);
            setContent(tradeListEntity);
        } else if (o == ChargeUnitManger.getInstance()) {
            chargeUnitEntityList = (List<ChargeUnitEntity>) arg;
            chargeUnitEntity = (ChargeUnitEntity) TradeUtil.chargeDetail(itemQuoteCode(itemData), chargeUnitEntityList);
            Log.d("print", "update:596: " + chargeUnitEntity);


        } else if (o == TagManger.getInstance()) {
            ChargeUnitManger.getInstance().chargeUnit((state, response) -> {

            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        QuoteItemManger.getInstance().clear();
        QuoteItemManger.getInstance().cancelTimer();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                layout_market_price.setVisibility(View.VISIBLE);
                layout_limit_price.setVisibility(View.GONE);
                text_buy_much.setVisibility(View.VISIBLE);
                text_buy_empty.setVisibility(View.VISIBLE);
                orderType = "0";

                break;
            case R.id.radio_1:
                layout_market_price.setVisibility(View.GONE);
                layout_limit_price.setVisibility(View.VISIBLE);
                text_buy_much.setVisibility(View.GONE);
                text_buy_empty.setVisibility(View.GONE);
                orderType = "1";
                break;


        }
    }
}
