package com.qw.simple;

import android.app.Application;
import android.util.Log;
import com.qw.soul.permission.SoulPermission;

/**
 * @author cd5160866
 */
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SimpleApplication.class.getSimpleName(), "appInit");
        SoulPermission.setDebug(true);
        //no necessary
//        SoulPermission.init(this);
    }
}
