package com.bart.crawler.model;

import java.net.URISyntaxException;

/**
 * Created by bart on 22/01/2017.
 */
public class PageLink extends Link {

    public PageLink(String uri) throws URISyntaxException {
        super(uri);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
