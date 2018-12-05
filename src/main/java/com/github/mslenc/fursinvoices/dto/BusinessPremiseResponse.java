package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessPremiseResponse {
    private FursHeader header;
    private FursError error;

    @JsonProperty("Header")
    public FursHeader getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(FursHeader header) {
        this.header = header;
    }

    @JsonProperty("Error")
    public FursError getError() {
        return error;
    }

    @JsonProperty("Error")
    public void setError(FursError error) {
        this.error = error;
    }
}
