package com.pro.bityard.fragment.tab;

import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.manger.QuoteManger;
import com.pro.bityard.utils.Util;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class MarketFragment extends BaseFragment implements View.OnClickListener, Observer {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private QuoteAdapter quoteAdapter;

    private int flag_new_price = 0;
    private int flag_up_down = 0;

    @BindView(R.id.img_new_price)
    ImageView img_new_price;

    @BindView(R.id.img_up_down)
    ImageView img_up_down;

    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        QuoteManger.getInstance().addObserver(this);


        quoteAdapter = new QuoteAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(quoteAdapter);


        view.findViewById(R.id.layout_new_price).setOnClickListener(this);
        view.findViewById(R.id.layout_up_down).setOnClickListener(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                // quoteAdapter.setDatas(quoteList);
            }
        });

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_market;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_new_price:

                if (flag_new_price == 0) {
                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_new_price = 1;
                } else if (flag_new_price == 1) {
                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_new_price = 0;

                }

                break;
            case R.id.layout_up_down:
                if (flag_up_down == 0) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_up_down = 1;
                } else if (flag_up_down == 1) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_up_down = 0;

                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuoteManger.getInstance().clear();
    }

    @Override
    public void update(Observable o, Object arg) {
        List<String> quoteList = (List<String>) arg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quoteAdapter.setDatas(quoteList);

            }
        });


    }
}
