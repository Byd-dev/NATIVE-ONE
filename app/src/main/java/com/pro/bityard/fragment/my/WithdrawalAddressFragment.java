package com.pro.bityard.fragment.my;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.adapter.WithdrawalAddressAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
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
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView_address)
    RecyclerView recyclerView_address;

    private WithdrawalAddressAdapter withdrawalAddressAdapter;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        text_title.setText(getResources().getString(R.string.text_withdrawal_address));
        view.findViewById(R.id.img_back).setOnClickListener(this);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));

        withdrawalAddressAdapter = new WithdrawalAddressAdapter(getActivity());
        recyclerView_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_address.setAdapter(withdrawalAddressAdapter);
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(this::initData);

        withdrawalAddressAdapter.setOnEditClick(data -> {

        });

        withdrawalAddressAdapter.setCopyClick(data -> {
            Util.copy(getActivity(), data.getAddress());
            Toast.makeText(getActivity(), R.string.text_copied, Toast.LENGTH_SHORT).show();

        });


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
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                WithdrawalAdressEntity withdrawalAdressEntity = (WithdrawalAdressEntity) response;
                withdrawalAddressAdapter.setDatas(withdrawalAdressEntity.getData());
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
