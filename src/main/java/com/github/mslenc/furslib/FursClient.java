package com.github.mslenc.furslib;

import com.github.mslenc.furslib.dto.BusinessPremise;
import com.github.mslenc.furslib.dto.FursHeader;
import com.github.mslenc.furslib.dto.Invoice;
import com.github.mslenc.furslib.dto.InvoiceRequest;
import com.github.mslenc.furslib.http.HttpClient;

import java.io.IOException;
import java.util.UUID;

public interface FursClient {
    String echo(String message) throws IOException, FursException;
    UUID invoice(InvoiceRequest request) throws IOException, FursException;
    void businessPremise(BusinessPremise request) throws IOException, FursException;

    default UUID invoice(Invoice invoice) throws IOException, FursException {
        return invoice(new InvoiceRequest(new FursHeader(), invoice));
    }

    static FursClient create(FursConfig config, HttpClient httpClient) {
        return new FursClientImpl(config, httpClient);
    }
}
