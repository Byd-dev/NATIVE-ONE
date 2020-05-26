package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.FundSelectAdapter;
import com.pro.bityard.adapter.TradeRecordAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FundItemEntity;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class TradeRecordFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.text_title)
    TextView text_title;

    private FundSelectAdapter fundSelectAdapter;

    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.layout_select)
    RelativeLayout layout_select;

    @BindView(R.id.layout_pop_title)
    LinearLayout layout_pop_title;

    @BindView(R.id.text_select)
    TextView text_select;

    @BindView(R.id.img_bg)
    ImageView img_bg;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    private FundItemEntity fundItemEntity;

    private TradeRecordAdapter tradeRecordAdapter;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    private String createTimeGe = null;
    private String createTimeLe = null;

    private String FIRST = "first";
    private String REFRESH = "refresh";
    private String LOAD = "load";

    private int page = 0;
    private String commodity = null;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_trade_record;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.img_back).setOnClickListener(this);

        text_title.setText(R.string.text_orders);

        layout_select.setOnClickListener(this);
        fundSelectAdapter = new FundSelectAdapter(getActivity());


        radioGroup.setOnCheckedChangeListener(this);

        tradeRecordAdapter = new TradeRecordAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tradeRecordAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);


        //监听
        tradeRecordAdapter.setOnItemClick(this::showDetailPopWindow);

    }

    /*显示详情*/
    private void showDetailPopWindow(TradeHistoryEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_trade_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_order_detail);

        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(dataBean.getCommodityCode());

        ImageView img_bg = view.findViewById(R.id.img_bg);
        ChartUtil.setIcon(dataBean.getCommodityCode().substring(0, dataBean.getCommodityCode().length() - dataBean.getCurrency().length()), img_bg);

        TextView text_income = view.findViewById(R.id.text_p_l);
        double income = dataBean.getIncome();

        double serviceCharge = dataBean.getServiceCharge();
        TextView text_profit = view.findViewById(R.id.text_profit);

        text_profit.setText(TradeUtil.getNumberFormat(TradeUtil.sub(income, serviceCharge), 2));

        if (income > 0) {
            text_income.setTextColor(getResources().getColor(R.color.text_quote_green));
        } else {
            text_income.setTextColor(getResources().getColor(R.color.text_quote_red));

        }
        text_income.setText(TradeUtil.getNumberFormat(income, 2));

        TextView text_side=view.findViewById(R.id.text_side);
        boolean isBuy = dataBean.isIsBuy();
        if (isBuy){
            text_side.setText(R.string.text_buy_much);
        }else {
            text_side.setText(R.string.text_buy_empty);
        }

        TextView text_margin=view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(dataBean.getMargin()));

        TextView text_lever=view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(dataBean.getLever()));
        TextView text_orders=view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));
        TextView text_opPrice=view.findViewById(R.id.text_open_price);
        text_opPrice.setText(String.valueOf(dataBean.getOpPrice()));
        TextView text_clPrice=view.findViewById(R.id.text_close_price);
        text_clPrice.setText(String.valueOf(dataBean.getCpPrice()));
        TextView text_fee=view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge));
        TextView text_o_n=view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));
        TextView text_o_n_fee=view.findViewById(R.id.text_o_n_fee);
        text_o_n_fee.setText(String.valueOf(dataBean.getDeferFee()));
        TextView text_currency=view.findViewById(R.id.text_currency_type);
        text_currency.setText(dataBean.getCurrency());
        TextView text_open_time=view.findViewById(R.id.text_open_time);
        text_open_time.setText(ChartUtil.getDate(dataBean.getTime()));
        TextView text_close_time=view.findViewById(R.id.text_close_time);
        text_close_time.setText(ChartUtil.getDate(dataBean.getTradeTime()));
        TextView text_order_id=view.findViewById(R.id.text_order_id);
        text_order_id.setText(dataBean.getId());



        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        NetManger.getInstance().currencyList("1", (state, response) -> {
            if (state.equals(BUSY)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            } else if (state.equals(SUCCESS)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                fundItemEntity = new Gson().fromJson(response.toString(), FundItemEntity.class);
                if (fundItemEntity==null){
                    return;
                }
                if (fundItemEntity.getData()==null){
                    return;
                }
                if (!fundItemEntity.getData().get(0).getName().equals("ALL")) {
                    fundItemEntity.getData().add(0, new FundItemEntity.DataBean("", true, "", "", false, "ALL", 0, 0, 0, ""));
                }

            } else if (state.equals(FAILURE)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        page = 0;
        getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe);

    }


    private void getTradeHistory(String loadType, String nowTime, String commodity, String createTimeGe,
                                 String createTimeLe) {
        NetManger.getInstance().tradeHistory("1", nowTime, "2", commodity, createTimeGe,
                createTimeLe, (state, response) -> {
                    if (state.equals(BUSY)) {

                        if (swipeRefreshLayout != null) {
                            if (loadType.equals(LOAD)) {
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                        }
                    } else if (state.equals(SUCCESS)) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        TradeHistoryEntity tradeHistoryEntity = (TradeHistoryEntity) response;
                        if (tradeHistoryEntity == null) {
                            return;
                        }
                        if (tradeHistoryEntity.getData()==null){
                            return;
                        }
                        if(isAdded()){
                            if (tradeHistoryEntity.getData().size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        if (loadType.equals(LOAD)) {
                            tradeRecordAdapter.addDatas(tradeHistoryEntity.getData());
                        } else {
                            tradeRecordAdapter.setDatas(tradeHistoryEntity.getData());
                        }
                    } else if (state.equals(FAILURE)) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select:
                showFundWindow(fundItemEntity);
                break;
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }

    private int oldSelect = 0;

    //选择
    private void showFundWindow(FundItemEntity fundItemEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fund_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(fundSelectAdapter);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_select, Gravity.CENTER, 0, 0);
        if (fundItemEntity==null){
            return;
        }
        if (fundItemEntity.getData()==null){
            return;
        }
        if (fundItemEntity != null) {
            List<FundItemEntity.DataBean> fundItemEntityData = fundItemEntity.getData();
            fundSelectAdapter.setDatas(fundItemEntityData);

            fundSelectAdapter.select(oldSelect);
            /*监听*/
            fundSelectAdapter.setOnItemClick((position, data) -> {
                oldSelect = position;
                fundSelectAdapter.select(position);
                recyclerView.setAdapter(fundSelectAdapter);
                fundSelectAdapter.notifyDataSetChanged();
                text_select.setText(data.getName());
                String code = data.getCode();
                ChartUtil.setIcon(code, img_bg);


                if (position == 0) {
                    commodity = null;
                } else {
                    commodity = data.getCode() + "USDT";

                }
                getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe);

                popupWindow.dismiss();


            });
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_today:
                createTimeGe = ChartUtil.getTodayZero();
                createTimeLe = ChartUtil.getTodayLastTime();

                break;
            case R.id.radio_week:
                createTimeGe = ChartUtil.getWeekZero();
                createTimeLe = ChartUtil.getTodayLastTime();
                break;
            case R.id.radio_month:
                createTimeGe = ChartUtil.getMonthZero();
                createTimeLe = ChartUtil.getTodayLastTime();
                break;

        }

        getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe);


    }
}
