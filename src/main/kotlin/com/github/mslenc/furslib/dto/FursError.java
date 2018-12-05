package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.FursException;

public class FursError {
    private String errorCode;
    private String errorMessage;

    @JsonProperty("ErrorCode")
    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("ErrorCode")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("ErrorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("ErrorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public FursException toException(String defaultErrorMessage) {
        return new FursException(
            getErrorCode() != null ? getErrorCode() : "????",
            getErrorMessage() != null ? getErrorMessage() : defaultErrorMessage
        );
    }
}
