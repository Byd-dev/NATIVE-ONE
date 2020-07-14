package com.pro.bityard.fragment.my;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.activity.MainOneActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

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
    @BindView(R.id.img_six)
    ImageView img_six;
    @BindView(R.id.img_seven)
    ImageView img_seven;
    @BindView(R.id.img_eight)
    ImageView img_eight;

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
        view.findViewById(R.id.layout_six).setOnClickListener(this);
        view.findViewById(R.id.layout_seven).setOnClickListener(this);
        view.findViewById(R.id.layout_eight).setOnClickListener(this);
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


        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, "null");
        Log.d("print", "initData:70:  "+language);
        if (language.equals(AppConfig.KEY_LANGUAGE)) {
            img_one.setBackgroundResource(R.mipmap.icon_check_false);
            img_two.setBackgroundResource(R.mipmap.icon_check_false);
            img_three.setBackgroundResource(R.mipmap.icon_check_false);
            img_four.setBackgroundResource(R.mipmap.icon_check_false);
            img_five.setBackgroundResource(R.mipmap.icon_check_false);
            img_six.setBackgroundResource(R.mipmap.icon_check_false);
            img_seven.setBackgroundResource(R.mipmap.icon_check_false);
            img_eight.setBackgroundResource(R.mipmap.icon_check_false);
        } else {
            if (language.equals(Util.ZH_SIMPLE)) {
                img_one.setBackgroundResource(R.mipmap.icon_check_true);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals(Util.ZH_TRADITIONAL)) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals(Util.EN_US)) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_true);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals(Util.VI_VN)) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_true);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            } else if (language.equals(Util.RU_RU)) {
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_true);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            }else if (language.equals(Util.KO_KR)){
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_true);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            }else if (language.equals(Util.IN_ID)){
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_true);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
            }else if (language.equals(Util.JA_JP)){
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_true);
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
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.ZH_SIMPLE);
                finish();


                break;
            case R.id.layout_two:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_true);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.ZH_TRADITIONAL);
                finish();


                break;
            case R.id.layout_three:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_true);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.EN_US);
                finish();


                break;
            case R.id.layout_four:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_true);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.VI_VN);
                finish();


                break;
            case R.id.layout_five:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_true);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.RU_RU);

                finish();


                break;
            case R.id.layout_six:


                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_true);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.KO_KR);

                finish();


                break;
            case R.id.layout_seven:
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_true);
                img_eight.setBackgroundResource(R.mipmap.icon_check_false);
                Util.switchLanguage(getContext(), Util.IN_ID);
                finish();
                break;
            case R.id.layout_eight:
                img_one.setBackgroundResource(R.mipmap.icon_check_false);
                img_two.setBackgroundResource(R.mipmap.icon_check_false);
                img_three.setBackgroundResource(R.mipmap.icon_check_false);
                img_four.setBackgroundResource(R.mipmap.icon_check_false);
                img_five.setBackgroundResource(R.mipmap.icon_check_false);
                img_six.setBackgroundResource(R.mipmap.icon_check_false);
                img_seven.setBackgroundResource(R.mipmap.icon_check_false);
                img_eight.setBackgroundResource(R.mipmap.icon_check_true);
                Util.switchLanguage(getContext(), Util.JA_JP);
                finish();
                break;
        }
    }


    private void finish() {
        Intent intent = new Intent(getActivity(), MainOneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
