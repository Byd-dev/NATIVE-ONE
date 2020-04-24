package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

public class SafeCenterFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.text_pin_tip)
    TextView text_pin_tip;

    @BindView(R.id.text_mobile_tip)
    TextView text_mobile_tip;

    @BindView(R.id.text_email_tip)
    TextView text_email_tip;

    @Override
    public void onResume() {
        super.onResume();
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        Log.d("print", "onResume:27:用户:  " + data);
        if (data != null) {
            LoginEntity.UserBean user = data.getUser();
            int pw_w = user.getPw_w();
            if (pw_w == 0) {
                text_pin_tip.setText(getResources().getString(R.string.text_not_set));
            } else {
                text_pin_tip.setText(getResources().getString(R.string.text_change));
            }
            Log.d("print", "onResume:42:  " + user.getMobile() + " --    " + user.getEmail());
            if (user.getMobile().equals("")) {
                text_mobile_tip.setText(R.string.text_unbond);
            } else {
                text_mobile_tip.setText(R.string.text_change);
            }

            if (user.getEmail().equals("")) {
                text_email_tip.setText(getResources().getString(R.string.text_not_set));
            } else {
                text_email_tip.setText(R.string.text_change);

            }

        }

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

                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_LOGIN_PASS);

                break;
            case R.id.layout_two:
                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_FUNDS_PASS);

                break;
            case R.id.layout_three:
                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_MOBILE);

                break;
            case R.id.layout_four:
                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_EMAIL);

                break;


        }
    }
}
