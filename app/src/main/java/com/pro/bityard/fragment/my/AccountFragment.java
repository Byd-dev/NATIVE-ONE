package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.activity.WebActivity;
import com.pro.bityard.adapter.AccountAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class AccountFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    TextView text_balance;

    TextView text_balance_currency;
    private boolean isEyeOpen = true;
    ImageView img_eye_switch;
    TextView text_currency;



    //礼金余额
    private TextView text_bonus_balance, text_bonus_balance_currency, text_bonus_currency;
    //红包余额
    private TextView text_gift_balance, text_gift_balance_currency, text_gift_currency;


    private AccountAdapter accountAdapter;

    private BalanceEntity balanceEntity;
    private String netIncomeResult;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView_account)
    HeaderRecyclerView recyclerView_account;
    private String prizeTradeValue;

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
        text_bonus_currency.setText("(" + cny + ")");
        text_gift_currency.setText("(" + cny + ")");

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_account));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        //余额初始化
        BalanceManger.getInstance().addObserver(this);
        NetIncomeManger.getInstance().addObserver(this);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_account_head, null);
        text_balance = headView.findViewById(R.id.text_balance);
        text_balance_currency = headView.findViewById(R.id.text_balance_currency);
        img_eye_switch = headView.findViewById(R.id.img_eye_switch);
        text_currency = headView.findViewById(R.id.text_currency);


        text_bonus_balance = headView.findViewById(R.id.text_bonus_balance);
        text_bonus_balance_currency = headView.findViewById(R.id.text_bonus_balance_currency);
        text_bonus_currency = headView.findViewById(R.id.text_bonus_currency);

        text_gift_balance = headView.findViewById(R.id.text_gift_balance);
        text_gift_balance_currency = headView.findViewById(R.id.text_gift_balance_currency);
        text_gift_currency = headView.findViewById(R.id.text_gift_currency);

        img_eye_switch.setOnClickListener(this);
        headView.findViewById(R.id.text_deposit).setOnClickListener(this);
        headView.findViewById(R.id.text_withdrawal).setOnClickListener(this);
        headView.findViewById(R.id.text_quick_exchange).setOnClickListener(this);
        headView.findViewById(R.id.text_fiat).setOnClickListener(this);
        accountAdapter = new AccountAdapter(getActivity());
        recyclerView_account.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_account.addHeaderView(headView);
        recyclerView_account.setAdapter(accountAdapter);
        Util.colorSwipe(getActivity(),swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this::initData);

        headView.findViewById(R.id.stay_bonus).setOnClickListener(this);
        headView.findViewById(R.id.stay_gift).setOnClickListener(this);

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
      LoginEntity  loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, AppConfig.ZH_SIMPLE);

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
                    accountAdapter.notifyDataSetChanged();
                } else {
                    img_eye_switch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open_black));
                    isEyeOpen = true;
                    accountAdapter.setHide(isEyeOpen);
                    accountAdapter.notifyDataSetChanged();
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
            /*充币*/
            case R.id.text_deposit:
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(getActivity(), NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null,"/deposit"), getResources().getString(R.string.text_recharge));
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*提币*/
            case R.id.text_withdrawal:
                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_WITHDRAWAL);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*币币闪兑*/
            case R.id.text_quick_exchange:
                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_QUICK_EXCHANGE);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*法币充值*/
            case R.id.text_fiat:
                String url_api=null;
                switch (language){
                    case AppConfig.ZH_SIMPLE:
                        url_api="/cnRecharge";
                        break;
                    case AppConfig.VI_VN:
                        url_api="/viRecharge";
                        break;
                    case AppConfig.IN_ID:
                        url_api="/idRecharge";
                        break;
                }
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(getActivity(), NetManger.getInstance().h5Url(loginEntity.getAccess_token(),null, url_api), getResources().getString(R.string.text_fabi_trade));
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.stay_bonus:
                String prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);
                if (prizeTrade!=null){
                    prizeTradeValue = TradeUtil.mul(Double.parseDouble(prizeTrade), 100)+"%";
                }else {
                    prizeTradeValue=null;
                }
                String format = String.format(getString(R.string.text_used_to_deduct), prizeTradeValue);

                Util.lightOff(getActivity());
                PopUtil.getInstance().showTip(getActivity(), layout_view, false, format,
                        state -> {

                        });
                break;
            case R.id.stay_gift:
                Util.lightOff(getActivity());
                PopUtil.getInstance().showTip(getActivity(), layout_view, false, getString(R.string.text_trading_fees),
                        state -> {

                        });
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

            setBonus(balanceEntity);
            setGift(balanceEntity);

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
                            if (isAdded()){
                                text_balance.setText(getResources().getString(R.string.text_default));
                                text_balance_currency.setText(getResources().getString(R.string.text_default));
                            }

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


    private void setBonus(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_bonus_balance.setText(TradeUtil.getNumberFormat(data.getPrize(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_bonus_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getPrize(), Double.parseDouble(string)), 2));


                //汇率是实时的
              /*  TradeUtil.getRateList(balanceEntity, "2", response -> {
                    double money = Double.parseDouble(response.toString());//所有钱包的和

                    if (isEyeOpen) {
                        if (isAdded()) {
                            text_bonus_balance.setText(TradeUtil.getNumberFormat(money, 2));
                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                            text_bonus_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(money, Double.parseDouble(string)), 2));
                        }
                    }
                });*/

            }
        }
    }

    private void setGift(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_gift_balance.setText(TradeUtil.getNumberFormat(data.getLucky(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_gift_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getLucky(), Double.parseDouble(string)), 2));

              /*  //汇率是实时的
                TradeUtil.getRateList(balanceEntity, "3", response -> {
                    double money = Double.parseDouble(response.toString());//所有钱包的和

                    if (isEyeOpen) {
                        if (isAdded()) {
                            text_gift_balance.setText(TradeUtil.getNumberFormat(money, 2));
                            String string = SPUtils.getString(AppConfig.USD_RATE, null);
                            text_gift_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(money, Double.parseDouble(string)), 2));
                        }
                    }
                });*/

            }
        }
    }
}
