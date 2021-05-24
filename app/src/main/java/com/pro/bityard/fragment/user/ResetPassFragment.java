package com.pro.bityard.fragment.user;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.CountryCodeEntity;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class ResetPassFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.edit_pass)
    EditText edit_password;

    @BindView(R.id.edit_pass_new)
    EditText edit_password_new;

    private ViewPager viewPager;
    private CountryCodeEntity countryCodeEntity;





    @BindView(R.id.img_eye)
    ImageView img_eye;

    @BindView(R.id.img_eye_new)
    ImageView img_eye_new;







    public ResetPassFragment() {

    }


    public ResetPassFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }




    @Override
    protected void initView(View view) {

        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        img_eye.setOnClickListener(this);
        img_eye_new.setOnClickListener(this);





    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_reset_pass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {




    }

    private boolean eye = true;
    private boolean eyeNew = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_eye:
                if (eye) {
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open_black));
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
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open_black));
                    eyeNew = false;
                } else  {
                    edit_password_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eyeNew = true;
                }
                break;





            case R.id.btn_submit:
                Log.d("print", "onClick:175:  "+"点击了去确定");

                break;

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
