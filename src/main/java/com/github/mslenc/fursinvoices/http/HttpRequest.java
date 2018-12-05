package com.github.mslenc.fursinvoices.http;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.util.Map;

public class HttpRequest {
    private final URL url;
    private final String method;
    private final SSLContext sslContext;
    private final Map<String, String> headers;
    private final byte[] content;

    public HttpRequest(URL url, String method, SSLContext sslContext, Map<String, String> headers, byte[] content) {
        this.url = url;
        this.method = method;
        this.sslContext = sslContext;
        this.headers = headers;
        this.content = content;
    }

    /**
     * The URL to connect to (will always be a HTTPS URL)
     */
    public URL getUrl() {
        return url;
    }

    /**
     * The HTTP method to use.
     */
    public String getMethod() {
        return method;
    }

    /**
     * The SSL context to use.
     */
    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * The request headers to set.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * The body of the request.
     */
    public byte[] getContent() {
        return content;
    }
}
