package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailBindChangeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.edit_account)
    EditText edit_account;

    @BindView(R.id.edit_code)
    EditText edit_code;



    @BindView(R.id.layout_email)
    LinearLayout layout_email;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.text_title)
    TextView text_title;





    @BindView(R.id.text_getCode)
    TextView text_getCode;
    private String googleToken;
    private LoginEntity loginEntity;
    private String account_value;
    private String sendType;
    private boolean email;
    private String account;

    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.text_err_email)
    TextView text_err_email;

    public EmailBindChangeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {


        btn_submit.setOnClickListener(this);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        text_getCode.setOnClickListener(this);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_email_bind;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        Log.d("print", "onResume:136:  " + loginEntity);
        if (loginEntity != null) {
            LoginEntity.UserBean user = loginEntity.getUser();

            if (user.getEmail().equals("")) {
                text_title.setText(R.string.text_bind_email);
                layout_email.setVisibility(View.VISIBLE);
                btn_submit.setText(R.string.text_sure);
                //邮箱输入框焦点的监听
                Util.isEmailEffective(edit_account, response -> {
                    if (response.toString().equals("1")) {
                        text_err_email.setVisibility(View.GONE);
                        layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                        text_getCode.setEnabled(true);
                        if (Util.isCode(edit_code.getText().toString())){
                            btn_submit.setEnabled(true);
                        }else {
                            btn_submit.setEnabled(false);
                        }
                    } else if (response.toString().equals("0")) {
                        text_err_email.setVisibility(View.VISIBLE);
                        layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                        text_getCode.setEnabled(false);
                        btn_submit.setEnabled(false);

                    } else if (response.toString().equals("-1")) {
                        text_err_email.setVisibility(View.GONE);
                        layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
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
                        if (s.length() > 4 && Util.isCode(s.toString()) && Util.isEmail(edit_account.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            } else {
                email = SPUtils.getBoolean(AppConfig.CHANGE_EMAIL, true);
                if (email == true) {
                    text_title.setText(R.string.text_change_email);
                    layout_email.setVisibility(View.GONE);
                    btn_submit.setText(R.string.text_next);
                    text_getCode.setEnabled(true);
                    edit_code.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() > 4 && Util.isCode(s.toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                } else {
                    text_title.setText(R.string.text_change_email);
                    layout_email.setVisibility(View.VISIBLE);
                    btn_submit.setText(R.string.text_sure);
                    //手机号码输入框焦点的监听
                    Util.isEmailEffective(edit_account, response -> {
                        if (response.toString().equals("1")) {
                            text_err_email.setVisibility(View.GONE);
                            layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                            text_getCode.setEnabled(true);
                            if (Util.isCode(edit_code.getText().toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                            }
                        } else if (response.toString().equals("0")) {
                            text_err_email.setVisibility(View.VISIBLE);
                            layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                            text_getCode.setEnabled(false);
                            btn_submit.setEnabled(false);

                        } else if (response.toString().equals("-1")) {
                            text_err_email.setVisibility(View.GONE);
                            layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
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
                            if (s.length() > 4 && Util.isCode(s.toString()) && Util.isEmail(edit_account.getText().toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }


            }


        }

    }

    @Override
    protected void initData() {






    }


    @Override
    public void onClick(View v) {
        account_value = edit_account.getText().toString();


        switch (v.getId()) {

            case R.id.img_back:
                getActivity().finish();
                break;

            case R.id.text_getCode:
                /*1首先判断是否绑定手机 没有的话就绑定手机
                 * 2再判断当前是设置手机号码 还是修改手机号码
                 * 3 设置手机号码就直接设置 修改是下一步*/
                // 旧手机验证的用CHANGE_EMAIL，新手机验证是用BIND_EMAIL
                if (loginEntity.getUser().getEmail().equals("")) {
                    sendType = "BIND_EMAIL";
                    account =  account_value;
                    if (account_value.equals("")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (email == true) {
                        account = loginEntity.getUser().getEmail();
                        sendType = "CHANGE_EMAIL";

                    } else {
                        sendType = "BIND_EMAIL";

                        if (account_value.equals("")) {
                            Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        account =   account_value;
                    }
                }

                Log.d("print", "onClick:245: " + email + "----" + account);
                NetManger.getInstance().getEmailCode(getActivity(),layout_view,account, sendType, (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        String[] split = response1.toString().split(",");
                        googleToken = split[0];
                        if (tipEntity.getCode() == 200) {
                            if (sendType.equals("BIND_EMAIL")){
                                mHandler.obtainMessage(0).sendToTarget();
                            }else if (sendType.equals("CHANGE_EMAIL")){
                                mHandler.obtainMessage(1).sendToTarget();
                            }
                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                break;

            case R.id.btn_submit:


                if (loginEntity.getUser().getEmail().equals("")) {
                    sendType = "BIND_EMAIL";
                    if (account_value.equals("")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (email == true) {
                        sendType = "CHANGE_EMAIL";
                        account = loginEntity.getUser().getEmail();
                    } else {
                        sendType = "BIND_EMAIL";
                        account =   account_value;

                    }

                }


                String code_value = edit_code.getText().toString();
                if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }




                Log.d("print", "onClick:310: " + email + "----" + account);

                NetManger.getInstance().checkEmailCode(account, sendType, code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                            Log.d("print", "onClick:319:验证验证码:  " + tipEntity);
                            if (tipEntity.getMessage().equals("")) {

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            if (loginEntity.getUser().getEmail().equals("")) {
                                //成功了再绑定
                                ArrayMap<String, String> map = new ArrayMap<>();
                                map.put("email",   account_value);
                                map.put("googleToken", googleToken);
                                bindEmail(map);
                            } else {
                                if (email == true) {
                                    Log.d("print", "onClick:314:  " + response + "    -----    " + email);
                                    SPUtils.putBoolean(AppConfig.CHANGE_EMAIL, false);
                                    getActivity().finish();
                                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_EMAIL);
                                } else {
                                    SPUtils.putBoolean(AppConfig.CHANGE_EMAIL, true);
                                    ArrayMap<String, String> map = new ArrayMap<>();
                                    map.put("oldEmail", loginEntity.getUser().getEmail());
                                    map.put("newEmail",   account_value);
                                    map.put("googleToken", googleToken);
                                    updateEmail(map);
                                }
                            }
                        } else {
                            if (tipEntity.getMessage().equals("")) {
                                Toast.makeText(getActivity(), R.string.text_correct_mobile_code, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;
        }
    }


    private void bindEmail(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/user/bind-email", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                Log.d("print", "bindMobile:284:绑定手机号:  " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    loginEntity.getUser().setEmail(account);
                    SPUtils.putData(AppConfig.LOGIN, loginEntity);
                    getActivity().finish();
                }
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), R.string.text_success_bind, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    private void updateEmail(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/user/update-email", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                Log.d("print", "bindMobile:397:修改手机号:  " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    loginEntity.getUser().setEmail(account);
                    SPUtils.putData(AppConfig.LOGIN, loginEntity);
                    getActivity().finish();


                }
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), R.string.text_change_success, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SmsTimeUtils.check(SmsTimeUtils.EMAIL_BIND, false);
                    SmsTimeUtils.startCountdown(text_getCode);
                    break;
                case 1:
                    SmsTimeUtils.check(SmsTimeUtils.EMAIL_CHANGE, false);
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
