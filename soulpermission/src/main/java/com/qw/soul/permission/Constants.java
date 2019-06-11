package com.qw.soul.permission;

/**
 * @author cd5160866
 */
public interface Constants {

    /**
     * request code for run time permission
     */
    int REQUEST_CODE_PERMISSION = 1 << 10;

    /**
     * request code for special permission
     */
    int REQUEST_CODE_PERMISSION_SPECIAL = 1 << 11;

    /**
     * request code for jump application settings
     */
    int REQUEST_CODE_APPLICATION_SETTINGS = 1 << 12;

    /**
     * default request code for jump application settings
     */
    @Deprecated
    int DEFAULT_CODE_APPLICATION_SETTINGS = 1 << 12;

}
