package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
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
import com.pro.bityard.adapter.CurrencyChainAdapter;
import com.pro.bityard.adapter.CurrencyHistoryAdapter;
import com.pro.bityard.adapter.WithdrawCurrencyAdapter;
import com.pro.bityard.adapter.WithdrawHistoryAdapter;
import com.pro.bityard.adapter.WithdrawalAddressAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.CurrencyDetailEntity;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UnionRateEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.entity.WithdrawCurrencyEntity;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class WithdrawalFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_record)
    ImageView img_record;
    @BindView(R.id.text_balance)
    TextView text_balance;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.edit_amount)
    EditText edit_amount;
    @BindView(R.id.text_address)
    TextView text_address;
    @BindView(R.id.text_range)
    TextView text_range;
    @BindView(R.id.edit_pass_withdraw)
    EditText edit_pass_withdraw;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.img_eye)
    ImageView img_eye;
    @BindView(R.id.text_getCode)
    TextView text_getCode;
    @BindView(R.id.text_withdrawal_tip_one)
    TextView text_withdrawal_tip_one;
    @BindView(R.id.text_withdrawal_tip_two)
    TextView text_withdrawal_tip_two;
    @BindView(R.id.text_withdrawal_tip_three)
    TextView text_withdrawal_tip_three;
    @BindView(R.id.text_withdrawal_tip_four)
    TextView text_withdrawal_tip_four;
    @BindView(R.id.text_withdrawal_tip_five)
    TextView text_withdrawal_tip_five;
    @BindView(R.id.text_withdrawal_tip_six)
    TextView text_withdrawal_tip_six;
    @BindView(R.id.text_withdrawal_tip_seven)
    TextView text_withdrawal_tip_seven;
    @BindView(R.id.text_withdrawal_tip_eight)
    TextView text_withdrawal_tip_eight;
    @BindView(R.id.text_transfer_title)
    TextView text_transfer_title;

    @BindView(R.id.layout_withdrawal)
    LinearLayout layout_withdrawal;

    @BindView(R.id.layout_transfer)
    LinearLayout layout_transfer;
    @BindView(R.id.text_record)
    TextView text_record;

    private CurrencyChainAdapter currencyChainAdapter;//连名称

    private String chain = "OMNI";
    private LoginEntity loginEntity;

    @BindView(R.id.text_fee)
    TextView text_fee;
    @BindView(R.id.text_today_fee)
    TextView text_transfer_fee;

    //private String account;

    @BindView(R.id.layout_address)
    LinearLayout layout_address;
    // 声明平移动画
    private TranslateAnimation animation;

    private String addressId;

    private WithdrawalAddressAdapter withdrawalAddressAdapter;
    private WithdrawalAdressEntity withdrawalAdressEntity;
    private UserDetailEntity.UserBean user;

    private UnionRateEntity unionRateEntity;

    /*转账*/
    @BindView(R.id.edit_amount_transfer)
    EditText edit_amount_transfer;
    @BindView(R.id.text_balance_transfer)
    TextView text_balance_transfer;
    @BindView(R.id.edit_username)
    EditText edit_username;
    @BindView(R.id.edit_pass_transfer)
    EditText edit_pass_transfer;
    @BindView(R.id.edit_code_transfer)
    EditText edit_code_transfer;
    @BindView(R.id.img_eye_transfer)
    ImageView img_eye_transfer;
    @BindView(R.id.text_getCode_transfer)
    TextView text_getCode_transfer;
    @BindView(R.id.btn_submit_transfer)
    Button btn_submit_transfer;

    @BindView(R.id.text_withdraw_currency)
    TextView text_withdraw_currency;
    @BindView(R.id.text_withdraw_currency_transfer)
    TextView text_withdraw_currency_transfer;
    @BindView(R.id.img_bg)
    ImageView img_bg;
    @BindView(R.id.img_bg_transfer)
    ImageView img_bg_transfer;
    private String email;
    private DepositWithdrawEntity depositWithdrawEntity;

    private WithdrawHistoryAdapter withdrawHistoryAdapter;
    private WithdrawCurrencyEntity withdrawCurrencyEntity;
    private BalanceEntity balanceEntity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());


    }

    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        email = loginEntity.getUser().getEmail();
        edit_code.setHint(getResources().getString(R.string.text_email_code));
        Log.d("print", "initView:229:  " + loginEntity);

        NetManger.getInstance().withdrawalAddressList((state, response) -> {
            if (state.equals(SUCCESS)) {
                withdrawalAdressEntity = (WithdrawalAdressEntity) response;

            }
        });

        if (email.equals("")) {
            runOnUiThread(() -> {
                Util.lightOff(getActivity());
                PopUtil.getInstance().showTip(getActivity(),
                        layout_view,
                        true,
                        getString(R.string.text_un_email_bind),
                        state -> {
                            if (state) {
                                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_BIND_CHANGE_EMAIL);
                            } else {
                                getActivity().finish();
                            }

                        });
            });
        } else {
            if (loginEntity.getUser().getPw_w() == 0) {
                runOnUiThread(() -> {
                    Util.lightOff(getActivity());
                    PopUtil.getInstance().showTip(getActivity(),
                            layout_view,
                            true,
                            getString(R.string.text_un_withdrawal_pass),
                            state -> {
                                if (state) {
                                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_SAFE_CENTER_FUNDS_PASS);
                                } else {
                                    getActivity().finish();
                                }
                            });
                });
            }
        }


        NetManger.getInstance().withdrawalAddressList((state, response) -> {
            refreshAddress((state1, response1) -> {
                if (state1.equals(BUSY)) {
                } else if (state1.equals(SUCCESS)) {
                    withdrawalAdressEntity = (WithdrawalAdressEntity) response1;
                    if (withdrawalAdressEntity != null) {
                        withdrawalAddressAdapter.setDatas(withdrawalAdressEntity.getData());
                    }
                } else if (state1.equals(FAILURE)) {
                }
            });


        });
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        layout_transfer.setVisibility(View.GONE);

        text_title.setText(getResources().getString(R.string.text_withdrawal));
        text_title.setTextColor(getResources().getColor(R.color.maincolor));

        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.text_address_manage).setOnClickListener(this);
        view.findViewById(R.id.layout_withdrawal_pop).setOnClickListener(this);
        view.findViewById(R.id.layout_withdrawal_transfer).setOnClickListener(this);

        text_address.setOnClickListener(this);
        text_record.setOnClickListener(this);

        text_title.setOnClickListener(this);
        text_transfer_title.setOnClickListener(this);

        text_withdrawal_tip_two.setText("3." + getResources().getString(R.string.text_withdrawal_tip_two));
        text_withdrawal_tip_four.setText("4." + getResources().getString(R.string.text_withdrawal_tip_four));
        text_withdrawal_tip_five.setText("5." + getResources().getString(R.string.text_withdrawal_tip_five));
        text_withdrawal_tip_six.setText("6." + getResources().getString(R.string.text_withdrawal_tip_six));
        text_withdrawal_tip_seven.setText("7." + getResources().getString(R.string.text_withdrawal_tip_seven));
        text_withdrawal_tip_eight.setText("8." + getResources().getString(R.string.text_withdrawal_tip_eight));


        img_eye.setOnClickListener(this);
        img_eye_transfer.setOnClickListener(this);

        text_getCode.setOnClickListener(this);
        text_getCode_transfer.setOnClickListener(this);

        btn_submit.setOnClickListener(this);
        btn_submit_transfer.setOnClickListener(this);

        layout_address.setOnClickListener(this);

        withdrawalAddressAdapter = new WithdrawalAddressAdapter(getActivity());
        edit_amount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String value_amount = edit_amount.getText().toString();
                if (value_amount.length() != 0) {
                    if (Double.parseDouble(value_amount) < 50) {
                        edit_amount.setText("50");
                    }
                }
            }
        });

        edit_amount_transfer.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String value_amount = edit_amount_transfer.getText().toString();
                if (value_amount.length() != 0) {
                    if (Double.parseDouble(value_amount) < 10) {
                        edit_amount_transfer.setText("10");
                    }
                }
            }
        });

        Util.setThreeUnClick(edit_amount, edit_pass_withdraw, edit_code, btn_submit);
        Util.setThreeUnClick(edit_pass_withdraw, edit_amount, edit_code, btn_submit);
        Util.setThreeUnClick(edit_pass_withdraw, edit_amount, edit_code, btn_submit);
        Util.setThreeUnClick(edit_code, edit_pass_withdraw, edit_amount, btn_submit);


        Util.setFourUnClick(edit_amount_transfer, edit_pass_transfer, edit_username, edit_code_transfer, btn_submit_transfer);
        Util.setFourUnClick(edit_pass_transfer, edit_username, edit_code_transfer, edit_amount_transfer, btn_submit_transfer);
        Util.setFourUnClick(edit_username, edit_code_transfer, edit_pass_transfer, edit_amount_transfer, btn_submit_transfer);
        Util.setFourUnClick(edit_code_transfer, edit_username, edit_pass_transfer, edit_amount_transfer, btn_submit_transfer);

        img_record.setVisibility(View.VISIBLE);
        img_record.setOnClickListener(this);


        currencyChainAdapter = new CurrencyChainAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(currencyChainAdapter);


        currencyChainAdapter.setEnable(true);

        currencyChainAdapter.setOnItemClick((position, data) -> {
            currencyChainAdapter.select(data.getChain());
            recyclerView.setAdapter(currencyChainAdapter);
            chain = data.getChain();
        });


        withdrawHistoryAdapter = new WithdrawHistoryAdapter(getActivity());



        /* account = loginEntity.getUser().getPrincipal();*/
      /*  if (account.contains("@")) {
            edit_code.setHint(getResources().getString(R.string.text_email_code));
        } else {
            edit_code.setHint(getResources().getString(R.string.text_mobile_code));
        }*/

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_withdrawal;
    }

    @Override
    protected void intPresenter() {

    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {

        UserDetailEntity userDetailEntity = SPUtils.getData(AppConfig.DETAIL, UserDetailEntity.class);

        if (userDetailEntity == null) {
            NetManger.getInstance().userDetail((state, response) -> {
                if (state.equals(SUCCESS)) {
                    UserDetailEntity userDetailEntity2 = (UserDetailEntity) response;
                    SPUtils.putData(AppConfig.DETAIL, userDetailEntity2);
                }
            });
        } else {
            user = userDetailEntity.getUser();
        }


        unionRateEntity = SPUtils.getData(AppConfig.KEY_UNION, UnionRateEntity.class);
        Log.d("print", "initData:513:  " + unionRateEntity);
        if (unionRateEntity == null) {
            NetManger.getInstance().unionRate((state, response) -> {
                if (state.equals(SUCCESS)) {
                    unionRateEntity = (UnionRateEntity) response;
                    //退出需要清除
                    SPUtils.putData(AppConfig.KEY_UNION, unionRateEntity);
                }
            });
        } else {
            if (unionRateEntity.getUnion() != null) {
                //内部转账都打开
               /* if (TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) > 5) {
                    text_transfer_title.setVisibility(View.VISIBLE);
                    text_title.setTextColor(getResources().getColor(R.color.maincolor));
                } else {
                    text_transfer_title.setVisibility(View.GONE);

                }*/
            }
        }


        //查看当前有记录

        getWithdrawalHistory((state, response) -> {
            if (state.equals(SUCCESS)) {
                depositWithdrawEntity = (DepositWithdrawEntity) response;
                Log.d("print", "initData: " + depositWithdrawEntity);
                List<DepositWithdrawEntity.DataBean> data = depositWithdrawEntity.getData();
                Iterator<DepositWithdrawEntity.DataBean> iterator = data.iterator();
                while (iterator.hasNext()) {
                    DepositWithdrawEntity.DataBean value = iterator.next();
                    long createTime = value.getCreateTime();
                    long time = System.currentTimeMillis();
                    long l = time - createTime;
                    if (l < TradeUtil.tenMin) {
                        value.setUseTime(TradeUtil.tenMin - l);
                    } else {
                        value.setUseTime(0);
                    }

                    if (value.getStatus() >= 1) {
                        iterator.remove();
                    }
                }


                if (data.size() == 0) {
                    text_record.setVisibility(View.GONE);
                } else {
                    text_record.setVisibility(View.VISIBLE);
                    text_record.setText(data.size() + getString(R.string.text_withdrawal_history));
                }
            }
        });


        text_balance.setText(String.valueOf(BalanceManger.getInstance().getBalanceReal()) + getResources().getString(R.string.text_usdt));
        text_balance_transfer.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));

        //获取可以支持的币种
        getWithdrawCurrency();
        //当前提币的详情
        getDetailCurrency(false, false, "USDT");


    }

    private void getDetailCurrency(boolean isAddress, boolean isTransfer, String currency) {

        NetManger.getInstance().currencyDetail(currency, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                CurrencyDetailEntity currencyDetailEntity = (CurrencyDetailEntity) response;

                if (isTransfer) {
                    text_transfer_fee.setText(TradeUtil.justDisplay(currencyDetailEntity.getData().get(0).getTransferMin()) + "~" +
                            TradeUtil.justDisplay(currencyDetailEntity.getData().get(0).getTransferMax()) + " " + "USDT");
                } else {
                    currencyChainAdapter.setDatas(currencyDetailEntity.getData());

                    if (!isAddress) {
                        currencyChainAdapter.select(currencyDetailEntity.getData().get(0).getChain());
                    }
                    Double withdrawMin = currencyDetailEntity.getData().get(0).getWithdrawMin();
                    Double withdrawDay = currencyDetailEntity.getData().get(0).getWithdrawDay();

                    text_range.setText(withdrawMin + "~" + currencyDetailEntity.getData().get(0).getWithdrawMax() + " " + currency);
                    text_fee.setText(currencyDetailEntity.getData().get(0).getWithdrawFee() + " " + currency);

                    String format = String.format(getString(R.string.text_withdrawal_tip_one), withdrawMin + " " + currency);

                    text_withdrawal_tip_one.setText("1." + format);


                    text_withdrawal_tip_three.setText("2." + String.format(getString(R.string.text_withdrawal_tip_three), withdrawDay + " " + currency));

                    Log.d("print", "onNetResult:444: " + currencyDetailEntity);
                    currencyChainAdapter.setOnItemClick((position, dataBean) -> {
                        currencyChainAdapter.select(dataBean.getChain());
                        for (CurrencyDetailEntity.DataBean detailEntity : currencyDetailEntity.getData()) {
                            if (dataBean.getChain().equals(detailEntity.getChain())) {
                                text_range.setText(detailEntity.getWithdrawMin() + "~" + detailEntity.getWithdrawMax() + " " + currency);
                                text_fee.setText(detailEntity.getWithdrawFee() + " " + currency);

                            }
                        }
                    });
                }


            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });
    }

    private void getWithdrawCurrency() {
        //查看可以支持提币的币种
        NetManger.getInstance().withdrawalCurrencyList((state, response) -> {
            if (state.equals(SUCCESS)) {
                withdrawCurrencyEntity = (WithdrawCurrencyEntity) response;
            }
        });

    }


    private void getWithdrawalHistory(OnNetResult onNetResult) {
        NetManger.getInstance().depositWithdraw("200", "false", "1", null, "USDT", null,
                null, "1", null, (state, response) -> {
                    if (state.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response);
                    }
                });
    }


    /*提币明细弹窗*/
    private void showWithdrawalPopWindow() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_withdrawal_history, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_withdrawal_history_detail);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        Util.colorSwipe(getActivity(), swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getWithdrawalHistory((state, response) -> {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    depositWithdrawEntity = (DepositWithdrawEntity) response;
                    List<DepositWithdrawEntity.DataBean> data = depositWithdrawEntity.getData();
                    Iterator<DepositWithdrawEntity.DataBean> iterator = data.iterator();
                    while (iterator.hasNext()) {
                        DepositWithdrawEntity.DataBean value = iterator.next();
                        long createTime = value.getCreateTime();
                        long time = System.currentTimeMillis();
                        long l = time - createTime;
                        if (l < TradeUtil.tenMin) {
                            value.setUseTime(TradeUtil.tenMin - l);
                        } else {
                            value.setUseTime(0);
                        }
                        if (value.getStatus() >= 1) {
                            iterator.remove();
                        }
                    }
                    if (data.size() == 0) {
                        text_record.setVisibility(View.GONE);
                    } else {
                        text_record.setVisibility(View.VISIBLE);
                        text_record.setText(data.size() + getString(R.string.text_withdrawal_history));
                    }
                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            });


        });

        RecyclerView recyclerView_history = view.findViewById(R.id.recyclerView_history);
        recyclerView_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_history.setAdapter(withdrawHistoryAdapter);
        //  防止更新item时防止抖动
        ((SimpleItemAnimator) recyclerView_history.getItemAnimator()).setSupportsChangeAnimations(false);
        if (depositWithdrawEntity != null) {
            withdrawHistoryAdapter.setDatas(depositWithdrawEntity.getData());
        }

        withdrawHistoryAdapter.setOnCancelItemClick(data -> {

            runOnUiThread(() -> {
                NetManger.getInstance().cancelWithdrawal(data.getId(), "cancel", (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        withdrawHistoryAdapter.cancel(true);
                        initData();
                        Util.lightOff(getActivity());
                        PopUtil.getInstance().showTip(getActivity(),
                                layout_view,
                                false,
                                getString(R.string.text_cancel_success), state1 -> {

                                });
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
            });

        });

        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(layout_address, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //view.startAnimation(animation);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;

            case R.id.img_record:

                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.text_record:

                showWithdrawalPopWindow();

                break;
            case R.id.text_address:
            case R.id.text_address_manage:
                if (isLogin()) {
                    showAddressPopWindow(withdrawalAdressEntity);

                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
            case R.id.img_eye:

                Util.setEye(getActivity(), edit_pass_withdraw, img_eye);
                break;
            case R.id.img_eye_transfer:
                Util.setEye(getActivity(), edit_pass_transfer, img_eye_transfer);
                break;
            case R.id.text_getCode:
                NetManger.getInstance().getEmailCode(getActivity(), layout_view, email, "CREATE_WITHDRAW", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.obtainMessage(0).sendToTarget();
                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });

               /* if (account.contains("@")) {
                    NetManger.getInstance().getEmailCode(email, "CREATE_WITHDRAW", (state, response1, response2) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response2;
                            if (tipEntity.getCode() == 200) {
                                mHandler.obtainMessage(0).sendToTarget();
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
                                mHandler.obtainMessage(0).sendToTarget();
                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }*/
                break;
            case R.id.text_getCode_transfer:

                NetManger.getInstance().getEmailCode(getActivity(), layout_view, email, "CREATE_WITHDRAW", (state, response1, response2) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response2;
                        if (tipEntity.getCode() == 200) {
                            mHandler.obtainMessage(1).sendToTarget();
                        } else if (tipEntity.getCode() == 500) {
                            Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


               /* if (account.contains("@")) {
                    NetManger.getInstance().getEmailCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", (state, response1, response2) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response2;
                            if (tipEntity.getCode() == 200) {
                                mHandler.obtainMessage(1).sendToTarget();
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
                                mHandler.obtainMessage(1).sendToTarget();

                            } else if (tipEntity.getCode() == 500) {
                                Toast.makeText(getContext(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }*/
                break;
            case R.id.btn_submit:
                String value_amount = edit_amount.getText().toString();
                String value_pass = edit_pass_withdraw.getText().toString();
                String value_code = edit_code.getText().toString();
                String value_address = text_address.getText().toString();

                NetManger.getInstance().checkEmailCode(email, "CREATE_WITHDRAW", value_code, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                        if (tipEntity.isCheck() == true) {
                            withdrawal(value_amount, user.getCurrency(),
                                    chain, value_address, email, value_pass);

                        } else {
                            Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


                /*if (account.contains("@")) {

                    NetManger.getInstance().checkEmailCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", value_code, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                            if (tipEntity.isCheck() == true) {
                                withdrawal(value_amount, user.getCurrency(),
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
                                withdrawal(value_amount, user.getCurrency(),
                                        chain, addressId, account, value_pass);
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }*/

                break;
            case R.id.btn_submit_transfer:
                String value_amount_transfer = edit_amount_transfer.getText().toString();
                String value_pass_transfer = edit_pass_transfer.getText().toString();
                String value_code_transfer = edit_code_transfer.getText().toString();
                String user_name = edit_username.getText().toString();

                NetManger.getInstance().checkEmailCode(email, "CREATE_WITHDRAW", value_code_transfer, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                        if (tipEntity.isCheck() == true) {
                            transfer(user.getCurrency(), value_amount_transfer, value_pass_transfer, user_name, loginEntity.getUser().getAccount());
                        } else {
                            Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });


               /* if (account.contains("@")) {

                    NetManger.getInstance().checkEmailCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", value_code_transfer, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                            if (tipEntity.isCheck() == true) {
                                transfer(user.getCurrency(), value_amount_transfer, value_pass_transfer, user_name, loginEntity.getUser().getAccount());
                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });

                } else {

                    NetManger.getInstance().checkMobileCode(loginEntity.getUser().getAccount(), "CREATE_WITHDRAW", value_code_transfer, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            TipEntity tipEntity = (TipEntity) response;
                            if (tipEntity.isCheck() == true) {
                                transfer(user.getCurrency(), value_amount_transfer, value_pass_transfer, user_name, loginEntity.getUser().getAccount());

                            } else {
                                Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                        }
                    });
                }*/

                break;

            case R.id.text_title:
                layout_withdrawal.setVisibility(View.VISIBLE);
                layout_transfer.setVisibility(View.GONE);
                text_title.setTextColor(getResources().getColor(R.color.maincolor));
                text_transfer_title.setTextColor(getResources().getColor(R.color.text_second_color));


                break;
            case R.id.text_transfer_title:
                layout_withdrawal.setVisibility(View.GONE);
                layout_transfer.setVisibility(View.VISIBLE);
                text_title.setTextColor(getResources().getColor(R.color.text_second_color));
                text_transfer_title.setTextColor(getResources().getColor(R.color.maincolor));
                break;
            case R.id.layout_withdrawal_pop:
                if (withdrawCurrencyEntity != null) {
                    showWithdrawCurrencyPopWindow(false, withdrawCurrencyEntity);
                } else {
                    getWithdrawCurrency();
                }
                break;
            case R.id.layout_withdrawal_transfer:
                if (withdrawCurrencyEntity != null) {
                    showWithdrawCurrencyPopWindow(true, withdrawCurrencyEntity);
                } else {
                    getWithdrawCurrency();
                }
                break;
        }
    }

    private void showAddressPopWindow(WithdrawalAdressEntity withdrawalAdressEntity2) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_withdrawal_address, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(getResources().getString(R.string.text_withdrawal_address));
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        swipeRefreshLayout.setOnRefreshListener(() -> {

            NetManger.getInstance().withdrawalAddressList((state, response) -> {
                refreshAddress((state1, response1) -> {
                    if (state1.equals(BUSY)) {
                        swipeRefreshLayout.setRefreshing(true);
                    } else if (state1.equals(SUCCESS)) {
                        swipeRefreshLayout.setRefreshing(false);
                        withdrawalAdressEntity = (WithdrawalAdressEntity) response1;
                        if (withdrawalAdressEntity != null) {
                            withdrawalAddressAdapter.setDatas(withdrawalAdressEntity.getData());
                        }
                    } else if (state1.equals(FAILURE)) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


            });
        });

        RecyclerView recyclerView_address = view.findViewById(R.id.recyclerView_address);
        recyclerView_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_address.setAdapter(withdrawalAddressAdapter);
        if (withdrawalAdressEntity2 != null) {
            withdrawalAddressAdapter.setDatas(withdrawalAdressEntity2.getData());
        }

        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_ADD_ADDRESS);

        });

        withdrawalAddressAdapter.setOnEditClick(data -> {
            Util.lightOff(getActivity());
            showEditPopWindow(data);
        });

        withdrawalAddressAdapter.setCopyClick(data -> {
            Util.copy(getActivity(), data.getAddress());
            Toast.makeText(getActivity(), R.string.text_copied, Toast.LENGTH_SHORT).show();

        });


        withdrawalAddressAdapter.setOnItemClick(data -> {
            text_address.setText(data.getAddress());
            addressId = data.getId();
            popupWindow.dismiss();
            chain = data.getChain();
            currencyChainAdapter.select(chain);
            recyclerView.setAdapter(currencyChainAdapter);
            //获取链细节
            getDetailCurrency(true, false, data.getCurrency());
            text_withdraw_currency.setText(data.getCurrency());
            ChartUtil.setIcon(data.getCurrency(), img_bg);
            BalanceManger.getInstance().getBalance(data.getCurrency(), response -> {
                BalanceEntity.DataBean data1 = (BalanceEntity.DataBean) response;
                TradeUtil.getScale(data1.getCurrency(), response2 -> {
                    int scale = (int) response2;
                    double money = data1.getMoney();
                    text_balance.setText(TradeUtil.numberHalfUp(money, scale) + " " + data1.getCurrency());
                    text_balance_transfer.setText(TradeUtil.numberHalfUp(money, scale) + " " + data1.getCurrency());
                });


            });


        });

        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(layout_address, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //view.startAnimation(animation);

    }


    private void refreshAddress(OnNetResult onNetResult) {
        NetManger.getInstance().withdrawalAddressList((state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                withdrawalAdressEntity = (WithdrawalAdressEntity) response;
                onNetResult.onNetResult(SUCCESS, withdrawalAdressEntity);
                if (withdrawalAdressEntity != null) {
                    withdrawalAddressAdapter.setDatas(withdrawalAdressEntity.getData());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*显示删除的按钮*/
    private void showEditPopWindow(WithdrawalAdressEntity.DataBean data) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_edit_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.findViewById(R.id.text_delete).setOnClickListener(v -> {
            NetManger.getInstance().deleteAddress(data.getId(), (state, response) -> {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    popupWindow.dismiss();

                    refreshAddress((state1, response1) -> {

                    });

                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            });
        });

        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(getActivity(), popupWindow);
        Util.isShowing(getActivity(), popupWindow);


        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);

    }

    private void withdrawal(String money, String currency, String chain, String address, String email, String password) {
        NetManger.getInstance().withdrawal(money, currency, chain, address, email, password, (state, response) -> {
            if (state.equals(SUCCESS)) {
                initData();
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.text_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();

                }
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
                    SmsTimeUtils.check(SmsTimeUtils.WITHDRAWAL, false);
                    SmsTimeUtils.startCountdown(text_getCode);
                    break;
                case 1:
                    SmsTimeUtils.check(SmsTimeUtils.TRANSFER, false);
                    SmsTimeUtils.startCountdown(text_getCode_transfer);
                    break;
                default:
                    break;
            }
        }

    };

    private void transfer(String currency, String money, String pass, String subName, String account) {
        NetManger.getInstance().transfer(currency, money, pass, subName, account, (state, response) -> {
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = (TipEntity) response;
                if (tipEntity.getMessage().equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.text_tip_success), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private WithdrawCurrencyAdapter withdrawCurrencyAdapter;
    private CurrencyHistoryAdapter currencyHistoryAdapter;
    private Set<String> historyList;

    private void showWithdrawCurrencyPopWindow(boolean isTransfer, WithdrawCurrencyEntity withdrawCurrencyEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_withdrawal_currency, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        RecyclerView recyclerView_withdraw_currency = view.findViewById(R.id.recyclerView_withdraw_currency);

        LinearLayout layout_null_currency = view.findViewById(R.id.layout_currency_null);
        //列表
        withdrawCurrencyAdapter = new WithdrawCurrencyAdapter(getActivity());
        recyclerView_withdraw_currency.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_withdraw_currency.setAdapter(withdrawCurrencyAdapter);
        withdrawCurrencyAdapter.setDatas(withdrawCurrencyEntity.getData());
        layout_null_currency.setVisibility(View.GONE);

        EditText edit_search_currency = view.findViewById(R.id.edit_search_currency);

        LinearLayout layout_history_content = view.findViewById(R.id.layout_history_content);
        //历史记录

        currencyHistoryAdapter = new CurrencyHistoryAdapter(getActivity());
        RecyclerView recyclerView_history_search = view.findViewById(R.id.recyclerView_history_search);
        recyclerView_history_search.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView_history_search.setAdapter(currencyHistoryAdapter);
        historyList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_WITHDRAW_CURRENCY_HISTORY, null));
        List<String> list = new ArrayList<>(historyList);
        Log.d("print", "showWithdrawCurrencyPopWindow:历史记录: " + list);
        if (list.size() != 0) {
            currencyHistoryAdapter.setDatas(list);
        } else {
            layout_history_content.setVisibility(View.GONE);
        }

        currencyHistoryAdapter.setOnItemClick(new CurrencyHistoryAdapter.OnItemClick() {
            @Override
            public void onSuccessListener(Integer position, String data) {
                List<String> searchList = TradeUtil.searchCurrencyList(data, withdrawCurrencyEntity.getData());
                withdrawCurrencyAdapter.setDatas(searchList);
            }
        });

        edit_search_currency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    layout_history_content.setVisibility(View.GONE);
                    List<String> searchList = TradeUtil.searchCurrencyList(s.toString(), withdrawCurrencyEntity.getData());
                    withdrawCurrencyAdapter.setDatas(searchList);
                } else {
                    historyList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_WITHDRAW_CURRENCY_HISTORY, null));
                    List<String> list = new ArrayList<>(historyList);
                    if (list.size() != 0) {
                        layout_history_content.setVisibility(View.VISIBLE);
                        currencyHistoryAdapter.setDatas(list);
                    } else {
                        layout_history_content.setVisibility(View.GONE);

                    }
                    withdrawCurrencyAdapter.setDatas(withdrawCurrencyEntity.getData());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        withdrawCurrencyAdapter.setDetailClick(data -> {
            historyList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_WITHDRAW_CURRENCY_HISTORY, null));
            if (historyList.size() != 0) {
                Util.isOptional(data, historyList, response -> {
                    boolean isOptional = (boolean) response;
                    if (!isOptional) {
                        historyList.add(data);
                    }
                });
            } else {
                historyList.add(data);
            }
            SPUtils.putString(AppConfig.KEY_WITHDRAW_CURRENCY_HISTORY, Util.SPDeal(historyList));
            //获取链细节
            if (isTransfer) {
                getDetailCurrency(false, true, data);
                text_withdraw_currency_transfer.setText(data);
                ChartUtil.setIcon(data, img_bg_transfer);

            } else {
                getDetailCurrency(false, false, data);
                text_withdraw_currency.setText(data);
                ChartUtil.setIcon(data, img_bg);
            }


            BalanceManger.getInstance().getBalance(data, response -> {
                BalanceEntity.DataBean data1 = (BalanceEntity.DataBean) response;
                TradeUtil.getScale(data1.getCurrency(), response2 -> {
                    int scale = (int) response2;
                    double money = data1.getMoney();
                    if (isTransfer) {
                        text_balance_transfer.setText(TradeUtil.numberHalfUp(money, scale) + " " + data1.getCurrency());
                    } else {
                        text_balance.setText(TradeUtil.numberHalfUp(money, scale) + " " + data1.getCurrency());
                        text_address.setText(data1.getCurrency() + getResources().getString(R.string.text_usdt_address));
                    }
                });


            });


            popupWindow.dismiss();
        });


        view.findViewById(R.id.img_clear_history).setOnClickListener(v -> {
            SPUtils.remove(AppConfig.KEY_WITHDRAW_CURRENCY_HISTORY);
            layout_history_content.setVisibility(View.GONE);
        });

        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        List<String> data = withdrawCurrencyEntity.getData();
        view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout_currency = view.findViewById(R.id.swipeRefreshLayout_currency);
        Util.colorSwipe(getActivity(), swipeRefreshLayout_currency);

        swipeRefreshLayout_currency.setOnRefreshListener(() -> {
            swipeRefreshLayout_currency.setRefreshing(false);
            getWithdrawCurrency();
        });

        popupWindow.showAsDropDown(layout_address, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //view.startAnimation(animation);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        withdrawHistoryAdapter.cancelTimer();


    }


}
