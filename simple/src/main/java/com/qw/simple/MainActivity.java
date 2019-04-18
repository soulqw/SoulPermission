package com.qw.simple;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS, new CheckRequestPermissionListener() {
            @Override
            public void onPermissionOk(Permission permission) {
                Toast.makeText(MainActivity.this, "請求成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(Permission permission) {
                Toast.makeText(MainActivity.this, "請求失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
