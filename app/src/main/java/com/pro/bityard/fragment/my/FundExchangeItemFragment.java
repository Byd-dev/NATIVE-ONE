package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
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
import com.pro.bityard.adapter.DepositWithdrawAdapter;
import com.pro.bityard.adapter.ExchangeRecordAdapter;
import com.pro.bityard.adapter.FundSelectAdapter;
import com.pro.bityard.adapter.TradeSelectAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.entity.ExchangeRecordEntity;
import com.pro.bityard.entity.FundItemEntity;
import com.pro.bityard.utils.ChartUtil;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class FundExchangeItemFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private FundSelectAdapter tradeSelectAdapter;

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

    private ExchangeRecordAdapter exchangeRecordAdapter;

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
    private String srcCurrency = "";
    private String transfer;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fund_statement_item;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        layout_select.setOnClickListener(this);
        tradeSelectAdapter = new FundSelectAdapter(getActivity());


        radioGroup.setOnCheckedChangeListener(this);

        exchangeRecordAdapter = new ExchangeRecordAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(exchangeRecordAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initData();
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == exchangeRecordAdapter.getItemCount() - 1) {
                    exchangeRecordAdapter.startLoad();
                    page = page + 1;
                    getWithdrawal(LOAD, null, transfer, null, null, "USDT", createTimeGe,
                            createTimeLe, String.valueOf(page), "10");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //监听
        exchangeRecordAdapter.setOnItemClick(data -> {

        });

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
                fundItemEntity = (FundItemEntity) response;
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
        getWithdrawal(FIRST, null, null, null, null, "USDT", createTimeGe,
                createTimeLe, String.valueOf(page), "10");

    }


    private void getWithdrawal(String loadType, String type, String transfer, String currencyType, String srcCurrency, String desCurrency, String createTimeGe,
                               String createTimeLe, String page, String rows) {
        NetManger.getInstance().exchangeRecord(type, transfer, currencyType, srcCurrency, desCurrency, createTimeGe,
                createTimeLe, page, rows, (state, response) -> {
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
                        ExchangeRecordEntity exchangeRecordEntity = (ExchangeRecordEntity) response;
                        if (layout_null != null && recyclerView != null) {
                            if (exchangeRecordEntity.getData().size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        if (loadType.equals(LOAD)) {
                            exchangeRecordAdapter.addDatas(exchangeRecordEntity.getData());
                        } else {
                            exchangeRecordAdapter.setDatas(exchangeRecordEntity.getData());
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
        }
    }

    private int oldSelect = 0;

    //选择杠杆
    private void showFundWindow(FundItemEntity fundItemEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fund_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(tradeSelectAdapter);


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_select, Gravity.CENTER, 0, 0);

        if (fundItemEntity == null) {
            return;
        }
        if (fundItemEntity.getData() == null) {
            return;
        }

        List<FundItemEntity.DataBean> fundItemEntityData = fundItemEntity.getData();
        tradeSelectAdapter.setDatas(fundItemEntityData);

        tradeSelectAdapter.select(oldSelect);
        /*监听*/
        tradeSelectAdapter.setOnItemClick((position, data) -> {
            oldSelect = position;
            tradeSelectAdapter.select(position);
            recyclerView.setAdapter(tradeSelectAdapter);
            tradeSelectAdapter.notifyDataSetChanged();
            text_select.setText(data.getName());
            String code = data.getCode();
            ChartUtil.setIcon(code, img_bg);


            page = 0;
            srcCurrency = data.getCode();
            getWithdrawal(FIRST, null, null, null, null, "USDT", createTimeGe,
                    createTimeLe, String.valueOf(page), "10");
            popupWindow.dismiss();


        });


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

        getWithdrawal(FIRST, null, null, null, null, "USDT", createTimeGe,
                createTimeLe, String.valueOf(page), "10");

    }
}
