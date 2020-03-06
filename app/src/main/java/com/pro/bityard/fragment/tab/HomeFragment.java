package com.pro.bityard.fragment.tab;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.view.AlphaChangeListener;
import com.pro.bityard.view.MyScrollView;
import com.pro.bityard.viewutil.StatusBarUtil;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import skin.support.SkinCompatManager;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.bar)
    RelativeLayout layout_bar;

    @BindView(R.id.text_login)
    TextView text_login;

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


        view.findViewById(R.id.img_icon1).setOnClickListener(this);
        view.findViewById(R.id.img_icon2).setOnClickListener(this);
        text_login.setOnClickListener(this);

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
            case R.id.img_icon1:
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);

                break;

            case R.id.img_icon2:
                SkinCompatManager.getInstance().restoreDefaultTheme();
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);

                break;

            case R.id.text_login:


                UserActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);


                break;
        }
    }
}





