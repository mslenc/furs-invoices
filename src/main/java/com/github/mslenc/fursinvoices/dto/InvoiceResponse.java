package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class InvoiceResponse {
    private FursHeader header;
    private UUID uniqueInvoiceId;
    private FursError error;

    @JsonProperty("Header")
    public FursHeader getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(FursHeader header) {
        this.header = header;
    }

    @JsonProperty("UniqueInvoiceID")
    public UUID getUniqueInvoiceId() {
        return uniqueInvoiceId;
    }

    @JsonProperty("UniqueInvoiceID")
    public void setUniqueInvoiceId(UUID uniqueInvoiceId) {
        this.uniqueInvoiceId = uniqueInvoiceId;
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
