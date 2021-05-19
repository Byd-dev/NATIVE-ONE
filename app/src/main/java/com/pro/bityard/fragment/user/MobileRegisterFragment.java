package com.pro.bityard.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.MainActivity;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.adapter.CountryCodeHeadAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class MobileRegisterFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    ScrollView layout_view;

    @BindView(R.id.text_CountryName)
    TextView text_countryName;
    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;
    @BindView(R.id.edit_code_mobile)
    EditText edit_code;
    @BindView(R.id.text_countryCode)
    TextView text_countryCode;
    @BindView(R.id.img_eye)
    ImageView img_eye;

    @BindView(R.id.edit_kode)
    EditText edit_kode;


    private ViewPager viewPager;


    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.layout_code)
    LinearLayout layout_code;
    @BindView(R.id.text_err_mobile)
    TextView text_err_mobile;

    @BindView(R.id.text_err_code)
    TextView text_err_code;

    @BindView(R.id.layout_pass)
    LinearLayout layout_pass;
    @BindView(R.id.text_err_pass)
    TextView text_err_pass;
    //地区的适配器
    private CountryCodeAdapter countryCodeAdapter;
    private CountryCodeHeadAdapter countryCodeSearchAdapter;
    private List<CountryCodeEntity.DataBean> searchData;

    private CountryCodeEntity countryCodeEntity;

    @BindView(R.id.text_getCode_mobile)
    TextView text_getCode;
    private String countryID;

    public MobileRegisterFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }


    public MobileRegisterFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_email_login).setOnClickListener(this);

        view.findViewById(R.id.layout_country).setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_eye.setOnClickListener(this);

        text_getCode.setOnClickListener(this);


        //手机号码输入框焦点的监听
        Util.isPhoneEffective(edit_account, response -> {
            if (response.toString().equals("1")) {
                text_err_mobile.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                text_getCode.setEnabled(true);
                if (Util.isCode(edit_code.getText().toString()) && Util.isPass(edit_password.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_mobile.setVisibility(View.VISIBLE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                text_getCode.setEnabled(false);
                btn_submit.setEnabled(false);

            } else if (response.toString().equals("-1")) {
                text_err_mobile.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                text_getCode.setEnabled(false);
                btn_submit.setEnabled(false);

            }
        });

        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 4 && Util.isCode(s.toString()) && Util.isPhone(edit_account.getText().toString())
                        && Util.isPass(edit_password.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //检测错误提示是否显示
        Util.isPassEffective(edit_password, response -> {
            if (response.toString().equals("1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPhone(edit_account.getText().toString()) && Util.isCode(edit_code.getText().toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_pass.setVisibility(View.VISIBLE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }

        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_register;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        //获取默认的国家区号 如果没有地理位置 就默认中国
        String country_name = SPUtils.getString(com.pro.switchlibrary.AppConfig.COUNTRY_NAME, getString(R.string.text_china));

        text_countryName.setText(country_name);


        //获取国家code
        countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
        if (countryCodeEntity == null) {


            NetManger.getInstance().getMobileCountryCode((state, response) -> {
                if (state.equals(SUCCESS)) {

                    CountryCodeEntity countryCodeEntity = (CountryCodeEntity) response;
                    SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);
                    //遍历国家地名 选择区号
                    for (int i = 0; i < countryCodeEntity.getData().size(); i++) {
                        if (country_name.startsWith(countryCodeEntity.getData().get(i).getNameCn())) {
                            text_countryCode.setText(countryCodeEntity.getData().get(i).getCountryCode());
                        }
                    }
                }
            });

        } else {
            //遍历国家地名 选择区号
            for (int i = 0; i < countryCodeEntity.getData().size(); i++) {
                if (country_name.startsWith(countryCodeEntity.getData().get(i).getNameCn())) {
                    text_countryCode.setText(countryCodeEntity.getData().get(i).getCountryCode());
                }
            }
        }


    }

    private boolean eye = true;


    @Override
    public void onClick(View v) {
        String account_value = edit_account.getText().toString();

        String country_code = text_countryCode.getText().toString();

        switch (v.getId()) {
            case R.id.text_email_login:
                viewPager.setCurrentItem(0);
                break;
            case R.id.img_eye:
                if (eye) {
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_open));
                    eye = false;
                } else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    img_eye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_eye_close));
                    eye = true;
                }
                break;

            case R.id.layout_country:
                countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
                showEditPopWindow(countryCodeEntity);
                break;

            case R.id.text_getCode_mobile:
                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取验证码
                //   getCode(country_code, account_value);

                NetManger.getInstance().getMobileCode(getActivity(),layout_view,country_code + account_value, "REGISTER", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.sendEmptyMessage(0);
                            Message msg = new Message();
                            mHandler.sendMessage(msg);
                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                break;

            case R.id.btn_submit:
                String value_kode = edit_kode.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                    return;
                }

                String code_value = edit_code.getText().toString();
                if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_mobile_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass_value = edit_password.getText().toString();
                if (pass_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_pass), Toast.LENGTH_SHORT).show();
                    return;
                }


                for (CountryCodeEntity.DataBean dataBean : countryCodeEntity.getData()) {
                    if (dataBean.getCountryCode().equals(country_code)) {
                        countryID = dataBean.getId();
                    }
                }


                HashMap<String, String> map1 = new HashMap<>();
                map1.put("contryId", countryID);
                map1.put("password", URLEncoder.encode(pass_value));
                map1.put("phone", country_code + account_value);
                map1.put("ru",value_kode);

                String value_sign = Util.getSign(map1, AppConfig.SIGN_KEY);


                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("contryId", countryID);
                map.put("phone", country_code + account_value);
                map.put("password", URLEncoder.encode(pass_value));
                map.put("sign", value_sign);
                //校验验证码

                NetManger.getInstance().checkMobileCode(country_code + account_value, "REGISTER", code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck()) {
                            //成功了再注册
                            Log.d("print", "onNetResult: 238: " + tipEntity);
                            //  register(map);
                            register(countryID, country_code, account_value, pass_value, value_sign,value_kode);


                        } else {
                            if (tipEntity.getMessage().equals("")) {
                                Toast.makeText(getContext(), getResources().getString(R.string.text_correct_mobile_code), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;
        }
    }

    private void register(String countryID, String country_code, String account_value, String pass_value, String value_sign,String value_kode) {
        NetManger.getInstance().register(false, countryID, country_code, account_value, pass_value, value_sign,value_kode, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getCode() == 200) {
                    //注册成功登录
                    login(country_code + account_value, pass_value, "undefined");
                }

                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_register_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });
    }


    private void register(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/register/submit", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    Objects.requireNonNull(getActivity()).finish();
                }

                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_register_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    private void login(String account_value, String pass_value, String geetestToken) {
        NetManger.getInstance().login(account_value, pass_value,true, Util.Random32(),geetestToken, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "onNetResult:196:  " + response.toString());
                dismissProgressDialog();
                LoginEntity loginEntity = (LoginEntity) response;
                //缓存上一次登录成功的区号和地址
                SPUtils.putString(AppConfig.USER_COUNTRY_CODE, text_countryCode.getText().toString());
                SPUtils.putString(AppConfig.USER_COUNTRY_NAME, text_countryName.getText().toString());
                SPUtils.putString(AppConfig.USER_MOBILE, edit_account.getText().toString());
                NetManger.getInstance().userDetail((state2, response2) -> {
                    if (state2.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state2.equals(SUCCESS)) {
                        dismissProgressDialog();
                        UserDetailEntity userDetailEntity = (UserDetailEntity) response2;
                        loginEntity.getUser().setUserName(userDetailEntity.getUser().getUsername());
                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                        //登录成功 初始化
                        TagManger.getInstance().tag();
                        getActivity().finish();
                        MainActivity.enter(getActivity(), MainActivity.TAB_TYPE.TAB_HOME);
                        SPUtils.putString(AppConfig.POP_LOGIN, "pop_login");

                    } else if (state2.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });

            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
                if (response != null) {
                    LoginEntity loginEntity = (LoginEntity) response;
                    Toast.makeText(getContext(), loginEntity.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                SmsTimeUtils.check(SmsTimeUtils.MOBILE_REGISTER, false);
                SmsTimeUtils.startCountdown(text_getCode);
            }
        }

    };

    //国际区号选择
    private void showEditPopWindow(CountryCodeEntity data) {


        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country_code_layout, null);

        TextView text_try = view.findViewById(R.id.text_try);

        TextView text_used = view.findViewById(R.id.text_used);


        HeaderRecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        RecyclerView recyclerView_search = view.findViewById(R.id.recyclerView_search);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_country_code_layout, null);
        countryCodeAdapter = new CountryCodeAdapter(getActivity());
        CountryCodeHeadAdapter countryCodeHeadAdapter = new CountryCodeHeadAdapter(getActivity());
        countryCodeSearchAdapter = new CountryCodeHeadAdapter(getActivity());
        RecyclerView recyclerViewHead = headView.findViewById(R.id.recyclerview_head);
        recyclerViewHead.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewHead.setAdapter(countryCodeHeadAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_search.setAdapter(countryCodeSearchAdapter);
        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addHeaderView(headView);
        recyclerView.setAdapter(countryCodeAdapter);

        EditText edit_search = view.findViewById(R.id.edit_search);
        //防止没有数据 再进行判断
        if (data != null) {
            text_try.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            final List<CountryCodeEntity.DataBean> dataData = data.getData();
            countryCodeAdapter.setDatas(dataData);
            setEdit(edit_search, text_used, dataData, recyclerView_search, recyclerView);




            List<CountryCodeEntity.DataBean> dataBeanList = new ArrayList<>();
            for (CountryCodeEntity.DataBean dataBean : dataData) {
                if (dataBean.isUsed()) {
                    dataBeanList.add(dataBean);
                }
            }

            countryCodeHeadAdapter.setDatas(dataBeanList);


        } else {
            text_try.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerView_search.setVisibility(View.GONE);

            text_try.setOnClickListener(v -> NetManger.getInstance().getMobileCountryCode((state, response) -> {
                        if (state.equals(SUCCESS)) {
                            CountryCodeEntity countryCodeEntity = (CountryCodeEntity) response;
                            text_try.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);
                            countryCodeAdapter.setDatas(countryCodeEntity.getData());
                            setEdit(edit_search, text_used, countryCodeEntity.getData(), recyclerView_search, recyclerView);

                        }
                    }
            ));

        }


        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        view.findViewById(R.id.img_back).setOnClickListener(v -> popupWindow.dismiss());

        countryCodeAdapter.setOnItemClick(dataBean -> {
            String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
            if (language.equals(AppConfig.ZH_SIMPLE)||language.equals(AppConfig.ZH_TRADITIONAL)){
                text_countryName.setText(dataBean.getNameCn());
            }else {
                text_countryName.setText(dataBean.getNameEn());
            }
            text_countryCode.setText(dataBean.getCountryCode());
            popupWindow.dismiss();

        });

        countryCodeHeadAdapter.setOnItemHeadClick(dataBean -> {
            String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
            if (language.equals(AppConfig.ZH_SIMPLE)||language.equals(AppConfig.ZH_TRADITIONAL)){
                text_countryName.setText(dataBean.getNameCn());
            }else {
                text_countryName.setText(dataBean.getNameEn());
            }
            text_countryCode.setText(dataBean.getCountryCode());
            popupWindow.dismiss();
        });


        countryCodeSearchAdapter.setOnItemHeadClick(dataBean -> {
            String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
            if (language.equals(AppConfig.ZH_SIMPLE)||language.equals(AppConfig.ZH_TRADITIONAL)){
                text_countryName.setText(dataBean.getNameCn());
            }else {
                text_countryName.setText(dataBean.getNameEn());
            }
            text_countryCode.setText(dataBean.getCountryCode());
            popupWindow.dismiss();
        });


        popupWindow.setFocusable(true);
        //popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /*输入框的监听*/
    private void setEdit(EditText edit_search, TextView text_used, List<CountryCodeEntity.DataBean> dataData, RecyclerView recyclerView_search, HeaderRecyclerView recyclerView) {
        edit_search.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    searchData = new ArrayList<>();
                    for (int i = 0; i < dataData.size(); i++) {
                        if (dataData.get(i).getNameCn().contains(s)
                                || dataData.get(i).getCountryCode().contains(s)
                                || dataData.get(i).getNameEn().contains(s)) {
                            searchData.add(dataData.get(i));
                        }
                    }
                    recyclerView_search.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    countryCodeSearchAdapter.setDatas(searchData);
                    text_used.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_search.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    countryCodeAdapter.setDatas(dataData);
                    text_used.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }
}
