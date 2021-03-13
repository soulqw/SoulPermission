package com.qw.soul.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;

import android.widget.Toast;

import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.debug.PermissionDebug;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class PermissionTools {

    private static final String TAG = PermissionTools.class.getSimpleName();

    public static boolean isOldPermissionSystem(Context context) {
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        return android.os.Build.VERSION.SDK_INT < M || targetSdkVersion < M;
    }

    /**
     * 判断Activity 是否可用
     *
     * @param activity 目标Activity
     * @return true of false
     */
    public static boolean isActivityAvailable(Activity activity) {
        if (null == activity) {
            return false;
        }
        if (activity.isFinishing()) {
            PermissionDebug.d(TAG, " activity is finishing :" + activity.getClass().getSimpleName());
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            PermissionDebug.d(TAG, " activity is destroyed :" + activity.getClass().getSimpleName());
            return false;
        }
        return true;
    }

    @CheckResult
    @Nullable
    public static Intent getSpecialPermissionIntent(Context context, Special specialPermission) {
        Intent intent;
        switch (specialPermission) {
            case SYSTEM_ALERT:
                intent = getDrawOverPermissionIntent(context);
                break;
            case UNKNOWN_APP_SOURCES:
                intent = getInstallPermissionIntent(context);
                break;
            case WRITE_SETTINGS:
                intent = getWriteSettingsIntent(context);
                break;
            case NOTIFICATION:
                intent = getNotificationIntent(context);
                break;
            default:
                intent = getAppManageIntent(context);
                break;
        }
        return intent;
    }

    public static Intent getAppManageIntent(Context context) {
        Intent intent;
        try {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
        } catch (Exception e) {
            intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
        }
        return intent;
    }

    static Intent getNotificationIntent(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            return intent;
        }
        return getAppManageIntent(context);
    }

    static Permission[] convert(List<Permission> permissions) {
        return permissions.toArray(new Permission[0]);
    }

    static boolean assertMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    static void toast(Context context, String message) {
        if (null == context) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static void jumpAppDetail(Activity activity, int requestCode) {
        if (!isActivityAvailable(activity)) {
            PermissionDebug.e(TAG, "activity status error");
            return;
        }
        Intent appDetailIntent = getAppManageIntent(activity);
        if (null == appDetailIntent) {
            PermissionDebug.e(TAG, "get system intent failed");
        }
        activity.startActivityForResult(appDetailIntent, requestCode);
    }

    private static Intent getInstallPermissionIntent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, getPackageUri(context));
        }
        return null;
    }

    private static Intent getDrawOverPermissionIntent(Context context) {
        //system support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, getPackageUri(context));
        }
        //  check by appOps, so go to Application settings
        return getAppManageIntent(context);
    }

    private static Intent getWriteSettingsIntent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, getPackageUri(context));
        }
        return getAppManageIntent(context);
    }

    private static Uri getPackageUri(Context context) {
        return Uri.parse("package:" + context.getPackageName());
    }

}
