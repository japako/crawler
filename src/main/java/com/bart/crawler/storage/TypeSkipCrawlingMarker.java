package com.bart.crawler.storage;

import com.bart.crawler.model.Link;
import com.bart.crawler.model.LinkType;

/**
 * Created by bart on 23/01/2017.
 */
public class TypeSkipCrawlingMarker implements SkipCrawlingMarker {

    @Override
    public boolean skipCrawling(Link link) {
        return !LinkType.PAGE.equals(link.getLinkType());
    }
}
