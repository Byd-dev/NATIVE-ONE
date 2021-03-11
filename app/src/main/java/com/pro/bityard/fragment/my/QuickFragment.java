package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuickAccountAdapter;
import com.pro.bityard.adapter.SelectQuickAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class QuickFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private BalanceEntity balanceEntity;

    private QuickAccountAdapter quickAccountAdapter;

    private SelectQuickAdapter selectQuickAdapter;
    @BindView(R.id.recyclerView_quick)
    RecyclerView recyclerView_quick;
    private LinearLayout layout_switch;
    private EditText edit_amount;
    private EditText edit_amount_transfer;
    private TextView text_currency;
    private ImageView img_bg;
    private TextView text_price;
    private TextView text_balance;
    private TextView text_all_exchange;
    private String rate = "1.00";

    @BindView(R.id.img_line)
    ImageView img_line;

    private boolean isEdit_amount = true;
    private boolean isEdit_amount_transfer = true;
    private double money;
    private Double amount_transfer;
    private String currency;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_coin_turn));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        //  View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_quick_head, null);
        layout_switch = view.findViewById(R.id.layout_switch);
        edit_amount = view.findViewById(R.id.edit_amount);
        edit_amount_transfer = view.findViewById(R.id.edit_amount_transfer);
        text_balance = view.findViewById(R.id.text_balance);
        text_currency = view.findViewById(R.id.text_currency);
        img_bg = view.findViewById(R.id.img_bg);
        text_price = view.findViewById(R.id.text_price);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);


        layout_switch.setOnClickListener(this);
        //全部兑换监听
        view.findViewById(R.id.text_all_exchange).setOnClickListener(v -> {
            edit_amount.setText(String.valueOf(money));
        });


        BalanceManger.getInstance().addObserver(this);

        quickAccountAdapter = new QuickAccountAdapter(getActivity());
        recyclerView_quick.setLayoutManager(new LinearLayoutManager(getActivity()));

        //recyclerView_quick.addHeaderView(headView);
        recyclerView_quick.setAdapter(quickAccountAdapter);
        Util.colorSwipe(getActivity(), swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this::initData);

        selectQuickAdapter = new SelectQuickAdapter(getActivity());


        img_line.setImageDrawable(getResources().getDrawable(R.mipmap.icon_quick_up));


        edit_amount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isEdit_amount = true;
                img_line.setImageDrawable(getResources().getDrawable(R.mipmap.icon_quick_up));
            }
        });

        edit_amount_transfer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isEdit_amount_transfer = true;
                img_line.setImageDrawable(getResources().getDrawable(R.mipmap.icon_quick_down));
            }
        });


        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (isEdit_amount) {
                        isEdit_amount_transfer = false;
                        double input_amount = Double.parseDouble(s.toString());
                        edit_amount_transfer.setText(TradeUtil.numberHalfUp(TradeUtil.mul(input_amount, Double.parseDouble(rate)), 2));
                        if (input_amount > money) {
                            edit_amount.setText(String.valueOf(money));
                        }
                    }
                } else {
                    edit_amount_transfer.setText(getResources().getString(R.string.text_default));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edit_amount_transfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0) {
                    if (isEdit_amount_transfer) {
                        isEdit_amount = false;
                        double input_amount_transfer = Double.parseDouble(s.toString());
                        Log.d("print", "onTextChanged:179:  " + input_amount_transfer + "   " + rate + "    " + TradeUtil.div(input_amount_transfer, Double.parseDouble(rate), 4));
                        if (input_amount_transfer > amount_transfer) {
                            edit_amount_transfer.setText(String.valueOf(amount_transfer));
                        } else if (input_amount_transfer < amount_transfer) {
                            edit_amount.setText(TradeUtil.numberHalfUp(TradeUtil.div(input_amount_transfer, Double.parseDouble(rate), 10), 8));
                        } else if (input_amount_transfer == amount_transfer) {
                            edit_amount.setText(String.valueOf(money));
                        }
                    }

                } else {
                    edit_amount.setText(getResources().getString(R.string.text_default));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_quick;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setRefreshing(false);
        BalanceManger.getInstance().getBalance("USDT");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.layout_switch:
                showSwitchPopWindow();
                break;
            case R.id.btn_submit:
                String value_amount = edit_amount.getText().toString();
                NetManger.getInstance().exchange(currency, value_amount, "USDT", (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        Toast.makeText(getActivity(), R.string.text_exchange_success, Toast.LENGTH_SHORT).show();


                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                        Toast.makeText(getActivity(), "FAILURE", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }

    private void showSwitchPopWindow() {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_quick_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_switch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(selectQuickAdapter);

        selectQuickAdapter.setOnItemClick(data -> {
            //防止输入框的监听
            isEdit_amount_transfer = false;
            isEdit_amount = false;
            money = data.getMoney();


            currency = data.getCurrency();

            TradeUtil.getScale(currency, response -> {
                int scale = (int) response;
                edit_amount.setText(TradeUtil.numberHalfUp(money, scale));
                text_balance.setText(TradeUtil.numberHalfUp(money, scale) + currency);
            });
            text_currency.setText(currency);
            ChartUtil.setIcon(currency, img_bg);
            popupWindow.dismiss();
            getRate(currency, money, (state, response1, response2) -> {
                rate = response1.toString();
                if (rate.equals("0.00")) {
                    rate = "1.00";
                }
                text_price.setText("1" + currency + "≈" + response1 + "USDT");
                edit_amount_transfer.setText(response2.toString());
                amount_transfer = Double.parseDouble(response2.toString());

            });


        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_switch, 0, 0, Gravity.CENTER);
    }

    private void getRate(String currency, double money, OnNetTwoResult onNetTwoResult) {
        RateListEntity rateListEntity = SPUtils.getData(AppConfig.RATE_LIST, RateListEntity.class);
        if (rateListEntity != null) {
            for (RateListEntity.ListBean rateList : rateListEntity.getList()) {
                if (currency.equals(rateList.getName())) {
                    double mul = TradeUtil.mul(money, rateList.getValue());
                    onNetTwoResult.setResult(SUCCESS, rateList.getValue(), TradeUtil.getNumberFormat(mul, 2));
                }
            }
        }
    }

    List<BalanceEntity.DataBean> dataSelect, dataBalance;

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            if (quickAccountAdapter != null && balanceEntity != null) {
                List<BalanceEntity.DataBean> data = balanceEntity.getData();
                Log.d("print", "update:321: " + data);
                dataSelect = new ArrayList<>();
                dataBalance = new ArrayList<>();
                for (BalanceEntity.DataBean item : data) {
                    if (item.getCurrency().toLowerCase().equals("eth")
                            || item.getCurrency().toLowerCase().equals("btc")
                            || item.getCurrency().toLowerCase().equals("trx")
                            || item.getCurrency().toLowerCase().equals("xrp")
                            || item.getCurrency().toLowerCase().equals("eos")) {
                        dataSelect.add(item);
                    }
                }


                for (BalanceEntity.DataBean item : data) {
                    if (item.getCurrency().toLowerCase().equals("usdt") ||
                            item.getCurrency().toLowerCase().equals("eth")
                            || item.getCurrency().toLowerCase().equals("btc")
                            || item.getCurrency().toLowerCase().equals("trx")
                            || item.getCurrency().toLowerCase().equals("xrp")
                            || item.getCurrency().toLowerCase().equals("eos")) {
                        dataBalance.add(item);
                    }
                }
                quickAccountAdapter.setDatas(dataBalance);
                Iterator<BalanceEntity.DataBean> iterator = dataSelect.iterator();
                while (iterator.hasNext()) {
                    BalanceEntity.DataBean value = iterator.next();
                    if (value.getCurrency().equals("USDT")) {
                        iterator.remove();
                    }
                }
                money = dataSelect.get(0).getMoney();
                selectQuickAdapter.setDatas(dataSelect);
                edit_amount.setText(TradeUtil.getNumberFormat(money, 2));
                currency = dataSelect.get(0).getCurrency();
                text_balance.setText(TradeUtil.getNumberFormat(money, 2) + currency);
                text_currency.setText(currency);
                ChartUtil.setIcon(currency, img_bg);
                getRate(currency, money, (state, response1, response2) -> {
                    rate = response1.toString();
                    if (rate.equals("0.00")) {
                        rate = "1.00";
                    }
                    text_price.setText("1" + currency + "≈" + response1 + "USDT");
                    edit_amount_transfer.setText(response2.toString());
                    amount_transfer = Double.parseDouble(response2.toString());

                });
            }
        }
    }
}
