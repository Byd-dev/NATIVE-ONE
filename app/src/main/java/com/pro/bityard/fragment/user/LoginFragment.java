package com.pro.bityard.fragment.user;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.RegisterActivity;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.view.NoScrollViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.text_right)
    TextView text_right;
    @BindView(R.id.view_line)
    View view_line;


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
        //text_title.setText(R.string.text_login_title);
        //text_right.setVisibility(View.VISIBLE);
       // text_right.setText(R.string.text_register);
        view_line.setVisibility(View.GONE);

        view.findViewById(R.id.img_back).setOnClickListener(this);
        text_right.setOnClickListener(this);

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
            case R.id.text_right:
                RegisterActivity.enter(getContext(), IntentConfig.Keys.KEY_REGISTER);
                break;

        }
    }
}
