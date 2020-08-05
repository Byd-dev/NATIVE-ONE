package com.pro.bityard.utils;

import com.pro.bityard.manger.NetIncomeManger;

public class ShareUtil {

    private ShareUtil() {

    }

    private static ShareUtil shareUtil = null;

    public static ShareUtil getInstance() {
        if (shareUtil == null) {
            synchronized (NetIncomeManger.class) {
                if (shareUtil == null) {
                    shareUtil = new ShareUtil();
                }
            }

        }
        return shareUtil;

    }


   /* public static void shareText(String content, String name, OnResult onResult) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_TEXT);
        shareParams.setText(content);//必须
        JShareInterface.share(name, shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                onResult.setResult(i);
            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                onResult.setResult(i1);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                onResult.setResult(i);
            }
        });
    }

    public static void shareImg(Bitmap bitmap, String name, OnResult onResult) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        shareParams.setImageData(bitmap);

        JShareInterface.share(name, shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                onResult.setResult(i);
            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                onResult.setResult(i1);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                onResult.setResult(i);
            }
        });
    }*/
}
