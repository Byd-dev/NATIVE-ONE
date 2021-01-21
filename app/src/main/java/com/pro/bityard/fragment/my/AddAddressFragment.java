package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
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
import com.pro.bityard.activity.WebActivity;
import com.pro.bityard.adapter.ChainListAdapter;
import com.pro.bityard.adapter.CurrencyChainAdapter;
import com.pro.bityard.adapter.CurrencyHistoryAdapter;
import com.pro.bityard.adapter.WithdrawCurrencyAdapter;
import com.pro.bityard.adapter.WithdrawHistoryAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.AddAddressItemEntity;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.CurrencyDetailEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.WithdrawCurrencyEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.img_service)
    ImageView img_service;

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.edit_address)
    EditText edit_address;

    @BindView(R.id.edit_remark)
    EditText edit_remark;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.text_err)
    TextView text_err;
    @BindView(R.id.layout_add_address)
    LinearLayout layout_add_address;
    private CurrencyChainAdapter currencyChainAdapter;//连名称

    private List<String> dataList;
    private WithdrawHistoryAdapter withdrawHistoryAdapter;
    private WithdrawCurrencyEntity withdrawCurrencyEntity;
    private String chain = "OMNI";
    private LoginEntity loginEntity;

    @Override
    public void onResume() {
        super.onResume();
        loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        img_service.setVisibility(View.VISIBLE);
        img_service.setOnClickListener(this);
        text_title.setText(getResources().getString(R.string.text_add_withdrawal_address));

        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.layout_withdrawal_pop).setOnClickListener(this);
        currencyChainAdapter = new CurrencyChainAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(currencyChainAdapter);
        currencyChainAdapter.setEnable(true);
        currencyChainAdapter.setOnItemClick((position, data) -> {
            currencyChainAdapter.select(data.getChain());
            recyclerView.setAdapter(currencyChainAdapter);
            chain = data.getChain();
        });

     /*   edit_address.setHint(getString(R.string.text_enter) + " OMNI " + getString(R.string.text_add_address));
        chainListAdapter.setOnItemClick((position, data) -> {
            String value_address = edit_address.getText().toString();
            chainListAdapter.select(data);
            recyclerView.setAdapter(chainListAdapter);
            edit_address.setHint(getString(R.string.text_enter) + " " + data + " " + getString(R.string.text_add_address));
            chain = data;
            if (value_address.equals("")){
                text_err.setVisibility(View.GONE);
            }else {
                switch (chain) {
                    case "OMNI":
                        if (value_address.startsWith("1")) {
                            text_err.setVisibility(View.GONE);
                        } else if (value_address.startsWith("3")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_omni_err));
                        }
                        break;
                    case "ERC20":
                        if (value_address.startsWith("0")) {
                            text_err.setVisibility(View.GONE);
                        } else if (value_address.startsWith("x")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_erc_err));
                        }
                        break;
                    case "TRC20":
                        if (value_address.startsWith("T")) {
                            text_err.setVisibility(View.GONE);
                        } else {
                            text_err.setVisibility(View.VISIBLE);
                            text_err.setText(getResources().getText(R.string.text_trc_err));
                        }
                        break;
                }
            }



        });*/

        btn_submit.setOnClickListener(this);
        Util.setTwoUnClick(edit_address, edit_remark, btn_submit);
        Util.setTwoUnClick(edit_remark, edit_address, btn_submit);

       /* edit_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    switch (chain) {
                        case "OMNI":
                            if (s.toString().startsWith("1")) {
                                text_err.setVisibility(View.GONE);
                            } else if (s.toString().startsWith("3")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_omni_err));
                            }
                            break;
                        case "ERC20":
                            if (s.toString().startsWith("0")) {
                                text_err.setVisibility(View.GONE);
                            } else if (s.toString().startsWith("x")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_erc_err));
                            }
                            break;
                        case "TRC20":
                            if (s.toString().startsWith("T")) {
                                text_err.setVisibility(View.GONE);
                            } else {
                                text_err.setVisibility(View.VISIBLE);
                                text_err.setText(getResources().getText(R.string.text_trc_err));
                            }
                            break;
                    }
                } else {
                    text_err.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_add_address;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        getWithdrawCurrency();

        getDetailCurrency(false, false, "USDT");

    }
    private void getWithdrawCurrency() {
        //查看可以支持提币的币种
        NetManger.getInstance().withdrawalCurrencyList((state, response) -> {
            if (state.equals(SUCCESS)) {
                withdrawCurrencyEntity = (WithdrawCurrencyEntity) response;
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.layout_withdrawal_pop:
                if (withdrawCurrencyEntity != null) {
                    showWithdrawCurrencyPopWindow( withdrawCurrencyEntity);
                } else {
                    getWithdrawCurrency();
                }
                break;

            case R.id.btn_submit:

                NetManger.getInstance().addAddress("USDT", chain, edit_address.getText().toString(), edit_remark.getText().toString(), (state, response) -> {
                    if (state.equals(BUSY)) {
                        showProgressDialog();
                    } else if (state.equals(SUCCESS)) {
                        dismissProgressDialog();
                        AddAddressItemEntity addAddressItemEntity = (AddAddressItemEntity) response;
                        if (addAddressItemEntity.getCode() == 200) {
                            Toast.makeText(getActivity(), R.string.text_add_success, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    } else if (state.equals(FAILURE)) {
                        if (response != null) {
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        dismissProgressDialog();
                    }
                });
                break;

            case R.id.img_service:
                String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, AppConfig.ZH_SIMPLE);
                String url;
                if (isLogin()) {
                    url = String.format(NetManger.SERVICE_URL, language, loginEntity.getUser().getUserId(), loginEntity.getUser().getAccount());
                } else {
                    url = String.format(NetManger.SERVICE_URL, language, "", "游客");
                }
                WebActivity.getInstance().openUrl(getActivity(), url, getResources().getString(R.string.text_my_service));
                break;

        }
    }

    private WithdrawCurrencyAdapter withdrawCurrencyAdapter;
    private CurrencyHistoryAdapter currencyHistoryAdapter;
    private Set<String> historyList;
    private void showWithdrawCurrencyPopWindow( WithdrawCurrencyEntity withdrawCurrencyEntity) {
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
        if (list.size() != 0) {
            currencyHistoryAdapter.setDatas(list);
        } else {
            layout_history_content.setVisibility(View.GONE);
        }

        currencyHistoryAdapter.setOnItemClick((position, data) -> {
            List<String> searchList = TradeUtil.searchCurrencyList(data, withdrawCurrencyEntity.getData());
            withdrawCurrencyAdapter.setDatas(searchList);
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

            getDetailCurrency(false, false, data);





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

        popupWindow.showAsDropDown(layout_add_address, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //view.startAnimation(animation);

    }

    private void getDetailCurrency(boolean isAddress, boolean isTransfer, String currency) {

        NetManger.getInstance().currencyDetail(currency, (state, response) -> {
            if (state.equals(BUSY)) {
                showProgressDialog();
            } else if (state.equals(SUCCESS)) {
                dismissProgressDialog();
                CurrencyDetailEntity currencyDetailEntity = (CurrencyDetailEntity) response;

                if (isTransfer) {

                } else {
                    currencyChainAdapter.setDatas(currencyDetailEntity.getData());

                    if (!isAddress) {
                        currencyChainAdapter.select(currencyDetailEntity.getData().get(0).getChain());
                    }

                    Log.d("print", "onNetResult:444: " + currencyDetailEntity);
                    currencyChainAdapter.setOnItemClick((position, dataBean) -> {
                        currencyChainAdapter.select(dataBean.getChain());

                    });
                }


            } else if (state.equals(FAILURE)) {
                dismissProgressDialog();
            }
        });
    }
}
