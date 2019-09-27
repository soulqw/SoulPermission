package com.qw.sample.guide.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.qw.sample.R;
import com.qw.sample.utils.ApiGuideUtils;

public class ApiGuideSupportFragment extends Fragment {

    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "use permission Based on SupportFragment", Toast.LENGTH_SHORT).show();
        root = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_api_guide, container, false);
        findViewById(R.id.checkSinglePermission)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkSinglePermission(v);
                    }
                });
        findViewById(R.id.requestSinglePermission)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.requestSinglePermission(v);
                    }
                });
        findViewById(R.id.requestPermissions)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.requestPermissions(v);
                    }
                });
        findViewById(R.id.requestSinglePermissionWithRationale)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.requestSinglePermissionWithRationale(v);
                    }
                });
        findViewById(R.id.checkNotification)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkNotification(v);
                    }
                });
        findViewById(R.id.checkAndRequestNotification)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkAndRequestNotification(v);
                    }
                });
        findViewById(R.id.checkAndRequestSystemAlert)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkAndRequestSystemAlert(v);
                    }
                });
        findViewById(R.id.checkAndRequestUnKnownSource)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkAndRequestUnKnownSource(v);
                    }
                });
        findViewById(R.id.checkAndRequestWriteSystemSettings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.checkAndRequestWriteSystemSettings(v);
                    }
                });
        findViewById(R.id.goApplicationSettings)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.goApplicationSettings(v);
                    }
                });
        findViewById(R.id.getTopActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiGuideUtils.getTopActivity(v);
                    }
                });

        return root;
    }

    private <T extends View> T findViewById(int id) {
        return root.findViewById(id);
    }

}
