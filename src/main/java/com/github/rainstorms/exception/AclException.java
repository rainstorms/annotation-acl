package com.github.rainstorms.exception;

import lombok.Getter;

public class AclException extends RuntimeException {
    @Getter private final int status;

    public AclException(String msg, int status) {
        super(msg);
        this.status = status;
    }
}
