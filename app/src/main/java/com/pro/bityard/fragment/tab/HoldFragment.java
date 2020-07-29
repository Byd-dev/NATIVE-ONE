package com.pro.bityard.fragment.tab;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.fragment.hold.HistoryFragment;
import com.pro.bityard.fragment.hold.PendingFragment;
import com.pro.bityard.fragment.hold.PositionFragment;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.QuoteCustomizeListManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.utils.TradeUtil;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;

public class HoldFragment extends BaseFragment implements Observer {
    /*持仓  ---------------------------------------------------*/
    @BindView(R.id.radioGroup_hold)
    RadioGroup radioGroup_hold;
    @BindView(R.id.layout_real)
    LinearLayout layout_real;
    @BindView(R.id.layout_simulation)
    LinearLayout layout_simulation;

    @BindView(R.id.img_back)
    ImageView img_back;


    /*持仓 实盘 ---------------------------------------------------*/
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.text_available)
    TextView text_available;
    @BindView(R.id.text_freeze)
    TextView text_freeze;
    @BindView(R.id.text_worth)
    TextView text_worth;
    private String tradeType = "1";
    private BalanceEntity balanceEntity;


    /*持仓 模拟 ---------------------------------------------------*/
    @BindView(R.id.tabLayout_simulation)
    TabLayout tabLayout_simulation;
    @BindView(R.id.viewPager_simulation)
    ViewPager viewPager_simulation;
    @BindView(R.id.text_available_simulation)
    TextView text_available_simulation;
    @BindView(R.id.text_freeze_simulation)
    TextView text_freeze_simulation;
    @BindView(R.id.text_worth_simulation)
    TextView text_worth_simulation;

    private List<PositionEntity.DataBean> positionRealList;
    private List<PositionEntity.DataBean> positionSimulationList;
    private String netIncomeResult;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        QuoteListManger.getInstance().startScheduleJob(QUOTE_SECOND, QUOTE_SECOND);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(v -> getActivity().finish());

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.tab_hold;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        //行情初始化
        QuoteCustomizeListManger.getInstance().addObserver(this);
        //余额注册
        BalanceManger.getInstance().addObserver(this);
        //净值注册
        NetIncomeManger.getInstance().addObserver(this);

        radioGroup_hold.getChildAt(0).performClick();
        radioGroup_hold.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_hold_0:
                    layout_real.setVisibility(View.VISIBLE);
                    layout_simulation.setVisibility(View.GONE);
                    tradeType = "1";
                    break;
                case R.id.radio_hold_1:
                    layout_real.setVisibility(View.GONE);
                    layout_simulation.setVisibility(View.VISIBLE);
                    tradeType = "2";
                    break;
            }
        });

        /*持仓 实盘 分割线-----------------------------------------------------------------------------*/
        //持仓注册
        PositionRealManger.getInstance().addObserver(this);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager, "1");

        /*持仓 模拟 分割线-----------------------------------------------------------------------------*/
        PositionSimulationManger.getInstance().addObserver(this);
        viewPager_simulation.setOffscreenPageLimit(3);
        tabLayout_simulation.setupWithViewPager(viewPager_simulation);
        initSimulationViewPager(viewPager_simulation, "2");
    }

    private void initViewPager(ViewPager viewPager, String tradeType) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new PendingFragment().newInstance(tradeType), getString(R.string.text_order));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));

        viewPager.setAdapter(myPagerAdapter);
    }

    private void initSimulationViewPager(ViewPager viewPager, String tradeType) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new PositionFragment().newInstance(tradeType), getString(R.string.text_open));
        myPagerAdapter.addFragment(new HistoryFragment().newInstance(tradeType), getString(R.string.text_history));
        viewPager.setAdapter(myPagerAdapter);
    }

    private List<String> quoteList;
    private ArrayMap<String, List<String>> arrayMap;
    private String type = "0";

    @Override
    public void update(Observable o, Object arg) {

         if (o == QuoteCustomizeListManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            List<String> quoteList = arrayMap.get(type);
            runOnUiThread(() -> {
                assert quoteList != null;
                if (quoteList.size() >= 3) {
                    if (isLogin()) {
                        if (tradeType.equals("1")) {
                            TradeUtil.setNetIncome(tradeType, positionRealList, quoteList);
                        } else {
                            TradeUtil.setNetIncome(tradeType, positionSimulationList, quoteList);

                        }
                    }

                }
            });


        }else if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            Log.d("print", "setResult:137实盘:  " + tradeType + "  " + balanceEntity);

            runOnUiThread(() -> {
                if (tradeType.equals("1") && text_available != null) {
                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals("USDT")) {
                            text_available.setText(TradeUtil.getNumberFormat(data.getMoney(), 2));
                        }
                    }
                }
            });
            runOnUiThread(() -> {
                if (tradeType.equals("2") && text_available_simulation != null) {
                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals("USDT")) {
                            text_available_simulation.setText(TradeUtil.getNumberFormat(data.getGame(), 2));
                        }
                    }
                }
            });
        } else if (o == PositionRealManger.getInstance()) {
            positionRealList = (List<PositionEntity.DataBean>) arg;
            if (positionRealList.size() == 0) {
                if (isAdded()){
                    text_freeze.setText(getResources().getString(R.string.text_default));
                }
            } else {
                runOnUiThread(() -> {
                    if (text_freeze != null) {
                        TradeUtil.getMargin(positionRealList, response -> {
                            if (response == null) {
                                text_freeze.setText(getResources().getString(R.string.text_default));
                            } else {
                                text_freeze.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()), 2));
                            }
                        });
                    }
                });
            }

        } else if (o == PositionSimulationManger.getInstance()) {
            positionSimulationList = (List<PositionEntity.DataBean>) arg;
            Log.d("print", "update:持仓列表模拟:  " + positionSimulationList.size());
            if (positionSimulationList.size() == 0) {
                if (isAdded()) {
                    text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                }

            } else {
                runOnUiThread(() -> {
                    if (text_freeze_simulation != null) {
                        TradeUtil.getMargin(positionSimulationList, response -> {
                            if (response == null) {
                                text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                            } else {
                                text_freeze_simulation.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()), 2));
                            }
                        });
                    }
                });
            }

        } else if (o == NetIncomeManger.getInstance()) {
            netIncomeResult = (String) arg;
            String[] NetIncome = netIncomeResult.split(",");
            runOnUiThread(() -> {
                // 1,2.5,5  类型 整体净盈亏  整体  保证金
                String netIncome = NetIncome[1];
                String margin = NetIncome[2];

                if (NetIncome[0].equals("1") && tradeType.equals("1")) {
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            setMyNetIncome(balanceEntity, netIncome, margin);
                        }
                    } else {
                        if (text_worth != null) {
                            text_worth.setText(getResources().getString(R.string.text_default));
                        }
                        if (isAdded()){
                            text_worth_simulation.setText(getResources().getString(R.string.text_default));
                            text_available.setText(getResources().getString(R.string.text_default));
                            text_available_simulation.setText(getResources().getString(R.string.text_default));
                            text_freeze.setText(getResources().getString(R.string.text_default));
                            text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                        }
                    }
                }
            });


            runOnUiThread(() -> {
                if (text_worth_simulation != null && NetIncome[0].equals("2") && tradeType.equals("2")) {
                    // 1,2.5,5
                    String netIncome = NetIncome[1];
                    String margin = NetIncome[2];
                    Log.d("print", "update:模拟: " + netIncome + "  --  " + margin);
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                                if (data.getCurrency().equals("USDT")) {
                                    double game = data.getGame();
                                    Log.d("print", "update:337:模拟:  " + game);
                                    double add1 = TradeUtil.add(game, Double.parseDouble(margin));
                                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));
                                    text_worth_simulation.setText(TradeUtil.getNumberFormat(add, 2));
                                }
                            }
                        }
                    } else {
                        text_worth.setText(getResources().getString(R.string.text_default));
                        text_worth_simulation.setText(getResources().getString(R.string.text_default));
                        text_available.setText(getResources().getString(R.string.text_default));
                        text_available_simulation.setText(getResources().getString(R.string.text_default));
                        text_freeze.setText(getResources().getString(R.string.text_default));
                        text_freeze_simulation.setText(getResources().getString(R.string.text_default));
                    }


                }
            });
        }
    }

    /*设置我的页面的总净资产*/
    private void setMyNetIncome(BalanceEntity balanceEntity, String netIncome, String margin) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                //汇率是实时的
                TradeUtil.getRateList(balanceEntity, "1", response -> {
                    double money1 = data.getMoney();//可用余额
                    double add2 = TradeUtil.add(money1, Double.parseDouble(margin));//+保证金
                    double ad3 = TradeUtil.add(add2, Double.parseDouble(netIncome));//+浮动盈亏
                    if (text_worth != null) {
                        text_worth.setText(TradeUtil.getNumberFormat(ad3, 2));
                    }

                });

            }
        }
    }

}
