package com.bart.crawler.storage;

import com.bart.crawler.model.Link;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by bart on 23/01/2017.
 */
public class CrawlerStorageAdapter {

    private CrawlerLinkStorage storage;
    private List<SkipCrawlingMarker> markers;

    public CrawlerStorageAdapter(CrawlerLinkStorage storage, List<SkipCrawlingMarker> markers) {
        this.storage = storage;
        this.markers = markers;
    }

    public void addCrawledLink(Link link) {
        storage.addCrawledLink(link);
    }

    public void addLinks(List<Link> links) {
        for(Link link : links ) {
            addLink(link);
        }
    }

    public void addLink(Link link) {
        if(skipCrawl(link, markers)) {
            storage.addCrawledLink(link);
        } else {
            storage.addLink(link);
        }
    }

    public Link retrieveAwaiting() {
        return storage.retrieveAwaiting();
    }

    public boolean hasAwaitingLinks() {
        return storage.getAwaitingNumber() > 0;
    }

    private boolean skipCrawl(Link link, List<SkipCrawlingMarker> markers) {
        for(SkipCrawlingMarker marker: markers) {
            if(marker.skipCrawling(link)) {
                return true;
            }
        }
        return false;
    }

    public Set<Link> getCrawledLinks() {
        return Collections.unmodifiableSet(storage.getCrawled().keySet());
    }
}
