package com.kpmg.cacm.api.exceptions;

public class NotAuthorizedException extends RuntimeException{

    private static final long serialVersionUID = -4970268883016356824L;

    public NotAuthorizedException() {
        super();
    }

    public NotAuthorizedException(final String message) {
        super(message);
    }
}
