package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class OpenFragment extends BaseFragment {
    @BindView(R.id.headerRecyclerView)
    HeaderRecyclerView headerRecyclerView;

    private PositionAdapter positionAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tradeType;


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


        positionAdapter = new PositionAdapter(getContext());

        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View headView= LayoutInflater.from(getContext()).inflate(R.layout.item_position_head_layout,null);

        headerRecyclerView.addHeaderView(headView);

        headerRecyclerView.setAdapter(positionAdapter);
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
        Log.d("print", "initData:91:OpenFragment:   "+tradeType);
        NetManger.getInstance().getHold(tradeType, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    OpenPositionEntity openPositionEntity = (OpenPositionEntity) response;
                    positionAdapter.setDatas(openPositionEntity.getData());
                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }
}
