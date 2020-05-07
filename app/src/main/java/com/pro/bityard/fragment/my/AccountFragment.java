package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.AccountAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class AccountFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.text_balance)
    TextView text_balance;
    @BindView(R.id.text_balance_currency)
    TextView text_balance_currency;
    private boolean isEyeOpen = true;
    @BindView(R.id.img_eye_switch)
    ImageView img_eye_switch;
    @BindView(R.id.text_currency)
    TextView text_currency;

    private AccountAdapter accountAdapter;

    private BalanceEntity balanceEntity;
    private String netIncomeResult;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView_account)
    RecyclerView recyclerView_account;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_account;
    }

    @Override
    public void onResume() {
        super.onResume();
        //我的页面 火币结算单位
        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        text_currency.setText("(" + cny + ")");
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_account));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        img_eye_switch.setOnClickListener(this);
        //余额初始化
        BalanceManger.getInstance().addObserver(this);
        NetIncomeManger.getInstance().addObserver(this);
        accountAdapter = new AccountAdapter(getActivity());
        recyclerView_account.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_account.setAdapter(accountAdapter);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        swipeRefreshLayout.setOnRefreshListener(this::initData);
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setRefreshing(false);
        BalanceManger.getInstance().getBalance("USDT");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_eye_switch:

                if (isEyeOpen) {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close_black));
                    text_balance.setText("***");
                    text_balance_currency.setText("***");
                    isEyeOpen = false;
                    accountAdapter.setHide(isEyeOpen);
                    recyclerView_account.setAdapter(accountAdapter);
                } else {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open_black));
                    isEyeOpen = true;
                    accountAdapter.setHide(isEyeOpen);
                    recyclerView_account.setAdapter(accountAdapter);
                    if (isLogin()) {
                        if (netIncomeResult != null) {
                            String[] NetIncome = netIncomeResult.split(",");
                            if (NetIncome[0].equals("1")) {
                                if (balanceEntity != null) {
                                    String netIncome = NetIncome[1];
                                    String margin = NetIncome[2];
                                    setMyNetIncome(balanceEntity, netIncome, margin);
                                }
                            }
                        } else {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }
                    } else {
                        text_balance.setText(getResources().getString(R.string.text_default));
                        text_balance_currency.setText(getResources().getString(R.string.text_default));
                    }

                }

                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            Log.d("print", "update:134:  " + balanceEntity);
            if (accountAdapter != null) {
                accountAdapter.setDatas(balanceEntity.getData());
            }
        } else if (o == NetIncomeManger.getInstance()) {
            netIncomeResult = (String) arg;

            String[] NetIncome = netIncomeResult.split(",");
            runOnUiThread(() -> {
                // 1,2.5,5  类型 整体净盈亏  整体  保证金
                String netIncome = NetIncome[1];
                String margin = NetIncome[2];

                if (NetIncome[0].equals("1")) {
                    if (isLogin()) {
                        if (balanceEntity != null) {
                            setMyNetIncome(balanceEntity, netIncome, margin);
                        }
                    } else {
                        if (isEyeOpen) {
                            text_balance.setText(getResources().getString(R.string.text_default));
                            text_balance_currency.setText(getResources().getString(R.string.text_default));
                        }

                    }
                }
            });
        }
    }

    private void setMyNetIncome(BalanceEntity balanceEntity, String netIncome, String margin) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                //汇率是实时的
                TradeUtil.getRateList(balanceEntity, "1", response -> {
                    double money1 = data.getMoney();//可用余额
                    double add2 = TradeUtil.add(money1, Double.parseDouble(margin));//+保证金
                    double ad3 = TradeUtil.add(add2, Double.parseDouble(netIncome));//+浮动盈亏
                    //账户净值=可用余额+占用保证金+浮动盈亏
                    double money = Double.parseDouble(response.toString());//所有钱包的和
                    double add1 = TradeUtil.add(money, Double.parseDouble(margin));//+保证金
                    double add = TradeUtil.add(add1, Double.parseDouble(netIncome));//+浮动盈亏
                    if (isEyeOpen) {
                        if (isAdded()) {
                            text_balance.setText(TradeUtil.getNumberFormat(add, 2));
                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                            text_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(add, Double.parseDouble(string)), 2));
                        }
                    }
                });

            }
        }
    }
}
