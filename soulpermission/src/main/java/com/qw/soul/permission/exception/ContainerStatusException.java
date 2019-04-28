package com.qw.soul.permission.exception;

/**
 * @author cd5160866
 */
public class ContainerStatusException extends IllegalStateException {

    public ContainerStatusException() {
        super(" activity did not existence, check your app status before use soulPermission");
    }
}
