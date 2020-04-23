package com.pro.bityard.fragment.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.ResetPassActivity;
import com.pro.bityard.adapter.CountryCodeAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnGtUtilResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.SmsTimeUtils;
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

public class MobileForgetFragment extends BaseFragment implements View.OnClickListener{


    private ViewPager viewPager;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_CountryName)
    TextView text_countryName;

    @BindView(R.id.text_countryCode)
    TextView text_countryCode;
    @BindView(R.id.edit_account)
    EditText edit_account;

    @BindView(R.id.edit_code)
    EditText edit_code;

    @BindView(R.id.text_getCode)
    TextView text_getCode;
    private CountryCodeEntity countryCodeEntity;
    //地区的适配器
    private CountryCodeAdapter countryCodeAdapter;

    private List<CountryCodeEntity.DataBean> searchData;

    private String geetestToken = null;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    public MobileForgetFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
    public MobileForgetFragment() {
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_email_forget).setOnClickListener(this);


        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.layout_country).setOnClickListener(this);

        text_getCode.setOnClickListener(this);


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mobile_forget;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        //获取默认的国家区号 如果没有地理位置 就默认中国
        String country_name = SPUtils.getString(com.pro.switchlibrary.AppConfig.COUNTRY_NAME,"中国");

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

        String text_mobile = SPUtils.getString(AppConfig.USER_MOBILE,null);
        if (text_mobile!=null) {
            text_countryCode.setText(SPUtils.getString(AppConfig.USER_COUNTRY_CODE,null));
            text_countryName.setText(SPUtils.getString(AppConfig.USER_COUNTRY_NAME,null));
            edit_account.setText(SPUtils.getString(AppConfig.USER_MOBILE,null));
        }


    }



    @Override
    public void onClick(View v) {
        String account_value = edit_account.getText().toString();
        String country_code = text_countryCode.getText().toString();

        switch (v.getId()) {
            case R.id.layout_country:
                countryCodeEntity = SPUtils.getData(AppConfig.COUNTRY_CODE, CountryCodeEntity.class);
                showEditPopWindow(countryCodeEntity);
                break;

            case R.id.text_email_forget:
                viewPager.setCurrentItem(0);
                break;
            case R.id.text_getCode:
                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                    return;
                }

             //   getCode(account_value, country_code);

                NetManger.getInstance().getMobileCode(country_code+account_value, "FORGOT_PASSWORD", (state, response1, response2) -> {
                    if (state.equals(BUSY)){
                        showProgressDialog();
                    }else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.sendEmptyMessage(0);
                            Message msg = new Message();
                            mHandler.sendMessage(msg);
                        }else if (tipEntity.getCode()==500){
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }else if (state.equals(FAILURE)){
                        dismissProgressDialog();
                    }
                });
                break;

            case R.id.btn_submit:
                //1  人机  2 发送验证码  3 验证验证码 4 验证账号 5改密



                String code_value = edit_code.getText().toString();

                if (account_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_input_number), Toast.LENGTH_SHORT).show();
                    return;
                } else if (code_value.equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.text_mobile_code_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                //1验证验证码
              //  checkCode(country_code, account_value, code_value);

                NetManger.getInstance().checkMobileCode(country_code + account_value, "FORGOT_PASSWORD", code_value, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                            //2 验证账号
                            checkAccount(country_code, account_value);

                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.text_code_lose), Toast.LENGTH_SHORT).show();

                        }

                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


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
        //popupWindow.setAnimationStyle(R.style.pop_anim);
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


    /*1获取验证码*/
    private void getCode(String account_value, String country_code) {


        Gt3Util.getInstance().customVerity(new OnGtUtilResult() {

            @Override
            public void onApi1Result(String result) {
                geetestToken = result;

            }

            @Override
            public void onSuccessResult(String result) {
                ArrayMap<String, String> map = new ArrayMap<>();

                map.put("account", country_code + account_value);
                map.put("type", "FORGOT_PASSWORD");
                map.put("geetestToken", geetestToken);
                NetManger.getInstance().postRequest("/api/system/sendSMS", map, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
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
                    }
                });
            }

            @Override
            public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                Toast.makeText(getContext(), gt3ErrorBean.errorDesc, Toast.LENGTH_SHORT).show();

            }
        });

    }

    /*2校验手机验证码*/
    private void checkCode(String country_code, String account_value, String code_value) {
        ArrayMap<String, String> map = new ArrayMap<>();

        map.put("account", country_code + account_value);
        map.put("type", "FORGOT_PASSWORD");
        map.put("code", code_value);

        NetManger.getInstance().postRequest("/api/system/checkSMS", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200 && tipEntity.isCheck() == true) {
                        //2 验证账号
                        checkAccount(country_code, account_value);

                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_code_lose), Toast.LENGTH_SHORT).show();

                    }

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    /*3账号验证*/
    private void checkAccount(String country_code, String account_value) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account_value);

        NetManger.getInstance().postRequest("/api/forgot/account-verify", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200 && tipEntity.isVerify_email() == true) {
                        //3 安全验证
                        checkSafe(country_code, account_value);


                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    /*4安全验证*/
    private void checkSafe(String country_code, String account_value) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", country_code + account_value);
        NetManger.getInstance().postRequest("/api/forgot/securify-verify", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200) {
                        String token = tipEntity.getToken();
                        ResetPassActivity.enter(getContext(),token);
                        getActivity().finish();



                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();

                    }
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Gt3Util.getInstance().destroy();
    }



}
