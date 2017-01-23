package com.bart.crawler.model;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bart on 22/01/2017.
 */
public class Link {
    private LinkType linkType;
    private URI uri;

    public Link(String uri, LinkType linkType ) throws URISyntaxException {
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        this.uri = new URI(uri);
        this.linkType = linkType;
    }

    public Link(URI uri, LinkType linkType) {
        this.uri = uri;
        this.linkType = linkType;
    }

    public URI getUri() {
        return uri;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        if (linkType != link.linkType) return false;
        return uri.equals(link.uri);
    }

    @Override
    public int hashCode() {
        int result = linkType.hashCode();
        result = 31 * result + uri.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Link{" +
                "linkType=" + linkType +
                ", uri=" + uri +
                '}';
    }
}
