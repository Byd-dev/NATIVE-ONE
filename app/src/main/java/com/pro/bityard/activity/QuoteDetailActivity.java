package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.adapter.QuotePopAdapter;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.manger.QuoteItemManger;
import com.pro.bityard.manger.QuoteListManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.pro.bityard.config.AppConfig.ITEM_QUOTE_SECOND;
import static com.pro.bityard.utils.TradeUtil.itemQuoteContCode;
import static com.pro.bityard.utils.TradeUtil.itemQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.itemQuoteMaxPrice;
import static com.pro.bityard.utils.TradeUtil.itemQuoteMinPrice;
import static com.pro.bityard.utils.TradeUtil.itemQuotePrice;
import static com.pro.bityard.utils.TradeUtil.itemQuoteTodayPrice;
import static com.pro.bityard.utils.TradeUtil.itemQuoteVolume;
import static com.pro.bityard.utils.TradeUtil.listQuoteIsRange;
import static com.pro.bityard.utils.TradeUtil.listQuotePrice;
import static com.pro.bityard.utils.TradeUtil.listQuoteTodayPrice;

public class QuoteDetailActivity extends BaseActivity implements View.OnClickListener, Observer {
    private static final String TYPE = "MoneyType";
    private static final String VALUE = "value";
    private static final String quoteType = "0";

    @BindView(R.id.layout_bar)
    RelativeLayout layout_bar;
    private String moneyType;
    private String itemData;

    @BindView(R.id.text_name)
    TextView text_name;

    @BindView(R.id.text_usdt)
    TextView text_name_usdt;

    @BindView(R.id.text_lastPrice)
    TextView text_lastPrice;

    @BindView(R.id.img_up_down)
    ImageView img_up_down;

    @BindView(R.id.text_change)
    TextView text_change;

    @BindView(R.id.text_range)
    TextView text_range;

    @BindView(R.id.text_max)
    TextView text_max;
    @BindView(R.id.text_min)
    TextView text_min;
    @BindView(R.id.text_volume)
    TextView text_volume;

    private List<String> quoteList;

    private QuotePopAdapter quotePopAdapter;


    public static void enter(Context context, String MoneyType, String data) {
        Intent intent = new Intent(context, QuoteDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VALUE, data);
        bundle.putString(TYPE, MoneyType);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_quote_detail;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

        QuoteListManger.getInstance().addObserver(this);

        QuoteItemManger.getInstance().addObserver(this);


        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_setting).setOnClickListener(this);
        findViewById(R.id.layout_more).setOnClickListener(this);


    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        moneyType = bundle.getString(TYPE);
        itemData = bundle.getString(VALUE);
        //开启单个刷新
        QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, TradeUtil.itemQuoteContCode(itemData));

        String[] split1 = Util.quoteList(itemQuoteContCode(itemData)).split(",");
        text_name.setText(split1[0]);
        text_name_usdt.setText(split1[1]);
        text_lastPrice.setText(listQuotePrice(itemData));
        text_change.setText(TradeUtil.quoteChange(listQuotePrice(itemData), listQuoteTodayPrice(itemData)));
        text_range.setText(TradeUtil.quoteRange(listQuotePrice(itemData), listQuoteTodayPrice(itemData)));

        if (listQuoteIsRange(itemData).equals("-1")) {
            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));

            img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_down));

        } else if (listQuoteIsRange(itemData).equals("1")) {
            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));

            img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_up));

        } else if (listQuoteIsRange(itemData).equals("0")) {

            text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
            text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
            text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));

        }


    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_more:

                showMoreWindow();

                break;


            case R.id.img_setting:
                //BNBUSDT1808
                QuoteItemManger.getInstance().startScheduleJob(ITEM_QUOTE_SECOND, ITEM_QUOTE_SECOND, "BNBUSDT1808");

                break;

        }
    }




    private void showMoreWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_more_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView=view.findViewById(R.id.recyclerView_pop);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        quotePopAdapter=new QuotePopAdapter(this);
        recyclerView.setAdapter(quotePopAdapter);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.6f;
        getWindow().setAttributes(params);

        view.findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1f);
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(layout_bar);
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == QuoteListManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get(quoteType);
            Log.d("print", "update:239:  "+quoteList);
            if (quotePopAdapter!=null){
                quotePopAdapter.setDatas(quoteList);
            }

        } else if (o == QuoteItemManger.getInstance()) {
            String quote = (String) arg;

            Log.d("print", "update:171:  " + quote);
            text_lastPrice.setText(itemQuotePrice(quote));
            text_change.setText(TradeUtil.quoteChange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));
            text_range.setText(TradeUtil.quoteRange(itemQuotePrice(quote), itemQuoteTodayPrice(quote)));

            if (itemQuoteIsRange(quote).equals("-1")) {
                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_red));

                img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_down));

            } else if (itemQuoteIsRange(quote).equals("1")) {
                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_quote_green));
                img_up_down.setImageDrawable(getApplicationContext().getResources().getDrawable(R.mipmap.icon_up));

            } else if (itemQuoteIsRange(quote).equals("0")) {

                text_lastPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                text_change.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));
                text_range.setTextColor(getApplicationContext().getResources().getColor(R.color.text_maincolor));

            }

            text_max.setText(itemQuoteMaxPrice(quote));
            text_min.setText(itemQuoteMinPrice(quote));
            text_volume.setText(itemQuoteVolume(quote));


        }
    }
    public void backgroundAlpha(float bgalpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgalpha;
        getWindow().setAttributes(lp);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        QuoteItemManger.getInstance().clear();
        QuoteItemManger.getInstance().cancelTimer();
    }
}
