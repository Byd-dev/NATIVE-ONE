package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.entity.TipEntity;
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

    private String buySell = null;


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
            getPosition(null, buySell, null, null, null);

        });


        spotPositionAdapter.setOnDetailClick(data -> NetManger.getInstance().spotClose(data.getId(), (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getCode() == 200) {
                    Toast.makeText(getActivity(), getString(R.string.text_tip_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.text_failure), Toast.LENGTH_SHORT).show();

                }

                initData();
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();

            }
        }));

    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


        getPosition(null, buySell, null, null, null);


    }

    private void getPosition(String commodity, String buy, String type,
                             String srcCurrency, String desCurrency) {
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().userSpotPosition(commodity, buy, type, srcCurrency, desCurrency, (state, response) -> {
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
                showSelect();
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }

    //选择杠杆
    private void showSelect() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_spot_select_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_all_commission = view.findViewById(R.id.text_all_commission);
        TextView text_sell_commission = view.findViewById(R.id.text_sell_commission);
        TextView text_buy_commission = view.findViewById(R.id.text_buy_commission);

        text_all_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_commission.setText(text_all_commission.getText().toString());
                popupWindow.dismiss();
                buySell = null;
                getPosition(null, buySell, null, null, null);

            }
        });
        text_sell_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_commission.setText(text_sell_commission.getText().toString());
                popupWindow.dismiss();
                buySell = "false";
                getPosition(null, buySell, null, null, null);

            }
        });
        text_buy_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_commission.setText(text_buy_commission.getText().toString());
                popupWindow.dismiss();
                buySell = "true";
                getPosition(null, buySell, null, null, null);

            }
        });
        Util.dismiss(getActivity(), popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(text_commission, 0, 0, Gravity.CENTER);
    }

}
