package com.pro.bityard.api;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            } else if (content.charAt(0) == '{'){
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
            }else {
                Log.e("异常", "json2Map: 字符串格式错误");
            }
        } catch (JSONException e) {
            Log.e("异常", "json2Map: ", e);
            result = null;
        }
        return result;
    }

    private static String content=null;

    private static List<String> list=new ArrayList<>();


    public static String requestPost(String urlString, String postParam){

        Map<String, Object> stringObjectMap = jsonToMap(postParam);
        Iterator<Map.Entry<String, Object>> iterator = stringObjectMap.entrySet().iterator();


        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            String value = (String) next.getValue();
            list.add(key+"="+value);
        }

        Log.d("print", "requestPost:127:  "+list);

        String s = list.toString().replaceAll(",", "&").replaceAll("\\[", "".replaceAll("\\]", "").replaceAll(" ",""));
        Log.d("print", "requestPost: "+(urlString+"?"+s).replaceAll(" ",""));

        OkGo.<String>post((urlString+"?"+s).replaceAll(" ","").substring(0,(urlString+"?"+s).replaceAll(" ","").length()-1))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.d("print", "onSuccess:71:  "+response.body().toString());
                    }
                });




        okHttpClient.followRedirects();
        okHttpClient.followSslRedirects();
       /* MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = new FormBody.Builder()
                .add("geetest_challenge", "434c23e4e7ff0f80ba933f7552d3bfbfd0")
                .add("geetest_seccode","867dc34fac7582522e145ae86d222ca3|jordan")
                .add("geetest_validate","867dc34fac7582522e145ae86d222ca3")
                .build();*/

        Request request = new Request
                .Builder()
               // .post(requestBody)
                .url((urlString+"?"+s).replaceAll(" ","").substring(0,(urlString+"?"+s).replaceAll(" ","").length()-1))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestGet(String urlString){
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
