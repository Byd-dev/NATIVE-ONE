package com.pro.bityard.fragment.tab;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.TradeTabActivity;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.adapter.QuoteHomeAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BannerEntity;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.utils.ListUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.HeaderRecyclerView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.stx.xhb.xbanner.XBanner;

import java.util.List;
import java.util.Objects;
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

    @BindView(R.id.banner)
    XBanner xBanner;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private QuoteHomeAdapter quoteHomeAdapter;

    private QuoteAdapter quoteAdapter;
    private TextSwitcher textSwitcher;

    private List<BannerEntity.NoticesBean> notices;
    private int mNewsIndex;

    @Override
    public void onResume() {
        super.onResume();
        xBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        xBanner.stopAutoPlay();
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

        SocketQuoteManger.getInstance().addObserver(this);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        @SuppressLint("InflateParams") View home_view = LayoutInflater.from(getContext()).inflate(R.layout.item_home_layout, null);



        textSwitcher = home_view.findViewById(R.id.ts_news);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setLineSpacing(1.1f, 1.1f);
                textView.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.text_main_color));
                textView.setTextSize(13);
                textView.setSingleLine();
                return textView;
            }
        });

        startScheduleJob(mHandler, 3000, 3000);

        RecyclerView recyclerView_hot = home_view.findViewById(R.id.recyclerView_hot);
        //首页三个行情
        quoteHomeAdapter = new QuoteHomeAdapter(getActivity());
        recyclerView_hot.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView_hot.setAdapter(quoteHomeAdapter);


        quoteHomeAdapter.setOnItemClick(data -> TradeTabActivity.enter(getContext(), "1", data));


        home_view.findViewById(R.id.img_icon1).setOnClickListener(this);
        home_view.findViewById(R.id.img_icon2).setOnClickListener(this);
        view.findViewById(R.id.img_head).setOnClickListener(this);


        quoteAdapter = new QuoteAdapter(getActivity());
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_list.setAdapter(quoteAdapter);

        recyclerView_list.addHeaderView(home_view);

        Util.colorSwipe(getActivity(),swipeRefreshLayout);

        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBanner();
            }
        });

        quoteAdapter.setOnItemClick(new QuoteAdapter.OnItemClick() {
            @Override
            public void onSuccessListener(String data) {
                TradeTabActivity.enter(getContext(), "1", data);


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


    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            updateNews();


        }
    };

    /*轮播图*/
    private void getBanner() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("action", "carousel");
        NetManger.getInstance().getRequest("/api/index.htm", map, new OnNetResult() {

            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    if (swipeRefreshLayout != null) {

                        swipeRefreshLayout.setRefreshing(true);
                    }
                } else if (state.equals(SUCCESS)) {
                    if (swipeRefreshLayout != null) {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                    BannerEntity bannerEntity = new Gson().fromJson(response.toString(), BannerEntity.class);
                    Log.d("print", "onNetResult:219: "+bannerEntity.getCarousels().size()+"  --  "+bannerEntity.getCarousels());
                    upBanner(bannerEntity.getCarousels());

                    notices = bannerEntity.getNotices();


                } else if (state.equals(FAILURE)) {
                    if (swipeRefreshLayout != null) {

                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
            }
        });
    }


    private void updateNews() {
        if (notices != null) {
            mNewsIndex++;
            if (notices.size() > 0) {
                if (mNewsIndex >= notices.size()) mNewsIndex = 0;
                if (ListUtil.isNotEmpty(notices)) {
                    textSwitcher.setText(notices.get(mNewsIndex).getTitle());
                }
            }
        }
    }

    private void upBanner(List<BannerEntity.CarouselsBean> data) {
        if (data == null) {
            return;
        }

        if (xBanner != null) {

            xBanner.setBannerData(R.layout.item_banner_layout, data);
            xBanner.loadImage((banner, model, view, position) -> {

                ImageView imageView = view.findViewById(R.id.img_banner);
                TextView text_title = view.findViewById(R.id.text_title);

                text_title.setText(data.get(position).getName());


                Glide.with(getActivity()).load(data.get(position).getXBannerUrl()).into(imageView);
                Log.d("print", "loadBanner:242:  " +  data.get(position).getXBannerUrl());
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
        SocketQuoteManger.getInstance().clear();
    }


    @Override
    public void update(Observable o, Object arg) {

        ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
        List<String> quoteList = arrayMap.get("all");
        runOnUiThread(() -> {
            quoteHomeAdapter.setDatas(quoteList.subList(0, 3));
            quoteAdapter.setDatas(quoteList);
        });


    }
}





