package com.pro.bityard.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Util {

    public static Map<String, Object> jsonToMap(String content) {
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

    public static String Random32() {
        String strRand = "";
        for (int i = 0; i < 32; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }

        return strRand;
    }

    public static String jsonReplace(String content) {
        String s = content.replaceAll("\\?", "").replaceAll("\\(", "").replaceAll("\\)", "");
        String substring = s.substring(0, s.length() - 1);


        return substring;
    }

    public static List<String> quoteResult(String content) {
        List<String> quoteList = new ArrayList<>();
        String[] split = content.split(";");
        if (split.length > 0) {
            for (String a : split) {
                quoteList.add(a);
            }
            return quoteList;
        } else {
            return null;
        }
    }

    public static String quoteNme(String content) {
        String name;
        if (content == null) {
            return null;
        } else {
            name = content.substring(0, content.length() - 8) + "/" + content.substring(content.length() - 8, content.length() - 4);
            return name;
        }
    }

    public static String quoteList(String content) {
        String name;
        if (content == null) {
            return null;
        } else {
            name = content.substring(0, content.length() - 8) + "," + content.substring(content.length() - 8, content.length() - 4);
            return name;
        }
    }


    public static String getNumberFormat2(String value) {
        double v = Double.parseDouble(value);
        DecimalFormat mFormat = new DecimalFormat("#0.00");
        return mFormat.format(v);
    }

}
