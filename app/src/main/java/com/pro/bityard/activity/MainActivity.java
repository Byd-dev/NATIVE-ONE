package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.fragment.tab.HoldFragment;
import com.pro.bityard.fragment.tab.HomeFragment;
import com.pro.bityard.fragment.tab.MarketFragment;
import com.pro.bityard.fragment.tab.MyFragment;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    /**
     * 首页Tab索引
     */
    public static final class TAB_TYPE {
        public static final int COUNT = 4;
        public static final int TAB_HOME = 0;
        public static final int TAB_HALL = TAB_HOME + 1;
        public static final int TAB_POSITION = TAB_HALL + 1;
        public static final int TAB_INFORMATION = TAB_POSITION + 1;


    }


    private StringBuilder stringBuilder;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    public static void enter(Context context, int tabIndex) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IntentConfig.Keys.POSITION, tabIndex);
        context.startActivity(intent);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        //主题是深色的标题
        StatusBarUtil.setStatusBarDarkTheme(this, false);


        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();


    }

    private Object result = null;

    @Override
    protected void initData() {
        //获取国家code
        NetManger.getInstance().getRequest("/api/home/country/list", null, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {
                    CountryCodeEntity countryCodeEntity = new Gson().fromJson(response.toString(), CountryCodeEntity.class);
                    SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);

                } else if (state.equals(FAILURE)) {

                }
            }
        });


        String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST);
        String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE);
        if (quote_host.equals("") && quote_code.equals("")) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.text_err_init), Toast.LENGTH_SHORT).show();
            NetManger.getInstance().initQuote();

        } else {
            NetManger.getInstance().getQuote(quote_host, "/quote.jsp", quote_code, new OnNetResult() {
                @Override
                public void onNetResult(String state, Object response) {
                    if (state.equals(BUSY)) {

                    } else if (state.equals(SUCCESS)) {
                        String jsonReplace = Util.jsonReplace(response.toString());
                        QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                        String data = quoteEntity.getData();
                        String[] split = data.split(";");

                        Log.d("print", "onNetResult:返回行情数据:  " + data);
                    } else if (state.equals(FAILURE)) {

                    }
                }
            });

        }


    }


    @Override
    protected void initEvent() {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_0:
                showFragment(R.id.layout_fragment_containter, new HomeFragment(), null, null);

                break;
            case R.id.radio_1:
                showFragment(R.id.layout_fragment_containter, new MarketFragment(), null, null);


                break;

            case R.id.radio_2:


                break;

            case R.id.radio_3:
                showFragment(R.id.layout_fragment_containter, new HoldFragment(), null, null);


                break;
            case R.id.radio_4:
                showFragment(R.id.layout_fragment_containter, new MyFragment(), null, null);

                break;
        }
    }


}
