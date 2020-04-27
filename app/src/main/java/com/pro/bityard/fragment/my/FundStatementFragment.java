package com.pro.bityard.fragment.my;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.fragment.hold.HistoryFragment;
import com.pro.bityard.fragment.hold.PendingFragment;
import com.pro.bityard.fragment.hold.PositionFragment;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class FundStatementFragment extends BaseFragment implements View.OnClickListener {
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
        text_title.setText(getResources().getString(R.string.text_fund_statement));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);
    }


    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager);
    }
    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new FundStatementItemFragment(), getString(R.string.text_d_w));
        myPagerAdapter.addFragment(new FundStatementItemFragment(), getString(R.string.text_transfer));
        myPagerAdapter.addFragment(new FundStatementItemFragment(), getString(R.string.text_exchange));
        myPagerAdapter.addFragment(new FundStatementItemFragment(), getString(R.string.text_fiat));

        viewPager.setAdapter(myPagerAdapter);
    }
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fund_statement;
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
