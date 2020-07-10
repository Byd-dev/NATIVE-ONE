package com.pro.bityard.fragment.my;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LogoutTipEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class SetUpFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_theme)
    TextView text_theme;

    @BindView(R.id.text_language)
    TextView text_language;

    @BindView(R.id.text_select)
    TextView text_select;

    @BindView(R.id.text_rate)
    TextView text_rate;

    @BindView(R.id.btn_logout)
    Button btn_logout;


    @Override
    public void onResume() {
        super.onResume();
        boolean theme = SPUtils.getBoolean(AppConfig.KEY_THEME, true);
        if (theme == true) {
            text_theme.setText(getResources().getText(R.string.text_night));
        } else {
            text_theme.setText(getResources().getText(R.string.text_day));
        }

        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, Util.ZH_SIMPLE);
        Log.d("print", "onResume:57设置页面: " + language);
        if (language.equals(AppConfig.KEY_LANGUAGE)) {
            text_language.setText(getResources().getText(R.string.text_chinese));

        } else {
            if (language.equals(Util.EN_US)) {
                text_language.setText(getResources().getText(R.string.text_english));
            } else if (language.equals(Util.ZH_SIMPLE)) {
                text_language.setText(getResources().getText(R.string.text_chinese));
            } else if (language.equals(Util.ZH_TRADITIONAL)) {
                text_language.setText(getResources().getText(R.string.text_traditional));
            } else if (language.equals(Util.JA_JP)) {
                text_language.setText(getResources().getText(R.string.text_japan));
            } else if (language.equals(Util.KO_KR)) {
                text_language.setText(getResources().getText(R.string.text_korean));
            } else if (language.equals(Util.VI_VN)) {
                text_language.setText(getResources().getText(R.string.text_vi));
            } else if (language.equals(Util.RU_RU)) {
                text_language.setText(getResources().getText(R.string.text_ru));
            } else if (language.equals(Util.IN_ID)) {
                text_language.setText(getResources().getText(R.string.text_id));
            }
        }
        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        text_rate.setText(cny);


    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.img_back).setOnClickListener(this);

        view.findViewById(R.id.btn_logout).setOnClickListener(this);

        view.findViewById(R.id.layout_one).setOnClickListener(this);
        view.findViewById(R.id.layout_two).setOnClickListener(this);
        view.findViewById(R.id.layout_three).setOnClickListener(this);

        if (isLogin()) {
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            btn_logout.setVisibility(View.GONE);

        }
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_setup;
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
                UserActivity.enter(getContext(), IntentConfig.Keys.KEY_LANGUAGE);

                break;
            case R.id.layout_two:
                UserActivity.enter(getContext(), IntentConfig.Keys.KEY_THEME);

                break;
            case R.id.layout_three:
                UserActivity.enter(getContext(), IntentConfig.Keys.KEY_EXCHANGE_RATE);

                break;


            case R.id.btn_logout:

                NetManger.getInstance().postRequest("/api/sso/user_logout", null, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            LogoutTipEntity tipEntity = new Gson().fromJson(response.toString(), LogoutTipEntity.class);
                            if (tipEntity.getCode() == 200) {
                                SPUtils.remove(AppConfig.LOGIN);
                                showToast(tipEntity.getMessage());
                                //退出成功 初始化
                                //余额初始化
                                TagManger.getInstance().clear();
                                getActivity().finish();


                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    }
                });
                break;


        }
    }
}
