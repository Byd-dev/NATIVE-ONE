package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class LoginPassChangeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.edit_pass_old)
    EditText edit_pass_old;
    @BindView(R.id.edit_pass_new)
    EditText edit_pass_new;
    @BindView(R.id.edit_pass_sure)
    EditText edit_pass_sure;
    @BindView(R.id.edit_code)
    EditText edit_code;
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

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.img_back).setOnClickListener(this);

        view.findViewById(R.id.btn_submit).setOnClickListener(this);

        text_getCode.setOnClickListener(this);

        img_eye_old.setOnClickListener(this);
        img_eye_new.setOnClickListener(this);
        img_eye_sure.setOnClickListener(this);


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_change_login_pass;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (data != null) {
            account = data.getUser().getAccount();
            email = data.getUser().getEmail();
            phone = data.getUser().getPhone();
        }
        if (account.contains("@")) {
            text_email_mobile.setText(getResources().getString(R.string.text_email_code));
            edit_code.setHint(getResources().getString(R.string.text_email_code_input));
        } else {
            text_email_mobile.setText(getResources().getString(R.string.text_mobile_code));
            edit_code.setHint(getResources().getString(R.string.text_mobile_code_input));

        }

    }

    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

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

    private boolean eyeOld=true;
    private boolean eyeNew=true;
    private boolean eyeSure=true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_eye_old:
                if (eyeOld) {
                    edit_pass_old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye_old.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eyeOld = false;
                } else  {
                    edit_pass_old.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye_old.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eyeOld = true;
                }
                break;
            case R.id.img_eye_new:
                if (eyeNew) {
                    edit_pass_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eyeNew = false;
                } else  {
                    edit_pass_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eyeNew = true;
                }
                break;
            case R.id.img_eye_sure:
                if (eyeSure) {
                    edit_pass_sure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye_sure.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eyeSure = false;
                } else  {
                    edit_pass_sure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye_sure.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eyeSure = true;
                }
                break;
            case R.id.text_getCode:

                if (account.contains("@")) {
                    NetManger.getInstance().getEmailCode(email, "CHANGE_PASSWORD", (state, response1, response2) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response2;
                            if (tipEntity.getCode() == 200) {
                                googleToken = (String) response1;
                                mHandler.sendEmptyMessage(0);
                                Message msg = new Message();
                                mHandler.sendMessage(msg);
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
                                mHandler.sendEmptyMessage(0);
                                Message msg = new Message();
                                mHandler.sendMessage(msg);
                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }


                break;

            case R.id.btn_submit:
                String value_pass_old = edit_pass_old.getText().toString();
                String value_pass_new = edit_pass_new.getText().toString();
                String value_pass_sure = edit_pass_sure.getText().toString();
                String value_code = edit_code.getText().toString();

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

                if (value_code.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!value_pass_new.equals(value_pass_sure)) {
                    Toast.makeText(getActivity(), R.string.text_pass_different, Toast.LENGTH_SHORT).show();
                    return;
                }


                if (account.equals("@")) {
                    NetManger.getInstance().checkEmailCode(account, "CHANGE_PASSWORD", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.getCode() == 200) {
                                changePass(account, value_pass_old, value_pass_new);
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                } else {
                    NetManger.getInstance().checkMobileCode(account, "CHANGE_PASSWORD", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.getCode() == 200) {
                                changePass(account, value_pass_old, value_pass_new);
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
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
                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

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
