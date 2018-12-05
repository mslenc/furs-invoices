package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.FursConfig;
import com.github.mslenc.furslib.Utils;
import com.github.mslenc.furslib.validation.AmountValidator;
import com.github.mslenc.furslib.validation.DateTimeValidator;
import com.github.mslenc.furslib.validation.StringValidator;
import com.github.mslenc.furslib.validation.StringValidator.NullEmptyMode;
import com.github.mslenc.furslib.validation.TaxNumberValidator;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.github.mslenc.furslib.Utils.computeRS256;
import static com.github.mslenc.furslib.validation.DecimalValidator.NullZeroMode.NO_NULLS;
import static com.github.mslenc.furslib.validation.DecimalValidator.NullZeroMode.ZERO_TO_NULL;
import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.bouncycastle.util.encoders.Hex.toHexString;

/**
 * Data about an invoice issued with an electronic device.
 * FURS says:
 * <blockquote>
 *     Data are entered about the invoice, which is issued with usage
 *     of the electronic device.
 * </blockquote>
 */
public class Invoice {
    private Integer taxNumber;
    private LocalDateTime issueDateTime;
    private NumberingStructure numberingStructure;
    private InvoiceIdentifier invoiceIdentifier;
    private String customerVatNumber;
    private BigDecimal invoiceAmount;
    private BigDecimal returnsAmount;
    private BigDecimal paymentAmount;
    private ArrayList<TaxPerSeller> taxesPerSeller = new ArrayList<>();
    private Integer operatorTaxNumber;
    private Boolean foreignOperator;
    private String protectedId;
    private Boolean subsequentSubmit;
    private ArrayList<ReferenceInvoice> referenceInvoices = new ArrayList<>();

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonProperty("TaxNumber")
    public Integer getTaxNumber() {
        return taxNumber;
    }

    /**
     * The tax number of the issuer of the invoice.
     * FURS says:
     * <blockquote>
     *     The tax number of the person liable, who has issued the
     *     invoice, is entered.
     * </blockquote>
     *
     * @param taxNumber the new value (not null, must be exactly 8 digits)
     * @return this, for fluent interface
     */
    @JsonProperty("TaxNumber")
    public Invoice setTaxNumber(Integer taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonIgnore
    public Invoice setTaxNumber(int taxNumber) {
        this.taxNumber = TAX_NUMBER.validate(taxNumber);
        return this;
    }

    /**
     * @see #setTaxNumber(Integer)
     */
    @JsonIgnore
    public Invoice setTaxNumber(String taxNumber) {
        this.taxNumber = TAX_NUMBER.validateAndConvert(taxNumber);
        return this;
    }

    /**
     * @see #setIssueDateTime(LocalDateTime)
     */
    @JsonProperty("IssueDateTime")
    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    /**
     * The date/time when the invoice was issued.
     * FURS says:
     * <blockquote>
     *     Date and time of issuing the invoice, which is stated on the
     *     invoice, are entered.
     * </blockquote>
     * @param issueDateTime the new value, must not be null
     * @return this, for fluent interface
     */
    @JsonProperty("IssueDateTime")
    public Invoice setIssueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = ISSUE_DATE_TIME.validate(issueDateTime);
        return this;
    }

    /**
     * @see #setIssueDateTime(LocalDateTime)
     */
    @JsonIgnore
    public Invoice setIssueDateTime(Instant issueDateTime) {
        this.issueDateTime = ISSUE_DATE_TIME.validateAndConvert(issueDateTime);
        return this;
    }

    /**
     * @see #setNumberingStructure(NumberingStructure)
     */
    @JsonProperty("NumberingStructure")
    public NumberingStructure getNumberingStructure() {
        return numberingStructure;
    }

    /**
     * The method used for assigning invoice numbers.
     * FURS says:
     * <blockquote>
     *     The mark is entered for the method of assigning numbers to invoices:
     *     <ul>
     *         <li><em>C</em> (<tt>CENTRALLY</tt>) - centrally at the level of business premises
     *         <li><em>B</em> (<tt>PER_DEVICE</tt>) - per individual electronic device (cash register)
     *     </ul>
     *     The mark explains the method for assigning numbers to invoices.
     *     Invoice numbers may be assigned centrally at the level of business premises or individually on
     *     the electronic device for issuing invoices.
     * </blockquote>
     * @param numberingStructure the new value (must not be null)
     * @return this, for fluent interface
     */
    @JsonProperty("NumberingStructure")
    public Invoice setNumberingStructure(NumberingStructure numberingStructure) {
        if (numberingStructure == null)
            throw new IllegalArgumentException("Null numberingStructure");

        this.numberingStructure = numberingStructure;
        return this;
    }

    /**
     * @see #setInvoiceIdentifier(InvoiceIdentifier)
     */
    @JsonProperty("InvoiceIdentifier")
    public InvoiceIdentifier getInvoiceIdentifier() {
        return invoiceIdentifier;
    }

    /**
     * The invoice identifier, consisting of premise ID, device ID and sequence number.
     * FURS says:
     * <blockquote>
     *     The number of the issued invoice is entered.<br />
     *     The number of the document is also entered, which changes the original
     *     invoice (credit, reversing, etc.) in cases of performing the procedure for
     *     verification of subsequent changes of data on the invoice, which
     *     changes the original invoice and refers to it with reasonable certainty. <br />
     *     The invoice number includes three parts:
     *     <ol>
     *         <li>Mark of business premises</li>
     *         <li>Mark of the electronic device for issuing invoices</li>
     *         <li>Sequence number of the invoice</li>
     *     </ol>
     *     The invoice number is stated on the invoice in the following form:
     *     <pre>mark of business premises-mark of the electronic device-sequence invoice number</pre>
     *     Example: <tt>TRGOVINA1-BLAG2-1234</tt> <br />
     *     Data are entered separately.
     * </blockquote>
     * @param invoiceIdentifier the new value (must not be null and must match the format described)
     * @return this, for fluent interface
     */
    @JsonProperty("InvoiceIdentifier")
    public Invoice setInvoiceIdentifier(InvoiceIdentifier invoiceIdentifier) {
        this.invoiceIdentifier = invoiceIdentifier;
        return this;
    }

    /**
     * @see #setCustomerVatNumber(String)
     */
    @JsonProperty("CustomerVATNumber")
    public String getCustomerVatNumber() {
        return customerVatNumber;
    }

    /**
     * The buyer's EU VAT ID or her tax number, if known.
     * FURS says:
     * <blockquote>
     *     Tax number or identification mark for VAT purposes of the buyer.<br />
     *     The tax number is entered or identification number for VAT purposes of the
     *     buyer or ordering party in cases when these data are stated on the
     *     invoice in accordance with tax regulations.
     * </blockquote>
     * @param customerVatNumber the new value (may be null, otherwise 1-20 ASCII alphanumeric characters and/or hyphens)
     * @return this, for fluent interface
     */
    @JsonProperty("CustomerVATNumber")
    public Invoice setCustomerVatNumber(String customerVatNumber) {
        this.customerVatNumber = CUSTOMER_VAT_NUMBER.validate(customerVatNumber);
        return this;
    }

    /**
     * @see #setInvoiceAmount(BigDecimal)
     */
    @JsonProperty("InvoiceAmount")
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * The value of the invoice.
     * FURS says:
     * <blockquote>
     *     The total amount of the invoice is entered. The amount of the invoice is entered
     *     together with VAT and other taxes/duties, decreased for amounts of discounts.
     * </blockquote>
     * @param invoiceAmount the new amount (not null)
     * @return this, for fluent interface
     */
    @JsonProperty("InvoiceAmount")
    public Invoice setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = INVOICE_AMOUNT.validateAndNormalize(invoiceAmount);
        return this;
    }

    /**
     * @see #setInvoiceAmount(BigDecimal)
     */
    @JsonIgnore
    public Invoice setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = INVOICE_AMOUNT.validateAndConvert(invoiceAmount);
        return this;
    }

    /**
     * @see #setReturnsAmount(BigDecimal)
     */
    @JsonProperty("ReturnsAmount")
    public BigDecimal getReturnsAmount() {
        return returnsAmount;
    }

    /**
     * The amount of refunds, if any.
     * FURS says:
     * <blockquote>
     *     The amount of refunds on the invoice, which are recognized to the
     *     buyer (e.g. on the basis of credit for returning packaging), is
     *     entered.<br />
     *     The data is entered only if there are refunds on the invoice.
     * </blockquote>
     * @param returnsAmount the new value (may be null)
     * @return this, for fluent interface
     */
    @JsonProperty("ReturnsAmount")
    public Invoice setReturnsAmount(BigDecimal returnsAmount) {
        this.returnsAmount = RETURNS_AMOUNT.validateAndNormalize(returnsAmount);
        return this;
    }

    /**
     * @see #setReturnsAmount(BigDecimal)
     */
    @JsonIgnore
    public Invoice setReturnsAmount(double returnsAmount) {
        this.returnsAmount = RETURNS_AMOUNT.validateAndConvert(returnsAmount);
        return this;
    }

    /**
     * @see #setReturnsAmount(BigDecimal)
     */
    @JsonIgnore
    public Invoice setReturnsAmount(Double returnsAmount) {
        this.returnsAmount = RETURNS_AMOUNT.validateAndConvert(returnsAmount);
        return this;
    }


    /**
     * @see #setPaymentAmount(BigDecimal)
     */
    @JsonProperty("PaymentAmount")
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Value for payment.
     * FURS says:
     * <blockquote>
     *     The amount of the invoice for payment is entered.
     * </blockquote>
     * @param paymentAmount the new amount (not null)
     * @return this, for fluent interface
     */
    @JsonProperty("PaymentAmount")
    public Invoice setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = PAYMENT_AMOUNT.validateAndNormalize(paymentAmount);
        return this;
    }

    /**
     * @see #setPaymentAmount(BigDecimal)
     */
    @JsonIgnore
    public Invoice setPaymentAmount(double paymentAmount) {
        this.paymentAmount = PAYMENT_AMOUNT.validateAndConvert(paymentAmount);
        return this;
    }

    /**
     * Adds tax information for a single taxpayer.
     * @see #setTaxesPerSeller(List)
     */
    @JsonIgnore
    public Invoice addTaxPerSeller(TaxPerSeller taxPerSeller) {
        if (taxPerSeller == null)
            throw new IllegalArgumentException("Null taxPerSeller");

        taxesPerSeller.add(taxPerSeller);

        return this;
    }

    /**
     * A list of tax information, with one entry per different taxpayer.
     * FURS says:
     * <blockquote>
     *     Value of the base according to taxes and duties, according to taxpayers.<br />
     *     The value is entered for bases according to types of taxes or duties, separated
     *     per tax rates, and associated taxes or duties, value of supplies on the basis of
     *     special arrangements, supplies where the payer of VAT is the buyer of goods or
     *     party ordering services, exempt supplies and nontaxable supplies, separated
     *     according to tax numbers of taxpayers.
     * </blockquote>
     * @param taxesPerSeller the new list of taxes (must not be null or empty; replaces previous info, if any)
     * @return this, for fluent interface
     */
    @JsonProperty("TaxesPerSeller")
    public Invoice setTaxesPerSeller(List<TaxPerSeller> taxesPerSeller) {
        if (taxesPerSeller == null || taxesPerSeller.isEmpty())
            throw new IllegalArgumentException("Null or empty taxesPerSeller list");

        for (TaxPerSeller element : taxesPerSeller)
            if (element == null)
                throw new IllegalArgumentException("Null taxesPerSeller element");

        this.taxesPerSeller.clear();
        this.taxesPerSeller.addAll(taxesPerSeller);

        return this;
    }

    /**
     * @see #setTaxesPerSeller(List)
     */
    @JsonIgnore
    public Invoice setTaxesPerSeller(TaxPerSeller... taxesPerSeller) {
        return setTaxesPerSeller(Arrays.asList(taxesPerSeller));
    }

    /**
     * @see #setTaxesPerSeller(List)
     */
    @JsonProperty("TaxesPerSeller")
    public List<TaxPerSeller> getTaxesPerSeller() {
        return new ArrayList<>(taxesPerSeller);
    }

    /**
     * @see #setOperatorTaxNumber(String)
     */
    @JsonProperty("OperatorTaxNumber")
    public Integer getOperatorTaxNumber() {
        return operatorTaxNumber;
    }

    /**
     * The tax number of the person operating the electronic device.
     * GURS says:
     * <blockquote>
     *     The tax number is entered of the individual (operator), who issues the invoice
     *     with the usage of the electronic device for issuing invoices. In cases of
     *     issuing invoices via self-service electronic devices or when invoices
     *     are issued without the presence of individuals, the tax number of the
     *     person liable is entered. <br />
     *     The data is not entered if the person has no Slovene tax number.
     * </blockquote>
     * It seems that if there is no operator tax number, foreignOperator must be true.
     * @param operatorTaxNumber the new value (null or 8 digits)
     */
    @JsonProperty("OperatorTaxNumber")
    public Invoice setOperatorTaxNumber(Integer operatorTaxNumber) {
        this.operatorTaxNumber = OPERATOR_TAX_NUMBER.validate(operatorTaxNumber);
        return this;
    }

    /**
     * @see #setOperatorTaxNumber(Integer)
     */
    @JsonIgnore
    public Invoice setOperatorTaxNumber(String operatorTaxNumber) {
        this.operatorTaxNumber = OPERATOR_TAX_NUMBER.validateAndConvert(operatorTaxNumber);
        return this;
    }

    /**
     * @see #setForeignOperator(Boolean)
     */
    @JsonProperty("ForeignOperator")
    public Boolean getForeignOperator() {
        return foreignOperator;
    }

    /**
     * True means that the operator of the electronic device doesn't have
     * a slovenian tax number.
     * FURS says:
     * <blockquote>
     *     You enter <tt>true</tt> if the individual (operator), who issues the invoice
     *     with the usage of the electronic device, has no Slovene tax number, otherwise
     *     <tt>false</tt>
     * </blockquote>
     * @param foreignOperator the new value (may be null)
     */
    @JsonProperty("ForeignOperator")
    public Invoice setForeignOperator(Boolean foreignOperator) {
        this.foreignOperator = foreignOperator;
        return this;
    }

    /**
     * @see #setProtectedId(String)
     */
    @JsonProperty("ProtectedID")
    public String getProtectedId() {
        return protectedId;
    }

    /**
     * The so-called ZOI - protective mark of the invoice issuer.
     * GURS says:
     * <blockquote>
     *     The protective mark of the invoice issuer is entered.<br />
     *     The protective mark includes 32 characters in the hexadecimal notation.
     * </blockquote>
     * @see #computeProtectedId(FursConfig)
     */
    @JsonProperty("ProtectedID")
    public Invoice setProtectedId(String protectedId) {
        this.protectedId = PROTECTED_ID.validate(protectedId);
        return this;
    }

    @JsonIgnore
    public Invoice computeProtectedId(FursConfig config) {
        int taxNumber = requireNonNull(this.taxNumber, "Missing taxNumber");
        LocalDateTime issueDateTime = requireNonNull(this.issueDateTime, "Missing issueDateTime");
        InvoiceIdentifier id = requireNonNull(this.invoiceIdentifier, "Missing invoiceIdentifier");
        String invoiceNumber = requireNonNull(id.getInvoiceNumber(), "Missing invoiceNumber");
        String premiseId = requireNonNull(id.getBusinessPremiseId(), "Missing businessPremiseId");
        String deviceId = requireNonNull(id.getElectronicDeviceId(), "Missing electronicDeviceId");
        BigDecimal amount = requireNonNull(this.invoiceAmount, "Missing invoiceAmount");
        String amountString = amount.toPlainString();

        String combined = taxNumber + Utils.formatDateTimeForZoi(issueDateTime) + invoiceNumber + premiseId + deviceId + amountString;

        byte[] signResult = computeRS256(combined.getBytes(UTF_8), config.getPrivateKey());

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        byte[] digest = md5.digest(signResult);

        String zoi = toHexString(digest);
        return setProtectedId(zoi);
    }


    /**
     * @see #setSubsequentSubmit(Boolean)
     */
    @JsonProperty("SubsequentSubmit")
    public Boolean getSubsequentSubmit() {
        return subsequentSubmit;
    }

    /**
     * Used to identify subsequent submitting of invoices.
     * FURS says:
     * <blockquote>
     *     Subsequently submitted invoices are invoices, which have been issued
     *     without the unique identification invoice mark – EOR (e.g. due to
     *     disconnections of electronic connections with the tax authority). If
     *     the invoice is subsequently submitted to the tax authority, <tt>true</tt> is
     *     entered, otherwise <tt>false</tt>.
     * </blockquote>
     * @param subsequentSubmit the new value (may be null)
     */
    @JsonProperty("SubsequentSubmit")
    public Invoice setSubsequentSubmit(Boolean subsequentSubmit) {
        this.subsequentSubmit = subsequentSubmit;
        return this;
    }


    /**
     * Adds tax information for a single taxpayer.
     * @see #setReferenceInvoices(List)
     */
    @JsonIgnore
    public Invoice addReferenceInvoice(ReferenceInvoice referenceInvoice) {
        if (referenceInvoice == null)
            throw new IllegalArgumentException("null referenceInvoice ");

        referenceInvoices.add(referenceInvoice);

        return this;
    }

    /**
     * Numbers of invoices that are being modified (if invoices are being modified).
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
     * @see #setInvoiceIdentifier(InvoiceIdentifier)
     */
    @JsonProperty("ReferenceInvoice")
    public Invoice setReferenceInvoices(List<ReferenceInvoice> referenceInvoices) {
        if (referenceInvoices == null || referenceInvoices.isEmpty()) {
            this.referenceInvoices.clear();
            return this;
        }

        for (ReferenceInvoice element : referenceInvoices)
            if (element == null)
                throw new IllegalArgumentException("Null referenceInvoices element");

        this.referenceInvoices.clear();
        this.referenceInvoices.addAll(referenceInvoices);

        return this;
    }

    /**
     * @see #setReferenceInvoices(List)
     */
    @JsonIgnore
    public Invoice setReferenceInvoices(ReferenceInvoice... referenceInvoices) {
        return setReferenceInvoices(Arrays.asList(referenceInvoices));
    }

    /**
     * @see #setReferenceInvoices(List)
     */
    @JsonProperty("ReferenceInvoice")
    public List<ReferenceInvoice> getReferenceInvoices() {
        return new ArrayList<>(referenceInvoices);
    }

    private static final
    TaxNumberValidator TAX_NUMBER = new TaxNumberValidator("taxNumber", false);

    private static final
    TaxNumberValidator OPERATOR_TAX_NUMBER = new TaxNumberValidator("operatorTaxNumber", true);

    private static final
    StringValidator CUSTOMER_VAT_NUMBER = new StringValidator("customerVatNumber", 1, 20, VAT_ID_CHARS, NullEmptyMode.NO_NULLS);

    private static final
    StringValidator PROTECTED_ID = new StringValidator("protectedId", 32, 32, HEX, NullEmptyMode.NO_NULLS);

    private static final
    DateTimeValidator ISSUE_DATE_TIME = new DateTimeValidator("issueDateTime");

    private static final
    AmountValidator INVOICE_AMOUNT = new AmountValidator("invoiceAmount", NO_NULLS);

    private static final
    AmountValidator PAYMENT_AMOUNT = new AmountValidator("paymentAmount", NO_NULLS);

    private static final
    AmountValidator RETURNS_AMOUNT = new AmountValidator("returnsAmount", ZERO_TO_NULL);
}
