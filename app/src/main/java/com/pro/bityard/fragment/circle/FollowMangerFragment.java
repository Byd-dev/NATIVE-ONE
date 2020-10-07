package com.pro.bityard.fragment.circle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.CopyMangerAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CopyMangerEntity;
import com.pro.bityard.entity.StatEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.view.HeaderRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.FIRST;
import static com.pro.bityard.config.AppConfig.LOAD;

public class FollowMangerFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    private TextView text_copy_total_amount;
    TextView text_copy_trade_profit;
    TextView stay_copy_total_amount;
    TextView stay_copy_trade_profit;

    @BindView(R.id.recyclerView_traders)
    HeaderRecyclerView recyclerView_traders;

    private CopyMangerAdapter copyMangerAdapter;

    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.swipeRefreshLayout_traders)
    SwipeRefreshLayout swipeRefreshLayout_traders;

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onLazyLoad() {
    }

    private int page = 1;

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_copy_trade_settings));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_head_follow_manger, null);

        text_copy_total_amount = headView.findViewById(R.id.text_copy_total_amount);
        text_copy_trade_profit = headView.findViewById(R.id.text_copy_trade_profit);

        stay_copy_total_amount = headView.findViewById(R.id.stay_copy_total_amount);
        stay_copy_total_amount.setText(getResources().getString(R.string.text_copy_total_amount) + "(USDT)");
        stay_copy_trade_profit = headView.findViewById(R.id.stay_copy_trade_profit);
        stay_copy_trade_profit.setText(getResources().getString(R.string.text_copy_trade_profit) + "(USDT)");

        recyclerView_traders.addHeaderView(headView);

        copyMangerAdapter = new CopyMangerAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_traders.setLayoutManager(linearLayoutManager);
        recyclerView_traders.setAdapter(copyMangerAdapter);
        swipeRefreshLayout_traders.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        swipeRefreshLayout_traders.setOnRefreshListener(() -> {
            page = 1;
            getData();
        });

        recyclerView_traders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout_traders.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == copyMangerAdapter.getItemCount() - 1) {
                    copyMangerAdapter.startLoad();
                    page = page + 1;
                    getFollowTradersList(LOAD, page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        copyMangerAdapter.setOnFollowClick(dataBean -> {
            UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_CIRCLE_EDIT_FOLLOW, dataBean);
        });

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_follow_manger;
    }

    @Override
    protected void intPresenter() {

    }

    private void getData(){
        NetManger.getInstance().followerStat((state, response) -> {
            if (state.equals(SUCCESS)) {
                StatEntity statEntity = (StatEntity) response;

                text_copy_total_amount.setText(String.valueOf(statEntity.getSumMargin()));
                text_copy_trade_profit.setText(TradeUtil.getNumberFormat(statEntity.getSumIncome(), 2));
            }
        });
        page = 1;
        getFollowTradersList(FIRST, page);
    }

    @Override
    protected void initData() {



    }


    private void getFollowTradersList(String type, int page) {

        NetManger.getInstance().followerTraders(String.valueOf(page), "10", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout_traders.setRefreshing(false);

                CopyMangerEntity copyMangerEntity = (CopyMangerEntity) response;
                if (type.equals(LOAD)) {
                    copyMangerAdapter.addDatas(copyMangerEntity.getData());
                } else {
                    copyMangerAdapter.setDatas(copyMangerEntity.getData());
                }
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout_traders.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
        }
    }
}
