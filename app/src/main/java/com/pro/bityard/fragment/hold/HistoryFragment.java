package com.pro.bityard.fragment.hold;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.HistoryAdapter;
import com.pro.bityard.adapter.MarginHistoryAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.entity.MarginHistoryEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.utils.TradeUtil.netIncome;
import static com.pro.bityard.utils.TradeUtil.numberHalfUp;

public class HistoryFragment extends BaseFragment implements Observer {
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.btn_login)
    Button btn_login;
    private HistoryAdapter historyAdapter;
    private MarginHistoryAdapter marginHistoryAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private HistoryEntity historyEntity;


    public HistoryFragment newInstance(String type) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("tradeType", type);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tradeType = getArguments().getString("tradeType");
        }
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
            layout_null.setVisibility(View.GONE);
        }

        initData();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        TagManger.getInstance().addObserver(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        @SuppressLint("InflateParams") View footView = LayoutInflater.from(getContext()).inflate(R.layout.tab_foot_view, null);

        headerRecyclerView.addFooterView(footView);
        historyAdapter = new HistoryAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        headerRecyclerView.setAdapter(historyAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::initData);


        historyAdapter.setOnDetailClick(data -> {
            showDetailPopWindow(getActivity(), layout_view, data);

        });

        //分享监听
        historyAdapter.setOnShareClick(data -> {
            Util.lightOff(getActivity());
            PopUtil.getInstance().showSharePlatform(getActivity(), layout_view, data);

        });

        btn_login.setOnClickListener(v -> {
            LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);
        });


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_open;
    }

    @Override
    protected void intPresenter() {

    }


    /*显示详情*/
    private void showDetailPopWindow(Activity activity, View layout_view, HistoryEntity.DataBean dataBean) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_history_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_history_detail);

        TextView text_name = view.findViewById(R.id.text_name);

        TextView text_gift_money = view.findViewById(R.id.text_gift_money);

        text_gift_money.setText(TradeUtil.getNumberFormat(dataBean.getLuckyDeduction(),2));

        TextView text_bonus = view.findViewById(R.id.text_bonus);
        text_bonus.setText(TradeUtil.getNumberFormat(dataBean.getMarginPrize(),2));


        String[] split = Util.quoteList(dataBean.getContractCode()).split(",");
        text_name.setText(split[0]);
        TextView text_currency = view.findViewById(R.id.text_currency);
        text_currency.setText("/" + dataBean.getCurrency());
        boolean isBuy = dataBean.isIsBuy();
        TextView text_side = view.findViewById(R.id.text_side);
        if (isBuy) {
            text_side.setText(R.string.text_buy_much);
        } else {
            text_side.setText(R.string.text_buy_empty);
        }
        //盈利
        TextView text_income = view.findViewById(R.id.text_income);
        //净收入
        //开盘价
        double opPrice = dataBean.getOpPrice();
        double cpPrice = dataBean.getCpPrice();
        int priceDigit = dataBean.getPriceDigit();
        double lever = dataBean.getLever();
        double margin = dataBean.getMargin();

        //开仓价
        TextView text_open_price = view.findViewById(R.id.text_open_price);
        text_open_price.setText(String.valueOf(opPrice));
        //平仓价
        TextView text_close_price = view.findViewById(R.id.text_close_price);
        text_close_price.setText(String.valueOf(cpPrice));
        //服务费
        double serviceCharge = dataBean.getServiceCharge();

        double income = dataBean.getIncome();

        text_income.setText(numberHalfUp(income, 2));
        TextView text_rate = view.findViewById(R.id.text_rate);
        text_rate.setText(TradeUtil.ratio(income, margin));

        String netIncome = netIncome(income, serviceCharge);


        if (income > 0) {
            text_income.setTextColor(activity.getResources().getColor(R.color.text_quote_green));
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_green));

        } else {
            text_income.setTextColor(activity.getResources().getColor(R.color.text_quote_red));
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_red));

        }


        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(margin));

        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(lever + "×");
        TextView text_orders = view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));

        TextView text_fee = view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge));
        /*TextView text_o_n = view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));*/
        TextView text_o_n_fee = view.findViewById(R.id.text_o_n_fee);
        String deferDays = dataBean.getDeferDays();
        String deferFee = dataBean.getDeferFee();
        double mul = TradeUtil.mul(Double.parseDouble(deferDays), Double.parseDouble(deferFee));

        text_o_n_fee.setText(TradeUtil.numberHalfUp(mul,2));
        TextView text_close_time = view.findViewById(R.id.text_close_time);
        text_close_time.setText(ChartUtil.getDate(dataBean.getTradeTime()));


        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });


        TextView text_id = view.findViewById(R.id.text_order_id);
        text_id.setText(dataBean.getId());

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));


        marginHistoryAdapter = new MarginHistoryAdapter(getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(marginHistoryAdapter);

        getMarginHistory(dataBean, swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getMarginHistory(dataBean, swipeRefreshLayout);
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }


    private void getMarginHistory(HistoryEntity.DataBean dataBean, SwipeRefreshLayout swipeRefreshLayout) {
        NetManger.getInstance().marginHistory(dataBean.getId(), (state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                MarginHistoryEntity marginHistoryEntity = (MarginHistoryEntity) response;
                List<MarginHistoryEntity.DataBean> data = marginHistoryEntity.getData();
                marginHistoryAdapter.setDatas(data);
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    protected void initData() {

        if (isLogin()) {
            NetManger.getInstance().getHistory(tradeType, (state, response) -> {
                if (state.equals(BUSY)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                } else if (state.equals(SUCCESS)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    historyEntity = (HistoryEntity) response;
                    if (historyEntity.getData().size() == 0) {
                        layout_null.setVisibility(View.VISIBLE);
                        headerRecyclerView.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        headerRecyclerView.setVisibility(View.VISIBLE);
                    }
                    historyAdapter.setDatas(historyEntity.getData());
                } else if (state.equals(FAILURE)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
            });
        } else {
            if (historyEntity != null) {
                historyEntity.getData().clear();
                historyAdapter.setDatas(historyEntity.getData());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

        }


    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == TagManger.getInstance()) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        TagManger.getInstance().clear();
    }
}
