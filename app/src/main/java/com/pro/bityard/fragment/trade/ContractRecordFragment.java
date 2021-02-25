package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.RadioDateAdapter;
import com.pro.bityard.adapter.TradeRecordAdapter;
import com.pro.bityard.adapter.TradeSelectAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.manger.ContractManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.timepicker.TimePickerBuilder;
import com.pro.bityard.view.timepicker.TimePickerView;
import com.pro.switchlibrary.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class ContractRecordFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Observer {


    private TradeSelectAdapter fundSelectAdapter;

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

    // private FundItemEntity fundItemEntity;

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

    private int page = 1;
    private String commodity = null;
    private List<String> contractList;
    private String code;


    private RadioDateAdapter radioDateAdapter, radioTypeAdapter;//杠杆适配器
    private List<String> dataList, typeList;
    private Calendar startDate;
    private Calendar endDate;
    private TextView text_start;
    private TextView text_end;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_trade_record;
    }

    @Override
    protected void onLazyLoad() {

    }

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    protected void initView(View view) {

        ContractManger.getInstance().addObserver(this);
        text_start = view.findViewById(R.id.text_start);
        text_end = view.findViewById(R.id.text_end);


        String nowTime = Util.getNowTime();

        text_end.setText(nowTime);

        //text_start.setText(Util.getBeforeNow7days());
        text_start.setText(nowTime);
        startDate = Calendar.getInstance();
        //startDate.set(Calendar.DAY_OF_YEAR, startDate.get(Calendar.DAY_OF_YEAR) - 7);
        endDate = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate.getTime());
        String endTime = sdf.format(endDate.getTime());

        createTimeGe = ChartUtil.getSelectZero(startTime);
        createTimeLe = ChartUtil.getSelectLastTime(endTime);
        page = 1;

        view.findViewById(R.id.layout_start).setOnClickListener(v -> {
            TimePickerView timePickerView = new TimePickerBuilder(getActivity(), (date, v1) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String selectStart = Util.startFormatDate(format, text_end.getText().toString());
                text_start.setText(selectStart);
                startDate.set(Util.str2Calendar(selectStart, "year"), Util.str2Calendar(selectStart, "month"),
                        Util.str2Calendar(selectStart, "day"));

                createTimeGe = ChartUtil.getSelectZero(selectStart);
                createTimeLe = ChartUtil.getSelectLastTime(text_end.getText().toString());
                page = 1;
                getTradeHistory(AppConfig.FIRST, null, null, createTimeGe, createTimeLe, null);


            }).setSubmitColor(getResources().getColor(R.color.maincolor))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.maincolor))
                    .setTitleBgColor(getResources().getColor(R.color.background_main_color))//标题背景颜色 Night mode
                    .setBgColor(getResources().getColor(R.color.background_main_color))
                    .setTextColorCenter(getResources().getColor(R.color.text_main_color))
                    .setTextColorOut(getResources().getColor(R.color.color_btn_bg))
                    .setDate(startDate)
                    .setSubCalSize(15)
                    .build();//滚轮背景颜色 Night mode.build();//取消按钮文字颜色build();
            timePickerView.show();

        });

        view.findViewById(R.id.layout_end).setOnClickListener(v -> {
            TimePickerView timePickerView = new TimePickerBuilder(getActivity(), (date, v12) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String selectEnd = Util.endFormatDate(format, text_start.getText().toString());
                text_end.setText(selectEnd);
                endDate.set(Util.str2Calendar(selectEnd, "year"), Util.str2Calendar(selectEnd, "month"),
                        Util.str2Calendar(selectEnd, "day"));

                createTimeGe = ChartUtil.getSelectZero(text_start.getText().toString());
                createTimeLe = ChartUtil.getSelectLastTime(selectEnd);
                page = 1;
                getTradeHistory(AppConfig.FIRST, null, null, createTimeGe, createTimeLe, null);


            }).setSubmitColor(getResources().getColor(R.color.maincolor))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.maincolor))
                    .setTitleBgColor(getResources().getColor(R.color.background_main_color))//标题背景颜色 Night mode
                    .setBgColor(getResources().getColor(R.color.background_main_color))
                    .setTextColorCenter(getResources().getColor(R.color.text_main_color))
                    .setTextColorOut(getResources().getColor(R.color.color_btn_bg))
                    .setDate(endDate)
                    .setSubCalSize(15)
                    .build();
            timePickerView.show();

        });


        layout_select.setOnClickListener(this);
        fundSelectAdapter = new TradeSelectAdapter(getActivity());


        radioGroup.setOnCheckedChangeListener(this);

        tradeRecordAdapter = new TradeRecordAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tradeRecordAdapter);
        Util.colorSwipe(getActivity(), swipeRefreshLayout);

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

        TextView text_side = view.findViewById(R.id.text_side);
        boolean isBuy = dataBean.isIsBuy();
        if (isBuy) {
            text_side.setText(R.string.text_buy_much);
        } else {
            text_side.setText(R.string.text_buy_empty);
        }

        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(dataBean.getMargin()));

        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(dataBean.getLever()));
        TextView text_orders = view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));
        TextView text_opPrice = view.findViewById(R.id.text_open_price);
        text_opPrice.setText(String.valueOf(dataBean.getOpPrice()));
        TextView text_clPrice = view.findViewById(R.id.text_close_price);
        text_clPrice.setText(String.valueOf(dataBean.getCpPrice()));
        TextView text_fee = view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge));
        TextView text_o_n = view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));
        TextView text_o_n_fee = view.findViewById(R.id.text_o_n_fee);
        text_o_n_fee.setText(String.valueOf(dataBean.getDeferFee()));
        TextView text_currency = view.findViewById(R.id.text_currency_type);
        text_currency.setText(dataBean.getCurrency());
        TextView text_open_time = view.findViewById(R.id.text_open_time);
        text_open_time.setText(ChartUtil.getDate(dataBean.getTime()));
        TextView text_close_time = view.findViewById(R.id.text_close_time);
        text_close_time.setText(ChartUtil.getDate(dataBean.getTradeTime()));
        TextView text_order_id = view.findViewById(R.id.text_order_id);
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

        String list = SPUtils.getString(AppConfig.CONTRACT_ID, null);

        if (list == null) {


            NetManger.getInstance().codeList((state, response) -> {
                if (state.equals(SUCCESS)) {
                    String[] contract = response.toString().split(";");
                    contractList = new ArrayList<>();
                    contractList.addAll(Arrays.asList(contract));
                    contractList.add(0, "ALL");
                    SPUtils.putString(AppConfig.CONTRACT_ID, response.toString());

                }
            });
            /*NetManger.getInstance().getInit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    InitEntity initEntity = (InitEntity) response;
                    if (initEntity.getGroup() != null) {
                        List<InitEntity.GroupBean> group = initEntity.getGroup();
                        ArrayMap<String, String> stringStringArrayMap = Util.groupData(group);
                        String allList = Util.groupList(stringStringArrayMap);



                        String[] contract = allList.split(";");

                        contractList = new ArrayList<>();
                        contractList.addAll(Arrays.asList(contract));
                        contractList.add(0, "ALL");
                        SPUtils.putString(AppConfig.CONTRACT_ID, allList);

                    }
                }

            });*/
        } else {
            String[] contract = list.split(";");

            contractList = new ArrayList<>();
            contractList.addAll(Arrays.asList(contract));
            contractList.add(0, "ALL");


        }
        page = 1;
        getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe, null);

    }


    private void getTradeHistory(String loadType, String nowTime, String commodity, String createTimeGe,
                                 String createTimeLe, String page) {
        NetManger.getInstance().tradeHistory("1", nowTime, "2", commodity, createTimeGe,
                createTimeLe, page, (state, response) -> {
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
                        if (tradeHistoryEntity.getData() == null) {
                            return;
                        }
                        if (isAdded()) {
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
                showFundWindow(contractList);
                break;

        }
    }

    private int oldSelect = 0;

    //选择
    private void showFundWindow(List<String> contractList) {
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

        if (contractList != null) {
            fundSelectAdapter.setDatas(contractList);
            fundSelectAdapter.select(oldSelect);
            /*监听*/
            fundSelectAdapter.setOnItemClick((position, data) -> {
                oldSelect = position;
                fundSelectAdapter.select(position);
                recyclerView.setAdapter(fundSelectAdapter);
                fundSelectAdapter.notifyDataSetChanged();
                if (position == 0) {
                    commodity = null;
                    text_select.setText("ALL");
                    ChartUtil.setIcon("", img_bg);
                } else {
                    if (data.length() > 4) {
                        code = data.substring(0, data.length() - 4);
                    } else {
                        code = data;
                    }
                    commodity = code + "USDT";
                    text_select.setText(code);
                    ChartUtil.setIcon(code, img_bg);
                }
                getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe, null);
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

        getTradeHistory(FIRST, String.valueOf(ChartUtil.getTimeNow()), commodity, createTimeGe, createTimeLe, null);


    }

    private String edit_search;
    private String buy_sell;

    @Override
    public void update(Observable o, Object arg) {
        if (o == ContractManger.getInstance()) {
            String value = (String) arg;
            String[] split = value.split(",");
            String value_date = split[0];
            String value_search = split[1];
            String value_type = split[2];
            Log.d("print", "onClick:179:  " + value_search + "   " + value_date + "   " + value_type);
            buy_sell = null;
            if (value_search.equals("")) {
                edit_search = null;
            } else {
                edit_search = value_search;
            }
            if (value_type.equals(activity.getResources().getString(R.string.text_buy_and_sell))) {
                buy_sell = null;
            } else if (value_type.equals(activity.getString(R.string.text_buy))) {
                buy_sell = "true";
            } else if (value_type.equals(activity.getString(R.string.text_sell))) {
                buy_sell = "false";
            }

            if (value_date.equals(activity.getString(R.string.text_near_one_day))) {
                createTimeGe = ChartUtil.getTodayZero();
                createTimeLe = ChartUtil.getTodayLastTime();
            }else if (value_date.equals(activity.getString(R.string.text_near_one_week))) {
                createTimeGe = ChartUtil.getDaysBefore(7);
                createTimeLe = ChartUtil.getTodayLastTime();
            } else if (value_date.equals(activity.getString(R.string.text_near_one_month))) {
                createTimeGe = ChartUtil.getDaysBefore(30);
                createTimeLe = ChartUtil.getTodayLastTime();
            } else if (value_date.equals(activity.getString(R.string.text_near_three_month))) {
                createTimeGe = ChartUtil.getDaysBefore(90);
                createTimeLe = ChartUtil.getTodayLastTime();
            }
            startDate.set(Util.str2Calendar(Util.startDisplay(createTimeGe), "year"), Util.str2Calendar(Util.startDisplay(createTimeGe), "month"),
                    Util.str2Calendar(Util.startDisplay(createTimeGe), "day"));
            text_start.setText(Util.startDisplay(createTimeGe));
            endDate.set(Util.str2Calendar(Util.startDisplay(createTimeLe), "year"), Util.str2Calendar(Util.startDisplay(createTimeLe), "month"),
                    Util.str2Calendar(Util.startDisplay(createTimeLe), "day"));
            text_end.setText(Util.startDisplay(createTimeLe));
            page = 1;
            getTradeHistory(AppConfig.FIRST, edit_search, buy_sell, createTimeGe, createTimeLe, null);
        }
    }
}
