package com.navya.soutions.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String message;
    private final int code;

    public CustomException(final String message, final int code ) {
        super(message);
        this.message = message;
        this.code = code;
    }


    public CustomException(Throwable rootException, int value) {
        super(rootException);
        this.message = rootException.getLocalizedMessage();
        this.code = value;
    }
}
