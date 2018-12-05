package com.github.mslenc.furslib;

import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.github.mslenc.furslib.Utils.*;

public class FursEnv {
    private final URL invoicesUrl;
    private final URL batchInvoicesUrl;
    private final URL premisesUrl;
    private final URL echoUrl;
    private final List<X509Certificate> rootCerts;
    private final Predicate<X509Certificate> isClientCert;
    private final X509Certificate serverSignatureCert;

    public FursEnv(URL invoicesUrl, URL batchInvoicesUrl, URL premisesUrl, URL echoUrl,
                   List<X509Certificate> rootCerts, Predicate<X509Certificate> isClientCert) {

        this.invoicesUrl = checkIfNull(invoicesUrl, "invoicesUrl");
        this.batchInvoicesUrl = checkIfNull(batchInvoicesUrl, "batchInvoicesUrl");
        this.premisesUrl = checkIfNull(premisesUrl, "premisesUrl");
        this.echoUrl = checkIfNull(echoUrl, "echoUrl");
        this.isClientCert = checkIfNull(isClientCert, "isClientCert");

        ArrayList<X509Certificate> certsCopy = new ArrayList<>();

        checkIfNull(rootCerts, "rootCerts");
        for (X509Certificate cert : rootCerts) {
            certsCopy.add(checkIfNull(cert, "rootCerts element"));
        }

        if (certsCopy.isEmpty())
            throw new IllegalStateException("Missing at least one certificate");

        this.rootCerts = Collections.unmodifiableList(certsCopy);
        this.serverSignatureCert = rootCerts.get(0);
    }

    public URL getInvoicesUrl() {
        return invoicesUrl;
    }

    public URL getBatchInvoicesUrl() {
        return batchInvoicesUrl;
    }

    public URL getPremisesUrl() {
        return premisesUrl;
    }

    public URL getEchoUrl() {
        return echoUrl;
    }

    public List<X509Certificate> getRootCerts() {
        return rootCerts;
    }

    public Predicate<X509Certificate> getIsClientCert() {
        return isClientCert;
    }

    public X509Certificate getServerSignatureCert() {
        return serverSignatureCert;
    }

    public static final FursEnv TEST;
    public static final FursEnv PRODUCTION;
    public static final ZoneId EUROPE_LJUBLJANA;

    static {
        String testPrefix = "https://blagajne-test.fu.gov.si:9002/v1";
        String prodPrefix = "https://blagajne.fu.gov.si:9003/v1";

        try {
            TEST = new FursEnv(
                new URL(testPrefix + "/cash_registers/invoices"),
                new URL(testPrefix + "/cash_registers_batch/invoices"),
                new URL(testPrefix + "/cash_registers/invoices/register"),
                new URL(testPrefix + "/cash_registers/echo"),
                readCertsFromResources("/furs_certs/test/", "test-sign.pem", "test-tls.pem", "sitest-ca.pem", "TaxCATest.pem"),
                containsOU("DavPotRacTEST")
            );

            PRODUCTION = new FursEnv(
                new URL(prodPrefix + "/cash_registers/invoices"),
                new URL(prodPrefix + "/cash_registers_batch/invoices"),
                new URL(prodPrefix + "/cash_registers/invoices/register"),
                new URL(prodPrefix + "/cash_registers/echo"),
                readCertsFromResources("/furs_certs/prod/", "DavPotRac.pem", "blagajne.fu.gov.si.pem", "si-trust-root.pem", "sigov-ca2.xcert.pem"),
                containsOU("DavPotRac")
            );

            EUROPE_LJUBLJANA = ZoneId.of("Europe/Ljubljana");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
