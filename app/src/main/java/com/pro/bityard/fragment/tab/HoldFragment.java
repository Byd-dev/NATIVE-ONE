package com.pro.bityard.fragment.tab;

import android.view.View;
import android.widget.RadioGroup;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.fragment.hold.HoldRealFragment;
import com.pro.bityard.fragment.hold.HoldSimulationFragment;

import butterknife.BindView;

public class HoldFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;


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


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                showFragment(R.id.layout_content, new HoldRealFragment(), "tradeType", "1");
                //持仓初始化
                //PositionManger.getInstance().getHold("1");
                break;
            case R.id.radio_1:

                showFragment(R.id.layout_content, new HoldSimulationFragment(), "tradeType", "2");
               // PositionManger.getInstance().getHold("2");


                break;


        }
    }


}
