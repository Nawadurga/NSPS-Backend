package com.project.carparking.exception;

import java.io.Serial;

public class ResourceAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistException(String msg) {
        super(msg);
    }
}
