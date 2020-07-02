package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.pro.bityard.adapter.ChainListAdapter;
import com.pro.bityard.adapter.WithdrawHistoryAdapter;
import com.pro.bityard.adapter.WithdrawalAddressAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UnionRateEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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


    @BindView(R.id.text_transfer_title)
    TextView text_transfer_title;

    @BindView(R.id.layout_withdrawal)
    LinearLayout layout_withdrawal;

    @BindView(R.id.layout_transfer)
    LinearLayout layout_transfer;
    @BindView(R.id.text_record)
    TextView text_record;

    private ChainListAdapter chainListAdapter;//杠杆适配器
    private List<String> dataList;

    private String chain = "OMNI";
    private LoginEntity loginEntity;
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
    private String email;
    private DepositWithdrawEntity depositWithdrawEntity;

    private WithdrawHistoryAdapter withdrawHistoryAdapter;

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
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        text_title.setText(getResources().getString(R.string.text_withdrawal));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.text_address_manage).setOnClickListener(this);
        text_address.setOnClickListener(this);
        text_record.setOnClickListener(this);

        text_title.setOnClickListener(this);
        text_transfer_title.setOnClickListener(this);


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
                if (TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) > 5) {
                    text_transfer_title.setVisibility(View.VISIBLE);
                    text_title.setTextColor(getResources().getColor(R.color.maincolor));
                } else {
                    text_transfer_title.setVisibility(View.GONE);

                }
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


        text_balance.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));
        text_balance_transfer.setText(TradeUtil.getNumberFormat(BalanceManger.getInstance().getBalanceReal(), 2) + " " + getResources().getString(R.string.text_usdt));


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
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
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

                NetManger.getInstance().getEmailCode(email, "CREATE_WITHDRAW", (state, response1, response2) -> {
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
                                    chain, addressId, email, value_pass);

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
                text_transfer_title.setTextColor(getResources().getColor(R.color.text_main_color));


                break;
            case R.id.text_transfer_title:
                layout_withdrawal.setVisibility(View.GONE);
                layout_transfer.setVisibility(View.VISIBLE);
                text_title.setTextColor(getResources().getColor(R.color.text_main_color));
                text_transfer_title.setTextColor(getResources().getColor(R.color.maincolor));
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
            chainListAdapter.select(chain);
            recyclerView.setAdapter(chainListAdapter);


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

    private void withdrawal(String money, String currency, String chain, String addressId, String email, String password) {
        NetManger.getInstance().withdrawal(money, currency, chain, addressId, email, password, (state, response) -> {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        withdrawHistoryAdapter.cancelTimer();
    }
}
