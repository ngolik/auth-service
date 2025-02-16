package com.ngolik.authservice.service.exception;

public class CognitoConflictException extends RuntimeException {

    public CognitoConflictException(String message) {
        super(message);
    }
}
