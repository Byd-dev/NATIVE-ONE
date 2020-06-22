package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class MobileBindChangeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_CountryName)
    TextView text_countryName;
    @BindView(R.id.edit_account)
    EditText edit_account;


    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.text_countryCode)
    TextView text_countryCode;

    @BindView(R.id.layout_country_code)
    LinearLayout layout_country_code;
    @BindView(R.id.layout_mobile)
    LinearLayout layout_mobile;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.text_err_mobile)
    TextView text_err_mobile;
    @BindView(R.id.layout_account)
    LinearLayout layout_account;
    //地区的适配器
    private CountryCodeAdapter countryCodeAdapter;

    private List<CountryCodeEntity.DataBean> searchData;

    private CountryCodeEntity countryCodeEntity;

    @BindView(R.id.text_getCode)
    TextView text_getCode;
    private String googleToken;
    private LoginEntity loginEntity;
    private String account_value;
    private String countryID;
    private String sendType;
    private boolean mobile;
    private String account;

    public MobileBindChangeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }


    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {


        view.findViewById(R.id.layout_country).setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        text_getCode.setOnClickListener(this);


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_bind;
    }

    @Override
    protected void intPresenter() {

    }


    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        Log.d("print", "onResume:136:  " + loginEntity);
        if (loginEntity != null) {
            LoginEntity.UserBean user = loginEntity.getUser();

            if (user.getMobile().equals("")) {
                text_title.setText(R.string.text_bind_mobile);
                layout_country_code.setVisibility(View.VISIBLE);
                layout_mobile.setVisibility(View.VISIBLE);
                btn_submit.setText(R.string.text_sure);
                //手机号码输入框焦点的监听
                Util.isPhoneEffective(edit_account, response -> {
                    if (response.toString().equals("1")) {
                        text_err_mobile.setVisibility(View.GONE);
                        layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                        text_getCode.setEnabled(true);
                        if (Util.isCode(edit_code.getText().toString())) {
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
                        if (s.length() > 4 && Util.isCode(s.toString()) && Util.isPhone(edit_account.getText().toString())) {
                            btn_submit.setEnabled(true);
                        } else {
                            btn_submit.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            } else {
                mobile = SPUtils.getBoolean(AppConfig.CHANGE_MOBILE, true);
                if (mobile == true) {
                    text_title.setText(R.string.text_mobile_change);
                    layout_country_code.setVisibility(View.GONE);
                    layout_mobile.setVisibility(View.GONE);
                    btn_submit.setText(R.string.text_next);
                    text_getCode.setEnabled(true);
                    edit_code.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() > 4 && Util.isCode(s.toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    text_title.setText(R.string.text_mobile_change);
                    layout_country_code.setVisibility(View.VISIBLE);
                    layout_mobile.setVisibility(View.VISIBLE);
                    btn_submit.setText(R.string.text_sure);
                    //手机号码输入框焦点的监听
                    Util.isPhoneEffective(edit_account, response -> {
                        if (response.toString().equals("1")) {
                            text_err_mobile.setVisibility(View.GONE);
                            layout_account.setBackground(getResources().getDrawable(R.drawable.bg_shape_edit));
                            text_getCode.setEnabled(true);
                            if (Util.isCode(edit_code.getText().toString())) {
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
                            if (s.length() > 4 && Util.isCode(s.toString()) && Util.isPhone(edit_account.getText().toString())) {
                                btn_submit.setEnabled(true);
                            } else {
                                btn_submit.setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }


            }


        }

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


    }


    @Override
    public void onClick(View v) {
        account_value = edit_account.getText().toString();

        String country_code = text_countryCode.getText().toString();

        switch (v.getId()) {

            case R.id.img_back:
                getActivity().finish();
                break;


            case R.id.layout_country:
                countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
                showEditPopWindow(countryCodeEntity);
                break;

            case R.id.text_getCode:
                /*1首先判断是否绑定手机 没有的话就绑定手机
                 * 2再判断当前是设置手机号码 还是修改手机号码
                 * 3 设置手机号码就直接设置 修改是下一步*/
                // 旧手机验证的用CHANGE_PHONE，新手机验证是用BIND_PHONE
                if (loginEntity.getUser().getMobile().equals("")) {
                    sendType = "BIND_PHONE";
                    account = country_code + account_value;
                    if (account_value.equals("")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (mobile == true) {
                        account = loginEntity.getUser().getMobile();
                        sendType = "CHANGE_PHONE";

                    } else {
                        sendType = "BIND_PHONE";

                        if (account_value.equals("")) {
                            Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        account = country_code + account_value;
                    }
                }

                Log.d("print", "onClick:245: " + mobile + "----" + account);
                NetManger.getInstance().getMobileCode(account, sendType, (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        googleToken = (String) response1;
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


                if (loginEntity.getUser().getMobile().equals("")) {
                    sendType = "BIND_PHONE";
                    if (account_value.equals("")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (mobile == true) {
                        sendType = "CHANGE_PHONE";
                        account = loginEntity.getUser().getMobile();
                    } else {
                        sendType = "BIND_PHONE";
                        account = country_code + account_value;

                    }

                }


                String code_value = edit_code.getText().toString();
                if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_mobile_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }


                for (CountryCodeEntity.DataBean dataBean : countryCodeEntity.getData()) {
                    if (dataBean.getCountryCode().equals(country_code)) {
                        countryID = dataBean.getId();
                    }
                }

                Log.d("print", "onClick:310: " + mobile + "----" + account);

                NetManger.getInstance().checkMobileCode(account, sendType, code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                            Log.d("print", "onClick:319:验证验证码:  " + tipEntity);
                            if (tipEntity.getMessage().equals("")) {

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            if (loginEntity.getUser().getMobile().equals("")) {
                                //成功了再绑定
                                ArrayMap<String, String> map = new ArrayMap<>();
                                map.put("phone", country_code + account_value);
                                map.put("countryId", countryID);
                                map.put("googleToken", googleToken);
                                bindMobile(map);
                            } else {
                                if (mobile == true) {
                                    Log.d("print", "onClick:314:  " + response + "    -----    " + mobile);
                                    SPUtils.putBoolean(AppConfig.CHANGE_MOBILE, false);
                                    getActivity().finish();
                                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_MOBILE);
                                } else {
                                    SPUtils.putBoolean(AppConfig.CHANGE_MOBILE, true);
                                    ArrayMap<String, String> map = new ArrayMap<>();
                                    map.put("oldPhone", loginEntity.getUser().getMobile());
                                    map.put("newPhone", country_code + account_value);
                                    map.put("countryId", countryID);
                                    map.put("googleToken", googleToken);
                                    updateMobile(map);
                                }
                            }
                        } else {
                            if (tipEntity.getMessage().equals("")) {
                                Toast.makeText(getActivity(), R.string.text_correct_mobile_code, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
                break;
        }
    }


    private void bindMobile(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/user/bind-phone", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                Log.d("print", "bindMobile:284:绑定手机号:  " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    loginEntity.getUser().setMobile(account);
                    SPUtils.putData(AppConfig.LOGIN, loginEntity);
                    getActivity().finish();


                }
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), R.string.text_success_bind, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    private void updateMobile(ArrayMap<String, String> map) {

        NetManger.getInstance().postRequest("/api/user/update-phone", map, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                Log.d("print", "bindMobile:397:修改手机号:  " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    loginEntity.getUser().setMobile(account);
                    loginEntity.getUser().setPhone(account);
                    SPUtils.putData(AppConfig.LOGIN, loginEntity);
                    getActivity().finish();


                }
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getContext(), R.string.text_change_success, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });

    }

    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SmsTimeUtils.check(SmsTimeUtils.SETTING_FINANCE_ACCOUNT_TIME, false);
                    SmsTimeUtils.startCountdown(text_getCode);
                    break;
                default:
                    break;
            }
        }

    };

    //国际区号选择
    private void showEditPopWindow(CountryCodeEntity data) {


        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country_code_layout, null);

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

            text_try.setOnClickListener(v -> NetManger.getInstance().getRequest("/api/home/country/list", null, new OnNetResult() {
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
            }));

        }


        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);


        view.findViewById(R.id.img_back).setOnClickListener(v -> popupWindow.dismiss());
        countryCodeAdapter.setOnItemClick(dataBean -> {
            // TODO: 2020/3/7   中英文切换
            text_countryName.setText(dataBean.getNameCn());
            text_countryCode.setText(dataBean.getCountryCode());
            popupWindow.dismiss();

        });


        popupWindow.setFocusable(true);
        //popupWindow.setAnimationStyle(R.style.pop_anim);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
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
