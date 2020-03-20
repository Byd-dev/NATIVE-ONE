package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.PositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.OpenPositionEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.quote.QuoteManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.GET_QUOTE_SECOND;

public class OpenFragment extends BaseFragment {
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    private PositionAdapter positionAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private OpenPositionEntity openPositionEntity;

    private TextView text_incomeAll;
    private View headView;


    public OpenFragment newInstance(String type) {
        OpenFragment fragment = new OpenFragment();
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
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));


        positionAdapter = new PositionAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        headView = LayoutInflater.from(getContext()).inflate(R.layout.item_position_head_layout, null);

        text_incomeAll = headView.findViewById(R.id.text_total_profit_loss);
        /*一键平仓*/
        headView.findViewById(R.id.text_close_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetManger.getInstance().closeAll(TradeUtil.positionIdList(openPositionEntity), tradeType, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                            Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            initData();
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });
            }
        });


        // headerRecyclerView.addHeaderView(headView);
        headerRecyclerView.setAdapter(positionAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        positionAdapter.setOnItemClick(new PositionAdapter.OnItemClick() {
            @Override
            public void onClickListener(OpenPositionEntity.DataBean data) {

            }

            @Override
            public void onCloseListener(String id) {


                /*平仓*/
                NetManger.getInstance().close(id, tradeType, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipCloseEntity tipCloseEntity = (TipCloseEntity) response;
                            Toast.makeText(getContext(), tipCloseEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            initData();

                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });


            }

            @Override
            public void onProfitLossListener() {

            }
        });

        startScheduleJob(mHandler, GET_QUOTE_SECOND, GET_QUOTE_SECOND);


    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> quoteList = QuoteManger.getInstance().getQuoteList();
            if (quoteList != null && openPositionEntity.getData() != null) {
                setIncome(quoteList, openPositionEntity);
                positionAdapter.setDatas(openPositionEntity.getData(), quoteList);
            }
        }
    };

    /*设置浮动盈亏*/
    private void setIncome(List<String> quoteList, OpenPositionEntity openPositionEntity) {
        TradeUtil.getIncome(quoteList, openPositionEntity, new TradeResult() {
            @Override
            public void setResult(Object response) {
                Double incomeAll = (Double) response;
                if (incomeAll > 0) {
                    text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_green));
                } else {
                    text_incomeAll.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.text_quote_red));
                }
                text_incomeAll.setText(response.toString());

            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_open;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


        NetManger.getInstance().getHold(tradeType, new OnNetTwoResult() {
            @Override
            public void setResult(String state, Object response1, Object response2) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    openPositionEntity = (OpenPositionEntity) response1;
                    List<String> quoteList = (List<String>) response2;
                    positionAdapter.setDatas(openPositionEntity.getData(), quoteList);
                    //这里根据持仓来是否显示头部视图
                    if (openPositionEntity.getData().size() == 0) {
                        text_incomeAll.setText("");
                        headerRecyclerView.removeHeaderView(headView);
                    } else {
                        //防止刷新已经有头布局 继续添加出现的bug
                        if (headerRecyclerView.getHeadersCount() == 0) {
                            headerRecyclerView.addHeaderView(headView);
                        }
                    }

                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }


        });


    }
}
