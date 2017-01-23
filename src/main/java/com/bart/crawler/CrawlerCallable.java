package com.bart.crawler;

import com.bart.crawler.model.CrawlLinkStorage;
import com.bart.crawler.model.Link;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by bartj on 23/01/2017.
 */
public class CrawlerCallable implements Callable<Void> {

    private LinkProcessor linkProcessor;
    private CrawlLinkStorage storage;
    private Link currentLink;

    public CrawlerCallable(Link currentLink, LinkProcessor linkProcessor, CrawlLinkStorage storage) {
        this.linkProcessor = linkProcessor;
        this.storage = storage;
        this.currentLink = currentLink;
    }


    public void processAndUpdateStorageWithLinks(Link currentLink, CrawlLinkStorage storage) {
        List<Link> links = linkProcessor.execute(currentLink);
        storage.addLinkToCrawled(currentLink);
        storage.addLinks(links);
    }

    @Override
    public Void call() throws Exception {
        processAndUpdateStorageWithLinks(currentLink, storage);
        return null;
    }
}