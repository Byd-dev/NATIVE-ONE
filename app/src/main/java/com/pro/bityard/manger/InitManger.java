package com.pro.bityard.manger;

import android.util.ArrayMap;

import com.pro.bityard.api.NetManger;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.FundItemEntity;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.entity.UnionRateEntity;
import com.pro.switchlibrary.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class InitManger extends Observable {

    private InitManger() {

    }

    private static InitManger initManger = null;

    public static InitManger getInstance() {
        if (initManger == null) {
            synchronized (InitManger.class) {
                if (initManger == null) {
                    initManger = new InitManger();
                }
            }

        }
        return initManger;

    }

    public void postTag(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void init() {

        //判断是否登录
        NetManger.getInstance().isLogin(response -> {
            boolean isLogin = (boolean) response;
            if (isLogin == true) {
               /* PositionRealManger.getInstance().getHold();
                PositionSimulationManger.getInstance().getHold();*/
            } else {
                SPUtils.remove(AppConfig.LOGIN);
            }
        });


        /*获取币种*/
        NetManger.getInstance().currencyList("1", (state, response) -> {
            if (state.equals(BUSY)) {

            } else if (state.equals(SUCCESS)) {

                FundItemEntity fundItemEntity = (FundItemEntity) response;
                SPUtils.putData(AppConfig.SCALE, fundItemEntity);

            } else if (state.equals(FAILURE)) {

            }
        });


        String currencyList = SPUtils.getString(AppConfig.SUPPORT_CURRENCY, null);

        if (currencyList != null) {
            /*获得多币种汇率*/
            NetManger.getInstance().getRateList(currencyList, "USDT", (state2, response2) -> {
                if (state2.equals(SUCCESS)) {
                    RateListEntity rateListEntity = (RateListEntity) response2;
                    SPUtils.putData(AppConfig.RATE_LIST, rateListEntity);
                }
            });
        } else {
            NetManger.getInstance().getInit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    InitEntity initEntity = (InitEntity) response;
                    String supportCurrency = initEntity.getBrand().getSupportCurrency();
                    /*获得多币种汇率*/
                    NetManger.getInstance().getRateList(supportCurrency, "USDT", (state2, response2) -> {
                        if (state2.equals(SUCCESS)) {
                            RateListEntity rateListEntity = (RateListEntity) response2;
                            SPUtils.putData(AppConfig.RATE_LIST, rateListEntity);
                        }
                    });
                }
            });

        }


        //获取佣金比例
        NetManger.getInstance().unionRate((state, response) -> {
            if (state.equals(SUCCESS)) {
                UnionRateEntity unionRateEntity = (UnionRateEntity) response;
                //退出需要清除
                SPUtils.putData(AppConfig.KEY_UNION, unionRateEntity);
            }
        });

        //获取国家code
        NetManger.getInstance().getMobileCountryCode((state, response) -> {

        });








        /*获取原先配置的货币单位汇率*/
        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        //获取USDT兑换CNY汇率
        NetManger.getInstance().getItemRate("1", cny, response -> {
            SPUtils.putString(AppConfig.USD_RATE, response.toString());
        });





        /*手续费*/

        //行情
        String quote_host = SPUtils.getString(AppConfig.QUOTE_HOST, null);
        String quote_code = SPUtils.getString(AppConfig.QUOTE_CODE, null);
        if (quote_host == null && quote_code == null) {
            NetManger.getInstance().initQuote();
        } else {
            assert quote_host != null;
            QuoteListManger.getInstance().quote(quote_host, quote_code);
        }

    }

    /**
     * 清理消息监听
     */
    public void clear() {

        deleteObservers();
        initManger = null;
    }

}
