package com.pro.bityard.fragment.trade;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pro.bityard.R;
import com.pro.bityard.adapter.MyPagerAdapter;
import com.pro.bityard.adapter.RadioDateAdapter;
import com.pro.bityard.adapter.SpotSearchAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.manger.CommissionManger;
import com.pro.bityard.manger.ControlManger;
import com.pro.bityard.manger.SocketQuoteManger;
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

public class SpotTradeRecordFragment extends BaseFragment implements View.OnClickListener, Observer {

    @BindView(R.id.text_title)
    TextView text_title;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.img_spot_position_filter)
    ImageView img_spot_position_filter;
    @BindView(R.id.img_spot_history_filter)
    ImageView img_spot_history_filter;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.layout_right)
    LinearLayout layout_right;

    @BindView(R.id.recyclerView_date)
    RecyclerView recyclerView_date;

    @BindView(R.id.recyclerView_type)
    RecyclerView recyclerView_type;

    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.btn_return)
    Button btn_return;

    @BindView(R.id.edit_search)
    EditText edit_search;

    private RadioDateAdapter radioDateAdapter, radioTypeAdapter;//杠杆适配器
    private List<String> dataList, typeList;

    private boolean isCommission = true;

    private SpotSearchAdapter spotSearchAdapter;
    @BindView(R.id.recyclerView_search)
    RecyclerView recyclerView_search;
    private String value_search;


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        SocketQuoteManger.getInstance().addObserver(this);

        text_title.setText(getResources().getString(R.string.text_spot));
        view.findViewById(R.id.img_back).setOnClickListener(this);
        btn_return.setOnClickListener(this);
        btn_sure.setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(() -> initContent(), 50);
    }

    private String value_date = null;
    private String value_type = null;

    private void initContent() {
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        initViewPager(viewPager);
        img_spot_history_filter.setOnClickListener(this);
        img_spot_position_filter.setOnClickListener(this);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    img_spot_position_filter.setVisibility(View.GONE);
                    img_spot_history_filter.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    img_spot_position_filter.setVisibility(View.VISIBLE);
                    img_spot_history_filter.setVisibility(View.GONE);
                    isCommission = true;
                } else if (tab.getPosition() == 2) {
                    img_spot_position_filter.setVisibility(View.GONE);
                    img_spot_history_filter.setVisibility(View.VISIBLE);
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

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_spot_trade_record;
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
                    type = AppConfig.SPOT_ALL;
                    List<String> strings = arrayMap.get(type);
                    List<String> searchQuoteList = TradeUtil.searchQuoteList(edit_search.getText().toString(), strings);
                    spotSearchAdapter.setDatas(searchQuoteList);
                } else {
                    type = AppConfig.SPOT_ALL;
                    List<String> quoteList = arrayMap.get(type);
                    spotSearchAdapter.setDatas(quoteList);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.addFragment(new SpotRecordItemFragment(), getString(R.string.text_spot_position));
        myPagerAdapter.addFragment(new SpotCommitHistoryFragment(), getString(R.string.text_history_spot_position));
        myPagerAdapter.addFragment(new SpotTradeHistoryFragment(), getString(R.string.text_history_trade));
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_spot_position_filter:
            case R.id.img_spot_history_filter:
                drawerLayout.openDrawer(layout_right);
                break;


            case R.id.btn_sure:
                String value = value_date + "," + value_search + "," + value_type;
                if (isCommission) {
                    CommissionManger.getInstance().postTag(value);
                } else {
                    ControlManger.getInstance().postTag(value);
                }
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

    private ArrayMap<String, List<String>> arrayMap;
    private String type = AppConfig.SPOT_ALL;
    private String zone_type = AppConfig.VIEW_SPOT;
    private List<String> quoteList;

    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(type);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
