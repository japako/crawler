package com.bart.crawler.storage;

import com.bart.crawler.model.Link;
import com.bart.crawler.util.URIUtil;

import java.net.URI;

/**
 * Created by bart on 23/01/2017.
 */
public class ExternalDomainSkipCrawlingMarker implements SkipCrawlingMarker {
    private URI domain;

    public ExternalDomainSkipCrawlingMarker(URI domain) {
        this.domain = domain;
    }

    @Override
    public boolean skipCrawling(Link link) {
        return !URIUtil.isTheSameDomain(domain, link.getUri());
    }
}
