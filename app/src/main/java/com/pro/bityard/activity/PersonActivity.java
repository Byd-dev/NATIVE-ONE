package com.pro.bityard.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pro.bityard.R;
import com.pro.bityard.api.NetManger;
import com.pro.bityard.base.BaseActivity;
import com.pro.bityard.config.AppConfig;
import com.pro.bityard.entity.LoginEntity;
import com.pro.bityard.manger.UserDetailManger;
import com.pro.bityard.utils.FileUtil;
import com.pro.bityard.utils.Util;
import com.pro.bityard.view.CircleImageView;
import com.pro.bityard.viewutil.StatusBarUtil;
import com.pro.switchlibrary.SPUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;

import static com.pro.bityard.api.NetManger.BUSY;
import static com.pro.bityard.api.NetManger.FAILURE;
import static com.pro.bityard.api.NetManger.SUCCESS;

public class PersonActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.layout_view)
    LinearLayout layout_view;


    @BindView(R.id.img_head)
    CircleImageView img_head;
    @BindView(R.id.text_uid)
    TextView text_uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public static void enter(Context context) {
        Intent intent = new Intent(context, PersonActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initView(View view) {
        findViewById(R.id.img_back).setOnClickListener(this);


        findViewById(R.id.layout_one).setOnClickListener(this);
        findViewById(R.id.layout_two).setOnClickListener(this);
        findViewById(R.id.layout_three).setOnClickListener(this);
        findViewById(R.id.layout_four).setOnClickListener(this);
        mExtStorDir = Environment.getExternalStorageDirectory().toString();

    }

    @Override
    protected void initData() {
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        text_uid.setText(data.getUser().getUserId());
        Log.d("print", "initData:114:  " + data);
        Glide.with(this).load(data.getUser().getAvatar())
                .error(R.mipmap.icon_my_bityard)
                .into(img_head);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.layout_two:
                Util.lightOff(this);
                showImgSelectWindow(this, layout_view);
                break;


        }
    }

    /*显示删除的按钮*/
    public void showImgSelectWindow(Activity activity, View layout_view) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.item_img_select_layout, null);
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.findViewById(R.id.text_camera).setOnClickListener(view1 -> {

            popupWindow.dismiss();
            checkStoragePermission();


        });


        view.findViewById(R.id.text_album).setOnClickListener(view12 -> {

            popupWindow.dismiss();
            checkReadPermission();

        });


        view.findViewById(R.id.text_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });


        Util.dismiss(activity, popupWindow);
        Util.isShowing(activity, popupWindow);


        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(100);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(layout_view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);

    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int resultCAMERA = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_DENIED || resultCAMERA == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, 0);
            } else {
                choseHeadImageFromCameraCapture();
            }
        }


    }

    private String mExtStorDir;
    private Uri mUriPath;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final String CROP_IMAGE_FILE_NAME = "bala_crop.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_RESULT_REQUEST = 3;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        String savePath = mExtStorDir;

        Intent intent;
        // 判断存储卡是否可以用，可用进行存储

        if (hasSdcard()) {
            //设定拍照存放到自己指定的目录,可以先建好
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            Uri pictureUri;
            File pictureFile = new File(savePath, IMAGE_FILE_NAME);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //  pictureUri = FileProvider.getUriForFile(PersonActivity.this, getPackageName() + ".fileProvider", pictureFile);
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureUri = Uri.fromFile(pictureFile);
            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureUri = Uri.fromFile(pictureFile);
            }*/
            if (intent != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        pictureUri);
                startActivityForResult(intent, CODE_CAMERA_REQUEST);
            }
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    private void checkReadPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(PersonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.请求权限
                ActivityCompat.requestPermissions(PersonActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
            } else {
                choseHeadImageFromGallery();

            }
        }



    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        // 设置文件类型    （在华为手机中不能获取图片，要替换代码）
        /*Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image*//*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);*/

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_GALLERY_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            // Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("print", "onActivityResult: 498:" + requestCode+"    "+intent.getData());

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                Uri uri = intent.getData();
                cropRawPhoto(uri);
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(getImageContentUri(tempFile));
                } else {
                    Toast.makeText(getApplication(), "null SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUriPath));
                    setImageToHeadView(intent, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Log.d("print", "cropRawPhoto: 683:" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        //startActivityForResult(intent, CODE_RESULT_REQUEST); //直接调用此代码在小米手机有异常，换以下代码
        String mLinshi = System.currentTimeMillis() + CROP_IMAGE_FILE_NAME;
        File mFile = new File(mExtStorDir, mLinshi);
//        mHeadCachePath = mHeadCacheFile.getAbsolutePath();
        // Log.d("print", "cropRawPhoto:702:  /storage/emulated/0/1553495151372bala_crop.jpg--- " + mFile);
        mUriPath = Uri.parse("file://" + mFile.getAbsolutePath());

        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //注意：此处应设置return-data为false，如果设置为true，是直接返回bitmap格式的数据，耗费内存。设置为false，然后，设置裁剪完之后保存的路径，即：intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }




    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent, Bitmap b) {
        try {
            if (intent != null) {
                Bitmap bitmap = imageZoom(b);//看个人需求，可以不压缩
                img_head.setImageBitmap(bitmap);
                long millis = System.currentTimeMillis();
                File file = FileUtil.saveFile(mExtStorDir, millis + CROP_IMAGE_FILE_NAME, bitmap);
                if (file != null) {
                    //传递新的头像信息给我的界面
                    NetManger.getInstance().postImg(file, (state, response) -> {
                        if (state.equals(BUSY)) {
                            showProgressDialog();
                        } else if (state.equals(SUCCESS)) {
                            dismissProgressDialog();
                            UserDetailManger.getInstance().detail();
                            Toast.makeText(this, getResources().getText(R.string.text_success), Toast.LENGTH_SHORT).show();
                        } else if (state.equals(FAILURE)) {
                            dismissProgressDialog();
                            Toast.makeText(this, getResources().getText(R.string.text_failure), Toast.LENGTH_SHORT).show();
                        }

                    });

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap imageZoom(Bitmap bitMap) {
        //图片允许最大空间   单位：KB
        double maxSize = 1000.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        return bitMap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
