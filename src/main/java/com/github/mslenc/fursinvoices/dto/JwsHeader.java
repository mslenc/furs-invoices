package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class JwsHeader {
    private String alg;
    private String subjectName;
    private String issuerName;
    private BigInteger serial;

    public JwsHeader() {

    }

    public JwsHeader(String alg, String subjectName, String issuerName, BigInteger serial) {
        setAlg(alg);
        setSubjectName(subjectName);
        setIssuerName(issuerName);
        setSerial(serial);
    }

    @JsonProperty("alg")
    public String getAlg() {
        return alg;
    }

    @JsonProperty("alg")
    public void setAlg(String alg) {
        this.alg = alg;
    }

    @JsonProperty("subject_name")
    public String getSubjectName() {
        return subjectName;
    }

    @JsonProperty("subject_name")
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @JsonProperty("issuer_name")
    public String getIssuerName() {
        return issuerName;
    }

    @JsonProperty("issuer_name")
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    @JsonProperty("serial")
    public BigInteger getSerial() {
        return serial;
    }

    @JsonProperty("serial")
    public void setSerial(BigInteger serial) {
        this.serial = serial;
    }
}
