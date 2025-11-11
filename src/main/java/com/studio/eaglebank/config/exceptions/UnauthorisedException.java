package com.studio.eaglebank.config.exceptions;

public class UnauthorisedException extends RuntimeException {

    public UnauthorisedException(String message) {
        super(message);
    }
}