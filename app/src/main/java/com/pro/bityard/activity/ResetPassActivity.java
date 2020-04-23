package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class ResetPassActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.edit_pass)
    EditText edit_password;

    @BindView(R.id.edit_pass_new)
    EditText edit_password_new;



    @BindView(R.id.img_eye)
    ImageView img_eye;

    @BindView(R.id.img_eye_new)
    ImageView img_eye_new;
    private String token;


    public static void enter(Context context, String token) {
        Intent intent = new Intent(context, ResetPassActivity.class);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }


    @Override
    protected void initView(View view) {

        StatusBarUtil.setStatusBarDarkTheme(this, false);


        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.text_right).setOnClickListener(this);
        img_eye.setOnClickListener(this);
        img_eye_new.setOnClickListener(this);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("token");

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_reset_pass;
    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected void initData() {


    }

    @Override
    protected void initEvent() {

    }

    private boolean eye = true;
    private boolean eyeNew = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.img_eye_new:
                if (eyeNew) {
                    edit_password_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eyeNew = false;
                } else  {
                    edit_password_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eyeNew = true;
                }
                break;



            case R.id.btn_submit:

                String pass_new_value = edit_password_new.getText().toString();

                if (pass_new_value.equals("")){
                    Toast.makeText(ResetPassActivity.this,getResources().getString(R.string.text_edit_pass_new),Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass_confirm_value = edit_password.getText().toString();

                if (pass_confirm_value.equals("")){
                    Toast.makeText(ResetPassActivity.this,getResources().getString(R.string.text_edit_pass_new_sure),Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("token", token);
                map.put("newPassword",pass_new_value);
                map.put("cfmPassword",pass_confirm_value);

                NetManger.getInstance().postRequest("/api/forgot/reset-password", map, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)){
                            showProgressDialog();
                        }else if (state.equals(SUCCESS)){
                            dismissProgressDialog();
                            TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                            if (tipEntity.getCode()==200){
                                Toast.makeText(ResetPassActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else if (state.equals(FAILURE)){
                            dismissProgressDialog();
                        }
                    }
                });

                break;
            case R.id.img_back:
            case R.id.text_right:
                finish();
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
