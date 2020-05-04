package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.RadioRateAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TradeSettingsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recyclerView_profit)
    RecyclerView recyclerView_profit;
    @BindView(R.id.recyclerView_loss)
    RecyclerView recyclerView_loss;
    private RadioRateAdapter radioRateProfitAdapter, radioRateLossAdapter;
    private double stopProfit = 3;
    private double stopLoss = -0.9;
    private boolean isDefer; //是否递延
    private boolean isOpenSure;
    private boolean isCloseSure;
    @BindView(R.id.img_one)
    ImageView img_one;
    @BindView(R.id.img_two)
    ImageView img_two;
    @BindView(R.id.img_three)
    ImageView img_three;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.img_back_two).setOnClickListener(this);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.layout_three).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_trade_settings;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        isDefer = SPUtils.getBoolean(AppConfig.KEY_DEFER, false);
        if (isDefer) {
            img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
        } else {
            img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
        }

        isOpenSure = SPUtils.getBoolean(AppConfig.KEY_OPEN_SURE, true);
        if (isOpenSure) {
            img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
        } else {
            img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
        }
        isCloseSure = SPUtils.getBoolean(AppConfig.KEY_CLOSE_SURE, false);
        if (isCloseSure) {
            img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));

        } else {
            img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));

        }

        List<Integer> stopProfitList = new ArrayList<>();
        stopProfitList.add(300);
        stopProfitList.add(350);
        stopProfitList.add(400);
        stopProfitList.add(450);
        stopProfitList.add(500);
        radioRateProfitAdapter = new RadioRateAdapter(getActivity());
        recyclerView_profit.setAdapter(radioRateProfitAdapter);
        recyclerView_profit.setLayoutManager(new GridLayoutManager(getActivity(), stopProfitList.size()));
        radioRateProfitAdapter.setDatas(stopProfitList);
        int index_profit = SPUtils.getInt(AppConfig.INDEX_PROFIT, 0);
        radioRateProfitAdapter.select(index_profit);
        radioRateProfitAdapter.setOnItemClick((position, data) -> {
            radioRateProfitAdapter.select(position);
            recyclerView_profit.setAdapter(radioRateProfitAdapter);
            stopProfit = TradeUtil.div(data, 100, 2);
            SPUtils.putInt(AppConfig.INDEX_PROFIT, position);
            SPUtils.putString(AppConfig.VALUE_PROFIT, String.valueOf(stopProfit));

        });

        List<Integer> stopLossList = new ArrayList<>();
        stopLossList.add(-10);
        stopLossList.add(-30);
        stopLossList.add(-50);
        stopLossList.add(-70);
        stopLossList.add(-90);
        radioRateLossAdapter = new RadioRateAdapter(getActivity());
        recyclerView_loss.setAdapter(radioRateLossAdapter);
        recyclerView_loss.setLayoutManager(new GridLayoutManager(getActivity(), stopLossList.size()));
        radioRateLossAdapter.setDatas(stopLossList);
        int index_loss = SPUtils.getInt(AppConfig.INDEX_LOSS, 4);
        radioRateLossAdapter.select(index_loss);
        radioRateLossAdapter.setOnItemClick((position, data) -> {
            radioRateLossAdapter.select(position);
            recyclerView_loss.setAdapter(radioRateLossAdapter);
            stopLoss = TradeUtil.div(data, 100, 2);
            SPUtils.putInt(AppConfig.INDEX_LOSS, position);
            SPUtils.putString(AppConfig.VALUE_LOSS, String.valueOf(stopLoss));

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_two:
            case R.id.btn_sure:
                getActivity().finish();
                break;
            case R.id.layout_one:
                if (isDefer) {
                    img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isDefer = false;
                    SPUtils.putBoolean(AppConfig.KEY_DEFER, false);
                } else {
                    img_one.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isDefer = true;
                    SPUtils.putBoolean(AppConfig.KEY_DEFER, true);
                }
                break;
            case R.id.layout_two:
                if (isOpenSure) {
                    img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isOpenSure = false;
                    SPUtils.putBoolean(AppConfig.KEY_OPEN_SURE, false);
                } else {
                    img_two.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isOpenSure = true;
                    SPUtils.putBoolean(AppConfig.KEY_OPEN_SURE, true);
                }
                break;
            case R.id.layout_three:
                if (isCloseSure) {
                    img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_false));
                    isCloseSure = false;
                    SPUtils.putBoolean(AppConfig.KEY_CLOSE_SURE, false);
                } else {
                    img_three.setBackground(getResources().getDrawable(R.mipmap.icon_check_true));
                    isCloseSure = true;
                    SPUtils.putBoolean(AppConfig.KEY_CLOSE_SURE, true);
                }
                break;
        }
    }
}
