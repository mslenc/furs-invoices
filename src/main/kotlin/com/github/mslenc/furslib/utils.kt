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

