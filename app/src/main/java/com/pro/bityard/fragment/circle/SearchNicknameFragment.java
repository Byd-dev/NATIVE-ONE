package com.pro.bityard.fragment.circle;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pro.bityard.R;
import com.pro.bityard.adapter.FollowAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.SoftKeyboardUtils;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class SearchNicknameFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.swipeRefreshLayout_circle)
    SwipeRefreshLayout swipeRefreshLayout_circle;


    @BindView(R.id.recyclerView_circle)
    HeaderRecyclerView recyclerView_circle;

    private FollowAdapter followAdapter;

    @BindView(R.id.layout_circle_null)
    LinearLayout layout_circle_null;

    @BindView(R.id.edit_search)
    EditText edit_search;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        view.findViewById(R.id.text_cancel).setOnClickListener(this);

        followAdapter = new FollowAdapter(getActivity());
        recyclerView_circle.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView_circle.setAdapter(followAdapter);


        followAdapter.setWarningClick(() -> {
            Util.lightOff(getActivity());
            PopUtil.getInstance().showTip(getActivity(), layout_view, false, getString(R.string.text_circle_warning),
                    state -> {

                    });
        });

        swipeRefreshLayout_circle.setOnRefreshListener(() -> {
            getFollowList(null);
        });
        swipeRefreshLayout_circle.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        edit_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                String content = edit_search.getText().toString();
                SoftKeyboardUtils.closeInoutDecorView(getActivity());

                if (TextUtils.isEmpty(content)) {
                    getFollowList(null);
                } else {
                    getFollowList(content);
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
        getFollowList(null);
    }

    private void getFollowList(String content) {
        NetManger.getInstance().followList(null, null,
                content, "usdt", null, null, null, null,
                null, null, null, (state, response) -> {
                    if (state.equals(BUSY)) {
                        swipeRefreshLayout_circle.setRefreshing(true);
                    } else if (state.equals(SUCCESS)) {
                        swipeRefreshLayout_circle.setRefreshing(false);
                        layout_circle_null.setVisibility(View.GONE);
                        recyclerView_circle.setVisibility(View.VISIBLE);
                        FollowEntity followEntity = (FollowEntity) response;
                        if (followEntity.getTotal()!=0){
                            followAdapter.setDatas(followEntity.getData());
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
            case R.id.text_cancel:
                getActivity().finish();
                break;
        }
    }
}
