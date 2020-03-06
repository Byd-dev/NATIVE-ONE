package com.pro.bityard.fragment.user;

import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;

import androidx.viewpager.widget.ViewPager;

public class MobileLoginFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;


    public MobileLoginFragment(ViewPager viewPager){
        this.viewPager=viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_email_login).setOnClickListener(this);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_login;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_email_login:
                viewPager.setCurrentItem(0);
                break;
        }
    }
}
