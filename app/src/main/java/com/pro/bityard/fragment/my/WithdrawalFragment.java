package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.ChainListAdapter;
import com.pro.bityard.adapter.WithdrawalAddressSelectAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class WithdrawalFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_record)
    ImageView img_record;
    private UserDetailEntity userDetailEntity;
    @BindView(R.id.text_balance)
    TextView text_balance;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.edit_amount)
    EditText edit_amount;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.edit_pass_withdraw)
    EditText edit_pass_withdraw;
    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.img_eye)
    ImageView img_eye;
    @BindView(R.id.text_getCode)
    TextView text_getCode;
    @BindView(R.id.img_right)
    ImageView img_right;

    private ChainListAdapter chainListAdapter;//杠杆适配器
    private List<String> dataList;

    private String chain = "OMNI";
    private LoginEntity loginEntity;
    private String account;

    @BindView(R.id.layout_address)
    LinearLayout layout_address;
    // 声明平移动画
    private TranslateAnimation animation;

    private String addressId;

    private WithdrawalAddressSelectAdapter withdrawalAddressSelectAdapter;
    private WithdrawalAdressEntity withdrawalAdressEntity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        text_title.setText(getResources().getString(R.string.text_withdrawal));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.text_address_manage).setOnClickListener(this);
        img_eye.setOnClickListener(this);
        text_getCode.setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        layout_address.setOnClickListener(this);
        img_right.setOnClickListener(this);

        withdrawalAddressSelectAdapter = new WithdrawalAddressSelectAdapter(getActivity());


        Util.setFourUnClick(edit_amount, edit_pass_withdraw, edit_address, edit_code, btn_submit);
        Util.setFourUnClick(edit_pass_withdraw, edit_amount, edit_address, edit_code, btn_submit);
        Util.setFourUnClick(edit_address, edit_pass_withdraw, edit_amount, edit_code, btn_submit);
        Util.setFourUnClick(edit_code, edit_pass_withdraw, edit_address, edit_amount, btn_submit);

        img_record.setVisibility(View.VISIBLE);
        img_record.setOnClickListener(this);

        chainListAdapter = new ChainListAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(chainListAdapter);


        dataList = new ArrayList<>();
        dataList.add("OMNI");
        dataList.add("ERC20");
        dataList.add("TRC20");
        chainListAdapter.setDatas(dataList);

        chainListAdapter.setOnItemClick((position, data) -> {
            chainListAdapter.select(data);
            recyclerView.setAdapter(chainListAdapter);
            chain = data;
        });


        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        account = loginEntity.getUser().getPrincipal();
        if (account.contains("@")) {
            edit_code.setHint(getResources().getString(R.string.text_email_code));
        } else {
            edit_code.setHint(getResources().getString(R.string.text_mobile_code));
        }


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_withdrawal;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        UserDetailManger.getInstance().addObserver(this);

        NetManger.getInstance().withdrawalAddressList((state, response) -> {
            if (state.equals(SUCCESS)) {
                withdrawalAdressEntity = (WithdrawalAdressEntity) response;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_right:
                img_right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_market_open));

                showAddressPopWindow(withdrawalAdressEntity);


                break;
            case R.id.layout_address:

                break;
            case R.id.img_record:

                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.text_address_manage:
                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_WITHDRAWAL_ADDRESS);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.img_eye:

                Util.setEye(getActivity(), edit_pass_withdraw, img_eye);
                break;
            case R.id.text_getCode:
                if (account.contains("@")) {
                    NetManger.getInstance().getEmailCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", (state, response1, response2) -> {
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
                } else {
                    NetManger.getInstance().getMobileCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", (state, response1, response2) -> {
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
                }
                break;
            case R.id.btn_submit:
                String value_amount = edit_amount.getText().toString();
                String value_pass = edit_pass_withdraw.getText().toString();
                String value_code = edit_code.getText().toString();
                String value_address = edit_address.getText().toString();

                if (account.contains("@")) {

                    NetManger.getInstance().checkEmailCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                            if (tipEntity.isCheck() == true) {
                                withdrawal(value_amount, userDetailEntity.getUser().getCurrency(),
                                        chain, addressId, account, value_pass);

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

                } else {

                    NetManger.getInstance().checkMobileCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.isCheck() == true) {
                                withdrawal(value_amount, userDetailEntity.getUser().getCurrency(),
                                        chain, addressId, account, value_pass);
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }

                break;
        }
    }

    private void showAddressPopWindow(WithdrawalAdressEntity withdrawalAdressEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_address_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
       /* animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());*/

        popupWindow.setOnDismissListener(() -> {
            img_right.setImageDrawable(getResources().getDrawable(R.mipmap.icon_market_right));

        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(withdrawalAddressSelectAdapter);
        if (withdrawalAdressEntity != null) {
            withdrawalAddressSelectAdapter.setDatas(withdrawalAdressEntity.getData());
        }

        withdrawalAddressSelectAdapter.setOnItemClick(data -> {
            edit_address.setText(data.getAddress());
            addressId = data.getId();
            popupWindow.dismiss();
            chain = data.getChain();
            chainListAdapter.select(chain);
            recyclerView.setAdapter(chainListAdapter);


        });

        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(layout_address, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //view.startAnimation(animation);

    }


    private void withdrawal(String money, String currency, String chain, String addressId, String email, String password) {
        NetManger.getInstance().withdrawal(money, currency, chain, addressId, email, password, (state, response) -> {
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = (TipEntity) response;
                Log.d("print", "transfer:提币结果:  " + response);
                if (tipEntity.getCode() == 200) {
                }
                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*获取倒计时*/
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

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
    public void update(Observable o, Object arg) {
        if (o == UserDetailManger.getInstance()) {
            userDetailEntity = (UserDetailEntity) arg;
            if (text_balance != null) {
                if (isAdded()) {
                    if (userDetailEntity == null) {
                        return;
                    }
                    if (userDetailEntity.getUser() == null) {
                        return;
                    }
                    text_balance.setText(TradeUtil.getNumberFormat(userDetailEntity.getUser().getMoney(), 2));

                }
            }
        }
    }
}
