package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.adapter.ChainListAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class WithdrawalFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_record)
    ImageView img_record;
    private UserDetailEntity userDetailEntity;
    @BindView(R.id.text_balance)
    TextView text_balance;

    @BindView(R.id.text_title)
    TextView text_title;

    private ChainListAdapter chainListAdapter;//杠杆适配器
    private List<String> dataList;

    private String chain = "OMNI";

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        text_title.setText(getResources().getString(R.string.text_withdrawal));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        img_record.setVisibility(View.VISIBLE);
        img_record.setOnClickListener(this);

        chainListAdapter = new ChainListAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(chainListAdapter);

        dataList = new ArrayList<>();
        dataList.add("OMNI");
        dataList.add("ERC20");
        dataList.add("TRC20");
        chainListAdapter.setDatas(dataList);

        chainListAdapter.setOnItemClick((position, data) -> {
            chainListAdapter.select(position);
            recyclerView.setAdapter(chainListAdapter);
            chain = data;
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_withdrawal;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        UserDetailManger.getInstance().addObserver(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_record:
                if (isLogin()) {
                    UserActivity.enter(getActivity(), IntentConfig.Keys.KEY_FUND_STATEMENT);
                } else {
                    LoginActivity.enter(getActivity(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == UserDetailManger.getInstance()) {
            userDetailEntity = (UserDetailEntity) arg;
            if (text_balance != null) {
                if (isAdded()) {
                    if (userDetailEntity != null) {
                        text_balance.setText(TradeUtil.getNumberFormat(userDetailEntity.getUser().getMoney(), 2));
                    }
                }
            }
        }
    }
}
