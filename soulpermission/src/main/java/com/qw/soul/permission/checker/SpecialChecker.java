package com.qw.soul.permission.checker;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import com.qw.soul.permission.bean.Special;

import static android.os.Build.VERSION_CODES.N;

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
            default:
                return true;
        }

    }

    private boolean checkNotification() {
        try {
            if (Build.VERSION.SDK_INT >= N) {
                return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();
            }
            return new AppOpsChecker(context).checkOp(11);
        } catch (Exception e) {
            return true;
        }
    }
}
