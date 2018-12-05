package com.github.mslenc.fursinvoices;

import com.github.mslenc.fursinvoices.dto.*;
import com.github.mslenc.fursinvoices.http.HttpClient;
import com.github.mslenc.fursinvoices.http.HttpRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static com.github.mslenc.fursinvoices.Utils.checkIfNull;
import static java.util.Collections.singletonMap;

class FursClientImpl extends AbstractFursClientImpl implements FursClient {
    private final HttpClient httpClient;

    FursClientImpl(FursConfig config, HttpClient httpClient) {
        super(config);

        this.httpClient = checkIfNull(httpClient, "httpClient");
    }

    private FursResponse exchangeJson(URL url, FursRequest request) throws IOException {
        byte[] content = JSON.byteify(request);
        HttpRequest httpRequest = new HttpRequest(url, "POST", config.getSslContext(), jsonHeaders, content);
        byte[] responseBytes = httpClient.execute(httpRequest);
        return JSON.parse(responseBytes, FursResponse.class);
    }

    private FursResponse exchangeJsonAsToken(URL url, FursRequest request) throws IOException, FursException {
        TokenEnvelope tokenEnvelope = wrapInTokenForm(request);
        byte[] content = JSON.byteify(tokenEnvelope);
        HttpRequest httpRequest = new HttpRequest(url, "POST", config.getSslContext(), jsonHeaders, content);
        byte[] responseBytes = httpClient.execute(httpRequest);
        TokenEnvelope responseToken = JSON.parse(responseBytes, TokenEnvelope.class);
        byte[] payload = verifySigAndExtractPayload(responseToken.getToken());
        return JSON.parse(payload, FursResponse.class);
    }

    @Override
    public String echo(String message) throws IOException, FursException {
        FursRequest req = new FursRequest(message);
        FursResponse res = exchangeJson(config.getEnv().getEchoUrl(), req);
        return extractEchoResponse(res);
    }

    @Override
    public void businessPremise(BusinessPremise request) throws IOException, FursException {
        BusinessPremiseRequest wrapper = new BusinessPremiseRequest(request);
        FursRequest req = new FursRequest(wrapper);
        FursResponse res = exchangeJsonAsToken(config.getEnv().getPremisesUrl(), req);
        checkBusinessPremiseResponse(res);
    }

    @Override
    public UUID invoice(InvoiceRequest request) throws IOException, FursException {
        checkInvoiceRequest(request);
        FursRequest req = new FursRequest(request);
        FursResponse res = exchangeJsonAsToken(config.getEnv().getInvoicesUrl(), req);
        return extractInvoiceResponse(res);
    }

    private static final Map<String, String> jsonHeaders = singletonMap("content-type", "application/json; charset=UTF-8");
}
