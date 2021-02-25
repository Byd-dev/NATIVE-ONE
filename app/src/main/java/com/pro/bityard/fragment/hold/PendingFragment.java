package com.pro.bityard.fragment.hold;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.PendingAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.manger.PendCountManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.utils.TradeUtil.StopLossPrice;
import static com.pro.bityard.utils.TradeUtil.StopProfitPrice;
import static com.pro.bityard.utils.TradeUtil.positionPrice;
import static com.pro.bityard.utils.TradeUtil.removeDigital;

public class PendingFragment extends BaseFragment implements Observer {
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    private PendingAdapter pendingAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private PositionEntity positionEntity;
    @BindView(R.id.btn_login)
    Button btn_login;
    private String contractCode = null;
    private List<String> quoteList;
    private TextView text_price_pop;
    private boolean isBuy_pop;

    public PendingFragment newInstance(String type) {
        PendingFragment fragment = new PendingFragment();
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
            NetManger.getInstance().getPending(tradeType, (state, response1, response2) -> {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    positionEntity = (PositionEntity) response1;
                    if (positionEntity.getData().size() == 0) {
                        layout_null.setVisibility(View.VISIBLE);
                        headerRecyclerView.setVisibility(View.GONE);

                    } else {
                        layout_null.setVisibility(View.GONE);
                        headerRecyclerView.setVisibility(View.VISIBLE);
                    }


                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            });


        } else {
            headerRecyclerView.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            layout_null.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        SocketQuoteManger.getInstance().addObserver(this);
        TagManger.getInstance().addObserver(this);


        Util.colorSwipe(getActivity(),swipeRefreshLayout);


        pendingAdapter = new PendingAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.foot_tab_view, null);

        headerRecyclerView.addFooterView(footView);
        headerRecyclerView.setAdapter(pendingAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> initData());

        //平仓
        pendingAdapter.setOnCancelClick(id -> {
            /*平仓*/
            NetManger.getInstance().cancel(id, tradeType, (state, response) -> {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                    Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                    //更新下持仓页面的数据
                    PositionRealManger.getInstance().getHold();
                    initData();

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            });
        });
        //详情
        pendingAdapter.setOnDetailClick(data -> {
            showDetailPopWindow(getActivity(), layout_view, data);

        });

        btn_login.setOnClickListener(v -> {
            LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);

        });


    }

    /*显示详情*/
    private void showDetailPopWindow(Activity activity, View layout_view, PositionEntity.DataBean dataBean) {
        contractCode = dataBean.getContractCode();

        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_pending_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_pendings);

        TextView text_name = view.findViewById(R.id.text_name);

        String[] split = Util.quoteList(contractCode).split(",");
        text_name.setText(split[0]);
        TextView text_currency = view.findViewById(R.id.text_currency);
        text_currency.setText(dataBean.getCurrency());
        isBuy_pop = dataBean.isIsBuy();

        String deferFee = dataBean.getDeferFee();
        TextView text_is_o_n = view.findViewById(R.id.text_is_o_n);

        if (Double.parseDouble(deferFee) == 0) {
            text_is_o_n.setText(R.string.text_no);
        } else {
            text_is_o_n.setText(R.string.text_yes);
        }


        //当前价
        text_price_pop = view.findViewById(R.id.text_price);
        //开盘价
        double price = dataBean.getPrice();
        int priceDigit = dataBean.getPriceDigit();
        double lever = dataBean.getLever();
        double margin_pop = dataBean.getMargin();
        double stopLoss = dataBean.getStopLoss();
        double stopProfit = dataBean.getStopProfit();
        //止损价
        TextView text_loss_price = view.findViewById(R.id.text_loss_price);
        //止损价格
        text_loss_price.setText(StopLossPrice(isBuy_pop, price, priceDigit, lever, margin_pop, Math.abs(stopLoss)));
        TextView text_profit_price = view.findViewById(R.id.text_profit_price);
        //止盈价格
        text_profit_price.setText(StopProfitPrice(isBuy_pop, price, priceDigit, lever, margin_pop, stopProfit));
        TextView text_pending_price = view.findViewById(R.id.text_pending_price);
        text_pending_price.setText(TradeUtil.getNumberFormat(price, priceDigit));
        //服务费
        double serviceCharge_pop = dataBean.getServiceCharge();

        ImageView img_bg = view.findViewById(R.id.img_bg);

        if (isBuy_pop) {
            img_bg.setBackgroundResource(R.mipmap.icon_up);
        } else {
            img_bg.setBackgroundResource(R.mipmap.icon_down);

        }
        //现价和盈亏
        positionPrice(isBuy_pop,quoteList, contractCode, response -> {
            text_price_pop.setText(response.toString());


        });

        LinearLayout layout_add = view.findViewById(R.id.layout_add);
        ImageView img_add = view.findViewById(R.id.img_add);
        //撤单
        view.findViewById(R.id.text_close_out).setOnClickListener(v -> {
            /*平仓*/
            NetManger.getInstance().cancel(dataBean.getId(), tradeType, (state, response) -> {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                    Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                    //更新下持仓页面的数据
                    PositionRealManger.getInstance().getHold();
                    initData();
                    popupWindow.dismiss();

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            });
        });


        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(margin_pop));

        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(lever));
        TextView text_orders = view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));

        TextView text_fee = view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge_pop));
        TextView text_o_n = view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));
        TextView text_o_n_fee = view.findViewById(R.id.text_o_n_fee);
        text_o_n_fee.setText(String.valueOf(deferFee));
        TextView text_open_time = view.findViewById(R.id.text_open_time);
        text_open_time.setText(ChartUtil.getDate(dataBean.getTime()));
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
    protected int setLayoutResourceID() {
        return R.layout.fragment_pending;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        if (isLogin()) {
            NetManger.getInstance().getPending(tradeType, (state, response1, response2) -> {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    positionEntity = (PositionEntity) response1;
                    pendingAdapter.setDatas(positionEntity.getData(),quoteList);
                    int size = positionEntity.getData().size();
                    PendCountManger.getInstance().post(String.valueOf(size));
                    if (size == 0) {
                        layout_null.setVisibility(View.VISIBLE);
                        headerRecyclerView.setVisibility(View.GONE);

                    } else {
                        layout_null.setVisibility(View.GONE);
                        headerRecyclerView.setVisibility(View.VISIBLE);
                    }


                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            });
        }

    }
    ArrayMap<String, String> map;

    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(AppConfig.CONTRACT_ALL);
            runOnUiThread(() -> {
                //现价和盈亏

                if (positionEntity != null) {
                    if (isLogin()) {
                        map = new ArrayMap<>();
                        for (int i = 0; i < positionEntity.getData().size(); i++) {
                            for (int j = 0; j < quoteList.size(); j++) {
                                String[] split1 = quoteList.get(j).split(",");

                                if (TradeUtil.filter(positionEntity.getData().get(i).getContractCode()).equals(removeDigital(split1[0]))) {
                                    removeDigital(split1[0]);
                                    boolean isBuy = positionEntity.getData().get(i).isIsBuy();
                                    if (isBuy) {
                                        map.put(positionEntity.getData().get(i).getContractCode(), split1[4]);
                                    } else {
                                        map.put(positionEntity.getData().get(i).getContractCode(), split1[6]);
                                    }
                                    pendingAdapter.refreshPartItem(i, map);
                                    continue;
                                }
                            }
                        }
                    } else {
                        positionEntity.getData().clear();
                    }

                }
                if (contractCode != null) {
                    TradeUtil.positionPrice(isBuy_pop,quoteList, contractCode, response -> {
                        if (text_price_pop != null) {
                            text_price_pop.setText(response.toString());
                        }
                    });
                }

            });
        } else if (o == TagManger.getInstance()) {
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
        SocketQuoteManger.getInstance().deleteObserver(this);

    }
}
