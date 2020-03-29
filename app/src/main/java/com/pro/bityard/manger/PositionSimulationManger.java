package com.pro.bityard.manger;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.TipEntity;

import java.util.Date;
import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class PositionSimulationManger extends Observable {

    private static PositionSimulationManger balanceManger = null;

    private PositionSimulationManger() {

    }

    public static PositionSimulationManger getInstance() {
        if (balanceManger == null) {
            synchronized (PositionSimulationManger.class) {
                if (balanceManger == null) {
                    balanceManger = new PositionSimulationManger();
                }
            }

        }
        return balanceManger;

    }

    public void postPosition(Object data) {

        setChanged();
        notifyObservers(data);

    }


    /*持仓列表*/
    public void getHold() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("tradeType", "2");
        map.put("_", String.valueOf(new Date().getTime()));
        NetManger.getInstance().getRequest("/api/trade/scheme/holdings", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    Log.d("print", "onNetResult:模拟持仓58: "+response.toString());
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {

                    } else if (tipEntity.getCode() == 200) {
                        PositionEntity positionEntity = new Gson().fromJson(response.toString(), PositionEntity.class);

                        postPosition(positionEntity.getData());

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
