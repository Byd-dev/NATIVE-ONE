package com.pro.bityard.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ResetPassActivity;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
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
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;

    @BindView(R.id.edit_code)
    EditText edit_code;

    @BindView(R.id.text_getCode)
    TextView text_getCode;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    private String geetestToken = null;

    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.layout_code)
    LinearLayout layout_code;

    @BindView(R.id.text_err_email)
    TextView text_err_email;
    @BindView(R.id.text_err_code)
    TextView text_err_code;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //?????????G3util
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


        edit_account.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edit_account.clearFocus();
            }
            return false;
        });

        edit_code.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edit_code.clearFocus();
            }
            return false;
        });
        //??????????????????????????????
        Util.isEmailEffective(edit_account, response -> {
            if (response.toString().equals("1")) {
                text_err_email.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                text_getCode.setEnabled(true);
            } else if (response.toString().equals("0")) {
                text_err_email.setVisibility(View.VISIBLE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                text_getCode.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_email.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                text_getCode.setEnabled(false);
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

                NetManger.getInstance().getEmailCode(getActivity(),layout_view,account_value, "FORGOT_PASSWORD", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.obtainMessage(0).sendToTarget();
                        } else {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                break;

            case R.id.btn_submit:
                //1  ??????  2 ???????????????  3 ??????????????? 4 ???????????? 5?????????

                String code_value = edit_code.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                } else if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                //1???????????????
                //  checkCode(account_value, code_value);

                NetManger.getInstance().checkEmailCode(account_value, "FORGOT_PASSWORD", code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                            //2 ????????????
                            checkAccount(account_value);
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


    /*3????????????*/
    private void checkAccount(String account_value) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account_value);

        NetManger.getInstance().postRequest("/api/forgot/account-verify", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200 && tipEntity.isVerify_email() == true) {
                    //3 ????????????
                    checkSafe(account_value);


                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    /*4????????????*/
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
                        Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });
    }


    /*???????????????*/
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SmsTimeUtils.check(SmsTimeUtils.EMAIL_FORGET, false);
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
