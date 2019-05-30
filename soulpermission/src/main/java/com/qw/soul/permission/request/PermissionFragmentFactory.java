package com.qw.soul.permission.request;

import android.app.Activity;
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
            FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            PermissionSupportFragment permissionSupportFragment = (PermissionSupportFragment) supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (null == permissionSupportFragment) {
                //makes other transactions execute immediately
                boolean hasOtherTask = supportFragmentManager.executePendingTransactions();
                PermissionDebug.d(TAG, " begin commit permissionSupportFragment \n begin with another transactions: " + hasOtherTask);
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
                //makes other transactions execute immediately
                boolean hasOtherTask = fragmentManager.executePendingTransactions();
                PermissionDebug.d(TAG, " begin commit permissionFragment \n begin with another transactions: " + hasOtherTask);
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
}
