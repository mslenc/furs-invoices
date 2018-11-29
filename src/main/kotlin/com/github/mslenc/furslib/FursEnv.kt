package com.github.mslenc.furslib

import java.net.URL
import java.security.cert.X509Certificate
import java.time.ZoneId
import java.util.function.Predicate

class FursEnv(
    val invoicesUrl: URL,
    val batchInvoicesUrl: URL,
    val premisesUrl: URL,
    val echoUrl: URL,
    val rootCerts: List<X509Certificate>,
    val isClientCert: Predicate<X509Certificate>
) {

    companion object {
        private const val testPrefix = "https://blagajne-test.fu.gov.si:9002/v1"
        private const val prodPrefix = "https://blagajne.fu.gov.si:9003/v1"

        @JvmStatic
        val TEST = FursEnv(
            invoicesUrl =      URL("$testPrefix/cash_registers/invoices"),
            batchInvoicesUrl = URL("$testPrefix/cash_registers_batch/invoices"),
            premisesUrl =      URL("$testPrefix/cash_registers/invoices/register"),
            echoUrl =          URL("$testPrefix/cash_registers/echo"),
            rootCerts = readCertsFromResources("/furs_certs/test/", "test-tls.pem", "test-sign.pem", "sitest-ca.pem", "TaxCATest.pem"),
            isClientCert = containsOU("DavPotRacTEST")
        )

        @JvmStatic
        val PRODUCTION = FursEnv(
            invoicesUrl =      URL("$prodPrefix/cash_registers/invoices"),
            batchInvoicesUrl = URL("$prodPrefix/cash_registers_batch/invoices"),
            premisesUrl =      URL("$prodPrefix/cash_registers/invoices/register"),
            echoUrl =          URL("$prodPrefix/cash_registers/echo"),
            rootCerts = readCertsFromResources("/furs_certs/prod/", "blagajne.fu.gov.si.pem", "DavPotRac.pem", "si-trust-root.pem", "sigov-ca2.xcert.pem"),
            isClientCert = containsOU("DavPotRac")
        )

        val EUROPE_LJUBLJANA = ZoneId.of("Europe/Ljubljana")
    }
}