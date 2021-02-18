package com.pro.bityard.fragment.trade;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.adapter.RadioDateAdapter;
import com.pro.bityard.adapter.SpotSearchAdapter;
import com.pro.bityard.adapter.TradeRecordAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.manger.CommissionManger;
import com.pro.bityard.manger.ContractManger;
import com.pro.bityard.manger.ControlManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.utils.ChartUtil;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class ContractFollowRecordFragment extends BaseFragment implements View.OnClickListener, Observer {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.layout_right)
    LinearLayout layout_right;

    // private FundItemEntity fundItemEntity;

    private TradeRecordAdapter tradeRecordAdapter;


    @BindView(R.id.img_contract_record)
    ImageView img_contract_record;


    private String createTimeGe = null;
    private String createTimeLe = null;

    private String FIRST = "first";
    private String REFRESH = "refresh";
    private String LOAD = "load";

    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.btn_return)
    Button btn_return;
    private boolean isCommission = true;
    @BindView(R.id.recyclerView_date)
    RecyclerView recyclerView_date;

    @BindView(R.id.recyclerView_type)
    RecyclerView recyclerView_type;
    private RadioDateAdapter radioDateAdapter, radioTypeAdapter;//杠杆适配器
    private List<String> dataList, typeList;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_contract_follow_record;
    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        SocketQuoteManger.getInstance().addObserver(this);





        view.findViewById(R.id.img_back).setOnClickListener(this);

        img_contract_record.setVisibility(View.VISIBLE);

        tradeRecordAdapter = new TradeRecordAdapter(getActivity());

        btn_return.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        /*刷新监听*/

        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);

        //监听
        tradeRecordAdapter.setOnItemClick(this::showDetailPopWindow);



    }

    private String value_date = null;
    private String value_type = null;
    private SpotSearchAdapter spotSearchAdapter;
    @BindView(R.id.recyclerView_search)
    RecyclerView recyclerView_search;
    private String value_search;
    @BindView(R.id.edit_search)
    EditText edit_search;

    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager);
        img_contract_record.setOnClickListener(this);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    img_contract_record.setVisibility(View.VISIBLE);
                    isCommission = true;

                } else if (tab.getPosition() == 1) {
                    img_contract_record.setVisibility(View.GONE);
                    isCommission = false;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        value_date = getActivity().getString(R.string.text_near_one_day);
        value_type = getActivity().getString(R.string.text_buy_and_sell);

        radioDateAdapter = new RadioDateAdapter(getActivity());
        recyclerView_date.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView_date.setAdapter(radioDateAdapter);

        dataList = new ArrayList<>();
        dataList.add(getString(R.string.text_near_one_day));
        dataList.add(getString(R.string.text_near_one_week));
        dataList.add(getString(R.string.text_near_one_month));
        dataList.add(getString(R.string.text_near_three_month));

        radioDateAdapter.setDatas(dataList);
        radioDateAdapter.select(getString(R.string.text_near_one_day));
        radioDateAdapter.setOnItemClick((position, data) -> {
            radioDateAdapter.select(data);
            value_date = data;
        });


        radioTypeAdapter = new RadioDateAdapter(getActivity());
        recyclerView_type.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView_type.setAdapter(radioTypeAdapter);

        typeList = new ArrayList<>();
        typeList.add(getString(R.string.text_buy_and_sell));
        typeList.add(getString(R.string.text_buy));
        typeList.add(getString(R.string.text_sell));
        radioTypeAdapter.select(getString(R.string.text_buy_and_sell));

        radioTypeAdapter.setDatas(typeList);
        radioTypeAdapter.setOnItemClick((position, data) -> {
            radioTypeAdapter.select(data);
            value_type = data;
        });

        spotSearchAdapter = new SpotSearchAdapter(getActivity());
        recyclerView_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_search.setAdapter(spotSearchAdapter);


        spotSearchAdapter.setOnItemClick(data -> {
            String name = TradeUtil.name(data);
            String currency = TradeUtil.currency(data);
            //value_search = name + currency;
            String code = TradeUtil.code(data);
            value_search = code;
            edit_search.setText(name);
        });
    }


    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new ContractRecordFragment(), getString(R.string.text_orders));
        myPagerAdapter.addFragment(new SpotCommitHistoryFragment(), getString(R.string.text_follow_record));

        viewPager.setAdapter(myPagerAdapter);
    }

    /*显示详情*/
    private void showDetailPopWindow(TradeHistoryEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_trade_detail_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText(R.string.text_order_detail);

        TextView text_name = view.findViewById(R.id.text_name);
        text_name.setText(dataBean.getCommodityCode());

        ImageView img_bg = view.findViewById(R.id.img_bg);
        ChartUtil.setIcon(dataBean.getCommodityCode().substring(0, dataBean.getCommodityCode().length() - dataBean.getCurrency().length()), img_bg);

        TextView text_income = view.findViewById(R.id.text_p_l);
        double income = dataBean.getIncome();

        double serviceCharge = dataBean.getServiceCharge();
        TextView text_profit = view.findViewById(R.id.text_profit);

        text_profit.setText(TradeUtil.getNumberFormat(TradeUtil.sub(income, serviceCharge), 2));

        if (income > 0) {
            text_income.setTextColor(getResources().getColor(R.color.text_quote_green));
        } else {
            text_income.setTextColor(getResources().getColor(R.color.text_quote_red));

        }
        text_income.setText(TradeUtil.getNumberFormat(income, 2));

        TextView text_side = view.findViewById(R.id.text_side);
        boolean isBuy = dataBean.isIsBuy();
        if (isBuy) {
            text_side.setText(R.string.text_buy_much);
        } else {
            text_side.setText(R.string.text_buy_empty);
        }

        TextView text_margin = view.findViewById(R.id.text_margin);
        text_margin.setText(String.valueOf(dataBean.getMargin()));

        TextView text_lever = view.findViewById(R.id.text_lever);
        text_lever.setText(String.valueOf(dataBean.getLever()));
        TextView text_orders = view.findViewById(R.id.text_order);
        text_orders.setText(String.valueOf(dataBean.getCpVolume()));
        TextView text_opPrice = view.findViewById(R.id.text_open_price);
        text_opPrice.setText(String.valueOf(dataBean.getOpPrice()));
        TextView text_clPrice = view.findViewById(R.id.text_close_price);
        text_clPrice.setText(String.valueOf(dataBean.getCpPrice()));
        TextView text_fee = view.findViewById(R.id.text_fee);
        text_fee.setText(String.valueOf(serviceCharge));
        TextView text_o_n = view.findViewById(R.id.text_o_n);
        text_o_n.setText(String.valueOf(dataBean.getDeferDays()));
        TextView text_o_n_fee = view.findViewById(R.id.text_o_n_fee);
        text_o_n_fee.setText(String.valueOf(dataBean.getDeferFee()));
        TextView text_currency = view.findViewById(R.id.text_currency_type);
        text_currency.setText(dataBean.getCurrency());
        TextView text_open_time = view.findViewById(R.id.text_open_time);
        text_open_time.setText(ChartUtil.getDate(dataBean.getTime()));
        TextView text_close_time = view.findViewById(R.id.text_close_time);
        text_close_time.setText(ChartUtil.getDate(dataBean.getTradeTime()));
        TextView text_order_id = view.findViewById(R.id.text_order_id);
        text_order_id.setText(dataBean.getId());


        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            popupWindow.dismiss();

        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
    }


    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    type = AppConfig.CONTRACT_ALL;
                    List<String> strings = arrayMap.get(type);
                    List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                    spotSearchAdapter.setDatas(searchQuoteList);
                } else {
                    type = AppConfig.CONTRACT_ALL;
                    List<String> quoteList = arrayMap.get(type);
                    spotSearchAdapter.setDatas(quoteList);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_contract_record:
                drawerLayout.openDrawer(layout_right);

                break;
            case R.id.btn_sure:
                String value = value_date + "," + value_search + "," + value_type;
                ContractManger.getInstance().postTag(value);
                drawerLayout.closeDrawer(layout_right);
                break;
            case R.id.btn_return:
                radioDateAdapter.select(getString(R.string.text_near_one_day));
                radioTypeAdapter.select(getString(R.string.text_buy_and_sell));
                edit_search.setText("");
                value_date = getActivity().getString(R.string.text_near_one_day);
                value_type = getActivity().getString(R.string.text_buy_and_sell);
                break;
        }
    }

    private int oldSelect = 0;
    private ArrayMap<String, List<String>> arrayMap;
    private String type = AppConfig.CONTRACT_ALL;
    private String zone_type = AppConfig.VIEW_SPOT;
    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;

        }
    }
}
