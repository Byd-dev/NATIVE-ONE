package com.pro.bityard.fragment.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.QuoteSpotManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;

public class SpotTradeFragment extends BaseFragment implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.text_currency)
    TextView text_currency;
    @BindView(R.id.layout_null)
    LinearLayout layout_null;
    private String tradeType = "1";//实盘=1 模拟=2
    private String itemData;
    private String quote_code = null;


    private Set<String> optionalList;//自选列表
    @BindView(R.id.img_star)
    ImageView img_star;

    @BindView(R.id.recyclerView_spot)
    HeaderRecyclerView recyclerView_spot;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private SpotPositionAdapter spotPositionAdapter;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.layout_spot_tab;
    }

    public SpotTradeFragment newInstance(String type, String value) {
        SpotTradeFragment fragment = new SpotTradeFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.layout_product).setOnClickListener(this);
        //自选监听
        view.findViewById(R.id.layout_optional).setOnClickListener(this);

        BalanceManger.getInstance().addObserver(this);
        QuoteSpotManger.getInstance().addObserver(this);

        spotPositionAdapter = new SpotPositionAdapter(getActivity());
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_spot.setAdapter(spotPositionAdapter);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_position_head_layout, null);
        recyclerView_spot.addHeaderView(headView);
        
        Util.colorSwipe(getActivity(),swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosition();
        });


    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> startScheduleJob(mHandler, QUOTE_SECOND, QUOTE_SECOND), 50);


        tradeType = getArguments().getString(TYPE);
        itemData = getArguments().getString(VALUE);

        quote_code = itemQuoteContCode(itemData);
        text_name.setText(TradeUtil.name(itemData));
        text_currency.setText(TradeUtil.currency(itemData));

        optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));

        Util.setOptional(getActivity(), optionalList, quote_code, img_star, response -> {
            if (!(boolean) response) {
                optionalList = new HashSet<>();
            }
        });

        getPosition();


    }

    private void getPosition() {
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        NetManger.getInstance().userSpotPosition(loginEntity.getUser().getUserId(), new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    SpotPositionEntity spotPositionEntity = (SpotPositionEntity) response;
                    if (spotPositionEntity.getData().size() == 0) {
                        layout_null.setVisibility(View.VISIBLE);
                        recyclerView_spot.setVisibility(View.GONE);
                    } else {
                        layout_null.setVisibility(View.GONE);
                        recyclerView_spot.setVisibility(View.VISIBLE);
                    }
                    spotPositionAdapter.setDatas(spotPositionEntity.getData());
                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_optional:
                optionalList = Util.SPDealResult(SPUtils.getString(AppConfig.KEY_OPTIONAL, null));
                Log.d("print", "onClick:自选目前: " + optionalList);
                //判断当前是否存在自选
                if (optionalList.size() != 0) {
                    Util.isOptional(quote_code, optionalList, response -> {
                        boolean isOptional = (boolean) response;
                        if (isOptional) {
                            img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star_normal));
                            optionalList.remove(quote_code);
                        } else {
                            img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                            optionalList.add(quote_code);
                        }
                    });
                } else {
                    img_star.setImageDrawable(getResources().getDrawable(R.mipmap.icon_star));
                    optionalList.add(quote_code);
                }
                SPUtils.putString(AppConfig.KEY_OPTIONAL, Util.SPDeal(optionalList));

                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NotNull Message msg) {
            super.handleMessage(msg);
            //发送行情包
            Log.d("print", "handleMessage:现货发送商号: " + quote_code);
            if (quote_code != null) {
                WebSocketManager.getInstance().send("5001", quote_code);
            }

        }
    };

    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteSpotManger.getInstance()) {
            if (!isAdded()) {
                return;
            }

            String quote = (String) arg;
            Log.d("print", "update:现货行情:  " + quote);

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }
}
