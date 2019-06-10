package com.qw.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.qw.sample.fragment.TestFragment;

import java.util.Arrays;
import java.util.List;

public class WorkWithSupportFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);
        ViewPager viewPager = findViewById(R.id.vp);
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
    }

    class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private final static int TAB_COUNT = 3;

        private List<Integer> listData = Arrays.asList(Color.RED, Color.BLACK, Color.YELLOW);

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public TestFragment getItem(int position) {

            return TestFragment.get(listData.get(position));
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
