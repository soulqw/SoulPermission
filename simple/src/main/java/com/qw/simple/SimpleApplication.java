package com.qw.simple;

import android.app.Application;
import com.qw.soul.permission.SoulPermission;

/**
 * @author cd5160866
 * @date 2019/4/19
 */
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SoulPermission.init(this);
    }
}
