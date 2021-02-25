package com.pro.bityard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.fragment.tab.HoldFragment;
import com.pro.bityard.fragment.tab.HoldRadioFragment;
import com.pro.bityard.fragment.tab.HomeFragment;
import com.pro.bityard.fragment.tab.MarketFragment;
import com.pro.bityard.fragment.tab.MyFragment;
import com.pro.bityard.manger.ChargeUnitManger;
import com.pro.bityard.manger.SocketQuoteManger;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.SUCCESS;
import static com.pro.bityard.config.AppConfig.QUOTE_SECOND;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, Observer {


    private List<String> quoteList;

    @Override
    public void update(Observable o, Object arg) {
        if (o == SocketQuoteManger.getInstance()) {
            ArrayMap<String, List<String>> arrayMap = (ArrayMap<String, List<String>>) arg;
            quoteList = arrayMap.get("1");
        }
    }

    /**
     * 首页Tab索引
     */
    public static final class TAB_TYPE {
        public static final int COUNT = 4;
        public static final int TAB_HOME = 0;
        public static final int TAB_HALL = 1;
        public static final int TAB_POSITION = 2;
        public static final int TAB_MY = 3;


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    public static void enter(Context context, int tabIndex) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IntentConfig.Keys.POSITION, tabIndex);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            int position = getIntent().getIntExtra(IntentConfig.Keys.POSITION, 0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (position == 0) {
                radioGroup.getChildAt(0).performClick();
            } else if (position == 1) {

            } else if (position == 2) {

            } else if (position == 3) {
                radioGroup.getChildAt(3).performClick();
            }

        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        //主题是深色的标题
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        //打开沉浸式状态栏
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);


        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.getChildAt(0).performClick();


        //初始化 交易设置


    }


    @Override
    protected void initData() {

        SocketQuoteManger.getInstance().addObserver(this);

        //获取国家code

        NetManger.getInstance().getMobileCountryCode((state, response) -> {

        });
        //合约号初始化


        /*手续费*/
        ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
            if (state.equals(SUCCESS)) {
                Toast.makeText(MainActivity.this, "手续费获取成功", Toast.LENGTH_SHORT).show();

            }
        });
        //行情
        String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
        String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
        if (quote_host == null && quote_code == null) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.text_err_tip), Toast.LENGTH_SHORT).show();
            NetManger.getInstance().initQuote();
        } else {
            assert quote_host != null;
        }

        if (quoteList != null) {
            findViewById(R.id.radio_2).setOnClickListener(v -> QuoteDetailActivity.enter(MainActivity.this, "1", quoteList.get(0)));
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


            case R.id.radio_3:
                showFragment(R.id.layout_fragment_containter, new HoldRadioFragment(), null, null);


                break;
            case R.id.radio_4:
                showFragment(R.id.layout_fragment_containter, new MyFragment(), null, null);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketQuoteManger.getInstance().clear();
    }
}
