package com.pro.bityard.fragment.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ForgetActivity;
import com.pro.bityard.activity.ResetPassActivity;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailForgetFragment extends BaseFragment implements View.OnClickListener {


    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;

    @BindView(R.id.edit_code)
    EditText edit_code;

    @BindView(R.id.text_getCode)
    TextView text_getCode;


    private String geetestToken = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    public EmailForgetFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_mobile_forget).setOnClickListener(this);


        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        text_getCode.setOnClickListener(this);


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_email_forget;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        LoginEntity.UserBean data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.UserBean.class);
        if (data != null) {
            edit_account.setText(data.getEmail());
        }

    }


    @Override
    public void onClick(View v) {
        String account_value = edit_account.getText().toString();

        switch (v.getId()) {
            case R.id.text_mobile_forget:
                viewPager.setCurrentItem(1);
                break;
            case R.id.text_getCode:
                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                }

               // getCode(account_value);

                NetManger.getInstance().getEmailCode(account_value, "FORGOT_PASSWORD", (state, response1, response2) -> {
                    if (state.equals(BUSY)){
                        showProgressDialog();
                    }else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.sendEmptyMessage(0);
                            Message msg = new Message();
                            mHandler.sendMessage(msg);
                        }
                    }else if (state.equals(FAILURE)){
                        dismissProgressDialog();
                    }
                });



                break;

            case R.id.btn_submit:
                //1  人机  2 发送验证码  3 验证验证码 4 验证账号 5改密码

                String code_value = edit_code.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                } else if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                //1验证验证码
                checkCode(account_value, code_value);


                break;


        }
    }

    /*1获取验证码*/
    private void getCode(String account_value) {


        Gt3Util.getInstance().customVerity(new OnGtUtilResult() {

            @Override
            public void onApi1Result(String result) {
                geetestToken = result;

            }

            @Override
            public void onSuccessResult(String result) {
                ArrayMap<String, String> map = new ArrayMap<>();

                map.put("account", account_value);
                map.put("type", "FORGOT_PASSWORD");
                map.put("geetestToken", geetestToken);
                NetManger.getInstance().postRequest("/api/system/sendEmail", map, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                            if (tipEntity.getCode() == 200) {
                                mHandler.sendEmptyMessage(0);
                                Message msg = new Message();
                                mHandler.sendMessage(msg);
                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                Toast.makeText(getContext(), gt3ErrorBean.errorDesc, Toast.LENGTH_SHORT).show();

            }
        });

    }

    /*2校验验证码*/
    private void checkCode(String account_value, String code_value) {
        ArrayMap<String, String> map = new ArrayMap<>();

        map.put("account", account_value);
        map.put("type", "FORGOT_PASSWORD");
        map.put("code", code_value);

        NetManger.getInstance().postRequest("/api/system/checkEmail", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                        //2 验证账号
                        checkAccount(account_value);

                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_code_lose), Toast.LENGTH_SHORT).show();

                    }

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    /*3账号验证*/
    private void checkAccount(String account_value) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account_value);

        NetManger.getInstance().postRequest("/api/forgot/account-verify", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200 && tipEntity.isVerify_email() == true) {
                        //3 安全验证
                        checkSafe(account_value);


                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    /*4安全验证*/
    private void checkSafe(String account_value) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account_value);
        NetManger.getInstance().postRequest("/api/forgot/securify-verify", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200) {
                        String token = tipEntity.getToken();
                        ResetPassActivity.enter(getContext(), token);
                        getActivity().finish();


                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });
    }


    /*获取倒计时*/
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, false);
                    SmsTimeUtils.startCountdown(text_getCode);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
