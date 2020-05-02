package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.InviteRecordAdapter;
import com.pro.bityard.api.Gt3Util;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.InviteEntity;
import com.pro.bityard.entity.InviteListEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.UnionRateEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.SmsTimeUtils;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class InviteFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.layout_null)
    LinearLayout layout_null;

    @BindView(R.id.text_commission)
    TextView text_commission;
    @BindView(R.id.text_commission_transfer)
    TextView text_commission_transfer;
    @BindView(R.id.text_all_referrals)
    TextView text_all_referrals;
    @BindView(R.id.text_number_trader)
    TextView text_number_trader;
    @BindView(R.id.text_new_invited)
    TextView text_new_invited;
    @BindView(R.id.text_total_volume)
    TextView text_total_volume;
    @BindView(R.id.text_total_orders)
    TextView text_total_orders;
    @BindView(R.id.text_commission_rate)
    TextView text_commission_rate;
    @BindView(R.id.text_url)
    TextView text_url;

    @BindView(R.id.edit_search)
    EditText edit_search;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private InviteRecordAdapter inviteRecordAdapter;
    private LoginEntity loginEntity;

    private int page = 1;
    private String FIRST = "first";
    private String REFRESH = "refresh";
    private String LOAD = "load";
    private UnionRateEntity unionRateEntity;
    private TextView text_balance;
    private TextView text_getCode;
    private UserDetailEntity userDetailEntity;
    private PopupWindow popupWindow;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity != null) {
            text_url.setText("http://app.bityard.com/?ru=" + loginEntity.getUser().getRefer());
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化G3util
        Gt3Util.getInstance().init(getContext());
    }

    @Override
    protected void initView(View view) {

        UserDetailManger.getInstance().addObserver(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        swipeRefreshLayout.setOnRefreshListener(() -> initData());
        text_title.setText(R.string.text_affiliate_stats);

        view.findViewById(R.id.img_back).setOnClickListener(this);
        text_url.setOnClickListener(this);

        inviteRecordAdapter = new InviteRecordAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(inviteRecordAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == inviteRecordAdapter.getItemCount() - 1) {
                    inviteRecordAdapter.startLoad();
                    page = page + 1;
                    getInviteList(LOAD, page, null);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        inviteRecordAdapter.setOnItemClick(data -> {
            showDetailPopWindow(data);
        });


        view.findViewById(R.id.img_search).setOnClickListener(this);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    page = 1;
                    getInviteList(FIRST, page, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    /*显示详情*/
    private void showDetailPopWindow(InviteListEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_invite_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_referrals_detail);

        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(dataBean.getUsername());
        TextView text_commission = view.findViewById(R.id.text_commission);
        text_commission.setText(String.valueOf(dataBean.getCommission()));
        TextView text_today_volume = view.findViewById(R.id.text_today_volume);
        text_today_volume.setText(String.valueOf(dataBean.getTradeVolumeDay()));
        TextView text_today_fee = view.findViewById(R.id.text_today_fee);
        text_today_fee.setText(String.valueOf(dataBean.getTradeChargeDay()));
        TextView text_total_volume = view.findViewById(R.id.text_total_volume);
        text_total_volume.setText(String.valueOf(dataBean.getTradeAmount()));
        TextView text_total_orders = view.findViewById(R.id.text_total_orders);
        text_total_orders.setText(String.valueOf(dataBean.getTradeCount()));


        TextView text_register_time = view.findViewById(R.id.text_register_time);
        text_register_time.setText(ChartUtil.getDate(dataBean.getRegisterTime()));
        TextView text_last_time = view.findViewById(R.id.text_last_time);
        text_last_time.setText(ChartUtil.getDate(dataBean.getLoginTime()));

        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });

        Button btn_submit = view.findViewById(R.id.btn_submit);

        if (TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) > 5) {
            btn_submit.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.GONE);

        }

        /*转账监听*/
        btn_submit.setOnClickListener(v -> {
            showTransferPopWindow(dataBean);
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }


    /*内部转账*/
    private void showTransferPopWindow(InviteListEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_transfer_detail_pop, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_internal_funds_transfer);

        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(dataBean.getUsername());

        text_balance = view.findViewById(R.id.text_balance);

        EditText edit_code = view.findViewById(R.id.edit_code);
        String account = loginEntity.getUser().getPrincipal();
        Log.d("print", "showTransferPopWindow:241:  " + account);
        if (account.contains("@")) {
            edit_code.setHint(getResources().getString(R.string.text_email_code));
        } else {
            edit_code.setHint(getResources().getString(R.string.text_mobile_code));
        }


        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });

        Button btn_submit = view.findViewById(R.id.btn_submit);

        if (TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) > 5) {
            btn_submit.setVisibility(View.VISIBLE);
        } else {
            btn_submit.setVisibility(View.GONE);
        }


        EditText edit_amount = view.findViewById(R.id.edit_amount);
        EditText edit_pass = view.findViewById(R.id.edit_pass_withdraw);
        ImageView img_eye = view.findViewById(R.id.img_eye);
        img_eye.setOnClickListener(v -> {
            Util.setEye(getActivity(), edit_pass, img_eye);
        });


        String value_code = edit_code.getText().toString();


        text_getCode = view.findViewById(R.id.text_getCode);

        text_getCode.setOnClickListener(v -> {
            if (account.contains("@")) {
                NetManger.getInstance().getEmailCode(loginEntity.getUser().getAccount(), "CREATE_TRANSFER", (state, response1, response2) -> {
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
                NetManger.getInstance().getMobileCode(loginEntity.getUser().getAccount(), "CREATE_TRANSFER", (state, response1, response2) -> {
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
        });


        Util.setThreeUnClick(edit_amount, edit_pass, edit_code, btn_submit);
        Util.setThreeUnClick(edit_pass, edit_amount, edit_code, btn_submit);
        Util.setThreeUnClick(edit_code, edit_pass, edit_amount, btn_submit);


        /*转账监听*/
        btn_submit.setOnClickListener(v -> {
            String value_amount = edit_amount.getText().toString();
            String value_pass = edit_pass.getText().toString();
            if (account.contains("@")) {

                NetManger.getInstance().checkEmailCode(loginEntity.getUser().getAccount(), "CREATE_TRANSFER", value_code, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        Log.d("print", "showTransferPopWindow:355:  " + tipEntity);
                        if (tipEntity.isCheck() == true) {
                            transfer(userDetailEntity.getUser().getCurrency(), value_amount, value_pass, dataBean.getUsername(), loginEntity.getUser().getAccount());
                        } else {
                            Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });

            } else {

                NetManger.getInstance().checkMobileCode(loginEntity.getUser().getAccount(), "CREATE_TRANSFER", value_code, (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        TipEntity tipEntity = (TipEntity) response;
                        if (tipEntity.isCheck() == true) {
                            transfer(userDetailEntity.getUser().getCurrency(), value_amount, value_pass, dataBean.getUsername(), loginEntity.getUser().getAccount());

                        } else {
                            Toast.makeText(getActivity(), tipEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (state.equals(FAILURE)) {
                        dismissProgressDialog();
                    }
                });
            }

        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);
    }

    private void transfer(String currency, String money, String pass, String subName, String account) {
        NetManger.getInstance().transfer(currency, money, pass, subName, account, (state, response) -> {
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = (TipEntity) response;
                Log.d("print", "transfer:转账结果:  " + response);
                if (tipEntity.getCode() == 200) {
                    popupWindow.dismiss();
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


    private void setUnClick(EditText edit_amount, EditText edit_pass, EditText edit_code, Button btn_submit) {
        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value_pass = edit_pass.getText().toString();
                String value_code = edit_code.getText().toString();
                if (s.length() != 0 && value_pass.length() != 0 && value_code.length() != 0) {
                    btn_submit.setEnabled(true);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_shape_main_color));
                } else {
                    btn_submit.setEnabled(false);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_shape_button));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_invite;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {


        NetManger.getInstance().inviteTopHistory("USDT", (state, response) -> {
            if (state.equals(BUSY)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }

            } else if (state.equals(SUCCESS)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                InviteEntity inviteEntity = (InviteEntity) response;
                if (inviteEntity.getData() == null) {
                    return;
                }

                text_all_referrals.setText(String.valueOf(inviteEntity.getData().getSubCount()));
                text_number_trader.setText(String.valueOf(inviteEntity.getData().getSubTrade()));
                text_new_invited.setText(String.valueOf(inviteEntity.getData().getSubCountNew()));
                text_total_volume.setText(String.valueOf(inviteEntity.getData().getTradeAmount()));
                text_total_orders.setText(String.valueOf(inviteEntity.getData().getTradeCount()));
                double commission = inviteEntity.getData().getCommission();
                text_commission.setText(TradeUtil.getNumberFormat(commission, 2) + "(" + inviteEntity.getData().getCurrency() + ")");
                String string = SPUtils.getString(AppConfig.USD_RATE, null);
                //我的页面 火币结算单位
                String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
                text_commission_transfer.setText("≈" + TradeUtil.getNumberFormat(TradeUtil.mul(commission, Double.parseDouble(string)), 2) + "(" + cny + ")");


            } else if (state.equals(FAILURE)) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        unionRateEntity = SPUtils.getData(AppConfig.KEY_UNION, UnionRateEntity.class);
        Log.d("print", "initData:677:  " + unionRateEntity);
        if (unionRateEntity == null) {
            //获取佣金比例
            NetManger.getInstance().unionRate((state, response) -> {
                if (state.equals(SUCCESS)) {
                    unionRateEntity = (UnionRateEntity) response;
                    //退出需要清除
                    SPUtils.putData(AppConfig.KEY_UNION, unionRateEntity);
                    text_commission_rate.setText(TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) + "%");

                }
            });
        } else {
            if (unionRateEntity.getUnion() != null) {
                text_commission_rate.setText(TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100) + "%");

            }
        }

        page = 1;
        getInviteList(FIRST, page, null);


    }

    private void getInviteList(String type, int page, String content) {

        NetManger.getInstance().inviteListHistory(String.valueOf(page), content, (state, response) -> {
            if (state.equals(BUSY)) {
                if (type.equals(REFRESH)) {
                    showProgressDialog();
                }
            } else if (state.equals(SUCCESS)) {
                if (type.equals(REFRESH)) {
                    dismissProgressDialog();
                }
                InviteListEntity inviteListEntity = (InviteListEntity) response;
                if (inviteListEntity == null) {
                    return;
                }
                if (inviteListEntity.getData().size() == 0) {
                    layout_null.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    layout_null.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (unionRateEntity == null) {
                    return;
                }

                if (type.equals(LOAD)) {
                    if (unionRateEntity.getUnion() != null) {
                        inviteRecordAdapter.addDatas(inviteListEntity.getData(), TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100));
                    }

                } else {
                    if (unionRateEntity.getUnion() != null) {
                        inviteRecordAdapter.setDatas(inviteListEntity.getData(), TradeUtil.mul(unionRateEntity.getUnion().getCommRatio(), 100));
                    }

                }


            } else if (state.equals(FAILURE)) {
                if (type.equals(REFRESH)) {
                    dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.text_url:
                Util.copy(getActivity(), text_url.getText().toString());
                Toast.makeText(getActivity(), R.string.text_copied, Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_search:
                String edit_content = edit_search.getText().toString();
                getInviteList(REFRESH, 1, edit_content);
                break;

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == UserDetailManger.getInstance()) {
            userDetailEntity = (UserDetailEntity) arg;
            if (text_balance != null) {
                if (isAdded()) {
                    if (userDetailEntity != null) {
                        text_balance.setText(getString(R.string.text_available_money) + ":" + TradeUtil.getNumberFormat(userDetailEntity.getUser().getMoney(), 2));
                    }
                }
            }
        }
    }


}
