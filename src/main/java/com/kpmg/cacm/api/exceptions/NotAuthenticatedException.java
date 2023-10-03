package com.kpmg.cacm.api.exceptions;

public class NotAuthenticatedException extends RuntimeException {

    private static final long serialVersionUID = 1582211826712533012L;

    public NotAuthenticatedException() {
        super();
    }

    public NotAuthenticatedException(final String message) {
        super(message);
    }
}
