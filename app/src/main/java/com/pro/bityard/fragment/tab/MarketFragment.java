package com.pro.bityard.fragment.tab;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.quote.Observer;
import com.pro.bityard.quote.QuoteManger;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pro.bityard.config.AppConfig.GET_QUOTE_SECOND;

public class MarketFragment extends BaseFragment implements Observer, View.OnClickListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private QuoteAdapter quoteAdapter;

    private int flag_new_price=0;
    private int flag_up_down=0;

    @BindView(R.id.img_new_price)
    ImageView img_new_price;

    @BindView(R.id.img_up_down)
    ImageView img_up_down;

    @Override
    protected void onLazyLoad() {
        startScheduleJob(mHandler, GET_QUOTE_SECOND, GET_QUOTE_SECOND);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> quoteList = QuoteManger.getInstance().getQuoteList();
            if (quoteList != null&&swipeRefreshLayout!=null) {
                swipeRefreshLayout.setRefreshing(false);
                quoteAdapter.setDatas(quoteList);
            }else {

            }


        }
    };

    @Override
    protected void initView(View view) {
        swipeRefreshLayout.setRefreshing(true);

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
                List<String> quoteList = QuoteManger.getInstance().getQuoteList();
                if (quoteList != null) {
                    quoteAdapter.setDatas(quoteList);
                }else {

                }
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
    public void update(String message) {

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
}
