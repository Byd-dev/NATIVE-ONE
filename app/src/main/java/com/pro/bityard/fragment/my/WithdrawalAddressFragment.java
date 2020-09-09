package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.WithdrawalAddressAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.utils.Util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class WithdrawalAddressFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // 声明平移动画
    private TranslateAnimation animation;
    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    private WithdrawalAddressAdapter withdrawalAddressAdapter;


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_withdrawal_address));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        withdrawalAddressAdapter = new WithdrawalAddressAdapter(getActivity());
        recyclerView_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_address.setAdapter(withdrawalAddressAdapter);
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);

        withdrawalAddressAdapter.setOnEditClick(data -> {
            Util.lightOff(getActivity());
            showEditPopWindow(data);
        });

        withdrawalAddressAdapter.setCopyClick(data -> {
            Util.copy(getActivity(), data.getAddress());
            Toast.makeText(getActivity(), R.string.text_copied, Toast.LENGTH_SHORT).show();
        });

    }

    /*显示删除的按钮*/
    private void showEditPopWindow(WithdrawalAdressEntity.DataBean data) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_edit_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.findViewById(R.id.text_delete).setOnClickListener(v -> {
            NetManger.getInstance().deleteAddress(data.getId(), (state, response) -> {
                if (state.equals(BUSY)) {
                    showProgressDialog();
                } else if (state.equals(SUCCESS)) {
                    dismissProgressDialog();
                    popupWindow.dismiss();
                    initData();
                } else if (state.equals(FAILURE)) {
                    dismissProgressDialog();
                }
            });
        });

        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(getActivity(), popupWindow);
        Util.isShowing(getActivity(), popupWindow);


        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);

    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_withdrawal_address;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

        NetManger.getInstance().withdrawalAddressList((state, response) -> {
            if (state.equals(BUSY)) {
                if (isAdded()){
                    swipeRefreshLayout.setRefreshing(true);
                }
            } else if (state.equals(SUCCESS)) {
                if (isAdded()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                WithdrawalAdressEntity withdrawalAdressEntity = (WithdrawalAdressEntity) response;
                if (withdrawalAdressEntity != null) {
                    withdrawalAddressAdapter.setDatas(withdrawalAdressEntity.getData());
                }
            } else if (state.equals(FAILURE)) {
                if (isAdded()){
                    swipeRefreshLayout.setRefreshing(false);
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
            case R.id.btn_submit:
                UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_ADD_ADDRESS);

                break;
        }
    }
}
