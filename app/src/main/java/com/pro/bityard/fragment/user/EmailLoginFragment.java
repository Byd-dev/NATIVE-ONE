package com.pro.bityard.fragment.user;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class EmailLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_eye)
    ImageView img_eye;


    private  ViewPager viewPager;


    @BindView(R.id.edit_pass)
    EditText edit_password;


    public EmailLoginFragment(ViewPager viewPager){
        this.viewPager=viewPager;
    }



    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_mobile_login).setOnClickListener(this);

        img_eye.setOnClickListener(this);

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
        switch (v.getId()){
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
        }
    }
}
