package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FursResponse {
    private String echoResponse;
    private InvoiceResponse invoiceResponse;
    private BusinessPremiseResponse businessPremiseResponse;

    @JsonProperty("EchoResponse")
    public String getEchoResponse() {
        return echoResponse;
    }

    @JsonProperty("EchoResponse")
    public void setEchoResponse(String echoResponse) {
        this.echoResponse = echoResponse;
    }

    @JsonProperty("InvoiceResponse")
    public InvoiceResponse getInvoiceResponse() {
        return invoiceResponse;
    }

    @JsonProperty("InvoiceResponse")
    public void setInvoiceResponse(InvoiceResponse invoiceResponse) {
        this.invoiceResponse = invoiceResponse;
    }

    @JsonProperty("BusinessPremiseResponse")
    public BusinessPremiseResponse getBusinessPremiseResponse() {
        return businessPremiseResponse;
    }

    @JsonProperty("BusinessPremiseResponse")
    public void setBusinessPremiseResponse(BusinessPremiseResponse businessPremiseResponse) {
        this.businessPremiseResponse = businessPremiseResponse;
    }
}
