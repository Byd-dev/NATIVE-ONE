package com.pro.bityard.manger;

import android.util.ArrayMap;

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

public class PositionRealManger extends Observable {

    private static PositionRealManger balanceManger = null;

    private PositionRealManger() {

    }

    public static PositionRealManger getInstance() {
        if (balanceManger == null) {
            synchronized (PositionRealManger.class) {
                if (balanceManger == null) {
                    balanceManger = new PositionRealManger();
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
        map.put("tradeType", "1");
        map.put("_", String.valueOf(new Date().getTime()));
        NetManger.getInstance().getRequest("/api/trade/scheme/holdings", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {

                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {

                    } else if (tipEntity.getCode() == 200) {
                        PositionEntity positionEntity = new Gson().fromJson(response.toString(), PositionEntity.class);
                        postPosition(positionEntity);

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
