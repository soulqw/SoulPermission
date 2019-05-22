package com.qw.soul.permission.adapter;

import com.qw.soul.permission.bean.Special;
import com.qw.soul.permission.callbcak.SpecialPermissionListener;

/**
 * @author cd5160866
 */
public abstract class SimpleSpecialPermissionAdapter implements SpecialPermissionListener {

    @Override
    public void onDenied(Special permission) {

    }

    @Override
    public void onGranted(Special permission) {

    }
}
