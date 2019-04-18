package com.qw.soul.permission;

import android.content.Context;
import android.os.Looper;

import com.qw.soul.permission.bean.Permission;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class Tools {

    public static Permission[] convert(List<Permission> permissions) {
        return permissions.toArray(new Permission[0]);
    }

    static boolean isOldPermissionSystem(Context context) {
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        return android.os.Build.VERSION.SDK_INT < M || targetSdkVersion < M;
    }

    static boolean checkMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

}
