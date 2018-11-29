package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.DecimalValidator;
import com.github.mslenc.furslib.UtilsKt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.mslenc.furslib.UtilsKt.validate8DigitTaxNumber;
import static java.math.BigDecimal.ZERO;

/**
 * Contains base and tax amounts and rates for a single taxpayer.
 */
public class TaxPerSeller {
    private Integer sellerTaxNumber = null;
    private ArrayList<VAT> vats = new ArrayList<>();
    private FlatRateCompensation flatRateCompensation = null;
    private BigDecimal otherTaxesAmount = null;
    private BigDecimal exemptVatTaxableAmount = null;
    private BigDecimal reverseVatTaxableAmount = null;
    private BigDecimal nonTaxableAmount = null;
    private BigDecimal specialTaxRulesAmount = null;

    /**
     * @see #setSellerTaxNumber(String)
     */
    @JsonProperty("SellerTaxNumber")
    public Integer getSellerTaxNumber() {
        return sellerTaxNumber;
    }

    /**
     * The taxpayer's tax number, if different from invoice issuer.
     * FURS says:
     * <blockquote>
     *     The tax number of the taxpayer is entered in the name of and on behalf of
     *     whose the invoice has been issued, if the invoice has been issued in the
     *     name of and on behalf of another person or if the invoice has been issued by the
     *     recipient of the invoice in the name of and on behalf of the supplier. If the
     *     invoice has not been issued in the name of and on behalf of another person,
     *     the data is not entered.
     * </blockquote>
     * @param sellerTaxNumber the new value (may be null, empty, or 8 digits)
     * @return this, for fluent interface
     */
    @JsonProperty("SellerTaxNumber")
    public TaxPerSeller setSellerTaxNumber(Integer sellerTaxNumber) {
        if (sellerTaxNumber == null) {
            this.sellerTaxNumber = null;
        } else {
            this.sellerTaxNumber = UtilsKt.validate8DigitTaxNumber(sellerTaxNumber, "sellerTaxNumber");
        }

        return this;
    }

    /**
     * @see #setSellerTaxNumber(Integer)
     */
    @JsonIgnore
    public TaxPerSeller setSellerTaxNumber(String sellerTaxNumber) {
        if (sellerTaxNumber == null || sellerTaxNumber.isEmpty()) {
            this.sellerTaxNumber = null;
        } else {
            this.sellerTaxNumber = validate8DigitTaxNumber(sellerTaxNumber, "sellerTaxNumber");
        }

        return this;
    }

    /**
     * Adds a single VAT entry.
     * @see #setVats(List)
     */
    @JsonIgnore
    public TaxPerSeller addVat(VAT vat) {
        if (vat == null)
            throw new IllegalArgumentException("Null vat");

        vats.add(vat);

        return this;
    }

    /**
     * Sets the VAT information for the single taxpayer.
     * FURS says:
     * <blockquote>
     *     Data about VAT are entered.<br />
     *     The data is submitted only if the invoice includes the amount of VAT settled.<br />
     *     The data consists of the tax rate, tax base and amount of tax.<br />
     *     The tax authority may have a list of tax rates.
     * </blockquote>
     * @param vats the new list of information (the list may be empty, but may not contain nulls)
     * @return this, for fluent interface
     */
    @JsonProperty("VAT")
    public TaxPerSeller setVats(List<VAT> vats) {
        if (vats == null)
            throw new IllegalArgumentException("Null VAT list");

        for (VAT element : vats)
            if (element == null)
                throw new IllegalArgumentException("Null VAT element");

        this.vats.clear();
        this.vats.addAll(vats);

        return this;
    }

    /**
     * @see #setVats(List)
     */
    @JsonIgnore
    public TaxPerSeller setVats(VAT... vats) {
        return setVats(Arrays.asList(vats));
    }

    /**
     * @see #setVats(List)
     */
    @JsonProperty("VAT")
    public List<VAT> getVats() {
        return new ArrayList<>(vats);
    }


    /**
     * @see #setFlatRateCompensation(FlatRateCompensation)
     */
    @JsonProperty("FlatRateCompensation")
    public FlatRateCompensation getFlatRateCompensation() {
        return flatRateCompensation;
    }

    /**
     * Flat-rate compensation.
     * FURS says:
     * <blockquote>
     *     Data about the flat-rate compensation are entered.<br />
     *     The data is submitted only if the invoice includes the amount
     *     of the flat-rate compensation settled.<br />
     *     The data consists of the rate, base and amount of the flat-rate
     *     compensation.
     * </blockquote>
     * @param flatRateCompensation the new value (may be null)
     * @return this, for fluent interface
     */
    @JsonProperty("FlatRateCompensation")
    public TaxPerSeller setFlatRateCompensation(FlatRateCompensation flatRateCompensation) {
        this.flatRateCompensation = flatRateCompensation;
        return this;
    }


    @JsonProperty("OtherTaxesAmount")
    public BigDecimal getOtherTaxesAmount() {
        return otherTaxesAmount;
    }

    @JsonProperty("OtherTaxesAmount")
    public TaxPerSeller setOtherTaxesAmount(BigDecimal otherTaxesAmount) {
        if (otherTaxesAmount == null || otherTaxesAmount.compareTo(ZERO) == 0) {
            this.otherTaxesAmount = null;
        } else {
            this.otherTaxesAmount = OTHER_TAXES_AMOUNT.validateAndNormalize(otherTaxesAmount);
        }

        return this;
    }

    @JsonIgnore
    public TaxPerSeller setOtherTaxesAmount(double otherTaxesAmount) {
        if (otherTaxesAmount == 0.0) {
            this.otherTaxesAmount = null;
        } else {
            this.otherTaxesAmount = OTHER_TAXES_AMOUNT.validateAndConvert(otherTaxesAmount);
        }

        return this;
    }


    @JsonProperty("ExemptVATTaxableAmount")
    public BigDecimal getExemptVATTaxableAmount() {
        return exemptVatTaxableAmount;
    }

    @JsonProperty("ExemptVATTaxableAmount")
    public void setExemptVATTaxableAmount(BigDecimal exemptVatTaxableAmount) {
        if (exemptVatTaxableAmount == null || exemptVatTaxableAmount.compareTo(ZERO) == 0) {
            this.exemptVatTaxableAmount = null;
        } else {
            this.exemptVatTaxableAmount = EXEMPT_VAT_TAXABLE_AMOUNT.validateAndNormalize(exemptVatTaxableAmount);
        }
    }

    @JsonIgnore
    public TaxPerSeller setExemptVATTaxableAmount(double exemptVatTaxableAmount) {
        if (exemptVatTaxableAmount == 0.0) {
            this.exemptVatTaxableAmount = null;
        } else {
            this.exemptVatTaxableAmount = EXEMPT_VAT_TAXABLE_AMOUNT.validateAndConvert(exemptVatTaxableAmount);
        }

        return this;
    }


    @JsonProperty("ReverseVATTaxableAmount")
    public BigDecimal getReverseVatTaxableAmount() {
        return reverseVatTaxableAmount;
    }

    @JsonProperty("ReverseVATTaxableAmount")
    public TaxPerSeller setReverseVatTaxableAmount(BigDecimal reverseVatTaxableAmount) {
        if (reverseVatTaxableAmount == null || reverseVatTaxableAmount.compareTo(ZERO) == 0) {
            this.reverseVatTaxableAmount = null;
        } else {
            this.reverseVatTaxableAmount = REVERSE_VAT_TAXABLE_AMOUNT.validateAndNormalize(reverseVatTaxableAmount);
        }

        return this;
    }

    @JsonIgnore
    public TaxPerSeller setReverseVatTaxableAmount(double reverseVatTaxableAmount) {
        if (reverseVatTaxableAmount == 0.0) {
            this.reverseVatTaxableAmount = null;
        } else {
            this.reverseVatTaxableAmount = REVERSE_VAT_TAXABLE_AMOUNT.validateAndConvert(reverseVatTaxableAmount);
        }

        return this;
    }


    @JsonProperty("NontaxableAmount")
    public BigDecimal getNonTaxableAmount() {
        return nonTaxableAmount;
    }

    @JsonProperty("NontaxableAmount")
    public TaxPerSeller setNonTaxableAmount(BigDecimal nonTaxableAmount) {
        if (nonTaxableAmount == null || nonTaxableAmount.compareTo(ZERO) == 0) {
            this.nonTaxableAmount = null;
        } else {
            this.nonTaxableAmount = NON_TAXABLE_AMOUNT.validateAndNormalize(nonTaxableAmount);
        }

        return this;
    }

    @JsonIgnore
    public TaxPerSeller setNonTaxableAmount(double nonTaxableAmount) {
        if (nonTaxableAmount == 0.0) {
            this.nonTaxableAmount = null;
        } else {
            this.nonTaxableAmount = NON_TAXABLE_AMOUNT.validateAndConvert(nonTaxableAmount);
        }

        return this;
    }


    @JsonProperty("SpecialTaxRulesAmount")
    public BigDecimal getSpecialTaxRulesAmount() {
        return specialTaxRulesAmount;
    }

    @JsonProperty("SpecialTaxRulesAmount")
    public TaxPerSeller setSpecialTaxRulesAmount(BigDecimal specialTaxRulesAmount) {
        if (specialTaxRulesAmount == null || specialTaxRulesAmount.compareTo(ZERO) == 0) {
            this.specialTaxRulesAmount = null;
        } else {
            this.specialTaxRulesAmount = SPECIAL_TAX_RULES_AMOUNT.validateAndNormalize(specialTaxRulesAmount);
        }

        return this;
    }

    @JsonIgnore
    public TaxPerSeller setSpecialTaxRulesAmount(double specialTaxRulesAmount) {
        if (specialTaxRulesAmount == 0.0) {
            this.specialTaxRulesAmount = null;
        } else {
            this.specialTaxRulesAmount = SPECIAL_TAX_RULES_AMOUNT.validateAndConvert(specialTaxRulesAmount);
        }

        return this;
    }


    private static final
    DecimalValidator OTHER_TAXES_AMOUNT = new DecimalValidator("otherTaxesAmount", 14, 2);

    private static final
    DecimalValidator EXEMPT_VAT_TAXABLE_AMOUNT = new DecimalValidator("exemptVatTaxableAmount", 14, 2);

    private static final
    DecimalValidator REVERSE_VAT_TAXABLE_AMOUNT = new DecimalValidator("reverseVatTaxableAmount", 14, 2);

    private static final
    DecimalValidator NON_TAXABLE_AMOUNT = new DecimalValidator("nonTaxableAmount", 14, 2);

    private static final
    DecimalValidator SPECIAL_TAX_RULES_AMOUNT = new DecimalValidator("specialTaxRulesAmount", 14, 2);
}
