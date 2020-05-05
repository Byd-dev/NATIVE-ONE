package com.pro.bityard.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pro.bityard.R;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.utils.DeviceUtil;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.WebFileUploader;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class WebActivity extends BaseActivity {


    private static final int MY_PERMISSION_REQUEST_CODE = 10000;


    private static WebActivity instance;
    private TextView text_err;

    private RelativeLayout layout;

    public static WebActivity getInstance() {


        if (instance == null) {
            instance = new WebActivity();
        }
        return instance;
    }

    public static class UrlBuilder {
        private String url;
        private Map<String, Object> params;

        public UrlBuilder url(String url) {
            this.url = url;
            return this;
        }

        public UrlBuilder put(String key, Object param) {
            if (params == null) {
                this.params = new HashMap<>();
            }
            this.params.put(key, param);
            return this;
        }

        public String toUrl() {
            StringBuilder builder = new StringBuilder();
            if (!TextUtils.isEmpty(url)) {
                builder.append(url);
            }
            if (params != null && !params.isEmpty()) {
                if (url.contains("?")) {
                    builder.append("&");
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        if (entry.getValue() != null) {
                            builder.append(entry.getKey());
                            builder.append('=');
                            builder.append(entry.getValue().toString());
                            builder.append('&');
                        }
                    }
                    if (builder.toString().endsWith("&")) {
                        builder.deleteCharAt(builder.length() - 1);
                    }
                } else {
                    builder.append("?");
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        if (entry.getValue() != null) {
                            builder.append(entry.getKey());
                            builder.append('=');
                            builder.append(entry.getValue().toString());
                            builder.append('&');
                        }
                    }
                    if (builder.toString().endsWith("&")) {
                        builder.deleteCharAt(builder.length() - 1);
                    }
                }

             /*   builder.append("?");
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        builder.append(entry.getKey());
                        builder.append('=');
                        builder.append(entry.getValue().toString());
                        builder.append('&');
                    }
                }
                if (builder.toString().endsWith("&")) {
                    builder.deleteCharAt(builder.length() - 1);
                }*/
            }
            return builder.toString();
        }
    }


    private static final String TAG = "WebView";

    private static final String KEY_TITLE = "title";

    private static final String KEY_URL = "url";
    private static final String KEY_HAS_SERVICE = "has_service";
    private static final String KEY_BACKGROUND_COLOR = "background_color";
    private static final String KEY_HAS_CLOSE_BUTTON = "has_close_button";
    private static final String KEY_HTML = "html";
    private static final String KEY_HAS_SHARE_ARTICLE = "has_share_article";
    private static final String KEY_ARTICLE = "article";
    private static final String KEY_FROM = "from";
    private static final String KEY_HAS_TITLE = "hasTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    protected int setContentLayout() {
        return R.layout.activity_webview;
    }

    private static void openWeb(Context context, Intent intent) {

        intent.putExtra("title", "color");

        context.startActivity(intent);

    }


    private void processIntent(Intent intent) {
        Log.d(TAG, "openUrlNotitle:366:   " + intent);
        Log.d(TAG, "openUrlNotitle:368:   " + intent);

        if (intent != null) {
            mTitle = intent.getStringExtra(KEY_TITLE);
            mUrl = intent.getStringExtra(KEY_URL);
            String title = intent.getStringExtra("title");

            if (title.equals("color")) {
                setStatusBar(getResources().getColor(com.pro.switchlibrary.R.color.black));

            }


            Boolean hasService = intent.getBooleanExtra(KEY_HAS_SERVICE, false);
            //boolean hasCloseButton = intent.getBooleanExtra(KEY_HAS_CLOSE_BUTTON, false);
            String html = intent.getStringExtra(KEY_HTML);
            boolean hasShareArticle = intent.getBooleanExtra(KEY_HAS_SHARE_ARTICLE, false);

            boolean hasTitle = intent.getBooleanExtra(KEY_HAS_TITLE, true);


            if (!TextUtils.isEmpty(html)) {
                mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                return;
            }
            Log.d(TAG, "processIntent:最终地址:  " + mUrl);
            mWebView.loadUrl(mUrl);
        }
    }


    private void initViews() {


        text_err.setOnClickListener(v -> mWebView.reload());

        //  mWebView=findViewById(R.id.webview);
     /*   mWebView = new WebView(getApplicationContext());
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.addView(mWebView);*/
        initWebViewSetting();
        mWebView.setBackgroundColor(0);
        mWebView.addJavascriptInterface(new AppJs(this, mWebView), "AppJs");


        mWebView.setWebViewClient(new WebViewClient() {


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()) {
                    text_err.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                text_err.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                    case SslError.SSL_DATE_INVALID:
                        message = "The date of the certificate is invalid";
                        break;
                    case SslError.SSL_INVALID:
                    default:
                        message = "A generic error occurred";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);

                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                text_err.setVisibility(View.INVISIBLE);
                mWebView.setVisibility(View.VISIBLE);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//  Logger.i("print", "web url override = 653:" + url);
                Log.d("print", "shouldOverrideUrlLoading:671: " + url);
                if (url.contains("mqqwpa")) { //企业QQ
                    openApp(url, "请先安装qq");
                } else if (url.startsWith("http://wpd.b.qq.com/")) { //防止跳回腾讯页面
                    // mWebView.loadUrl(ApiConfig.getFullUrl(ApiConfig.Web.CUSTOMER_SERVICE));
                } else if (url.startsWith("intent://")) {
                    openApp(url, "未安装应用");
                } else if (url.startsWith("alipays://") || url.startsWith("mqqapi://")) {
                    startAlipayActivity(url);
                    //pay.palmpay
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M) && (url.contains("alipays://") || url.contains("mqqapi://"))) {
                    //   Log.d("print", "shouldOverrideUrlLoading:683:: " + url);

                    startAlipayActivity(url);
                } else if (url.startsWith("weixin://")) {
                    //   Log.d("print", "shouldOverrideUrlLoading:686:" + url);
                    //如果return false  就会先提示找不到页面，然后跳转微信
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                    }
                    return true;
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M) && (url.startsWith("weixin://"))) {
                    //  Log.d("print", "shouldOverrideUrlLoading:699: " + url);

                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                    }
                    return true;
                } else {
                    //H5微信支付要用，不然说"商家参数格式有误"
                    Map<String, String> extraHeaders = new HashMap<String, String>();
                    extraHeaders.put("Referer", "http://www.smartgouwu.com");
                    view.loadUrl(url, extraHeaders);
                    //    Log.d("print", "shouldOverrideUrlLoading:729: " + url);
                    // mWebView.loadUrl(url);
                }


                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String titleText = view.getTitle();
                Log.d(TAG, "onPageFinished:658: " + titleText);
                if (!TextUtils.isEmpty(titleText) && !url.contains(titleText)) {
                    // mTitle = titleText;
                    //mTitleBar.setTitle(mTitle);
                }
                if ("adv".equals(getIntent() != null ? getIntent().getStringExtra(KEY_FROM) : null)) {
                    mWebView.loadUrl("javascript: "
                            + "Array.prototype.slice.call(document.getElementsByTagName('img')).forEach(function(item) { item.style.width = \"100%\"})");
                }
            }


        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                return super.onConsoleMessage(consoleMessage);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return mWebFileUploader.onLOLLIPOP(filePathCallback, fileChooserParams);
            }

            //// Andorid 4.1+
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mWebFileUploader.onJellyBean(uploadMsg, acceptType, capture);
            }

            // Andorid 3.0+
            protected void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mWebFileUploader.onHoneyComB(valueCallback, acceptType);
            }


        });
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> openApp(url));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


    private static boolean isProgress = true;

    public void openUrlWithTitle(Context context, String H5url) {

        isProgress = false;
        if (context != null) {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra(KEY_URL, H5url);
            intent.putExtra(KEY_HAS_TITLE, true);
            openWeb(context, intent);
        }
    }

    public void openUrlNoTitle(Context context, String H5url, String title) {

        String performance = DeviceUtil.isPerformance(context);


        isProgress = false;
        if (context != null) {
            String url = new UrlBuilder()
                    .url(H5url)
                    .put("lv", performance)
                    .toUrl();

            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra(KEY_URL, url);
            intent.putExtra(KEY_TITLE, title);
            intent.putExtra(KEY_HAS_TITLE, false);
            openWeb(context, intent);
        }
    }


    private WebView mWebView;

    private String mUrl;
    private String mTitle;

    private WebFileUploader mWebFileUploader;


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0X0000000);
        }

        mWebView = findViewById(com.pro.switchlibrary.R.id.webview);

        text_err = findViewById(com.pro.switchlibrary.R.id.text_err);
        layout = findViewById(com.pro.switchlibrary.R.id.layout);


        initViews();
        initData();
        processIntent(getIntent());

    }

    protected void load(String url) {
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.removeJavascriptInterface("AppJs");
            layout.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public void goForward() {
        mWebView.goForward();
    }

    public void goBottom() {
        while (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }


    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            //  finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initWebViewSetting() {
        WebSettings settings = mWebView.getSettings();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(getCacheDir().getPath());
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setGeolocationEnabled(true);

        //设置自适应
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    @Override
    public void initData() {
        mWebFileUploader = new WebFileUploader(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            // super.onBackPressed();
        }
    }


    //打开本地应用
    private void openApp(String... url) {
        try {
            Intent intent = Intent.parseUri(url[0], Intent.URI_INTENT_SCHEME);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else if (url.length > 1 && url[1] != null) {
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    //old
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 跳转到主页

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }

    }


    //系统授权设置的弹框
    AlertDialog openAppDetDialog = null;

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(com.pro.switchlibrary.R.string.app_name) + getString(com.pro.switchlibrary.R.string.all_permission_required));
        builder.setPositiveButton("手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });
        if (null == openAppDetDialog)
            openAppDetDialog = builder.create();
        if (null != openAppDetDialog && !openAppDetDialog.isShowing())
            openAppDetDialog.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
