package com.pro.bityard.fragment.tab;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.fragment.hold.HoldRealFragment;
import com.pro.bityard.fragment.hold.HoldSimulationFragment;

import butterknife.BindView;

public class HoldFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.text_available)
    TextView text_balance;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.text_freeze)
    TextView text_freeze;

    private BalanceEntity balanceEntity;
    private BalanceEntity.DataBean data;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();
        showFragment(R.id.layout_content, new HoldRealFragment(), "tradeType", "1");


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_hold;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        //获得余额
       /* NetManger.getInstance().getBalance(new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)){

                }else if (state.equals(SUCCESS)){
                    balanceEntity = (BalanceEntity) response;
                    text_balance.setText(TradeUtil.getNumberFormat(balanceEntity.getData().get(0).getMoney(),2));



                }else if (state.equals(FAILURE)){

                }
            }
        });*/

        // text_balance.setText(TradeUtil.getNumberFormat(data.getMoney(),2));

        data = NetManger.getInstance().getBalanceData();
        // text_balance.setText(TradeUtil.getNumberFormat((data.getMoney()), 2));


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                showFragment(R.id.layout_content, new HoldRealFragment(), "tradeType", "1");
                // text_balance.setText(TradeUtil.getNumberFormat((data.getMoney()), 2));


                break;
            case R.id.radio_1:

                showFragment(R.id.layout_content, new HoldSimulationFragment(), "tradeType", "2");
                //text_balance.setText(TradeUtil.getNumberFormat((data.getGame()), 2));


                break;


        }
    }


}
