package com.qw.soul.permission.checker;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import com.qw.soul.permission.bean.Special;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class SpecialChecker implements PermissionChecker {

    private Context context;

    private Special permission;

    SpecialChecker(Context context, Special permission) {
        this.context = context;
        this.permission = permission;
    }

    @Override
    public boolean check() {
        switch (permission) {
            case NOTIFICATION:
                return checkNotification();
            case SYSTEM_ALERT:
                return checkSystemAlert();
            default:
                return true;
        }
    }

    private boolean checkNotification() {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    private boolean checkSystemAlert(){
        if (Build.VERSION.SDK_INT >= M) {
            return  Settings.canDrawOverlays(context);
        }
        return new AppOpsChecker(context).checkOp(24);
    }
}
