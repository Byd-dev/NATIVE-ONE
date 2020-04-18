package com.pro.bityard.manger;

import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.TipEntity;

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

    private double balanceReal;
    private double balanceSim;


    public double getBalanceReal() {
        return balanceReal;
    }

    public void setBalanceReal(double balanceReal) {
        this.balanceReal = balanceReal;
    }

    public double getBalanceSim() {
        return balanceSim;
    }

    public void setBalanceSim(double balanceSim) {
        this.balanceSim = balanceSim;
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
                        Log.d("print", "onNetResult:59:  " + balanceEntity);

                        for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                            if (data.getCurrency().equals(moneyType)) {
                                setBalanceReal(data.getMoney());
                                setBalanceSim(data.getGame());

                            }
                        }
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
