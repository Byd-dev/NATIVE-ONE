package com.pro.bityard.api;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.geetest.sdk.GT3ErrorBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.AddAddressItemEntity;
import com.pro.bityard.entity.AddScoreEntity;
import com.pro.bityard.entity.AnnouncementEntity;
import com.pro.bityard.entity.BalanceEntity;
import com.pro.bityard.entity.CopyMangerEntity;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.DepositWithdrawEntity;
import com.pro.bityard.entity.ExchangeRecordEntity;
import com.pro.bityard.entity.FollowEntity;
import com.pro.bityard.entity.FollowHistoryEntity;
import com.pro.bityard.entity.FollowLogEntity;
import com.pro.bityard.entity.FollowerDetailEntity;
import com.pro.bityard.entity.FollowerIncomeEntity;
import com.pro.bityard.entity.FollowerIncomeListEntity;
import com.pro.bityard.entity.FollowersListEntity;
import com.pro.bityard.entity.FundItemEntity;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.InviteEntity;
import com.pro.bityard.entity.InviteListEntity;
import com.pro.bityard.entity.IsLoginEntity;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.entity.MarginHistoryEntity;
import com.pro.bityard.entity.OrderEntity;
import com.pro.bityard.entity.PositionEntity;
import com.pro.bityard.entity.RateEntity;
import com.pro.bityard.entity.RateListEntity;
import com.pro.bityard.entity.SpotHistoryEntity;
import com.pro.bityard.entity.SpotPositionEntity;
import com.pro.bityard.entity.SpotTradeHistoryEntity;
import com.pro.bityard.entity.StatEntity;
import com.pro.bityard.entity.StyleEntity;
import com.pro.bityard.entity.TipCloseEntity;
import com.pro.bityard.entity.TipEntity;
import com.pro.bityard.entity.TipSPSLMarginEntity;
import com.pro.bityard.entity.TradeHistoryEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.entity.UnionRateEntity;
import com.pro.bityard.entity.UpdateEntity;
import com.pro.bityard.entity.UserAssetEntity;
import com.pro.bityard.entity.UserDetailEntity;
import com.pro.bityard.entity.WithdrawalAdressEntity;
import com.pro.bityard.manger.TradeListManger;
import com.pro.bityard.utils.PopUtil;
import com.pro.bityard.utils.TradeUtil;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.AES;
import com.pro.switchlibrary.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
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


    public static String BASE_URL = "http://byd-test.ttms.io";   //测试

    public static String H5_BASE_URL = "https://test7777.bityard.com";   //测试


    public static String QUOTE_SOCKET = "wss://quote.76bao.hk/wsquote";

    public static String QUOTE_HISTORY = "https://app.bityard.com";

    public static String SERVICE_URL = "https://v2.live800.com/live800/chatClient/chatbox.jsp?companyID=1360004&configID=128342&jid=1252134905&s=1&lan=%s&s=1&info=userId=%sname=%s";

    // public static String BASE_URL = "https://www.bityard.com";    //正式
    public static String HOST_H5 = "https://m233.bityard.com";

    public static String UPDATE_URL = "https://static.bityard.com/APP-apk/test.txt";

    public static NetManger getInstance() {

        if (instance == null) {
            instance = new NetManger();
        }

        return instance;
    }


    /*H5地址*/
    public String h5Url(String token, String id, String url) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        if (token != null) {
            map.put("token", token);
        }
        if (id != null) {
            map.put("id", id);
        }
        map.put("lang", language);

        return getH5URL(url, map);

    }

    //get 请求
    public void getRequest(String url, ArrayMap map, OnNetResult onNetResult) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }


        OkGo.<String>get(getURL(url, map))
                .headers("Accept-Language", language)
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
                        onNetResult.onNetResult(FAILURE, null);
                    }
                });

    }

    //get 请求
    public void getBitmapRequest(String url, ArrayMap map, OnNetResult onNetResult) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }

        OkGo.<Bitmap>get(getURL(url, map))
                .headers("Accept-Language", language)
                .execute(new BitmapCallback() {

                    @Override
                    public void onStart(Request<Bitmap, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, null);

                    }

                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        onNetResult.onNetResult(SUCCESS, response.body());
                    }

                    @Override
                    public void onError(Response<Bitmap> response) {
                        super.onError(response);
                        onNetResult.onNetResult(FAILURE, null);

                    }
                });

    }

    //post 请求
    public void postRequest(String url, ArrayMap map, OnNetResult onNetResult) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;

        }

        OkGo.<String>post(getURL(url, map))
                .headers("Accept-Language", language)
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
    public void postFileRequest(String url, String param, File file, OnNetResult onNetResult) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;

        }

        OkGo.<String>post(getURL(url, null))
                .params(param, file)
                .headers("Accept-Language", language)
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
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }
        OkGo.<String>get(getHostURL(host, url, map))
                .headers("Accept-Language", language)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, null);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.body().startsWith("err")) {
                            onNetResult.onNetResult(SUCCESS, response.body());
                        } else {
                            onNetResult.onNetResult(FAILURE, null);
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

            return url_result;
        }
    }

    //URL拼接参数
    public String getH5URL(String url, ArrayMap map) {

        String substring_url = null;
        if (map == null) {
            return H5_BASE_URL + url;
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
            String url_result = H5_BASE_URL + url + "?" + substring_url;
            Log.d("print", "getH5URL:508:  " + url_result);
            return url_result;
        }
    }

    public void getURL2(String url, ArrayMap map, OnNetResult onNetResult) {

        String substring_url = null;
        if (map == null) {
            onNetResult.onNetResult(SUCCESS, (BASE_URL + url));
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

            try {
                onNetResult.onNetResult(SUCCESS, Uri.encode(url_result));

            } catch (Exception e) {
                e.printStackTrace();
            }
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
    /*public void getHostCodeTradeList(OnNetThreeResult onNetThreeResult) {

        NetManger.getInstance().getInit((state, response) -> {
            if (state.equals(BUSY)) {
                onNetThreeResult.setResult(BUSY, null, null, null);
            } else if (state.equals(SUCCESS)) {
                InitEntity initEntity = new Gson().fromJson(response.toString(), InitEntity.class);
                if (initEntity.getGroup() != null) {
                    List<InitEntity.GroupBean> group = initEntity.getGroup();

                    ArrayMap<String, String> stringStringArrayMap = Util.groupData(group);
                    String allList = Util.groupList(stringStringArrayMap);
                    getTradeList(allList, (state1, response1) -> {
                        if (state1.equals(BUSY)) {

                        } else if (state1.equals(SUCCESS)) {

                            List<TradeListEntity> tradeListEntityList = (List<TradeListEntity>) response1;
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < tradeListEntityList.size(); i++) {
                                stringBuilder.append(tradeListEntityList.get(i).getContractCode() + ",");
                            }
                            onNetThreeResult.setResult(SUCCESS, response1, stringBuilder.toString(), tradeListEntityList);
                            SPUtils.putString(AppConfig.SUPPORT_CURRENCY, initEntity.getBrand().getSupportCurrency());
                            SPUtils.putString(AppConfig.PRIZE_TRADE, initEntity.getBrand().getPrizeTrade());
                        } else if (state1.equals(FAILURE)) {
                        }
                    });//获取合约号



                    *//*for (InitEntity.GroupBean data : group) {
                        if (data.getName().equals("数字货币")) {
                            String list = data.getList();
                            getTradeList(list, (state1, response1) -> {
                                if (state1.equals(BUSY)) {

                                } else if (state1.equals(SUCCESS)) {

                                    List<TradeListEntity> tradeListEntityList = (List<TradeListEntity>) response1;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 0; i < tradeListEntityList.size(); i++) {
                                        stringBuilder.append(tradeListEntityList.get(i).getContractCode() + ",");
                                    }
                                    onNetThreeResult.setResult(SUCCESS, response1, stringBuilder.toString(), tradeListEntityList);
                                    SPUtils.putString(AppConfig.SUPPORT_CURRENCY, initEntity.getBrand().getSupportCurrency());
                                    SPUtils.putString(AppConfig.PRIZE_TRADE, initEntity.getBrand().getPrizeTrade());
                                } else if (state1.equals(FAILURE)) {
                                }
                            });//获取合约号
                        }
                    }*//*
                }

            } else if (state.equals(FAILURE)) {
                onNetThreeResult.setResult(FAILURE, null, null, null);
            }

        });


    }*/

    /*获取codelist*/
    public void codeList(OnNetResult onNetResult) {

        NetManger.getInstance().getInit((state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                InitEntity initEntity = (InitEntity) response;
                if (initEntity.getGroup() != null) {
                    List<InitEntity.GroupBean> group = initEntity.getGroup();
                    ArrayMap<String, String> stringStringArrayMap = Util.groupData(group);
                    String allList = Util.groupList(stringStringArrayMap);
                    String allList2 = Util.initContractList(initEntity.getData());
                    onNetResult.onNetResult(SUCCESS, allList2);
                    SPUtils.putData(AppConfig.KEY_COMMODITY, initEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });

    }

    /*获取初始化数据*/
    public void getInit(OnNetResult onNetResult) {
        /*获取行情的host*/
        NetManger.getInstance().getRequest("/api/trade/commodity/initial", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                String s = response.toString().replaceAll(" ", "");


                if (s.startsWith("error")) {
                    onNetResult.onNetResult(FAILURE, response);
                } else {
                    TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                    if (tipEntity.getCode() == 200) {
                        InitEntity initEntity = new Gson().fromJson(response.toString(), InitEntity.class);
                        //初始化需要存储极验的判断
                        SPUtils.putBoolean(AppConfig.KEY_VERIFICATION, initEntity.getBrand().isGeetest());
                        if (initEntity.getGroup() != null) {
                            onNetResult.onNetResult(SUCCESS, initEntity);
                        }
                    } else {
                        onNetResult.onNetResult(FAILURE, null);

                    }
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    private List<TradeListEntity> tradeListEntityList;


    /*获取合约号*/
    public void getTradeList(String codeList, OnNetResult onNetResult) {
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
                getHostRequest(split[count], url, map, (state, response) -> {
                    if (state.equals(BUSY)) {

                    } else if (state.equals(SUCCESS)) {
                        onNetResult.onNetResult(SUCCESS, response.toString());
                    } else if (state.equals(FAILURE)) {
                        if (length == 0) {

                        } else {
                            count++;
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


    /*获取行情*/
    public void getCustomizeQuote(String quoteDomain, String url, String codeList, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("callback", "?");
        map.put("code", codeList);
        map.put("customize", "code,isUp,price,prev,buyPrice,sellPrice");

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
                nowBefore.add(Calendar.MINUTE, -1200);
                break;
            case "3":
                nowBefore.add(Calendar.MINUTE, -1200 * 3);
                break;
            case "5":
                nowBefore.add(Calendar.MINUTE, -1200 * 5);
                break;
            case "15":
                nowBefore.add(Calendar.MINUTE, -1200 * 15);
                break;
            case "60":
                nowBefore.add(Calendar.MINUTE, -1200 * 60);
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
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                if (response.toString().replaceAll(" ", "").startsWith("<")) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    onNetResult.onNetResult(SUCCESS, response.toString());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }

    public void initQuote() {

        NetManger.getInstance().getInit((state, response) -> {
            if (state.equals(SUCCESS)) {
                InitEntity initEntity = (InitEntity) response;
                if (initEntity.getGroup() != null) {
                    SPUtils.putString(AppConfig.SUPPORT_CURRENCY, initEntity.getBrand().getSupportCurrency());//可支持的货币
                    SPUtils.putString(AppConfig.PRIZE_TRADE, initEntity.getBrand().getPrizeTrade());//礼金抵扣比例
                    String quoteDomain = initEntity.getQuoteDomain();//获取域名
                    SPUtils.putString(AppConfig.QUOTE_HOST, quoteDomain);

                    List<InitEntity.GroupBean> group = initEntity.getGroup();
                    ArrayMap<String, String> stringStringArrayMap = Util.groupData(group);
                    String allList = Util.groupList(stringStringArrayMap);

                    SPUtils.putData(AppConfig.KEY_COMMODITY, initEntity);

                    String allList2 = Util.initContractList(initEntity.getData());


                    SPUtils.putString(AppConfig.CONTRACT_ID, allList2);
                    TradeListManger.getInstance().getTradeList(allList2, (state1, response1) -> {
                        if (state1.equals(BUSY)) {
                        } else if (state1.equals(SUCCESS)) {
                            List<TradeListEntity> tradeListEntityList = (List<TradeListEntity>) response1;
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < tradeListEntityList.size(); i++) {
                                stringBuilder.append(tradeListEntityList.get(i).getContractCode() + ",");
                            }
                            SPUtils.putString(AppConfig.QUOTE_CODE, stringBuilder.toString());
                            SPUtils.putString(AppConfig.QUOTE_DETAIL, Util.SPDealContract(tradeListEntityList));
                        } else if (state1.equals(FAILURE)) {
                        }
                    });//获取合约号
                }
            }
        });

    }

    /*持仓列表*/
    public void getHold(String tradeType, OnNetTwoResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("tradeType", tradeType);
        map.put("_", String.valueOf(new Date().getTime()));
        getRequest("/api/trade/scheme/holdings", map, (state, response) -> {

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

    public void currencyList(String type, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("type", type);//0是货币 1是法币
        getRequest("/api/home/currency/list", map, (state, response) -> {
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    FundItemEntity fundItemEntity = new Gson().fromJson(response.toString(), FundItemEntity.class);
                    onNetResult.onNetResult(SUCCESS, fundItemEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }
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

    /*撤销提币*/
    public void cancelWithdrawal(String id, String action, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("id", id);
        map.put("action", action);
        getRequest("/api/pay/withdraw.htm", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipCloseEntity tipCloseEntity = new Gson().fromJson(response.toString(), TipCloseEntity.class);
                onNetResult.onNetResult(SUCCESS, tipCloseEntity);

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*持仓历史*/
    public void getHistory(String tradeType, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("schemeSort", "2");
        map.put("tradeType", tradeType);
        map.put("_", String.valueOf(new Date().getTime()));
        getRequest("/api/trade/scheme/history", map, (state, response) -> {
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
        });
    }

    /*下单*/
    public void contractOpen(String tradeType, String leverType,
                             String commodity, String contract, String isBuy, String margin,
                             String lever, String price, String defer, String deferFee,
                             String stopProfit, String stopLoss, String serviceCharge,
                             String eagleDeduction, String volume, String moneyType,
                             String currency,
                             OnNetResult onNetResult) {
        String SEED = AppConfig.OPEN_SEED;

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
        postRequest("/api/trade/open.htm", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                OrderEntity orderEntity = new Gson().fromJson(response.toString(), OrderEntity.class);
                onNetResult.onNetResult(SUCCESS, orderEntity.getMessage());

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

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
        postRequest("/api/trade/margin.htm", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipSPSLMarginEntity tipSPSLMarginEntity = new Gson().fromJson(response.toString(), TipSPSLMarginEntity.class);
                if (tipSPSLMarginEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, tipSPSLMarginEntity.getMessage());
                } else {
                    onNetResult.onNetResult(FAILURE, tipSPSLMarginEntity.getMessage());

                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

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
            for (RateListEntity.ListBean rateList : list) {
                if (src.equals(rateList.getName())) {


                    if (moneyType.equals("1")) {
                        double mul = TradeUtil.mul(dataBean.getMoney(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);
                    } else if (moneyType.equals("0")) {
                        double mul = TradeUtil.mul(dataBean.getGame(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);
                    } else if (moneyType.equals("2")) {
                        double mul = TradeUtil.mul(dataBean.getPrize(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);
                    } else if (moneyType.equals("3")) {
                        double mul = TradeUtil.mul(dataBean.getLucky(), rateList.getValue());
                        onNetResult.onNetResult(SUCCESS, mul);
                    }
                }
            }

        } else {

            NetManger.getInstance().getInit((state, response) -> {
                if (state.equals(SUCCESS)) {
                    InitEntity initEntity = (InitEntity) response;
                    String supportCurrency = initEntity.getBrand().getSupportCurrency();
                    /*获得多币种汇率*/
                    NetManger.getInstance().getRateList(supportCurrency, "USDT", (state2, response2) -> {
                        if (state2.equals(SUCCESS)) {
                            RateListEntity rateListEntity2 = (RateListEntity) response2;
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
                Log.d("print", "isLogin:是否登录: " + response.toString());
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


    /*模拟加币*/
    public void addScore(OnNetResult onNetResult) {

        getRequest("/api/trade/addScore.htm", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                AddScoreEntity addScoreEntity = new Gson().fromJson(response.toString(), AddScoreEntity.class);
                onNetResult.onNetResult(SUCCESS, addScoreEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }

    /*修改登录密码*/
    public void passwordChange(String account, String oldPass, String newPass, String googleToken, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account);
        map.put("oldPassword", oldPass);
        map.put("newPassword", newPass);
        map.put("googleToken", googleToken);
        postRequest("/api/user/update-password", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }


    private String geetestToken = null;
    private String resultStr = null;

    /*获取邮箱验证码*/
    public void getEmailCode(Activity activity, View view, String account, String sendType, OnNetTwoResult onNetTwoResult) {

        Gt3Util.getInstance().customVerity(activity, view, new OnGtUtilResult() {
            @Override
            public void onApi1Result(String result) {
                String[] split = result.split(",");
                geetestToken = split[0];
                Log.d("print", "onApi1Result:gt:  " + split[1]);
                resultStr = result;
            }

            @Override
            public void onSuccessResult(String result) {
                ArrayMap<String, String> map = new ArrayMap<>();

                map.put("account", account);
                map.put("type", sendType);
                map.put("geetestToken", geetestToken);
                NetManger.getInstance().postRequest("/api/system/sendEmail", map, (state, response) -> {
                    if (state.equals(BUSY)) {
                        onNetTwoResult.setResult(BUSY, null, null);
                    } else if (state.equals(SUCCESS)) {
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        onNetTwoResult.setResult(SUCCESS, resultStr, tipEntity);
                    } else if (state.equals(FAILURE)) {
                        onNetTwoResult.setResult(FAILURE, null, null);
                    }
                });
            }

            @Override
            public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                onNetTwoResult.setResult(FAILURE, null, null);
            }

            @Override
            public void onImageSuccessResult(String result) {
                Log.d("print", "onImageSuccessResult: " + result);
                String[] split = result.split(",");
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("account", account);
                map.put("type", sendType);
                map.put("vHash", split[0]);
                map.put("vCode", split[1]);
                NetManger.getInstance().postRequest("/api/system/sendEmail", map, (state, response) -> {
                    if (state.equals(BUSY)) {
                        onNetTwoResult.setResult(BUSY, null, null);
                    } else if (state.equals(SUCCESS)) {
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        onNetTwoResult.setResult(SUCCESS, split[1], tipEntity);

                    } else if (state.equals(FAILURE)) {
                        onNetTwoResult.setResult(FAILURE, null, null);

                    }
                });
            }
        });


    }


    public void getMobileCountryCode(OnNetResult onNetResult) {
        //获取国家code
        NetManger.getInstance().getRequest("/api/home/country/list", null, (state, response) -> {
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    CountryCodeEntity countryCodeEntity = new Gson().fromJson(response.toString(), CountryCodeEntity.class);
                    List<CountryCodeEntity.DataBean> data = countryCodeEntity.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getNameCn().equals("CHINA") ||
                                data.get(i).getNameCn().equals("中国香港") ||
                                data.get(i).getNameCn().equals("美国") ||
                                data.get(i).getNameCn().equals("韩国") ||
                                data.get(i).getNameCn().equals("日本") ||
                                data.get(i).getNameCn().equals("泰国") ||
                                data.get(i).getNameCn().equals("越南") ||
                                data.get(i).getNameCn().equals("印度尼西亚") ||
                                data.get(i).getNameCn().equals("土耳其") ||
                                data.get(i).getNameCn().equals("俄罗斯")) {
                            data.get(i).setUsed(true);
                        }
                    }
                    SPUtils.putData(AppConfig.COUNTRY_CODE, countryCodeEntity);
                    onNetResult.onNetResult(SUCCESS, countryCodeEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }


            }
        });
    }


    /*获取手机验证码*/
    public void getMobileCode(Activity activity, View view, String account, String sendType, OnNetTwoResult onNetTwoResult) {

        Gt3Util.getInstance().customVerity(activity, view, new OnGtUtilResult() {
            @Override
            public void onApi1Result(String result) {
                String[] split = result.split(",");
                geetestToken = split[0];
            }

            @Override
            public void onSuccessResult(String result) {
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("account", account);
                map.put("type", sendType);
                map.put("geetestToken", geetestToken);
                NetManger.getInstance().postRequest("/api/system/sendSMS", map, (state, response) -> {
                    if (state.equals(BUSY)) {
                        onNetTwoResult.setResult(BUSY, null, null);
                    } else if (state.equals(SUCCESS)) {
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        onNetTwoResult.setResult(SUCCESS, geetestToken, tipEntity);

                    } else if (state.equals(FAILURE)) {
                        onNetTwoResult.setResult(FAILURE, null, null);

                    }
                });
            }

            @Override
            public void onFailedResult(GT3ErrorBean gt3ErrorBean) {
                onNetTwoResult.setResult(FAILURE, null, null);


            }

            @Override
            public void onImageSuccessResult(String result) {
                String[] split = result.split(",");
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("account", account);
                map.put("type", sendType);
                map.put("vHash", split[0]);
                map.put("vCode", split[1]);

                NetManger.getInstance().postRequest("/api/system/sendSMS", map, (state, response) -> {
                    if (state.equals(BUSY)) {
                        onNetTwoResult.setResult(BUSY, null, null);
                    } else if (state.equals(SUCCESS)) {
                        TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                        onNetTwoResult.setResult(SUCCESS, split[1], tipEntity);

                    } else if (state.equals(FAILURE)) {
                        onNetTwoResult.setResult(FAILURE, null, null);

                    }
                });
            }
        });


    }

    /*验证邮箱验证码*/
    public void checkEmailCode(String account, String sendType, String code_value, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account);
        map.put("type", sendType);
        map.put("code", code_value);
        postRequest("/api/system/checkEmail", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });

    }

    /*验证手机验证码*/
    public void checkMobileCode(String account, String sendType, String code_value, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account);
        map.put("type", sendType);
        map.put("code", code_value);
        NetManger.getInstance().postRequest("/api/system/checkSMS", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });

    }


    /*设置资金密码*/
    public void widthDrawPasswordSet(String loginPass, String newPass, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("password", loginPass);
        map.put("withdrawPw", newPass);
        postRequest("/api/user/init-withdrawPw", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);

                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }

    /*忘记资金密码*/
    public void widthDrawPasswordForget(String account, String withdrawPw, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account);
        map.put("withdrawPw", withdrawPw);
        postRequest("/api/user/update-withdrawPw", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }

    /*修改资金密码*/
    public void widthDrawPasswordChange(String account, String oldPass, String newPass, String googleToken, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", account);
        map.put("password", oldPass);
        map.put("withdrawPw", newPass);
        map.put("googleToken", googleToken);
        postRequest("/api/user/update-withdrawPw", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });


    }

    /*资金记录*/
    public void depositWithdraw(String type, String transfer, String currencyType, String srcCurrency, String currency, String createTimeGe,
                                String createTimeLe, String page, String rows, OnNetResult onNetResult) {


        ArrayMap<String, String> map = new ArrayMap<>();
        if (type != null) {
            map.put("type", type);
        }
        if (transfer != null) {
            map.put("transfer", transfer);
        }
        if (currencyType != null) {
            map.put("currencyType", currencyType);
        }
        if (srcCurrency != null) {
            map.put("srcCurrency", srcCurrency);
        }
        if (currency != null) {
            map.put("currency", currency);
        }
        if (createTimeGe != null) {
            map.put("createTimeGe", createTimeGe);
        }
        if (createTimeLe != null) {
            map.put("createTimeLe", createTimeLe);
        }
        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }

        getRequest("/api/mine/funds/deposit-withdraw", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "depositWithdraw:1563:  " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    DepositWithdrawEntity depositWithdrawEntity = new Gson().fromJson(response.toString(), DepositWithdrawEntity.class);

                    onNetResult.onNetResult(SUCCESS, depositWithdrawEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });


    }

    /*兑换记录*/
    public void exchangeRecord(String type, String transfer, String currencyType, String srcCurrency, String desCurrency, String createTimeGe,
                               String createTimeLe, String page, String rows, OnNetResult onNetResult) {


        ArrayMap<String, String> map = new ArrayMap<>();
        if (type != null) {
            map.put("type", type);
        }
        if (transfer != null) {
            map.put("transfer", transfer);
        }
        if (currencyType != null) {
            map.put("currencyType", currencyType);
        }
        if (srcCurrency != null) {
            map.put("srcCurrency", srcCurrency);
        }
        if (desCurrency != null) {
            map.put("desCurrency", desCurrency);
        }
        if (createTimeGe != null) {
            map.put("createTimeGe", createTimeGe);
        }
        if (createTimeLe != null) {
            map.put("createTimeLe", createTimeLe);
        }
        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }

        postRequest("/api/user/asset/exchangeHistory", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    Log.d("print", "getWithdrawal:273:  " + response);
                    ExchangeRecordEntity exchangeRecordEntity = new Gson().fromJson(response.toString(), ExchangeRecordEntity.class);
                    onNetResult.onNetResult(SUCCESS, exchangeRecordEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });


    }

    /*交易记录*/
    public void tradeHistory(String tradeType, String nowTime, String schemeSort, String commodity, String createTimeGe,
                             String createTimeLe, OnNetResult onNetResult) {


        ArrayMap<String, String> map = new ArrayMap<>();
        if (tradeType != null) {
            map.put("tradeType", tradeType);
        }
        if (nowTime != null) {
            map.put("_", nowTime);
        }
        if (schemeSort != null) {
            map.put("schemeSort", schemeSort);
        }

        if (commodity != null) {
            map.put("commodity", commodity);
        }
        if (createTimeGe != null) {
            map.put("createTimeGe", createTimeGe);
        }
        if (createTimeLe != null) {
            map.put("createTimeLe", createTimeLe);
        }


        getRequest("/api/trade/scheme/history", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    Log.d("print", "getWithdrawal:273:  " + response);
                    TradeHistoryEntity tradeHistoryEntity = new Gson().fromJson(response.toString(), TradeHistoryEntity.class);

                    onNetResult.onNetResult(SUCCESS, tradeHistoryEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }


            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });


    }

    /*邀请记录头部*/
    public void inviteTopHistory(String currency, OnNetResult onNetResult) {

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("currency", currency);

        getRequest("/api/mine/agent/sub-stat", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {

                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    Log.d("print", "getWithdrawal:273:  " + response);
                    InviteEntity inviteEntity = new Gson().fromJson(response.toString(), InviteEntity.class);

                    onNetResult.onNetResult(SUCCESS, inviteEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*邀请记录列表*/
    public void inviteListHistory(String page, String subName, OnNetResult onNetResult) {

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("page", page);
        map.put("rows", "10");
        if (subName != null) {
            map.put("subName", subName);
        }
        getRequest("/api/mine/agent/sub-list", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    InviteListEntity inviteListEntity = new Gson().fromJson(response.toString(), InviteListEntity.class);
                    onNetResult.onNetResult(SUCCESS, inviteListEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    public void unionRate(OnNetResult onNetResult) {
        getRequest("/api/mine/union.htm", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                UnionRateEntity unionRateEntity = new Gson().fromJson(response.toString(), UnionRateEntity.class);
                onNetResult.onNetResult(SUCCESS, unionRateEntity);


            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*获取个人详情*/
    public void userDetail(OnNetResult onNetResult) {
        getRequest("/api/user/detail", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    UserDetailEntity userDetailEntity = new Gson().fromJson(response.toString(), UserDetailEntity.class);
                    LoginEntity loginEntity = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
                    if (loginEntity != null) {
                        loginEntity.getUser().setAvatar(userDetailEntity.getUser().getAvatar());
                        SPUtils.putData(AppConfig.LOGIN, loginEntity);
                    }

                    SPUtils.putData(AppConfig.DETAIL, userDetailEntity);
                    onNetResult.onNetResult(SUCCESS, userDetailEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);

                }


            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*个人资产明细*/
    public void userAsset(String currency, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("currency", currency);
        getRequest("/api/user/asset/detail", map, (state, response) -> {
            if (state.equals(SUCCESS)) {
                UserAssetEntity userAssetEntity = new Gson().fromJson(response.toString(), UserAssetEntity.class);
                onNetResult.onNetResult(SUCCESS, userAssetEntity);

            }
        });
    }

    /*转账*/
    public void transfer(String currency, String money, String pass, String subName, String account, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("currency", currency);
        map.put("money", money);
        map.put("password", URLEncoder.encode(pass));
        map.put("subName", subName);
        if (account.contains("@")) {
            map.put("email", URLEncoder.encode(account));
        } else {
            map.put("mobile", account);
        }
        postRequest("/api/pay/withdraw/transfer", map, (state, response) -> {
            Log.d("transfer", "transfer: 1586: " + response);
            if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
            }
        });
    }

    public void postRequest2(String url, ArrayMap map, OnNetResult onNetResult) {
        getURL2(url, map, (state, response) -> {
            if (state.equals(SUCCESS)) {
                OkGo.<String>post(response.toString())
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

        });


    }


    /*提币地址管理*/
    public void withdrawalAddressList(OnNetResult onNetResult) {
        getRequest("/api/user/withdraw-address/list", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    WithdrawalAdressEntity entity = new Gson().fromJson(response.toString(), WithdrawalAdressEntity.class);
                    onNetResult.onNetResult(SUCCESS, entity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*提币地址删除*/
    public void deleteAddress(String addressId, OnNetResult onNetResult) {
        postRequest("/api/user/withdraw-address/delete/" + addressId, null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity entity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, entity);
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*提币地址添加*/
    public void addAddress(String currency, String chain, String address, String remark, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("currency", currency);
        map.put("chain", chain);
        map.put("address", address);
        map.put("remark", remark);

        postRequest("/api/user/withdraw-address/create", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    AddAddressItemEntity entity = new Gson().fromJson(response.toString(), AddAddressItemEntity.class);
                    onNetResult.onNetResult(SUCCESS, entity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    /*最新公告*/
    public void discover(OnNetResult onNetResult) {
        getRequest("/api/discover/index.htm", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    AnnouncementEntity entity = new Gson().fromJson(response.toString(), AnnouncementEntity.class);
                    onNetResult.onNetResult(SUCCESS, entity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    /*提币*/
    public void withdrawal(String money, String currency, String chain, String address, String email, String password, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("currency", currency);
        map.put("money", money);
        map.put("chain", chain);
        map.put("address", address);
        map.put("email", email);
        map.put("password", URLEncoder.encode(password));
        postRequest("/api/pay/withdraw/create", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("withdrawal", "withdrawal: " + response);
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*币币闪兑*/
    /*提币地址添加*/
    public void exchange(String srcCurrency, String srcMoney, String desCurrency, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("srcCurrency", srcCurrency);
        map.put("srcMoney", srcMoney);
        map.put("desCurrency", desCurrency);

        postRequest("/api/user/asset/exchangeApply", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }


    public void updateNickName(String username, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("username", username);
        postRequest("/api/user/update-username", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*历史详情的操作记录*/
    public void marginHistory(String bettingId, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("bettingId", bettingId);
        getRequest("/api/trade/margin/history", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    MarginHistoryEntity marginHistoryEntity = new Gson().fromJson(response.toString(), MarginHistoryEntity.class);
                    onNetResult.onNetResult(SUCCESS, marginHistoryEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*验证码获取的*/
    public void Gt3GetRequest(String url, OnNetResult onNetResult) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;

        }
        OkGo.<String>get(url)
                .headers("Accept-Language", language)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onNetResult.onNetResult(SUCCESS, response.body());
                    }
                });
    }

    private static List<String> listKey;
    private static List<String> listValue;

    public void Gt3PostRequest(String url, String geetestToken, String postParam, OnNetResult onNetResult) {
        Map<String, Object> stringObjectMap = Gt3Util.jsonToMap(postParam);
        Iterator<Map.Entry<String, Object>> iterator = stringObjectMap.entrySet().iterator();

        listKey = new ArrayList<>();
        listValue = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            String value = (String) next.getValue();
            listKey.add(key);
            listValue.add(value);
        }
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }
        OkGo.<String>post(url)
                .headers("Accept-Language", language)
                .params("geetestToken", geetestToken)
                .params("clientType", "native")
                .params(listKey.get(0), listValue.get(0))
                .params(listKey.get(1), listValue.get(1))
                .params(listKey.get(2), listValue.get(2))
                .params("_", String.valueOf(new Date().getTime()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        onNetResult.onNetResult(SUCCESS, response.body());
                    }
                });
    }

    /*登录*/
    public void login(String account, String pass, boolean verification, String vHash, String geetestToken, OnNetResult onNetResult) {

        ArrayMap<String, String> map = new ArrayMap<>();

        map.put("vHash", vHash);
        map.put("username", account);
        map.put("password", URLEncoder.encode(pass));
        if (verification) {
            map.put("geetestToken", geetestToken);
        } else {
            map.put("vCode", geetestToken);

        }
        map.put("terminal", "Android");

        NetManger.getInstance().postRequest("/api/sso/user_login_check", map, (state, response) -> {
            Log.d("print", "login: 2325: " + state + response);
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                LoginEntity loginEntity = new Gson().fromJson(response.toString(), LoginEntity.class);
                if (loginEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, loginEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, loginEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }

        });
    }


    /*注册*/
    public void register(boolean isEmail, String countryID, String country_code, String account, String pass, String sign, String kode, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (isEmail) {
            map.put("email", account);
        } else {
            map.put("contryId", countryID);
            map.put("phone", country_code + account);
        }
        map.put("password", URLEncoder.encode(pass));
        map.put("sign", sign);
        map.put("ru", kode);
        postRequest("/api/register/submit", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "onNetResult:176: " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    //版本更新
    public void updateCheck(Activity activity, View layout_view) {
        String language = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        switch (language) {
            case AppConfig.KEY_LANGUAGE:
            case AppConfig.EN_US:
                language = "en-US";
                break;
            case AppConfig.ZH_SIMPLE:
                language = "zh-CN";
                break;
            case AppConfig.ZH_TRADITIONAL:
                language = "zh-TW";
                break;
            case AppConfig.RU_RU:
                language = "ru-RU";
                break;
            case AppConfig.JA_JP:
                language = "ja-JP";
                break;
            case AppConfig.KO_KR:
                language = "ko-KR";
                break;
            case AppConfig.VI_VN:
                language = "vi-VN";
                break;
            case AppConfig.IN_ID:
                language = "in-ID";
                break;
            case AppConfig.PT_PT:
                language = "pt-PT";
                break;
        }
        OkGo.<String>get(UPDATE_URL)
                .headers("Accept-Language", language)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("print", "onSuccess:更新内容:  " + response.body());
                        UpdateEntity updateEntity = new Gson().fromJson(response.body(), UpdateEntity.class);
                        String versionCode = updateEntity.getUpdate().getVersionCode();
                        String versionMessage = updateEntity.getUpdate().getVersionMessage();
                        String versionUrl = updateEntity.getUpdate().getVersionUrl();
                        if (Util.updateJudge(activity, Integer.parseInt(versionCode))) {
                            PopUtil.getInstance().dialogUp(activity, layout_view, versionMessage, versionUrl);
                        }
                    }
                });


    }

    /*社区*/
    /*上传头像*/
    public void postImg(File file, OnNetResult onNetResult) {

        postFileRequest("/api/mine/profile/uploadAvatar", "image", file, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);

            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, null);

                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });

    }


    /*社区 列表*/
    /*持仓列表*/
    public void followList(String traderId, String type, String username, String currency, String tags, String defeatGe,
                           String defeatLe, String drawGe, String drawLe, String daysGe, String daysLe, String page, String rows, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (traderId != null) {
            map.put("traderId", traderId);
        }
        if (type != null) {
            map.put("type", type);
        }
        if (username != null) {
            map.put("username", username);
        }
        if (currency != null) {
            map.put("currency", currency);
        }
        if (tags != null) {
            map.put("tags", tags);
        }
        if (defeatGe != null) {
            map.put("defeatGe", defeatGe);
        }
        if (defeatLe != null) {
            map.put("defeatLe", defeatLe);
        }
        if (drawGe != null) {
            map.put("drawGe", drawGe);
        }
        if (drawLe != null) {
            map.put("drawLe", drawLe);
        }
        if (daysGe != null) {
            map.put("daysGe", daysGe);
        }
        if (daysLe != null) {
            map.put("daysLe", daysLe);
        }
        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }
        if (map.values().size() == 0) {
            map = null;
        }

        getRequest("/api/follow/trader/list", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    FollowEntity followEntity = new Gson().fromJson(response.toString(), FollowEntity.class);
                    onNetResult.onNetResult(SUCCESS, followEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    /*风格理念*/
    public void styleList(String type, OnNetResult onNetResult) {
        String url = String.format("/api/follow/follower/sysTags/%s", type);
        getRequest(url, null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    StyleEntity styleEntity = new Gson().fromJson(response.toString(), StyleEntity.class);
                    onNetResult.onNetResult(SUCCESS, styleEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*风格理念*/
    public void follow(String traderId, String currency, String followWay, String followVal, String followMax, String maxDay,
                       String maxHold, String slRatio, String active, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("traderId", traderId);
        map.put("currency", currency);
        if (followWay != null) {
            map.put("followWay", followWay);
        }
        if (followVal != null) {
            map.put("followVal", followVal);
        }
        if (followMax != null) {
            map.put("followMax", followMax);
        }
        if (maxDay != null) {
            map.put("maxDay", maxDay);
        }
        if (maxHold != null) {
            map.put("maxHold", maxHold);
        }
        if (slRatio != null) {
            map.put("slRatio", slRatio);
        }
        if (active != null) {
            map.put("active", active);
        }

        getRequest("/api/follow/follower/apply", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity);
                /*if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                }*/

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }


    /*跟单收益*/
    public void followerStat(OnNetResult onNetResult) {
        getRequest("/api/follow/follower/stat", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    StatEntity statEntity = new Gson().fromJson(response.toString(), StatEntity.class);
                    onNetResult.onNetResult(SUCCESS, statEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*带单收益*/
    public void followerIncome(OnNetResult onNetResult) {
        getRequest("/api/follow/trader/income-view", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    FollowerIncomeEntity statEntity = new Gson().fromJson(response.toString(), FollowerIncomeEntity.class);
                    onNetResult.onNetResult(SUCCESS, statEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*跟单列表*/
    public void followerTraders(String page, String rows, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();

        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }

        if (map.values().size() == 0) {
            map = null;
        }
        getRequest("/api/follow/follower/traders", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {

                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    CopyMangerEntity copyMangerEntity = new Gson().fromJson(response.toString(), CopyMangerEntity.class);
                    onNetResult.onNetResult(SUCCESS, copyMangerEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*带单列表*/
    public void followerIncomeList(String page, String rows, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();

        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }

        if (map.values().size() == 0) {
            map = null;
        }
        getRequest("/api/follow/trader/income-list", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {

                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    FollowerIncomeListEntity copyMangerEntity = new Gson().fromJson(response.toString(), FollowerIncomeListEntity.class);
                    onNetResult.onNetResult(SUCCESS, copyMangerEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*交易员详情*/
    public void followerDetail(String traderId, String currency, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("traderId", traderId);
        map.put("currency", currency);
        getRequest("/api/follow/trader/detail", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "followerList:2757:  " + response);
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    FollowerDetailEntity dataBean = new Gson().fromJson(response.toString(), FollowerDetailEntity.class);
                    onNetResult.onNetResult(SUCCESS, dataBean);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*取消跟随*/
    public void followCancel(String traderId, OnNetResult onNetResult) {
        String url = String.format("/api/follow/follower/cancel/%s", traderId);

        getRequest(url, null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*跟单开关*/
    public void followSwitch(String active, String id, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("active", active);
        map.put("id", id);
        getRequest("/api/follow/follower/active", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*带单开关*/
    public void followerSwitch(String active, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (active != null) {
            map.put("active", active);
        } else {
            map = null;
        }
        postRequest("/api/follow/trader/active", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() != 200) {
                    onNetResult.onNetResult(FAILURE, null);
                } else {
                    onNetResult.onNetResult(SUCCESS, tipEntity);
                }

            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);

            }
        });
    }

    /*跟单失败记录*/
    public void followLog(OnNetResult onNetResult) {
        getRequest("/api/follow/follower/log", null, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    FollowLogEntity followLogEntity = new Gson().fromJson(response.toString(), FollowLogEntity.class);
                    onNetResult.onNetResult(SUCCESS, followLogEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*历史记录*/
    public void followHistory(String traderId, String page, String rows, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("traderId", traderId);
        map.put("page", page);
        map.put("rows", rows);
        getRequest("/api/follow/trader/history", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    FollowHistoryEntity followHistoryEntity = new Gson().fromJson(response.toString(), FollowHistoryEntity.class);
                    onNetResult.onNetResult(SUCCESS, followHistoryEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*历史记录*/
    public void followerList(String traderId, String page, String rows, OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("traderId", traderId);
        map.put("page", page);
        map.put("rows", rows);
        getRequest("/api/follow/trader/followers", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    FollowersListEntity followersListEntity = new Gson().fromJson(response.toString(), FollowersListEntity.class);
                    onNetResult.onNetResult(SUCCESS, followersListEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*现货当前委托*/
    public void userSpotPosition(String commodity, String buy, String type,
                                String srcCurrency, String desCurrency,
                                 OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (commodity != null) {
            map.put("commodity", commodity);
        }
        if (buy != null) {
            map.put("buy", buy);
        }
        if (type != null) {
            map.put("type", type);
        }
        if (srcCurrency != null) {
            map.put("srcCurrency", srcCurrency);
        }
        if (desCurrency != null) {
            map.put("desCurrency", desCurrency);
        }
        getRequest("/api/order/position", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotPosition:现货当前持仓: " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    SpotPositionEntity spotPositionEntity = new Gson().fromJson(response.toString(), SpotPositionEntity.class);
                    onNetResult.onNetResult(SUCCESS, spotPositionEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*现货当前委托*/
    public void userSpotPosition(String id,
                                 OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (id != null) {
            map.put("_", id);
        }

        getRequest("/api/order/position", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotPosition:现货当前持仓: " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 500) {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                } else {
                    SpotPositionEntity spotPositionEntity = new Gson().fromJson(response.toString(), SpotPositionEntity.class);
                    onNetResult.onNetResult(SUCCESS, spotPositionEntity);
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*历史委托*/
    public void spotPositionHistory(String commodity, String buy, String type, String status, String srcCurrency, String desCurrency,
                                    String createTimeGe, String createTimeLe, String page, String rows,
                                    OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (commodity != null) {
            map.put("commodity", commodity);
        }
        if (buy != null) {
            map.put("buy", buy);
        }
        if (type != null) {
            map.put("type", type);
        }
        if (status != null) {
            map.put("status", status);
        }
        if (srcCurrency != null) {
            map.put("srcCurrency", srcCurrency);
        }
        if (desCurrency != null) {
            map.put("desCurrency", desCurrency);
        }
        if (createTimeGe != null) {
            map.put("createTimeGe", createTimeGe);
        }
        if (createTimeLe != null) {
            map.put("createTimeLe", createTimeLe);
        }
        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }
        getRequest("/api/order/history", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotPosition:现货历史: " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    SpotHistoryEntity spotHistoryEntity = new Gson().fromJson(response.toString(), SpotHistoryEntity.class);
                    onNetResult.onNetResult(SUCCESS, spotHistoryEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }


    /*成交历史*/
    public void spotPositionTradeHistory(String commodity, String buy, String type, String status, String srcCurrency, String desCurrency,
                                    String createTimeGe, String createTimeLe, String page, String rows,
                                    OnNetResult onNetResult) {
        ArrayMap<String, String> map = new ArrayMap<>();
        if (commodity != null) {
            map.put("commodity", commodity);
        }
        if (srcCurrency != null) {
            map.put("srcCurrency", srcCurrency);
        }
        if (desCurrency != null) {
            map.put("desCurrency", desCurrency);
        }
        if (buy != null) {
            map.put("buy", buy);
        }
        if (type != null) {
            map.put("type", type);
        }
        if (status != null) {
            map.put("status", status);
        }
        if (createTimeGe != null) {
            map.put("createTimeGe", createTimeGe);
        }
        if (createTimeLe != null) {
            map.put("createTimeLe", createTimeLe);
        }
        if (page != null) {
            map.put("page", page);
        }
        if (rows != null) {
            map.put("rows", rows);
        }
        getRequest("/api/order/detail-list", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotPosition:成交历史: " + response.toString());
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                if (tipEntity.getCode() == 200) {
                    SpotTradeHistoryEntity spotTradeHistoryEntity = new Gson().fromJson(response.toString(), SpotTradeHistoryEntity.class);
                    onNetResult.onNetResult(SUCCESS, spotTradeHistoryEntity);
                } else {
                    onNetResult.onNetResult(FAILURE, tipEntity.getMessage());
                }
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }

    /*现货买卖*/
    public void spotOpen(String commodity, String buy, String type, String srcCurrency, String desCurrency,
                         String price, String volume, String chargeEagle, OnNetResult onNetResult) {
        String SEED = AppConfig.OPEN_SEED;
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int i1 = random.nextInt(SEED.length());
            stringBuffer.append(SEED.charAt(i1));
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("identity", stringBuffer.toString());
        map.put("commodity", commodity);
        map.put("buy", buy);
        map.put("type", type);
        map.put("srcCurrency", srcCurrency);
        map.put("desCurrency", desCurrency);
        if (price != null) {
            map.put("price", price);
        }
        map.put("volume", volume);
        map.put("chargeEagle", chargeEagle);
        map.put("platform", "Android");

        postRequest("/api/order/open", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotOpen:现货下单: " + response);
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity.getMessage());
               /* if (tipEntity.getCode() == 500) {
                } else {
                    OrderEntity orderEntity = new Gson().fromJson(response.toString(), OrderEntity.class);
                    onNetResult.onNetResult(SUCCESS, orderEntity.getMessage());
                }*/
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }


    /*现货撤单*/
    public void spotClose(String orderId, OnNetResult onNetResult) {

        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("orderId", orderId);


        postRequest("/api/order/revoke", map, (state, response) -> {
            if (state.equals(BUSY)) {
                onNetResult.onNetResult(BUSY, null);
            } else if (state.equals(SUCCESS)) {
                Log.d("print", "spotOpen:现货撤单: " + response);
                TipEntity tipEntity = new Gson().fromJson(response.toString(), TipEntity.class);
                onNetResult.onNetResult(SUCCESS, tipEntity.getMessage());
               /* if (tipEntity.getCode() == 500) {
                } else {
                    OrderEntity orderEntity = new Gson().fromJson(response.toString(), OrderEntity.class);
                    onNetResult.onNetResult(SUCCESS, orderEntity.getMessage());
                }*/
            } else if (state.equals(FAILURE)) {
                onNetResult.onNetResult(FAILURE, null);
            }
        });
    }
}
