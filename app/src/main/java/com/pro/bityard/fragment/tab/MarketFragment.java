package com.pro.bityard.fragment.tab;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pro.bityard.R;
import com.pro.bityard.activity.QuoteDetailActivity;
import com.pro.bityard.adapter.QuoteAdapter;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.manger.QuoteItemManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;

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


    private String type = "0";
    private ArrayMap<String, List<String>> arrayMap;

    @Override
    protected void onLazyLoad() {

    }


    @Override
    protected void initView(View view) {

        swipeRefreshLayout.setRefreshing(true);

        QuoteListManger.getInstance().addObserver(this);


        quoteAdapter = new QuoteAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(quoteAdapter);


        view.findViewById(R.id.layout_new_price).setOnClickListener(this);
        view.findViewById(R.id.layout_up_down).setOnClickListener(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.maincolor));
        /*刷新监听*/
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
            String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
            if (quote_host == null && quote_code == null) {
                NetManger.getInstance().initQuote();
            } else {
                assert quote_host != null;
                QuoteListManger.getInstance().quote(quote_host, quote_code);
            }
        });


        quoteAdapter.setOnItemClick(data -> {
            Log.d("print", "onSuccessListener:92:  " + data);
            QuoteDetailActivity.enter(getContext(), "1", data);


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
                if (arrayMap == null) {
                    return;
                }

                if (flag_new_price == 0) {
                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_new_price = 1;
                    type = "1";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter.setDatas(quoteList);


                } else if (flag_new_price == 1) {

                    img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_new_price = 0;
                    type = "2";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter.setDatas(quoteList);

                }
                img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
            case R.id.layout_up_down:
                if (arrayMap == null) {
                    return;
                }
                if (flag_up_down == 0) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_down));
                    flag_up_down = 1;
                    type = "3";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter.setDatas(quoteList);

                } else if (flag_up_down == 1) {
                    img_up_down.setImageDrawable(getResources().getDrawable(R.mipmap.market_up));
                    flag_up_down = 0;
                    type = "4";
                    List<String> quoteList = arrayMap.get(type);
                    quoteAdapter.setDatas(quoteList);

                }
                img_new_price.setImageDrawable(getResources().getDrawable(R.mipmap.market_up_down));

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuoteListManger.getInstance().clear();
    }

    @Override
    public void update(Observable o, Object arg) {
        arrayMap = (ArrayMap<String, List<String>>) arg;
        List<String> quoteList = arrayMap.get(type);
        runOnUiThread(() -> {
            quoteAdapter.setDatas(quoteList);
            swipeRefreshLayout.setRefreshing(false);
        });


    }
}
