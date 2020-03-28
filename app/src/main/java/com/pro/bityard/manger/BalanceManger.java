package com.pro.bityard.manger;

import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.RateEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.utils.TradeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class BalanceManger extends Observable {

    private BalanceManger() {

    }

    private static BalanceManger balanceManger = null;

    public static BalanceManger getInstance() {
        if (balanceManger == null) {
            synchronized (BalanceManger.class) {
                if (balanceManger == null) {
                    balanceManger = new BalanceManger();
                }
            }

        }
        return balanceManger;

    }

    public void postBalance(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void getBalance(String moneyType) {
        NetManger.getInstance().getRequest("/api/user/asset/list", null, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    //Log.d("print", "onNetResult:52: "+response.toString());
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {

                    } else if (tipEntity.getCode() == 200) {
                        BalanceEntity balanceEntity = new Gson().fromJson(response.toString(), BalanceEntity.class);


                        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                            if (data.getCurrency().equals(moneyType)) {
                                postBalance(data);
                            }
                        }


                    }

                } else if (state.equals(FAILURE)) {

                }
            }
        });
    }




    public void getBalance() {
        List<Double> balanceList=new ArrayList<>();

        NetManger.getInstance().getRequest("/api/user/asset/list", null, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    //Log.d("print", "onNetResult:52: "+response.toString());
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {

                    } else if (tipEntity.getCode() == 200) {
                        BalanceEntity balanceEntity = new Gson().fromJson(response.toString(), BalanceEntity.class);
                        Log.d("print", "onNetResult:59:  "+balanceEntity);

                        for (BalanceEntity.DataBean data:balanceEntity.getData()) {
                            String currency = data.getCurrency();
                            NetManger.getInstance().rate(data,currency, "USDT", new OnNetResult() {
                                @Override
                                public void onNetResult(String state, Object response) {
                                    if (state.equals(SUCCESS)){
                                        balanceList.add(Double.parseDouble(response.toString()));
                                        double balance=0.0;
                                        for (double a:balanceList) {
                                            balance=TradeUtil.add(balance,a);
                                        }
                                        Log.d("print", "onNetResult:110: "+balance);
                                    }
                                }
                            });

                        }
                        Log.d("print", "onNetResult:112:  "+balanceList);

                        postBalance(balanceEntity);




                    }

                } else if (state.equals(FAILURE)) {

                }
            }
        });
    }
    /**
     * 清理消息监听
     */
    public void clear() {

        deleteObservers();
        balanceManger = null;
    }

}
