package com.qw.sample.guide.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


/**
 * @author cd5160866
 * @date 2019-06-11
 */
public class ContainerActivity extends AppCompatActivity {

    public static void start(Activity activity, boolean isSupport) {
        Intent intent = new Intent(activity, ContainerActivity.class);
        intent.putExtra("isSupport", isSupport);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contentViewId = 1000;
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(contentViewId);
        setContentView(frameLayout);
        if (getIntent().getExtras() == null) {
            return;
        }
        boolean isSupport = getIntent().getExtras().getBoolean("isSupport", false);
        if (isSupport) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(contentViewId, new ApiGuideSupportFragment())
                    .commitNowAllowingStateLoss();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(contentViewId, new ApiGuideFragment())
                    .commitAllowingStateLoss();
        }
    }
}
