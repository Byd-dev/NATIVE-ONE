package com.pro.bityard.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnNetTwoResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.SmsTimeUtils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailRegisterFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;


    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;
    @BindView(R.id.edit_code)
    EditText edit_code;

    @BindView(R.id.text_getCode)
    TextView text_getCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    public EmailRegisterFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_mobile_login).setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);

        img_eye.setOnClickListener(this);

        text_getCode.setOnClickListener(this);


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_email_register;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }



    private String geetestToken = null;

    private boolean eye = true;

    @Override
    public void onClick(View v) {
        String account_value = edit_account.getText().toString();
        switch (v.getId()) {
            case R.id.text_mobile_login:
                viewPager.setCurrentItem(1);
                break;

            case R.id.img_eye:
                if (eye) {
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eye = false;
                } else  {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eye = true;
                }
                break;
            case R.id.text_getCode:
                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                }

             //   getCode( account_value);

                NetManger.getInstance().getEmailCode(account_value, "REGISTER", (state, response1, response2) -> {
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

            case R.id.btn_login:

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                }

                String code_value = edit_code.getText().toString();
                if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass_value = edit_password.getText().toString();
                if (pass_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_pass), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayMap<String, String> map = new ArrayMap<>();

                map.put("email", account_value);
                map.put("password", pass_value);


              //  checkCode(account_value, code_value, map);

                NetManger.getInstance().checkEmailCode(account_value, "REGISTER", code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                            //成功了再注册
                            register(map);
                        } else {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });



                break;

        }
    }

    /*校验验证码*/
    private void checkCode(String account_value, String code_value, ArrayMap<String, String> register_map) {
        ArrayMap<String, String> map = new ArrayMap<>();

        map.put("account", account_value);
        map.put("type", "REGISTER");
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
                        //成功了再注册
                        Log.d("print", "onNetResult: 238: " + tipEntity);
                        register(register_map);
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_code_lose), Toast.LENGTH_SHORT).show();

                    }

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    private void register(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/register/submit",map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    Log.d("print", "onNetResult:176: "+response.toString());
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200) {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_register_success), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    /*获取验证码*/
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
                map.put("type", "REGISTER");
                map.put("geetestToken", geetestToken);
                NetManger.getInstance().postRequest("/api/system/sendEmail",map, new OnNetResult() {
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
                Toast.makeText(getContext(),gt3ErrorBean.errorDesc,Toast.LENGTH_SHORT).show();

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
