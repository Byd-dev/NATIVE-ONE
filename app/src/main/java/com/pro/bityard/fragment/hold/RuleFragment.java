package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class RuleFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.text_trade_name)
    TextView text_trade_name;

    @BindView(R.id.text_currency)
    TextView text_currency;

    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.text_o_n_rule)
    TextView text_o_n_rule;

    public RuleFragment newInstance(String value) {
        RuleFragment fragment = new RuleFragment();
        Bundle args = new Bundle();
        args.putString("VALUE", value);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(View view) {

        if (getArguments() != null) {
            String value = getArguments().getString("VALUE");
            text_trade_name.setText(TradeUtil.listQuoteName(value));
            text_currency.setText("/" + TradeUtil.listQuoteUSD(value));
            ChartUtil.setIcon(TradeUtil.listQuoteName(value), img_bg);
        }
        text_title.setText(getResources().getString(R.string.text_rule));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        text_o_n_rule.setLineSpacing(1.5f, 1.1f);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_rule;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}
