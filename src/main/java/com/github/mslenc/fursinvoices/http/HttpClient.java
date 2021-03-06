package com.github.mslenc.fursinvoices.http;

import java.io.IOException;

/**
 * A simple interface for HTTP clients to implement, to decouple FURS-specific
 * logic from underlying transport library.
 */
public interface HttpClient {
    /**
     * Should execute the request specified by <tt>request</tt>, returning
     * the response as a byte array (or throwing an IOException in case of
     * any errors). Well-behaved implementations will do at least the
     * following:
     * <ul>
     *     <li>Check that the HTTP status returned is 2xx</li>
     *     <li>Check that the host name in the server certificate matches the one in the request's URL</li>
     * </ul>
     *
     * @param request the request to be made
     * @return the response bytes
     * @throws IOException if an error occurs
     */
    byte[] execute(HttpRequest request) throws IOException;
}
