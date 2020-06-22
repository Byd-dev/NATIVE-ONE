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

import com.pro.bityard.R;
import com.pro.bityard.adapter.DepositWithdrawAdapter;
import com.pro.bityard.adapter.FundSelectAdapter;
import com.pro.bityard.adapter.TradeSelectAdapter;
import com.pro.bityard.api.NetManger;
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

    private DepositWithdrawAdapter depositWithdrawAdapter;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    private String createTimeGe = "";
    private String createTimeLe = "";

    private String FIRST = "first";
    private String REFRESH = "refresh";
    private String LOAD = "load";

    private int page = 0;
    private String currency = "";
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
                    getWithdrawal(LOAD, null, transfer, "1", null, currency, null,
                            null, String.valueOf(page), "10");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //监听
        depositWithdrawAdapter.setOnItemClick(data -> {
            showDetailPopWindow(data);
        });

    }

    /*显示详情*/
    private void showDetailPopWindow(DepositWithdrawEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fund_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_d_w_detail);

        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(dataBean.getCurrency());
        TextView text_type = view.findViewById(R.id.text_type);
        ImageView img_bg=view.findViewById(R.id.img_bg);
        ChartUtil.setIcon(dataBean.getCurrency(),img_bg);

        String explain = dataBean.getExplain();
        switch (explain) {
            case "提款取出":
                text_type.setText(R.string.text_withdrawal);
                break;
            case "充值存入":
                text_type.setText(R.string.text_recharge);
                break;
        }

        TextView text_status = view.findViewById(R.id.text_status);
        int status = dataBean.getStatus();
        switch (status) {
            case 0:
            case -1:
                text_status.setText(R.string.text_pending);
                break;
            case 1:
                text_status.setText(R.string.text_tip_success);
                break;
            case 2:
                text_status.setText(R.string.text_failure);
                break;
            case 3:
                text_status.setText(R.string.text_tip_cancel);
                break;
            case 4:
                text_status.setText(R.string.text_processing);
                break;
            case 5:
                text_status.setText(R.string.text_in_progress);
                break;
            case 6:
                text_status.setText(R.string.text_refunded);
                break;
        }


        TextView text_time = view.findViewById(R.id.text_time);
        text_time.setText(ChartUtil.getDate(dataBean.getCreateTime()));

        TextView text_amount = view.findViewById(R.id.text_amount);
        text_amount.setText(dataBean.getMoney() + dataBean.getCurrency());

        TextView text_id = view.findViewById(R.id.text_id);
        text_id.setText(dataBean.getId());
        TextView text_address = view.findViewById(R.id.text_address);
        text_address.setText(dataBean.getAddress());


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
                        if (depositWithdrawEntity==null){
                            return;
                        }
                        if (depositWithdrawEntity.getData()==null){
                            return;
                        }


                        if (loadType.equals(LOAD)) {
                            depositWithdrawAdapter.addDatas(depositWithdrawEntity.getData());
                        } else {
                            depositWithdrawAdapter.setDatas(depositWithdrawEntity.getData());
                            if (depositWithdrawEntity.getData().size() == 0) {
                                layout_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                layout_null.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
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
        if (fundItemEntity != null) {
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
                ChartUtil.setIcon(code,img_bg);



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
