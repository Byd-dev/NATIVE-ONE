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
import com.pro.bityard.api.OnNetHostResult;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.QuoteEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.fragment.tab.HoldFragment;
import com.pro.bityard.fragment.tab.HomeFragment;
import com.pro.bityard.fragment.tab.MarketFragment;
import com.pro.bityard.fragment.tab.MyFragment;
import com.pro.bityard.utils.Util;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.AES;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;

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


        NetManger.getInstance().initURL(new OnNetHostResult() {

            @Override
            public void setResult(String state, Object response1, Object response2) {
                if (state.equals(BUSY)) {

                } else if (state.equals(SUCCESS)) {

                    List<TradeListEntity> tradeListEntityList = (List<TradeListEntity>) response2;
                    stringBuilder = new StringBuilder();
                    for (int i = 0; i < tradeListEntityList.size(); i++) {
                        Log.d("print", "onNetResult:返回得到的数值:  " + tradeListEntityList.get(i).getContractCode());
                        stringBuilder.append(tradeListEntityList.get(i).getContractCode() + ",");
                    }

                    NetManger.getInstance().getQuote(response1.toString(), "/quote.jsp", stringBuilder.toString(), new OnNetResult() {
                        @Override
                        public void onNetResult(String state, Object response) {
                            if (state.equals(BUSY)) {

                            } else if (state.equals(SUCCESS)) {
                                String jsonReplace = Util.jsonReplace(response.toString());
                                QuoteEntity quoteEntity = new Gson().fromJson(jsonReplace, QuoteEntity.class);
                                String data = quoteEntity.getData();
                                Log.d("print", "onNetResult:返回行情数据:  " + data);
                            } else if (state.equals(FAILURE)) {

                            }
                        }
                    });
                    result = response1;


                } else if (state.equals(FAILURE)) {

                }
            }
        });

        if (result == null) {
            return;
        } else {

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
