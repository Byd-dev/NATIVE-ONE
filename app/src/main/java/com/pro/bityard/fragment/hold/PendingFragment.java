package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.PendingAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.manger.TagManger;
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

public class PendingFragment extends BaseFragment implements Observer {
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    private PendingAdapter pendingAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private PositionEntity positionEntity;
    @BindView(R.id.btn_login)
    Button btn_login;

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
                    Log.d("print", "initData:挂单:  " + positionEntity);
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
        QuoteListManger.getInstance().addObserver(this);
        TagManger.getInstance().addObserver(this);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));


        pendingAdapter = new PendingAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.tab_foot_view, null);

        headerRecyclerView.addFooterView(footView);
        headerRecyclerView.setAdapter(pendingAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> initData());

        pendingAdapter.setOnItemClick(new PendingAdapter.OnItemClick() {
            @Override
            public void onClickListener(PositionEntity.DataBean data) {

            }

            @Override
            public void onCancelListener(String id) {
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
            }

            @Override
            public void onProfitLossListener() {

            }
        });

        btn_login.setOnClickListener(v -> {
            LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);

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
        NetManger.getInstance().getPending(tradeType, (state, response1, response2) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                positionEntity = (PositionEntity) response1;
                Log.d("print", "initData:挂单:  " + positionEntity);
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
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteListManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            List<String> quoteList = arrayMap.get("0");
            runOnUiThread(() -> {

                if (positionEntity != null) {
                    if (isLogin()) {
                        pendingAdapter.setDatas(positionEntity.getData(), quoteList);
                    } else {
                        positionEntity.getData().clear();
                        ;
                        pendingAdapter.setDatas(positionEntity.getData(), quoteList);
                    }

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
    }
}
