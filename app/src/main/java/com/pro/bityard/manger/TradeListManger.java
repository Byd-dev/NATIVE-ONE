package com.pro.bityard.manger;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.TradeListEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class TradeListManger extends Observable {

    private TradeListManger() {

    }

    private static TradeListManger tradeListManger = null;

    public static TradeListManger getInstance() {
        if (tradeListManger == null) {
            synchronized (TradeListManger.class) {
                if (tradeListManger == null) {
                    tradeListManger = new TradeListManger();
                }
            }

        }
        return tradeListManger;

    }

    public void postTradeList(Object data) {

        setChanged();
        notifyObservers(data);

    }



    public void tradeList(OnNetResult onNetResult) {

        NetManger.getInstance().codeList((state, response) -> {
            if (state.equals(SUCCESS)) {
                getTradeList(response.toString(), (state1, response1) -> {
                    if (state1.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response1);
                    }
                });
            }
        });

    }

    private List<TradeListEntity> tradeListEntityList;

    public List<TradeListEntity> getTradeListEntityList() {
        return tradeListEntityList;
    }

    public void setTradeListEntityList(List<TradeListEntity> tradeListEntityList) {
        this.tradeListEntityList = tradeListEntityList;
    }

    /*获取合约号*/
    private void getTradeList(String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("code", codeList);
        String[] codeSplitList = codeList.split(";");
        NetManger.getInstance().getRequest("/api/trade/commodity/tradeList", map, new OnNetResult() {

            private TradeListEntity tradeListEntity;

            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);

                } else if (state.equals(SUCCESS)) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");

                        tradeListEntityList = new ArrayList<>();
                        for (int i = 0; i < jsonObject1.length(); i++) {
                            for (int j = codeSplitList.length - 1; j > 0; j--) {
                                JSONObject trxusdt = (JSONObject) jsonObject1.get(codeSplitList[i]);  //trxusdt.length() =46
                                tradeListEntity = new Gson().fromJson(trxusdt.toString(), TradeListEntity.class);
                            }
                            tradeListEntityList.add(tradeListEntity);


                        }
                        setTradeListEntityList(tradeListEntityList);
                        onNetResult.onNetResult(SUCCESS, tradeListEntity);
                        postTradeList(tradeListEntityList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);
                    setTradeListEntityList(null);

                }
            }
        });
    }

    /**
     * 清理消息监听
     */
    public void clear() {

        deleteObservers();
        tradeListManger = null;
    }

}
