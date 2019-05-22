package com.qw.soul.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.qw.soul.permission.debug.PermissionDebug;
import com.qw.soul.permission.exception.ContainerStatusException;
import com.qw.soul.permission.exception.InitException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author cd5160866
 */
public class PermissionActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = PermissionActivityLifecycle.class.getSimpleName();

    private List<Activity> activities = new ArrayList<>();

    /**
     * 获取可用Activity
     *
     * @return Activity 优先栈顶
     * @throws InitException            初始化失败
     * @throws ContainerStatusException Activity状态异常
     */
    Activity getActivity() {
        if (null == activities || activities.size() == 0) {
            throw new InitException();
        }
        PermissionDebug.d(TAG, "current activity stack:" + activities.toString());
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (PermissionTools.isActivityAvailable(activity)) {
                PermissionDebug.d(TAG, "top available activity is :" + activity.getClass().getSimpleName());
                return activity;
            }
        }
        throw new ContainerStatusException();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
        PermissionDebug.d(TAG, "stack added:" + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
        PermissionDebug.d(TAG, "stack removed:" + activity.getClass().getSimpleName());
    }
}
