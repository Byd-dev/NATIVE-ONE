package com.pro.bityard.api;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.pro.bityard.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.pro.bityard.api.NetManger.SUCCESS;

public class Gt3Util {

    public static Gt3Util instance;
    private Map<String, Object> stringObjectMap;

    public static Gt3Util getInstance() {
        if (instance == null) {
            instance = new Gt3Util();
        }

        return instance;
    }


    /**
     * api1，需替换成自己的服务器URL
     */
    private static final String URL_API1 = NetManger.BASE_URL + "/api/geetest/api1";
    /**
     * api2，需替换成自己的服务器URL
     */
    private static final String URL_API2 = NetManger.BASE_URL + "/api/geetest/api2";
    private String TAG = "register";


    private GT3GeetestUtils gt3GeetestUtils;
    private GT3ConfigBean gt3ConfigBean;


    public void init(Context context) {
        gt3GeetestUtils = new GT3GeetestUtils(context);
    }

    public void destroy() {
        gt3GeetestUtils.destory();
    }


    public void customVerity(OnGtUtilResult onGtUtilResult) {
        // 配置bean文件，也可在oncreate初始化
        gt3ConfigBean = new GT3ConfigBean();
        // 设置验证模式，1：bind，2：unbind
        gt3ConfigBean.setPattern(1);
        // 设置点击灰色区域是否消失，默认不消失
        gt3ConfigBean.setCanceledOnTouchOutside(false);
        // 设置语言，如果为null则使用系统默认语言
        gt3ConfigBean.setLang(null);
        // 设置加载webview超时时间，单位毫秒，默认10000，仅且webview加载静态文件超时，不包括之前的http请求
        gt3ConfigBean.setTimeout(10000);
        // 设置webview请求超时(用户点选或滑动完成，前端请求后端接口)，单位毫秒，默认10000
        gt3ConfigBean.setWebviewTimeout(10000);
        // 设置自定义view
//        gt3ConfigBean.setLoadImageView(new TestLoadingView(this));
        // 设置回调监听
        gt3ConfigBean.setListener(new GT3Listener() {

            /**
             * api1结果回调
             * @param result
             */
            @Override
            public void onApi1Result(String result) {
                Log.e(TAG, "onApi1Result-->" + result);

                if (null != result) {
                    Map<String, Object> stringObjectMap = Util.jsonToMap(result);
                    String code = (String) stringObjectMap.get("code");
                    if (code.equals("200")) {
                        String geetestToken = (String) stringObjectMap.get("geetestToken");
                        StringBuilder stringBuilder = new StringBuilder();
                        Map<String, Object> data = (Map<String, Object>) stringObjectMap.get("data");
                        String gt = (String) data.get("gt");
                        StringBuilder append = stringBuilder.append(geetestToken).append(",").append(gt);
                        onGtUtilResult.onApi1Result(append.toString());
                    }

                }

            }

            /**
             * 验证码加载完成
             * @param duration 加载时间和版本等信息，为json格式
             */
            @Override
            public void onDialogReady(String duration) {
                // onGtUtilResult.onGtResult(duration);

                Log.e(TAG, "onDialogReady-->" + duration);
            }

            /**
             * 验证结果
             * @param result
             */
            @Override
            public void onDialogResult(String result) {
                // onGtUtilResult.onGtResult(result);

                Log.e(TAG, "onDialogResult-->" + result);
                requestAPI2(result);

              //  new RequestAPI2().execute(result);


            }

            /**
             * api2回调
             * @param result
             */
            @Override
            public void onApi2Result(String result) {
                // onGtUtilResult.onGtResult(result);

                Log.e(TAG, "onApi2Result-->" + result);
            }

            /**
             * 统计信息，参考接入文档
             * @param result
             */
            @Override
            public void onStatistics(String result) {
                // onGtUtilResult.onGtResult(result);


                Log.e(TAG, "onStatistics-->" + result);
            }

            /**
             * 验证码被关闭
             * @param num 1 点击验证码的关闭按钮来关闭验证码, 2 点击屏幕关闭验证码, 3 点击返回键关闭验证码
             */
            @Override
            public void onClosed(int num) {
                // onGtUtilResult.onGtResult(String.valueOf(num));

                Log.e(TAG, "onClosed-->" + num);
            }

            /**
             * 验证成功回调
             * @param result
             */
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess-->" + result);
                onGtUtilResult.onSuccessResult(result);

            }

            /**
             * 验证失败回调
             * @param errorBean 版本号，错误码，错误描述等信息
             */
            @Override
            public void onFailed(GT3ErrorBean errorBean) {
                Log.e(TAG, "onFailed-->" + errorBean.toString());
                onGtUtilResult.onFailedResult(errorBean);

            }

            /**
             * api1回调
             */
            @Override
            public void onButtonClick() {

                requestAPI1();

            }
        });
        gt3GeetestUtils.init(gt3ConfigBean);
        // 开启验证
        gt3GeetestUtils.startCustomFlow();
    }
    /**
     * 请求api1
     */
    void requestAPI1() {
        NetManger.getInstance().Gt3GetRequest(URL_API1, (state, response) -> {
            if (state.equals(SUCCESS)) {
                stringObjectMap = jsonToMap(response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gt3ConfigBean.setApi1Json(jsonObject);
                // 继续api验证
                gt3GeetestUtils.getGeetest();
            }
        });
    }
    /**
     * 请求api2
     */
    void requestAPI2(String postParam){
        NetManger.getInstance().Gt3PostRequest(URL_API2,(String) stringObjectMap.get("geetestToken"), postParam, (state, response) -> {
            if (state.equals(SUCCESS)){
                if (!TextUtils.isEmpty(response.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String status = jsonObject.getString("status");
                        if ("success".equals(status)) {
                            gt3GeetestUtils.showSuccessDialog();
                        } else {
                            gt3GeetestUtils.showFailedDialog();
                        }
                    } catch (Exception e) {
                        gt3GeetestUtils.showFailedDialog();
                        e.printStackTrace();
                    }
                } else {
                    gt3GeetestUtils.showFailedDialog();
                }
            }
        });
    }





    public static Map<String, Object> jsonToMap(String content) {
        if (content != null && content.length() != 0) {
            content = content.trim();
            Map<String, Object> result = new HashMap<>();
            try {
                if (content.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(content);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object value = jsonArray.get(i);
                        if (value instanceof JSONArray || value instanceof JSONObject) {
                            result.put(i + "", jsonToMap(value.toString().trim()));
                        } else {
                            result.put(i + "", jsonArray.getString(i));
                        }
                    }
                } else if (content.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(content);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Object value = jsonObject.get(key);
                        if (value instanceof JSONArray || value instanceof JSONObject) {
                            result.put(key, jsonToMap(value.toString().trim()));
                        } else {
                            result.put(key, value.toString().trim());
                        }
                    }
                } else {
                    Log.e("异常", "json2Map: 字符串格式错误");
                }
            } catch (JSONException e) {
                Log.e("异常", "json2Map: ", e);
                result = null;
            }
            return result;
        } else {
            return null;
        }

    }
}
