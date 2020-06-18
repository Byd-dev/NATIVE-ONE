package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.CurrencyListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CurrencyListEntity;
import com.pro.switchlibrary.SPUtils;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class CurrencyRateFragment extends BaseFragment {

    private CurrencyListAdapter currencyListAdapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.img_back).setOnClickListener(v -> Objects.requireNonNull(getActivity()).finish());
        currencyListAdapter = new CurrencyListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(currencyListAdapter);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            initData();

        });

        int anInt = SPUtils.getInt(AppConfig.CURRENCY_INDEX, 1);
        currencyListAdapter.select(anInt);

        currencyListAdapter.setOnItemClick((data, position) -> {
            SPUtils.putString(AppConfig.CURRENCY, data.getCode());
            currencyListAdapter.select(position);
            recyclerView.setAdapter(currencyListAdapter);
            SPUtils.putInt(AppConfig.CURRENCY_INDEX, position);
            NetManger.getInstance().getItemRate("1", data.getCode(), response -> {
                SPUtils.putString(AppConfig.USD_RATE, response.toString());
            });
        });

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_currency_rate;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        CurrencyListEntity data = SPUtils.getData(AppConfig.CURRENCY_LIST, CurrencyListEntity.class);
        if (data != null) {
            currencyListAdapter.setDatas(data.getData());
        } else {
            NetManger.getInstance().currencyList("0",(state, response) -> {
                if (state.equals(SUCCESS)) {
                    CurrencyListEntity currencyListEntity = (CurrencyListEntity) response;
                    SPUtils.putData(AppConfig.CURRENCY_LIST, currencyListEntity);
                    currencyListAdapter.setDatas(currencyListEntity.getData());
                }
            });
        }


    }


}
