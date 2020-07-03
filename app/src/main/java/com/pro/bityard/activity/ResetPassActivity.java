package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class ResetPassActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.edit_pass_sure)
    EditText edit_pass_sure;

    @BindView(R.id.edit_pass_new)
    EditText edit_pass_new;


    @BindView(R.id.btn_submit)
    Button btn_submit;


    @BindView(R.id.img_eye_sure)
    ImageView img_eye_sure;

    @BindView(R.id.img_eye_new)
    ImageView img_eye_new;
    private String token;

    @BindView(R.id.layout_pass_new)
    LinearLayout layout_pass_new;
    @BindView(R.id.text_err_pass_new)
    TextView text_err_pass_new;
    @BindView(R.id.layout_pass_sure)
    LinearLayout layout_pass_sure;
    @BindView(R.id.text_err_pass_sure)
    TextView text_err_pass_sure;

    public static void enter(Context context, String token) {
        Intent intent = new Intent(context, ResetPassActivity.class);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }


    @Override
    protected void initView(View view) {

        StatusBarUtil.setStatusBarDarkTheme(this, false);

        edit_pass_sure.setHint(getResources().getString(R.string.text_edit_pass_new_sure));
        btn_submit.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.text_right).setOnClickListener(this);
        img_eye_sure.setOnClickListener(this);
        img_eye_new.setOnClickListener(this);

        Util.isPassEffective(edit_pass_new, response -> {
            if (response.toString().equals("1")) {
                text_err_pass_new.setVisibility(View.GONE);
                layout_pass_new.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPass(edit_pass_sure.getText().toString())) {
                    btn_submit.setEnabled(true);

                } else {
                    btn_submit.setEnabled(false);

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

                if (Util.isPass(edit_pass_new.getText().toString())
                ) {
                    btn_submit.setEnabled(true);

                } else {
                    btn_submit.setEnabled(false);

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
            case R.id.img_eye_sure:
                Util.setEye(this, edit_pass_sure, img_eye_sure);
                break;
            case R.id.img_eye_new:
                Util.setEye(this, edit_pass_new, img_eye_new);


                break;


            case R.id.btn_submit:

                String pass_new_value = edit_pass_new.getText().toString();

                if (pass_new_value.equals("")) {
                    Toast.makeText(ResetPassActivity.this, getResources().getString(R.string.text_edit_pass_new), Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass_confirm_value = edit_pass_sure.getText().toString();

                if (pass_confirm_value.equals("")) {
                    Toast.makeText(ResetPassActivity.this, getResources().getString(R.string.text_edit_pass_new_sure), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass_new_value.equals(pass_confirm_value)) {
                    text_err_pass_sure.setVisibility(View.VISIBLE);
                    text_err_pass_sure.setText("两次输入的密码不一致");

                } else {
                    text_err_pass_sure.setVisibility(View.GONE);

                }

                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("token", token);
                map.put("newPassword", pass_new_value);
                map.put("cfmPassword", pass_confirm_value);

                NetManger.getInstance().postRequest("/api/forgot/reset-password", map, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                            if (tipEntity.getCode() == 200) {
                                Toast.makeText(ResetPassActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else if (state.equals(FAILURE)) {
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
