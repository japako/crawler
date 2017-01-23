package com.bart.crawler.model;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bart on 22/01/2017.
 */
public class Link {
    private URI uri;

    public Link(String uri) throws URISyntaxException {
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        this.uri = new URI(uri);
    }

    public Link(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        return uri.equals(link.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public String toString() {
        return "Link{" +
                "uri=" + uri +
                '}';
    }
}
