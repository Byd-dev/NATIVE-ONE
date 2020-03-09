package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.fragment.user.LoginFragment;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends BaseActivity {
    private static final String TYPE = "USER_TYPE";
    private String type;

    private FragmentTransaction ft;
    //登录页面
    private LoginFragment loginFragment;





    @Override
    protected int setContentLayout() {
        return R.layout.activity_user;
    }

    public static void enter(Context context, String type) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);


        if (type.equals(IntentConfig.Keys.KEY_LOGIN)) {
            addLoginFragment();
        }


    }



    private void addLoginFragment() {
        String name = LoginFragment.class.getSimpleName();
        loginFragment = new LoginFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, loginFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



}
