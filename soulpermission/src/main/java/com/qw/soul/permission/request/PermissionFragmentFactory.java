package com.qw.soul.permission.request;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.qw.soul.permission.debug.PermissionDebug;
import com.qw.soul.permission.request.fragment.PermissionFragment;
import com.qw.soul.permission.request.fragment.PermissionSupportFragment;


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
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
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
        if (fragmentManager.getFragments().size() > 0
                && null != fragmentManager.getFragments().get(0)) {
            return fragmentManager.getFragments().get(0).getChildFragmentManager();
        }
        return fragmentManager;
    }

//    private static android.app.FragmentManager getFragmentManager(Activity activity) {
//        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (fragmentManager.getFragments().size() > 0 && fragmentManager.getFragments().get(0) != null) {
//                return fragmentManager.getFragments().get(0).getChildFragmentManager();
//            }
//        } else {
//            //todo reflect
//        }
//        return fragmentManager;
//    }
}
