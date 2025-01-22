package com.ilker.task_service.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String s) {
        super(s);
    }
}
