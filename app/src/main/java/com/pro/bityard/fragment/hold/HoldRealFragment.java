package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class HoldRealFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String tradeType;

    public HoldRealFragment newInstance(String type) {
        HoldRealFragment fragment = new HoldRealFragment();
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


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initContent();
            }
        }, 50);


    }

    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager);
    }


    private void initViewPager(ViewPager viewPager) {
        Log.d("print", "initViewPager:HoldRealFragment: "+tradeType);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new PendingFragment().newInstance(tradeType), getString(R.string.text_order));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));

        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_hold_real;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


    }
}
