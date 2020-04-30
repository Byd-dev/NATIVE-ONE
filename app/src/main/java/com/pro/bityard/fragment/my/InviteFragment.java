package com.pro.bityard.fragment.my;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.InviteRecordAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.InviteEntity;
import com.pro.bityard.entity.InviteListEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.SPUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class InviteFragment extends BaseFragment implements View.OnClickListener {
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
    protected void initView(View view) {
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
                if (inviteListEntity.getData().size() == 0) {
                    layout_null.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    layout_null.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (type.equals(LOAD)) {
                    inviteRecordAdapter.addDatas(inviteListEntity.getData());
                } else {
                    inviteRecordAdapter.setDatas(inviteListEntity.getData());
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
}
