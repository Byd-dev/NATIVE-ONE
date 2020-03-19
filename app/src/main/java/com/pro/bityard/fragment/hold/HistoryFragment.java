package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.adapter.PositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.OpenPositionEntity;
import com.pro.bityard.view.HeaderRecyclerView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class HistoryFragment extends BaseFragment {
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;


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
        if (getArguments()!=null){
            tradeType = getArguments().getString("tradeType");
        }
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
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

    }
}
