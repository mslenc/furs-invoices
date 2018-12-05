package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FursRequest {
    private String echoRequest;
    private InvoiceRequest invoiceRequest;
    private BusinessPremiseRequest businessPremiseRequest;

    public FursRequest() {

    }

    public FursRequest(String echoRequest) {
        setEchoRequest(echoRequest);
    }

    public FursRequest(InvoiceRequest invoiceRequest) {
        setInvoiceRequest(invoiceRequest);
    }

    public FursRequest(BusinessPremiseRequest businessPremiseRequest) {
        setBusinessPremiseRequest(businessPremiseRequest);
    }


    @JsonProperty("EchoRequest")
    public String getEchoRequest() {
        return echoRequest;
    }

    @JsonProperty("EchoRequest")
    public void setEchoRequest(String echoRequest) {
        this.echoRequest = echoRequest;
    }

    @JsonProperty("InvoiceRequest")
    public InvoiceRequest getInvoiceRequest() {
        return invoiceRequest;
    }

    @JsonProperty("InvoiceRequest")
    public void setInvoiceRequest(InvoiceRequest invoiceRequest) {
        this.invoiceRequest = invoiceRequest;
    }

    @JsonProperty("BusinessPremiseRequest")
    public BusinessPremiseRequest getBusinessPremiseRequest() {
        return businessPremiseRequest;
    }

    @JsonProperty("BusinessPremiseRequest")
    public void setBusinessPremiseRequest(BusinessPremiseRequest businessPremiseRequest) {
        this.businessPremiseRequest = businessPremiseRequest;
    }
}
