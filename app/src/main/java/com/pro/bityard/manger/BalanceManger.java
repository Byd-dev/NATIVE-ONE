package com.pro.bityard.manger;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
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

    private double prize;
    private double lucky;

    public double getLucky() {
        return lucky;
    }

    public void setLucky(double lucky) {
        this.lucky = lucky;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

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


        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", "1");

        NetManger.getInstance().getRequest("/api/user/asset/list", map, (state, response) -> {
            if (state.equals(BUSY)) {
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 401) {

                } else if (tipEntity.getCode() == 200) {
                    BalanceEntity balanceEntity = new Gson().fromJson(response.toString(), BalanceEntity.class);

                    for (BalanceEntity.DataBean data : balanceEntity.getData()) {
                        if (data.getCurrency().equals(moneyType)) {
                            setBalanceReal(data.getMoney());
                            setBalanceSim(data.getGame());
                            setPrize(data.getPrize());
                            setLucky(data.getLucky());
                        }
                    }
                    postBalance(balanceEntity);


                }

            } else if (state.equals(FAILURE)) {

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
