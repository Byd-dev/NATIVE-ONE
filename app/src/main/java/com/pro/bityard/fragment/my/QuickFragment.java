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

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuickAccountAdapter;
import com.pro.bityard.adapter.SelectQuickAdapter;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
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
    HeaderRecyclerView recyclerView_quick;
    private LinearLayout layout_switch;
    private EditText edit_amount;
    private EditText edit_amount_transfer;
    private TextView text_currency;
    private ImageView img_bg;
    private TextView text_price;
    private TextView text_balance;
    private TextView text_all_exchange;
    private String rate;

    private boolean isEdit_amount = true;
    private boolean isEdit_amount_transfer = false;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_coin_turn));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_quick_head, null);
        layout_switch = headView.findViewById(R.id.layout_switch);
        edit_amount = headView.findViewById(R.id.edit_amount);
        edit_amount_transfer = headView.findViewById(R.id.edit_amount_transfer);
        text_balance = headView.findViewById(R.id.text_balance);
        text_currency = headView.findViewById(R.id.text_currency);
        img_bg = headView.findViewById(R.id.img_bg);
        text_price = headView.findViewById(R.id.text_price);

        layout_switch.setOnClickListener(this);
        headView.findViewById(R.id.text_all_exchange).setOnClickListener(v -> {

        });


        BalanceManger.getInstance().addObserver(this);

        quickAccountAdapter = new QuickAccountAdapter(getActivity());
        recyclerView_quick.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView_quick.addHeaderView(headView);
        recyclerView_quick.setAdapter(quickAccountAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        swipeRefreshLayout.setOnRefreshListener(this::initData);

        selectQuickAdapter = new SelectQuickAdapter(getActivity());

        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (Util.isNumber(s.toString())) {
                        Log.d("print", "onTextChanged:117: "+s.toString()+"       "+rate);
                        edit_amount_transfer.setText(String.valueOf(TradeUtil.mul(Double.parseDouble(edit_amount.getText().toString()), Double.parseDouble(rate))));
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
                    if (Util.isNumber(s.toString())) {
                        Log.d("print", "onTextChanged:143: "+s.toString()+"       "+rate);
                        edit_amount.setText(String.valueOf(TradeUtil.div(Double.parseDouble(edit_amount_transfer.getText().toString()), Double.parseDouble(rate), 10)));
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
            double money = data.getMoney();
            edit_amount.setText(TradeUtil.getNumberFormat(money, 2));
            text_balance.setText(TradeUtil.getNumberFormat(money, 2));
            String currency = data.getCurrency();
            text_currency.setText(currency);
            ChartUtil.setIcon(currency, img_bg);
            popupWindow.dismiss();
            getRate(currency, money, (state, response1, response2) -> {
                rate = response1.toString();
                text_price.setText("1" + currency + "≈" + response1 + "USDT");
                edit_amount_transfer.setText(response2.toString());

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

    List<BalanceEntity.DataBean> dataSelect;

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            if (quickAccountAdapter != null && balanceEntity != null) {
                List<BalanceEntity.DataBean> data = balanceEntity.getData();
                quickAccountAdapter.setDatas(data);

                dataSelect = new ArrayList<>();
                dataSelect.addAll(data);
                Iterator<BalanceEntity.DataBean> iterator = dataSelect.iterator();
                while (iterator.hasNext()) {
                    BalanceEntity.DataBean value = iterator.next();
                    if (value.getCurrency().equals("USDT")) {
                        iterator.remove();
                    }
                }
                double money = dataSelect.get(0).getMoney();
                selectQuickAdapter.setDatas(dataSelect);
                edit_amount.setText(TradeUtil.getNumberFormat(money, 2));
                text_balance.setText(TradeUtil.getNumberFormat(money, 2));
                String currency = dataSelect.get(0).getCurrency();
                text_currency.setText(currency);
                ChartUtil.setIcon(currency, img_bg);
                getRate(currency, money, (state, response1, response2) -> {
                    rate = response1.toString();
                    text_price.setText("1" + currency + "≈" + response1 + "USDT");
                    edit_amount_transfer.setText(response2.toString());

                });
            }
        }
    }
}
