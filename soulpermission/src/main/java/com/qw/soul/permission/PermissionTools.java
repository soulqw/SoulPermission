package com.qw.soul.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.qw.soul.R;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.debug.PermissionDebug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            case NOTIFICATION:
            default:
                intent = getAppManageIntent(context);
                break;
        }
        return intent;
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

    static void jumpPermissionPage(Context context) {
        String name = Build.MANUFACTURER;
        switch (name) {
            case "HUAWEI":
                goHuaWeiManager(context);
                break;
            case "vivo":
                goVivoManager(context);
                break;
            case "OPPO":
                goOppoManager(context);
                break;
            case "Coolpad":
                goCoolpadManager(context);
                break;
            case "Meizu":
                goMeizuManager(context);
                break;
            case "Xiaomi":
                goXiaoMiManager(context);
                break;
            case "samsung":
                goSamsungManager(context);
                break;
            case "Sony":
                goSonyManager(context);
                break;
            case "LG":
                goLGManager(context);
                break;
            default:
                goOtherIntentSetting(context);
                break;
        }
    }

    private static void goLGManager(Context context) {
        try {
            Intent intent = new Intent(context.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            goOtherIntentSetting(context);
        }
    }

    private static void goSonyManager(Context context) {
        try {
            Intent intent = new Intent(context.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            goOtherIntentSetting(context);
        }
    }

    private static void goHuaWeiManager(Context context) {
        try {
            Intent intent = new Intent(context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            goOtherIntentSetting(context);
        }
    }

    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private static void goXiaoMiManager(Context context) {
        try {
            String rom = getMiuiVersion();
            Intent intent = new Intent();
            if ("V6".equals(rom) || "V7".equals(rom)) {
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
            } else if ("V8".equals(rom) || "V9".equals(rom)) {
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
            } else {
                goOtherIntentSetting(context);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            goOtherIntentSetting(context);
        }
    }

    private static void goMeizuManager(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            goOtherIntentSetting(context);
        }
    }

    private static void goSamsungManager(Context context) {
        goOtherIntentSetting(context);
    }

    private static void goOtherIntentSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            goHomeSetting(context);
        }
    }

    private static void goHomeSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.permission_jump_failed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static void goOppoManager(Context context) {
        doStartApplicationWithPackageName(context, "com.coloros.safecenter");
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     */
    private static void goCoolpadManager(Context context) {
        doStartApplicationWithPackageName(context, "com.yulong.android.security:remote");
    }

    private static void goVivoManager(Context context) {
        doStartApplicationWithPackageName(context, "com.bairenkeji.icaller");
    }

    private static void doStartApplicationWithPackageName(Context context, String packagename) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            goOtherIntentSetting(context);
        }
        if (packageinfo == null) {
            goOtherIntentSetting(context);
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e("PermissionPageManager", resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        if (resolveinfoList.iterator().hasNext()) {
            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null) {
                String packageName = resolveinfo.activityInfo.packageName;
                String className = resolveinfo.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    goOtherIntentSetting(context);
                }
            }
        } else {
            goOtherIntentSetting(context);
        }
    }

    private static Intent getAppManageIntent(Context context) {
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

    private static Intent getInstallPermissionIntent(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        }
        return null;
    }

    private static Intent getDrawOverPermissionIntent(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        //system support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI);
        }
        //  check by appOps, so go to Application settings
        return getAppManageIntent(context);
    }
}
