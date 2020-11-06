package com.pro.bityard.manger;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.ChargeUnitEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class ChargeUnitManger extends Observable {


    private static ChargeUnitManger chargeUnitManger;


    public static ChargeUnitManger getInstance() {
        if (chargeUnitManger == null) {
            synchronized (ChargeUnitManger.class) {
                if (chargeUnitManger == null) {
                    chargeUnitManger = new ChargeUnitManger();
                }
            }

        }
        return chargeUnitManger;

    }

    public void chargeUnit(OnNetResult onNetResult) {
        NetManger.getInstance().codeList((state, response) -> {
            if (state.equals(SUCCESS)) {
                chargeJsonUnit(response.toString(), (state1, response1) -> {
                    if (state1.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response1);
                    }
                });

            }
        });
    }





    public void chargeJsonUnit(String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("code", codeList);
        NetManger.getInstance().getRequest("/api/trade/commodity/chargeUnit", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response.toString());
                    JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                    onNetResult.onNetResult(SUCCESS, jsonObject1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });

    }





    public void postChargeUnit(Object data) {
        setChanged();
        notifyObservers(data);

    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        chargeUnitManger = null;
    }


}
