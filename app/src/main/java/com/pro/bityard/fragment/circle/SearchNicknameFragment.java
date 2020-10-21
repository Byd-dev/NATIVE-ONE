package com.pro.bityard.fragment.circle;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.FollowDetailActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.SoftKeyboardUtils;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class SearchNicknameFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;

    @BindView(R.id.layout_content)
    LinearLayout layout_content;

    @BindView(R.id.layout_search_null)
    LinearLayout layout_search_null;
    @BindView(R.id.swipeRefreshLayout_circle)
    SwipeRefreshLayout swipeRefreshLayout_circle;


    @BindView(R.id.recyclerView_circle)
    HeaderRecyclerView recyclerView_circle;

    private FollowAdapter followAdapter;
    private int page = 1;


    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;

    @BindView(R.id.edit_search)
    EditText edit_search;
    private String content;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_cancel).setOnClickListener(this);

        followAdapter = new FollowAdapter(getActivity());
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView_circle.setLayoutManager(linearLayoutManager);


        recyclerView_circle.setAdapter(followAdapter);


        followAdapter.setWarningClick(() -> {
            Util.lightOff(getActivity());
            PopUtil.getInstance().showTip(getActivity(), layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });
        recyclerView_circle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeRefreshLayout_circle.isRefreshing()) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == followAdapter.getItemCount() - 1) {
                    followAdapter.startLoad();
                    page = page + 1;
                    getFollowList(AppConfig.LOAD,content,page);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        //跟单监听
        followAdapter.setOnFollowClick((dataBean) -> {
            FollowDetailActivity.enter(getActivity(),AppConfig.FOLLOW, dataBean);

        });
        swipeRefreshLayout_circle.setOnRefreshListener(() -> {
            page=1;
            getFollowList(AppConfig.FIRST,content,page);
        });
        Util.colorSwipe(getActivity(),swipeRefreshLayout_circle);

        edit_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                content = edit_search.getText().toString();
                SoftKeyboardUtils.closeInoutDecorView(getActivity());

                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_search_all_traders), Toast.LENGTH_SHORT).show();
                } else {
                    page=1;
                    getFollowList(AppConfig.FIRST, content,page);
                }
            }
            return false;
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_search_nickname;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        page=1;
        getFollowList(AppConfig.FIRST,null,page);
    }

    private void getFollowList(String loadType,String content,int page) {
        NetManger.getInstance().followList(null, null,
                content, "usdt", null, null, null, null,
                null, null, null, String.valueOf(page),"10",(state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.GONE);
                        layout_content.setVisibility(View.VISIBLE);
                        FollowEntity followEntity = (FollowEntity) response;
                        if (loadType.equals(AppConfig.LOAD)){
                            followAdapter.addDatas(followEntity.getData());
                        }else {
                            if (followEntity.getTotal() != 0) {
                                followAdapter.setDatas(followEntity.getData());
                                layout_search_null.setVisibility(View.GONE);

                            } else {
                                layout_search_null.setVisibility(View.VISIBLE);
                            }
                        }

                    } else if (state.equals(FAILURE)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.VISIBLE);
                        layout_content.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
                getActivity().finish();
                break;
        }
    }
}
