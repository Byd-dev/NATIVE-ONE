package com.pro.bityard.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.view.AlphaChangeListener;
import com.pro.bityard.view.MyScrollView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.bar)
    RelativeLayout layout_bar;

    @BindView(R.id.scrollView)
    MyScrollView myScrollView;


    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_home;
    }


    @Override
    protected void onLazyLoad() {


    }

    @Override
    protected void initView(View view) {


        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


        myScrollView.setAlphaChangeListener(new AlphaChangeListener() {
            @Override
            public void alphaChanging(float alpha) {
                layout_bar.setAlpha(1 - alpha);
            }
        });


    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


    }


}





