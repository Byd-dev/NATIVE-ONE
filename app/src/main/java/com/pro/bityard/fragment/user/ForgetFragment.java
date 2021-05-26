package com.pro.bityard.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.view.NoScrollViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class ForgetFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    private int pageIndex;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.view_line)
    View view_line;


    public ForgetFragment newInstance(int pageIndex) {
        ForgetFragment forgetFragment = new ForgetFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageIndex);
        forgetFragment.setArguments(args);
        return forgetFragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt("page");
        }

    }

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
        viewPager.setCurrentItem(pageIndex);
        viewPager.setOffscreenPageLimit(3);

        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.text_right).setOnClickListener(this);

        view_line.setVisibility(View.GONE);
        text_title.setText(R.string.text_forget);

    }


    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new EmailForgetFragment(viewPager));
        myPagerAdapter.addFragment(new MobileForgetFragment(viewPager));
        // myPagerAdapter.addFragment(new ResetPassFragment(viewPager));
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
