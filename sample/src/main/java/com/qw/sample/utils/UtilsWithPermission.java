package com.qw.sample.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;
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
                    @SuppressLint("MissingPermission")
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
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionOk(Permission permission) {
                        activity.startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), requestCode);
                    }
                });
    }

    /**
     * 读取手机状态
     * 读取联系人权限和打电话是一组，只要一个授予即可无需重复请求
     */
    public static void readPhoneStatus() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_PHONE_STATE, new CheckPermissionAdapter() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionOk(Permission permission) {
                Context context = SoulPermission.getInstance().getContext();
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    return;
                }
                Toast.makeText(context, "phone " + tm.getLine1Number() + "\nime " + tm.getDeviceId() + "\nsimSerialNumber " + tm.getSimSerialNumber(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
