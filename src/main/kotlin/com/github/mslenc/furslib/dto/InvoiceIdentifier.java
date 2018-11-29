package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

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
        this.businessPremiseId = validateIdPart(businessPremiseID, "businessPremiseID");
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
        this.electronicDeviceId = validateIdPart(electronicDeviceID, "electronicDeviceID");
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
        this.invoiceNumber = validateIdPart(invoiceNumber, "invoiceNumber");
        return this;
    }

    private static final Pattern ID_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,20}$");
    private static String validateIdPart(String value, String propertyName) {
        if (value == null)
            throw new IllegalArgumentException("Null " + propertyName);

        if (ID_PART_PATTERN.matcher(value).matches())
            return value;

        throw new IllegalArgumentException("Invalid " + propertyName + " - it must be 1-20 ASCII alphanumeric characters");
    }
}