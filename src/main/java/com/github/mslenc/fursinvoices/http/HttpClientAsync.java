package com.github.mslenc.fursinvoices.http;

import java.util.concurrent.CompletableFuture;

/**
 * A simple interface for asynchronous (non-blocking) HTTP clients to implement,
 * to decouple FURS-specific logic from underlying transport library.
 */
public interface HttpClientAsync {
    /**
     * Should execute the request specified by <tt>request</tt>, returning
     * the response as a byte array (or throwing an IOException in case of
     * any errors). Well-behaved implementations will do at least the
     * following:
     * <ul>
     *     <li>Check that the HTTP status returned is 2xx</li>
     *     <li>Check that the host name in the server certificate matches the one in the request's URL</li>
     *     <li>Return any errors via the CompletableFuture, rather than throwing exceptions in calls to this method</li>
     * </ul>
     *
     * @param request the request to be made
     * @return the future that will receive the response bytes (or an error)
     */
    CompletableFuture<byte[]> execute(HttpRequest request);
}
