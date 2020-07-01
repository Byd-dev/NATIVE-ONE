package com.pro.bityard.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.pro.bityard.R;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.config.AppConfig;
import com.pro.switchlibrary.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //            name = content.substring(0, content.length() - 8) + "/" + content.substring(content.length() - 8, content.length() - 4);
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
        //设置语言类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateContext = createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            updateContext = context;
        }
        //保存设置语言的类型
        SPUtils.putString(AppConfig.KEY_LANGUAGE, language);
        return updateContext;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        //设置语言类型
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale;
        switch (language) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "zh_simple":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "zh_traditional":
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case "ja":
                locale = Locale.JAPANESE;
                break;
            case "ko":
                locale = Locale.KOREAN;
                break;
            default:
                locale = Locale.getDefault();
                break;
        }
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void applyLanguage(Context context, String language) {
        //设置语言类型
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale;
        switch (language) {
            case "en":
                locale = Locale.ENGLISH;
                break;
            case "zh_simple":
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "zh_traditional":
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case "vi":
                locale = new Locale("vi");
                break;
            case "ru":
                locale = new Locale("ru");
                break;
            case "in":
                locale = new Locale("in");
                break;
            case "ja":
                locale = Locale.JAPANESE;
                break;
            case "ko":
                locale = Locale.KOREAN;
                break;
            default:
                locale = Locale.getDefault();
                break;
        }
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

    /*日期转成时间戳*/
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

    /*时间戳转成日期*/
    public static String stampToDate(long milSecond) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        return sdf.format(milSecond);
    }


    public static String getHours(long second) {//计算秒有多少小时
        long h = 00;
        if (second > 3600) {
            h = second / 3600;
        }
        return h+"";
    }

    public static String getMins(long second) {//计算秒有多少分
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

        if (d<10){
            return "0"+d;
        }else {
            return d + "";
        }
    }
    public static String getSeconds(long second) {//计算秒有多少秒
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
        if (s<10){
            return "0"+s;
        }else {
            return s + "";
        }
    }

    /*日期*/
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

        // 不参与签名
        sortedMap.remove("sign");
        // 特殊：登录情况下会有access_token参数
        sortedMap.remove("access_token");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            // 空值不参与签名
            if (TextUtils.isEmpty(val)) {
                continue;
            }
            sb.append(key).append("=").append(val).append("&");
        }
        // 拼接key
        sb.append("key").append("=").append(security);

        // MD5加密转小写
        String sign = md5(sb.toString());
        return sign.toLowerCase();
    }

    //复制
    public static void copy(Context context, String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

    //粘贴
    public static void paste(Context context, TextView textView) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // 获取剪贴板的剪贴数据集
        ClipData clipData = clipboard.getPrimaryClip();

        if (clipData != null && clipData.getItemCount() > 0) {
            // 从数据集中获取（粘贴）第一条文本数据
            CharSequence text = clipData.getItemAt(0).getText();

            textView.setText(text);
        }

    }

    public static void setEye(Context context, EditText edit_password, ImageView img_eye) {
        if (eye) {
            edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            img_eye.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_eye_open));
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

       /* //检测错误提示是否显示
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
        //检测错误提示是否显示
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

        //检测错误提示是否显示
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


        //检测错误提示是否显示
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
        lp.alpha = 0.2f;
        activity.getWindow().setAttributes(lp);
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
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断字符串是否是整数
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

    /*数字和字母可以有特殊符号*/
    public static boolean isPass(String str) {
        String regex = "^.*(?=.{6,16})(?=.*\\d)(?=.*[a-z])[A-Za-z0-9!@#$%^&*?].*$";
        boolean isRight = str.matches(regex);
        return isRight;
    }

    //隐藏软键盘并让editText失去焦点
    public static void hideKeyboard(Activity activity, IBinder token, EditText editText) {
        editText.clearFocus();
        if (token != null) {
            //这里先获取InputMethodManager再调用他的方法来关闭软键盘
            //InputMethodManager就是一个管理窗口输入的manager
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom - getSoftButtonsBarHeight(context) != 0;
    }

    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 禁止EditText输入空格
     *今天遇到个情况这个InputFilter不好使了，找了半天原因是冲突在android:inputType="textPassword"这个属性上了
     * @param editText
     */
    public static void setEditTextInhibitInputSpaChat(EditText editText) {
        //过滤空格
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ")) return "";
            else return source;
        },});

    }

    public static void setEditTextPassSpaChat(EditText editText) {
        //过滤空格
        editText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ")) return "";
            else return source;
        }, new InputFilter.LengthFilter(16)});

    }
}
