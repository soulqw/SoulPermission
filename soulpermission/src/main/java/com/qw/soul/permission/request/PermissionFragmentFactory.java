package com.qw.soul.permission.request;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.qw.soul.permission.request.fragment.PermissionFragment;
import com.qw.soul.permission.request.fragment.PermissionSupportFragment;


/**
 * @author cd5160866
 */
class PermissionFragmentFactory {

    private static final String FRAGMENT_TAG = "permission_fragment_tag";

    static IPermissionActions create(Activity activity) {
        IPermissionActions action;
        if (activity instanceof FragmentActivity) {
            FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            PermissionSupportFragment permissionSupportFragment = (PermissionSupportFragment) supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            android.support.v4.app.FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            if (permissionSupportFragment != null) { // 如果已存在PermissionFragment的实例，先将它移除，然后再添加新的
                transaction.remove(permissionSupportFragment);
            }
            permissionSupportFragment = new PermissionSupportFragment();
            transaction.add(permissionSupportFragment, FRAGMENT_TAG);
            transaction.commitAllowingStateLoss();
            action = permissionSupportFragment;
        } else {
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            PermissionFragment permissionFragment = (PermissionFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (permissionFragment != null) { // 如果已存在PermissionFragment的实例，先将它移除，然后再添加新的
                transaction.remove(permissionFragment);
            }
            permissionFragment = new PermissionFragment();
            transaction.add(permissionFragment, FRAGMENT_TAG);
            transaction.commitAllowingStateLoss();
            action = permissionFragment;
        }
        return action;
    }
}
