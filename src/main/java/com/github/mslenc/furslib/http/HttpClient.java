package com.github.mslenc.furslib.http;

import java.io.IOException;

public interface HttpClient {
    byte[] execute(HttpRequest request) throws IOException;
}
