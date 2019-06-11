package com.qw.soul.permission.request;

import android.annotation.TargetApi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.callbcak.GoAppDetailCallBack;
import com.qw.soul.permission.callbcak.RequestPermissionListener;
import com.qw.soul.permission.callbcak.SpecialPermissionListener;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public interface IPermissionActions {

    /**
     * 请求权限
     *
     * @param permissions 权限
     * @param listener    回调
     */
    @TargetApi(M)
    void requestPermissions(String[] permissions, RequestPermissionListener listener);


    /**
     * 请求特殊权限
     *
     * @param permission 特殊权限
     * @param listener   回调
     */
    void requestSpecialPermission(Special permission, SpecialPermissionListener listener);

    /**
     * 去应用详情页
     *
     * @param callBack 回调
     */
    void goAppDetail(@Nullable GoAppDetailCallBack callBack);

}
