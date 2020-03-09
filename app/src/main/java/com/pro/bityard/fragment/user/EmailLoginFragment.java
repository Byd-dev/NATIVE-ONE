package com.pro.bityard.fragment.user;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.pro.bityard.R;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.HttpUtils;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.utils.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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

    private String geetestToken = null;
    private String keyHash;

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

        img_eye.setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);


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





                String strRand = "";
                for (int i = 0; i < 32; i++) {
                    strRand += String.valueOf((int) (Math.random() * 10));
                }

                keyHash = strRand;

                Log.d("print", "onClick: "+keyHash);

                String account_value = edit_account.getText().toString();
                String pass_value = edit_password.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_email_input), Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_pass), Toast.LENGTH_SHORT).show();
                    return;
                }


                Gt3Util.getInstance().customVerity(new OnGtUtilResult() {

                    @Override
                    public void onApi1Result(String result) {
                        geetestToken = result;
                    }

                    @Override
                    public void onSuccessResult(String result) {
                        HashMap<String,String> map=new HashMap<>();

                        map.put("vHash",keyHash);
                        map.put("username",keyHash);
                        map.put("username",account_value);
                        map.put("password",pass_value);
                        map.put("geetestToken",geetestToken);



                        NetManger.getInstance().login(map,keyHash, null, account_value, pass_value, geetestToken, new OnNetResult() {
                            @Override
                            public void onNetResult(String state, Object response) {
                                if (state.equals(BUSY)) {
                                    showProgressDialog();
                                } else if (state.equals(SUCCESS)) {
                                    dismissProgressDialog();
                                    Log.d("print", "onNetResult:116:登录返回:  " + response.toString());
                                } else if (state.equals(FAILURE)) {
                                    dismissProgressDialog();
                                }
                            }
                        });


                    }

                    @Override
                    public void onFailedResult(GT3ErrorBean gt3ErrorBean) {

                    }
                });


                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
