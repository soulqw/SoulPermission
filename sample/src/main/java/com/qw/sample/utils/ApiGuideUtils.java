package com.qw.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.qw.soul.permission.callbcak.GoAppDetailCallBack;
import com.qw.soul.permission.callbcak.SpecialPermissionListener;

/**
 * @author cd5160866
 */
public class ApiGuideUtils {

    public static void checkSinglePermission(View view) {
        //you can also use checkPermissions() for a series of permissions
        Permission checkResult = SoulPermission.getInstance().checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION);
        Utils.showMessage(view, checkResult.toString());
    }

    public static void requestSinglePermission(final View view) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Utils.showMessage(view, permission.toString() + "\n is ok , you can do your operations");
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        Utils.showMessage(view, permission.toString() + " \n is refused you can not do next things");
                    }
                });
    }

    public static void requestPermissions(final View view) {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                //if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        Utils.showMessage(view, allPermissions.length + "permissions is ok" + " \n  you can do your operations");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        Utils.showMessage(view, refusedPermissions[0].toString() + " \n is refused you can not do next things");
                    }
                });
    }

    public static void requestSinglePermissionWithRationale(final View view) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS,
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Utils.showMessage(view, permission.toString() + "\n is ok , you can do your operations");
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        // see CheckPermissionWithRationaleAdapter
                        if (permission.shouldRationale()) {
                            Utils.showMessage(view, permission.toString() + " \n you should show a explain for user then retry ");
                        } else {
                            Utils.showMessage(view, permission.toString() + " \n is refused you can not do next things");
                        }
                    }
                });
    }

    public static void checkNotification(View view) {
        boolean checkResult = SoulPermission.getInstance().checkSpecialPermission(Special.NOTIFICATION);
        Utils.showMessage(view, checkResult ? "Notification is enable" :
                "Notification is disable \n you may invoke checkAndRequestPermission and enable notification");
    }

    public static void checkAndRequestNotification(final View view) {
        //if you want do noting or no need all the callbacks you may use SimpleSpecialPermissionAdapter instead
        SoulPermission.getInstance().checkAndRequestPermission(Special.NOTIFICATION, new SpecialPermissionListener() {
            @Override
            public void onGranted(Special permission) {
                Utils.showMessage(view, "Notification is enable now ");
            }

            @Override
            public void onDenied(Special permission) {
                Snackbar.make(view, "Notification is disable yet ", Snackbar.LENGTH_LONG)
                        .setAction("retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkAndRequestNotification(v);
                            }
                        }).show();
            }
        });
    }

    public static void checkAndRequestSystemAlert(final View view) {
        //if you want do noting or no need all the callbacks you may use SimpleSpecialPermissionAdapter instead
        SoulPermission.getInstance().checkAndRequestPermission(Special.SYSTEM_ALERT, new SpecialPermissionListener() {
            @Override
            public void onGranted(Special permission) {
                Utils.showMessage(view, "System Alert is enable now ");
            }

            @Override
            public void onDenied(Special permission) {
                Utils.showMessage(view, "System Alert is disable yet ");
            }
        });
    }

    public static void checkAndRequestUnKnownSource(final View view) {
        //if you want do noting or no need all the callbacks you may use SimpleSpecialPermissionAdapter instead
        SoulPermission.getInstance().checkAndRequestPermission(Special.UNKNOWN_APP_SOURCES, new SpecialPermissionListener() {
            @Override
            public void onGranted(Special permission) {
                Utils.showMessage(view, "install unKnown app  is enable now ");
            }

            @Override
            public void onDenied(Special permission) {
                Utils.showMessage(view, "install unKnown app  is disable yet");
            }
        });
    }

    public static void checkAndRequestWriteSystemSettings(final View view) {
        //if you want do noting or no need all the callbacks you may use SimpleSpecialPermissionAdapter instead
        SoulPermission.getInstance().checkAndRequestPermission(Special.WRITE_SETTINGS, new SpecialPermissionListener() {
            @Override
            public void onGranted(Special permission) {
                Utils.showMessage(view, "install unKnown app  is enable now ");
            }

            @Override
            public void onDenied(Special permission) {
                Utils.showMessage(view, "install unKnown app  is disable yet");
            }
        });
    }

    public static void goApplicationSettings(final View view) {
        SoulPermission.getInstance().goApplicationSettings(new GoAppDetailCallBack() {
            @Override
            public void onBackFromAppDetail(Intent data) {
                //if you need to know when back from app detail
                Utils.showMessage(view, "back from go appDetail");
            }
        });
    }

    public static void getTopActivity(View view) {
        Activity activity = SoulPermission.getInstance().getTopActivity();
        if (null != activity) {
            Utils.showMessage(view, activity.getClass().getSimpleName() + " " + activity.hashCode());
        }
    }
}
