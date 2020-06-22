package com.pro.bityard.fragment.user;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ForgetActivity;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class MobileLoginFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_CountryName)
    TextView text_countryName;

    @BindView(R.id.text_countryCode)
    TextView text_countryCode;

    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_pass)
    EditText edit_password;

    private ViewPager viewPager;
    private CountryCodeEntity countryCodeEntity;
    @BindView(R.id.btn_login)
    Button btn_submit;




    @BindView(R.id.text_forget_pass)
    TextView text_forget_pass;
    private int count_pass = 0;
    private String geetestToken = null;

    @BindView(R.id.img_eye)
    ImageView img_eye;

    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    @BindView(R.id.layout_pass)
    LinearLayout layout_pass;
    @BindView(R.id.text_err_pass)
    TextView text_err_pass;
    @BindView(R.id.text_err_mobile)
    TextView text_err_mobile;
    //地区的适配器
    private CountryCodeAdapter countryCodeAdapter;

    private List<CountryCodeEntity.DataBean> searchData;


    public MobileLoginFragment() {

    }


    public MobileLoginFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_email_login).setOnClickListener(this);
        view.findViewById(R.id.layout_country).setOnClickListener(this);
        view.findViewById(R.id.text_forget_pass).setOnClickListener(this);

        view.findViewById(R.id.btn_login).setOnClickListener(this);
        img_eye.setOnClickListener(this);
        text_forget_pass.setText(getResources().getText(R.string.text_forget_pass)+"?");
        //监听软键盘的按钮
        edit_account.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_DONE){
                edit_account.clearFocus();
            }
            return false;
        });

        edit_password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_DONE){
                edit_password.clearFocus();
            }
            return false;
        });
        //手机号输入框焦点的监听
        Util.isPhoneEffective(edit_account, response -> {
            if (response.toString().equals("1")) {
                text_err_mobile.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPass(edit_password.getText().toString()) && edit_password.getText().toString().length() > 5) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            } else if (response.toString().equals("0")) {
                text_err_mobile.setVisibility(View.VISIBLE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit_err));
                btn_submit.setEnabled(false);
            } else if (response.toString().equals("-1")) {
                text_err_mobile.setVisibility(View.GONE);
                layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                btn_submit.setEnabled(false);
            }
        });


        //检测错误提示是否显示
        Util.isPassEffective(edit_password, response -> {
            if (response.toString().equals("1")) {
                text_err_pass.setVisibility(View.GONE);
                layout_pass.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                if (Util.isPhone(edit_account.getText().toString())) {
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

        //忘记密码下划线
        text_forget_pass.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text_forget_pass.getPaint().setAntiAlias(true);//抗锯齿

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        //获取默认的国家区号 如果没有地理位置 就默认中国
        String country_name = SPUtils.getString(com.pro.switchlibrary.AppConfig.COUNTRY_NAME, "中国");

        text_countryName.setText(country_name);
        //获取国家code
        countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
        if (countryCodeEntity == null) {
            NetManger.getInstance().getRequest("/api/home/country/list", null, new OnNetResult() {
                @Override
                public void onNetResult(String state, Object response) {
                    if (state.equals(BUSY)) {
                    } else if (state.equals(SUCCESS)) {
                        CountryCodeEntity countryCodeEntity = new Gson().fromJson(response.toString(), CountryCodeEntity.class);
                        SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);
                        //遍历国家地名 选择区号
                        for (int i = 0; i < countryCodeEntity.getData().size(); i++) {
                            if (country_name.startsWith(countryCodeEntity.getData().get(i).getNameCn())) {
                                text_countryCode.setText(countryCodeEntity.getData().get(i).getCountryCode());
                            }
                        }
                    } else if (state.equals(FAILURE)) {

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


        String text_mobile = SPUtils.getString(AppConfig.USER_MOBILE, null);
        if (text_mobile != null) {
            text_countryCode.setText(SPUtils.getString(AppConfig.USER_COUNTRY_CODE, null));
            text_countryName.setText(SPUtils.getString(AppConfig.USER_COUNTRY_NAME, null));
            edit_account.setText(SPUtils.getString(AppConfig.USER_MOBILE, null));
        }
       /* Util.setTwoUnClick(edit_account, edit_password, btn_submit);
        Util.setTwoUnClick(edit_password, edit_account, btn_submit);*/


    }

    private boolean eye = true;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

            case R.id.text_email_login:
                viewPager.setCurrentItem(0);
                break;

            case R.id.layout_country:
                countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
                showEditPopWindow(countryCodeEntity);
                break;
            case R.id.btn_login:


                String account_value = edit_account.getText().toString();
                String pass_value = edit_password.getText().toString();
                String code_value = text_countryCode.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_pass), Toast.LENGTH_SHORT).show();
                    return;
                }


                Gt3Util.getInstance().customVerity(new OnGtUtilResult() {

                    @Override
                    public void onApi1Result(String result) {
                        geetestToken = result;
                    }

                    @Override
                    public void onSuccessResult(String result) {
                        ArrayMap<String, String> map = new ArrayMap<>();

                        map.put("vHash", Util.Random32());
                        map.put("username", (code_value + account_value));//默认加上区号
                        map.put("password", pass_value);
                        map.put("contryCode", code_value);
                        map.put("geetestToken", geetestToken);
                        map.put("terminal", "Android");

                        NetManger.getInstance().postRequest("/api/sso/user_login_check", map, new OnNetResult() {
                            @Override
                            public void onNetResult(String state, Object response) {
                                if (state.equals(BUSY)) {
                                    showProgressDialog();
                                } else if (state.equals(SUCCESS)) {
                                    Log.d("print", "onNetResult:268:  " + response.toString());
                                    dismissProgressDialog();
                                    LoginEntity loginEntity = new Gson().fromJson(response.toString(), LoginEntity.class);
                                    if (loginEntity.getCode() == 200) {
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
                                            } else if (state2.equals(FAILURE)) {
                                                dismissProgressDialog();

                                            }
                                        });

                                    } else if (loginEntity.getCode() == 401) {
                                        Toast.makeText(getContext(), loginEntity.getMessage(), Toast.LENGTH_SHORT).show();

                                        count_pass++;
                                    } else if (loginEntity.getCode() == 500) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
                                    }
                                } else if (state.equals(FAILURE)) {
                                    dismissProgressDialog();
                                    Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                    }

                    @Override
                    public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                        Toast.makeText(getContext(), gt3ErrorBean.errorDesc, Toast.LENGTH_SHORT).show();
                    }
                });


                break;
            case R.id.text_forget_pass:
                ForgetActivity.enter(getContext(), IntentConfig.Keys.KEY_FORGET, 1);
                break;
        }
    }

    //国际区号选择
    private void showEditPopWindow(CountryCodeEntity data) {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country_code_layout, null);

        TextView text_try = view.findViewById(R.id.text_try);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        countryCodeAdapter = new CountryCodeAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(countryCodeAdapter);

        EditText edit_search = view.findViewById(R.id.edit_search);
        //防止没有数据 再进行判断
        if (data != null) {
            text_try.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            final List<CountryCodeEntity.DataBean> dataData = data.getData();
            countryCodeAdapter.setDatas(dataData);
            setEdit(edit_search, dataData);

        } else {
            text_try.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            text_try.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetManger.getInstance().getRequest("/api/home/country/list", null, new OnNetResult() {
                        @Override
                        public void onNetResult(String state, Object response) {
                            if (state.equals(BUSY)) {
                            } else if (state.equals(SUCCESS)) {

                                CountryCodeEntity countryCodeEntity = new Gson().fromJson(response.toString(), CountryCodeEntity.class);
                                text_try.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);
                                countryCodeAdapter.setDatas(countryCodeEntity.getData());
                                setEdit(edit_search, countryCodeEntity.getData());

                            } else if (state.equals(FAILURE)) {

                            }
                        }
                    });
                }
            });

        }


        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        view.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        countryCodeAdapter.setOnItemClick(new CountryCodeAdapter.OnItemClick() {
            @Override
            public void onSuccessListener(CountryCodeEntity.DataBean dataBean) {
                // TODO: 2020/3/7   中英文切换
                text_countryName.setText(dataBean.getNameCn());
                text_countryCode.setText(dataBean.getCountryCode());
                popupWindow.dismiss();

            }
        });

        popupWindow.setFocusable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /*输入框的监听*/
    private void setEdit(EditText edit_search, List<CountryCodeEntity.DataBean> dataData) {
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
                                || dataData.get(i).getNameEn().contains(s)
                        ) {
                            searchData.add(dataData.get(i));
                        }
                    }
                    countryCodeAdapter.setDatas(searchData);

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
