package com.pro.bityard.fragment.circle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.circleAdapter.FollowLogAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FollowLogEntity;
import com.pro.bityard.utils.Util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class FollowLogFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // 声明平移动画
    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    private FollowLogAdapter followLogAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_follow_log;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(R.string.text_follow_log);
        view.findViewById(R.id.img_back).setOnClickListener(this);

        Util.colorSwipe(getActivity(),swipeRefreshLayout);

        followLogAdapter = new FollowLogAdapter(getActivity());
        recyclerView_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_address.setAdapter(followLogAdapter);
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);




    }




    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        NetManger.getInstance().followLog((state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                FollowLogEntity entity = (FollowLogEntity) response;
                if (entity != null) {
                    followLogAdapter.setDatas(entity.getData());
                }
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);
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
