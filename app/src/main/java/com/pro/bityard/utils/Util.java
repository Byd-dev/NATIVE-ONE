


package com.pro.bityard.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pro.bityard.R;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.BuySellEntity;
import com.pro.bityard.entity.CountryCodeEntity;
import com.pro.bityard.entity.InitEntity;
import com.pro.bityard.entity.TagEntity;
import com.pro.bityard.entity.TradeListEntity;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Util {


    private static boolean eye = true;

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
                Log.e("??????", "json2Map: ?????????????????????");
            }
        } catch (JSONException e) {
            Log.e("??????", "json2Map: ", e);
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
            for (int i = 0; i < split.length; i++) {
                String a = split[i];
                quoteList.add(a);
            }
            return quoteList;
        } else {
            return null;
        }
    }

    public static List<String> quoteResultAdd(List<String> dataList) {
        InitEntity data = SPUtils.getData(AppConfig.KEY_COMMODITY, InitEntity.class);
        List<InitEntity.DataBean> dataDetail = data.getData();
        List<String> quoteList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            String itemQuote = dataList.get(i);
            String[] split = itemQuote.split(",");
            Log.d("print", "quoteResultAdd:163:  "+itemQuote+"   "+filter2(split[0]));
            quoteList.add(itemQuote + "," +filter2(split[0])
                    + "," + 0
                    + "," + 0
                    + "," + filter(split[0].replaceAll("CC",""))
                    + "," + data.getBrand().getDefaultCurrency());
            //??????????????????

           /* for (int j = 0; j < dataDetail.size(); j++) {

                if (filter(split[0]).equals(filter(dataDetail.get(j).getCode()))) {
                    quoteList.add(itemQuote + "," + dataDetail.get(j).getCode()
                            + "," + dataDetail.get(j).getType()
                            + "," + dataDetail.get(j).getZone()
                            + "," + dataDetail.get(j).getName()
                            + "," + data.getBrand().getDefaultCurrency());
                  //  Log.d("print", "quoteResultAdd:162:  "+filter(split[0])+"    "+filter(dataDetail.get(j).getCode())+"   "+itemQuote);
                }
            }*/
        }
        return quoteList;
    }

    public static String filter(String content) {
        StringBuilder stringBuilder;
        ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
        stringBuilder = new StringBuilder();
        for (int i = 0; i < allSatisfyStr.size(); i++) {
            stringBuilder.append(allSatisfyStr.get(i));
        }
        if (stringBuilder.toString().contains("_")) {
            return stringBuilder.toString().replaceAll("_", "");
        } else {
            return stringBuilder.toString();
        }
    }

    public static String filter2(String content) {

        return content.substring(0,content.length()-4);
    }

    public static String styleValue(Map<String, TagEntity> tag_select) {
        String selectValue = null;
        Iterator<Map.Entry<String, TagEntity>> iterator = tag_select.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, TagEntity> next = iterator.next();
            String code = next.getValue().getCode();
            StringBuilder append = stringBuilder.append(code).append(",");
            selectValue = append.toString().substring(0, append.toString().length() - 1);
        }
        return selectValue;
    }

    public static String filterNumber(String content) {
        StringBuilder stringBuilder;
        ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[\\u4e00-\\u9fa5_a-zA-Z_]{4,10}");
        stringBuilder = new StringBuilder();
        for (int i = 0; i < allSatisfyStr.size(); i++) {
            stringBuilder.append(allSatisfyStr.get(i));
        }
        return stringBuilder.toString();

    }


    public static String quoteNme(String content) {
        String name;
        StringBuilder stringBuilder;

        if (content == null) {
            return null;
        } else {
            ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
            stringBuilder = new StringBuilder();
            for (int i = 0; i < allSatisfyStr.size(); i++) {
                stringBuilder.append(allSatisfyStr.get(i));
            }
            if (stringBuilder.toString().contains("USDT")) {
                name = content.substring(0, content.length() - 8) + "/" + content.substring(content.length() - 8, content.length() - 4);
            } else {
                name = stringBuilder.toString() + "," + "null";
            }
            return name;
        }
    }

    public static String quoteList(String content) {
        String name;

        StringBuilder stringBuilder;
        if (content == null) {
            return null;
        } else {
            ArrayList<String> allSatisfyStr = getAllSatisfyStr(content, "[a-zA-Z]");
            stringBuilder = new StringBuilder();
            for (int i = 0; i < allSatisfyStr.size(); i++) {
                stringBuilder.append(allSatisfyStr.get(i));
            }


            if (stringBuilder.toString().contains("USDT")) {
                name = stringBuilder.substring(0, content.length() - 8) + "," + stringBuilder.substring(content.length() - 8, content.length() - 4);
            } else {
                name = stringBuilder.toString() + "," + "null";
            }
            return name;
        }
    }

    public static ArrayList<String> getAllSatisfyStr(String str, String regex) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        ArrayList<String> allSatisfyStr = new ArrayList<>();
        if (regex == null || regex.isEmpty()) {
            allSatisfyStr.add(str);
            return allSatisfyStr;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            allSatisfyStr.add(matcher.group());
        }
        return allSatisfyStr;
    }


    public static String getNumberFormat2(String value) {
        double v = Double.parseDouble(value);
        DecimalFormat mFormat = new DecimalFormat("#0.00");
        return mFormat.format(v);
    }


    public static Context selectLanguage(Context context, String language) {
        Context updateContext;
        //??????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateContext = createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            updateContext = context;
        }
        //???????????????????????????
        SPUtils.putString(AppConfig.KEY_LANGUAGE, language);
        return updateContext;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        //??????????????????
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        switch (language) {
            case AppConfig.EN_US:
                locale = Locale.ENGLISH;
                break;
            case AppConfig.ZH_SIMPLE:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case AppConfig.ZH_TRADITIONAL:
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case AppConfig.VI_VN:
                locale = new Locale("vi", "VN");
                break;
            case AppConfig.RU_RU:
                locale = new Locale("ru", "RU");
                break;
            case AppConfig.IN_ID:
                locale = new Locale("in", "ID");
                break;
            case AppConfig.JA_JP:
                locale = Locale.JAPAN;
                break;
            case AppConfig.KO_KR:
                locale = Locale.KOREA;
                break;
            case AppConfig.PT_PT:
                locale = new Locale("pt", "PT");
                break;
        }
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void applyLanguage(Context context, String language) {
        //??????????????????
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        switch (language) {
            case AppConfig.EN_US:
                locale = Locale.ENGLISH;
                break;
            case AppConfig.ZH_SIMPLE:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case AppConfig.ZH_TRADITIONAL:
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case AppConfig.VI_VN:
                locale = new Locale("vi", "VN");
                break;
            case AppConfig.RU_RU:
                locale = new Locale("ru", "RU");
                break;
            case AppConfig.IN_ID:
                locale = new Locale("in", "ID");
                break;
            case AppConfig.JA_JP:
                locale = Locale.JAPAN;
                break;
            case AppConfig.KO_KR:
                locale = Locale.KOREA;
                break;
            case AppConfig.PT_PT:
                locale = new Locale("pt", "PT");
                break;
        }
        Log.d("print", "applyLanguage:????????????: " + locale.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocale(locale);
        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }

    public static Context updateLanguage(Context context) {
        String curLanguage = SPUtils.getString(AppConfig.KEY_LANGUAGE, null);
        if (null == curLanguage || TextUtils.isEmpty(curLanguage)) {
            curLanguage = AppConfig.KEY_LANGUAGE;
        }
        return selectLanguage(context, curLanguage);
    }

    public static void switchLanguage(Context context, String value) {

        selectLanguage(context, value);
    }

    /*?????????????????????*/
    public static String dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime() / 1000;
        return String.valueOf(ts);
    }

    public static boolean compareDate(String nowDate, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(nowDate);
            Date compare = df.parse(compareDate);
            if (now.before(compare)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int str2Calendar(String str, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (type.equals("year")) {
                return calendar.get(Calendar.YEAR);
            } else if (type.equals("month")) {
                return calendar.get(Calendar.MONTH);
            } else if (type.equals("day")) {
                return calendar.get(Calendar.DAY_OF_MONTH);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String startFormatDate(String selectTime, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(selectTime);
            Date compare = df.parse(compareDate);
            Date newDate = new Date(System.currentTimeMillis());

            if (now.before(compare) && now.before(newDate)) {
                String format = df.format(now);
                return format;
            } else {
                return getNowTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String startDisplay(String createTimeGe) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(createTimeGe);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(now);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String endFormatDate(String selectTime, String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date now = df.parse(selectTime);
            Date compare = df.parse(compareDate);
            Date newDate = new Date(System.currentTimeMillis());

            if (now.after(compare)) {
                String format = df.format(now);
                return format;
            } else {
                return getNowTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void changeViewVisibilityGone(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    public static Long dateToStampLong(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }

    /*?????????????????????*/
    public static String stampToDate(long milSecond) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(milSecond);
    }


    public static String getHours(long second) {//????????????????????????
        long h = 00;
        if (second > 3600) {
            h = second / 3600;
        }
        return h + "";
    }

    public static String getMins(long second) {//?????????????????????
        long d = 00;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                }
            }
        } else {
            d = second / 60;
        }

        if (d < 10) {
            return "0" + d;
        } else {
            return d + "";
        }
    }

    public static String getSeconds(long second) {//?????????????????????
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            if (temp != 0) {
                if (temp > 60) {
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (s < 10) {
            return "0" + s;
        } else {
            return s + "";
        }
    }

    /*??????*/
    public static Date parseServerTime(String serverTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINESE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = sdf.parse(serverTime);
        } catch (Exception e) {

        }
        return date;
    }


    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getSign(Map<String, String> params, String security) {
        Map<String, String> sortedMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedMap.putAll(params);

        // ???????????????
        sortedMap.remove("sign");
        // ??????????????????????????????access_token??????
        sortedMap.remove("access_token");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // ?????????????????????
            if (TextUtils.isEmpty(val)) {
                continue;
            }
            sb.append(key).append("=").append(val).append("&");
        }
        // ??????key
        sb.append("key").append("=").append(security);

        // MD5???????????????
        String sign = md5(sb.toString());
        return sign.toLowerCase();
    }

    //??????
    public static void copy(Context context, String data) {
        // ?????????????????????
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // ?????????????????????????????????????????????????????????????????????????????????????????????,???????????????
        // newHtmlText???
        // newIntent???
        // newUri???
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // ??????????????????????????????????????????
        clipboard.setPrimaryClip(clipData);
    }

    //??????
    public static void paste(Context context, TextView textView) {
        // ?????????????????????
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // ?????????????????????????????????
        ClipData clipData = clipboard.getPrimaryClip();

        if (clipData != null && clipData.getItemCount() > 0) {
            // ??????????????????????????????????????????????????????
            CharSequence text = clipData.getItemAt(0).getText();

            textView.setText(text);
        }

    }

    public static void setEye(Context context, EditText edit_password, ImageView img_eye) {
        if (eye) {
            edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_eye_open_black));
            eye = false;
        } else {
            edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_eye_close));
            eye = true;
        }
    }

    public static void setOneUnClick(EditText edit_account, Button btn_submit) {
        edit_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void setTwoUnClick(EditText edit_account, EditText edit_pass, Button btn_submit) {

        edit_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value_pass = edit_pass.getText().toString();
                if (s.length() != 0 && value_pass.length() != 0) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static void isPassEffective(EditText edit_password, OnResult onResult) {

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().contains(" ")) {
                    String[] str = charSequence.toString().split(" ");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < str.length; i++) {
                        sb.append(str[i]);
                    }
                    edit_password.setText(sb.toString());
                    edit_password.setSelection(start);
                }

                if (charSequence.length() != 0) {
                    if (Util.isPass(edit_password.getText().toString()) && edit_password.getText().toString().length() > 5) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       /* //??????????????????????????????
        edit_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edit_password.getText().toString().length() != 0) {
                    if (Util.isPass(edit_password.getText().toString()) && edit_password.getText().toString().length() > 5) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");
                }
            } else {

            }
        });*/
    }

    public static void isEmailEffective(EditText edit_account, OnResult onResult) {
        setEditTextInhibitInputSpaChat(edit_account);
        edit_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() != 0) {
                    if (Util.isEmail(edit_account.getText().toString())) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


       /* setEditTextInhibitInputSpaChat(edit_account);
        //??????????????????????????????
        edit_account.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edit_account.getText().toString().length() != 0) {
                    if (Util.isEmail(edit_account.getText().toString())) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");
                }
            }
        });*/
    }

    public static void isCodeEffective(EditText edit_account, OnResult onResult) {
        setEditTextInhibitInputSpaChat(edit_account);

        //??????????????????????????????
        edit_account.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edit_account.getText().toString().length() != 0) {
                    if (Util.isCode(edit_account.getText().toString())) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");
                }
            }
        });
    }

    public static void isPhoneEffective(EditText edit_account, OnResult onResult) {
        setEditTextInhibitInputSpaChat(edit_account);

        edit_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() != 0) {
                    if (Util.isPhone(edit_account.getText().toString())) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //??????????????????????????????
       /* edit_account.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edit_account.getText().toString().length() != 0) {
                    if (Util.isPhone(edit_account.getText().toString())) {
                        onResult.setResult("1");
                    } else {
                        onResult.setResult("0");
                    }
                } else {
                    onResult.setResult("-1");
                }
            }
        });*/
    }

    public static boolean isPhone(String phoneStr) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(phoneStr);
        if (isNum.matches() && phoneStr.length() > 4) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCode(String phoneStr) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if (phoneStr.equals("")) {
            return false;
        } else {
            Matcher isNum = pattern.matcher(phoneStr);
            if (isNum.matches()) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static void setThreeUnClick(EditText edit_amount, EditText edit_pass, EditText edit_code, Button btn_submit) {
        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value_pass = edit_pass.getText().toString();
                String value_code = edit_code.getText().toString();
                if (s.length() != 0 && value_pass.length() != 0 && value_code.length() != 0) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void setFourUnClick(EditText edit_amount, EditText edit_pass, EditText edit_pass_sure, EditText edit_code, Button btn_submit) {
        edit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value_pass = edit_pass.getText().toString();
                String value_code = edit_code.getText().toString();
                String value_sure = edit_pass_sure.getText().toString();
                if (s.length() != 0 && value_pass.length() != 0 && value_code.length() != 0 && value_sure.length() != 0) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static void lightOff(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
    }

    public static void colorSwipe(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.maincolor));

    }

    public static void lightOn(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }

    public static void dismiss(Activity activity, PopupWindow popupWindow) {
        popupWindow.setOnDismissListener(() -> {
            lightOn(activity);
        });
    }

    public static void isShowing(Activity activity, PopupWindow popupWindow) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lightOn(activity);
        }
    }

    public static void twoDismiss(Activity activity, PopupWindow popupWindow, PopupWindow popupWindow1) {
        popupWindow.setOnDismissListener(() -> {
            lightOn(activity);
        });
        popupWindow1.setOnDismissListener(() -> {
            lightOn(activity);
        });
    }

    public static void twoIsShowing(Activity activity, PopupWindow popupWindow, PopupWindow popupWindow1) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lightOn(activity);
        }
        if (popupWindow1.isShowing()) {
            popupWindow1.dismiss();
            lightOn(activity);
        }

    }

    /**
     * ??????????????????????????????
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * ??????????????????????????????
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    /*????????????????????????????????????*/
    public static boolean isPass(String str) {
        String regex = "^.*(?=.{6,16})(?=.*\\d)(?=.*[a-z])[A-Za-z0-9!@#$%^&*?].*$";
        boolean isRight = str.matches(regex);
        return isRight;
    }

    //?????????????????????editText????????????
    public static void hideKeyboard(Activity activity, IBinder token, EditText editText) {
        editText.clearFocus();
        if (token != null) {
            //???????????????InputMethodManager???????????????????????????????????????
            //InputMethodManager?????????????????????????????????manager
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static boolean isSoftShowing(Activity context) {
        //?????????????????????????????????
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //??????View???????????????bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom - getSoftButtonsBarHeight(context) != 0;
    }

    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //???????????????????????????????????????????????????
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //?????????????????????????????????
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * ??????EditText????????????
     * ???????????????????????????InputFilter?????????????????????????????????????????????android:inputType="textPassword"??????????????????
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpaChat(EditText editText) {
        //????????????
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ")) return "";
            else return source;
        },});

    }

    public static void setEditTextPassSpaChat(EditText editText) {
        //????????????
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ")) return "";
            else return source;
        }, new InputFilter.LengthFilter(16)});

    }

    List<CountryCodeEntity.DataBean> searchData;

    /*?????????????????????*/
    public static boolean updateJudge(Context context, int versionCode) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionCode > packInfo.versionCode) {
            return true;
        } else {
            return false;
        }
    }


    public static String deal(String content) {
        String replace = content.replaceAll("\\[", "").replaceAll("]", "").replace(" ", "");
        return replace;
    }

    /*??????*/
    public static String SPDeal(Set<String> setList) {
        Gson gson = new Gson();
        String s = gson.toJson(setList);
        return s;
    }

    /*??????*/
    public static Set<String> SPDealResult(String data) {
        if (data == null) {
            return new HashSet<>();
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<Set<String>>() {
            }.getType();
            Set<String> list = gson.fromJson(data, listType);
            return list;
        }
    }


    /*??????*/
    public static String SPDealContract(List<TradeListEntity> tradeListEntities) {
        Gson gson = new Gson();
        String s = gson.toJson(tradeListEntities);
        return s;
    }

    public static String SPDealString(List<String> quote_list) {
        Gson gson = new Gson();
        String s = gson.toJson(quote_list);
        return s;
    }

    /*??????*/
    public static List<TradeListEntity> SPDealEntityResult(String data) {
        if (data == null) {
            return null;
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TradeListEntity>>() {
            }.getType();
            List<TradeListEntity> list = gson.fromJson(data, listType);
            return list;
        }
    }

    /*??????*/
    public static List<String> SPDealStringResult(String data) {
        if (data == null) {
            return null;
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            List<String> list = gson.fromJson(data, listType);
            return list;
        }
    }

    /*??????????????????*/
    public static void setOptional(Context context, Set<String> optionalList, String quote_code, ImageView img_star, OnResult onResult) {
        if (optionalList.size() != 0) {
            onResult.setResult(true);
            //??????????????????????????????
            Util.isOptional(quote_code, optionalList, response -> {
                boolean isOptional = (boolean) response;
                if (isOptional) {
                    img_star.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star));
                } else {
                    img_star.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star_normal));

                }
            });
        } else {
            onResult.setResult(false);
        }
    }


    public static void isOptional(String contCode, Set<String> list, OnResult onResult) {
        if (list.toString().contains(contCode)) {
            onResult.setResult(true);
        } else {
            onResult.setResult(false);
        }

    }


    public static ArrayMap<String, String> groupData(List<InitEntity.GroupBean> group) {
        ArrayMap<String, String> map = new ArrayMap<>();
        for (InitEntity.GroupBean data : group) {
            map.put(data.getName(), data.getList());
        }
        return map;
    }

    public static String initContractList(List<InitEntity.DataBean> group) {
        List<String> allList = new ArrayList<>();
        for (InitEntity.DataBean data : group) {
            String code = data.getCode();
            if (data.getType().equals(AppConfig.TYPE_FT) || data.getType().equals(AppConfig.TYPE_CH)) {
                allList.add(code);
            }
           /* //????????????
            if (!data.getZone().equals("")){
                if (data.getType().equals(AppConfig.TYPE_FT) || data.getType().equals(AppConfig.TYPE_CH)) {
                    allList.add(code);
                }
            }*/

        }
        Log.d("print", "initContractList:1009:  " + allList.size());
        return allList.toString().replaceAll(" ", "").replaceAll(",", ";").replaceAll("\\[", "").replaceAll("]", "");
    }


    public static String groupList(ArrayMap<String, String> stringStringArrayMap) {
        String list0 = stringStringArrayMap.get("????????????");
        String list1 = stringStringArrayMap.get("????????????");
        String list = stringStringArrayMap.get("????????????");
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder append = stringBuilder2.append(list0).append(";").append(list1).append(";").append(list);
        return append.toString();
    }


    public static void priceTypeHigh2Low(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT:
            case AppConfig.VIEW_POP_SPOT:
                type = AppConfig.SPOT_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_POS:

                type = AppConfig.SPOT_POS_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:

                type = AppConfig.CONTRACT_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_PRICE_HIGH2LOW;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_PRICE_HIGH2LOW;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }


    public static void priceTypeLow2High(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_POP_SPOT:
            case AppConfig.VIEW_SPOT:
                type = AppConfig.SPOT_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_POS:
                type = AppConfig.SPOT_POS_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:
                type = AppConfig.CONTRACT_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_PRICE_LOW2HIGH;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_PRICE_LOW2HIGH;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }


    public static void rateTypeHigh2Low(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT:
            case AppConfig.VIEW_POP_SPOT:
                type = AppConfig.SPOT_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_SPOT_POS:
                type = AppConfig.SPOT_POS_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:

                type = AppConfig.CONTRACT_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_RATE_HIGH2LOW;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_RATE_HIGH2LOW;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }


    public static void rateTypeLow2High(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT:
            case AppConfig.VIEW_POP_SPOT:
                type = AppConfig.SPOT_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_SPOT_POS:
                type = AppConfig.SPOT_POS_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:

                type = AppConfig.CONTRACT_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_RATE_LOW2HIGH;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_RATE_LOW2HIGH;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }

    public static void nameTypeA2Z(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_NAME_A2Z;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_NAME_A2Z;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_NAME_A2Z;
                break;
            case AppConfig.VIEW_SPOT:
            case AppConfig.VIEW_POP_SPOT:

                type = AppConfig.SPOT_NAME_A2Z;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_NAME_A2Z;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_NAME_A2Z;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_NAME_A2Z;
                break;
            case AppConfig.VIEW_SPOT_POS:
                type = AppConfig.SPOT_POS_NAME_A2Z;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_NAME_A2Z;

                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:

                type = AppConfig.CONTRACT_NAME_A2Z;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_NAME_A2Z;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_NAME_A2Z;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }


    public static void nameTypeZ2A(String zone_type, OnResult onResult) {
        String type;
        switch (zone_type) {
            case AppConfig.VIEW_OPTIONAL_SPOT:
                type = AppConfig.OPTIONAL_SPOT_NAME_Z2A;
                break;
            case AppConfig.VIEW_OPTIONAL_CONTRACT:
                type = AppConfig.OPTIONAL_CONTRACT_NAME_Z2A;
                break;
            case AppConfig.VIEW_OPTIONAL_DERIVATIVES:
                type = AppConfig.OPTIONAL_DERIVATIVES_NAME_Z2A;
                break;
            case AppConfig.VIEW_SPOT:
            case AppConfig.VIEW_POP_SPOT:

                type = AppConfig.SPOT_NAME_Z2A;
                break;
            case AppConfig.VIEW_SPOT_DEFI:
                type = AppConfig.SPOT_DEFI_NAME_Z2A;
                break;
            case AppConfig.VIEW_SPOT_GRAY:
                type = AppConfig.SPOT_GRAY_NAME_Z2A;
                break;
            case AppConfig.VIEW_SPOT_BSC:
                type = AppConfig.SPOT_BSC_NAME_Z2A;
                break;
            case AppConfig.VIEW_SPOT_POS:
                type = AppConfig.SPOT_POS_NAME_Z2A;
                break;
            case AppConfig.VIEW_CONTRACT_IN:
                type = AppConfig.CONTRACT_IN_NAME_Z2A;

                break;
            case AppConfig.VIEW_POP_CONTRACT:
            case AppConfig.VIEW_CONTRACT:

                type = AppConfig.CONTRACT_NAME_Z2A;
                break;
            case AppConfig.VIEW_DERIVATIVES:
                type = AppConfig.DERIVATIVES_NAME_Z2A;
                break;
            case AppConfig.VIEW_FOREIGN_EXCHANGE:
                type = AppConfig.FOREIGN_EXCHANGE_NAME_Z2A;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + zone_type);
        }
        onResult.setResult(type);
    }

    public static List<BuySellEntity> getBuyList(String quote) {
        List<BuySellEntity> buyEntityList = new ArrayList<>();
        String[] split = quote.split(",");
        buyEntityList.add(new BuySellEntity(split[0], split[1]));
        buyEntityList.add(new BuySellEntity(split[2], split[3]));
        buyEntityList.add(new BuySellEntity(split[4], split[5]));
        buyEntityList.add(new BuySellEntity(split[6], split[7]));
        buyEntityList.add(new BuySellEntity(split[8], split[9]));
        buyEntityList.add(new BuySellEntity(split[10], split[11]));
        buyEntityList.add(new BuySellEntity(split[12], split[13]));
        buyEntityList.add(new BuySellEntity(split[14], split[15]));
        buyEntityList.add(new BuySellEntity(split[16], split[17]));
        buyEntityList.add(new BuySellEntity(split[18], split[19]));
        return buyEntityList;
    }

    public static Double buyMax(String quote) {
        List<Double> list = new ArrayList<>();
        String[] split = quote.split(",");
        list.add(TradeUtil.mul(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
        list.add(TradeUtil.mul(Double.parseDouble(split[2]), Double.parseDouble(split[3])));
        list.add(TradeUtil.mul(Double.parseDouble(split[4]), Double.parseDouble(split[5])));
        list.add(TradeUtil.mul(Double.parseDouble(split[6]), Double.parseDouble(split[7])));
        list.add(TradeUtil.mul(Double.parseDouble(split[8]), Double.parseDouble(split[9])));
        list.add(TradeUtil.mul(Double.parseDouble(split[10]), Double.parseDouble(split[11])));
        list.add(TradeUtil.mul(Double.parseDouble(split[12]), Double.parseDouble(split[13])));
        list.add(TradeUtil.mul(Double.parseDouble(split[14]), Double.parseDouble(split[15])));
        list.add(TradeUtil.mul(Double.parseDouble(split[16]), Double.parseDouble(split[17])));
        list.add(TradeUtil.mul(Double.parseDouble(split[18]), Double.parseDouble(split[19])));
        Double max = Collections.max(list);
        return max;
    }

    public static Double sellMax(String quote) {
        List<Double> list = new ArrayList<>();
        String[] split = quote.split(",");
        list.add(TradeUtil.mul(Double.parseDouble(split[20]), Double.parseDouble(split[21])));
        list.add(TradeUtil.mul(Double.parseDouble(split[22]), Double.parseDouble(split[23])));
        list.add(TradeUtil.mul(Double.parseDouble(split[24]), Double.parseDouble(split[25])));
        list.add(TradeUtil.mul(Double.parseDouble(split[26]), Double.parseDouble(split[27])));
        list.add(TradeUtil.mul(Double.parseDouble(split[28]), Double.parseDouble(split[29])));
        list.add(TradeUtil.mul(Double.parseDouble(split[30]), Double.parseDouble(split[31])));
        list.add(TradeUtil.mul(Double.parseDouble(split[32]), Double.parseDouble(split[33])));
        list.add(TradeUtil.mul(Double.parseDouble(split[34]), Double.parseDouble(split[35])));
        list.add(TradeUtil.mul(Double.parseDouble(split[36]), Double.parseDouble(split[37])));
        list.add(TradeUtil.mul(Double.parseDouble(split[38]), Double.parseDouble(split[39])));
        Double max = Collections.max(list);
        return max;
    }

    public static List<BuySellEntity> getSellList(String quote) {
        List<BuySellEntity> sellEntityList = new ArrayList<>();
        String[] split = quote.split(",");
        sellEntityList.add(new BuySellEntity(split[20], split[21]));
        sellEntityList.add(new BuySellEntity(split[22], split[23]));
        sellEntityList.add(new BuySellEntity(split[24], split[25]));
        sellEntityList.add(new BuySellEntity(split[26], split[27]));
        sellEntityList.add(new BuySellEntity(split[28], split[29]));
        sellEntityList.add(new BuySellEntity(split[30], split[31]));
        sellEntityList.add(new BuySellEntity(split[32], split[33]));
        sellEntityList.add(new BuySellEntity(split[34], split[35]));
        sellEntityList.add(new BuySellEntity(split[36], split[37]));
        sellEntityList.add(new BuySellEntity(split[38], split[39]));
        return sellEntityList;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getNowTime() {
        Date newDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(newDate);
        return format;
    }

    public static String timestamp2Date(String str_num) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
            return date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
            return date;
        }
    }


    public static String stampToTime(long stamp) {
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(stamp * 1000);
        time = simpleDateFormat.format(date);
        return time;
    }

    public static String getBeforeNow7days() {
        Date newDate = new Date(System.currentTimeMillis() - 604800000L); // 7 * 24 * 60 * 60 * 1000
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(newDate);
        return format;
    }


    public static String getJson(String fileName, Context context) {
        //???json?????????????????????
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //??????assets???????????????
            AssetManager assetManager = context.getAssets();
            //????????????????????????????????????
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void setTheme(Activity activity) {
        boolean theme = SPUtils.getBoolean(AppConfig.KEY_THEME, true);
        if (theme) {
            StatusBarUtil.setStatusBarDarkTheme(activity, false);
        } else {
            StatusBarUtil.setStatusBarDarkTheme(activity, true);
        }
    }

    public static void setTheme(OnResult onResult){
        boolean theme = SPUtils.getBoolean(AppConfig.KEY_THEME, true);
        if (theme){
            onResult.setResult(true);
        }else {
            onResult.setResult(false);
        }
    }


}
