package com.pro.bityard.fragment.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ForgetActivity;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.net.URLEncoder;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;
    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.layout_pass)
    LinearLayout layout_pass;


    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.text_err_pass)
    TextView text_err_pass;
    @BindView(R.id.text_err_email)
    TextView text_err_email;

    @BindView(R.id.text_forget_pass)
    TextView text_forget_pass;

    private String geetestToken = null;

    public EmailLoginFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    public EmailLoginFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        view.findViewById(R.id.text_mobile_login).setOnClickListener(this);

        text_forget_pass.setText(getResources().getText(R.string.text_forget_pass) + "?");

        view.findViewById(R.id.text_forget_pass).setOnClickListener(this);

        img_eye.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        edit_account.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edit_account.clearFocus();
            }
            return false;
        });

        edit_password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edit_password.clearFocus();
            }
            return false;
        });

        //邮箱输入框焦点的监听
        Util.isEmailEffective(edit_account, response -> {
            if (response.toString().equals("1")) {
                text_err_email.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPass(edit_password.getText().toString()) && edit_password.getText().toString().length() > 5) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_email.setVisibility(View.VISIBLE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_email.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }
        });


        //检测错误提示是否显示
        Util.isPassEffective(edit_password, response -> {
            if (response.toString().equals("1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isEmail(edit_account.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_pass.setVisibility(View.VISIBLE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }

        });


        /*edit_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edit_password.getText().toString().length() != 0) {
                    if (Util.isPass(edit_password.getText().toString()) && edit_password.getText().toString().length() > 5) {
                        text_err_pass.setVisibility(View.GONE);
                        layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                        if (Util.isEmail(edit_account.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    } else {
                        text_err_pass.setVisibility(View.VISIBLE);
                        layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                        btn_submit.setEnabled(false);

                    }
                } else {
                    text_err_pass.setVisibility(View.GONE);
                    layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                    btn_submit.setEnabled(false);

                }
            }
        });*/

        //忘记密码下划线
        text_forget_pass.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text_forget_pass.getPaint().setAntiAlias(true);//抗锯齿


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_email_login;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        String text_email = SPUtils.getString(AppConfig.USER_EMAIL, null);
        if (text_email != null) {
            edit_account.setText(text_email);
        }

        //Util.setTwoUnClick(edit_account, edit_password, btn_submit);
        // Util.setTwoUnClick(edit_password, edit_account, btn_submit);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_mobile_login:
                viewPager.setCurrentItem(1);
                break;

            case R.id.img_eye:

                Util.setEye(getActivity(), edit_password, img_eye);

                break;
            case R.id.btn_submit:


                String account_value = edit_account.getText().toString();
                String pass_value = edit_password.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_pass), Toast.LENGTH_SHORT).show();
                    return;
                }

                //注册需要人机验证
                Gt3Util.getInstance().customVerity(new OnGtUtilResult() {

                    @Override
                    public void onApi1Result(String result) {
                        String[] split = result.split(",");
                        geetestToken = split[0];
                    }

                    @Override
                    public void onSuccessResult(String result) {
                        ArrayMap<String, String> map = new ArrayMap<>();

                        map.put("vHash", Util.Random32());
                        map.put("username", account_value);
                        map.put("password", URLEncoder.encode(pass_value));
                        map.put("geetestToken", geetestToken);
                        map.put("terminal", "Android");

                        NetManger.getInstance().login(account_value, pass_value, geetestToken, (state, response) -> {
                            if (state.equals(BUSY)) {
                                showProgressDialog();
                            } else if (state.equals(SUCCESS)) {
                                Log.d("print", "onNetResult:196:  " + response.toString());
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
                                    } else if (state2.equals(FAILURE)) {
                                        dismissProgressDialog();
                                    }
                                });

                            } else if (state.equals(FAILURE)) {
                                dismissProgressDialog();
                                if (response!=null){
                                    LoginEntity loginEntity = (LoginEntity) response;
                                    Toast.makeText(getContext(), loginEntity.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                        Toast.makeText(getContext(), gt3ErrorBean.errorDesc, Toast.LENGTH_SHORT).show();

                    }
                });


                break;

            case R.id.text_forget_pass:
                ForgetActivity.enter(getContext(), IntentConfig.Keys.KEY_FORGET, 0);
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
