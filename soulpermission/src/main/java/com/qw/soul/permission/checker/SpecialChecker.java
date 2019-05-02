package com.qw.soul.permission.checker;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import com.qw.soul.permission.bean.Special;

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
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
