package com.github.mslenc.furslib

import com.github.mslenc.furslib.dto.*
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo
import java.security.*
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class FursConfig(val env: FursEnv, val sslContext: SSLContext, val privateKey: PrivateKey, val clientCert: X509Certificate) {
    val subjectName = clientCert.subjectDN.toString()
    val issuerName = clientCert.issuerDN.toString()
    val serial = clientCert.serialNumber

    fun makeJwsHeader(alg: String = "RS256"): JwsHeader {
        return JwsHeader(
            alg = alg,
            subjectName = subjectName,
            issuerName = issuerName,
            serial = serial
        )
    }

    companion object {
        fun createFromPem(pem: ByteArray, password: CharArray, env: FursEnv): FursConfig {
            val pemParser = PEMParser(pem.toReader())

            val certs = ArrayList<X509Certificate>()
            val keys = ArrayList<PrivateKey>()
            while (true) {
                val obj = pemParser.readObject() ?: break

                if (obj is X509CertificateHolder) {
                    certs.add(x509CertFactory.generateCertificate(obj.encoded.toStream()) as X509Certificate)
                } else
                if (obj is PKCS8EncryptedPrivateKeyInfo) {
                    val decryptor =
                        JceOpenSSLPKCS8DecryptorProviderBuilder().
                            setProvider("BC").
                            build(password)

                    val privateKey =
                        JcaPEMKeyConverter().
                            setProvider("BC").
                            getPrivateKey(obj.decryptPrivateKeyInfo(decryptor))

                    keys.add(privateKey)
                }
            }

            val privateKey = keys.single()
            val clientCert = certs.single(env.isClientCert::test)

            val keyStore = KeyStore.getInstance("JKS")
            keyStore.load(null, password)

            for ((index, cert) in (certs + env.rootCerts).withIndex())
                keyStore.setCertificateEntry("cert$index", cert)

            keyStore.setKeyEntry("theKey", keys.single(), password, certs.toTypedArray())

            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, password)

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)

            return FursConfig(env, sslContext, privateKey, clientCert)
        }
    }
}

