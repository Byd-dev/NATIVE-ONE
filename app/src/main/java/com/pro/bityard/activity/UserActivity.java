package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.fragment.circle.FollowEditFragment;
import com.pro.bityard.fragment.circle.FollowLogFragment;
import com.pro.bityard.fragment.circle.FollowMangerFragment;
import com.pro.bityard.fragment.circle.FollowSettingsFragment;
import com.pro.bityard.fragment.circle.FollowerListFragment;
import com.pro.bityard.fragment.circle.FollowerMangerFragment;
import com.pro.bityard.fragment.circle.IdeaSelectFragment;
import com.pro.bityard.fragment.circle.SearchNicknameFragment;
import com.pro.bityard.fragment.hold.RuleFragment;
import com.pro.bityard.fragment.my.AccountFragment;
import com.pro.bityard.fragment.my.AddAddressFragment;
import com.pro.bityard.fragment.my.AnnouncementFragment;
import com.pro.bityard.fragment.tab.HoldRadioFragment;
import com.pro.bityard.fragment.trade.ContractFollowRecordFragment;
import com.pro.bityard.fragment.my.CurrencyRateFragment;
import com.pro.bityard.fragment.my.EmailBindChangeFragment;
import com.pro.bityard.fragment.my.FundStatementFragment;
import com.pro.bityard.fragment.my.FundsPassChangeFragment;
import com.pro.bityard.fragment.my.FundsPassFrogetFragment;
import com.pro.bityard.fragment.my.InviteFragment;
import com.pro.bityard.fragment.my.LanguageFragment;
import com.pro.bityard.fragment.my.LoginPassChangeFragment;
import com.pro.bityard.fragment.my.MobileBindChangeFragment;
import com.pro.bityard.fragment.my.PersonFragment;
import com.pro.bityard.fragment.my.QuickFragment;
import com.pro.bityard.fragment.my.SafeCenterFragment;
import com.pro.bityard.fragment.my.SetUpFragment;
import com.pro.bityard.fragment.my.ThemeFragment;
import com.pro.bityard.fragment.my.TradeSettingsFragment;
import com.pro.bityard.fragment.my.WithdrawalAddressFragment;
import com.pro.bityard.fragment.my.WithdrawalFragment;
import com.pro.bityard.fragment.tab.HoldFragment;
import com.pro.bityard.fragment.trade.SpotTradeRecordFragment;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class UserActivity extends BaseActivity {
    private static final String TYPE = "USER_TYPE";
    private static final String VALUE = "VALUE";
    private static final String DATA_VALUE = "DATA_VALUE";
    private static final String DATA_VALUE_EDIT = "DATA_VALUE_EDIT";

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

    public static void enter(Context context, String type, String value) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(VALUE, value);
        context.startActivity(intent);
    }

    public static void enter(Context context, String type, Object value) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(DATA_VALUE, (Serializable) value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);

        String value = intent.getStringExtra(VALUE);


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
            case IntentConfig.Keys.KEY_EXCHANGE_RATE:
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
            case IntentConfig.Keys.KEY_SAFE_CENTER_FUNDS_FORGET_PASS:
                addFundsPassForgetFragment();
                break;
            case IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_EMAIL:
                addBindChangeEmailFragment();
                break;
            case IntentConfig.Keys.KEY_FUND_STATEMENT:
                addFundStatementFragment();
                break;
            case IntentConfig.Keys.KEY_TRADE_HISTORY:
                addTradeHistoryFragment();
                break;
            case IntentConfig.Keys.KEY_INVITE_HISTORY:
                addInviteFragment();
                break;
            case IntentConfig.Keys.KEY_TRADE_SETTINGS:
                addTradeSettingsFragment();
                break;
            case IntentConfig.Keys.KEY_WITHDRAWAL_ADDRESS:
                addWithdrawalAddressFragment();
                break;
            case IntentConfig.Keys.KEY_ADD_ADDRESS:
                addAddAddressFragment();
                break;
            case IntentConfig.Keys.KEY_ANNOUNCEMENT:
                addAnnouncementFragment();
                break;
            case IntentConfig.Keys.KEY_ACCOUNT:
                addAccountFragment();
                break;
            case IntentConfig.Keys.KEY_WITHDRAWAL:
                addWithdrawalFragment();
                break;
            case IntentConfig.Keys.KEY_QUICK_EXCHANGE:
                addQuickExchangeFragment();
                break;
            case IntentConfig.Keys.KEY_HOLD:
                addHoldFragment();
                break;
            case IntentConfig.Keys.RULE:
                addRuleFragment(value);
                break;
            case IntentConfig.Keys.KEY_PERSON_INFORMATION:
                addPersonInformationFragment();
                break;
            case IntentConfig.Keys.KEY_CIRCLE_SEARCH_NICKNAME:
                addSearchNicknameFragment();
                break;
            case IntentConfig.Keys.KEY_CIRCLE_SETTINGS_FOLLOW:
                FollowerDetailEntity.DataBean dataBean = (FollowerDetailEntity.DataBean) intent.getSerializableExtra(DATA_VALUE);

                addSettingsFollowFragment(dataBean);
                break;
            case IntentConfig.Keys.KEY_CIRCLE_FOLLOWER_LIST:
                addFollowerListFragment();
                break;
            case IntentConfig.Keys.KEY_FOLLOW_SETTINGS:
                addFollowMangerFragment();
                break;
            case IntentConfig.Keys.KEY_CIRCLE_EDIT_FOLLOW:
                FollowerDetailEntity.DataBean dataBean2 = (FollowerDetailEntity.DataBean) intent.getSerializableExtra(DATA_VALUE);
                addFollowEditFragment(dataBean2);
                break;
            case IntentConfig.Keys.KEY_FOLLOW_LOG:
                addFollowLogFragment();
                break;

            case IntentConfig.Keys.KEY_FOLLOWER_MANGER:
                addFollowerMangerFragment();
                break;

            case IntentConfig.Keys.KEY_SPOT_RECORD:
                addSpotTradeFragment();
                break;
            case IntentConfig.Keys.KEY_SYS:
                addIdeaSelectFragment();
                break;
        }


    }

    private void addIdeaSelectFragment() {
        String name = IdeaSelectFragment.class.getSimpleName();
        //带单理念
        IdeaSelectFragment fragment = new IdeaSelectFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }


    private void addSpotTradeFragment() {
        String name = SpotTradeRecordFragment.class.getSimpleName();
        //安全中心
        SpotTradeRecordFragment fragment = new SpotTradeRecordFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFollowerMangerFragment() {
        String name = FollowerMangerFragment.class.getSimpleName();
        FollowerMangerFragment fragment = new FollowerMangerFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFollowLogFragment() {
        String name = FollowLogFragment.class.getSimpleName();
        FollowLogFragment fragment = new FollowLogFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFollowEditFragment(FollowerDetailEntity.DataBean dataBean) {
        String name = FollowEditFragment.class.getSimpleName();
        FollowEditFragment fragment = new FollowEditFragment().newInstance(dataBean);
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFollowMangerFragment() {
        String name = FollowMangerFragment.class.getSimpleName();
        FollowMangerFragment fragment = new FollowMangerFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addSettingsFollowFragment(FollowerDetailEntity.DataBean dataBean) {
        String name = FollowSettingsFragment.class.getSimpleName();
        FollowSettingsFragment fragment = new FollowSettingsFragment().newInstance(dataBean);
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFollowerListFragment() {
        String name = FollowerListFragment.class.getSimpleName();
        FollowerListFragment fragment = new FollowerListFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addSearchNicknameFragment() {
        String name = SearchNicknameFragment.class.getSimpleName();
        //持仓
        SearchNicknameFragment fragment = new SearchNicknameFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addPersonInformationFragment() {
        String name = PersonFragment.class.getSimpleName();
        //持仓
        PersonFragment fragment = new PersonFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFundsPassForgetFragment() {
        String name = FundsPassFrogetFragment.class.getSimpleName();
        //持仓
        FundsPassFrogetFragment fragment = new FundsPassFrogetFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addRuleFragment(String value) {
        String name = RuleFragment.class.getSimpleName();
        //持仓
        RuleFragment fragment = new RuleFragment().newInstance(value);
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addHoldFragment() {
        String name = HoldFragment.class.getSimpleName();
        //持仓
        HoldFragment fragment = new HoldFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addQuickExchangeFragment() {
        String name = QuickFragment.class.getSimpleName();
        //币币闪兑
        QuickFragment fragment = new QuickFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addWithdrawalFragment() {
        String name = WithdrawalFragment.class.getSimpleName();
        //安全中心
        WithdrawalFragment fragment = new WithdrawalFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addAccountFragment() {
        String name = AccountFragment.class.getSimpleName();
        //安全中心
        AccountFragment fragment = new AccountFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addAnnouncementFragment() {
        String name = AnnouncementFragment.class.getSimpleName();
        //安全中心
        AnnouncementFragment fragment = new AnnouncementFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addAddAddressFragment() {
        String name = AddAddressFragment.class.getSimpleName();
        //安全中心
        AddAddressFragment fragment = new AddAddressFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addWithdrawalAddressFragment() {
        String name = WithdrawalAddressFragment.class.getSimpleName();
        //安全中心
        WithdrawalAddressFragment fragment = new WithdrawalAddressFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addTradeSettingsFragment() {
        String name = TradeSettingsFragment.class.getSimpleName();
        //安全中心
        TradeSettingsFragment fragment = new TradeSettingsFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addInviteFragment() {
        String name = InviteFragment.class.getSimpleName();
        //安全中心
        InviteFragment fragment = new InviteFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addTradeHistoryFragment() {
        String name = ContractFollowRecordFragment.class.getSimpleName();
        //安全中心
        ContractFollowRecordFragment fragment = new ContractFollowRecordFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
    }

    private void addFundStatementFragment() {
        String name = FundStatementFragment.class.getSimpleName();
        //安全中心
        FundStatementFragment fragment = new FundStatementFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_fragment_containter, fragment, name);
        ft.addToBackStack(name);
        ft.commitAllowingStateLoss();
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
