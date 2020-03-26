package com.qw.soul.permission.checker;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.NotificationManagerCompat;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.debug.PermissionDebug;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.O;

/**
 * @author cd5160866
 */
public class SpecialChecker implements PermissionChecker {

    private static final String TAG = SpecialChecker.class.getSimpleName();

    private Context context;

    private Special permission;

    public SpecialChecker(Context context, Special permission) {
        this.context = context;
        this.permission = permission;
    }

    @Override
    public boolean check() {
        try {
            switch (permission) {
                case NOTIFICATION:
                    return checkNotification();
                case SYSTEM_ALERT:
                    return checkSystemAlert();
                case UNKNOWN_APP_SOURCES:
                    return checkUnknownSource();
                case WRITE_SETTINGS:
                    return checkWriteSystemSettings();
                default:
                    return true;
            }
        } catch (Exception e) {
            PermissionDebug.w(TAG, e.toString());
        }
        return true;
    }

    private boolean checkNotification() {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    private boolean checkSystemAlert() {
        if (Build.VERSION.SDK_INT >= M) {
            return Settings.canDrawOverlays(context);
        }
        return new AppOpsChecker(context).checkOp(24);
    }

    private boolean checkUnknownSource() {
        if (Build.VERSION.SDK_INT >= O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    private boolean checkWriteSystemSettings() {
        if (Build.VERSION.SDK_INT >= M) {
            return Settings.System.canWrite(context);
        }
        return true;
    }

}
