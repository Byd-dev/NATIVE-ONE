package com.pro.bityard.fragment.user;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.base.BaseFragment;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class EmailLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;


    private ViewPager viewPager;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;

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

                Gt3Util.getInstance().customVerity(new OnGtUtilResult() {
                    @Override
                    public void onGtResult(String result) {
                        Log.d("print", "onGtResult:119:  "+result);

                    }
                });





             /*   HashSet integerHashSet = new HashSet();
                Random random = new Random();
                int randoms = random.nextInt(1000);
                if (!integerHashSet.contains(randoms)) {
                    integerHashSet.add(randoms);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssss");
                    String hash = sdf.format(new Date()) + String.valueOf(randoms);//唯一哈希码
                    Log.d("print", "onClick: " + hash);
                    String account_value = edit_account.getText().toString();
                    String pass_value = edit_password.getText().toString();
                    NetManger.getInstance().login(hash, null, account_value, pass_value, null, new OnNetResult() {
                        @Override
                        public void onNetResult(String state, Object response) {
                            if (state.equals(BUSY)){
                                showProgressDialog();
                            }else if (state.equals(SUCCESS)){
                                dismissProgressDialog();
                                Log.d("print", "onNetResult:116:登录返回:  "+response.toString());
                            }else if (state.equals(FAILURE)){
                                dismissProgressDialog();
                            }
                        }
                    });


                }*/
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
