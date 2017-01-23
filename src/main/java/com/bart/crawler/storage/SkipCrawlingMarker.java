package com.bart.crawler.storage;

import com.bart.crawler.model.Link;

/**
 * Created by bart on 23/01/2017.
 */
public interface SkipCrawlingMarker {

    boolean skipCrawling(Link link);
}
