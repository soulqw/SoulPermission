package com.qw.soul.permission;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import com.qw.soul.permission.debug.PermissionDebug;

import java.lang.reflect.Method;

/**
 * @author cd5160866
 */
class OldPermissionChecker {

    private static final String TAG = OldPermissionChecker.class.getSimpleName();

    private Context context;

    private String permission;

    OldPermissionChecker(Context context, String permission) {
        this.context = context;
        this.permission = permission;
    }

    /**
     * 老的通過反射方式檢查權限狀態
     * 结果可能不准确，如果返回false一定未授予
     * 按需在里面添加
     * <p>
     * 如果没匹配上或者异常都默认权限授予
     *
     * @return 检查结果
     */

    boolean check() {
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                return checkOp(context, 4);
            case Manifest.permission.CALL_PHONE:
                return checkOp(context, 13);
            case Manifest.permission.CAMERA:
                return checkOp(context, 26);
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return checkOp(context, 59);
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return checkOp(context, 60);
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return checkOp(context, 2);
            default:
                return true;
        }
    }


    private boolean checkOp(Context context, int op) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) {
            PermissionDebug.e(TAG, e.toString());
            e.printStackTrace();
        }
        return true;
    }

}
