package com.pro.bityard.fragment.trade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.view.timepicker.TimePickerBuilder;
import com.pro.bityard.view.timepicker.TimePickerView;
import com.pro.switchlibrary.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class SpotHistoryItemFragment extends BaseFragment implements View.OnClickListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;
    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;
    private String quote_code = null;

    private SpotPositionAdapter spotPositionAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String createTimeGe;
    private String createTimeLe;
    private int page;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_item;
    }

    public SpotHistoryItemFragment newInstance(String type, String value) {
        SpotHistoryItemFragment fragment = new SpotHistoryItemFragment();
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


        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_spot_history_layout, null);
        TextView text_start = headView.findViewById(R.id.text_start);
        TextView text_end = headView.findViewById(R.id.text_end);


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

        headView.findViewById(R.id.layout_start).setOnClickListener(v -> {
            TimePickerView timePickerView = new TimePickerBuilder(getActivity(), (date, v1) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String selectStart = Util.startFormatDate(format,  text_end.getText().toString());
                text_start.setText(selectStart);
                startDate.set(Util.str2Calendar(selectStart, "year"), Util.str2Calendar(selectStart, "month"),
                        Util.str2Calendar(selectStart, "day"));

                createTimeGe = ChartUtil.getSelectZero(selectStart);
                createTimeLe = ChartUtil.getSelectLastTime(text_end.getText().toString());
                getHistoryPosition();

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

        headView.findViewById(R.id.layout_end).setOnClickListener(v -> {
            TimePickerView timePickerView = new TimePickerBuilder(getActivity(), (date, v12) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String selectEnd = Util.endFormatDate(format, text_start.getText().toString());
                text_end.setText(selectEnd);
                endDate.set(Util.str2Calendar(selectEnd, "year"), Util.str2Calendar(selectEnd, "month"),
                        Util.str2Calendar(selectEnd, "day"));

                createTimeGe = ChartUtil.getSelectZero(text_start.getText().toString());
                createTimeLe = ChartUtil.getSelectLastTime(selectEnd);
                getHistoryPosition();


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


        spotPositionAdapter = new SpotPositionAdapter(getActivity());
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_spot.setAdapter(spotPositionAdapter);

        recyclerView_spot.addHeaderView(headView);


        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getHistoryPosition();
        });


    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


        getHistoryPosition();


    }

    private void getHistoryPosition() {

        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().spotPositionHistory(null, null, null, null, null,
                null, createTimeGe, createTimeLe, String.valueOf(page), "10", (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout.setRefreshing(false);

                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout.setRefreshing(false);

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


}
