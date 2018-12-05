package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessPremiseRequest {
    private FursHeader header;
    private BusinessPremise businessPremise;

    public BusinessPremiseRequest() {
        this(new FursHeader(), null);
    }

    public BusinessPremiseRequest(BusinessPremise businessPremise) {
        this(new FursHeader(), businessPremise);
    }

    public BusinessPremiseRequest(FursHeader header, BusinessPremise businessPremise) {
        this.header = header;
        this.businessPremise = businessPremise;
    }

    @JsonProperty("Header")
    public FursHeader getHeader() {
        return header;
    }

    @JsonProperty("Header")
    public void setHeader(FursHeader header) {
        this.header = header;
    }

    @JsonProperty("BusinessPremise")
    public BusinessPremise getBusinessPremise() {
        return businessPremise;
    }

    @JsonProperty("BusinessPremise")
    public void setBusinessPremise(BusinessPremise businessPremise) {
        this.businessPremise = businessPremise;
    }
}
