package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.fursinvoices.validation.AmountValidator;
import com.github.mslenc.fursinvoices.validation.TaxRateValidator;

import java.math.BigDecimal;

import static com.github.mslenc.fursinvoices.validation.DecimalValidator.NullZeroMode.NO_NULLS;

/**
 * Contains information about flat-rate compensation.
 */
public class FlatRateCompensation {
    private BigDecimal flatRateRate;
    private BigDecimal flatRateTaxableAmount;
    private BigDecimal flatRateAmount;

    /**
     * Creates an object with all three fields initialized to 0.
     */
    public FlatRateCompensation() {
        flatRateRate = FLAT_RATE_RATE.getNormalizedZero();
        flatRateTaxableAmount = FLAT_RATE_TAXABLE_AMOUNT.getNormalizedZero();
        flatRateAmount = FLAT_RATE_AMOUNT.getNormalizedZero();
    }

    /**
     * Creates an object with all three fields specified explicitly.
     */
    public FlatRateCompensation(BigDecimal flatRateRate, BigDecimal flatRateTaxableAmount, BigDecimal flatRateAmount) {
        setFlatRateRate(flatRateRate);
        setFlatRateTaxableAmount(flatRateTaxableAmount);
        setFlatRateAmount(flatRateAmount);
    }

    /**
     * Creates an object with all three fields specified explicitly.
     */
    public FlatRateCompensation(double flatRateRate, double flatRateTaxableAmount, double flatRateAmount) {
        setFlatRateRate(flatRateRate);
        setFlatRateTaxableAmount(flatRateTaxableAmount);
        setFlatRateAmount(flatRateAmount);
    }

    /**
     * @see #setFlatRateRate(BigDecimal)
     */
    @JsonProperty("FlatRateRate")
    public BigDecimal getFlatRateRate() {
        return flatRateRate;
    }

    /**
     * Sets the flat-rate compensation rate as percentage (e.g. 20% should be 20.0, not 0.2).
     * FURS says:
     * <blockquote>
     *     Value of the flat-rate compensation's rate.
     * </blockquote>
     * @param flatRateRate the new value (not null, between 0 and 999.99)
     * @return this, for fluent interface
     */
    @JsonProperty("FlatRateRate")
    public FlatRateCompensation setFlatRateRate(BigDecimal flatRateRate) {
        this.flatRateRate = FLAT_RATE_RATE.validateAndNormalize(flatRateRate);
        return this;
    }

    /**
     * @see #setFlatRateRate(BigDecimal)
     */
    @JsonIgnore
    public FlatRateCompensation setFlatRateRate(double taxRate) {
        this.flatRateRate = FLAT_RATE_RATE.validateAndConvert(taxRate);
        return this;
    }

    /**
     * @see #setFlatRateTaxableAmount(BigDecimal)
     */
    @JsonProperty("FlatRateTaxableAmount")
    public BigDecimal getFlatRateTaxableAmount() {
        return flatRateTaxableAmount;
    }

    /**
     * The flat-rate compensation taxable amount.
     * FURS says:
     * <blockquote>
     *     The base or value from which the amount of the flat-rate compensation is
     *     settled (after reduction for discounts).
     * </blockquote>
     * @param flatRateTaxableAmount the new value (not null, at most 2 decimal digits, between 0 and 999999999999.99)
     * @return this, for fluent interface
     */
    @JsonProperty("FlatRateTaxableAmount")
    public FlatRateCompensation setFlatRateTaxableAmount(BigDecimal flatRateTaxableAmount) {
        this.flatRateTaxableAmount = FLAT_RATE_TAXABLE_AMOUNT.validateAndNormalize(flatRateTaxableAmount);
        return this;
    }

    /**
     * @see #setFlatRateTaxableAmount(BigDecimal)
     */
    @JsonIgnore
    public FlatRateCompensation setFlatRateTaxableAmount(double taxableAmount) {
        this.flatRateTaxableAmount = FLAT_RATE_TAXABLE_AMOUNT.validateAndConvert(taxableAmount);
        return this;
    }


    /**
     * @see #setFlatRateAmount(BigDecimal)
     */
    @JsonProperty("FlatRateAmount")
    public BigDecimal getFlatRateAmount() {
        return flatRateAmount;
    }

    /**
     * The amount of the flat-rate compensation.
     * FURS says:
     * <blockquote>
     *     Amount of the flatrate compensation.
     * </blockquote>
     * @param flatRateAmount the new value (not null, at most 2 decimal digits, between 0 and 999999999999.99)
     * @return this, for fluent interface
     */
    @JsonProperty("FlatRateAmount")
    public FlatRateCompensation setFlatRateAmount(BigDecimal flatRateAmount) {
        this.flatRateAmount = FLAT_RATE_AMOUNT.validateAndNormalize(flatRateAmount);
        return this;
    }

    /**
     * @see #setFlatRateAmount(BigDecimal)
     */
    @JsonIgnore
    public FlatRateCompensation setFlatRateAmount(double taxAmount) {
        this.flatRateAmount = FLAT_RATE_AMOUNT.validateAndConvert(taxAmount);
        return this;
    }


    private static final
    TaxRateValidator FLAT_RATE_RATE = new TaxRateValidator("flatRateRate", NO_NULLS);

    private static final
    AmountValidator FLAT_RATE_TAXABLE_AMOUNT = new AmountValidator("flatRateTaxableAmount", NO_NULLS);

    private static final
    AmountValidator FLAT_RATE_AMOUNT = new AmountValidator("flatRateAmount", NO_NULLS);
}
