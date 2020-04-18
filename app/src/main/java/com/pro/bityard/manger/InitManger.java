package com.pro.bityard.manger;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.switchlibrary.SPUtils;

import java.util.Observable;

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

        NetManger.getInstance().isLogin(response -> {
            boolean isLogin = (boolean) response;
            if (isLogin == true) {

            } else {
                SPUtils.remove(AppConfig.LOGIN);
            }
        });

        //获取国家code
        NetManger.getInstance().getRequest("/api/home/country/list", null, (state, response) -> {
            if (state.equals(SUCCESS)) {
                CountryCodeEntity countryCodeEntity = new Gson().fromJson(response.toString(), CountryCodeEntity.class);
                SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);

            }
        });

        String cny = SPUtils.getString(AppConfig.CURRENCY, "CNY");
        //获取USDT兑换CNY汇率
        NetManger.getInstance().getItemRate("1", cny, response -> {
            SPUtils.putString(AppConfig.USD_RATE, response.toString());
        });


        //合约号初始化
        TradeListManger.getInstance().tradeList((state, response) -> {
            if (state.equals(SUCCESS)) {
            }

        });


        /*手续费*/
        ChargeUnitManger.getInstance().chargeUnit((state, response) -> {
            if (state.equals(SUCCESS)) {
            }
        });
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
