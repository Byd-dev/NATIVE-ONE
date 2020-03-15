package com.pro.bityard.fragment.tab;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.adapter.HomeQuoteAdapter;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.quote.Observer;
import com.pro.bityard.quote.QuoteManger;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.AlphaChangeListener;
import com.pro.bityard.view.MyScrollView;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import skin.support.SkinCompatManager;

public class HomeFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.bar)
    RelativeLayout layout_bar;


    @BindView(R.id.scrollView)
    MyScrollView myScrollView;


    @BindView(R.id.recyclerView_hot)
    RecyclerView recyclerView_hot;
    @BindView(R.id.recyclerView_list)
    RecyclerView recyclerView_list;

    private HomeQuoteAdapter homeQuoteAdapter;

    private QuoteAdapter quoteAdapter;




    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_home;
    }


    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {


        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


        myScrollView.setAlphaChangeListener(new AlphaChangeListener() {
            @Override
            public void alphaChanging(float alpha) {
                layout_bar.setAlpha(1 - alpha);
            }
        });


        view.findViewById(R.id.img_icon1).setOnClickListener(this);
        view.findViewById(R.id.img_icon2).setOnClickListener(this);
        view.findViewById(R.id.img_head).setOnClickListener(this);

        initRecyclerView(recyclerView_hot);
        initRecyclerView(recyclerView_list);


        homeQuoteAdapter = new HomeQuoteAdapter(getActivity());
        recyclerView_hot.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView_hot.setAdapter(homeQuoteAdapter);

        quoteAdapter = new QuoteAdapter(getActivity());
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_list.setAdapter(quoteAdapter);

        startScheduleJob(mHandler,500,500);


    }

    private void initRecyclerView(RecyclerView recyclerView){
        //解决数据加载不完的问题
        recyclerView.setNestedScrollingEnabled(false);
        //当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        recyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        recyclerView.setFocusable(false);


        //防止嵌套出现轻微卡顿的问题
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> quoteList = QuoteManger.getInstance().getQuoteList();
            if (quoteList!=null){
                homeQuoteAdapter.setDatas(quoteList.subList(0,3));
                quoteAdapter.setDatas(quoteList);
            }


        }
    };


    @Override
    protected void intPresenter() {

    }


    @Override
    protected void initData() {



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


                LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);


                break;
        }
    }

    @Override
    public void update(String data) {
        List<String> strings = Util.quoteResult(data);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}





