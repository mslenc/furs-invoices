package com.github.mslenc.furslib.dto;

public class FursException extends Exception {
    private final String errorCode;
    private final String errorMessage;

    public FursException(String errorCode, String errorMessage) {
        super(errorCode + " - " + errorMessage);

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
