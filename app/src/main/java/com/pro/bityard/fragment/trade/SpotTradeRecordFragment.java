package com.pro.bityard.fragment.trade;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.fragment.my.FundExchangeItemFragment;
import com.pro.bityard.fragment.my.FundFiatItemFragment;
import com.pro.bityard.fragment.my.FundStatementItemFragment;
import com.pro.bityard.fragment.my.FundTransferFragment;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class SpotTradeRecordFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_spot));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);
    }

    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_spot_trade_record;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }

    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new SpotRecordItemFragment(), getString(R.string.text_spot_position));
        myPagerAdapter.addFragment(new SpotRecordItemFragment(), getString(R.string.text_history_spot_position));
        myPagerAdapter.addFragment(new SpotRecordItemFragment(), getString(R.string.text_history_trade));

        viewPager.setAdapter(myPagerAdapter);
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
