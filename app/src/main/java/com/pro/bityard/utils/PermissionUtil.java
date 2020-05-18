package com.pro.bityard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import com.pro.bityard.R;
import com.pro.bityard.manger.NetIncomeManger;

import androidx.core.app.ActivityCompat;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class PermissionUtil {

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


}
