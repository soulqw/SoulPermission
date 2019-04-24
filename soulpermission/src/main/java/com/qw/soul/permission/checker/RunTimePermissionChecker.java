package com.qw.soul.permission.checker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
class RunTimePermissionChecker implements PermissionChecker {

    private String permission;

    private Context context;

    RunTimePermissionChecker(Context context, String permission) {
        this.permission = permission;
        this.context = context;
    }

    @TargetApi(M)
    @Override
    public boolean check() {
        int checkResult = ContextCompat.checkSelfPermission(context, permission);
        return checkResult == PackageManager.PERMISSION_GRANTED;
    }

}
