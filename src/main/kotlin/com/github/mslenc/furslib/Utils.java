package com.github.mslenc.furslib;

import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Utils {
    static <T> T checkIfNull(T value, String propName) {
        if (value == null) {
            throw new IllegalArgumentException("null " + propName);
        } else {
            return value;
        }
    }

    public static byte[] readFully(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];

        int offset = 0;
        while (true) {
            if (offset >= bytes.length) {
                int newSize;
                if (bytes.length <= Integer.MAX_VALUE / 2) {
                    newSize = bytes.length * 2;
                } else {
                    newSize = Integer.MAX_VALUE;
                    if (newSize == bytes.length)
                        throw new IOException("Response too long (over 2 GiB)");
                }

                bytes = Arrays.copyOf(bytes, newSize);
            }

            int bytesRead = is.read(bytes, offset, bytes.length - offset);
            if (bytesRead < 0) {
                if (bytes.length != offset) {
                    return Arrays.copyOf(bytes, offset);
                } else {
                    return bytes;
                }
            } else {
                offset += bytesRead;
            }
        }
    }

    static byte[] readResourceBytes(String path) throws IOException {
        return readFully(Utils.class.getResourceAsStream(path));
    }

    static final CertificateFactory x509CertFactory;
    static {
        try {
            x509CertFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new IllegalStateException(e);
        }
    }

    static List<X509Certificate> readCertsFromResources(String pathPrefix, String... fileNames) throws IOException, CertificateException {
        ArrayList<X509Certificate> result = new ArrayList<>();

        for (String fileName : fileNames) {
            String fullPath = pathPrefix + fileName;
            byte[] pemBytes = readResourceBytes(fullPath);
            result.add((X509Certificate) x509CertFactory.generateCertificate(new ByteArrayInputStream(pemBytes)));
        }

        return result;
    }

    static Predicate<X509Certificate> containsOU(String expected) {
        return certificate -> {
            X500Name subject;
            try {
                subject = new JcaX509CertificateHolder(certificate).getSubject();
            } catch (CertificateEncodingException e) {
                throw new IllegalStateException(e);
            }

            if (subject == null)
                return false;

            RDN[] OUs = subject.getRDNs(BCStyle.OU);
            if (OUs == null || OUs.length == 0)
                return false;

            for (RDN OU : OUs) {
                AttributeTypeAndValue first = OU.getFirst();
                if (first.getValue() instanceof ASN1String) {
                    String value = ((ASN1String) first.getValue()).getString();
                    if (expected.equals(value)) {
                        return true;
                    }
                }
            }

            return false;
        };
    }

    public static String formatDateTimeForZoi(LocalDateTime dateTime) {
        String time = dateTime.toLocalTime().toString();

        if (time.length() < 8) {
            time += ":00";
        } else
        if (time.length() > 8) {
            time = time.substring(0, 8);
        }

        return dateTime.toLocalDate() + " " + time;
    }

    public static byte[] computeRS256(byte[] content, PrivateKey key) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(content);
            return signature.sign();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}
