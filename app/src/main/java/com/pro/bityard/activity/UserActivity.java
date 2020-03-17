package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.fragment.my.LanguageFragment;
import com.pro.bityard.fragment.my.SetUpFragment;
import com.pro.bityard.fragment.my.ThemeFragment;
import com.pro.bityard.fragment.user.LoginFragment;
import com.pro.bityard.fragment.user.RegisterFragment;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class UserActivity extends BaseActivity {
    private static final String TYPE = "USER_TYPE";
    private String type;

    private FragmentTransaction ft;
    //系统设置
    private SetUpFragment setUpFragment;
    //主题设置
    private ThemeFragment themeFragment;
    //语言设置
    private LanguageFragment languageFragment;



    @Override
    protected int setContentLayout() {
        return R.layout.activity_content;
    }

    public static void enter(Context context, String type) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);


        if (type.equals(IntentConfig.Keys.KEY_SET_UP)) {
            addSetUpFragment();
        }else if (type.equals(IntentConfig.Keys.KEY_THEME)){
            addThemeFragment();
        }else if (type.equals(IntentConfig.Keys.KEY_LANGUAGE)){
            addLanguageFragment();
        }


    }

    private void addLanguageFragment() {
        String name = LanguageFragment.class.getSimpleName();
        languageFragment = new LanguageFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, languageFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addThemeFragment() {
        String name = ThemeFragment.class.getSimpleName();
        themeFragment = new ThemeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, themeFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }


    private void addSetUpFragment() {
        String name = SetUpFragment.class.getSimpleName();
        setUpFragment = new SetUpFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, setUpFragment, name);
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
