package com.ripplereach.ripplereach.exceptions;

public class RippleReachException extends RuntimeException {
    public RippleReachException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RippleReachException(String exMessage) {
        super(exMessage);
    }
}
