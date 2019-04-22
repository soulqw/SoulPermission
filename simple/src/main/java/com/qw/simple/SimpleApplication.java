package com.qw.simple;

import android.app.Application;
import android.util.Log;

/**
 * @author cd5160866
 */
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SimpleApplication.class.getSimpleName(), "appInit");
        //no necessary
//        SoulPermission.init(this);
    }
}
