package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.fursinvoices.validation.StringValidator;
import com.github.mslenc.fursinvoices.validation.TaxNumberValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.mslenc.fursinvoices.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.fursinvoices.validation.StringValidator.CharsAllowed.ASCII_ALNUM;
import static com.github.mslenc.fursinvoices.validation.StringValidator.NullEmptyMode.NO_NULLS;
import static com.github.mslenc.fursinvoices.validation.StringValidator.NullEmptyMode.NULL_ALLOWED;

public class BusinessPremise {
    private Integer taxNumber;
    private String businessPremiseId;
    private LocalDate validityDate;
    private String closingTag;
    private String specialNotes;
    private ArrayList<SoftwareSupplier> softwareSuppliers = new ArrayList<>();
    private BPIdentifier bpIdentifier;


    @JsonProperty("TaxNumber")
    public Integer getTaxNumber() {
        return taxNumber;
    }

    @JsonProperty("TaxNumber")
    public BusinessPremise setTaxNumber(Integer taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    @JsonIgnore
    public BusinessPremise setTaxNumber(int taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    @JsonIgnore
    public BusinessPremise setTaxNumber(String taxNumber) {
        this.taxNumber = TAX_NUMBER.validateAndConvert(taxNumber);
        return this;
    }


    @JsonProperty("BusinessPremiseID")
    public String getBusinessPremiseId() {
        return businessPremiseId;
    }

    @JsonProperty("BusinessPremiseID")
    public BusinessPremise setBusinessPremiseId(String businessPremiseId) {
        this.businessPremiseId = BUSINESS_PREMISE_ID.validate(businessPremiseId);
        return this;
    }


    /**
     * Adds a single SoftwareSupplier entry.
     * @see #setSoftwareSuppliers(List)
     */
    @JsonIgnore
    public BusinessPremise addSoftwareSupplier(SoftwareSupplier softwareSupplier) {
        if (softwareSupplier == null)
            throw new IllegalArgumentException("null softwareSupplier");

        softwareSuppliers.add(softwareSupplier);

        return this;
    }

    /**
     * TODO
     */
    @JsonProperty("SoftwareSupplier")
    public BusinessPremise setSoftwareSuppliers(List<SoftwareSupplier> softwareSuppliers) {
        if (softwareSuppliers == null)
            throw new IllegalArgumentException("null software supplier list");

        for (SoftwareSupplier element : softwareSuppliers)
            if (element == null)
                throw new IllegalArgumentException("null software supplier element");

        this.softwareSuppliers.clear();
        this.softwareSuppliers.addAll(softwareSuppliers);

        return this;
    }

    /**
     * @see #setSoftwareSuppliers(List)
     */
    @JsonIgnore
    public BusinessPremise setSoftwareSuppliers(SoftwareSupplier... softwareSuppliers) {
        return setSoftwareSuppliers(Arrays.asList(softwareSuppliers));
    }

    /**
     * @see #setSoftwareSuppliers(List)
     */
    @JsonProperty("SoftwareSupplier")
    public List<SoftwareSupplier> getSoftwareSuppliers() {
        return new ArrayList<>(softwareSuppliers);
    }



    @JsonIgnore
    public BusinessPremise irreversiblyMarkBusinessPremiseAsClosedForever() {
        this.closingTag = "Z";
        return this;
    }

    @JsonProperty("ClosingTag")
    public String getClosingTag() {
        return closingTag;
    }


    /**
     * @see #setValidityDate(LocalDate)
     */
    @JsonProperty("ValidityDate")
    public LocalDate getValidityDate() {
        return validityDate;
    }

    /** TODO */
    @JsonProperty("ValidityDate")
    public BusinessPremise setValidityDate(LocalDate validityDate) {
        if (validityDate == null)
            throw new IllegalArgumentException("null validityDate");

        this.validityDate = validityDate;
        return this;
    }


    /**
     * @see #setSpecialNotes(String)
     */
    @JsonProperty("SpecialNotes")
    public String getSpecialNotes() {
        return specialNotes;
    }

    /**
     * TODO
     */
    @JsonProperty("SpecialNotes")
    public BusinessPremise setSpecialNotes(String specialNotes) {
        this.specialNotes = SPECIAL_NOTES.validate(specialNotes);
        return this;
    }


    @JsonProperty("BPIdentifier")
    public BPIdentifier getBpIdentifier() {
        return bpIdentifier;
    }

    @JsonProperty("BPIdentifier")
    public BusinessPremise setBpIdentifier(BPIdentifier bpIdentifier) {
        this.bpIdentifier = bpIdentifier;
        return this;
    }

    private static final
    TaxNumberValidator TAX_NUMBER = new TaxNumberValidator("taxNumber", false);

    private static final
    StringValidator BUSINESS_PREMISE_ID = new StringValidator("businessPremiseId", 1, 20, ASCII_ALNUM, NO_NULLS);

    private static final
    StringValidator SPECIAL_NOTES = new StringValidator("SpecialNotes", 1, 1000, ANY, NULL_ALLOWED);


}
