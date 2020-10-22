package com.pro.bityard.fragment.circle;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.IncomeListAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowerIncomeEntity;
import com.pro.bityard.entity.FollowerIncomeListEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
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

public class FollowerMangerFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_title)
    TextView text_title;



    @BindView(R.id.recyclerView_traders)
    HeaderRecyclerView recyclerView_traders;

    private IncomeListAdapter incomeListAdapter;

    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.swipeRefreshLayout_traders)
    SwipeRefreshLayout swipeRefreshLayout_traders;
    private TextView text_day_profit;
    private TextView text_week_profit;
    private TextView text_all_profit;
    private TextView text_rate;
    private TextView text_content;

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        getFollowIncomeList(FIRST, page);
    }

    @Override
    protected void onLazyLoad() {
    }

    private int page = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_being_copied);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_head_follower_manger, null);

        text_content = headView.findViewById(R.id.text_content);

        text_day_profit = headView.findViewById(R.id.text_day_profit);
        text_week_profit = headView.findViewById(R.id.text_week_profit);
        text_all_profit = headView.findViewById(R.id.text_all_profit);
        text_rate = headView.findViewById(R.id.text_rate);

        Switch btn_switch = headView.findViewById(R.id.btn_switch);
        btn_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String active;
            if (isChecked) {
                active = "true";
            } else {
                active = "false";
            }
            NetManger.getInstance().followerSwitch(active, (state, response) -> {
                if (state.equals(SUCCESS)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_tip_success), Toast.LENGTH_SHORT).show();
                }
            });

        });


        recyclerView_traders.addHeaderView(headView);

        incomeListAdapter = new IncomeListAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_traders.setLayoutManager(linearLayoutManager);
        recyclerView_traders.setAdapter(incomeListAdapter);
        Util.colorSwipe(getActivity(), swipeRefreshLayout_traders);
        swipeRefreshLayout_traders.setOnRefreshListener(() -> {
            page = 1;
            getData();
        });

        recyclerView_traders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout_traders.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == incomeListAdapter.getItemCount() - 1) {
                    incomeListAdapter.startLoad();
                    page = page + 1;
                    getFollowIncomeList(LOAD, page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_follower_manger;
    }

    @Override
    protected void intPresenter() {

    }

    private void getData() {
        NetManger.getInstance().followerIncome((state, response) -> {
            if (state.equals(SUCCESS)) {
                FollowerIncomeEntity followerIncomeEntity = (FollowerIncomeEntity) response;

                text_day_profit.setText(String.valueOf(followerIncomeEntity.getIncomeDay()));
                text_week_profit.setText(String.valueOf(followerIncomeEntity.getIncomeWeek()));
                text_all_profit.setText(String.valueOf(followerIncomeEntity.getIncomeAll()));

                String format1 = String.format(getResources().getString(R.string.text_follower_tip), followerIncomeEntity.getFollower(), followerIncomeEntity.getVolume());
                text_content.setText(Html.fromHtml(format1));


                double ratio = followerIncomeEntity.getRatio();
                String string = getResources().getString(R.string.text_yours_profit);
                @SuppressLint("StringFormatMatches") String format2 = String.format(string, TradeUtil.mul(ratio, 100));
                text_rate.setText(format2 + "%");

                text_rate.setOnClickListener(v -> {
                    Util.lightOff(getActivity());
                    String text_ratio_tip = getString(R.string.text_yours_profit);
                    @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String format = String.format(text_ratio_tip, (ratio + "%"));
                    PopUtil.getInstance().showTip(getActivity(), layout_view, false, format,
                            state2 -> {

                            });
                });


            }
        });
        page = 1;
        getFollowIncomeList(FIRST, page);
    }

    @Override
    protected void initData() {
        getData();

    }


    private void getFollowIncomeList(String type, int page) {

        NetManger.getInstance().followerIncomeList(String.valueOf(page), "10", (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                if (isAdded()) {
                    swipeRefreshLayout_traders.setRefreshing(false);
                }
                FollowerIncomeListEntity copyMangerEntity = (FollowerIncomeListEntity) response;
                if (type.equals(LOAD)) {
                    incomeListAdapter.addDatas(copyMangerEntity.getData());
                } else {
                    incomeListAdapter.setDatas(copyMangerEntity.getData());
                }
            } else if (state.equals(FAILURE)) {
                if (isAdded()) {
                    swipeRefreshLayout_traders.setRefreshing(false);
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

        }
    }
}
