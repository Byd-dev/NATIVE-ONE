package com.pro.bityard.api;


import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public class NetManger {

    public static NetManger instance;

    public static String BUSY = "busy";
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";

    private String BASE_URL = "https://www.bityard.com/";


    public static NetManger getInstance() {
        if (instance == null) {
            instance = new NetManger();
        }

        return instance;
    }
    //获取国家code
    public void countryCode(OnNetResult onNetResult) {

        OkGo.<String>get(BASE_URL+"api/home/country/list")
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
    public void login(String vHash,String contryCode,String username,String password,String geetestToken,OnNetResult onNetResult) {

        OkGo.<String>post(BASE_URL+"api/sso/user_login_check")
                .params("vHash",vHash)
                .params("contryCode",contryCode)
                .params("username",username)
                .params("password",password)
                .params("geetestToken",geetestToken)
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
    //获取国家code
    public void register(OnNetResult onNetResult) {

        OkGo.<String>get(BASE_URL+"api/home/country/list")
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
}
