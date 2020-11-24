package com.pro.bityard.fragment.trade;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.utils.TradeUtil;

import butterknife.BindView;

public class SpotTradeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.text_currency)
    TextView text_currency;
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;


    public SpotTradeFragment newInstance(String type, String value) {
        SpotTradeFragment fragment = new SpotTradeFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.layout_product).setOnClickListener(this);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_tab;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        tradeType = getArguments().getString(TYPE);
        itemData = getArguments().getString(VALUE);

        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));
    }

    @Override
    public void onClick(View v) {

    }
}
