package com.pro.bityard.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.pro.bityard.utils.DeviceUtil;
import com.pro.switchlibrary.SPUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/**
 * 客服端和h5交互代码
 */
public class AppJs {

    private static final String TAG = "AppJs";
    private static final int REQUEST_CODE_CAMERA = 102;
    private static final int REQUEST_CODE_BANKCARD = 111;

    private Activity activity;
    private WebView webView;

    public AppJs(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }


    /**
     * 打开Android手机应用市场，让用户对app进行下载
     *
     * @author
     * @version 1
     */
    @JavascriptInterface
    public void openAppMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + activity.getPackageName()));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "未找到任何应用市场", Toast.LENGTH_SHORT).show();
        }
    }

    @JavascriptInterface
    public void postMessage(String key) {

    }

    @JavascriptInterface
    public void openWeChat() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            this.activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this.activity, "检测到您手机没有安装微信,请安装后使用该功能", Toast.LENGTH_SHORT).show();
        }
    }

    @JavascriptInterface
    public void hello(String msg) {
        Toast.makeText(activity, "调用了方法 hello", Toast.LENGTH_SHORT).show();


    }

    @JavascriptInterface
    public String GetDeviceInfo() {
        String phoneInfo = DeviceUtil.getPhoneInfo(activity);
        Log.d(TAG, "GetDeviceInfo:68:  " + phoneInfo);
        return phoneInfo;
    }


    //前端可以凭此属性判断内容是否是使用移动端打开的
    @JavascriptInterface
    public String isSuperman() {
        return "true";
    }

    //判断当前手机性能
    @JavascriptInterface
    public String isPerformance() {
        return DeviceUtil.isPerformance(activity);
    }

    @JavascriptInterface
    public void LinkTo(String url) {
        Uri content_url = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, content_url);
        activity.startActivity(intent);
    }


    //指纹识别
    @JavascriptInterface
    public boolean TouchIDSupport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final FingerprintManagerCompat fingerprint = FingerprintManagerCompat.from(activity);   //v4包下的API，包装内部已经判断Android系统版本是否大于6.0，这也是官方推荐的方式
            return fingerprint.hasEnrolledFingerprints();
        } else {
            return false;
        }
    }


    @JavascriptInterface
    public void TouchIDAuthenticate() {
        DeviceUtil.TouchIDAuthenticate(activity, webView);
    }








    /**
     * 结束当前页面
     *
     * @author
     */
    @JavascriptInterface
    public void finishActivity() {
        activity.finish();
    }

    /**
     * 提供h5直接拨打电话
     *
     * @param number
     */
    @JavascriptInterface
    public void call(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }







}
