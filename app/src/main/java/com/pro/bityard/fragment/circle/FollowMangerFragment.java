package com.pro.bityard.fragment.circle;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.StatEntity;
import com.pro.bityard.utils.TradeUtil;

import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class FollowMangerFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_copy_total_amount)
    TextView text_copy_total_amount;
    @BindView(R.id.text_copy_trade_profit)
    TextView text_copy_trade_profit;
    @BindView(R.id.stay_copy_total_amount)
    TextView stay_copy_total_amount;
    @BindView(R.id.stay_copy_trade_profit)
    TextView stay_copy_trade_profit;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        text_title.setText(getResources().getString(R.string.text_copy_trade_settings));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        stay_copy_total_amount.setText(getResources().getString(R.string.text_copy_total_amount)+"(USDT)");
        stay_copy_trade_profit.setText(getResources().getString(R.string.text_copy_trade_profit)+"(USDT)");

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_follow_manger;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        NetManger.getInstance().followerStat((state, response) -> {
            if (state.equals(SUCCESS)){
                StatEntity statEntity= (StatEntity) response;
                text_copy_total_amount.setText(String.valueOf(statEntity.getSumMargin()));
                text_copy_trade_profit.setText(TradeUtil.getNumberFormat(statEntity.getSumIncome(),2));
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}
