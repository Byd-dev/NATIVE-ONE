package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.SellBuyListAdapter;
import com.pro.bityard.adapter.SpotPositionAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BuySellEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.QuoteMinEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.QuoteCurrentManger;
import com.pro.bityard.manger.QuoteSpotManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.switchlibrary.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;

public class SpotTradeFragment extends BaseFragment implements View.OnClickListener, Observer {
    private static final String TYPE = "tradeType";
    private static final String VALUE = "value";
    @BindView(R.id.layout_spot)
    LinearLayout layout_view;

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
    /*头部视图id*/
    RelativeLayout layout_cancel;
    View view_line_two;
    private SpotPositionAdapter spotPositionAdapter;


    private SellBuyListAdapter sellAdapter, buyAdapter;
    private RecyclerView recyclerView_sell;
    private RecyclerView recyclerView_buy;
    private List<BuySellEntity> buyList;
    private List<BuySellEntity> sellList;
    private String quote;
    private TextView text_price;
    private TextView text_currency_price;
    private String value_rate;
    private TextView text_scale;


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
        QuoteCurrentManger.getInstance().addObserver(this);//1min 实时


        spotPositionAdapter = new SpotPositionAdapter(getActivity());
        recyclerView_spot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_spot.setAdapter(spotPositionAdapter);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_spot_layout, null);
        recyclerView_spot.addHeaderView(headView);
        layout_cancel = headView.findViewById(R.id.layout_cancel);
        view_line_two = headView.findViewById(R.id.view_line_two);
        Util.colorSwipe(getActivity(), swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosition();
        });

        text_scale = headView.findViewById(R.id.text_scale);
        text_price = headView.findViewById(R.id.text_price);
        text_currency_price = headView.findViewById(R.id.text_currency_price);


        headView.findViewById(R.id.layout_buy_sell_switch).setOnClickListener(this);


        sellAdapter = new SellBuyListAdapter(getActivity());
        recyclerView_sell = headView.findViewById(R.id.recyclerView_sell);
        recyclerView_sell.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_sell.setAdapter(sellAdapter);

        buyAdapter = new SellBuyListAdapter(getActivity());
        recyclerView_buy = headView.findViewById(R.id.recyclerView_buy);
        recyclerView_buy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_buy.setAdapter(buyAdapter);


    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> startScheduleJob(mHandler, ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND), 50);


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

        value_rate = SPUtils.getString(AppConfig.USD_RATE, null);


    }

    private void getPosition() {
        LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        if (loginEntity == null) {
            return;
        }
        NetManger.getInstance().userSpotPosition(loginEntity.getUser().getUserId(), (state, response) -> {
            if (state.equals(BUSY)) {
                swipeRefreshLayout.setRefreshing(true);
            } else if (state.equals(SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                SpotPositionEntity spotPositionEntity = (SpotPositionEntity) response;
                if (spotPositionEntity.getData().size() == 0) {
                    layout_null.setVisibility(View.VISIBLE);
                    layout_cancel.setVisibility(View.GONE);
                    view_line_two.setVisibility(View.GONE);
                } else {
                    layout_null.setVisibility(View.GONE);
                    layout_cancel.setVisibility(View.VISIBLE);
                    view_line_two.setVisibility(View.VISIBLE);
                }
                spotPositionAdapter.setDatas(spotPositionEntity.getData());
            } else if (state.equals(FAILURE)) {
                swipeRefreshLayout.setRefreshing(false);

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
            /*买卖展示切换*/
            case R.id.layout_buy_sell_switch:
                if (quote != null) {
                    Util.lightOff(getActivity());
                    showBuySellSwitch(getActivity(), layout_view);
                }

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
                WebSocketManager.getInstance().send("4001", quote_code);

            }

        }
    };


    private int length = 5;

    @SuppressLint("SetTextI18n")
    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteSpotManger.getInstance()) {
            if (!isAdded()) {
                return;
            }

            quote = (String) arg;


            runOnUiThread(() -> {
                buyList = Util.getBuyList(quote);
                buyAdapter.isSell(false);
                buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));


                sellList = Util.getSellList(quote);
                sellAdapter.isSell(true);
                sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));
            });


        } else if (o == QuoteCurrentManger.getInstance()) {
            if (!isAdded()) {
                return;
            }
            QuoteMinEntity quoteMinEntity = (QuoteMinEntity) arg;
            if (quoteMinEntity != null) {
                runOnUiThread(() -> {
                    if (text_price != null) {
                        int isUp = quoteMinEntity.getIsUp();
                        double price = quoteMinEntity.getPrice();
                        text_price.setText(String.valueOf(price));
                        text_scale.setText(TradeUtil.scaleString(TradeUtil.decimalPoint(String.valueOf(price))));
                        if (value_rate != null) {
                            text_currency_price.setText("≈" + TradeUtil.numberHalfUp(TradeUtil.mul(price, Double.parseDouble(value_rate)), 2));
                        } else {
                            text_currency_price.setText("≈" + price);
                        }
                        switch (isUp) {
                            case -1:
                                text_price.setTextColor(getActivity().getResources().getColor(R.color.text_quote_red));
                                break;
                            case 1:
                                text_price.setTextColor(getActivity().getResources().getColor(R.color.text_quote_green));
                                break;
                            case 0:
                                text_price.setTextColor(getActivity().getResources().getColor(R.color.text_main_color));
                                break;
                        }

                    }
                });
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();


    }

    /*买卖单显示切换*/
    public void showBuySellSwitch(Activity activity, View layout_view) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_buy_sell_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.text_default).setOnClickListener(view1 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.VISIBLE);
            recyclerView_sell.setVisibility(View.VISIBLE);
            length = 5;

            buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));
            sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));

        });


        view.findViewById(R.id.text_show_sell).setOnClickListener(view12 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.GONE);
            recyclerView_sell.setVisibility(View.VISIBLE);
            length = 10;
            if (sellList != null) {
                sellAdapter.setDatas(sellList.subList(0, length), Util.sellMax(quote));
            }

        });
        view.findViewById(R.id.text_show_buy).setOnClickListener(view12 -> {

            popupWindow.dismiss();
            recyclerView_buy.setVisibility(View.VISIBLE);
            recyclerView_sell.setVisibility(View.GONE);
            length = 10;
            if (buyList != null) {
                buyAdapter.setDatas(buyList.subList(0, length), Util.buyMax(quote));
            }

        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);


        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
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
}
