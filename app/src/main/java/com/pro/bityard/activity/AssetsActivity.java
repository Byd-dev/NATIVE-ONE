package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.FollowerIncomeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.CircleImageView;
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class AssetsActivity extends BaseActivity implements View.OnClickListener {
    private LoginEntity loginEntity;

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
        Util.setTheme(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
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
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
