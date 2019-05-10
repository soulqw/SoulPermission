package com.qw.soul.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.SparseArray;
import com.qw.soul.permission.exception.ContainerStatusException;
import com.qw.soul.permission.exception.InitException;

import java.lang.ref.WeakReference;


/**
 * @author cd5160866
 */
public class PermissionActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private SparseArray<WeakReference<Activity>> activities = new SparseArray<>();

    /**
     * 获取可用Activity
     *
     * @return Activity 优先栈顶
     * @throws InitException            初始化失败
     * @throws ContainerStatusException Activity状态异常
     */
    public Activity getActivity() {
        if (null == activities || activities.size() == 0) {
            throw new InitException();
        }
        for (int i = activities.size() - 1; i >= 0; i--) {
            WeakReference<Activity> activity = activities.valueAt(i);
            if (null != activity.get() && !activity.get().isFinishing()) {
                return activity.get();
            }
        }
        throw new ContainerStatusException();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.put(activity.hashCode(), new WeakReference<>(activity));
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
        activities.remove(activity.hashCode());
    }
}
