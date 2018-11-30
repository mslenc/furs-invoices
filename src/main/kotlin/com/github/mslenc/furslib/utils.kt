package com.github.mslenc.furslib

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.bouncycastle.asn1.ASN1String
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import java.io.*
import java.lang.ArithmeticException
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.time.LocalDateTime
import java.util.function.Predicate

fun ByteArray.toStream(): ByteArrayInputStream {
    return ByteArrayInputStream(this)
}

fun ByteArray.toReader(charset: Charset = StandardCharsets.UTF_8): Reader {
    return InputStreamReader(toStream(), charset)
}

object JSON {
    private val jsonMapper = ObjectMapper()

    init {
        jsonMapper.registerModule(KotlinModule())
        jsonMapper.registerModule(ParameterNamesModule())
        jsonMapper.registerModule(Jdk8Module())
        jsonMapper.registerModule(JavaTimeModule())

        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    fun stringify(value: Any): String {
        return jsonMapper.writeValueAsString(value)
    }

    fun writeJson(value: Any, out: OutputStream) {
        jsonMapper.writeValue(out, value)
    }

    fun <T: Any> parse(value: String, klass: Class<T>): T {
        return jsonMapper.readValue(value, klass) as T
    }

    fun <T: Any> parse(input: InputStream, klass: Class<T>): T {
        return jsonMapper.readValue(input, klass) as T
    }
}

val x509CertFactory = CertificateFactory.getInstance("X.509")!!

fun readCertsFromResources(pathPrefix: String, vararg fileNames: String): List<X509Certificate> {
    return fileNames.map { fileName ->
        val pemBytes = (pathPrefix + fileName).readResourceBytes()
        x509CertFactory.generateCertificate(pemBytes.toStream()) as X509Certificate
    }
}

fun String.readResourceBytes(): ByteArray {
    return this.javaClass::class.java.getResource(this).readBytes()
}

fun containsOU(expected: String): Predicate<X509Certificate> {
    return Predicate {
        val subject = JcaX509CertificateHolder(it).subject ?: return@Predicate false
        val OUs = subject.getRDNs(BCStyle.OU) ?: return@Predicate false
        for (ou in OUs) {
            if ((ou.first?.value as? ASN1String)?.string == expected) {
                return@Predicate true
            }
        }
        false
    }
}


fun validate20CharTaxNumber(taxNumber: String, propertyName: String): String {
    if (taxNumber.length !in 1..20)
        throw IllegalArgumentException("$propertyName must be 1-20 characters")

    for (i in 0 until taxNumber.length)
        taxNumber[i].let {
            if (!it.isValidVatIdChar())
                throw IllegalArgumentException("$propertyName can only contain ASCII letters, digits and hyphens")
        }

    return taxNumber
}

private fun Char.isValidVatIdChar(): Boolean {
    return (this in 'a'..'z') || (this in 'A'..'Z') || (this in '0'..'9')
}

fun validateZoiFormat(zoi: String?): String {
    if (zoi == null)
        throw IllegalArgumentException("null protectedId")

    if (zoi.length != 32)
        throw IllegalArgumentException("illegal protectedId length - it must be 32")

    for (i in 0..31)
        if (!zoi[i].isHexChar())
            throw IllegalArgumentException("illegal protectedId length - it can only contain hexadecimal characters")

    return zoi
}

private fun Char.isHexChar(): Boolean {
    return (this in 'a'..'f') || (this in 'A'..'F') || (this in '0'..'9')
}

fun formatDateTimeForZoi(dateTime: LocalDateTime): String {
    val time = dateTime.toLocalTime().toString()
    val normalTime = when {
        time.length > 8 -> time.substring(0, 8)
        time.length < 8 -> "$time:00"
        else -> time
    }

    return dateTime.toLocalDate().toString() + " " + normalTime
}

fun computeRS256(content: ByteArray, key: PrivateKey): ByteArray {
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(key)
    signature.update(content)
    return signature.sign()
}

