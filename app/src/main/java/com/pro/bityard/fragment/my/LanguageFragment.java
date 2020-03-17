package com.pro.bityard.fragment.my;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.activity.MainActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.Locale;

import butterknife.BindView;
import skin.support.SkinCompatManager;

public class LanguageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_one)
    ImageView img_one;

    @BindView(R.id.img_two)
    ImageView img_two;
    @BindView(R.id.img_three)
    ImageView img_three;

    @BindView(R.id.img_four)
    ImageView img_four;
    @BindView(R.id.img_five)
    ImageView img_five;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.layout_three).setOnClickListener(this);
        view.findViewById(R.id.layout_four).setOnClickListener(this);
        view.findViewById(R.id.layout_five).setOnClickListener(this);
        view.findViewById(R.id.img_back).setOnClickListener(this);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_language;
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

            case R.id.layout_one:


                img_one.setBackgroundResource(R.mipmap.icon_check_true);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                SPUtils.putString(AppConfig.KEY_LANGUAGE, "en");
              //  switchLanguage("en");
                break;
            case R.id.layout_two:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                SPUtils.putString(AppConfig.KEY_LANGUAGE, "zh_simple");
              //  switchLanguage("zh_simple");
                break;
            case R.id.layout_three:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_true);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                SPUtils.putString(AppConfig.KEY_LANGUAGE, "zh_traditional");
               // switchLanguage("zh_traditional");

                break;
            case R.id.layout_four:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_true);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                SPUtils.putString(AppConfig.KEY_LANGUAGE, "ja");
              //  switchLanguage("ja");
                break;
            case R.id.layout_five:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_true);
                SPUtils.putString(AppConfig.KEY_LANGUAGE, "ko");
               // switchLanguage("ko");
                break;
        }
    }

    private void switchLanguage(String language) {

        //设置应用语言类型

        Resources resources = getResources();

        Configuration config = resources.getConfiguration();

        DisplayMetrics dm = resources.getDisplayMetrics();

        if (language.equals("zh_simple")) {

            config.locale = Locale.SIMPLIFIED_CHINESE;

        } else if (language.equals("en")) {

            config.locale = Locale.ENGLISH;

        } else if (language.equals("zh_traditional")) {

            config.locale = Locale.TRADITIONAL_CHINESE;

        } else if (language.equals("ja")) {
            config.locale = Locale.JAPANESE;

        } else if (language.equals("ko")) {
            config.locale = Locale.KOREAN;

        }

        resources.updateConfiguration(config, dm);

        //更新语言后，destroy当前页面，重新绘制

        getActivity().finish();

        Intent it = new Intent(getActivity(), MainActivity.class);

        //清空任务栈确保当前打开activit为前台任务栈栈顶

        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(it);

    }
}
