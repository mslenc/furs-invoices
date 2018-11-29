package com.github.mslenc.furslib

import com.github.mslenc.furslib.dto.*
import java.io.ByteArrayInputStream
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.Proxy
import java.net.URL
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class FursClientJavaNet(private val config: FursConfig, private val proxy: Proxy? = null) {
    internal fun openConnection(url: URL): HttpsURLConnection {
        if (!url.protocol.equals("https", true))
            throw IllegalArgumentException("Expected a HTTPS URL")

        val conn = if (proxy != null) {
            url.openConnection(proxy)
        } else {
            url.openConnection()
        }

        conn as HttpsURLConnection

        conn.sslSocketFactory = config.sslContext.socketFactory
        conn.hostnameVerifier = HostnameVerifier { hostname, _ ->
            hostname.equals(url.host, true)
        }

        return conn
    }

    internal fun exchangeJson(url: URL, request: FursRequest): FursResponse {
        val conn = openConnection(url)
        conn.setRequestProperty("content-type", "application/json; charset=UTF-8")
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.doInput = true

        JSON.writeJson(request, conn.outputStream)

        return JSON.parse(conn.inputStream, FursResponse::class.java)
    }

    internal fun exchangeJsonAsToken(url: URL, request: FursRequest): FursResponse {
        val tokenEnvelope = wrapInTokenForm(request)

        val conn = openConnection(url)
        conn.setRequestProperty("content-type", "application/json; charset=UTF-8")
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.doInput = true

        JSON.writeJson(tokenEnvelope, conn.outputStream)

        val responseToken = JSON.parse(conn.inputStream, TokenEnvelope::class.java)

        // TODO: validate signature

        val firstDot = responseToken.token.indexOf('.')
        val secondDot = responseToken.token.indexOf('.', firstDot + 1)
        val payloadPart = responseToken.token.substring(firstDot + 1, secondDot)
        val decodedPayload = Base64.getUrlDecoder().decode(payloadPart)

        System.err.println("---------------------------------")
        System.err.println(decodedPayload.toString(UTF_8))
        System.err.println("---------------------------------")

        return JSON.parse(ByteArrayInputStream(decodedPayload), FursResponse::class.java)
    }

    fun wrapInTokenForm(payload: FursRequest): TokenEnvelope {
        val jwsHeader = config.makeJwsHeader(alg = "RS256")
        val jwsHeaderBase64 = Base64.getUrlEncoder().encodeToString(JSON.stringify(jwsHeader).toByteArray(UTF_8))

        val payload = JSON.stringify(payload)
        System.out.println("---------------------------------")
        System.out.println(payload)
        System.out.println("---------------------------------")

        val payloadBase64 = Base64.getUrlEncoder().encodeToString(payload.toByteArray(UTF_8))

        val sb = StringBuilder(jwsHeaderBase64.length + payloadBase64.length + 200)
        sb.append(jwsHeaderBase64)
        sb.append('.')
        sb.append(payloadBase64)

        val signature = computeRS256(sb.toString().toByteArray(UTF_8), config.privateKey)
        val signatureBase64 = Base64.getUrlEncoder().encodeToString(signature)
        sb.append('.')
        sb.append(signatureBase64)

        return TokenEnvelope(sb.toString())
    }

    fun echo(message: String): String {
        val req = FursRequest(echoRequest = message)
        val res = exchangeJson(config.env.echoUrl, req)
        return res.echoResponse ?: throw IllegalStateException("No response in the message received")
    }

    fun invoice(request: InvoiceRequest): String {
        val response = exchangeJsonAsToken(config.env.invoicesUrl, FursRequest(invoiceRequest = request))

        return response.invoiceResponse?.uniqueInvoiceId ?:
            throw response.invoiceResponse?.error?.toException("Missing uniqueInvoiceId in response") ?:
                  FursException(null, "Missing both uniqueInvoiceId and error in response")
    }
}
