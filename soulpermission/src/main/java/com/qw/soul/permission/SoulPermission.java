package com.qw.soul.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.qw.soul.permission.callbcak.RequestPermissionListener;
import com.qw.soul.permission.debug.PermissionDebug;
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
        return checkPermissions(permission).get(0);
    }

    /**
     * 一次检查多项权限
     *
     * @param permissions 权限名称 ,可检测多个
     * @return 返回检查的结果
     */
    public List<Permission> checkPermissions(@NonNull String... permissions) {
        Activity activity = getContainer();
        List<Permission> resultPermissions = new LinkedList<>();
        for (String permission : permissions) {
            int isGranted = checkPermission(activity, permission)
                    ? PackageManager.PERMISSION_GRANTED
                    : PackageManager.PERMISSION_DENIED;
            resultPermissions.add(new Permission(permission, isGranted, false));
        }
        return resultPermissions;
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
        //get activity
        Activity activity = getContainer();
        //首先检查权限
        List<Permission> checkResult = checkPermissions(permissions.getPermissions());
        //选出被拒绝的权限
        final List<Permission> refusedPermissionList = filterRefusedPermissions(checkResult);
        //大于0表示有被拒绝的权限
        if (refusedPermissionList.size() > 0) {
            //满足请求运行时权限的条件
            if (canRequestRunTimePermission()) {
                requestRuntimePermission(activity, refusedPermissionList, listener);
            } else {
                PermissionDebug.d(TAG, "some permission refused but can not request");
                listener.onPermissionDenied(Tools.convert(refusedPermissionList));
            }
        } else {
            PermissionDebug.d(TAG, "all permissions ok");
            listener.onAllPermissionOk(Tools.convert(checkResult));
        }
    }

    public Context getContext() {
        return globalContext;
    }

    void init(@NonNull Application application) {
        if (null != globalContext) {
            PermissionDebug.w(TAG, "already init");
            return;
        }
        globalContext = application;
        getInstance().registerLifecycle(globalContext);
    }

    private SoulPermission() {
    }

    private void registerLifecycle(Application context) {
        lifecycle = new PermissionActivityLifecycle();
        context.registerActivityLifecycleCallbacks(lifecycle);
    }

    /**
     * 获取栈顶Activity
     */
    private Activity getContainer() {
        //check status
        if (null == lifecycle || lifecycle.topActWeakReference.get() == null) {
            PermissionDebug.e(TAG, "status error");
            throw new IllegalStateException("lifecycle or activity did not existence, check your app status before use soulPermission");
        }
        return lifecycle.topActWeakReference.get();
    }

    /**
     * 筛选出被拒绝的权限
     */
    private List<Permission> filterRefusedPermissions(List<Permission> in) {
        final List<Permission> out = new LinkedList<>();
        for (Permission permission : in) {
            boolean isPermissionOk = permission.isGranted();
            //若一项权限不ok,添加到列表中
            if (!isPermissionOk) {
                out.add(permission);
            }
        }
        PermissionDebug.d(TAG, "refusedPermissionList.size" + out.size());
        return out;
    }

    /**
     * 是否满足请求运行时权限的条件
     */
    private boolean canRequestRunTimePermission() {
        Context context = getContainer();
        return !Tools.isOldPermissionSystem(context);
    }

    private boolean checkPermission(Context context, String permission) {
        if (Tools.isOldPermissionSystem(context)) {
            return checkPermissionOld(context, permission);
        } else {
            return checkPermissionHigher(context, permission);
        }
    }

    private boolean checkPermissionOld(Context context, String permission) {
        return new OldPermissionChecker(context, permission).check();
    }

    private void requestRuntimePermission(final Activity activity, final List<Permission> permissionsToRequest, final CheckRequestPermissionsListener listener) {
        if (!Tools.checkMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    requestRuntimePermission(activity, permissionsToRequest, listener);
                }
            });
            return;
        }
        PermissionDebug.d(TAG, "start to request permissions size= " + permissionsToRequest.size());
        new PermissionRequester(activity)
                .withPermission(Tools.convert(permissionsToRequest))
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
                            listener.onAllPermissionOk(Tools.convert(permissionsToRequest));
                        } else {
                            PermissionDebug.d(TAG, "some permission are refused size=" + refusedListAfterRequest.size());
                            listener.onPermissionDenied(Tools.convert(refusedListAfterRequest));
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
