package com.pro.bityard.fragment.trade;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class SpotRecordItemFragment extends BaseFragment implements View.OnClickListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;
    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    @BindView(R.id.text_commission)
    TextView text_commission;


    private SpotPositionAdapter spotPositionAdapter;


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_current_record;
    }

    public SpotRecordItemFragment newInstance(String type, String value) {
        SpotRecordItemFragment fragment = new SpotRecordItemFragment();
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


        spotPositionAdapter = new SpotPositionAdapter(getActivity());
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_spot.setAdapter(spotPositionAdapter);

        text_commission.setOnClickListener(this);


        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosition();
        });


    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


        getPosition();


    }

    private void getPosition() {
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().userSpotPosition(loginEntity.getUser().getUserId(), (state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                SpotPositionEntity spotPositionEntity = (SpotPositionEntity) response;
                if (spotPositionEntity.getData().size() == 0) {
                    layout_null.setVisibility(View.VISIBLE);
                } else {
                    layout_null.setVisibility(View.GONE);
                }
                spotPositionAdapter.setDatas(spotPositionEntity.getData());
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_commission:

                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }


}
