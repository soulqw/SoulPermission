package com.qw.soul.permission.exception;

import java.lang.IllegalStateException;


/**
 * @author cd5160866
 */
public class InitException extends IllegalStateException {

    public InitException() {
        super("auto init failed ,you need call SoulPermission.init() in your application");
    }
}
