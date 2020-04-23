package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;

import butterknife.BindView;

public class SafeCenterFragment extends BaseFragment implements View.OnClickListener {



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.img_back).setOnClickListener(this);


        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.layout_three).setOnClickListener(this);
        view.findViewById(R.id.layout_four).setOnClickListener(this);


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_safe_center;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.layout_one:

                UserActivity.enter(getActivity(),IntentConfig.Keys.KEY_SAFE_CENTER_LOGIN_PASS);

                break;
            case R.id.layout_two:

                break;
            case R.id.layout_three:
                break;
            case R.id.layout_four:

                break;


        }
    }
}
