package com.qw.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import com.qw.sample.adapter.CheckPermissionAdapter;
import com.qw.sample.adapter.CheckPermissionWithRationaleAdapter;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;

/**
 * @author cd5160866
 */
public class UtilsWithPermission {

    /**
     * 拨打指定电话
     */
    public static void makeCall(final Context context, final String phoneNumber) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
                new CheckPermissionWithRationaleAdapter("如果你拒绝了权限，你将无法拨打电话，请点击授予权限",
                        new Runnable() {
                            @Override
                            public void run() {
                                //retry
                                makeCall(context, phoneNumber);
                            }
                        }) {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phoneNumber);
                        intent.setData(data);
                        if (!(context instanceof Activity)) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                    }
                });
    }

    /**
     * 选择联系人
     */
    public static void chooseContact(final Activity activity, final int requestCode) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS,
                new CheckPermissionAdapter() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        activity.startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), requestCode);
                    }
                });
    }
}
