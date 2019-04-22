package com.qw.soul.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.qw.soul.permission.callbcak.RequestPermissionListener;
import com.qw.soul.permission.debug.PermissionDebug;
import com.qw.soul.permission.exception.InitException;
import com.qw.soul.permission.request.PermissionRequester;

import java.util.LinkedList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class SoulPermission {

    private static final String TAG = SoulPermission.class.getSimpleName();

    private static SoulPermission instance;

    private PermissionActivityLifecycle lifecycle;

    private static Application globalContext;

    private static boolean alreadyInit;

    /**
     * 获取 SoulPermission 对象
     */
    public static synchronized SoulPermission getInstance() {
        if (null == instance) {
            instance = new SoulPermission();
        }
        return instance;
    }

    /**
     * init
     */
    public static void init(@NonNull Application application) {
        if (alreadyInit) {
            PermissionDebug.w(TAG, "already init");
            return;
        }
        globalContext = application;
        getInstance().registerLifecycle(globalContext);
        alreadyInit = true;
    }

    /**
     * 设置debug
     */
    public static void setDebug(boolean isDebug) {
        PermissionDebug.setDebug(isDebug);
    }

    /**
     * 检查权限
     *
     * @param permission 权限名称
     * @return 返回检查的结果
     * @see #checkPermissions
     */
    public Permission checkSinglePermission(@NonNull String permission) {
        return checkPermissions(permission)[0];
    }

    /**
     * 一次检查多项权限
     *
     * @param permissions 权限名称 ,可检测多个
     * @return 返回检查的结果
     */
    public Permission[] checkPermissions(@NonNull String... permissions) {
        List<Permission> resultPermissions = new LinkedList<>();
        for (String permission : permissions) {
            int isGranted = checkPermission(getContext(), permission)
                    ? PackageManager.PERMISSION_GRANTED
                    : PackageManager.PERMISSION_DENIED;
            resultPermissions.add(new Permission(permission, isGranted, false));
        }
        return PermissionTools.convert(resultPermissions);
    }

    /**
     * 单个权限的检查与申请
     * 在敏感操作前，先检查权限和请求权限，当完成操作后可做后续的事情
     *
     * @param permissionName 权限名称 例如：Manifest.permission.CALL_PHONE
     * @param listener       请求之后的回调
     * @see #checkAndRequestPermissions
     */
    public void checkAndRequestPermission(@NonNull final String permissionName, @NonNull final CheckRequestPermissionListener listener) {
        checkAndRequestPermissions(Permissions.build(permissionName), new CheckRequestPermissionsListener() {
            @Override
            public void onAllPermissionOk(Permission[] allPermissions) {
                listener.onPermissionOk(allPermissions[0]);
            }

            @Override
            public void onPermissionDenied(Permission[] refusedPermissions) {
                listener.onPermissionDenied(refusedPermissions[0]);
            }
        });
    }

    /**
     * 多个权限的检查与申请
     * 在敏感操作前，先检查权限和请求权限，当完成操作后可做后续的事情
     *
     * @param permissions 多个权限的申请  Permissions.build(Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA)
     * @param listener    请求之后的回调
     */
    public void checkAndRequestPermissions(@NonNull Permissions permissions, @NonNull final CheckRequestPermissionsListener listener) {
        //check permission first
        Permission[] checkResult = checkPermissions(permissions.getPermissionsString());
        //get refused permissions
        final Permission[] refusedPermissionList = filterRefusedPermissions(checkResult);
        if (refusedPermissionList.length > 0) {
            //can  request runTime permission
            if (canRequestRunTimePermission()) {
                requestPermissions(Permissions.build(refusedPermissionList), listener);
            } else {
                PermissionDebug.d(TAG, "some permission refused but can not request");
                listener.onPermissionDenied(refusedPermissionList);
            }
        } else {
            PermissionDebug.d(TAG, "all permissions ok");
            listener.onAllPermissionOk(checkResult);
        }
    }

    /**
     * 获得全局applicationContext
     */
    public Context getContext() {
        return globalContext;
    }

    /**
     * 提供当前栈顶Activity
     *
     * @return the top Activity in your app
     */
    @Nullable
    public Activity getTopActivity() {
        Activity result = null;
        try {
            result = getContainer();
        } catch (Exception e) {
            PermissionDebug.e(TAG, e.toString());
        }
        return result;
    }

    /**
     * 到系统权限设置页，已经适配部分手机系统，逐步更新
     */
    public void goPermissionSettings() {
        PermissionTools.jumpPermissionPage(getContext());
    }

    void autoInit(Application application) {
        if (null != globalContext) {
            PermissionDebug.w(TAG, "already init ,did not auto Init");
            return;
        }
        globalContext = application;
        registerLifecycle(globalContext);
    }

    private SoulPermission() {
    }

    private void registerLifecycle(Application context) {
        if (null != lifecycle) {
            context.unregisterActivityLifecycleCallbacks(lifecycle);
        }
        lifecycle = new PermissionActivityLifecycle();
        context.registerActivityLifecycleCallbacks(lifecycle);
    }

    /**
     * 获取栈顶Activity
     */
    private Activity getContainer() {
        //check status
        // may auto init failed
        if (null == lifecycle || null == lifecycle.topActWeakReference) {
            throw new InitException();
        }
        // activity status error
        if (null == lifecycle.topActWeakReference.get() || lifecycle.topActWeakReference.get().isFinishing()) {
            throw new IllegalStateException(" activity did not existence, check your app status before use soulPermission");
        }
        return lifecycle.topActWeakReference.get();
    }

    /**
     * 筛选出被拒绝的权限
     */
    private Permission[] filterRefusedPermissions(Permission[] in) {
        final List<Permission> out = new LinkedList<>();
        for (Permission permission : in) {
            boolean isPermissionOk = permission.isGranted();
            //若一项权限不ok,添加到列表中
            if (!isPermissionOk) {
                out.add(permission);
            }
        }
        PermissionDebug.d(TAG, "refusedPermissionList.size" + out.size());
        return PermissionTools.convert(out);
    }

    /**
     * 是否满足请求运行时权限的条件
     */
    private boolean canRequestRunTimePermission() {
        return !PermissionTools.isOldPermissionSystem(getContext());
    }

    private boolean checkPermission(Context context, String permission) {
        if (PermissionTools.isOldPermissionSystem(context)) {
            return checkPermissionOld(context, permission);
        } else {
            return checkPermissionHigher(context, permission);
        }
    }

    private boolean checkPermissionOld(Context context, String permission) {
        return new OldPermissionChecker(context, permission).check();
    }

    private void requestPermissions(Permissions permissions, final CheckRequestPermissionsListener listener) {
        //get activity
        Activity activity;
        try {
            activity = getContainer();
        } catch (Exception e) {
            //activity status error do not request
            PermissionDebug.e(TAG, e.toString());
            return;
        }
        requestRuntimePermission(activity, permissions.getPermissions(), listener);
    }

    private void requestRuntimePermission(final Activity activity, final Permission[] permissionsToRequest, final CheckRequestPermissionsListener listener) {
        if (!PermissionTools.checkMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    requestRuntimePermission(activity, permissionsToRequest, listener);
                }
            });
            return;
        }
        PermissionDebug.d(TAG, "start to request permissions size= " + permissionsToRequest.length);
        new PermissionRequester(activity)
                .withPermission(permissionsToRequest)
                .request(new RequestPermissionListener() {
                    @Override
                    public void onPermissionResult(Permission[] permissions) {
                        //这个列表用于展示运行时权限请求的结果，如果被拒，添加
                        List<Permission> refusedListAfterRequest = new LinkedList<>();
                        for (Permission requestResult : permissions) {
                            if (!requestResult.isGranted()) {
                                refusedListAfterRequest.add(requestResult);
                            }
                        }
                        //运行时权限授予全部授予成功
                        if (refusedListAfterRequest.size() == 0) {
                            PermissionDebug.d(TAG, "all permission are request ok");
                            listener.onAllPermissionOk(permissionsToRequest);
                        } else {
                            PermissionDebug.d(TAG, "some permission are refused size=" + refusedListAfterRequest.size());
                            listener.onPermissionDenied(PermissionTools.convert(refusedListAfterRequest));
                        }
                    }
                });
    }

    @TargetApi(M)
    private boolean checkPermissionHigher(Context context, String permission) {
        int checkResult = ContextCompat.checkSelfPermission(context, permission);
        return checkResult == PackageManager.PERMISSION_GRANTED;
    }

}
