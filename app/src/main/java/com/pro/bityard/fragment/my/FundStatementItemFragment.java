package com.pro.bityard.fragment.my;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.adapter.FundSelectAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.AppContext;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.entity.FundItemEntity;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class FundStatementItemFragment extends BaseFragment implements View.OnClickListener {

    private FundSelectAdapter fundSelectAdapter;

    @BindView(R.id.layout_select)
    RelativeLayout layout_select;

    @BindView(R.id.layout_pop_title)
    LinearLayout layout_pop_title;

    @BindView(R.id.text_select)
    TextView text_select;

    @BindView(R.id.img_bg)
    ImageView img_bg;
    private FundItemEntity fundItemEntity;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        layout_select.setOnClickListener(this);
        fundSelectAdapter = new FundSelectAdapter(getActivity());
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fund_statement_item;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        NetManger.getInstance().currencyList("1", (state, response) -> {
            if (state.equals(SUCCESS)) {
                fundItemEntity = new Gson().fromJson(response.toString(), FundItemEntity.class);
                fundItemEntity.getData().add(0, new FundItemEntity.DataBean("", true, "", "", false, "ALL", 0, 0, 0, ""));
                Log.d("print", "initData: " + fundItemEntity);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select:
                showFundWindow(fundItemEntity);
                break;
        }
    }

    private int oldSelect = 0;

    //选择杠杆
    private void showFundWindow(FundItemEntity fundItemEntity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fund_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(fundSelectAdapter);
        if (fundItemEntity != null) {
            List<FundItemEntity.DataBean> fundItemEntityData = fundItemEntity.getData();
            fundSelectAdapter.setDatas(fundItemEntityData);

            fundSelectAdapter.select(oldSelect);

            fundSelectAdapter.setOnItemClick((position, data) -> {
                oldSelect = position;
                fundSelectAdapter.select(position);
                recyclerView.setAdapter(fundSelectAdapter);
                fundSelectAdapter.notifyDataSetChanged();
                text_select.setText(data.getName());
                String code = data.getCode();
                switch (code) {
                    case "":
                        img_bg.setVisibility(View.GONE);
                        break;
                    case "EOS":
                        img_bg.setVisibility(View.VISIBLE);
                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_eos));
                        break;
                    case "LTC":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_ltc));
                        break;
                    case "BCH":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_bch));
                        break;
                    case "USDT":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_usdt));
                        break;
                    case "BTC":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_btc));
                        break;
                    case "ETH":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_eth));
                        break;
                    case "XRP":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_xrp));
                        break;
                    case "TRX":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_trx));
                        break;
                    case "HT":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_ht));
                        break;
                    case "LINK":
                        img_bg.setVisibility(View.VISIBLE);

                        img_bg.setImageDrawable(AppContext.getAppContext().getResources().getDrawable(R.mipmap.icon_link));
                        break;
                }
                popupWindow.dismiss();


            });
        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_select, Gravity.CENTER, 0, 0);
    }
}
