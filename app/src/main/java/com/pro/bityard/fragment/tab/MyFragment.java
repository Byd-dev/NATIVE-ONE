package com.pro.bityard.fragment.tab;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.activity.LoginActivity;
import com.pro.bityard.activity.UserActivity;
import com.pro.bityard.base.BaseFragment;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.config.IntentConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.manger.BalanceManger;
import com.pro.bityard.manger.TagManger;
import com.pro.bityard.utils.TradeUtil;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class MyFragment extends BaseFragment implements View.OnClickListener, Observer {


    @BindView(R.id.text_userName)
    TextView text_userName;

    @BindView(R.id.text_uid)
    TextView text_uid;

    @BindView(R.id.text_balance)
    TextView text_balance;


    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
            Log.d("print", "onResume:30:  " + data);
            text_userName.setText(data.getUser().getAccount());
            text_uid.setText(data.getUser().getUserId());
        } else {
            text_userName.setText(getResources().getText(R.string.text_unlogin));
            text_uid.setText("--");

        }

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    protected void initView(View view) {

        //余额注册
        BalanceManger.getInstance().addObserver(this);
        view.findViewById(R.id.layout_six).setOnClickListener(this);

        TagManger.getInstance().addObserver(this);


    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void intPresenter() {

    }

    @Override
    protected void initData() {
        //余额注册
        BalanceManger.getInstance().getBalance("USDT");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_six:
                if (isLogin()) {
                    UserActivity.enter(getContext(), IntentConfig.Keys.KEY_SET_UP);
                } else {
                    LoginActivity.enter(getContext(), IntentConfig.Keys.KEY_LOGIN);
                }
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == BalanceManger.getInstance()) {
            BalanceEntity data = (BalanceEntity) arg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (text_balance != null) {
                        for (BalanceEntity.DataBean data : data.getData()) {
                            if (data.getCurrency().equals("USDT")) {
                                text_balance.setText(TradeUtil.getNumberFormat(data.getMoney(), 2));
                            }
                        }

                    }
                }
            });
        }else if (o==TagManger.getInstance()){
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BalanceManger.getInstance().clear();
        TagManger.getInstance().clear();
    }
}
