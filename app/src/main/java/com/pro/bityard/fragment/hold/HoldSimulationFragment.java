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
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.utils.TradeUtil;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class HoldSimulationFragment extends BaseFragment implements Observer {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.text_available)
    TextView text_balance;
    @BindView(R.id.text_freeze)
    TextView text_freeze;

    private String tradeType;

    public HoldSimulationFragment newInstance(String type) {
        HoldSimulationFragment fragment = new HoldSimulationFragment();
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

        BalanceManger.getInstance().addObserver(this);
        //持仓注册
        PositionSimulationManger.getInstance().addObserver(this);
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
        //myPagerAdapter.addFragment(new PendingFragment().newInstance(tradeType), getString(R.string.text_order));
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
            BalanceEntity.DataBean data = (BalanceEntity.DataBean) arg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tradeType.equals("2") && text_balance != null) {
                        text_balance.setText(TradeUtil.getNumberFormat(data.getGame(), 2));
                    }
                }
            });
        } else if (o == PositionSimulationManger.getInstance()) {
            PositionEntity positionEntity = (PositionEntity) arg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (text_freeze != null) {
                        TradeUtil.getMargin(positionEntity, new TradeResult() {
                            @Override
                            public void setResult(Object response) {
                                text_freeze.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()),2));

                            }
                        });
                    }
                }
            });
        }

    }
}
