package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceRequest {
    private FursHeader header;
    private Invoice invoice;

    public InvoiceRequest() {
        this(new FursHeader(), null);
    }

    public InvoiceRequest(FursHeader header, Invoice invoice) {
        this.header = header;
        this.invoice = invoice;
    }

    @JsonProperty("Header")
    public FursHeader getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(FursHeader header) {
        this.header = header;
    }

    @JsonProperty("Invoice")
    public Invoice getInvoice() {
        return invoice;
    }

    @JsonProperty("Invoice")
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
