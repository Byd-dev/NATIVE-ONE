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
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotHistoryAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.SpotHistoryEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.manger.CommissionManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.view.timepicker.TimePickerBuilder;
import com.pro.bityard.view.timepicker.TimePickerView;
import com.pro.switchlibrary.SPUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class SpotCommitHistoryFragment extends BaseFragment implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;
    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;


    private SpotHistoryAdapter spotHistoryAdapter;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String createTimeGe;
    private String createTimeLe;
    private int page=1;
    private Activity activity;
    private TextView text_start;
    private TextView text_end;
    private Calendar startDate;
    private Calendar endDate;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_commit;
    }

    public SpotCommitHistoryFragment newInstance(String type, String value) {
        SpotCommitHistoryFragment fragment = new SpotCommitHistoryFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
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

        CommissionManger.getInstance().addObserver(this);

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


        view.findViewById(R.id.layout_start).setOnClickListener(v -> {
            TimePickerView timePickerView = new TimePickerBuilder(getActivity(), (date, v1) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String selectStart = Util.startFormatDate(format,  text_end.getText().toString());
                text_start.setText(selectStart);
                startDate.set(Util.str2Calendar(selectStart, "year"), Util.str2Calendar(selectStart, "month"),
                        Util.str2Calendar(selectStart, "day"));

                createTimeGe = ChartUtil.getSelectZero(selectStart);
                createTimeLe = ChartUtil.getSelectLastTime(text_end.getText().toString());
                page = 1;
                getHistoryPosition(AppConfig.FIRST,"null",null,null,null,page);

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
                page=1;
                getHistoryPosition(AppConfig.FIRST,"null",null,null,null,page);


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


        spotHistoryAdapter = new SpotHistoryAdapter(getActivity());
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView_spot.setLayoutManager(linearLayoutManager);
        recyclerView_spot.setAdapter(spotHistoryAdapter);
        spotHistoryAdapter.setOnDetailClick(new SpotHistoryAdapter.OnDetailClick() {
            @Override
            public void onClickListener(SpotHistoryEntity.DataBean data) {
                if (data.getStatus()!=6){
                    showDetailPopWindow(data);
                }
            }
        });


        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page=1;
            getHistoryPosition(AppConfig.FIRST,"null",null,null,null,page);

        });
        recyclerView_spot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == spotHistoryAdapter.getItemCount() - 1) {
                    spotHistoryAdapter.startLoad();
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



    /*显示详情*/
    private void showDetailPopWindow(SpotHistoryEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_spot_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_trade_detail_spot);
        Log.d("print", "showDetailPopWindow:234:  "+dataBean);
        TextView text_name = view.findViewById(R.id.text_name);
        Integer type = dataBean.getType();
        Boolean buy = dataBean.getBuy();
        TextView text_type = view.findViewById(R.id.text_type);
        TextView text_type_name=view.findViewById(R.id.text_type_name);
        TextView text_status=view.findViewById(R.id.text_status);
        TextView text_volume=view.findViewById(R.id.text_volume);
        TextView text_average_price=view.findViewById(R.id.text_average_price);
        TextView text_charge=view.findViewById(R.id.text_charge);
        TextView text_trade_amount = view.findViewById(R.id.text_trade_amount);
        TextView text_time = view.findViewById(R.id.text_time);
        TextView text_price=view.findViewById(R.id.text_price);
        TextView text_amount_spot=view.findViewById(R.id.text_amount_spot);
        if (buy){
           text_name.setText(dataBean.getDesCurrency() + "/" + dataBean.getSrcCurrency());
            text_type.setText(activity.getResources().getString(R.string.text_buy));
            text_type.setTextColor(activity.getResources().getColor(R.color.text_quote_green));
            text_type_name.setText(activity.getResources().getString(R.string.text_buy_tip));
            text_type_name.setTextColor(activity.getResources().getColor(R.color.text_quote_green));
            text_charge.setText(TradeUtil.justDisplay(dataBean.getCharge())+dataBean.getDesCurrency());
            text_trade_amount.setText(TradeUtil.justDisplay(dataBean.getOpAmount())+dataBean.getSrcCurrency());


        }else {
            text_name.setText(dataBean.getSrcCurrency() + "/" + dataBean.getDesCurrency());
            text_type.setText(activity.getResources().getString(R.string.text_sell));
            text_type.setTextColor(activity.getResources().getColor(R.color.text_quote_red));
            text_type_name.setText(activity.getResources().getString(R.string.text_sell_tip));
            text_type_name.setTextColor(activity.getResources().getColor(R.color.text_quote_red));
            text_charge.setText(TradeUtil.justDisplay(dataBean.getCharge())+dataBean.getSrcCurrency());
            text_trade_amount.setText(TradeUtil.justDisplay(dataBean.getOpAmount())+dataBean.getDesCurrency());
        }


        text_status.setText(dataBean.getStatus().toString());
        text_volume.setText(TradeUtil.justDisplay(dataBean.getOpVolume()));
        text_average_price.setText(TradeUtil.justDisplay(dataBean.getOpPrice())+"/"+TradeUtil.justDisplay(dataBean.getPrice()));



        text_price.setText(TradeUtil.justDisplay(dataBean.getPrice()));
        text_amount_spot.setText(TradeUtil.justDisplay(dataBean.getOpVolume()));
        text_time.setText(ChartUtil.getDate(dataBean.getCreateTime()));







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

        getHistoryPosition(AppConfig.FIRST,"null",null,null,null,page);


    }

    private void getHistoryPosition(String addType, String commodity, String buy, String type, String srcCurrency, int page) {

        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().spotPositionHistory(commodity, buy, type, null, srcCurrency,
                null, createTimeGe, createTimeLe, String.valueOf(page), "10", (state, response) -> {
                    if (state.equals(BUSY)) {
                        if (isAdded()){
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    } else if (state.equals(SUCCESS)) {
                        if (isAdded()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        SpotHistoryEntity spotHistoryEntity= (SpotHistoryEntity) response;
                        List<SpotHistoryEntity.DataBean> data = spotHistoryEntity.getData();

                        if (addType.equals(AppConfig.LOAD)){
                            spotHistoryAdapter.addDatas(data);
                        }else {
                            if (isAdded()){
                                if (data.size() != 0) {
                                    spotHistoryAdapter.setDatas(data);
                                    layout_null.setVisibility(View.GONE);
                                    recyclerView_spot.setVisibility(View.VISIBLE);
                                } else {
                                    layout_null.setVisibility(View.VISIBLE);
                                    recyclerView_spot.setVisibility(View.GONE);

                                }
                            }

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

    private String edit_search;
    private String buy_sell;
    @Override
    public void update(Observable o, Object arg) {
        if (o == CommissionManger.getInstance()) {
            String value = (String) arg;
            String[] split = value.split(",");
            String value_date = split[0];
            String value_search = split[1];
            String value_type = split[2];
            Log.d("print", "onClick:179:  " + value_search + "   " + value_date + "   " + value_type);
            buy_sell = null;
            if (value_search.equals("")) {
                edit_search = "null";
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


            } else if (value_date.equals(activity.getString(R.string.text_near_one_week))) {
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
            getHistoryPosition(AppConfig.FIRST, edit_search, buy_sell, null, null, page);
        }
    }
}
