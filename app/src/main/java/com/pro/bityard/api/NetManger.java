package com.pro.bityard.api;


import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pro.bityard.utils.Util;
import com.pro.switchlibrary.PhotoUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NetManger {

    public static NetManger instance;

    public static String BUSY = "busy";
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";

    public static String BASE_URL = "http://test.bityard.com/";
    private String substring_url;


    public static NetManger getInstance() {
        if (instance == null) {
            instance = new NetManger();
        }

        return instance;
    }

    //获取国家code
    public void countryCode(OnNetResult onNetResult) {

        OkGo.<String>get(BASE_URL + "api/home/country/list")
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


    //登录
    public void login(ArrayMap map, OnNetResult onNetResult) {

        OkGo.<String>post(getURL("api/sso/user_login_check",map))
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

    //获取验证码
    public void getCode(ArrayMap map,OnNetResult onNetResult) {

        OkGo.<String>post(getURL("api/system/sendEmail",map) )
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

    //获取验证码
    public void register(ArrayMap map,OnNetResult onNetResult) {

        OkGo.<String>post(getURL("api/register/submit",map) )
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
    public String getURL(String url,ArrayMap map) {
        String substring_url = null;
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
