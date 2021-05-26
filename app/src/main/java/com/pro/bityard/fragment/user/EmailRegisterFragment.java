package com.pro.bityard.fragment.user;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.MainActivity;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailRegisterFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;

    @BindView(R.id.layout_view)
    ScrollView layout_view;
    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;
    @BindView(R.id.edit_code)
    EditText edit_code;

    @BindView(R.id.text_getCode)
    TextView text_getCode;

    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.layout_pass)
    LinearLayout layout_pass;
    @BindView(R.id.layout_code)
    LinearLayout layout_code;

    @BindView(R.id.text_err_pass)
    TextView text_err_pass;
    @BindView(R.id.text_err_email)
    TextView text_err_email;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.edit_kode)
    EditText edit_kode;

    @BindView(R.id.line_code)
    View line_code;
    @BindView(R.id.line_code_err)
    View line_code_err;
    @BindView(R.id.line_email)
    View line_email;
    @BindView(R.id.line_email_err)
    View line_email_err;

    @BindView(R.id.line_pass)
    View line_pass;
    @BindView(R.id.line_pass_err)
    View line_pass_err;

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
        btn_submit.setOnClickListener(this);

        img_eye.setOnClickListener(this);

        text_getCode.setOnClickListener(this);


        //邮箱输入框焦点的监听
        Util.isEmailEffective(edit_account, response -> {
            if (response.toString().equals("1")) {
                text_err_email.setVisibility(View.GONE);
                line_email.setVisibility(View.VISIBLE);
                line_email_err.setVisibility(View.GONE);
                text_getCode.setEnabled(true);
                if (Util.isCode(edit_code.getText().toString()) && Util.isPass(edit_password.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_email.setVisibility(View.VISIBLE);
                line_email.setVisibility(View.GONE);
                line_email_err.setVisibility(View.VISIBLE);
                text_getCode.setEnabled(false);
                btn_submit.setEnabled(false);

            } else if (response.toString().equals("-1")) {
                text_err_email.setVisibility(View.GONE);
                line_email.setVisibility(View.VISIBLE);
                line_email_err.setVisibility(View.GONE);
                text_getCode.setEnabled(false);
                btn_submit.setEnabled(false);

            }
        });
        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && Util.isCode(s.toString()) && Util.isEmail(edit_account.getText().toString())
                        && Util.isPass(edit_password.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //检测错误提示是否显示
        Util.isPassEffective(edit_password, response -> {
            if (response.toString().equals("1")) {
                text_err_pass.setVisibility(View.GONE);
                line_pass.setVisibility(View.VISIBLE);
                line_pass_err.setVisibility(View.GONE);
                if (Util.isEmail(edit_account.getText().toString()) && Util.isCode(edit_code.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_pass.setVisibility(View.VISIBLE);
                line_pass.setVisibility(View.GONE);
                line_pass_err.setVisibility(View.VISIBLE);
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass.setVisibility(View.GONE);
                line_pass.setVisibility(View.VISIBLE);
                line_pass_err.setVisibility(View.GONE);
                btn_submit.setEnabled(false);
            }

        });
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
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open_black));
                    eye = false;
                } else {
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


                NetManger.getInstance().getEmailCode(getActivity(),layout_view,account_value, "REGISTER", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();

                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.sendEmptyMessage(0);
                            Message msg = new Message();
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                break;

            case R.id.btn_submit:


                String value_kode = edit_kode.getText().toString();

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


                HashMap<String, String> map2 = new HashMap<>();
                map2.put("email", account_value);
                map2.put("password", pass_value);
                map2.put("ru",value_kode);

                String value_sign = Util.getSign(map2, AppConfig.SIGN_KEY);


                Log.d("print", "onClick:加密后:  " + value_sign);
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("email", account_value);
                map.put("password", URLEncoder.encode(pass_value));
                map.put("sign", value_sign);

                NetManger.getInstance().checkEmailCode(account_value, "REGISTER", code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        Log.d("print", "onClick:验证验证码: " + tipEntity);
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck()) {
                            //成功了再注册
                            register(account_value, pass_value, value_sign,value_kode);


                        } else {
                            if (tipEntity.getMessage().equals("")) {
                                Toast.makeText(getContext(), R.string.text_correct_email_code, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                break;

        }
    }


    private void register(String account_value, String pass_value, String value_sign,String value_kode) {


        NetManger.getInstance().register(true,null,null,account_value, pass_value, value_sign,value_kode, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getCode() == 200) {
                    //注册成功登录
                    login(account_value, pass_value,"undefined");
                }

                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_register_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });


    }

    private void login(String account_value, String pass_value, String geetestToken) {
        NetManger.getInstance().login(account_value, pass_value,true, Util.Random32(), geetestToken, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                LoginEntity loginEntity = (LoginEntity) response;
                SPUtils.putString(AppConfig.USER_EMAIL, account_value);
                NetManger.getInstance().userDetail((state2, response2) -> {
                    if (state2.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state2.equals(SUCCESS)) {
                        dismissProgressDialog();
                        UserDetailEntity userDetailEntity = (UserDetailEntity) response2;
                        loginEntity.getUser().setUserName(userDetailEntity.getUser().getUsername());
                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                        //登录成功 初始化
                        TagManger.getInstance().tag();
                        getActivity().finish();
                        MainActivity.enter(getActivity(), MainActivity.TAB_TYPE.TAB_HOME);
                        SPUtils.putString(AppConfig.POP_LOGIN,"pop_login");
                    } else if (state2.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });

            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
                if (response != null) {
                    LoginEntity loginEntity = (LoginEntity) response;
                    Toast.makeText(getContext(), loginEntity.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                SmsTimeUtils.check(SmsTimeUtils.EMAIL_REGISTER, false);
                SmsTimeUtils.startCountdown(text_getCode);
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
