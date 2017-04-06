package com.zhaobo.progressviews.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.zhaobo.progressviews.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by anzhuo002 on 2016/7/5.
 */

public abstract class BaseFragment extends Fragment {
    protected static final int PERMISSON_REQUESTCODE = 11;
    protected static final String[] CAMERA = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    protected static final String[] STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public BaseActivity mContext;
    public View mContentView = null;


    public boolean isVisible;                  //是否可见状态
    public boolean isPrepared;                 //标志位，View已经初始化完成。
    public boolean isFirstLoad = true;         //是否第一次加载

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(onSetLayoutId(), container, false);

        }
        isFirstLoad = true;
        mContext = getBaseActivity();
        isPrepared = true;
        lazyLoad();
        // 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent != null) {
            parent.removeView(mContentView);
        }
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑 presenter
    }

    /**
     * 创建相应的 presenter
     */

    public BaseActivity getBaseActivity() {
        return (BaseActivity) this.getActivity();
    }
    /**
     * 初始化 Api  更具需要初始化
     */
//    public void initApi() {
//        mCompositeSubscription  = mContext.getCompositeSubscription();
//        mApiWrapper = mContext.getApiWrapper();
//    }

    /**
     * 设置布局文件
     *
     * @return 返回布局文件资源Id
     */
    public abstract int onSetLayoutId();

    public abstract void fillData(boolean isVisible);

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {


        if (!isPrepared || !isFirstLoad) {
            return;
        }
        if (isVisible) {
            isFirstLoad = false;
        }

        fillData(isVisible);
    }


//    public void skipAct(Class clazz) {
//        mContext.skipAct(clazz);
//    }
//
//    public void skipAct(Class clazz, Bundle bundle) {
//        mContext.skipAct(clazz, bundle);
//    }
//
//    public void skipAct(Class clazz, Bundle bundle, int flags) {
//        mContext.skipAct(clazz, bundle, flags);
//    }

    /**
     * 判断一个字符串是否有值
     *
     * @param arg 需要判断的字符串
     * @return 有值返回true, 否则返回false;
     */
//    public boolean isNotEmpty(String arg) {
//        return mContext.isNotEmpty(arg);
//    }

    /**
     * 判断一个集合
     *
     * @param datas 需要判断的集合
     * @return 有值返回true, 否则返回false;
     */

    /**
     * 根据字符串的ID弹出吐司
     *
     * @param resId 字符串ID
     */
    public void toastShow(int resId) {
//        mContext.toastShow(resId);
    }






    /**
     * 检查权限是否存在，不存在申请
     *
     * @since 2.5.0
     */
    public void checkPermissions(String... permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> needRequestPermissonList = findDeniedPermissions(permissions);
            if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
                requestPermissions(needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),PERMISSON_REQUESTCODE);
            } else {
                permissionSuccess();
            }
        } else {
            permissionSuccess();
        }

    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {

            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED
                    ||shouldShowRequestPermissionRationale(perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {//onRequestPermissionsResult
//        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
//        LogUtils.i("onRequestPermissionsResult");
//        if (requestCode == PERMISSON_REQUESTCODE) {
//
//            if (!verifyPermissions(paramArrayOfInt)) {
//                permissionFail();
//                toastShow("权限授予失败");
//            } else {
//                permissionSuccess();
//
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Toast.makeText(getActivity(),"done base", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void permissionSuccess() {
    }

    public void permissionFail() {
    }

//    public void getContacts() {
//        int flag = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int flag1=ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (flag != PackageManager.PERMISSION_GRANTED||flag1!= PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        }
//    }

}
