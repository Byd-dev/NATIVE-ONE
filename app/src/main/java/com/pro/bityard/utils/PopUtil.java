package com.pro.bityard.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pro.bityard.BuildConfig;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.api.OnNetResult;
import com.pro.bityard.api.OnResult;
import com.pro.bityard.api.PopResult;
import com.pro.bityard.entity.HistoryEntity;
import com.pro.bityard.manger.QuoteListManger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import cn.jiguang.share.wechat.Wechat;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class PopUtil {
    private static PopUtil popUtil;


    public static PopUtil getInstance() {
        if (popUtil == null) {
            synchronized (QuoteListManger.class) {
                if (popUtil == null) {
                    popUtil = new PopUtil();
                }
            }

        }
        return popUtil;

    }

    public void showTip(Activity activity, View layout_view, boolean showCancel, String content, OnPopResult onPopResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_tip_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_tip = view.findViewById(R.id.text_content);
        text_tip.setLineSpacing(1.1f, 1.5f);

        text_tip.setText(content);


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            onPopResult.setPopResult(true);
            popupWindow.dismiss();
        });

        TextView text_cancel = view.findViewById(R.id.text_cancel);
        if (showCancel) {
            text_cancel.setVisibility(View.VISIBLE);
        } else {
            text_cancel.setVisibility(View.GONE);

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            onPopResult.setPopResult(false);
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    public void showSuccessTip(Activity activity, View layout_view, OnPopResult onPopResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_tip_pop_register_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            onPopResult.setPopResult(false);
            popupWindow.dismiss();
        });

        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            onPopResult.setPopResult(true);
            popupWindow.dismiss();
        });

        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    public void showLongTip(Activity activity, View layout_view, boolean showCancel, String title, String title2, String title3,
                            String value, String value2, OnPopResult onPopResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_tip_long_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView text_one = view.findViewById(R.id.text_one);
        text_one.setLineSpacing(1.1f, 1.5f);
        text_one.setText(title);
        TextView text_two = view.findViewById(R.id.text_two);
        text_two.setLineSpacing(1.1f, 1.5f);
        text_two.setText(title2);
        TextView text_three = view.findViewById(R.id.text_three);
        text_three.setLineSpacing(1.1f, 1.5f);
        text_three.setText(title3);

        TextView text_value = view.findViewById(R.id.text_value);
        text_value.setLineSpacing(1.1f, 1.5f);
        text_value.setText(value + " " + activity.getResources().getString(R.string.text_usdt));

        TextView text_value2 = view.findViewById(R.id.text_value2);
        text_value2.setLineSpacing(1.1f, 1.5f);
        text_value2.setText(value2 + " " + activity.getResources().getString(R.string.text_usdt));


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            onPopResult.setPopResult(true);
            popupWindow.dismiss();
        });

        TextView text_cancel = view.findViewById(R.id.text_cancel);
        if (showCancel) {
            text_cancel.setVisibility(View.VISIBLE);
        } else {
            text_cancel.setVisibility(View.GONE);

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            onPopResult.setPopResult(false);
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    public void showEdit(Activity activity, View layout_view, boolean showCancel, OnResult onResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_edit_pop_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText edit_username = view.findViewById(R.id.edit_username);


        if (edit_username.getText().toString().equals("")) {

        }


        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            NetManger.getInstance().updateNickName(edit_username.getText().toString(), (state, response) -> {
                if (state.equals(SUCCESS)) {
                    onResult.setResult(edit_username.getText().toString());
                    popupWindow.dismiss();


                } else if (state.equals(FAILURE)) {
                    Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        TextView text_cancel = view.findViewById(R.id.text_cancel);
        if (showCancel) {
            text_cancel.setVisibility(View.VISIBLE);
        } else {
            text_cancel.setVisibility(View.GONE);

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }


    private String hash=null;
    /*图片验证码*/
    public void showVerification(Activity activity, View layout_view, OnResult onResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_verification_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        ImageView img_code = view.findViewById(R.id.img_code);

        EditText edit_code = view.findViewById(R.id.edit_code);


        getCode(activity, img_code, response -> {
            hash=response.toString();
        });

        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        view.findViewById(R.id.img_refresh).setOnClickListener(v -> getCode(activity, img_code, response -> {

        }));

        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            String s = edit_code.getText().toString();
            if (s.equals("")) {
                Toast.makeText(activity, "请输入图形验证码", Toast.LENGTH_SHORT).show();
            } else {
                onResult.setResult(hash+","+s);
                popupWindow.dismiss();
            }
        });

        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);

    }

    public void getCode(Context context, ImageView img_code,OnResult onResult) {
        String hash = Util.Random32();
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("vHash", hash);
        Log.d("print", "getCode:290:  "+hash);
        NetManger.getInstance().getBitmapRequest("/api/code/image.jpg", map, (state, response) -> {
            if (state.equals(SUCCESS)) {
                Bitmap bitmap = (Bitmap) response;
                img_code.post(() -> {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    Glide.with(context.getApplicationContext()).load(bytes)
                            .centerCrop().into(img_code);
                    onResult.setResult(hash);
                });
            }
        });


    }


    private List<String> topList = new ArrayList<>();
    private List<String> midList = new ArrayList<>();
    private List<String> lowList = new ArrayList<>();


    public void dialogShare() {

    }


    /*显示分享详情*/
    public void showShare(Activity activity, View layout_view, HistoryEntity.DataBean dataBean, PopResult popResult) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_share_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layout_share = view.findViewById(R.id.layout_share);


        double opPrice = dataBean.getOpPrice();
        double cpPrice = dataBean.getCpPrice();
        //服务费
        double lever = dataBean.getLever();
        double margin = dataBean.getMargin();
        double income = dataBean.getIncome();


        TextView text_name = view.findViewById(R.id.text_name);
        String[] split = Util.quoteList(dataBean.getContractCode()).split(",");
        text_name.setText(split[0]);
        TextView text_currency = view.findViewById(R.id.text_currency);
        text_currency.setText("/" + dataBean.getCurrency());

        TextView text_save = view.findViewById(R.id.text_save);

        text_save.setOnClickListener(v -> {
            if (PermissionUtil.readAndWrite(activity)) {
                ImageUtil.SaveBitmapFromView(activity, layout_share);
                Toast.makeText(activity, activity.getResources().getString(R.string.text_save), Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            } else {
                String[] PERMISSIONS = {
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
            }
        });
        //开仓价
        TextView text_open_price = view.findViewById(R.id.text_open_price);
        text_open_price.setText(String.valueOf(opPrice));
        //平仓价
        TextView text_close_price = view.findViewById(R.id.text_close_price);
        text_close_price.setText(String.valueOf(cpPrice));
        TextView text_lever = view.findViewById(R.id.text_lever);
        if (dataBean.isIsBuy()) {
            text_lever.setText(activity.getString(R.string.text_much) + lever + "×");
        } else {
            text_lever.setText(activity.getString(R.string.text_empty) + lever + "×");

        }
        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });
        TextView text_rate = view.findViewById(R.id.text_rate);
        text_rate.setText(TradeUtil.ratio(income, margin));

        if (income > 0) {
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_green));

        } else {
            text_rate.setTextColor(activity.getResources().getColor(R.color.text_quote_red));

        }

        topList.add(activity.getString(R.string.text_wonderful_unrivalled));
        topList.add(activity.getString(R.string.text_king));
        topList.add(activity.getString(R.string.text_awesome));

        midList.add(activity.getString(R.string.text_set_goal));
        midList.add(activity.getString(R.string.text_one_step));
        midList.add(activity.getString(R.string.text_get_less));

        lowList.add(activity.getString(R.string.text_stop_loss));
        lowList.add(activity.getString(R.string.text_do_hesitate));
        lowList.add(activity.getString(R.string.text_just_case));


        Random random = new Random();
        int top = random.nextInt(topList.size());
        int mid = random.nextInt(midList.size());
        int low = random.nextInt(lowList.size());


        TextView text_title = view.findViewById(R.id.text_title);

        ImageView img_person = view.findViewById(R.id.img_person);
        double rate = TradeUtil.ratioDouble(income, margin);
        if (rate >= 0 && rate < 100) {
            text_title.setText(midList.get(mid));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_win));
        } else if (rate >= 100) {
            text_title.setText(topList.get(top));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_win));

        } else if (rate < 0) {
            text_title.setText(lowList.get(low));
            img_person.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_loss));

        }
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);

        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);

        popResult.setResult(layout_share, popupWindow);

        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);


    }


    /*分享弹窗*/
    public void showSharePlatform(Activity activity, View layout_view, HistoryEntity.DataBean dataBean) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.layout_share_pop, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        /*同时打开图片*/
        showShare(activity, layout_view, dataBean, (response1, repose2) -> {
            PopupWindow popupWindow1 = (PopupWindow) repose2;


            view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
                popupWindow.dismiss();
                popupWindow1.dismiss();
            });

            //保存图片
            view.findViewById(R.id.text_save).setOnClickListener(v -> {

                if (PermissionUtil.readAndWrite(activity)) {
                    View view1 = (View) response1;
                    ImageUtil.SaveBitmapFromView(activity, view1);
                    Toast.makeText(activity, activity.getResources().getString(R.string.text_save), Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    popupWindow1.dismiss();
                } else {
                    String[] PERMISSIONS = {
                            "android.permission.READ_EXTERNAL_STORAGE",
                            "android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
                }


            });

            //微信朋友分享
            view.findViewById(R.id.text_friend);
            view.setOnClickListener(v -> {
                ShareUtil.shareImg(ImageUtil.getBitmap((View) response1), Wechat.Name, response ->
                        Log.d("jiguang", "setResult:292:  " + response));
            });
        });


        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);
    }

    public void dialogUp(Activity activity, View layout_view, String versionMessage, String url) {
        Util.lightOff(activity);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_tip_pop_update_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView text_content = view.findViewById(R.id.text_content);

        text_content.setText(versionMessage);

        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        view.findViewById(R.id.text_sure).setOnClickListener(v -> {
            //更新应用提醒
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
            getFile(activity, url, (state, response) -> {
                if (state.equals(BUSY)) {
                    progressDialog.setMessage(activity.getString(R.string.text_progressing));
                } else if (state.equals(SUCCESS)) {
                    progressDialog.setMessage(activity.getResources().getText(R.string.text_success));
                    progressDialog.dismiss();
                } else if (state.equals(FAILURE)) {
                    progressDialog.setMessage(activity.getResources().getText(R.string.text_failure));
                    progressDialog.dismiss();
                }
            }); //下载apk

            popupWindow.dismiss();
        });

        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // popupWindow.setAnimationStyle(R.style.pop_anim_quote);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(layout_view, Gravity.CENTER, 0, 0);






      /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//		alertDialogBuilder.setIcon(R.drawable.)
        alertDialogBuilder.setTitle("版本更新");
        alertDialogBuilder.setMessage(versionMessage);
        alertDialogBuilder.setPositiveButton(activity.getResources().getText(R.string.text_sure), (dialog, which) -> {

            //更新应用提醒
            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
            getFile(activity, url, (state, response) -> {
                if (state.equals(BUSY)) {
                    progressDialog.setMessage("软件下载中~");
                } else if (state.equals(SUCCESS)) {
                    progressDialog.setMessage("下载成功~");
                    progressDialog.dismiss();

                } else if (state.equals(FAILURE)) {
                    progressDialog.setMessage("下载失败~");
                    progressDialog.dismiss();
                }
            }); //下载apk
        });
        alertDialogBuilder.setNegativeButton(activity.getResources().getText(R.string.text_cancel_position), (dialog, which) -> {

        });

        alertDialogBuilder.setCancelable(false);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();//将dialog显示出来*/
    }


    public void getFile(Activity activity, String versionUrl, OnNetResult onNetResult) {

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
        final String fileName = "bityard.apk";
        final File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            file.delete();
        }
        OkGo.<File>get(versionUrl)
                .tag(this)
                .execute(new FileCallback(filePath, fileName) {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        onNetResult.onNetResult(BUSY, "start");
                        Log.e("MainActivity", "开始下载");
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        onNetResult.onNetResult(SUCCESS, "success");

                        Log.e("MainActivity", "file--" + file);
                        openFile(activity, file);  //安装apk
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        onNetResult.onNetResult(FAILURE, "failure");
                        Log.e("MainActivity", "onError: " + response.message());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Log.e("MainActivity", "progress" + progress.fraction * 100);
                    }
                });
    }

    private void openFile(Activity activity, File file) {

        //Android 7.0及以上
        if (Build.VERSION.SDK_INT >= 24) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                }
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".MyFileProvider", file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } else {
            Log.e("MainActivity", "小于24");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            activity.startActivity(intent);
        }
    }


}
