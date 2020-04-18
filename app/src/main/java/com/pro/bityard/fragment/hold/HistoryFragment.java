package com.pro.bityard.fragment.hold;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.HistoryAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class HistoryFragment extends BaseFragment implements Observer {
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    @BindView(R.id.btn_login)
    Button btn_login;
    private HistoryAdapter historyAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;
    private HistoryEntity historyEntity;


    public HistoryFragment newInstance(String type) {
        HistoryFragment fragment = new HistoryFragment();
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
        } else {
            headerRecyclerView.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        TagManger.getInstance().addObserver(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        @SuppressLint("InflateParams") View footView = LayoutInflater.from(getContext()).inflate(R.layout.tab_foot_view, null);

        headerRecyclerView.addFooterView(footView);
        historyAdapter = new HistoryAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        headerRecyclerView.setAdapter(historyAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::initData);
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

        if (isLogin()) {
            NetManger.getInstance().getHistory(tradeType, (state, response) -> {
                if (state.equals(BUSY)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                } else if (state.equals(SUCCESS)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    historyEntity = (HistoryEntity) response;
                    historyAdapter.setDatas(historyEntity.getData());
                } else if (state.equals(FAILURE)) {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
            });
        } else {
            if (historyEntity != null) {
                historyEntity.getData().clear();
                historyAdapter.setDatas(historyEntity.getData());
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

        }


    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == TagManger.getInstance()) {
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
