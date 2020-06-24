package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
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

import java.net.URLEncoder;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class LoginPassChangeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.edit_pass_old)
    EditText edit_pass_old;
    @BindView(R.id.edit_pass_new)
    EditText edit_pass_new;
    @BindView(R.id.edit_pass_sure)
    EditText edit_pass_sure;
    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.edit_code_mobile)
    EditText edit_code_mobile;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    private String email;
    private String phone;
    private String googleToken;

    @BindView(R.id.text_getCode)
    TextView text_getCode;
    private String account;

    @BindView(R.id.text_email_mobile)
    TextView text_email_mobile;

    @BindView(R.id.img_eye_old)
    ImageView img_eye_old;
    @BindView(R.id.img_eye_new)
    ImageView img_eye_new;
    @BindView(R.id.img_eye_sure)
    ImageView img_eye_sure;


    @BindView(R.id.layout_pass_old)
    LinearLayout layout_pass_old;
    @BindView(R.id.text_err_pass)
    TextView text_err_pass;

    @BindView(R.id.layout_pass_new)
    LinearLayout layout_pass_new;
    @BindView(R.id.text_err_pass_new)
    TextView text_err_pass_new;

    @BindView(R.id.layout_pass_sure)
    LinearLayout layout_pass_sure;
    @BindView(R.id.text_err_pass_sure)
    TextView text_err_pass_sure;
    @BindView(R.id.text_confirm)
    TextView text_confirm;
    private String value;

    @BindView(R.id.layout_mobile)
    LinearLayout layout_mobile;
    @BindView(R.id.layout_code)
    LinearLayout layout_code;
    @BindView(R.id.text_getCode_mobile)
    TextView text_getCode_mobile;
    private boolean isEmail = true;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_change_login_pass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_change_login_pass));


        view.findViewById(R.id.img_back).setOnClickListener(this);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        text_getCode.setOnClickListener(this);
        text_getCode_mobile.setOnClickListener(this);

        img_eye_old.setOnClickListener(this);
        img_eye_new.setOnClickListener(this);
        img_eye_sure.setOnClickListener(this);
        text_confirm.setOnClickListener(this);


        //旧密码
        Util.isPassEffective(edit_pass_old, response -> {
            if (response.toString().equals("1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass_old.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));


                if (Util.isPass(edit_pass_new.getText().toString()) && Util.isPass(edit_pass_sure.getText().toString())
                        && Util.isPass(edit_pass_old.getText().toString())) {
                    text_getCode.setEnabled(true);
                    text_getCode_mobile.setEnabled(true);
                    if (isEmail){
                        if (Util.isPass(edit_pass_new.getText().toString()) && Util.isPass(edit_pass_sure.getText().toString())
                                && Util.isCode(edit_code.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }else {
                        if (Util.isPass(edit_pass_new.getText().toString()) && Util.isPass(edit_pass_sure.getText().toString())
                                && Util.isCode(edit_code_mobile.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }

                } else {
                    text_getCode.setEnabled(false);
                    text_getCode_mobile.setEnabled(false);

                }

            } else if (response.toString().equals("0")) {
                text_err_pass.setVisibility(View.VISIBLE);
                layout_pass_old.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass_old.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }

        });
        //新密码
        Util.isPassEffective(edit_pass_new, response -> {
            if (response.toString().equals("1")) {
                text_err_pass_new.setVisibility(View.GONE);
                layout_pass_new.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPass(edit_pass_sure.getText().toString())
                        && Util.isPass(edit_pass_old.getText().toString())) {
                    text_getCode.setEnabled(true);
                    text_getCode_mobile.setEnabled(true);
                    if (isEmail){
                        if (Util.isPass(edit_pass_old.getText().toString()) && Util.isPass(edit_pass_sure.getText().toString())
                                && Util.isCode(edit_code.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);

                        }
                    }else {
                        if (Util.isPass(edit_pass_new.getText().toString()) && Util.isPass(edit_pass_sure.getText().toString())
                                && Util.isCode(edit_code_mobile.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }

                } else {
                    text_getCode.setEnabled(false);
                    text_getCode_mobile.setEnabled(false);

                }
            } else if (response.toString().equals("0")) {
                text_err_pass_new.setVisibility(View.VISIBLE);
                layout_pass_new.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass_new.setVisibility(View.GONE);
                layout_pass_new.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }

        });
        //确认新密码
        Util.isPassEffective(edit_pass_sure, response -> {
            if (response.toString().equals("1")) {
                text_err_pass_sure.setVisibility(View.GONE);
                layout_pass_sure.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));

                if (Util.isPass(edit_pass_new.getText().toString()) && Util.isPass(edit_pass_old.getText().toString())) {
                    text_getCode.setEnabled(true);
                    text_getCode_mobile.setEnabled(true);
                    if (isEmail){
                        if (Util.isPass(edit_pass_old.getText().toString()) && Util.isPass(edit_pass_new.getText().toString())
                                && Util.isCode(edit_code.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }else {
                        if (Util.isPass(edit_pass_old.getText().toString()) && Util.isPass(edit_pass_new.getText().toString())
                                && Util.isCode(edit_code_mobile.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }

                } else {
                    text_getCode.setEnabled(false);
                    text_getCode_mobile.setEnabled(false);

                }
            } else if (response.toString().equals("0")) {
                text_err_pass_sure.setVisibility(View.VISIBLE);
                layout_pass_sure.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass_sure.setVisibility(View.GONE);
                layout_pass_sure.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }

        });

        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && Util.isCode(s.toString())
                        && Util.isPass(edit_pass_old.getText().toString())
                        && Util.isPass(edit_pass_new.getText().toString())
                        && Util.isPass(edit_pass_sure.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit_code_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && Util.isCode(s.toString())
                        && Util.isPass(edit_pass_old.getText().toString())
                        && Util.isPass(edit_pass_new.getText().toString())
                        && Util.isPass(edit_pass_sure.getText().toString())) {
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


    @Override
    protected void intPresenter() {

    }

    @Override
    public void onResume() {
        super.onResume();
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);

        if (data != null) {
            account = data.getUser().getPrincipal();
            email = data.getUser().getEmail();
            phone = data.getUser().getPhone();
        }

        if (account.contains("@")) {
            isEmail = true;
            layout_code.setVisibility(View.VISIBLE);
            layout_mobile.setVisibility(View.GONE);
            text_confirm.setText(getResources().getText(R.string.text_verify_by_mobile) + " ->");

        } else {
            isEmail = false;
            layout_code.setVisibility(View.GONE);
            layout_mobile.setVisibility(View.VISIBLE);
            text_confirm.setText(getString(R.string.text_verify_by_email) + " ->");
        }
        if (email.equals("")) {
            text_confirm.setVisibility(View.GONE);
        } else if (phone.equals("")) {
            text_confirm.setVisibility(View.GONE);

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {


    }

    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SmsTimeUtils.check(SmsTimeUtils.LOGIN_PASS_CHANGE_EMAIL, false);
                    SmsTimeUtils.startCountdown(text_getCode);
                    break;
                case 1:
                    SmsTimeUtils.check(SmsTimeUtils.LOGIN_PASS_CHANGE_MOBILE, false);
                    SmsTimeUtils.startCountdown(text_getCode_mobile);
                    break;
                default:
                    break;
            }
        }

    };

    private boolean eyeOld = true;
    private boolean eyeNew = true;
    private boolean eyeSure = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_eye_old:
                Util.setEye(getActivity(), edit_pass_old, img_eye_old);

                break;
            case R.id.img_eye_new:
                Util.setEye(getActivity(), edit_pass_new, img_eye_new);

                break;
            case R.id.img_eye_sure:
                Util.setEye(getActivity(), edit_pass_sure, img_eye_sure);

                break;
            case R.id.text_getCode:
                NetManger.getInstance().getEmailCode(email, "CHANGE_PASSWORD", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            googleToken = (String) response1;
                            mHandler.obtainMessage(0).sendToTarget();
                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });

            /*    if (isEmail) {
                    NetManger.getInstance().getEmailCode(email, "CHANGE_PASSWORD", (state, response1, response2) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response2;
                            if (tipEntity.getCode() == 200) {
                                googleToken = (String) response1;
                                mHandler.obtainMessage(0).sendToTarget();
                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                } else {
                    NetManger.getInstance().getMobileCode(phone, "CHANGE_PASSWORD", (state, response1, response2) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response2;
                            if (tipEntity.getCode() == 200) {
                                googleToken = (String) response1;
                                mHandler.obtainMessage(1).sendToTarget();

                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

                }*/


                break;
            case R.id.text_getCode_mobile:
                NetManger.getInstance().getMobileCode(phone, "CHANGE_PASSWORD", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            googleToken = (String) response1;
                            mHandler.obtainMessage(1).sendToTarget();

                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;
            case R.id.btn_submit:
                String value_pass_old = edit_pass_old.getText().toString();
                String value_pass_new = edit_pass_new.getText().toString();
                String value_pass_sure = edit_pass_sure.getText().toString();
                String value_code = edit_code.getText().toString();
                String value_code_mobile = edit_code_mobile.getText().toString();

                if (value_pass_old.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_old_pass_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value_pass_new.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_edit_pass_new), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value_pass_sure.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_edit_pass_new_sure), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEmail){
                    if (value_code.equals("")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    if (value_code_mobile.equals("")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.text_mobile_code_input), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                if (!value_pass_new.equals(value_pass_sure)) {
                    Toast.makeText(getActivity(), R.string.text_pass_different, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmail) {
                    NetManger.getInstance().checkEmailCode(email, "CHANGE_PASSWORD", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.getCode() == 200) {
                                changePass(email, URLEncoder.encode(value_pass_old), URLEncoder.encode(value_pass_new));
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

                } else {
                    NetManger.getInstance().checkMobileCode(phone, "CHANGE_PASSWORD", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.getCode() == 200) {
                                changePass(phone, URLEncoder.encode(value_pass_old), URLEncoder.encode(value_pass_new));
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

                }


                break;

            case R.id.text_confirm:
                if (isEmail) {
                    layout_code.setVisibility(View.GONE);
                    layout_mobile.setVisibility(View.VISIBLE);
                    text_confirm.setText(getString(R.string.text_verify_by_email) + " ->");
                    isEmail = false;
                } else {
                    layout_code.setVisibility(View.VISIBLE);
                    layout_mobile.setVisibility(View.GONE);
                    text_confirm.setText(getResources().getText(R.string.text_verify_by_mobile) + " ->");
                    isEmail = true;
                }


                break;


        }
    }

    private void changePass(String account, String value_pass_old, String value_pass_new) {
        NetManger.getInstance().passwordChange(account, value_pass_old, value_pass_new, googleToken, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getCode() == 200) {
                    SPUtils.remove(AppConfig.LOGIN);
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                    getActivity().finish();
                }
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }

                dismissProgressDialog();
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();

    }
}
