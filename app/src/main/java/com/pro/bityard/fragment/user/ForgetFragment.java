package com.pro.bityard.fragment.user;

import android.view.View;

import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.view.NoScrollViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class ForgetFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_register;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        initViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.text_right).setOnClickListener(this);

    }


    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new EmailForgetFragment(viewPager));
        myPagerAdapter.addFragment(new MobileForgetFragment(viewPager));
        myPagerAdapter.addFragment(new ResetPassFragment(viewPager));
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
            case R.id.text_right:
                getActivity().finish();
                break;
        }
    }
}
