package com.pro.bityard.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pro.bityard.entity.LoginEntity;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 10998 on 2017/5/21 /13:32
 * com.ddzl.lottery251.base
 */

public abstract class BaseFragment extends Fragment  {
    private FragmentManager fragmentManager;
    private Fragment showFragment;
    public View mView;
    private ProgressDialog mProgressDialog;
    public Bundle mSavedBundle;
    private Unbinder unbinder;

    private View mContentView;
    private Timer mTimer;

    private boolean isPrepared;
    private boolean isLazyLoaded;
    protected Context mContext;


    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    protected boolean mIsViewCreated = false;

    private String token;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared=true;
        this.mContext=getActivity();

        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    private  void lazyLoad(){
        if (getUserVisibleHint()&&isPrepared&&!isLazyLoaded){
            onLazyLoad();
            isLazyLoaded=true;
        }
    }

    protected abstract void onLazyLoad();

    protected void startScheduleJob(final Handler handler, long delay, long interval) {
        if (mTimer != null) cancelTimer();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.sendEmptyMessage(0);
                }
            }
        }, delay, interval);
    }

    protected void startScheduleJob(final Handler handler, long delay, long interval, final int what) {
        if (mTimer != null) cancelTimer();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (handler != null) {
                    handler.sendEmptyMessage(what);
                }
            }
        }, delay, interval);
    }

    protected void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        intPresenter();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       /* if (isFinishing()) {
            View v = new View(mContext);
            mIsViewCreated = true;
            return v;
        } else {
            View v = initView(inflater, container, savedInstanceState);
            initData();
            mIsViewCreated = true;
            return v;
        }*/

        mContentView = inflater.inflate(setLayoutResourceID(), container, false);
        unbinder = ButterKnife.bind(this, mContentView);
        mProgressDialog = new ProgressDialog(getActivity());
        fragmentManager = getChildFragmentManager();
        if (mContentView==null){

        }
        initView(mContentView);
        initData();


        return mContentView;

    }

    protected boolean isFinishing() {
        return null == mContext || ((Activity) mContext).isFinishing();
    }

    protected abstract void initView(View view);

    protected abstract int setLayoutResourceID();

    public View getmContentView() {
        return mContentView;
    }

    protected boolean isLogin() {
        return LoginEntity.getInstance().isLogin();
    }


    /*protected boolean isMineLogin() {
        return OMineEntity.getInstance().isMineLogin();
    }*/

    public void showToast(String msg) {
//        Looper.prepare();
//        Looper mlooper=Looper.myLooper();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//        mlooper.quit();
//        Looper.loop();
    }






    protected abstract void intPresenter();


    protected abstract void initData();


    public void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    /**
     * 展示Fragment
     */
    protected void showFragment(int resid, BaseFragment fragment, String key, Object object) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //隐藏正在暂时的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }

        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }

        Bundle bundle = new Bundle();
        // bundle.putString(key,value);
        bundle.putSerializable(key, (Serializable) object);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

    }


}
