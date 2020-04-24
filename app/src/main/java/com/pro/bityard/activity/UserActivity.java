package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.fragment.my.CurrencyRateFragment;
import com.pro.bityard.fragment.my.EmailBindChangeFragment;
import com.pro.bityard.fragment.my.FundsPassChangeFragment;
import com.pro.bityard.fragment.my.LanguageFragment;
import com.pro.bityard.fragment.my.LoginPassChangeFragment;
import com.pro.bityard.fragment.my.MobileBindChangeFragment;
import com.pro.bityard.fragment.my.SafeCenterFragment;
import com.pro.bityard.fragment.my.SetUpFragment;
import com.pro.bityard.fragment.my.ThemeFragment;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class UserActivity extends BaseActivity {
    private static final String TYPE = "USER_TYPE";

    private FragmentTransaction ft;


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
        String type = intent.getStringExtra(TYPE);


        assert type != null;
        switch (type) {
            case IntentConfig.Keys.KEY_SET_UP:
                addSetUpFragment();
                break;
            case IntentConfig.Keys.KEY_THEME:
                addThemeFragment();
                break;
            case IntentConfig.Keys.KEY_LANGUAGE:
                addLanguageFragment();
                break;
            case IntentConfig.Keys.KEY_ASSET:
                addAssetFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER:
                addSafeCenterFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER_LOGIN_PASS:
                addLoginPassChangeFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER_FUNDS_PASS:
                addFundsPassChangeFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_MOBILE:
                addBindChangePhoneFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_EMAIL:
                addBindChangeEmailFragment();
                break;
        }


    }

    private void addBindChangeEmailFragment() {
        String name = EmailBindChangeFragment.class.getSimpleName();
        //安全中心
        EmailBindChangeFragment fragment = new EmailBindChangeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addBindChangePhoneFragment() {
        String name = MobileBindChangeFragment.class.getSimpleName();
        //安全中心
        MobileBindChangeFragment fragment = new MobileBindChangeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFundsPassChangeFragment() {
        String name = FundsPassChangeFragment.class.getSimpleName();
        //安全中心
        FundsPassChangeFragment fragment = new FundsPassChangeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addLoginPassChangeFragment() {
        String name = LoginPassChangeFragment.class.getSimpleName();
        //安全中心
        LoginPassChangeFragment fragment = new LoginPassChangeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addSafeCenterFragment() {
        String name = SafeCenterFragment.class.getSimpleName();
        //安全中心
        SafeCenterFragment safeCenterFragment = new SafeCenterFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, safeCenterFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addAssetFragment() {
        String name = CurrencyRateFragment.class.getSimpleName();
        //汇率
        CurrencyRateFragment currencyRateFragment = new CurrencyRateFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, currencyRateFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addLanguageFragment() {
        String name = LanguageFragment.class.getSimpleName();
        //语言设置
        LanguageFragment languageFragment = new LanguageFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, languageFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addThemeFragment() {
        String name = ThemeFragment.class.getSimpleName();
        //主题设置
        ThemeFragment themeFragment = new ThemeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, themeFragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }


    private void addSetUpFragment() {
        String name = SetUpFragment.class.getSimpleName();
        //系统设置
        SetUpFragment setUpFragment = new SetUpFragment();
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
