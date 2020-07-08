package com.pro.bityard.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.guide.SplashActivity;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.switchlibrary.JumpPermissionManagement;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class PermissionUtil {
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    private PermissionUtil() {

    }

    private static PermissionUtil imageUtil = null;

    public static PermissionUtil getInstance() {
        if (imageUtil == null) {
            synchronized (NetIncomeManger.class) {
                if (imageUtil == null) {
                    imageUtil = new PermissionUtil();
                }
            }

        }
        return imageUtil;

    }

    public static boolean readAndWrite(Context context) {

//检测是否有写的权限
        int permission = checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            return false;
        } else {
            return true;
        }

    }

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    public void initPermission(Activity activity, OnResult onResult) {
//        判断是否是6.0以上的系统
        if (Build.VERSION.SDK_INT >= 23) {
            if (isAllGranted(activity)) {
                if (isMIUI()) {
                    if (!initMiuiPermission(activity)) {
                        openMiuiAppDetails(activity);
                        return;
                    }
                }
                onResult.setResult(SUCCESS);
                return;
            } else {
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{
                                 Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSION_REQUEST_CODE
                );
            }
        } else {
            // gotoHomeActivity();
            onResult.setResult(SUCCESS);

        }
    }
    AlertDialog openMiuiAppDetDialog = null;

    private void openMiuiAppDetails(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.app_name) + activity.getString(R.string.all_permission_required));

        builder.setPositiveButton("手动授权", (dialog, which) -> JumpPermissionManagement.GoToSetting(activity));
        builder.setCancelable(false);
        builder.setNegativeButton("取消", (dialog, which) -> {
            //finish();
        });
        if (null == openMiuiAppDetDialog)
            openMiuiAppDetDialog = builder.create();
        if (null != openMiuiAppDetDialog && !openMiuiAppDetDialog.isShowing())
            openMiuiAppDetDialog.show();
    }

    public boolean isAllGranted(Activity activity) {


        boolean isAllGranted = checkPermissionAllGranted(activity,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );

        return isAllGranted;
    }


    private boolean checkPermissionAllGranted(Activity activity,String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean initMiuiPermission(Activity activity) {
        AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        int locationOp = appOpsManager.checkOp(AppOpsManager.OPSTR_FINE_LOCATION, Binder.getCallingUid(), activity.getPackageName());
        if (locationOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int cameraOp = appOpsManager.checkOp(AppOpsManager.OPSTR_CAMERA, Binder.getCallingUid(), activity.getPackageName());
        if (cameraOp == AppOpsManager.MODE_IGNORED) {
            return false;
        }

        int phoneStateOp = appOpsManager.checkOp(AppOpsManager.OPSTR_READ_PHONE_STATE, Binder.getCallingUid(),activity. getPackageName());
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
}
