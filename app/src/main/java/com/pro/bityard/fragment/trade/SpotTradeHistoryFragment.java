package com.pro.bityard.fragment.trade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotTradeHistoryAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.SpotTradeHistoryEntity;
import com.pro.bityard.manger.ControlManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.view.timepicker.TimePickerBuilder;
import com.pro.bityard.view.timepicker.TimePickerView;
import com.pro.switchlibrary.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class SpotTradeHistoryFragment extends BaseFragment implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;

    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;


    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    private SpotTradeHistoryAdapter spotTradeHistoryAdapter;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String createTimeGe;
    private String createTimeLe;
    private int page;
    private String edit_search;
    private String buy_sell;
    private Activity activity;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_trade_history;
    }

    public SpotTradeHistoryFragment newInstance(String type, String value) {
        SpotTradeHistoryFragment fragment = new SpotTradeHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        ControlManger.getInstance().addObserver(this);

        TextView text_start = view.findViewById(R.id.text_start);
        TextView text_end = view.findViewById(R.id.text_end);


        String nowTime = Util.getNowTime();

        text_end.setText(nowTime);

        text_start.setText(Util.getBeforeNow7days());
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DAY_OF_YEAR, startDate.get(Calendar.DAY_OF_YEAR) - 7);
        Calendar endDate = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate.getTime());
        String endTime = sdf.format(endDate.getTime());

        createTimeGe = ChartUtil.getSelectZero(startTime);
        createTimeLe = ChartUtil.getSelectLastTime(endTime);
        page = 0;

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
                page = 0;
                getHistoryPosition(AppConfig.FIRST, null, null, null, null, page);


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
                page = 0;
                getHistoryPosition(AppConfig.FIRST, null, null, null, null, page);


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

        linearLayoutManager = new LinearLayoutManager(getActivity());

        spotTradeHistoryAdapter = new SpotTradeHistoryAdapter(getActivity());
        recyclerView_spot.setLayoutManager(linearLayoutManager);
        recyclerView_spot.setAdapter(spotTradeHistoryAdapter);


        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getHistoryPosition(AppConfig.FIRST, null, null, null, null, page);

        });
        recyclerView_spot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == spotTradeHistoryAdapter.getItemCount() - 1) {
                    spotTradeHistoryAdapter.startLoad();
                    page = page + 1;
                    getHistoryPosition(AppConfig.LOAD, edit_search, buy_sell, null, null, page);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


        getHistoryPosition(AppConfig.FIRST, null, null, null, null, 1);


    }

    private void getHistoryPosition(String addType, String commodity, String buy, String type, String srcCurrency, int page) {

        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().spotPositionTradeHistory(commodity, buy, null, type, srcCurrency,
                null, createTimeGe, createTimeLe, String.valueOf(page), null, (state, response) -> {
                    if (state.equals(BUSY)) {
                        if (isAdded()){
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    } else if (state.equals(SUCCESS)) {
                        if (isAdded()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        SpotTradeHistoryEntity spotTradeHistoryEntity= (SpotTradeHistoryEntity) response;
                        List<SpotTradeHistoryEntity.DataBean> data = spotTradeHistoryEntity.getData();


                        if (addType.equals(AppConfig.LOAD)){
                            spotTradeHistoryAdapter.addDatas(data);
                        }else {
                            spotTradeHistoryAdapter.setDatas(data);
                            if (isAdded()){
                                if (data.size() != 0) {
                                    layout_null.setVisibility(View.GONE);
                                    recyclerView_spot.setVisibility(View.VISIBLE);
                                } else {
                                    layout_null.setVisibility(View.VISIBLE);
                                    recyclerView_spot.setVisibility(View.GONE);

                                }
                            }

                            /*new Thread(() -> {
                                if (data.size() != 0) {
                                    layout_null.setVisibility(View.GONE);
                                    recyclerView_spot.setVisibility(View.VISIBLE);
                                } else {
                                    layout_null.setVisibility(View.VISIBLE);
                                    recyclerView_spot.setVisibility(View.GONE);

                                }
                            });*/

                        }


                    } else if (state.equals(FAILURE)) {
                        if (isAdded()){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == ControlManger.getInstance()) {
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
            } else if (value_type.equals(activity.getResources().getString(R.string.text_buy))) {
                buy_sell = "true";
            } else if (value_type.equals(activity.getResources().getString(R.string.text_sell))) {
                buy_sell = "false";
            }

            if (value_date.equals(activity.getResources().getString(R.string.text_near_one_day))) {
                createTimeGe = ChartUtil.getTodayZero();
                createTimeLe = ChartUtil.getTodayLastTime();
            } else if (value_date.equals(activity.getResources().getString(R.string.text_near_one_week))) {
                createTimeGe = ChartUtil.getWeekZero();
                createTimeLe = ChartUtil.getTodayLastTime();
            } else if (value_date.equals(activity.getResources().getString(R.string.text_near_one_month))) {
                createTimeGe = ChartUtil.getMonthZero();
                createTimeLe = ChartUtil.getTodayLastTime();
            } else if (value_date.equals(activity.getResources().getString(R.string.text_near_three_month))) {
                createTimeGe = ChartUtil.getThreeMonthZero();
                createTimeLe = ChartUtil.getTodayLastTime();
            }

            page = 0;
            getHistoryPosition(AppConfig.FIRST, edit_search, buy_sell, null, null, page);
        }
    }
}
