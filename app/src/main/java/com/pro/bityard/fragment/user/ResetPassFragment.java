package com.pro.bityard.fragment.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ForgetActivity;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

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

    private int isHide = 0;
    private int isHideNew = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.img_eye_new:
                if (isHideNew == 0) {
                    edit_password_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isHideNew = 1;
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                } else if (isHide == 1) {
                    edit_password_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isHideNew = 0;
                    img_eye_new.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));

                }
                break;




            case R.id.btn_submit:
                String token = getToken();
                Log.d("print", "initData:130:  "+token);

                break;

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
