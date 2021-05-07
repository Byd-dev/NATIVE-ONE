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
import com.pro.bityard.manger.Quote15MinHistoryManger;
import com.pro.bityard.manger.Quote1MinHistoryManger;
import com.pro.bityard.manger.Quote3MinHistoryManger;
import com.pro.bityard.manger.Quote5MinHistoryManger;
import com.pro.bityard.manger.Quote60MinHistoryManger;
import com.pro.bityard.manger.QuoteCodeManger;
import com.pro.bityard.manger.QuoteDayHistoryManger;
import com.pro.bityard.manger.QuoteMonthHistoryManger;
import com.pro.bityard.manger.QuoteWeekHistoryManger;
import com.pro.bityard.manger.SpotCodeManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.SocketUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;

public class TradeTabActivity extends BaseActivity implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    private static final String quoteType = "all";
    private int lever;
    private String tradeType = "1"; //实盘=1 模拟=2
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
    protected void onStart() {
        super.onStart();
        Log.d("progress", "onStart: "+"TabActivity onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("progress", "onStop: "+"TabActivity onStop");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("progress", "onResume: "+"TabActivity onResume");

        BalanceManger.getInstance().getBalance("USDT");
        WebSocketManager.getInstance().sendQuotes("4001", itemQuoteContCode(itemData), "1");


    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("progress", "onPause: "+"TabActivity onPause");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("progress", "onDestroy: "+"TabActivity onDestroy");


    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setTheme(this);


    }




    @Override
    protected void initPresenter() {

    }

    private String defaultContract = "BTCUSDT1808,-1,31603.68,34457.51,31601.71,0.5521,31604.92,0.5521,34616.86,31312.54,48911.0,FT,main,BTCUSDT,USDT";
    private String defaultSpot = "BTCUSDT_CC1808,-1,31619.20,33528.86,31619.2,0.0025,31619.2,0.0025,34875.0,31311.93,88386.8095,CH,defi,BTCUSDT,USDT";



    @Override
    protected void initView(View view) {
        Log.d("progress", "initView: "+"TabActivity initView");

        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);
        findViewById(R.id.img_back).setOnClickListener(v -> finish());
        QuoteCodeManger.getInstance().addObserver(this);
        SpotCodeManger.getInstance().addObserver(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                    WebSocketManager.getInstance().cancelQuotes("4002", itemQuoteContCode(defaultSpot));
                    WebSocketManager.getInstance().sendQuotes("4001", itemQuoteContCode(defaultContract), "1");

                } else if (position == 1) {

                    WebSocketManager.getInstance().cancelQuotes("4002", itemQuoteContCode(defaultContract));
                    WebSocketManager.getInstance().sendQuotes("4001", itemQuoteContCode(defaultSpot), "1");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout_title.setupWithViewPager(viewPager);
        initViewPager(viewPager);
    }

    private void initViewPager(ViewPager viewPager) {

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new ContractTradeFragment().newInstance(tradeType, defaultContract), getString(R.string.text_contract));
        myPagerAdapter.addFragment(new SpotTradeFragment().newInstance(tradeType, defaultSpot), getString(R.string.text_spot));
        viewPager.setAdapter(myPagerAdapter);
        String isChOrFt = TradeUtil.type(itemData);
        if (isChOrFt.equals(AppConfig.TYPE_FT)) {
            tabLayout_title.getTabAt(0).select();


        } else if (isChOrFt.equals(AppConfig.TYPE_CH)) {
            tabLayout_title.getTabAt(1).select();


        }

    }


    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        assert bundle != null;
        tradeType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        Log.d("print", "initData:590:  " + itemData);


        String isChOrFt = TradeUtil.type(itemData);
        if (isChOrFt.equals(AppConfig.TYPE_FT)) {
            defaultContract = itemData;

        } else if (isChOrFt.equals(AppConfig.TYPE_CH)) {
            defaultSpot = itemData;

        }

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
    public void update(Observable o, Object arg) {
        if (o == QuoteCodeManger.getInstance()) {
            defaultContract = (String) arg;
            Log.d("print", "update:272: " + defaultContract);

            tabLayout_title.getTabAt(0).select();


        } else if (o == SpotCodeManger.getInstance()) {
            defaultSpot = (String) arg;
            Log.d("print", "update:285: " + defaultSpot);

            tabLayout_title.getTabAt(1).select();

        }

    }
}
