package com.qw.sample.guide.fragment;


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

public class PagerItemFragment extends Fragment {

    public static PagerItemFragment get(int color) {
        PagerItemFragment fragment = new PagerItemFragment();
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
            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.BODY_SENSORS, new SimplePermissionAdapter() {});
        }
        return view;
    }
}
