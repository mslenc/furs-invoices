package com.github.mslenc.fursinvoices.dto;

import com.github.mslenc.fursinvoices.validation.StringValidator;

import static com.github.mslenc.fursinvoices.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.fursinvoices.validation.StringValidator.NullEmptyMode.NO_NULLS;

public class SalesBookIdentifier {
    private String invoiceNumber;
    private String setNumber;
    private String serialNumber;

    public SalesBookIdentifier() {

    }

    public SalesBookIdentifier(String invoiceNumber, String setNumber, String serialNumber) {
        setInvoiceNumber(invoiceNumber);
        setSetNumber(setNumber);
        setSerialNumber(serialNumber);
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = INVOICE_NUMBER.validate(invoiceNumber);
    }

    public String getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = SET_NUMBER.validate(setNumber);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = SERIAL_NUMBER.validate(serialNumber);
    }

    private static final
    StringValidator INVOICE_NUMBER = new StringValidator("invoiceNumber", 1, 20, ANY, NO_NULLS);

    private static final
    StringValidator SET_NUMBER = new StringValidator("setNumber", 2, 2, ANY, NO_NULLS);

    private static final
    StringValidator SERIAL_NUMBER = new StringValidator("serialNumber", 12, 12, ANY, NO_NULLS);
}
