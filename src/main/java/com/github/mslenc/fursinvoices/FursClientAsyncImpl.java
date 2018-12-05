package com.github.mslenc.fursinvoices;

import com.github.mslenc.fursinvoices.dto.*;
import com.github.mslenc.fursinvoices.http.HttpClientAsync;
import com.github.mslenc.fursinvoices.http.HttpRequest;

import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.github.mslenc.fursinvoices.Utils.checkIfNull;
import static java.util.Collections.singletonMap;

class FursClientAsyncImpl extends AbstractFursClientImpl implements FursClientAsync {
    private final HttpClientAsync httpClient;

    FursClientAsyncImpl(FursConfig config, HttpClientAsync httpClient) {
        super(config);

        this.httpClient = checkIfNull(httpClient, "httpClient");
    }

    private CompletableFuture<byte[]> safelyHttpExec(HttpRequest request) {
        CompletableFuture<byte[]> httpPromise;

        try {
            httpPromise = httpClient.execute(request);
            if (httpPromise == null)
                throw new NullPointerException("null future returned from httpClient");
            return httpPromise;
        } catch (Throwable t) {
            CompletableFuture<byte[]> failure = new CompletableFuture<>();
            failure.completeExceptionally(t);
            return failure;
        }
    }

    private CompletableFuture<FursResponse> exchangeJson(URL url, FursRequest request) {
        CompletableFuture<FursResponse> promise = new CompletableFuture<>();

        HttpRequest httpRequest;
        try {
            byte[] content = JSON.byteify(request);
            httpRequest = new HttpRequest(url, "POST", config.getSslContext(), jsonHeaders, content);
        } catch (Throwable t) {
            promise.completeExceptionally(t);
            return promise;
        }

        safelyHttpExec(httpRequest).whenComplete((responseBytes, error) -> {
            if (error != null) {
                promise.completeExceptionally(error);
                return;
            }

            FursResponse result;
            try {
                result = JSON.parse(responseBytes, FursResponse.class);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
                return;
            }

            promise.complete(result);
        });

        return promise;
    }

    private CompletableFuture<FursResponse> exchangeJsonAsToken(URL url, FursRequest request) {
        CompletableFuture<FursResponse> promise = new CompletableFuture<>();

        HttpRequest httpRequest;
        try {
            TokenEnvelope tokenEnvelope = wrapInTokenForm(request);
            byte[] content = JSON.byteify(tokenEnvelope);
            httpRequest = new HttpRequest(url, "POST", config.getSslContext(), jsonHeaders, content);
        } catch (Throwable t) {
            promise.completeExceptionally(t);
            return promise;
        }

        safelyHttpExec(httpRequest).whenComplete((responseBytes, error) -> {
            if (error != null) {
                promise.completeExceptionally(error);
                return;
            }

            FursResponse result;

            try {
                TokenEnvelope responseToken = JSON.parse(responseBytes, TokenEnvelope.class);
                byte[] payload = verifySigAndExtractPayload(responseToken.getToken());
                result = JSON.parse(payload, FursResponse.class);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
                return;
            }

            promise.complete(result);
        });

        return promise;
    }

    @Override
    public CompletableFuture<String> echo(String message) {
        CompletableFuture<String> promise = new CompletableFuture<>();

        FursRequest req;
        URL url;
        try {
            req = new FursRequest(message);
            url = config.getEnv().getEchoUrl();
        } catch (Throwable t) {
            promise.completeExceptionally(t);
            return promise;
        }

        exchangeJson(url, req).whenComplete((response, error) -> {
            if (error != null) {
                promise.completeExceptionally(error);
                return;
            }

            String res;
            try {
                res = extractEchoResponse(response);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
                return;
            }

            promise.complete(res);
        });

        return promise;
    }

    @Override
    public CompletableFuture<Void> businessPremise(BusinessPremise request) {
        CompletableFuture<Void> promise = new CompletableFuture<>();

        FursRequest req;
        URL url;
        try {
            BusinessPremiseRequest wrapper = new BusinessPremiseRequest(request);
            req = new FursRequest(wrapper);
            url = config.getEnv().getPremisesUrl();
        } catch (Throwable t) {
            promise.completeExceptionally(t);
            return promise;
        }

        exchangeJsonAsToken(url, req).whenComplete((response, error) -> {
            if (error != null) {
                promise.completeExceptionally(error);
                return;
            }

            try {
                checkBusinessPremiseResponse(response);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
                return;
            }

            promise.complete(null);
        });

        return promise;
    }

    @Override
    public CompletableFuture<UUID> invoice(InvoiceRequest request) {
        CompletableFuture<UUID> promise = new CompletableFuture<>();

        FursRequest req;
        URL url;
        try {
            checkInvoiceRequest(request);
            req = new FursRequest(request);
            url = config.getEnv().getInvoicesUrl();
        } catch (Throwable t) {
            promise.completeExceptionally(t);
            return promise;
        }

        exchangeJsonAsToken(url, req).whenComplete((response, error) -> {
            if (error != null) {
                promise.completeExceptionally(error);
                return;
            }

            UUID res;
            try {
                res = extractInvoiceResponse(response);
            } catch (Throwable t) {
                promise.completeExceptionally(t);
                return;
            }

            promise.complete(res);
        });

        return promise;
    }

    private static final Map<String, String> jsonHeaders = singletonMap("content-type", "application/json; charset=UTF-8");
}
