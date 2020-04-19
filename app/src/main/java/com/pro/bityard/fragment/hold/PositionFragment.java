package com.pro.bityard.fragment.hold;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.PositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.DecimalEditText;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.utils.TradeUtil.ProfitAmount;
import static com.pro.bityard.utils.TradeUtil.big;
import static com.pro.bityard.utils.TradeUtil.getNumberFormat;
import static com.pro.bityard.utils.TradeUtil.lossAmount;
import static com.pro.bityard.utils.TradeUtil.lossRate;
import static com.pro.bityard.utils.TradeUtil.numberHalfUp;
import static com.pro.bityard.utils.TradeUtil.price;
import static com.pro.bityard.utils.TradeUtil.profitRate;
import static com.pro.bityard.utils.TradeUtil.small;

public class PositionFragment extends BaseFragment implements Observer {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    private PositionAdapter positionAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private PositionEntity positionEntity;

    private TextView text_incomeAll;
    private View headView;
    private TextView text_price;
    private String contractCode;

    private boolean isEdit_profit_amount = true;
    private boolean isEdit_profit_price = false;

    private boolean isEdit_loss_amount = true;
    private boolean isEdit_loss_price = false;
    private List<String> quoteList;


    public PositionFragment newInstance(String type) {
        PositionFragment fragment = new PositionFragment();
        Bundle args = new Bundle();
        args.putString("tradeType", type);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_open;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tradeType = getArguments().getString("tradeType");
        }
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            headerRecyclerView.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView(View view) {
        //行情的注册
        QuoteListManger.getInstance().addObserver(this);
        //标签
        TagManger.getInstance().addObserver(this);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        positionAdapter = new PositionAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        headView = LayoutInflater.from(getContext()).inflate(R.layout.item_position_head_layout, null);

        View footView = LayoutInflater.from(getContext()).inflate(R.layout.tab_foot_view, null);

        headerRecyclerView.addFooterView(footView);

        text_incomeAll = headView.findViewById(R.id.text_total_profit_loss);
        /*一键平仓*/
        headView.findViewById(R.id.text_close_all).setOnClickListener(v -> NetManger.getInstance().closeAll(TradeUtil.positionIdList(positionEntity), tradeType, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                    Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                    initData();
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        }));


        headerRecyclerView.setAdapter(positionAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> initData());

        positionAdapter.setOnItemClick(new PositionAdapter.OnItemClick() {
            @Override
            public void onClickListener(PositionEntity.DataBean data) {
                showAddPopWindow(data);
            }

            @Override
            public void onCloseListener(String id) {

                boolean isCloseSure = SPUtils.getBoolean(AppConfig.KEY_CLOSE_SURE, false);
                if (isCloseSure) {
                    PopUtil.getInstance().showTip(getActivity(), layout_view, true, getResources().getString(R.string.text_are_you_sure), state -> {
                        if (state) {
                            close(id);
                        }
                    });
                } else {
                    close(id);
                }

            }

            @Override
            public void onProfitLossListener(PositionEntity.DataBean data) {
                Log.d("print", "onProfitLossListener:165:  " + data);

                showPopWindow(data);

            }


        });
        btn_login.setOnClickListener(v -> {
            LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);

        });


    }

    private void close(String id) {
        /*平仓*/
        NetManger.getInstance().close(id, tradeType, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                initData();

            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });
    }


    /*增加保证金*/
    private void showAddPopWindow(PositionEntity.DataBean data) {

        boolean isBuy = data.isIsBuy();
        double lever = data.getLever();
        double margin = data.getMargin();
        double price = data.getPrice();
        int priceDigit = data.getPriceDigit();
        double stopProfit = data.getStopProfit();
        double stopLoss = data.getStopLoss();


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_margin_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //可用余额
        TextView text_balance = view.findViewById(R.id.text_balance);

        if (tradeType.equals("1")) {
            text_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2));
        } else {
            text_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceSim(), 2));

        }
        //保证金
        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(margin));
        //保证金 之后
        TextView text_margin_after = view.findViewById(R.id.text_margin_after);
        text_margin_after.setText(String.valueOf(margin));

        //杠杆
        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(lever));
        //杠杆 之后
        TextView text_lever_after = view.findViewById(R.id.text_lever_after);
        text_lever_after.setText(String.valueOf(lever));

        //止盈价
        TextView text_stop_profit = view.findViewById(R.id.text_stop_profit);
        text_stop_profit.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
        //止盈价 之后
        TextView text_stop_profit_after = view.findViewById(R.id.text_stop_profit_after);
        text_stop_profit_after.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
        //止损价
        TextView text_stop_loss = view.findViewById(R.id.text_stop_loss);
        text_stop_loss.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
        //止损价 之后
        TextView text_stop_loss_after = view.findViewById(R.id.text_stop_loss_after);
        text_stop_loss_after.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
        //输入框
        DecimalEditText edit_margin = view.findViewById(R.id.edit_margin);
        edit_margin.setDecimalEndNumber(2);
        edit_margin.setHint(0 + "~" + TradeUtil.maxMargin(lever, data.getOpPrice(), data.getVolume()));
        edit_margin.setSelection(0, Objects.requireNonNull(edit_margin.getText()).toString().length());

        //总计
        TextView text_all = view.findViewById(R.id.text_all);


        //加 监听
        view.findViewById(R.id.text_add).setOnClickListener(v -> {
            if (edit_margin.getText().toString().length() == 0) {
                double a = TradeUtil.add(0, TradeUtil.scale(2));
                edit_margin.setText(String.valueOf(a));

            } else {
                double a = TradeUtil.add(Double.parseDouble(edit_margin.getText().toString()), TradeUtil.scale(2));
                edit_margin.setText(String.valueOf(a));

            }

        });

        //减 监听
        view.findViewById(R.id.text_sub).setOnClickListener(v -> {
            if (edit_margin.getText().toString().length() == 0 || Double.parseDouble(edit_margin.getText().toString()) <= 0) {
                edit_margin.setText(String.valueOf(0));
            } else {
                double a = TradeUtil.sub(Double.parseDouble(edit_margin.getText().toString()), TradeUtil.scale(2));
                edit_margin.setText(String.valueOf(a));
            }

        });
        edit_margin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                } else {
                    String edit_result = edit_margin.getText().toString();
                    text_margin_after.setText(String.valueOf(TradeUtil.add(margin, Double.parseDouble(edit_result))));
                    text_all.setText(edit_margin.getText().toString() + " USDT");

                    String margin_after = text_margin_after.getText().toString();

                    text_lever_after.setText(TradeUtil.lever(Double.parseDouble(margin_after),
                            data.getOpPrice(), data.getVolume()));

                    String lever_after = TradeUtil.lever(Double.parseDouble(margin_after),
                            data.getOpPrice(), data.getVolume());

                    text_stop_profit_after.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit,
                            Double.parseDouble(lever_after),
                            margin, stopProfit));

                    text_stop_loss_after.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit,
                            Double.parseDouble(lever_after),
                            margin, Math.abs(stopLoss)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        view.findViewById(R.id.text_sure).setOnClickListener(v -> NetManger.getInstance().submitMargin(data.getId(), text_margin_after.getText().toString(), new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    popupWindow.dismiss();
                    backgroundAlpha(1f);
                    Toast.makeText(getActivity(), getResources().getText(R.string.text_success), Toast.LENGTH_SHORT).show();

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }));


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        });

        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.6f;
        getActivity().getWindow().setAttributes(params);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        // popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    /*修改止盈止损*/
    @SuppressLint("SetTextI18n")
    private void showPopWindow(PositionEntity.DataBean data) {
        contractCode = data.getContractCode();


        final boolean isBuy = data.isIsBuy();
        final int priceDigit = data.getPriceDigit();
        final double price = data.getPrice();
        final double lever = data.getLever();
        final double margin = data.getMargin();
        double stopProfit = data.getStopProfit();
        double stopLoss = data.getStopLoss();


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_spsl_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        /* -----------------------------------------------------------RadioGroup监听---------------------------------------------------------------------------*/

        //金额布局
        LinearLayout layout_amount = view.findViewById(R.id.layout_amount);
        //价格布局
        LinearLayout layout_price = view.findViewById(R.id.layout_price);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_0:
                    layout_amount.setVisibility(View.VISIBLE);
                    layout_price.setVisibility(View.GONE);
                    isEdit_profit_amount = true;
                    isEdit_profit_price = false;
                    isEdit_loss_amount = true;
                    isEdit_loss_price = false;
                    break;
                case R.id.radio_1:
                    layout_amount.setVisibility(View.GONE);
                    layout_price.setVisibility(View.VISIBLE);
                    isEdit_profit_amount = false;
                    isEdit_profit_price = true;
                    isEdit_loss_amount = false;
                    isEdit_loss_price = true;
                    break;
            }
        });
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
            backgroundAlpha(1f);

        });

        //当前价格
        text_price = view.findViewById(R.id.text_price);

        //量
        TextView text_volume = view.findViewById(R.id.text_volume);
        text_volume.setText("×" + data.getVolume());
        //名称
        TextView text_name = view.findViewById(R.id.text_name);
        String[] split = Util.quoteList(contractCode).split(",");
        text_name.setText(split[0]);
        //开仓价
        TextView text_open_price = view.findViewById(R.id.text_open_price);
        text_open_price.setText(String.valueOf(data.getOpPrice()));


        double profit_max_amount = TradeUtil.mul(margin, 5.00);
        double profit_min_amount = TradeUtil.mul(margin, 0.05);

        double loss_max_amount = TradeUtil.mul(margin, 0.90);

        double loss_min_amount = TradeUtil.mul(margin, 0.05);


        double profit_max_price = Double.parseDouble(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_max_amount));
        double profit_min_price = Double.parseDouble(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_min_amount));

        double loss_max_price = Double.parseDouble(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_max_amount));
        double loss_min_price = Double.parseDouble(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_min_amount));

        /* -----------------------------------------------------------金额---------------------------------------------------------------------------*/

        //金额 预计盈利 输入框
        DecimalEditText edit_profit_amount = view.findViewById(R.id.edit_profit_amount);
        edit_profit_amount.setText(String.valueOf(stopProfit));
        edit_profit_amount.setSelection(String.valueOf(stopProfit).length());//默认光标在最后面
        edit_profit_amount.setDecimalEndNumber(2);
        //金额 盈利 加号
        view.findViewById(R.id.text_add_profit_amount).setOnClickListener(v -> {
            if (Objects.requireNonNull(edit_profit_amount.getText()).toString().length() > 0 &&
                    Double.parseDouble(edit_profit_amount.getText().toString()) < profit_max_amount) {
                double a = TradeUtil.add(Double.parseDouble(edit_profit_amount.getText().toString()), TradeUtil.scale(2));
                edit_profit_amount.setText(String.valueOf(a));
                edit_profit_amount.setSelection(String.valueOf(a).length());
            }

        });
        //金额 盈利 减号
        view.findViewById(R.id.text_sub_profit_amount).setOnClickListener(v -> {
            if (Objects.requireNonNull(edit_profit_amount.getText()).toString().length() > 0 &&
                    Double.parseDouble(edit_profit_amount.getText().toString()) > profit_min_amount) {
                double a = TradeUtil.sub(Double.parseDouble(edit_profit_amount.getText().toString()), TradeUtil.scale(2));
                edit_profit_amount.setText(String.valueOf(a));
                edit_profit_amount.setSelection(String.valueOf(a).length());

            }
        });
        //金额 止盈价
        TextView text_stop_profit_amount = view.findViewById(R.id.text_stop_profit_amount);
        text_stop_profit_amount.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
        //金额 盈利百分比 默认
        TextView text_profit_rate_amount = view.findViewById(R.id.text_profit_rate_amount);
        text_profit_rate_amount.setText(profitRate(stopProfit, margin));
        //金额 预计亏损 输入框
        DecimalEditText edit_loss_amount = view.findViewById(R.id.edit_loss_amount);
        edit_loss_amount.setText(String.valueOf(Math.abs(stopLoss)));
        edit_loss_amount.setSelection(String.valueOf(Math.abs(stopLoss)).length());//默认光标在最后面
        edit_loss_amount.setDecimalEndNumber(2);
        //金额 亏损 加号
        view.findViewById(R.id.text_add_loss_amount).setOnClickListener(v -> {
            //当前得小与初始亏损比例
            if (edit_loss_amount.getText().toString().length() > 0 &&
                    Double.parseDouble(edit_loss_amount.getText().toString()) < loss_max_amount
            ) {
                double a = TradeUtil.add(Double.parseDouble(edit_loss_amount.getText().toString()), TradeUtil.scale(2));
                edit_loss_amount.setText(String.valueOf(a));
                edit_loss_amount.setSelection(String.valueOf(a).length());

            }
        });
        //金额 亏损 减号
        view.findViewById(R.id.text_sub_loss_amount).setOnClickListener(v -> {
            if (edit_loss_amount.getText().toString().length() > 0 &&
                    Double.parseDouble(edit_loss_amount.getText().toString()) > loss_min_amount) {
                double a = TradeUtil.sub(Double.parseDouble(edit_loss_amount.getText().toString()), TradeUtil.scale(2));
                edit_loss_amount.setText(String.valueOf(a));
                edit_loss_amount.setSelection(String.valueOf(a).length());
            }

        });
        //金额 止损价
        TextView text_stop_loss_amount = view.findViewById(R.id.text_stop_loss_amount);
        text_stop_loss_amount.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
        //金额 亏损百分比 默认
        TextView text_loss_rate_amount = view.findViewById(R.id.text_loss_rate_amount);
        text_loss_rate_amount.setText(profitRate(Double.parseDouble(edit_loss_amount.getText().toString()), margin));




        /* -----------------------------------------------------------价格---------------------------------------------------------------------------*/


        //价格 止盈价 输入框
        DecimalEditText edit_stop_profit_price = view.findViewById(R.id.edit_stop_profit_price);
        edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit));
        edit_stop_profit_price.setSelection(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, stopProfit).length());
        edit_stop_profit_price.setDecimalEndNumber(priceDigit);
        //价格 盈利金额
        TextView text_profit_amount_price = view.findViewById(R.id.text_profit_amount_price);
        text_profit_amount_price.setText(String.valueOf(stopProfit));
        //价格 止盈价 加
        view.findViewById(R.id.text_add_profit_price).setOnClickListener(v -> {
            if (edit_stop_profit_price.getText().toString().length() > 0 /*&&
                    Double.parseDouble(edit_stop_profit_price.getText().toString()) < small(profit_min_price,profit_max_price)*/) {
                double a = TradeUtil.add(Double.parseDouble(edit_stop_profit_price.getText().toString()), TradeUtil.scale(priceDigit));
                edit_stop_profit_price.setText(String.valueOf(a));
                edit_stop_profit_price.setSelection(String.valueOf(a).length());
            }
        });
        //价格 止盈价 减
        view.findViewById(R.id.text_sub_profit_price).setOnClickListener(v -> {
            if (edit_stop_profit_price.getText().toString().length() > 0 /*&&
                    Double.parseDouble(edit_stop_profit_price.getText().toString()) > big(profit_min_price,profit_max_price)*/) {
                double a = TradeUtil.sub(Double.parseDouble(edit_stop_profit_price.getText().toString()), TradeUtil.scale(priceDigit));
                edit_stop_profit_price.setText(String.valueOf(a));
                edit_stop_profit_price.setSelection(String.valueOf(a).length());

            }
        });
        //价格 盈利百分比 默认
        TextView text_profit_rate_price = view.findViewById(R.id.text_profit_rate_price);
        text_profit_rate_price.setText(profitRate(stopProfit, margin));
        //价格 止损价 输入框
        DecimalEditText edit_stop_loss_price = view.findViewById(R.id.edit_stop_loss_price);
        edit_stop_loss_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)));
        edit_stop_loss_price.setSelection(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(stopLoss)).length());
        edit_stop_loss_price.setDecimalEndNumber(priceDigit);

        //价格 止损价 加
        view.findViewById(R.id.text_add_loss_price).setOnClickListener(v -> {
            if (edit_stop_loss_price.getText().toString().length() > 0 /*&&
                    Double.parseDouble(edit_stop_loss_price.getText().toString()) < loss_max_price*/) {
                double a = TradeUtil.add(Double.parseDouble(edit_stop_loss_price.getText().toString()), TradeUtil.scale(priceDigit));
                edit_stop_loss_price.setText(String.valueOf(a));
                edit_stop_loss_price.setSelection(String.valueOf(a).length());

            }
        });
        //价格 止损价 减
        view.findViewById(R.id.text_sub_loss_price).setOnClickListener(v -> {
            if (Objects.requireNonNull(edit_stop_loss_price.getText()).toString().length() > 0/* &&
                    Double.parseDouble(edit_stop_loss_price.getText().toString()) > loss_min_price*/) {
                double a = TradeUtil.sub(Double.parseDouble(edit_stop_loss_price.getText().toString()), TradeUtil.scale(priceDigit));
                edit_stop_loss_price.setText(String.valueOf(a));
                edit_stop_loss_price.setSelection(String.valueOf(a).length());

            }
        });
        //价格 亏损金额
        TextView text_loss_amount_price = view.findViewById(R.id.text_loss_amount_price);
        text_loss_amount_price.setText(String.valueOf(Math.abs(stopLoss)));
        //价格 亏损百分比
        TextView text_loss_rate_price = view.findViewById(R.id.text_loss_rate_price);
        text_loss_rate_price.setText(profitRate(Double.parseDouble(edit_loss_amount.getText().toString()), margin));


        /* -----------------------------------------------------------金额监听---------------------------------------------------------------------------*/


        //金额 预计盈利 监听
        edit_profit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdit_profit_amount) {
                    if (TextUtils.isEmpty(s)) {
                        edit_profit_amount.setHint(profit_min_amount + "~" + profit_max_amount);
                        text_profit_amount_price.setText(String.valueOf(profit_min_amount));
                        text_profit_rate_amount.setText("0.00%");
                        text_profit_rate_price.setText("0.00%");
                        edit_stop_profit_price.setText(text_stop_profit_amount.getText().toString());


                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (Double.parseDouble(s.toString()) > profit_max_amount) {
                                Log.d("print", "onTextChanged:445: " + String.valueOf(profit_max_amount));
                                edit_profit_amount.setText(String.valueOf(profit_max_amount));
                                text_stop_profit_amount.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_max_amount));
                                text_profit_rate_amount.setText(profitRate(profit_max_amount, margin));
                                edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_max_amount));
                                text_profit_amount_price.setText(String.valueOf(profit_max_amount));
                                text_profit_rate_price.setText(profitRate(profit_max_amount, margin));


                            } else if (Double.parseDouble(s.toString()) < profit_min_amount) {

                                edit_profit_amount.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (s.length() != 0 && Double.parseDouble(s.toString()) < profit_min_amount) {
                                            //金额页面 预计盈利 输入
                                            edit_profit_amount.setText(String.valueOf(profit_min_amount));
                                            //金额页面 止盈价
                                            text_stop_profit_amount.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_min_amount));
                                            //金额页面 盈利百分比
                                            text_profit_rate_amount.setText(profitRate(profit_min_amount, margin));
                                            //价格页面  止盈价输入框
                                            edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, profit_min_amount));
                                            //价格页面 盈利金额 直接输入获取
                                            text_profit_amount_price.setText(String.valueOf(profit_min_amount));
                                            //价格页面 盈利百分比
                                            text_profit_rate_price.setText(profitRate(profit_min_amount, margin));


                                        }
                                    }
                                }, 1000);
                            } else {
                                //金额页面 止盈价
                                text_stop_profit_amount.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //金额页面 盈利百分比
                                text_profit_rate_amount.setText(profitRate(Double.parseDouble(s.toString()), margin));
                                //价格页面  止盈价输入框
                                edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //价格页面 盈利金额 直接输入获取
                                text_profit_amount_price.setText(s.toString());
                                //价格页面 盈利百分比
                                text_profit_rate_price.setText(profitRate(Double.parseDouble(s.toString()), margin));


                            }
                        } else {
                            String s1 = s.toString().replace(".", "");
                            edit_profit_amount.setText(s1);
                            text_profit_rate_amount.setText(profitRate(Double.parseDouble(s1), margin));
                            text_profit_rate_price.setText(profitRate(Double.parseDouble(s1), margin));
                            edit_stop_profit_price.setText(TradeUtil.StopProfitPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s1)));


                        }


                    }
                } else {
                }
            }
        });

        //金额 预计亏损 监听
        edit_loss_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdit_loss_amount) {
                    if (TextUtils.isEmpty(s)) {
                        edit_loss_amount.setHint(loss_min_amount + "~" + loss_max_amount);
                        text_loss_amount_price.setText(String.valueOf(loss_min_amount));
                        text_loss_rate_amount.setText("0.00%");
                        text_loss_rate_price.setText("0.00%");
                        edit_stop_loss_price.setText(text_stop_loss_amount.getText().toString());

                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (Double.parseDouble(s.toString()) > loss_max_amount) {
                                edit_loss_amount.setText(String.valueOf(loss_max_amount));
                                text_stop_loss_amount.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_max_amount));
                                text_loss_rate_amount.setText(profitRate(loss_max_amount, margin));
                                edit_stop_loss_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_max_amount));
                                text_loss_rate_price.setText(profitRate(loss_max_amount, margin));
                                text_loss_amount_price.setText(String.valueOf(loss_max_amount));

                            } else if (Double.parseDouble(s.toString()) < loss_min_amount) {

                                edit_loss_amount.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (s.length() != 0 && Double.parseDouble(s.toString()) < loss_min_amount) {
                                            //金额 预计亏损 输入框
                                            edit_loss_amount.setText(String.valueOf(loss_min_amount));
                                            //金额 止损价
                                            text_stop_loss_amount.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_min_amount));
                                            //金额 亏损百分比
                                            text_loss_rate_amount.setText(profitRate(loss_min_amount, margin));
                                            //价格 止损价 输入框
                                            edit_stop_loss_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, loss_min_amount));
                                            //价格 亏损百分比
                                            text_loss_rate_price.setText(profitRate(loss_min_amount, margin));
                                            //价格 亏损金额
                                            text_loss_amount_price.setText(String.valueOf(loss_min_amount));


                                        }
                                    }
                                }, 1000);
                            } else {
                                //金额 止损价
                                text_stop_loss_amount.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //金额 亏损百分比
                                text_loss_rate_amount.setText(profitRate(Double.parseDouble(s.toString()), margin));
                                //价格 止损价 输入框
                                edit_stop_loss_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //价格 亏损金额
                                text_loss_amount_price.setText(s.toString());
                                //价格 亏损百分比
                                text_loss_rate_price.setText(profitRate(Double.parseDouble(s.toString()), margin));

                            }
                        } else {
                            String s1 = s.toString().replace(".", "");
                            edit_loss_amount.setText(s1);
                            text_loss_rate_amount.setText(profitRate(Double.parseDouble(s1), margin));
                            text_loss_rate_price.setText(profitRate(Double.parseDouble(s1), margin));
                            edit_stop_loss_price.setText(TradeUtil.StopLossPrice(isBuy, price, priceDigit, lever, margin, Math.abs(Double.parseDouble(s1))));

                        }
                    }
                }
            }
        });

        /* -----------------------------------------------------------价格监听---------------------------------------------------------------------------*/

        //价格 止盈价 监听
        edit_stop_profit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdit_profit_price) {
                    if (TextUtils.isEmpty(s)) {
                        if (isBuy) {
                            edit_stop_profit_price.setHint(profit_min_price + "~" + profit_max_price);
                        } else {
                            edit_stop_profit_price.setHint(profit_max_price + "~" + profit_min_price);
                        }

                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (Double.parseDouble(s.toString()) > big(profit_min_price, profit_max_price)) {
                                //价格 止盈价 输入框
                                edit_stop_profit_price.setText(String.valueOf(big(profit_min_price, profit_max_price)));
                                //价格 盈利金额
                                text_profit_amount_price.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price)));
                                //价格 盈利百分比
                                text_profit_rate_price.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price))), margin));
                                //金额 预计盈利 输入框
                                edit_profit_amount.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price)));
                                //金额 止盈价
                                text_stop_profit_amount.setText(String.valueOf(big(profit_min_price, profit_max_price)));
                                //金额 盈利百分比
                                text_profit_rate_amount.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, big(profit_min_price, profit_max_price))), margin));

                            } else if (Double.parseDouble(s.toString()) < small(profit_min_price, profit_max_price)) {

                                edit_stop_profit_price.postDelayed(() -> {
                                    if (s.length() != 0 && Double.parseDouble(s.toString()) < small(profit_min_price, profit_max_price)) {
                                        //价格 止盈价 输入框
                                        edit_stop_profit_price.setText(String.valueOf(small(profit_min_price, profit_max_price)));
                                        //价格 盈利金额
                                        text_profit_amount_price.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, small(profit_min_price, profit_max_price)));
                                        //价格 盈利百分比
                                        text_profit_rate_price.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, small(profit_min_price, profit_max_price))), margin));
                                        //金额 预计盈利 输入框
                                        edit_profit_amount.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, small(profit_min_price, profit_max_price)));
                                        //金额 止盈价
                                        text_stop_profit_amount.setText(String.valueOf(small(profit_min_price, profit_max_price)));
                                        //金额 盈利百分比
                                        text_profit_rate_amount.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, small(profit_min_price, profit_max_price))), margin));
                                    }
                                }, 3000);
                            } else {
                                //价格 盈利金额
                                text_profit_amount_price.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //价格 盈利百分比
                                text_profit_rate_price.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString()))), margin));
                                //金额 预计盈利 输入框
                                edit_profit_amount.setText(ProfitAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //金额 止盈价
                                text_stop_profit_amount.setText(s.toString());
                                //金额 盈利百分比
                                text_profit_rate_amount.setText(profitRate(Double.parseDouble(ProfitAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString()))), margin));


                            }
                        } else {
                            String replace = s.toString().replace(".", "");
                            edit_stop_profit_price.setText(replace);
                            text_stop_profit_amount.setText(replace);


                        }
                    }
                }
            }
        });

        //价格 止损价 监听
        edit_stop_loss_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdit_loss_price) {
                    if (TextUtils.isEmpty(s)) {
                        if (isBuy) {
                            edit_stop_loss_price.setHint(loss_max_price + "~" + loss_min_price);
                        } else {
                            edit_stop_loss_price.setHint(loss_min_price + "~" + loss_max_price);
                        }

                    } else {
                        if (!s.toString().startsWith(".")) {
                            if (Double.parseDouble(s.toString()) > big(loss_min_price, loss_max_price)) {
                                //价格 止损价 输入框
                                edit_stop_loss_price.setText(String.valueOf(big(loss_min_price, loss_max_price)));//ok
                                //价格 亏损金额
                                text_loss_amount_price.setText(lossAmount(isBuy, price, priceDigit, lever, margin, big(loss_min_price, loss_max_price)));
                                //价格 亏损百分比
                                text_loss_rate_price.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, big(loss_min_price, loss_max_price))), margin));
                                //金额 预计亏损 输入框
                                edit_loss_amount.setText(lossAmount(isBuy, price, priceDigit, lever, margin, big(loss_min_price, loss_max_price)));
                                //金额 止损价
                                text_stop_loss_amount.setText(String.valueOf(big(loss_min_price, loss_max_price)));
                                //金额 亏损百分比
                                text_loss_rate_amount.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, big(loss_min_price, loss_max_price))), margin));


                            } else if (Double.parseDouble(s.toString()) < small(loss_min_price, loss_max_price)) {

                                edit_loss_amount.postDelayed(() -> {
                                    if (s.length() != 0 && Double.parseDouble(s.toString()) < small(loss_min_price, loss_max_price)) {
                                        //价格 止损价 输入框
                                        edit_stop_loss_price.setText(String.valueOf(small(loss_min_price, loss_max_price)));//ok
                                        //价格 亏损金额
                                        text_loss_amount_price.setText(lossAmount(isBuy, price, priceDigit, lever, margin, small(loss_min_price, loss_max_price)));
                                        //价格 亏损百分比
                                        text_loss_rate_price.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, small(loss_min_price, loss_max_price))), margin));
                                        //金额 预计亏损 输入框
                                        edit_loss_amount.setText(lossAmount(isBuy, price, priceDigit, lever, margin, small(loss_min_price, loss_max_price)));
                                        //金额 止损价
                                        text_stop_loss_amount.setText(String.valueOf(small(loss_min_price, loss_max_price)));
                                        //金额 亏损百分比
                                        text_loss_rate_amount.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, small(loss_min_price, loss_max_price))), margin));


                                    }
                                }, 1000);
                            } else {

                                //价格 亏损金额
                                text_loss_amount_price.setText(lossAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //价格 亏损百分比
                                text_loss_rate_price.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString()))), margin));
                                //金额 预计亏损 输入框
                                edit_loss_amount.setText(lossAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString())));
                                //金额 止损价
                                text_stop_loss_amount.setText(s.toString());
                                //金额 亏损百分比
                                text_loss_rate_amount.setText(lossRate(Double.parseDouble(lossAmount(isBuy, price, priceDigit, lever, margin, Double.parseDouble(s.toString()))), margin));

                            }
                        } else {
                            String s1 = s.toString().replace(".", "");
                            edit_stop_loss_price.setText(s1);
                            text_stop_loss_amount.setText(s1);

                        }
                    }
                }
            }
        });


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            String rateProfit = numberHalfUp(TradeUtil.div(Double.parseDouble(edit_profit_amount.getText().toString()), margin, priceDigit), 2);
            String rateLoss = numberHalfUp(TradeUtil.div(Double.parseDouble(edit_loss_amount.getText().toString()), margin, priceDigit), 2);

            NetManger.getInstance().submitSPSL(data.getId(),
                    tradeType, rateProfit,
                    "-" + rateLoss,
                    (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            popupWindow.dismiss();
                            backgroundAlpha(1f);
                            initData();

                            Toast.makeText(getActivity(), getResources().getText(R.string.text_success), Toast.LENGTH_SHORT).show();

                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
        });


        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.6f;
        getActivity().getWindow().setAttributes(params);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        // popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }


    public void backgroundAlpha(float bgalpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgalpha;
        getActivity().getWindow().setAttributes(lp);


    }


    /*设置浮动盈亏*/
    private void setIncome(List<String> quoteList, PositionEntity positionEntity) {
        if (positionEntity == null) {
            return;
        }
        TradeUtil.getIncome(quoteList, positionEntity, response -> {
            Double incomeAll = (Double) response;
            if (incomeAll > 0) {
                text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_green));
            } else {
                text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_red));
            }
            text_incomeAll.setText(getNumberFormat(Double.parseDouble(response.toString()), 2));

        });
    }


    public void setNetIncome(String tradeType, List<PositionEntity.DataBean> positionList, List<String> quoteList) {

        TradeUtil.getNetIncome(quoteList, positionList, response1 -> TradeUtil.getMargin(positionList, response2 -> {
            double margin;
            double income;
            if (positionEntity == null) {
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
            NetIncomeManger.getInstance().postNetIncome(append.toString());
        }));

    }




    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        //余额初始化
        BalanceManger.getInstance().getBalance("USDT");
        //持仓初始化
        PositionRealManger.getInstance().getHold();
        PositionSimulationManger.getInstance().getHold();


        NetManger.getInstance().getHold(tradeType, (state, response1, response2) -> {
            if (state.equals(BUSY)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            } else if (state.equals(SUCCESS)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                positionEntity = (PositionEntity) response1;
                positionAdapter.setDatas(positionEntity.getData(), quoteList);

                //这里根据持仓来是否显示头部视图
                if (positionEntity.getData().size() == 0) {
                    text_incomeAll.setText("");
                    headerRecyclerView.removeHeaderView(headView);
                } else {
                    //防止刷新已经有头布局 继续添加出现的bug
                    if (headerRecyclerView != null) {
                        if (headerRecyclerView.getHeadersCount() == 0) {
                            headerRecyclerView.addHeaderView(headView);
                        }
                    }
                }

            } else if (state.equals(FAILURE)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuoteListManger.getInstance().clear();
        TagManger.getInstance().clear();
    }


    @Override
    public void update(Observable o, Object arg) {

        if (o == QuoteListManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get("0");
            //OpenPositionEntity{code=200, message='', data=[]}    或者是null
            if (positionEntity != null && positionEntity.getData().size() > 0) {
                runOnUiThread(() -> {
                    if (isLogin()) {
                        //整体盈亏
                        setIncome(quoteList, positionEntity);
                        //整体净值
                        setNetIncome(tradeType, positionEntity.getData(), quoteList);
                        positionAdapter.setDatas(positionEntity.getData(), quoteList);
                    } else {
                        positionEntity.getData().clear();
                        positionAdapter.setDatas(positionEntity.getData(), quoteList);
                        headerRecyclerView.removeHeaderView(headView);

                    }

                    //pop 实时价格也是同步刷新
                    if (text_price != null) {
                        price(quoteList, contractCode, response -> text_price.setText(response.toString()));
                    }
                });
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder append = stringBuilder.append(tradeType).append(",").append("0.0")
                        .append(",").append("0.0");
                NetIncomeManger.getInstance().postNetIncome(append.toString());

            }


        } else if (o == TagManger.getInstance()) {

            if (isLogin()) {
                headerRecyclerView.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
            } else {
                headerRecyclerView.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
            initData();
        }
    }


}
