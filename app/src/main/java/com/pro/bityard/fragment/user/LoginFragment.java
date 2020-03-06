package com.pro.bityard.fragment.user;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.view.NoScrollViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;




    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_login;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        initViewPager(viewPager);

        view.findViewById(R.id.img_back).setOnClickListener(this);

    }


    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new EmailLoginFragment(viewPager));
        myPagerAdapter.addFragment(new MobileLoginFragment(viewPager));
        viewPager.setAdapter(myPagerAdapter);
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;

        }
    }
}
