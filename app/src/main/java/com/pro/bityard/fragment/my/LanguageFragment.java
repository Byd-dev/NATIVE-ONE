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
import com.pro.bityard.utils.Util;
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


        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE);
        if (language.equals("")) {

        } else {
            if (language.equals("en")) {
                img_one.setBackgroundResource(R.mipmap.icon_check_true);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals("zh_simple")) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals("zh_traditional")) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_true);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals("ja")) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_true);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals("ko")) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_true);
            }
        }

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

                Util.switchLanguage(getContext(), "en");
                getActivity().finish();


                break;
            case R.id.layout_two:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);

                Util.switchLanguage(getContext(), "zh_simple");
                getActivity().finish();


                break;
            case R.id.layout_three:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_true);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);

                Util.switchLanguage(getContext(), "zh_traditional");
                getActivity().finish();


                break;
            case R.id.layout_four:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_true);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);

                Util.switchLanguage(getContext(), "ja");
                getActivity().finish();


                break;
            case R.id.layout_five:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_true);

                Util.switchLanguage(getContext(), "ko");

                getActivity().finish();


                break;
        }
    }


    private void finish() {
        getActivity().finish();

        Intent it = new Intent(getActivity(), MainActivity.class);

        //清空任务栈确保当前打开activit为前台任务栈栈顶

        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(it);
    }
}
