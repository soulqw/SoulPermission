package com.qw.soul.permission.request;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.qw.soul.permission.debug.PermissionDebug;
import com.qw.soul.permission.request.fragment.PermissionFragment;
import com.qw.soul.permission.request.fragment.PermissionSupportFragment;

import java.lang.reflect.Field;
import java.util.List;


/**
 * @author cd5160866
 */
class PermissionFragmentFactory {

    private static final String TAG = PermissionFragmentFactory.class.getSimpleName();

    private static final String FRAGMENT_TAG = "permission_fragment_tag";

    static IPermissionActions create(Activity activity) {
        IPermissionActions action;
        if (activity instanceof FragmentActivity) {
            FragmentManager supportFragmentManager = getSupportFragmentManager((FragmentActivity) activity);
            PermissionSupportFragment permissionSupportFragment = (PermissionSupportFragment) supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (null == permissionSupportFragment) {
                permissionSupportFragment = new PermissionSupportFragment();
                supportFragmentManager.beginTransaction()
                        .add(permissionSupportFragment, FRAGMENT_TAG)
                        .commitNowAllowingStateLoss();
            }
            action = permissionSupportFragment;
        } else {
            android.app.FragmentManager fragmentManager = getFragmentManager(activity);
            PermissionFragment permissionFragment = (PermissionFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (null == permissionFragment) {
                permissionFragment = new PermissionFragment();
                fragmentManager.beginTransaction()
                        .add(permissionFragment, FRAGMENT_TAG)
                        .commitAllowingStateLoss();
                //make it commit like commitNow
                fragmentManager.executePendingTransactions();
            }
            action = permissionFragment;
        }
        return action;
    }

    private static FragmentManager getSupportFragmentManager(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        //some specific rom will provide a null List
        boolean childAvailable = null != fragmentManager.getFragments();
        if (childAvailable && fragmentManager.getFragments().size() > 0
                && null != fragmentManager.getFragments().get(0)) {
            return fragmentManager.getFragments().get(0).getChildFragmentManager();
        }
        return fragmentManager;
    }

    private static android.app.FragmentManager getFragmentManager(Activity activity) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null != fragmentManager.getFragments()
                    && fragmentManager.getFragments().size() > 0
                    && null != fragmentManager.getFragments().get(0)) {
                return fragmentManager.getFragments().get(0).getChildFragmentManager();
            }
        } else {
            try {
                Field fragmentsField = Class.forName("android.app.FragmentManagerImpl").getDeclaredField("mAdded");
                fragmentsField.setAccessible(true);
                List<Fragment> fragmentList = (List<Fragment>) fragmentsField.get(fragmentManager);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                        && null != fragmentList
                        && fragmentList.size() > 0
                        && null != fragmentList.get(0)) {
                    PermissionDebug.d(TAG, "reflect get child fragmentManager success");
                    return fragmentList.get(0).getChildFragmentManager();
                }
            } catch (Exception e) {
                PermissionDebug.w(TAG, "try to get childFragmentManager failed " + e.toString());
                e.printStackTrace();
            }
        }
        return fragmentManager;
    }
}
