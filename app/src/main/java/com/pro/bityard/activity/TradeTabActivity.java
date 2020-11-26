package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.fragment.trade.ContractTradeFragment;
import com.pro.bityard.fragment.trade.SpotTradeFragment;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class TradeTabActivity extends BaseActivity implements View.OnClickListener {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "all";
    private int lever;
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;
    @BindView(R.id.tabLayout_title)
    TabLayout tabLayout_title;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    public static void enter(Context context, String tradeType, String data) {
        Intent intent = new Intent(context, TradeTabActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, data);
        bundle.putString(TYPE, tradeType);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }


    @Override
    protected int setContentLayout() {
        return R.layout.activity_trade_tab;
    }


    @Override
    protected void onResume() {
        super.onResume();
        BalanceManger.getInstance().getBalance("USDT");


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }


    @Override
    protected void initPresenter() {

    }


    @Override
    protected void initView(View view) {


        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);
        findViewById(R.id.img_back).setOnClickListener(v -> finish());

    }

    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout_title.setupWithViewPager(viewPager);
        initViewPager(viewPager);
    }

    private void initViewPager(ViewPager viewPager) {
        Log.d("print", "initViewPager:HoldRealFragment: " + tradeType);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new ContractTradeFragment().newInstance(tradeType, itemData), getString(R.string.text_contract));
        myPagerAdapter.addFragment(new SpotTradeFragment().newInstance(tradeType, itemData), getString(R.string.text_derived));
        myPagerAdapter.addFragment(new SpotTradeFragment().newInstance(tradeType, itemData), getString(R.string.text_foreign_exchange));
        myPagerAdapter.addFragment(new SpotTradeFragment().newInstance(tradeType, itemData), getString(R.string.text_spot));
        viewPager.setAdapter(myPagerAdapter);

        String isChOrFt = TradeUtil.type(itemData);
        String zone = TradeUtil.zone(itemData);

        if (isChOrFt.equals(AppConfig.TYPE_FT) && zone.equals(AppConfig.ZONE_MAIN)) {
            tabLayout_title.getTabAt(0).select();
        } else if (isChOrFt.equals(AppConfig.TYPE_FT) && zone.equals(AppConfig.ZONE_DERIVATIVES)) {
            tabLayout_title.getTabAt(1).select();
        } else if (isChOrFt.equals(AppConfig.TYPE_FE)) {
            tabLayout_title.getTabAt(2).select();
        } else if (isChOrFt.equals(AppConfig.TYPE_CH)) {
            tabLayout_title.getTabAt(3).select();

        }
    }




    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        Log.d("print", "initData:590:  " + itemData);
        //根据合约还是现货跳转相应的页面


    }


    @Override
    protected void initEvent() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(TradeTabActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
        //要取消计时 防止内存溢出
        cancelTimer();


    }


}
