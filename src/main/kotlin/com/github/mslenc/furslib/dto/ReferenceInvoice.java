package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ReferenceInvoice {
    private Instant referenceInvoiceIssueDateTime;
    private InvoiceIdentifier referenceInvoiceIdentifier;

    /**
     * @see #setReferenceInvoiceIssueDateTime(Instant)
     */
    @JsonProperty("ReferenceInvoiceIssueDateTime")
    public Instant getReferenceInvoiceIssueDateTime() {
        return referenceInvoiceIssueDateTime;
    }

    /**
     * Date/time of the invoice which is being modified.
     * FURS says:
     * <blockquote>
     *     Date and time are entered of issuing the original invoice in cases of
     *     subsequent changes of data on the original invoice if the original
     *     invoice has been issued via the electronic device.
     * </blockquote>
     * @param referenceInvoiceIssueDateTime the new value, may not be null
     */
    @JsonProperty("ReferenceInvoiceIssueDateTime")
    public ReferenceInvoice setReferenceInvoiceIssueDateTime(Instant referenceInvoiceIssueDateTime) {
        if (referenceInvoiceIssueDateTime == null)
            throw new IllegalArgumentException("null referenceInvoiceIssueDateTime");

        this.referenceInvoiceIssueDateTime = referenceInvoiceIssueDateTime;
        return this;
    }

    /**
     * @see #setReferenceInvoiceIdentifier(InvoiceIdentifier)
     */
    @JsonProperty("ReferenceInvoiceIdentifier")
    public InvoiceIdentifier getReferenceInvoiceIdentifier() {
        return referenceInvoiceIdentifier;
    }

    /**
     * The invoice identifier of the invoice being modified.
     * FURS says:
     * <blockquote>
     *     The number of the original invoice is entered in cases of subsequent
     *     changes of data on the original invoice if the original invoice has been
     *     issued via the electronic device.<br />
     *     The person liable conducts the procedure for verification of invoices also for all
     *     subsequent changes of data on the invoice, which change the original invoice and they
     *     refer to it with reasonable certainty.<br />
     *     The data is entered in cases if the original invoice, which has been issued via the
     *     electronic device, changes with the invoice, issued via the electronic device.<br />
     *     Rules for entry of the invoice number, which is changed, are the same as those for
     *     entry of the invoice number.
     * </blockquote>
     * @param referenceInvoiceIdentifier the new value (may not be null)
     */
    @JsonProperty("ReferenceInvoiceIdentifier")
    public ReferenceInvoice setReferenceInvoiceIdentifier(InvoiceIdentifier referenceInvoiceIdentifier) {
        if (referenceInvoiceIdentifier == null)
            throw new IllegalArgumentException("null referenceInvoiceIdentifier");

        this.referenceInvoiceIdentifier = referenceInvoiceIdentifier;
        return this;
    }
}
