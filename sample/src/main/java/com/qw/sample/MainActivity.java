package com.qw.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.qw.sample.guide.ApiGuideActivity;
import com.qw.sample.guide.ApiGuideAppComponentActivity;
import com.qw.sample.guide.WithPagerFragmentActivity;
import com.qw.sample.guide.fragment.ContainerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void before(View view) {
        startActivity(new Intent(MainActivity.this, BeforeActivity.class));
    }

    public void after(View view) {
        startActivity(new Intent(MainActivity.this, AfterActivity.class));
    }

    public void apiGuideActivity(View view) {
        startActivity(new Intent(MainActivity.this, ApiGuideActivity.class));
    }

    public void apiGuideAppComponentActivity(View view) {
        startActivity(new Intent(MainActivity.this, ApiGuideAppComponentActivity.class));
    }

    public void fragment(View view) {
        ContainerActivity.start(this, false);
    }

    public void supportFragment(View view) {
        ContainerActivity.start(this, true);
    }

    public void fragmentWithViewPager(View view) {
        startActivity(new Intent(MainActivity.this, WithPagerFragmentActivity.class));
    }

}
