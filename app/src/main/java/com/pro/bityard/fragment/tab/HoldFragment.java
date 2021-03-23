package com.pro.bityard.fragment.tab;

import android.app.Activity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.fragment.hold.HistoryFragment;
import com.pro.bityard.fragment.hold.PendingFragment;
import com.pro.bityard.fragment.hold.PositionFragment;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.PendCountManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.manger.TabCountManger;
import com.pro.bityard.utils.SocketUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

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
    @BindView(R.id.radio_position)
    RadioButton radio_position;
    @BindView(R.id.radio_pend)
    RadioButton radio_pend;

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
    @BindView(R.id.radio_position_simulation)
    RadioButton radio_position_simulation;


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

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        // SocketQuoteManger.getInstance().startScheduleJob(QUOTE_SECOND, QUOTE_SECOND);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(v -> getActivity().finish());

        TabCountManger.getInstance().addObserver(this);
        PendCountManger.getInstance().addObserver(this);


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.tab_hold;
    }

    @Override
    protected void intPresenter() {

    }

    private List<String> titles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();

        //行情初始化
        SocketQuoteManger.getInstance().addObserver(this);
        //余额注册
        BalanceManger.getInstance().addObserver(this);
        //净值注册
        NetIncomeManger.getInstance().addObserver(this);
        SocketUtil.switchQuotesList("3001");

    }

    @Override
    public void onPause() {
        super.onPause();
        SocketUtil.switchQuotesList("3002");

    }

    @Override
    protected void initData() {

        titles.add(getString(R.string.text_open));
        titles.add(getString(R.string.text_order));
        titles.add(getString(R.string.text_history));
        mFragments.add(new PositionFragment().newInstance(tradeType));
        mFragments.add(new PendingFragment().newInstance(tradeType));
        mFragments.add(new HistoryFragment().newInstance(tradeType));

        radioGroup_hold.getChildAt(0).performClick();
        radio_position.setChecked(true);
        radio_position_simulation.setChecked(true);
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("print", "onTabSelected:169:  "+tab.getPosition());
                if (tab.getPosition() == 0) {
                    radio_position.setTextColor(activity.getResources().getColor(R.color.maincolor));
                    radio_pend.setTextColor(activity.getResources().getColor(R.color.text_second_color_night));
                } else if (tab.getPosition() == 1) {
                    radio_position.setTextColor(activity.getResources().getColor(R.color.text_second_color_night));
                    radio_pend.setTextColor(activity.getResources().getColor(R.color.maincolor));
                } else if (tab.getPosition() == 2) {
                    radio_position.setTextColor(activity.getResources().getColor(R.color.text_second_color_night));
                    radio_pend.setTextColor(activity.getResources().getColor(R.color.text_second_color_night));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //持仓注册


        PositionRealManger.getInstance().addObserver(this);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager, "1");

        /*持仓 模拟 分割线-----------------------------------------------------------------------------*/
        PositionSimulationManger.getInstance().addObserver(this);
        viewPager_simulation.setOffscreenPageLimit(2);
        tabLayout_simulation.setupWithViewPager(viewPager_simulation);
        initSimulationViewPager(viewPager_simulation, "2");
        tabLayout_simulation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    radio_position_simulation.setTextColor(activity.getResources().getColor(R.color.maincolor));
                } else if (tab.getPosition() == 1) {
                    radio_position_simulation.setTextColor(activity.getResources().getColor(R.color.text_second_color_night));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*持仓 实盘 分割线-----------------------------------------------------------------------------*/


    }

    private String holdSize = "0";
    private String pendSize = "0";

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


    private String type = AppConfig.CONTRACT_ALL;

    @Override
    public void update(Observable o, Object arg) {

        if (o == SocketQuoteManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            List<String> quoteList = arrayMap.get(type);
            Log.d("print", "update:持仓行情: " + quoteList.size());
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


        } else if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            // Log.d("print", "setResult:137实盘:  " + tradeType + "  " + balanceEntity);

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
                if (isAdded()) {
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
                        if (isAdded()) {
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
                                    //   Log.d("print", "update:337:模拟:  " + game);
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
        } else if (o == TabCountManger.getInstance()) {
            String holdSize = (String) arg;

            runOnUiThread(() -> {
                if (tradeType.equals("1")) {
                    radio_position.setText("(" + holdSize + ")");
                } else {
                    radio_position_simulation.setText("(" + holdSize + ")");
                }
            });


        } else if (o == PendCountManger.getInstance()) {
            String pendSize = (String) arg;
            runOnUiThread(() -> {
                if (tradeType.equals("1")) {
                    if (radio_pend!=null){
                        radio_pend.setText("(" + pendSize + ")");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //行情初
        SocketQuoteManger.getInstance().deleteObserver(this);
        //余额注册
        BalanceManger.getInstance().deleteObserver(this);
        //净值注册
        NetIncomeManger.getInstance().deleteObserver(this);
        //
        TabCountManger.getInstance().clear();

    }


}
