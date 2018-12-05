package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.validation.StringValidator;
import com.github.mslenc.furslib.validation.TaxNumberValidator;

import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.furslib.validation.StringValidator.NullEmptyMode.NULL_ALLOWED;

public class SoftwareSupplier {
    private Integer taxNumber;
    private String nameForeign;

    public SoftwareSupplier() {

    }

    // no other constructors, because we support string tax numbers
    // and it would be confusing as to what is intended.. setters
    // return this anyway.

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonProperty("TaxNumber")
    public Integer getTaxNumber() {
        return taxNumber;
    }

    /** TODO */
    @JsonProperty("TaxNumber")
    public SoftwareSupplier setTaxNumber(Integer taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonIgnore
    public SoftwareSupplier setTaxNumber(int taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonIgnore
    public SoftwareSupplier setTaxNumber(String taxNumber) {
        this.taxNumber = TAX_NUMBER.validateAndConvert(taxNumber);
        return this;
    }


    /**
     * @see #setNameForeign(String)
     */
    @JsonProperty("NameForeign")
    public String getNameForeign() {
        return nameForeign;
    }

    /** TODO */
    @JsonProperty("NameForeign")
    public SoftwareSupplier setNameForeign(String nameForeign) {
        this.nameForeign = nameForeign;
        return this;
    }

    private static final
    TaxNumberValidator TAX_NUMBER = new TaxNumberValidator("taxNumber", true);

    private static final
    StringValidator NAME_FOREIGN = new StringValidator("nameForeign", 1, 1000, ANY, NULL_ALLOWED);
}
