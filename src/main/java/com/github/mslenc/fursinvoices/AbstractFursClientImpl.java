package com.github.mslenc.fursinvoices;

import com.github.mslenc.fursinvoices.dto.*;

import java.security.GeneralSecurityException;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

import static com.github.mslenc.fursinvoices.Utils.checkIfNull;
import static com.github.mslenc.fursinvoices.Utils.computeRS256;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AbstractFursClientImpl {
    protected final FursConfig config;

    protected AbstractFursClientImpl(FursConfig config) {
        this.config = checkIfNull(config, "config");
    }

    protected byte[] verifySigAndExtractPayload(String token) throws FursException {
        if (token == null || token.length() < 2)
            throw new FursException("C002", "Server returned empty token");

        int firstDot = token.indexOf('.');
        int secondDot = token.indexOf('.', firstDot + 1);
        if (secondDot < 0)
            throw new FursException("C002", "The token returned is not in a valid format");

        String signedPart = token.substring(0, secondDot);
        String sigBase64 = token.substring(secondDot + 1);
        byte[] sigBytes;

        try {
            sigBytes = Base64.getUrlDecoder().decode(sigBase64);
        } catch (Exception e) {
            throw new FursException("C002", "The server signature could not be decoded", e);
        }

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(config.getEnv().getServerSignatureCert());
            signature.update(signedPart.getBytes(UTF_8));
            if (signature.verify(sigBytes)) {
                // TODO - check that jwsHeader contents match the certificate?
                String payloadBase64 = token.substring(firstDot + 1, secondDot);
                try {
                    return Base64.getUrlDecoder().decode(payloadBase64);
                } catch (Exception e) {
                    throw new FursException("C002", "The payload could not be base64-decoded", e);
                }
            } else {
                throw new FursException("C003", "The server signature was not valid");
            }
        } catch (GeneralSecurityException e) {
            throw new FursException("C003", "Signature verification failed", e);
        }
    }

    protected TokenEnvelope wrapInTokenForm(FursRequest payload) {
        JwsHeader jwsHeader = config.makeJwsHeader("RS256");
        String jwsHeaderBase64 = Base64.getUrlEncoder().encodeToString(JSON.byteify(jwsHeader));

        byte[] payloadBytes = JSON.byteify(payload);
        String payloadBase64 = Base64.getUrlEncoder().encodeToString(payloadBytes);

        StringBuilder sb = new StringBuilder(jwsHeaderBase64.length() + payloadBase64.length() + 350); // the encoded sig is 339 bytes
        sb.append(jwsHeaderBase64);
        sb.append('.');
        sb.append(payloadBase64);

        byte[] signature = computeRS256(sb.toString().getBytes(UTF_8), config.getPrivateKey());
        String signatureBase64 = Base64.getUrlEncoder().encodeToString(signature);
        sb.append('.');
        sb.append(signatureBase64);

        return new TokenEnvelope(sb.toString());
    }

    protected String extractEchoResponse(FursResponse response) throws FursException {
        String echoResponse = response.getEchoResponse();
        if (echoResponse == null)
            throw new FursException("C002", "No echo response in the message received");
        return echoResponse;
    }

    protected void checkInvoiceRequest(InvoiceRequest request) {
        checkIfNull(request, "request");
        checkIfNull(request.getInvoice(), "invoice");

        if (request.getHeader() == null)
            request.setHeader(new FursHeader());

        if (request.getInvoice().getProtectedId() == null)
            request.getInvoice().computeProtectedId(config);
    }

    protected UUID extractInvoiceResponse(FursResponse response) throws FursException {
        InvoiceResponse invoiceResponse = response.getInvoiceResponse();

        if (invoiceResponse != null && invoiceResponse.getUniqueInvoiceId() != null)
            return invoiceResponse.getUniqueInvoiceId();

        if (invoiceResponse != null && invoiceResponse.getError() != null)
            throw invoiceResponse.getError().toException("Missing uniqueInvoiceId in response");

        throw new FursException("C002", "Missing both uniqueInvoiceId and error in response");
    }

    protected void checkBusinessPremiseResponse(FursResponse response) throws FursException {
        BusinessPremiseResponse bpResponse = response.getBusinessPremiseResponse();

        if (bpResponse == null)
            throw new FursException("C002", "Missing businessPremiseResponse in response");

        if (bpResponse.getError() != null)
            throw bpResponse.getError().toException("Unknown error");
    }
}
