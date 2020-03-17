package com.pro.bityard.fragment.tab;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

public class MyFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.text_userName)
    TextView text_userName;

    @BindView(R.id.text_uid)
    TextView text_uid;


    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()){
            LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            Log.d("print", "onResume:30:  "+data);
            text_userName.setText(data.getUser().getAccount());
            text_uid.setText(data.getUser().getUserId());
        }else {
            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setText("--");

        }

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        view.findViewById(R.id.layout_six).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_six:
                if (isLogin()){
                    UserActivity.enter(getContext(), IntentConfig.Keys.KEY_SET_UP);
                }else {
                    LoginActivity.enter(getContext(),IntentConfig.Keys.KEY_LOGIN);
                }
                break;
        }
    }
}
