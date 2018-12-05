package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.validation.StringValidator;

import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.ASCII_ALNUM;
import static com.github.mslenc.furslib.validation.StringValidator.NullEmptyMode.NO_NULLS;

public class InvoiceIdentifier {
    private String businessPremiseId = null;
    private String electronicDeviceId = null;
    private String invoiceNumber = null;

    public InvoiceIdentifier() {

    }

    public InvoiceIdentifier(String businessPremiseId, String electronicDeviceId, String invoiceNumber) {
        setBusinessPremiseId(businessPremiseId);
        setElectronicDeviceId(electronicDeviceId);
        setInvoiceNumber(invoiceNumber);
    }

    /**
     * @see #setBusinessPremiseId(String)
     */
    @JsonProperty("BusinessPremiseID")
    public String getBusinessPremiseId() {
        return businessPremiseId;
    }

    /**
     * Identifier of the business premise.
     * FURS says:
     * <blockquote>
     *     Mark of business premises.
     *     It may include only the following letters and numbers: 0-9, a-z, A-Z.
     * </blockquote>
     * @param businessPremiseID the new value (not null, matching the pattern described)
     * @return this, for fluent interface
     */
    @JsonProperty("BusinessPremiseID")
    public InvoiceIdentifier setBusinessPremiseId(String businessPremiseID) {
        this.businessPremiseId = BUSINESS_PREMISE_ID.validate(businessPremiseID);
        return this;
    }

    @JsonProperty("ElectronicDeviceID")
    public String getElectronicDeviceId() {
        return electronicDeviceId;
    }

    /**
     * Identifier of the electronic device.
     * FURS says:
     * <blockquote>
     *     Mark of the electronic device.
     *     It may include only the following letters and numbers: 0-9, a-z, A-Z.
     * </blockquote>
     * @param electronicDeviceID the new value (not null, matching the pattern described)
     * @return this, for fluent interface
     */
    @JsonProperty("ElectronicDeviceID")
    public InvoiceIdentifier setElectronicDeviceId(String electronicDeviceID) {
        this.electronicDeviceId = ELECTRONIC_DEVICE_ID.validate(electronicDeviceID);
        return this;
    }

    @JsonProperty("InvoiceNumber")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Sequence number of the invoice.
     * FURS says:
     * <blockquote>
     *     Sequence number of the invoice.
     *     It may include only the following letters and numbers: 0-9, a-z, A-Z.
     * </blockquote>
     * @param invoiceNumber the new value (not null, matching the pattern described)
     * @return this, for fluent interface
     */
    @JsonProperty("InvoiceNumber")
    public InvoiceIdentifier setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = INVOICE_NUMBER.validate(invoiceNumber);
        return this;
    }

    static final
    StringValidator BUSINESS_PREMISE_ID = new StringValidator("businessPremiseId", 1, 20, ASCII_ALNUM, NO_NULLS);

    private static final
    StringValidator ELECTRONIC_DEVICE_ID = new StringValidator("electronicDeviceId", 1, 20, ASCII_ALNUM, NO_NULLS);

    private static final
    StringValidator INVOICE_NUMBER = new StringValidator("invoiceNumber", 1, 20, ANY, NO_NULLS);
}