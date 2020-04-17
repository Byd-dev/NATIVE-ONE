package com.pro.bityard.fragment.hold;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.utils.TradeUtil;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class HoldRealFragment extends BaseFragment implements Observer {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @BindView(R.id.text_available)
    TextView text_balance;
    @BindView(R.id.text_freeze)
    TextView text_freeze;
    @BindView(R.id.text_worth)
    TextView text_worth;
    private String tradeType;
    private BalanceEntity balanceEntity;

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
        if (getArguments() != null) {
            tradeType = getArguments().getString("tradeType");
        }
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        //余额注册
        BalanceManger.getInstance().addObserver(this);
        //持仓注册
        PositionRealManger.getInstance().addObserver(this);
        //净值注册
        NetIncomeManger.getInstance().addObserver(this);



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
        Log.d("print", "initViewPager:HoldRealFragment: " + tradeType);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new PendingFragment().newInstance(tradeType), getString(R.string.text_order));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));

        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_hold_tab;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {

            balanceEntity = (BalanceEntity) arg;

            TradeUtil.getRate(balanceEntity, "1", new TradeResult() {
                @Override
                public void setResult(Object response) {
                    Log.d("print", "setResult:137实盘:  " + response.toString());
                }
            });

            runOnUiThread(() -> {
                if (tradeType.equals("1") && text_balance != null) {
                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals("USDT")) {
                            text_balance.setText(TradeUtil.getNumberFormat(data.getMoney(), 2));
                        }
                    }
                }
            });
        } else if (o == PositionRealManger.getInstance()) {
            List<PositionEntity.DataBean> positionList= (List<PositionEntity.DataBean>) arg;
         //   PositionEntity positionEntity = (PositionEntity) arg;
            runOnUiThread(() -> {
                if (text_freeze != null) {
                    TradeUtil.getMargin(positionList, new TradeResult() {
                        @Override
                        public void setResult(Object response) {
                            if (response == null) {
                                text_freeze.setText("--.--");
                            } else {
                                text_freeze.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()), 2));

                            }

                        }
                    });
                }
            });
        } else if (o == NetIncomeManger.getInstance()) {

            String result = (String) arg;
            String[] split = result.split(",");
            runOnUiThread(() -> {
                if (text_worth != null && split[0].equals("1") && tradeType.equals("1")) {
                    // 1,2.5,5  类型 整体净盈亏  整体  保证金
                    String netIncome = split[1];
                    String margin = split[2];
                    if (balanceEntity != null) {
                        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                            if (data.getCurrency().equals("USDT")) {
                                //汇率是实时的
                                TradeUtil.getRate(balanceEntity, "1", response -> {
                                    double money = Double.parseDouble(response.toString());
                                    double add1 = TradeUtil.add(money, Double.parseDouble(margin));
                                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));
                                    text_worth.setText(TradeUtil.getNumberFormat(add, 2));
                                });

                            }
                        }
                    }

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //余额注册
        BalanceManger.getInstance().clear();
        //持仓注册
        PositionRealManger.getInstance().clear();
        //净值注册
        NetIncomeManger.getInstance().clear();
    }
}
