package com.qw.soul.permission.callbcak;


import com.qw.soul.permission.bean.Permission;

/**
 * @author cd5160866
 */
public interface CheckRequestPermissionsListener {

    /**
     * 所有权限ok，可做后续的事情
     *
     * @param allPermissions 权限实体类
     */
    void onAllPermissionOk(Permission[] allPermissions);

    /**
     * 不ok的权限，被拒绝或者未授予
     *
     * @param refusedPermissions 权限实体类
     */
    void onPermissionDenied(Permission[] refusedPermissions);
}
