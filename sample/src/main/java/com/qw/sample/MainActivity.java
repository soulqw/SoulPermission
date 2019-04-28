package com.qw.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    public void apiGuide(View view) {
        startActivity(new Intent(MainActivity.this, ApiGuideActivity.class));
    }
}
