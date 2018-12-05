package com.github.mslenc.fursinvoices;

import com.github.mslenc.fursinvoices.dto.BusinessPremise;
import com.github.mslenc.fursinvoices.dto.FursHeader;
import com.github.mslenc.fursinvoices.dto.Invoice;
import com.github.mslenc.fursinvoices.dto.InvoiceRequest;
import com.github.mslenc.fursinvoices.http.HttpClientAsync;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FursClientAsync {
    CompletableFuture<String> echo(String message);
    CompletableFuture<UUID> invoice(InvoiceRequest request);
    CompletableFuture<Void> businessPremise(BusinessPremise request);

    default CompletableFuture<UUID> invoice(Invoice invoice) {
        return invoice(new InvoiceRequest(new FursHeader(), invoice));
    }

    static FursClientAsync create(FursConfig config, HttpClientAsync httpClient) {
        return new FursClientAsyncImpl(config, httpClient);
    }
}
