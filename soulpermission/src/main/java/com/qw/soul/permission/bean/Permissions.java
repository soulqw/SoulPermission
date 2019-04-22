package com.qw.soul.permission.bean;

/**
 * 这个类只为了参数清晰..
 *
 * @author cd5160866
 */
public class Permissions {

    private Permission[] permissions;

    public static Permissions build(String... permissions) {
        return new Permissions(permissions);
    }

    public static Permissions build(Permission... permissions) {
        return new Permissions(permissions);
    }

    private Permissions() {
    }

    private Permissions(String[] permissions) {
        this.permissions = new Permission[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            this.permissions[i] = Permission.getDefault(permissions[i]);
        }
    }

    private Permissions(Permission[] permissions) {
        this.permissions = permissions;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    public String[] getPermissionsString() {
        String[] result = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            result[i] = permissions[i].permissionName;
        }
        return result;
    }
}
