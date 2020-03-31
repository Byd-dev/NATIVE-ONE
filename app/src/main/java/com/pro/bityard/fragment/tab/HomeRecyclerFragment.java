package com.pro.bityard.fragment.tab;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.HomeQuoteAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BannerEntity;
import com.pro.bityard.manger.QuoteManger;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.stx.xhb.xbanner.XBanner;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import skin.support.SkinCompatManager;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class HomeRecyclerFragment extends BaseFragment implements View.OnClickListener, Observer {


    @BindView(R.id.recyclerView_list)
    HeaderRecyclerView recyclerView_list;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private HomeQuoteAdapter homeQuoteAdapter;

    private QuoteAdapter quoteAdapter;
    private XBanner xBanner;
    private TextSwitcher textSwitcher;


    @Override
    public void onResume() {
        super.onResume();
        xBanner.startAutoPlay();


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_home_recycler;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        QuoteManger.getInstance().addObserver(this);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        View home_view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_layout, null);

        xBanner = home_view.findViewById(R.id.banner);


        textSwitcher = home_view.findViewById(R.id.ts_news);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setLineSpacing(1.1f, 1.1f);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_report_color));
                textView.setTextSize(15);
                textView.setSingleLine();
                return textView;
            }
        });


        RecyclerView recyclerView_hot = home_view.findViewById(R.id.recyclerView_hot);
        //首页三个行情
        homeQuoteAdapter = new HomeQuoteAdapter(getActivity());
        recyclerView_hot.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView_hot.setAdapter(homeQuoteAdapter);


        home_view.findViewById(R.id.img_icon1).setOnClickListener(this);
        home_view.findViewById(R.id.img_icon2).setOnClickListener(this);
        view.findViewById(R.id.img_head).setOnClickListener(this);


        quoteAdapter = new QuoteAdapter(getActivity());
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_list.setAdapter(quoteAdapter);

        recyclerView_list.addHeaderView(home_view);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBanner();
                getReport();
            }
        });


    }


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {


        //获取轮播图和banner
        getBanner();
        getReport();


    }

    /*跑马灯*/
    private void getReport() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("action", "notices");
        NetManger.getInstance().getRequest("/api/index.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);

                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }


    /*轮播图*/
    private void getBanner() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("action", "carousel");
        NetManger.getInstance().getRequest("/api/index.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    swipeRefreshLayout.setRefreshing(true);
                } else if (state.equals(SUCCESS)) {
                    swipeRefreshLayout.setRefreshing(false);
                    // BannerEntity bannerEntity = new Gson().fromJson(response.toString(), BannerEntity.class);
                    //  upBanner(bannerEntity.getCarousels());

                } else if (state.equals(FAILURE)) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }


    private void upBanner(List<BannerEntity.CarouselsBean> data) {
        if (data == null) {
            return;
        }

        if (xBanner != null) {

            xBanner.setBannerData(R.layout.item_banner_layout, data);
            xBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {

                    ImageView imageView = view.findViewById(R.id.img_banner);
                    TextView text_title = view.findViewById(R.id.text_title);

                    text_title.setText(data.get(position).getName());


                    Glide.with(getActivity()).load(NetManger.getInstance().BASE_URL + data.get(position).getXBannerUrl()).into(imageView);
                    Log.d("print", "loadBanner:242:  " + NetManger.getInstance().BASE_URL + data.get(position).getXBannerUrl());
                }
            });

            xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, Object model, View view, int position) {


                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_icon1:
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);

                break;

            case R.id.img_icon2:
                SkinCompatManager.getInstance().restoreDefaultTheme();
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);

                break;

            case R.id.img_head:

                if (isLogin()) {

                } else {
                    LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);
                }


                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
        if (xBanner != null) {
            xBanner.stopAutoPlay();
        }
        QuoteManger.getInstance().clear();
    }


    @Override
    public void update(Observable o, Object arg) {

        ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
        List<String> quoteList = arrayMap.get("0");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                homeQuoteAdapter.setDatas(quoteList.subList(0, 3));
                quoteAdapter.setDatas(quoteList);
            }
        });


    }
}





