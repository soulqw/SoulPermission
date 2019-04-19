package com.qw.soul.permission.bean;

/**
 * 这个类只为了参数清晰..
 *
 * @author cd5160866
 */
public class Permissions {

    public static Permissions build(String... permissions) {
        return new Permissions(permissions);
    }

    private Permissions() {
    }

    private Permissions(String[] permissions) {
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    private String[] permissions;

}
