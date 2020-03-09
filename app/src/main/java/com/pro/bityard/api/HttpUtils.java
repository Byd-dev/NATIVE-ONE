package com.pro.bityard.api;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

/**
 * Created by chensongsong on 2018/7/12.
 */

public class HttpUtils {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static String requsetUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String resultData = null;
                InputStream inputStream = urlConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                inputStream.close();
                resultData = new String(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                return resultData;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> jsonToMap(String content) {
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
    }

    private static String content = null;

    private static List<String> listKey;
    private static List<String> listValue;


    public static String requestPost(String urlString, String postParam, String geetestToken) {

        Map<String, Object> stringObjectMap = jsonToMap(postParam);
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


       /* String encode1 = URLEncoder.encode(listKey.get(2));
        String value1 = URLEncoder.encode(listValue.get(2));
        String encode2 = URLEncoder.encode(listKey.get(0));
        String value2 = URLEncoder.encode(listValue.get(0));
        String encode3 = URLEncoder.encode(listKey.get(1));
        String value3 = URLEncoder.encode(listValue.get(1));

        String url = urlString + "?" + "geetestToken" + "=" + geetestToken
                + "&" + "clientType=native"
                + "&" + encode1 + "=" + value1
                + "&" + encode2 + "=" + value2
                + "&" + encode3 + "=" + value3
                + "&_" + "=" + new Date().getTime();

        Log.d("print", "requestPost:135:  " + url);*/

        okHttpClient.followRedirects();
        okHttpClient.followSslRedirects();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = new FormBody.Builder()
                .add("geetestToken", geetestToken)
                .add("clientType", "native")
                .add(listKey.get(0), listValue.get(0))
                .add(listKey.get(1), listValue.get(1))
                .add(listKey.get(2), listValue.get(2))
                .add("_", String.valueOf(new Date().getTime()))
                .build();

        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(urlString)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String requestGet(String urlString) {
        okHttpClient.followRedirects();
        okHttpClient.followSslRedirects();
        Request request = new Request.Builder().url(urlString).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
