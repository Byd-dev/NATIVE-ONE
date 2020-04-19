package com.pro.bityard.api;


import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.CurrencyListEntity;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.IsLoginEntity;
import com.pro.bityard.entity.OrderEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.RateEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.TipSPSLMarginEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.AES;
import com.pro.switchlibrary.SPUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NetManger {
    private String TAG = "NetManger";

    public static NetManger instance;

    public static String BUSY = "busy";
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";

    public static String BASE_URL = "http://test.bityard.com";   //测试

    //  public static String BASE_URL = "https://www.bityard.com";    //正式

    public static NetManger getInstance() {
        if (instance == null) {
            instance = new NetManger();
        }

        return instance;
    }

    //get 请求
    public void getRequest(String url, ArrayMap map, OnNetResult onNetResult) {
        Log.d("NetManger ", "getRequest:get请求地址:  " + getURL(url, map));
        OkGo.<String>get(getURL(url, map))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, null);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!TextUtils.isEmpty(response.body())) {
                            onNetResult.onNetResult(SUCCESS, response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        onNetResult.onNetResult(FAILURE, response.body());
                    }
                });

    }


    //post 请求
    public void postRequest(String url, ArrayMap map, OnNetResult onNetResult) {

        OkGo.<String>post(getURL(url, map))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, null);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!TextUtils.isEmpty(response.body())) {
                            onNetResult.onNetResult(SUCCESS, response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        onNetResult.onNetResult(FAILURE, response.body());
                    }
                });

    }


    //动态host get 请求
    public void getHostRequest(String host, String url, ArrayMap map, OnNetResult onNetResult) {
        Log.d("NetManger", "getHostRequest:动态:  " + getHostURL(host, url, map));
        OkGo.<String>get(getHostURL(host, url, map))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, null);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!TextUtils.isEmpty(response.body())) {
                            onNetResult.onNetResult(SUCCESS, response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        onNetResult.onNetResult(FAILURE, response.body());
                    }
                });

    }


    //URL拼接参数
    public String getURL(String url, ArrayMap map) {

        String substring_url = null;
        if (map == null) {
            return BASE_URL + url;
        } else {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                StringBuilder append = stringBuilder.append(key).append("=").append(value).append("&");
                substring_url = append.toString().substring(0, append.toString().length() - 1);
            }
            String url_result = BASE_URL + url + "?" + substring_url;
            Log.d("NetManger", "getURL:请求地址:  " + url_result);

            return url_result;
        }


    }

    //动态URL拼接参数
    public String getHostURL(String host, String url, ArrayMap map) {
        Log.d("NetManger", "getURL:HOST参数:  " + map);

        String substring_url = null;
        if (map == null) {
            return host + url;
        } else {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                StringBuilder append = stringBuilder.append(key).append("=").append(value).append("&");
                substring_url = append.toString().substring(0, append.toString().length() - 1);
            }
            String url_result = host + url + "?" + substring_url;
            return url_result;
        }


    }

    /*行情地址host 合约号 合约号详情 的方法  关键*/
    public void getHostCodeTradeList(OnNetThreeResult onNetThreeResult) {

        initURL(new OnNetTwoResult() {
            @Override
            public void setResult(String state, Object response1, Object response2) {
                if (state.equals(BUSY)) {
                    onNetThreeResult.setResult(BUSY, null, null, null);

                } else if (state.equals(SUCCESS)) {

                    List<TradeListEntity> tradeListEntityList = (List<TradeListEntity>) response2;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < tradeListEntityList.size(); i++) {
                        stringBuilder.append(tradeListEntityList.get(i).getContractCode() + ",");
                    }
                    onNetThreeResult.setResult(SUCCESS, response1, stringBuilder.toString(), tradeListEntityList);

                } else if (state.equals(FAILURE)) {
                    onNetThreeResult.setResult(FAILURE, null, null, null);

                }
            }
        });
    }

    /*获取codelist*/
    public void codeList(OnNetResult onNetResult) {
        /*获取行情的host*/
        NetManger.getInstance().getRequest("/api/trade/commodity/initial", null, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    InitEntity initEntity = new Gson().fromJson(response.toString(), InitEntity.class);
                    List<InitEntity.GroupBean> group = initEntity.getGroup();
                    // TODO: 2020/3/13 暂时这里只固定是数字货币的遍历
                    for (InitEntity.GroupBean data : group) {
                        if (data.getName().equals("数字货币")) {
                            String list = data.getList();
                            onNetResult.onNetResult(SUCCESS, list);
                        }
                    }
                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }


    //行情init初始化
    public void initURL(OnNetTwoResult onNetResult) {
        /*获取行情的host*/
        getRequest("/api/trade/commodity/initial", null, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    InitEntity initEntity = new Gson().fromJson(response.toString(), InitEntity.class);
                    List<InitEntity.GroupBean> group = initEntity.getGroup();
                    // TODO: 2020/3/13 暂时这里只固定是数字货币的遍历
                    for (InitEntity.GroupBean data : group) {
                        if (data.getName().equals("数字货币")) {
                            String list = data.getList();
                            getTradeList(list, new OnNetResult() {
                                @Override
                                public void onNetResult(String state, Object response) {
                                    if (state.equals(BUSY)) {

                                    } else if (state.equals(SUCCESS)) {
                                        String quoteDomain = initEntity.getQuoteDomain();//获取域名
                                        onNetResult.setResult(SUCCESS, quoteDomain, response);

                                    } else if (state.equals(FAILURE)) {
                                        onNetResult.setResult(FAILURE, null, response);
                                    }
                                }
                            });//获取合约号
                        }
                    }
                } else if (state.equals(FAILURE)) {
                    onNetResult.setResult(FAILURE, null, null);
                }
            }
        });
    }


    private List<TradeListEntity> tradeListEntityList;


    /*获取合约号*/
    private void getTradeList(String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("code", codeList);
        String[] codeSplitList = codeList.split(";");
        getRequest("/api/trade/commodity/tradeList", map, new OnNetResult() {

            private TradeListEntity tradeListEntity;

            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                } else if (state.equals(SUCCESS)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.toString());
                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                        Log.d("NetManger", "onNetResult:250:  " + jsonObject1.length());

                        Log.d("NetManger", "onNetResult:252: " + codeSplitList.length);

                        tradeListEntityList = new ArrayList<>();
                        for (int i = 0; i < jsonObject1.length(); i++) {
                            for (int j = codeSplitList.length - 1; j > 0; j--) {
                                JSONObject trxusdt = (JSONObject) jsonObject1.get(codeSplitList[i]);  //trxusdt.length() =46
                                // Log.d("NetManger", "onNetResult: 258:"+trxusdt.length());
                                tradeListEntity = new Gson().fromJson(trxusdt.toString(), TradeListEntity.class);
                                //  Log.d("NetManger", "onNetResult:260: "+tradeListEntity);
                            }
                            tradeListEntityList.add(tradeListEntity);
                            // Log.d("NetManger", "onNetResult:263:  "+tradeListEntityList.size());


                        }
                        onNetResult.onNetResult(SUCCESS, tradeListEntityList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, "获取合约号失败");
                }
            }
        });
    }

    int count = 0;

    /*获取行情*/
    public void getQuote(String quoteDomain, String url, String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("callback", "?");
        map.put("code", codeList);
        map.put("_", String.valueOf(new Date().getTime()));
        map.put("simple", "true");

        try {
            String urlList = AES.HexDecrypt(quoteDomain.getBytes(), AppConfig.S_KEY);

            String[] split = urlList.split(";");
            int length = split.length;
            Log.d("NetManger", "getQuote:324: 请求次数: " + count + "请求地址长度: " + length + "  --   " + urlList);
            if (count < length) {
                getHostRequest(split[count], url, map, new OnNetResult() {
                    @Override
                    public void onNetResult(String state, Object response) {
                        if (state.equals(BUSY)) {

                        } else if (state.equals(SUCCESS)) {
                            onNetResult.onNetResult(SUCCESS, response.toString());
                        } else if (state.equals(FAILURE)) {
                            if (length == 0) {

                            } else {
                                count++;
                            }
                        }
                    }
                });
            } else {
                count = 0;//这里是重置
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /*获取单个行情*/
    public void getItemQuote(String quoteDomain, String url, String code, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("callback", "?");
        map.put("code", code);
        map.put("_", String.valueOf(new Date().getTime()));

        try {
            String urlList = AES.HexDecrypt(quoteDomain.getBytes(), AppConfig.S_KEY);
            Log.d("quoteItem", "getItemQuote:390:  " + urlList);
            String[] split = urlList.split(";");
            int length = split.length;
            Log.d("quoteItem", "getItemQuote:393:  " + count + "  --   " + split[count]);
            if (count < length) {
                getHostRequest(split[count], url, map, (state, response) -> {
                    if (state.equals(BUSY)) {

                    } else if (state.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response.toString());
                    } else if (state.equals(FAILURE)) {
                        if (length == 0) {
                        } else {
                            count++;
                        }
                        onNetResult.onNetResult(FAILURE, null);

                    }
                });
            } else {
                count = 0;//这里是重置
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*获取单个行情图*/
    public void getQuoteChart(String quoteDomain, String url, String contactCode, String resolution, OnNetResult onNetResult) {
        Calendar nowBefore = Calendar.getInstance();
        nowBefore.add(Calendar.MINUTE, -10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("callback", "%3F");
        map.put("symbol", contactCode);
        map.put("resolution", resolution);
        map.put("from", Util.dateToStamp(sdf.format(nowBefore.getTimeInMillis())));
        map.put("to", Util.dateToStamp(sdf.format(System.currentTimeMillis())));
        map.put("_", Util.dateToStamp(sdf.format(System.currentTimeMillis())));
        try {
            String urlList = AES.HexDecrypt(quoteDomain.getBytes(), AppConfig.S_KEY);
            Log.d("quoteItem", "getItemQuote:390:  " + urlList);
            String[] split = urlList.split(";");
            int length = split.length;
            Log.d("quoteItem", "getItemQuote:393:  " + count + "  --   " + split[count]);
            if (count < length) {
                getHostRequest(split[count], url, map, (state, response) -> {
                    if (state.equals(BUSY)) {

                    } else if (state.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response.toString());
                    } else if (state.equals(FAILURE)) {
                        if (length == 0) {
                        } else {
                            count++;
                        }
                        onNetResult.onNetResult(FAILURE, null);
                    }
                });
            } else {
                count = 0;//这里是重置
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    int countHistory = 0;

    /*获取单个历史行情图*/
    public void getQuoteHistory(String quoteDomain, int times, String url, String contactCode, String resolution, OnNetResult onNetResult) {
        Calendar nowBefore2 = Calendar.getInstance();

        Calendar nowBefore = Calendar.getInstance();
        switch (resolution) {
            case "1":
                nowBefore.add(Calendar.MINUTE, -300);
                break;
            case "3":
                nowBefore.add(Calendar.MINUTE, -300 * 3);
                break;
            case "5":
                nowBefore.add(Calendar.MINUTE, -300 * 5);
                break;
            case "15":
                nowBefore.add(Calendar.MINUTE, -300 * 15);
                break;
            case "60":
                nowBefore.add(Calendar.MINUTE, -300 * 60);
                break;
           /* case "60":
                nowBefore.add(Calendar.DAY_OF_YEAR, times);
                break;*/
            case "D":
                nowBefore.add(Calendar.YEAR, times);
                break;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        nowBefore2.add(Calendar.MINUTE, 0);

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("symbol", contactCode);
        map.put("resolution", resolution);
        map.put("from", Util.dateToStamp(sdf.format(nowBefore.getTimeInMillis())));
        map.put("to", Util.dateToStamp(sdf.format(nowBefore2.getTimeInMillis())));


        getHostRequest(quoteDomain, url, map, (state, response) -> {
            if (state.equals(BUSY)) {

            } else if (state.equals(SUCCESS)) {
                onNetResult.onNetResult(SUCCESS, response.toString());
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });


    }

    public void initQuote() {
        /*初始化获取行情 合约号 行情地址*/
        getHostCodeTradeList(new OnNetThreeResult() {
            @Override
            public void setResult(String state, Object response1, Object response2, Object response3) {
                if (state.equals(SUCCESS)) {
                    SPUtils.putString(AppConfig.QUOTE_HOST, response1.toString());
                    SPUtils.putString(AppConfig.QUOTE_CODE, response2.toString());
                    SPUtils.putString(AppConfig.QUOTE_DETAIL, response3.toString());
                }
            }
        });
    }

    /*持仓列表*/
    public void getHold(String tradeType, OnNetTwoResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("tradeType", tradeType);
        map.put("_", String.valueOf(new Date().getTime()));
        getRequest("/api/trade/scheme/holdings", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.setResult(BUSY, null, null);
                } else if (state.equals(SUCCESS)) {

                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {
                        onNetResult.setResult(FAILURE, null, null);

                    } else if (tipEntity.getCode() == 200) {
                        PositionEntity positionEntity = new Gson().fromJson(response.toString(), PositionEntity.class);
                        //  List<String> quoteList = QuoteManger.getInstance().getQuoteList();
                        onNetResult.setResult(SUCCESS, positionEntity, null);


                    }

                } else if (state.equals(FAILURE)) {
                    onNetResult.setResult(FAILURE, null, null);

                }
            }
        });
    }

    /*平仓*/
    public void close(String id, String tradeType, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingId", id);
        map.put("tradeType", tradeType);
        map.put("source", "下单");
        postRequest("/api/trade/close.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    TipCloseEntity tipCloseEntity = new Gson().fromJson(response.toString(), TipCloseEntity.class);
                    onNetResult.onNetResult(SUCCESS, tipCloseEntity);

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    /*一键平仓*/
    public void closeAll(String idList, String tradeType, OnNetResult onNetResult) {

        if (idList == null) {
            return;
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingList", idList);
        map.put("tradeType", tradeType);
        map.put("source", "一键平仓");
        postRequest("/api/trade/close.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    TipCloseEntity tipCloseEntity = new Gson().fromJson(response.toString(), TipCloseEntity.class);
                    onNetResult.onNetResult(SUCCESS, tipCloseEntity);
                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    public void currencyList(OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", "0");//0是货币 1是法币
        getRequest("/api/home/currency/list", map, (state, response) -> {
            Log.d("print", "assetList:625:  " + response);
            if (state.equals(SUCCESS)) {
                CurrencyListEntity currencyListEntity = new Gson().fromJson(response.toString(), CurrencyListEntity.class);
                onNetResult.onNetResult(SUCCESS, currencyListEntity);
            }
        });

    }

    /*挂单列表*/
    public void getPending(String tradeType, OnNetTwoResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("schemeSort", "4");
        map.put("tradeType", tradeType);
        map.put("beginTime", "");
        map.put("_", String.valueOf(new Date().getTime()));
        getRequest("/api/trade/scheme/limit", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.setResult(BUSY, null, null);
            } else if (state.equals(SUCCESS)) {

                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 401) {
                    onNetResult.setResult(FAILURE, null, null);

                } else if (tipEntity.getCode() == 200) {
                    PositionEntity positionEntity = new Gson().fromJson(response.toString(), PositionEntity.class);


                    onNetResult.setResult(SUCCESS, positionEntity, null);


                }

            } else if (state.equals(FAILURE)) {
                onNetResult.setResult(FAILURE, null, null);

            }
        });
    }


    /*撤单*/
    public void cancel(String id, String tradeType, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingId", id);
        map.put("tradeType", tradeType);
        map.put("source", "取消挂单");
        postRequest("/api/trade/revoke.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    TipCloseEntity tipCloseEntity = new Gson().fromJson(response.toString(), TipCloseEntity.class);
                    onNetResult.onNetResult(SUCCESS, tipCloseEntity);

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    /*持仓历史*/
    public void getHistory(String tradeType, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("schemeSort", "2");
        map.put("tradeType", tradeType);
        map.put("_", String.valueOf(new Date().getTime()));
        getRequest("/api/trade/scheme/history", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {

                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {
                        onNetResult.onNetResult(FAILURE, null);

                    } else if (tipEntity.getCode() == 200) {
                        HistoryEntity historyEntity = new Gson().fromJson(response.toString(), HistoryEntity.class);


                        onNetResult.onNetResult(SUCCESS, historyEntity);


                    }

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    /*下单*/
    public void order(String tradeType, String leverType,
                      String commodity, String contract, String isBuy, String margin,
                      String lever, String price, String defer, String deferFee,
                      String stopProfit, String stopLoss, String serviceCharge,
                      String eagleDeduction, String volume, String moneyType,
                      String currency,
                      OnNetResult onNetResult) {
        String SEED = "0Aa1Bb2Cc3Dd4Ee5Ff6Gg7Hh8Ii9Jj0Kk1Ll2Mm3Nn4Oo5Pp6Qq7Rr8Ss9Tt0Uu1Vv2Ww3Xx4Yy5Zz6789";

        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int i1 = random.nextInt(SEED.length());
            stringBuffer.append(SEED.charAt(i1));
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("identity", stringBuffer.toString());
        map.put("tradeType", tradeType);
        map.put("leverType", leverType);
        map.put("source", "下单");
        map.put("commodity", commodity);
        map.put("contract", contract);
        map.put("isBuy", isBuy);
        map.put("margin", margin);
        map.put("lever", lever);
        map.put("price", price);
        map.put("defer", defer);
        map.put("deferFee", deferFee);
        map.put("stopProfit", stopProfit);
        map.put("stopLoss", stopLoss);
        map.put("serviceCharge", serviceCharge);
        map.put("eagleDeduction", eagleDeduction);
        map.put("volume", volume);
        map.put("moneyType", moneyType);
        map.put("platform", "Android");
        map.put("currency", currency);
        postRequest("/api/trade/open.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    OrderEntity orderEntity = new Gson().fromJson(response.toString(), OrderEntity.class);
                    onNetResult.onNetResult(SUCCESS, orderEntity.getMessage());

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }


    /*设置止盈止损*/
    public void submitSPSL(String bettingId, String tradeType, String stopProfit, String stopLoss, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingId", bettingId);
        map.put("tradeType", tradeType);
        map.put("stopProfit", stopProfit);
        map.put("stopLoss", stopLoss);
        map.put("source", "设置止盈止损");
        postRequest("/api/trade/spsl.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    Log.d("print", "onNetResult:设置止盈止损:  " + response.toString());
                    TipSPSLMarginEntity tipSPSLMarginEntity = new Gson().fromJson(response.toString(), TipSPSLMarginEntity.class);
                    if (tipSPSLMarginEntity.getCode() == 200) {

                        onNetResult.onNetResult(SUCCESS, tipSPSLMarginEntity.getMessage());
                    } else {
                        onNetResult.onNetResult(FAILURE, tipSPSLMarginEntity.getMessage());

                    }

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    /*追加保证金*/
    public void submitMargin(String bettingId, String margin, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingId", bettingId);
        map.put("margin", margin);
        postRequest("/api/trade/margin.htm", map, new OnNetResult() {
            @Override
            public void onNetResult(String state, Object response) {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {
                    Log.d("print", "onNetResult:583:  " + response.toString());
                    TipSPSLMarginEntity tipSPSLMarginEntity = new Gson().fromJson(response.toString(), TipSPSLMarginEntity.class);
                    if (tipSPSLMarginEntity.getCode() == 200) {
                        onNetResult.onNetResult(SUCCESS, tipSPSLMarginEntity.getMessage());
                    } else {
                        onNetResult.onNetResult(FAILURE, tipSPSLMarginEntity.getMessage());

                    }

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            }
        });
    }

    private boolean isEmpty = false;

    /*汇率*/
    public void rate(BalanceEntity.DataBean dataBean, String moneyType, String src, String des, OnNetResult onNetResult) {
        Log.d("print", "rate:832:  " + isEmpty + "---" + src);
        if (src.equals(AppConfig.USDT)) {
            String rate = SPUtils.getString(AppConfig.USDT, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.BTC)) {
            String rate = SPUtils.getString(AppConfig.BTC, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.ETH)) {
            String rate = SPUtils.getString(AppConfig.ETH, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.XRP)) {
            String rate = SPUtils.getString(AppConfig.XRP, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.HT)) {
            String rate = SPUtils.getString(AppConfig.HT, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.TRX)) {
            String rate = SPUtils.getString(AppConfig.TRX, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        } else if (src.equals(AppConfig.LINK)) {
            String rate = SPUtils.getString(AppConfig.LINK, null);
            if (rate != null) {
                double mul = TradeUtil.mul(dataBean.getMoney(), Double.parseDouble(rate));
                isEmpty = true;
                onNetResult.onNetResult(SUCCESS, mul);
            } else {
                isEmpty = false;
            }
        }


        if (isEmpty == false) {
            ArrayMap<String, String> map = new ArrayMap<>();
            map.put("src", src);
            map.put("des", des);
            getRequest("/api/home/currency/rate", map, (state, response) -> {
                if (state.equals(BUSY)) {
                    onNetResult.onNetResult(BUSY, null);
                } else if (state.equals(SUCCESS)) {

                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 401) {
                        onNetResult.onNetResult(FAILURE, null);
                    } else if (tipEntity.getCode() == 200) {
                        isEmpty = true;
                        RateEntity rateEntity = new Gson().fromJson(response.toString(), RateEntity.class);
                        Log.d("print", "rate: 第一次: 843: " + rateEntity);
                        if (moneyType.equals("1")) {
                            if (src.equals(AppConfig.USDT)) {
                                SPUtils.putString(AppConfig.USDT, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.BTC)) {
                                SPUtils.putString(AppConfig.BTC, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.ETH)) {
                                SPUtils.putString(AppConfig.ETH, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.XRP)) {
                                SPUtils.putString(AppConfig.XRP, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.HT)) {
                                SPUtils.putString(AppConfig.HT, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.TRX)) {
                                SPUtils.putString(AppConfig.TRX, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            } else if (src.equals(AppConfig.LINK)) {
                                SPUtils.putString(AppConfig.LINK, String.valueOf(rateEntity.getRate()));
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateEntity.getRate());
                                onNetResult.onNetResult(SUCCESS, mul);
                            }

                        } else {
                            double mul = TradeUtil.mul(dataBean.getGame(), rateEntity.getRate());
                            onNetResult.onNetResult(SUCCESS, mul);
                        }


                    }

                } else if (state.equals(FAILURE)) {
                    onNetResult.onNetResult(FAILURE, null);

                }
            });
        }
    }


    public void rateList(BalanceEntity.DataBean dataBean, String moneyType, String src, String des, OnNetResult onNetResult) {
        RateListEntity rateListEntity = SPUtils.getData(AppConfig.RATE_LIST, RateListEntity.class);
        if (rateListEntity != null) {
            List<RateListEntity.ListBean> list = rateListEntity.getList();
            Log.d("print", "rateList:缓存: " + list);
            for (RateListEntity.ListBean rateList : list) {
                if (src.equals(rateList.getName())) {
                    if (moneyType.equals("1")) {
                        double mul = TradeUtil.mul(dataBean.getMoney(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);

                    } else {
                        double mul = TradeUtil.mul(dataBean.getGame(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);
                    }

                }
            }

        } else {
            /*获得多币种汇率*/
            NetManger.getInstance().getRateList("USDT,BTC,ETH,XRP,HT,TRX,LINK", "USDT", (state, response) -> {
                if (state.equals(SUCCESS)) {
                    RateListEntity rateListEntity2 = (RateListEntity) response;
                    Log.d("print", "rateList:最新:  " + rateListEntity2);
                    SPUtils.putData(AppConfig.RATE_LIST, rateListEntity2);
                    for (RateListEntity.ListBean rateList : rateListEntity2.getList()) {
                        if (src.equals(rateList.getName())) {
                            if (moneyType.equals("1")) {
                                double mul = TradeUtil.mul(dataBean.getMoney(), rateList.getValue());
                                onNetResult.onNetResult(SUCCESS, mul);

                            } else {
                                double mul = TradeUtil.mul(dataBean.getGame(), rateList.getValue());
                                onNetResult.onNetResult(SUCCESS, mul);
                            }

                        }
                    }
                }
            });
        }
    }


    /*获得汇率*/
    public void getItemRate(String moneyType, String des, OnResult onResult) {

        NetManger.getInstance().ItemRate(moneyType, "USDT", des, (state, response) -> {
            if (state.equals(SUCCESS)) {
                onResult.setResult(response);
            }
        });
    }

    public void isLogin(OnResult onResult) {
        postRequest("/api/user/isLogin", null, (state, response) -> {
            if (state.equals(SUCCESS)) {
                Log.d("print", "isLogin: " + response.toString());
                IsLoginEntity isLoginEntity = new Gson().fromJson(response.toString(), IsLoginEntity.class);
                onResult.setResult(isLoginEntity.getContent().isIsLogin());

            }
        });
    }


    /*多币种汇率查询*/
    public void getRateList(String src, String des, OnNetResult onNetResult) {

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("src", src);
        map.put("des", des);
        getRequest("/api/home/currency/rates", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 401) {
                    onNetResult.onNetResult(FAILURE, null);
                } else if (tipEntity.getCode() == 200) {
                    RateListEntity rateListEntity = new Gson().fromJson(response.toString(), RateListEntity.class);
                    onNetResult.onNetResult(SUCCESS, rateListEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    public void ItemRate(String moneyType, String src, String des, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("src", src);
        map.put("des", des);
        getRequest("/api/home/currency/rate", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {

                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 401) {
                    onNetResult.onNetResult(FAILURE, null);
                } else if (tipEntity.getCode() == 200) {
                    RateEntity rateEntity = new Gson().fromJson(response.toString(), RateEntity.class);

                    if (moneyType.equals("1")) {
                        onNetResult.onNetResult(SUCCESS, rateEntity.getRate());
                    } else {
                        onNetResult.onNetResult(SUCCESS, rateEntity.getRate());
                    }


                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


}
