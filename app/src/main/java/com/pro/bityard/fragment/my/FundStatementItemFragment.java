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

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.DepositWithdrawAdapter;
import com.pro.bityard.adapter.FundSelectAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.DepositWithdrawEntity;
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

public class FundStatementItemFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private FundSelectAdapter fundSelectAdapter;

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

    private DepositWithdrawAdapter depositWithdrawAdapter;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private String createTimeGe = "";
    private String createTimeLe = "";

    private String FIRST = "first";
    private String REFRESH = "refresh";
    private String LOAD = "load";

    private int page = 0;
    private String currency = "";


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        layout_select.setOnClickListener(this);
        fundSelectAdapter = new FundSelectAdapter(getActivity());


        radioGroup.setOnCheckedChangeListener(this);

        depositWithdrawAdapter = new DepositWithdrawAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(depositWithdrawAdapter);
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == depositWithdrawAdapter.getItemCount() - 1) {
                    depositWithdrawAdapter.startLoad();
                    page = page + 1;
                    getWithdrawal(LOAD, null, null, "1", null, currency, null,
                            null, String.valueOf(page), "10");
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
    protected int setLayoutResourceID() {
        return R.layout.fragment_fund_statement_item;
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
        Log.d("print", "initData:162:  "+currency+createTimeGe+createTimeLe);
        getWithdrawal(FIRST, null, null, "1", null, currency, createTimeGe,
                createTimeLe, String.valueOf(page), "10");

    }





    private void getWithdrawal(String loadType, String type, String transfer, String currencyType, String srcCurrency, String currency, String createTimeGe,
                               String createTimeLe, String page, String rows) {
        NetManger.getInstance().depositWithdraw(type, transfer, currencyType, srcCurrency, currency, createTimeGe,
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
                        DepositWithdrawEntity depositWithdrawEntity = (DepositWithdrawEntity) response;
                        if (loadType.equals(LOAD)) {
                            depositWithdrawAdapter.addDatas(depositWithdrawEntity.getData());
                        } else {
                            depositWithdrawAdapter.setDatas(depositWithdrawEntity.getData());
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
        recyclerView.setAdapter(fundSelectAdapter);
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
                switch (code) {
                    case "":
                        img_bg.setVisibility(View.GONE);
                        break;
                    case "EOS":
                        img_bg.setVisibility(View.VISIBLE);
                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_eos));
                        break;
                    case "LTC":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_ltc));
                        break;
                    case "BCH":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_bch));
                        break;
                    case "USDT":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_usdt));
                        break;
                    case "BTC":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_btc));
                        break;
                    case "ETH":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_eth));
                        break;
                    case "XRP":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_xrp));
                        break;
                    case "TRX":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_trx));
                        break;
                    case "HT":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_ht));
                        break;
                    case "LINK":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_link));
                        break;
                }


                page = 0;
                currency = data.getCode();
                getWithdrawal(FIRST, null, null, "1", null, currency, createTimeGe,
                        createTimeLe, String.valueOf(page), "10");
                popupWindow.dismiss();


            });
        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_select, Gravity.CENTER, 0, 0);
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

        getWithdrawal(FIRST, null, null, "1", null, currency, createTimeGe,
                createTimeLe, String.valueOf(page), "10");

    }
}
