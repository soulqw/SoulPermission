package com.qw.simple.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

/**
 * @author cd5160866
 */
public abstract class CheckPermissionAdapter implements CheckRequestPermissionListener {

    @Override
    public void onPermissionDenied(Permission permission) {
        //SoulPermission提供栈顶Activity
        Activity activity = SoulPermission.getInstance().getTopActivity();
        if (null == activity) {
            return;
        }
        String permissionDesc = permission.getPermissionNameDesc();
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //去设置页
                        SoulPermission.getInstance().goPermissionSettings();
                    }
                }).create().show();
    }
}
