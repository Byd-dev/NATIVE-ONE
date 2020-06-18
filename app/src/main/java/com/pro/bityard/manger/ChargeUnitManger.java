package com.pro.bityard.manger;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.ChargeUnitEntity;

import org.json.JSONObject;

import java.util.ArrayList;
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
                chargeUnit(response.toString(), (state1, response1) -> {
                    if (state1.equals(SUCCESS)) {
                        chargeUnit(response1.toString(), (state2, response2) -> {
                            if (state2.equals(SUCCESS)) {
                                onNetResult.onNetResult(SUCCESS, response2);
                            }

                        });
                    }
                });
            }
        });
    }


    List<ChargeUnitEntity> chargeUnitEntityList;

    public List<ChargeUnitEntity> getChargeUnitEntityList() {
        return chargeUnitEntityList;
    }

    public void setChargeUnitEntityList(List<ChargeUnitEntity> chargeUnitEntityList) {
        this.chargeUnitEntityList = chargeUnitEntityList;
    }

    public void chargeUnit(String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("code", codeList);
        String[] codeSplitList = codeList.split(";");
        NetManger.getInstance().getRequest("/api/trade/commodity/chargeUnit", map, new OnNetResult() {

            private ChargeUnitEntity chargeUnitEntity;

            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);

                } else if (state.equals(SUCCESS)) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                        chargeUnitEntityList = new ArrayList<>();
                        for (int i = 0; i < jsonObject1.length(); i++) {
                            for (int j = codeSplitList.length - 1; j > 0; j--) {
                                JSONObject trxusdt = (JSONObject) jsonObject1.get(codeSplitList[i]);  //trxusdt.length() =7
                                chargeUnitEntity = new Gson().fromJson(trxusdt.toString(), ChargeUnitEntity.class);
                                chargeUnitEntity.setCode(codeSplitList[i]);//因为后台返回没有加code  例如: BTCUSDT
                            }
                            chargeUnitEntityList.add(chargeUnitEntity);
                        }
                        setChargeUnitEntityList(chargeUnitEntityList);
                        onNetResult.onNetResult(SUCCESS, chargeUnitEntity);
                        postChargeUnit(chargeUnitEntityList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);
                    setChargeUnitEntityList(null);

                }
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
