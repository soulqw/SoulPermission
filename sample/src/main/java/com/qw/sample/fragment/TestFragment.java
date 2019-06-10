package com.qw.sample.fragment;


import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.adapter.SimplePermissionAdapter;

public class TestFragment extends Fragment {

    public static TestFragment get(int color) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Color", color);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = new View(container.getContext());
        int color = getArguments().getInt("Color");
        view.setBackgroundColor(color);
        if (color == Color.RED) {
            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, new SimplePermissionAdapter() {
            });
        }
        return view;
    }
}
