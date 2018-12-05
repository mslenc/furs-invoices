package com.github.mslenc.fursinvoices.http;

import java.io.IOException;

public interface HttpClient {
    byte[] execute(HttpRequest request) throws IOException;
}
