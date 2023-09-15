package com.qw.soul.permission;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qw.soul.BuildConfig;
import com.qw.soul.permission.debug.PermissionDebug;

/**
 * @author cd5160866
 */
public class InitProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        boolean shouldAutoInit = BuildConfig.PERMISSION_AUTO_INIT;
        Log.d("qw","init "+shouldAutoInit);
        PermissionDebug.d(InitProvider.class.getSimpleName(), "auto init " + shouldAutoInit);
        if (shouldAutoInit) {
            SoulPermission.getInstance().autoInit((Application) getContext());
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
