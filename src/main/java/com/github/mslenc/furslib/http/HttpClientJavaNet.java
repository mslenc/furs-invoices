package com.github.mslenc.furslib.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.util.Map;

import static com.github.mslenc.furslib.Utils.readFully;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpClientJavaNet implements HttpClient {
    private final Proxy proxy;

    public HttpClientJavaNet() {
        this(null);
    }

    public HttpClientJavaNet(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public byte[] execute(HttpRequest request) throws IOException {
        HttpsURLConnection conn;

        if (proxy != null) {
            conn = (HttpsURLConnection) request.getUrl().openConnection(proxy);
        } else {
            conn = (HttpsURLConnection) request.getUrl().openConnection();
        }

        conn.setSSLSocketFactory(request.getSslContext().getSocketFactory());
        conn.setHostnameVerifier((hostName, sslSession) -> hostName.equalsIgnoreCase(request.getUrl().getHost()));

        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        conn.setRequestMethod(request.getMethod());

        conn.setDoInput(true);

        if (request.getContent() != null) {
            conn.setRequestProperty("content-length", String.valueOf(request.getContent().length));

            conn.setDoOutput(true);

            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(request.getContent());
            }
        }

        if (conn.getResponseCode() >= 400) {
            byte[] error;
            try (InputStream errorStream = conn.getErrorStream()) {
                error = readFully(errorStream);
            }

            throw new IOException("Response had status " + conn.getResponseCode() + ":\n" + new String(error, UTF_8));
        }

        if (conn.getResponseCode() >= 300) {
            throw new IOException("Response had status " + conn.getResponseCode());
        }

        try (InputStream inputStream = conn.getInputStream()) {
            return readFully(inputStream);
        }
    }
}
