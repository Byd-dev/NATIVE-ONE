package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;
import skin.support.SkinCompatManager;

public class ThemeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_one)
    ImageView img_one;

    @BindView(R.id.img_two)
    ImageView img_two;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.img_back).setOnClickListener(this);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_theme;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        boolean theme = SPUtils.getBoolean(AppConfig.KEY_THEME, true);
        if (theme==true){
            img_one.setBackgroundResource(R.mipmap.icon_check_true);
            img_two.setBackgroundResource(R.mipmap.icon_check_false);
        }else {
            img_one.setBackgroundResource(R.mipmap.icon_check_false);
            img_two.setBackgroundResource(R.mipmap.icon_check_true);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;

            case R.id.layout_one:
                SkinCompatManager.getInstance().restoreDefaultTheme();
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
                SPUtils.putBoolean(AppConfig.KEY_THEME, true);

                img_one.setBackgroundResource(R.mipmap.icon_check_true);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                break;
            case R.id.layout_two:

                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
                SPUtils.putBoolean(AppConfig.KEY_THEME, false);
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                break;
        }
    }
}
