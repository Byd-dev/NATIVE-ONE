package com.pro.bityard.fragment.tab;

import android.util.Log;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.quote.Observer;

public class MarketFragment extends BaseFragment implements Observer {
    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_market;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void update(String message) {
        Log.d("print", "update:行情页面:  "+message);
    }
}
