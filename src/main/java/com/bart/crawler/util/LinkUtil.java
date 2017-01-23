package com.bart.crawler.util;

import java.net.URI;

/**
 * Created by bart on 22/01/2017.
 */
public class LinkUtil {

    /**
     * Checks if domain is equals to domain of provided uri.
     * <p>
     * <b>WARNING:</b> This is simple implementation that strictly compare domains but doesn't treat a subdomain as part of the same domain.
     *
     * @param domain
     * @param uri
     * @return
     */
    public static boolean isTheSameDomain(URI domain, URI uri) {
        if (domain == null) {
            throw new IllegalArgumentException("Domain cannot be null!");
        }
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null!");
        }
        //this is inefficient
        return domain.getHost().equals(uri.getHost());
    }
}
