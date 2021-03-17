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
import com.pro.bityard.manger.QuoteCodeManger;
import com.pro.bityard.manger.SpotCodeManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.TradeUtil;
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

    private String quote_code;

    private boolean isContract = true;

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

    private String defaultContract = "BTCUSDT1808,-1,31603.68,34457.51,31601.71,0.5521,31604.92,0.5521,34616.86,31312.54,48911.0,FT,main,BTCUSDT,USDT";
    private String defaultSpot = "BTCUSDT_CC1808,-1,31619.20,33528.86,31619.2,0.0025,31619.2,0.0025,34875.0,31311.93,88386.8095,CH,defi,BTCUSDT,USDT";

  /*  private final Timer timer = new Timer();
    private TimerTask task;*/

    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (quote_code != null) {
                String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);

                if (isContract) {
                    quote_code = itemQuoteContCode(defaultContract);


                } else {
                    Log.d("print", "handleMessage:TradeTabd订阅现货: " + isContract + "  " + quote_code);
                    quote_code = itemQuoteContCode(defaultSpot);

                }


            }
            super.handleMessage(msg);
        }

    };*/


    @Override
    protected void initView(View view) {


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
                    isContract = true;

                } else if (position == 1) {
                    isContract = false;
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
        Log.d("print", "initViewPager:HoldRealFragment:实盘模拟: " + tradeType);
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
        String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
        WebSocketManager.getInstance().sendQuotes("3001", quote_code,null);

        String isChOrFt = TradeUtil.type(itemData);
        if (isChOrFt.equals(AppConfig.TYPE_FT)) {
            defaultContract = itemData;
            quote_code = itemQuoteContCode(defaultContract);
        } else if (isChOrFt.equals(AppConfig.TYPE_CH)) {
            defaultSpot = itemData;
            quote_code = itemQuoteContCode(defaultSpot);

        }

      /*  task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(task, ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND);*/
        //根据合约还是现货跳转相应的页面
//BTCUSDT1808,-1,31603.68,34457.51,31601.71,0.5521,31604.92,0.5521,34616.86,31312.54,48911.0,FT,main,BTC,USDT
//BTCUSDT_CC1808,-1,31619.20,33528.86,31619.2,0.0025,31619.2,0.0025,34875.0,31311.93,88386.8095,CH,defi,BTC,USDT
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
    protected void onPause() {
        super.onPause();
        Toast.makeText(TradeTabActivity.this, "onPause", Toast.LENGTH_LONG).show();

        String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
        WebSocketManager.getInstance().cancelQuotes("3002", quote_code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(TradeTabActivity.this, "onDestroy", Toast.LENGTH_LONG).show();
        //要取消计时 防止内存溢出
        //timer.cancel();


    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteCodeManger.getInstance()) {
            defaultContract = (String) arg;
            Log.d("print", "update:272: " + defaultContract);
            isContract = true;
            String isChOrFt = TradeUtil.type(defaultContract);
            tabLayout_title.getTabAt(0).select();


        } else if (o == SpotCodeManger.getInstance()) {
            defaultSpot = (String) arg;
            Log.d("print", "update:285: " + defaultSpot);
            isContract = false;
            String isChOrFt = TradeUtil.type(defaultSpot);
            tabLayout_title.getTabAt(1).select();

        }

    }
}
