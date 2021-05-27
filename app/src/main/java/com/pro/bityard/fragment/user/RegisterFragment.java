package com.pro.bityard.fragment.user;

import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.view.NoScrollViewPager;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {

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
        return R.layout.fragment_register;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        initViewPager(viewPager);

        view.findViewById(R.id.img_back).setOnClickListener(this);
        text_right.setOnClickListener(this);
       // text_title.setText(R.string.text_register);
      //  text_right.setVisibility(View.VISIBLE);
       // text_right.setText(R.string.text_login);
        view_line.setVisibility(View.GONE);

    }


    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new EmailRegisterFragment(viewPager));
        myPagerAdapter.addFragment(new MobileRegisterFragment(viewPager));
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
            /*case R.id.text_right:*/
                getActivity().finish();
                break;
        }
    }
}
