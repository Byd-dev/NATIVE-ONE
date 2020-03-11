package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.fragment.user.ForgetFragment;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class ForgetActivity extends BaseActivity {
    private static final String TYPE = "USER_TYPE";
    private String type;

    private FragmentTransaction ft;
    //注册页面
    private ForgetFragment forgetFragment;




    @Override
    protected int setContentLayout() {
        return R.layout.activity_content;
    }

    public static void enter(Context context, String type) {
        Intent intent = new Intent(context, ForgetActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);


        if (type.equals(IntentConfig.Keys.KEY_FORGET)) {
            addForgetFragment();
        }


    }

    private void addForgetFragment() {
        String name = ForgetFragment.class.getSimpleName();
        forgetFragment = new ForgetFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, forgetFragment, name);
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
