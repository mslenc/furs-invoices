package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.validation.AmountValidator;
import com.github.mslenc.furslib.validation.TaxRateValidator;

import java.math.BigDecimal;

import static com.github.mslenc.furslib.validation.DecimalValidator.NullZeroMode.NO_NULLS;

/**
 * Describes a VAT-taxed amount with a single tax rate (and single taxpayer).
 */
public class VAT {
    private BigDecimal taxRate;
    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;

    /**
     * Creates a VAT object with all three values initialized to 0.
     */
    public VAT() {
        taxRate = TAX_RATE.getNormalizedZero();
        taxableAmount = TAXABLE_AMOUNT.getNormalizedZero();
        taxAmount = TAX_AMOUNT.getNormalizedZero();
    }

    /**
     * Creates a VAT object with all three values explicitly specified.
     */
    public VAT(BigDecimal taxRate, BigDecimal taxableAmount, BigDecimal taxAmount) {
        setTaxRate(taxRate);
        setTaxableAmount(taxableAmount);
        setTaxAmount(taxAmount);
    }

    /**
     * Creates a VAT object with all three values explicitly specified.
     */
    public VAT(double taxRate, double taxableAmount, double taxAmount) {
        setTaxRate(taxRate);
        setTaxableAmount(taxableAmount);
        setTaxAmount(taxAmount);
    }

    /**
     * @see #setTaxRate(BigDecimal)
     */
    @JsonProperty("TaxRate")
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * The tax rate in percent (e.g. for 22% tax rate, provide 22.0 and not 0.22).
     * FURS says:
     * <blockquote>
     *     Value of the tax rate.
     * </blockquote>
     * @param taxRate the new value (must be between 0 and 999.99, with at most 2 decimal digits)
     * @return this, for fluent interface
     */
    @JsonProperty("TaxRate")
    public VAT setTaxRate(BigDecimal taxRate) {
        this.taxRate = TAX_RATE.validateAndNormalize(taxRate);
        return this;
    }

    /**
     * @see #setTaxRate(BigDecimal)
     */
    @JsonIgnore
    public VAT setTaxRate(double taxRate) {
        this.taxRate = TAX_RATE.validateAndConvert(taxRate);
        return this;
    }


    /**
     * @see #setTaxableAmount(BigDecimal)
     */
    @JsonProperty("TaxableAmount")
    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    /**
     * The amount that is taxed, i.e. the tax base.
     * FURS says:
     * <blockquote>
     *      Amount of the tax base (after reduction for discounts)
     * </blockquote>
     * @param taxableAmount the new value (not null, can have at most 2 decimals, and must be between 0 and 999999999999.99)
     * @return this, for fluent interface
     */
    @JsonProperty("TaxableAmount")
    public VAT setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = TAXABLE_AMOUNT.validateAndNormalize(taxableAmount);
        return this;
    }

    /**
     * @see #setTaxableAmount(BigDecimal)
     */
    @JsonIgnore
    public VAT setTaxableAmount(double taxableAmount) {
        this.taxableAmount = TAXABLE_AMOUNT.validateAndConvert(taxableAmount);
        return this;
    }

    /**
     * @see #setTaxAmount(BigDecimal)
     */
    @JsonProperty("TaxAmount")
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * The amount of tax, i.e. basically taxRate * taxableAmount.
     * FURS says:
     * <blockquote>
     *     Amount of tax.
     * </blockquote>
     * @param taxAmount the new value (not null, can have at most 2 decimals, and must be between 0 and 999999999999.99)
     * @return this, for fluent interface
     */
    @JsonProperty("TaxAmount")
    public VAT setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = TAX_AMOUNT.validateAndNormalize(taxAmount);
        return this;
    }

    /**
     * @see #setTaxAmount(BigDecimal)
     */
    @JsonIgnore
    public VAT setTaxAmount(double taxAmount) {
        this.taxAmount = TAX_AMOUNT.validateAndConvert(taxAmount);
        return this;
    }


    private static final
    TaxRateValidator TAX_RATE = new TaxRateValidator("taxRate", NO_NULLS);

    private static final
    AmountValidator TAXABLE_AMOUNT = new AmountValidator("taxableAmount", NO_NULLS);

    private static final
    AmountValidator TAX_AMOUNT = new AmountValidator("taxAmount", NO_NULLS);
}
