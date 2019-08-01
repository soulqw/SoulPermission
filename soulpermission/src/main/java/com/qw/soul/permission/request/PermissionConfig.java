package com.qw.soul.permission.request;

/**
 * @author cd5160866
 */
public class PermissionConfig {

    /**
     * 是否跳过老的rom的权限检查 ，即老的rom默认直接返回ture 而不是appOpps反射检查后的结果
     */
    public static boolean skipOldRom = false;

}
