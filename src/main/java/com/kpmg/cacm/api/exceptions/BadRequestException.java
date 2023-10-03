package com.kpmg.cacm.api.exceptions;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -6598699868119766268L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }
}
