package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowerIncomeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.CircleImageView;
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class AssetsActivity extends BaseActivity implements View.OnClickListener {
    private LoginEntity loginEntity;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.text_userName)
    TextView text_userName;

    @BindView(R.id.img_head)
    CircleImageView img_head;
    @BindView(R.id.text_uid)
    TextView text_uid;
    @BindView(R.id.text_register)
    TextView text_register;
    @BindView(R.id.img_edit)
    ImageView img_edit;

    @BindView(R.id.text_byd_balance)
    TextView text_byd_balance;
    @BindView(R.id.layout_commissionRate)
    LinearLayout layout_commissionRate;
    @BindView(R.id.text_all_profit)
    TextView text_all_profit;
    @BindView(R.id.text_commissionRate)
    TextView text_commissionRate;
    
    
    
    @Override
    protected int setContentLayout() {
        return R.layout.activity_assets;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setTheme(this);

        if (isLogin()) {
            loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            text_userName.setText(loginEntity.getUser().getUserName());
            text_uid.setVisibility(View.VISIBLE);
            text_uid.setText("UID:" + loginEntity.getUser().getUserId());
            text_register.setVisibility(View.GONE);
            img_edit.setVisibility(View.VISIBLE);
            Glide.with(this).load(loginEntity.getUser().getAvatar()).error(R.mipmap.icon_my_bityard).into(img_head);
            layout_commissionRate.setVisibility(View.VISIBLE);
            getFollowIncome();

        } else {


            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setVisibility(View.GONE);
            text_register.setVisibility(View.VISIBLE);
            img_edit.setVisibility(View.GONE);
            text_byd_balance.setText(getResources().getString(R.string.text_default));
            text_commissionRate.setText(getResources().getString(R.string.text_default));
            Glide.with(this).load(R.mipmap.icon_my_bityard).into(img_head);
            layout_commissionRate.setVisibility(View.GONE);
            text_all_profit.setText(getResources().getString(R.string.text_all_profit) + ": 0.0");

        }

    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, AssetsActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_my_setting).setOnClickListener(this);
        findViewById(R.id.img_my_service).setOnClickListener(this);

        findViewById(R.id.layout_hold).setOnClickListener(this);//我的持仓
        findViewById(R.id.layout_one).setOnClickListener(this);//安全中心监听
        findViewById(R.id.layout_two).setOnClickListener(this);
        findViewById(R.id.layout_three).setOnClickListener(this);
        findViewById(R.id.layout_four).setOnClickListener(this);
        findViewById(R.id.layout_spot_record).setOnClickListener(this);
        //带单管理
        findViewById(R.id.layout_copied_manger).setOnClickListener(this);
        findViewById(R.id.layout_copy_manger).setOnClickListener(this);


        findViewById(R.id.layout_five).setOnClickListener(this);
        findViewById(R.id.layout_six).setOnClickListener(this);
        findViewById(R.id.layout_seven).setOnClickListener(this);
        findViewById(R.id.layout_help).setOnClickListener(this);//系统设置

        findViewById(R.id.layout_nine).setOnClickListener(this);
        findViewById(R.id.layout_login).setOnClickListener(this);
        findViewById(R.id.text_register).setOnClickListener(this);
        findViewById(R.id.img_edit).setOnClickListener(this);
        findViewById(R.id.text_mining).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getFollowIncome() {
        NetManger.getInstance().followerIncome((state, response) -> {
            if (state.equals(SUCCESS)) {
                FollowerIncomeEntity followerIncomeEntity = (FollowerIncomeEntity) response;
                text_all_profit.setText(getResources().getString(R.string.text_all_profit)
                        + ": " + TradeUtil.getNumberFormat(followerIncomeEntity.getIncomeAll(), 2));

            }
        });
    }

    @Override
    public void onClick(View v) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, AppConfig.ZH_SIMPLE);

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_help:
                WebActivity.getInstance().openUrl(this, NetManger.H5_HELP_CENTER, getString(R.string.text_help_center));
                break;
            case R.id.img_my_setting:
                UserActivity.enter(this, IntentConfig.Keys.KEY_SET_UP);
                break;
            case R.id.layout_hold:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_HOLD);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            //安全中心
            case R.id.layout_one:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_SAFE_CENTER);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            //资金记录
            case R.id.img_record:
            case R.id.layout_two:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.layout_spot_record:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_SPOT_RECORD);

                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*交易记录*/
            case R.id.layout_three:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_TRADE_HISTORY);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*邀请记录*/
            case R.id.layout_four:

                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_INVITE_HISTORY);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;

            /*带单管理*/

            case R.id.layout_copied_manger:

                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FOLLOWER_MANGER);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*跟单管理*/
            case R.id.layout_copy_manger:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_FOLLOW_SETTINGS);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*交易设置*/
            case R.id.layout_five:
                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_TRADE_SETTINGS);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*提币地址管理*/
            case R.id.layout_six:

                if (isLogin()) {
                    UserActivity.enter(this, IntentConfig.Keys.KEY_WITHDRAWAL_ADDRESS);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*客服系统*/

            case R.id.img_my_service:

                String url;
                if (isLogin()) {
                    url = String.format(NetManger.SERVICE_URL, language, loginEntity.getUser().getUserId(), loginEntity.getUser().getAccount());
                } else {
                    url = String.format(NetManger.SERVICE_URL, language, "", "游客");
                }
                WebActivity.getInstance().openUrl(this, url, getResources().getString(R.string.text_my_service));

                break;
            /*前往挖矿*/
            case R.id.layout_mining:
            case R.id.text_mining:
                if (isLogin()) {
                    WebActivity.getInstance().openUrl(this, NetManger.getInstance().h5Url(loginEntity.getAccess_token(), null, "/mining"), getString(R.string.text_mining_title));

                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            /*修改昵称*/
            case R.id.img_edit:
                if (isLogin()) {
                    Util.lightOff(this);
                    PopUtil.getInstance().showEdit(this, layout_view, true, result -> {
                        loginEntity.getUser().setUserName(result.toString());
                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                        onResume();
                    });
                }

                break;
            case R.id.text_register:
                RegisterActivity.enter(this, IntentConfig.Keys.KEY_REGISTER);
                break;
            case R.id.layout_login:

                if (isLogin()) {
                    PersonActivity.enter(this);
                } else {
                    LoginActivity.enter(this, IntentConfig.Keys.KEY_LOGIN);
                }
                break;
        }
    }
}
