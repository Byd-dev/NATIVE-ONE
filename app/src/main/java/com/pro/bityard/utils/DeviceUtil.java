package com.pro.bityard.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.pro.switchlibrary.AppConfig;
import com.pro.switchlibrary.AppUtil;
import com.pro.switchlibrary.JumpPermissionManagement;
import com.pro.switchlibrary.SPUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

public class DeviceUtil {


    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    //??????MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    static Handler handler;
    //private LocationClient locationClient;


    public static boolean isAllGranted(Activity activity) {
        /**
         * ??? 1 ???: ??????????????????????????????
         */
        boolean isAllGranted = checkPermissionAllGranted(activity,
                new String[]{
                        /*Manifest.permission.READ_PHONE_STATE,
                               Manifest.permission.ACCESS_COARSE_LOCATION,
                               Manifest.permission.ACCESS_FINE_LOCATION,
                               Manifest.permission.READ_EXTERNAL_STORAGE,*/
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );

        return isAllGranted;
    }

    /**
     * ???????????????????????????????????????
     */
    private static boolean checkPermissionAllGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // ????????????????????????????????????, ??????????????? false
                return false;
            }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean initMiuiPermission(Activity activity) {
        AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        int locationOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, Binder.getCallingUid(), activity.getPackageName());
        if (locationOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int cameraOp = appOpsManager.checkOp(AppOpsManager.OPSTR_CAMERA, Binder.getCallingUid(), activity.getPackageName());
        if (cameraOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int phoneStateOp = appOpsManager.checkOp(AppOpsManager.OPSTR_READ_PHONE_STATE, Binder.getCallingUid(), activity.getPackageName());
        if (phoneStateOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int readSDOp = appOpsManager.checkOp(AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE, Binder.getCallingUid(), activity.getPackageName());
        if (readSDOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int writeSDOp = appOpsManager.checkOp(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE, Binder.getCallingUid(), activity.getPackageName());
        if (writeSDOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }
        return true;
    }

    /**
     * ?????????????????????px???
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * ?????????????????????px???
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * SD?????????
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static void TouchIDAuthenticate(Activity context, final WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManagerCompat fingerprint = FingerprintManagerCompat.from(context);   //v4?????????API???????????????????????????Android????????????????????????6.0?????????????????????????????????
            if (fingerprint.isHardwareDetected() == true) {
                boolean b = fingerprint.hasEnrolledFingerprints();
                if (b == true) {
                    touch(context, webView);
                } else {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "??????????????????????????????" + "')");

                        }
                    });

                }
            } else {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "?????????????????????????????????" + "')");

                    }
                });

            }

        } else {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????????????????" + "')");

                }
            });

        }
    }

    private static void handleErrorCode(Context context, int code, WebView webView) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                //todo ?????????????????????????????????????????????
                //Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                //todo ???????????????????????????????????????
                //Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                //todo ????????????????????????????????????????????????????????????
                //Toast.makeText(context, "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????????????????????????????????????????????????????" + "')");

                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                //todo ?????????????????????????????????????????????????????????????????????
                // Toast.makeText(context, "?????????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "?????????????????????????????????????????????????????????????????????" + "')");

                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                //todo ??????????????????????????????30???
                //Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                //todo ???????????????????????????????????????
                //Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                break;
        }
    }

    public static void touch(final Activity context, final WebView webView) {

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:   //????????????
                        //todo ????????????
                        handleErrorCode(context, msg.arg1, webView);
                        break;
                    case 2:   //????????????
                        //todo ????????????
                        //Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                        break;
                    case 3:    //????????????
                        //todo ????????????
                        //Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript:sendMessageFromNative('" + AppConfig.key_touch_id + "????????????" + "')");

                        break;
                }

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final FingerprintManagerCompat fingerprint = FingerprintManagerCompat.from(context);   //v4?????????API???????????????????????????Android????????????????????????6.0?????????????????????????????????
            if (fingerprint.isHardwareDetected() == true) {
                boolean b = fingerprint.hasEnrolledFingerprints();
                if (b == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final AlertDialog dialog = builder.create();
                    View dialogView = View.inflate(context, com.pro.switchlibrary.R.layout.layout_item_touch, null);
                    dialog.setView(dialogView);
                    dialog.show();

                    if (dialog.isShowing()) {
                        fingerprint.authenticate(null, 0, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                                super.onAuthenticationError(errMsgId, errString);
                                handler.obtainMessage(1, errMsgId, 0).sendToTarget();
                                dialog.dismiss();

                            }

                            @Override
                            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                handler.obtainMessage(2).sendToTarget();
                                dialog.dismiss();


                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                handler.obtainMessage(3).sendToTarget();
                                dialog.dismiss();

                            }
                        }, handler);

                    }


                } else {

                }


            }


        }
    }


    /**
     * ???????????????????????????
     *
     * @return ["????????????CTCC":3]["????????????CUCC:2]["????????????CMCC":1]["other":0]["???sim???":-1]
     */
    public static String getCarrier(Activity context) {
        String ProvidersName = "N/A";
        // No sim
        if (!hasSim(context)) {
            return ProvidersName;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x004);
            return ProvidersName;
        } else {
            String IMSI = telephonyManager.getSubscriberId();
            if (IMSI == null) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0x004);
            }
            Log.d("print", "getCarrier:89:  " + IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "????????????";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "????????????";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "????????????";
            }
        }


        return ProvidersName;

    }

    /**
     * ?????????????????????sim???
     */
    private static boolean hasSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        if (TextUtils.isEmpty(operator)) {
            return false;
        }
        return true;
    }

    /**
     * ????????????
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * ??????????????????
     * ??????build.gradle??????versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * ???????????????
     * ??????build.gradle??????versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * ?????????????????????miui??????
     *
     * @return
     */

    /*public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            System.out.println("this is a xiaomi device");
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } else {
            return false;
        }
    }*/
    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //?????????????????????????????????,???????????????????????????huawei,???????????????meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    public static AlertDialog openMiuiAppDetDialog = null;

    public static void openMiuiAppDetails(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(com.pro.switchlibrary.R.string.app_name) + "???????????? \"????????????\"???\"??????\"???\"????????????\" ??? \"???????????????\",?????? \"???????????? -> ??????\" ????????????");
        builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JumpPermissionManagement.GoToSetting(activity);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        if (null == openMiuiAppDetDialog)
            openMiuiAppDetDialog = builder.create();
        if (null != openMiuiAppDetDialog && !openMiuiAppDetDialog.isShowing())
            openMiuiAppDetDialog.show();
    }


    /**
     * ??????????????????????????????deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Activity context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
            return "";
        } else {
            String deviceId = tm.getDeviceId();
            if (deviceId == null) {
                return "";
            } else {
                return deviceId;
            }
        }
    }


    // Mac??????
    public static String getMACAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public static String getUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * ??????android????????????????????????
     */
    public static double getAvailMemory(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return (double) mi.totalMem / (1024 * 1024);
    }

    /**
     * CPU??????
     */
    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = 9;
        } catch (NullPointerException e) {
            cores = 9;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };


    public static String getHandSetInfo(Context context) {
        double ran = getAvailMemory(context) / 1000;
        BigDecimal c = new BigDecimal(ran);
        double d = c.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        String handSetInfo = "????????????:" + Build.MODEL
                + "\n????????????:" + Build.VERSION.RELEASE
                + "\n??????:" + Build.PRODUCT
                + "\n??????:" + d + "GB"
                + "\nCPU:" + Build.CPU_ABI + "???" + getMaxCpuFreq() + "GHz*" + getNumberOfCPUCores()
                + "\n????????????:" + Build.DISPLAY
                + "\n????????????:" + getBaseband_Ver()
                + "\n???????????????:" + Build.BRAND
                + "\n????????????:" + Build.DEVICE
                + "\n????????????:" + Build.VERSION.CODENAME
                + "\nIMEI:" + getIMEI(context)
                + "\nSDK?????????:" + Build.VERSION.SDK_INT
                + "\n????????????:" + Build.HARDWARE
                + "\n??????:" + Build.HOST
                + "\n??????ID:" + Build.ID
                + "\nROM?????????:" + Build.MANUFACTURER // ??????????????????rom??????????????????
                + "\napp?????????" + getVersion(context);
        return handSetInfo;
    }


    public static String isPerformance(Context context) {
        String performance = "";
        int sdkInt = Build.VERSION.SDK_INT;
        double ran = getAvailMemory(context) / 1000;
        BigDecimal c = new BigDecimal(ran);
        double d = c.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        Log.d("SplashActivity", "isPerformance:310:   " + d);
        if (sdkInt > 27 && d >= 6) {
            performance = "A+";
        } else if (sdkInt >= 23 && sdkInt <= 27 && d >= 4 && d < 6) {
            performance = "A";
        } else if (sdkInt >= 23 && d < 4 && d >= 3) {
            performance = "B+";
        } else if (sdkInt >= 23 && d < 3) {
            performance = "B";
        } else if (sdkInt > 19 && sdkInt < 23 && d >= 3 && d < 4) {
            performance = "B";
        } else if (sdkInt > 19 && sdkInt < 23 && d <= 2) {
            performance = "C+";
        } else if (sdkInt <= 19) {
            performance = "C";

        }
        return performance;
    }

    /**
     * ????????????????????????
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "?????????";
        }
    }


    /**
     * ????????????????????????
     */
    public static String getBaseband_Ver() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CPU??????????????????
     */
    public static double getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        double a = Integer.parseInt(result.trim());
        double b = a / 1000000;
        BigDecimal c = new BigDecimal(b);
        double d = c.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * ????????????IMEI???
     */
    public static String getIMEI(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        String imei = tm.getDeviceId();
        return imei;
    }


    public static String getCpu() {
        return Build.CPU_ABI;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * ????????????Android API?????????22???23 ...???
     *
     * @return
     */
    public static int getBuildLevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * ????????????Android ?????????4.4???5.0???5.1 ...???
     *
     * @return
     */
    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * ????????????App?????????id
     *
     * @return
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * ????????????App?????????Name
     *
     * @param context
     * @param processId
     * @return
     */
    public static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // ??????????????????App???????????????
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processId) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e(DeviceUtil.class.getName(), e.getMessage(), e);
            }
        }
        return processName;
    }

    /**
     * ??????App?????????
     *
     * @param appName
     * @param application
     * @return
     */
    public static String createAPPFolder(String appName, Application application) {
        return createAPPFolder(appName, application, null);
    }

    /**
     * ??????App?????????
     *
     * @param appName
     * @param application
     * @param folderName
     * @return
     */
    public static String createAPPFolder(String appName, Application application, String folderName) {
        File root = Environment.getExternalStorageDirectory();
        File folder;
        /**
         * ????????????SD???
         */
        if (DeviceUtil.isSDCardAvailable() && root != null) {
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } else {
            /**
             * ?????????SD?????????????????????????????????
             */
            root = application.getCacheDir();
            folder = new File(root, appName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (folderName != null) {
            folder = new File(folder, folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return folder.getAbsolutePath();
    }

    /**
     * ??????Uri??????File
     *
     * @param context
     * @param uri
     * @return
     */
    public static File uri2File(Activity context, Uri uri) {
        File file;
        String[] project = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = context.getContentResolver().query(uri, project, null, null, null);
        if (actualImageCursor != null) {
            int actual_image_column_index = actualImageCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            String img_path = actualImageCursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } else {
            file = new File(uri.getPath());
        }
        if (actualImageCursor != null) actualImageCursor.close();
        return file;
    }

    /**
     * ??????AndroidManifest.xml??? ??????
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String judgeProvider(LocationManager locationManager, Activity activity) {

        Location location;
        StringBuilder sb = new StringBuilder();

        Criteria criteria = new Criteria();

        String locationProvider = locationManager.getBestProvider(criteria, true);


        // String locationProvider = null;

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0x006);
            return "???????????????";
        } else {

            if (TextUtils.isEmpty(locationProvider)) {
                location = getNetWorkLocation(locationManager);

            } else {

                location = locationManager.getLastKnownLocation(locationProvider);


            }

            if (location != null) {

                double latitude1 = location.getLatitude();
                double longitude1 = location.getLongitude();
                sb.append("(latitude:" + latitude1 + ",").append("longitude:" + longitude1 + ")");
                return sb.toString();

            } else {

                //   locationManager.requestLocationUpdates();
                return null;

            }
        }


    }


    @SuppressLint("MissingPermission")
    public static Location getNetWorkLocation(LocationManager locationManager) {
        Location location = null;

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        return location;
    }


    public static String getPhoneInfo(Activity context) {


        StringBuilder sb = new StringBuilder();
        String phoneBrand = getBrand();//????????????

        String OnIdsAvalid = SPUtils.getString(AppConfig.ONIDSAVALID, null);


        //String deviceId = getDeviceId(context);//??????ID
        String carrier = getCarrier(context);//???????????????
        String systemVersion = getSystemVersion();//??????????????????
        String uniqueID = getUniqueID();
        String firstInstallTime = AppUtil.getFirstInstallTime();
        String packageName = context.getApplication().getPackageName();
        String ipAddress = getIPAddress(context);
        String macAddress = getMACAddress();
        String phoneNumber = getPhoneNumber(context);

        String location = SPUtils.getString(AppConfig.LOCATION, null);

       /* LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String location = judgeProvider(locationManager, context);*/


        sb.append("code:" + 200).append(",brand:" + phoneBrand).append(",deviceId:" + OnIdsAvalid)
                .append(",carrier:" + carrier).append(",phoneNumber:" + phoneNumber).append(",systemVersion:" + systemVersion)
                .append(",uniqueID:" + uniqueID).append(",firstInstallTime:" + firstInstallTime).append(",bundleId:" + packageName)
                .append(",ip:" + ipAddress).append(",mac:" + macAddress)
                .append(",location:" + location);



       /* return "brand:" + phoneBrand + ",deviceId:" + deviceId
                + ",carrier:" + carrier + ",systemVersion:" + systemVersion
                + ",uniqueID:" + uniqueID + ",bundleId:" + packageName
                + ",ip:" + ipAddress + ",mac:" + macAddress + ",phone:" + phoneNumber;*/

        return sb.toString();
    }

    //??????????????????
    public static String getPhoneNumber(Activity context) {
        String string = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, 0x003);
            return null;

        } else {

            string = telephonyManager.getLine1Number() != null ? telephonyManager.getLine1Number() : "";
            if (TextUtils.isEmpty(string)) {
                return string;
            } else {
                //    string = string.substring(3, 14);
                return string;
            }

        }

    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//????????????2G/3G/4G??????
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//????????????????????????
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//??????IPV4??????
                return ipAddress;
            }
        } else {
            //?????????????????????,???????????????????????????
        }
        return null;
    }

    /**
     * ????????????int?????????IP?????????String??????
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    private AppIdsUpdater _listener;


    public DeviceUtil() {

    }

    public DeviceUtil(AppIdsUpdater callback) {
        _listener = callback;
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull String ids);

        void getOaid(boolean isSupport, String oaid);

    }


    public static String getDeviceUniqueID(Context context) {
        StringBuilder sbDeviceId = new StringBuilder();

        //??????????????????IMEI???>=6.0 ??????ReadPhoneState?????????
        String imei = getIMEI(context);
        //??????AndroidId??????????????????
        String androidid = getAndroidId(context);
        //???????????????????????????????????????
        String serial = getSERIAL();
        //????????????uuid????????????????????????????????????uuid?????????????????????
        String uuid = getDeviceUUID().replace("-", "");

        //??????imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //??????androidid
        if (androidid != null && androidid.length() > 0) {
            sbDeviceId.append(androidid);
            sbDeviceId.append("|");
        }
        //??????serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //????????????uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }

        //??????SHA1?????????DeviceId??????
        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = getHashByString(sbDeviceId.toString());
                String sha1 = bytesToHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //???????????????DeviceId
                    return sha1;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //????????????????????????????????????????????????
        //???DeviceId??????????????????????????????????????????DeviceId?????????
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * ???????????????AndroidId
     *
     * @param context ?????????
     * @return ?????????AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * ??????????????????????????????WTK7N16923005607???, ????????????????????????
     *
     * @return ???????????????
     */
    private static String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * ??????????????????uuid
     * ?????????????????????????????????????????????
     *
     * @return ????????????uuid
     */
    private static String getDeviceUUID() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * ???SHA1
     *
     * @param data ??????
     * @return ?????????hash???
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    /**
     * ???16???????????????
     *
     * @param data ??????
     * @return 16???????????????
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = (Integer.toHexString(data[n] & 0xFF));
            if (stmp.length() == 1)
                sb.append("0");
            sb.append(stmp);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }


    public static File initCacheDir(Context context, String dirPath) {
        File cacheDir;
        if (!isSDCardAvailable()) {
            cacheDir = context.getDir(dirPath, Context.MODE_PRIVATE);
        } else {
            if (context.getExternalCacheDir() == null) {
                cacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName(), dirPath);
            } else {
                cacheDir = new File(context.getExternalCacheDir(), dirPath);
            }
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
        }
        return cacheDir;
    }
}
