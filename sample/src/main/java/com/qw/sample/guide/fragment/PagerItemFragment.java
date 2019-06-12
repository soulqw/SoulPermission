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
import android.widget.Toast;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.adapter.SimplePermissionAdapter;
import com.qw.soul.permission.bean.Permission;

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
            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.BODY_SENSORS, new SimplePermissionAdapter() {
                @Override
                public void onPermissionOk(Permission permission) {
                    Toast.makeText(container.getContext(), "sensor permission ok", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionDenied(Permission permission) {
                    Toast.makeText(container.getContext(), "sensor permission denied", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }
}
