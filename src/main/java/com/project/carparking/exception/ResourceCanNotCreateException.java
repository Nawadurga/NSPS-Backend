package com.project.carparking.exception;

import java.io.Serial;

public class ResourceCanNotCreateException extends  RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceCanNotCreateException(String msg) {
        super(msg);
    }
}
