package com.pro.bityard.fragment.tab;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.api.TradeResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.fragment.hold.HoldRealFragment;
import com.pro.bityard.fragment.hold.HoldSimulationFragment;
import com.pro.bityard.utils.TradeUtil;

import java.util.List;

import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class HoldFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.text_available)
    TextView text_balance;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.text_freeze)
    TextView text_freeze;

    private boolean isReal=true;
    private BalanceEntity balanceEntity;
    private PositionEntity positionEntity;

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
        NetManger.getInstance().getBalance(new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)){

                }else if (state.equals(SUCCESS)){
                    balanceEntity = (BalanceEntity) response;
                    text_balance.setText(TradeUtil.getNumberFormat(balanceEntity.getData().get(0).getMoney(),2));



                }else if (state.equals(FAILURE)){

                }
            }
        });


        NetManger.getInstance().getHold("1", new OnNetTwoResult() {
            @Override
            public void setResult(String state, Object response1, Object response2) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    positionEntity = (PositionEntity) response1;
                    List<String> quoteList = (List<String>) response2;
                    TradeUtil.getMargin(positionEntity, new TradeResult() {
                        @Override
                        public void setResult(Object response) {
                            text_freeze.setText(TradeUtil.getNumberFormat(Double.parseDouble(response.toString()),2));
                        }
                    });

                } else if (state.equals(FAILURE)) {

                }
            }


        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                showFragment(R.id.layout_content, new HoldRealFragment(), "tradeType", "1");
                text_balance.setText(TradeUtil.getNumberFormat(balanceEntity.getData().get(0).getMoney(),2));
                break;
            case R.id.radio_1:

                showFragment(R.id.layout_content, new HoldSimulationFragment(), "tradeType", "2");
                text_balance.setText(TradeUtil.getNumberFormat(balanceEntity.getData().get(0).getGame(),2));

                break;



        }
    }
}
