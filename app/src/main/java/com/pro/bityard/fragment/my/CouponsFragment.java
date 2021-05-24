package com.pro.bityard.fragment.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class CouponsFragment extends BaseFragment implements Observer, View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_bonus_balance)
    TextView text_bonus_balance;
    @BindView(R.id.text_bonus_balance_currency)
    TextView text_bonus_balance_currency;

    @BindView(R.id.text_gift_balance)
    TextView text_gift_balance;

    @BindView(R.id.text_gift_balance_currency)
    TextView text_gift_balance_currency;

    @BindView(R.id.text_title)
    TextView text_title;

    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(activity.getResources().getString(R.string.text_coupons));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.stay_bonus).setOnClickListener(this);
        view.findViewById(R.id.stay_gift).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_coupons;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        BalanceManger.getInstance().addObserver(this);


    }

    private void setBonus(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_bonus_balance.setText(TradeUtil.getNumberFormat(data.getPrize(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_bonus_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getPrize(), Double.parseDouble(string)), 2));


            }
        }
    }

    private void setGift(BalanceEntity balanceEntity) {
        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
            if (data.getCurrency().equals("USDT")) {
                text_gift_balance.setText(TradeUtil.getNumberFormat(data.getLucky(), 2));
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                text_gift_balance_currency.setText(TradeUtil.getNumberFormat(TradeUtil.mul(data.getLucky(), Double.parseDouble(string)), 2));


            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {

            BalanceEntity balanceEntity = (BalanceEntity) arg;
            setBonus(balanceEntity);
            setGift(balanceEntity);
        }
    }
    private String prizeTradeValue;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.stay_bonus:
                String prizeTrade = SPUtils.getString(AppConfig.PRIZE_TRADE, null);
                if (prizeTrade != null) {
                    prizeTradeValue = TradeUtil.mul(Double.parseDouble(prizeTrade), 100) + "%";
                } else {
                    prizeTradeValue = null;
                }
                String format = String.format(getString(R.string.text_used_to_deduct), prizeTradeValue);

                Util.lightOff(activity);
                PopUtil.getInstance().showTip(activity, layout_view, false, format,
                        state -> {

                        });
                break;
            case R.id.stay_gift:
                Util.lightOff(activity);
                PopUtil.getInstance().showTip(activity, layout_view, false, getString(R.string.text_trading_fees),
                        state -> {

                        });
                break;
        }
    }
}
