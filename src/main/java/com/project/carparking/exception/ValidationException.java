package com.project.carparking.exception;

import java.io.Serial;

public class ValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ValidationException(String msg) {
        super(msg);
    }
}
