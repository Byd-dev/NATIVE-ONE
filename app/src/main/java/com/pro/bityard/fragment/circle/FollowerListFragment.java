package com.pro.bityard.fragment.circle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.FollowDetailActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.adapter.StyleAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class FollowerListFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.text_title)
    TextView text_title;




    @BindView(R.id.swipeRefreshLayout_circle)
    SwipeRefreshLayout swipeRefreshLayout_circle;


    @BindView(R.id.recyclerView_circle)
    RecyclerView recyclerView_circle;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;

    private FollowAdapter followAdapter;
    private int page = 1;


    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_follower_list;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        text_title.setText(R.string.text_top_performing_investors);

        view.findViewById(R.id.img_back).setOnClickListener(this);

        followAdapter = new FollowAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView_circle.setLayoutManager(linearLayoutManager);


        recyclerView_circle.setAdapter(followAdapter);


        followAdapter.setWarningClick(() -> {
            Util.lightOff(getActivity());
            PopUtil.getInstance().showTip(getActivity(), layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });
        //跟单监听
        followAdapter.setOnFollowClick((dataBean) -> {
            FollowDetailActivity.enter(getActivity(), AppConfig.FOLLOW,dataBean);

        });
        swipeRefreshLayout_circle.setOnRefreshListener(() -> {
            page=1;
            getFollowList(AppConfig.FIRST,page);
        });

        recyclerView_circle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout_circle.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == followAdapter.getItemCount() - 1) {
                    followAdapter.startLoad();
                    page = page + 1;
                    getFollowList(AppConfig.LOAD,page);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        Util.colorSwipe(getActivity(),swipeRefreshLayout_circle);

    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        getFollowList(AppConfig.FIRST,1);


    }


    private void getFollowList(String type,int page) {
        NetManger.getInstance().followList(null, null,
                null, null, null, null, null, null,
                null, null, null,String.valueOf(page),"10", (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);

                        FollowEntity followEntity = (FollowEntity) response;
                        if (type.equals(AppConfig.LOAD)){
                            followAdapter.addDatas(followEntity.getData());
                        }else {
                            if (followEntity.getTotal() != 0) {
                                followAdapter.setDatas(followEntity.getData());
                                layout_circle_null.setVisibility(View.GONE);
                                recyclerView_circle.setVisibility(View.VISIBLE);
                            } else {
                                layout_circle_null.setVisibility(View.VISIBLE);
                                recyclerView_circle.setVisibility(View.GONE);
                            }
                        }


                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.VISIBLE);
                        recyclerView_circle.setVisibility(View.GONE);
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
