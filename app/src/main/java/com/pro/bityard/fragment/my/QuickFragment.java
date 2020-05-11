package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuickAccountAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.view.HeaderRecyclerView;

import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class QuickFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private BalanceEntity balanceEntity;

    private QuickAccountAdapter quickAccountAdapter;
    @BindView(R.id.recyclerView_quick)
    HeaderRecyclerView recyclerView_quick;
    private LinearLayout layout_switch;

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_coin_turn));
        view.findViewById(R.id.img_back).setOnClickListener(this);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_quick_head, null);
        layout_switch = headView.findViewById(R.id.layout_switch);

        layout_switch.setOnClickListener(this);

        BalanceManger.getInstance().addObserver(this);

        quickAccountAdapter = new QuickAccountAdapter(getActivity());
        recyclerView_quick.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView_quick.addHeaderView(headView);
        recyclerView_quick.setAdapter(quickAccountAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        swipeRefreshLayout.setOnRefreshListener(this::initData);

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_quick;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setRefreshing(false);
        BalanceManger.getInstance().getBalance("USDT");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.layout_switch:
                showSwitchPopWindow();
                break;
        }
    }

    private void showSwitchPopWindow() {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_quick_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_switch, 0, 0, Gravity.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {
            balanceEntity = (BalanceEntity) arg;
            if (quickAccountAdapter != null) {
                quickAccountAdapter.setDatas(balanceEntity.getData());
            }
        }
    }
}
