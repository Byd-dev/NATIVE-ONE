package com.pro.bityard.fragment.user;

import android.graphics.Paint;
import android.os.Bundle;
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
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.PositionRealManger;
import com.pro.bityard.manger.PositionSimulationManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class EmailLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;


    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;

    @BindView(R.id.text_err)
    TextView text_err;

    @BindView(R.id.text_forget_pass)
    TextView text_forget_pass;
    private int count_pass = 0;

    private String geetestToken = null;


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

        view.findViewById(R.id.text_forget_pass).setOnClickListener(this);

        img_eye.setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);


        //检测错误提示是否显示
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count_pass >= 1 && s.length() != 0) {
                    text_err.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        String text_email = SPUtils.getString(AppConfig.USER_EMAIL);
        if (!text_email.equals("")) {
            edit_account.setText(text_email);
        }


    }


    private int isHide = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_mobile_login:
                viewPager.setCurrentItem(1);
                break;

            case R.id.img_eye:
                if (isHide == 0) {
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isHide = 1;
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                } else if (isHide == 1) {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isHide = 0;
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));


                }
                break;
            case R.id.btn_login:


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
                        geetestToken = result;
                    }

                    @Override
                    public void onSuccessResult(String result) {
                        ArrayMap<String, String> map = new ArrayMap<>();

                        map.put("vHash", Util.Random32());
                        map.put("username", account_value);
                        map.put("password", pass_value);
                        map.put("geetestToken", geetestToken);

                        NetManger.getInstance().postRequest("/api/sso/user_login_check",map, new OnNetResult() {
                            @Override
                            public void onNetResult(String state, Object response) {
                                if (state.equals(BUSY)) {
                                    showProgressDialog();
                                } else if (state.equals(SUCCESS)) {
                                    Log.d("print", "onNetResult:196:  " + response.toString());

                                    dismissProgressDialog();
                                    LoginEntity loginEntity = new Gson().fromJson(response.toString(), LoginEntity.class);
                                    if (loginEntity.getCode() == 200) {
                                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                                        getActivity().finish();
                                        SPUtils.putString(AppConfig.USER_EMAIL, edit_account.getText().toString());


                                        //登录成功 初始化
                                      TagManger.getInstance().tag();


                                    } else if (loginEntity.getCode() == 401) {
                                        count_pass++;
                                        text_err.setVisibility(View.VISIBLE);
                                    } else if (loginEntity.getCode() == 500) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
                                    }
                                } else if (state.equals(FAILURE)) {
                                    dismissProgressDialog();
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
                ForgetActivity.enter(getContext(), IntentConfig.Keys.KEY_FORGET,0);
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
