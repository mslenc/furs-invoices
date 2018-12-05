package com.github.mslenc.fursinvoices;

import com.github.mslenc.fursinvoices.dto.JwsHeader;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import static com.github.mslenc.fursinvoices.Utils.checkIfNull;
import static com.github.mslenc.fursinvoices.Utils.x509CertFactory;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FursConfig {
    private final FursEnv env;
    private final SSLContext sslContext;
    private final PrivateKey privateKey;
    private final String subjectName;
    private final String issuerName;
    private final BigInteger serialNumber;

    public FursConfig(FursEnv env, SSLContext sslContext, PrivateKey privateKey, X509Certificate clientCertificate) {
        this.env = checkIfNull(env, "env");
        this.sslContext = checkIfNull(sslContext, "sslContext");
        this.privateKey = checkIfNull(privateKey, "privateKey");

        checkIfNull(clientCertificate, "clientCertificate");
        this.subjectName = clientCertificate.getSubjectDN().toString();
        this.issuerName = clientCertificate.getIssuerDN().toString();
        this.serialNumber = clientCertificate.getSerialNumber();

        // sanity check
        Utils.computeRS256(new byte[] { 1, 2, 3 }, privateKey);
    }

    public JwsHeader makeJwsHeader(String alg) {
        checkIfNull(alg, "alg");

        return new JwsHeader(alg, subjectName, issuerName, serialNumber);
    }

    public FursEnv getEnv() {
        return env;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static FursConfig createFromPem(byte[] pem, char[] password, FursEnv env) throws IOException, CertificateException, OperatorCreationException, PKCSException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        checkIfNull(pem, "pem");
        checkIfNull(password, "password");
        checkIfNull(env, "env");

        PEMParser pemParser = new PEMParser(new InputStreamReader(new ByteArrayInputStream(pem), UTF_8));

        ArrayList<X509Certificate> certs = new ArrayList<>();
        ArrayList<PrivateKey> keys = new ArrayList<>();
        while (true) {
            Object obj = pemParser.readObject();
            if (obj == null)
                break;

            if (obj instanceof X509CertificateHolder) {
                X509CertificateHolder certHolder = (X509CertificateHolder) obj;
                InputStream inputStream = new ByteArrayInputStream(certHolder.getEncoded());
                Certificate certificate = x509CertFactory.generateCertificate(inputStream);
                certs.add((X509Certificate) certificate);
            } else
            if (obj instanceof PKCS8EncryptedPrivateKeyInfo) {
                PKCS8EncryptedPrivateKeyInfo pkcsInfo = (PKCS8EncryptedPrivateKeyInfo) obj;

                InputDecryptorProvider decryptor =
                    new JceOpenSSLPKCS8DecryptorProviderBuilder().
                        setProvider("BC").
                        build(password);

                PrivateKey privateKey =
                    new JcaPEMKeyConverter().
                        setProvider("BC").
                        getPrivateKey(pkcsInfo.decryptPrivateKeyInfo(decryptor));

                keys.add(privateKey);
            }
        }

        if (keys.size() != 1)
            throw new IllegalStateException("0 or more than 1 private key");
        PrivateKey privateKey = keys.get(0);

        X509Certificate clientCert = null;
        for (X509Certificate cert : certs) {
            if (env.getIsClientCert().test(cert)) {
                if (clientCert == null) {
                    clientCert = cert;
                } else {
                    throw new IllegalStateException("More than 1 client certificate");
                }
            }
        }
        if (clientCert == null)
            throw new IllegalStateException("0 client certificates");

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, password);

        int numCerts = 0;
        for (X509Certificate cert : certs)
            keyStore.setCertificateEntry("cert" + numCerts++, cert);
        for (X509Certificate cert : env.getRootCerts())
            keyStore.setCertificateEntry("cert" + numCerts++, cert);

        keyStore.setKeyEntry("theKey", privateKey, password, certs.toArray(new Certificate[0]));

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return new FursConfig(env, sslContext, privateKey, clientCert);
    }
}
