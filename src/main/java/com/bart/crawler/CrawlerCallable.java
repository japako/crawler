package com.bart.crawler;

import com.bart.crawler.storage.CrawlerLinkStorage;
import com.bart.crawler.model.Link;
import com.bart.crawler.storage.CrawlerStorageAdapter;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by bartj on 23/01/2017.
 */
public class CrawlerCallable implements Callable<Void> {

    private LinkProcessor linkProcessor;
    private CrawlerStorageAdapter adapter;
    private Link currentLink;

    public CrawlerCallable(Link currentLink, LinkProcessor linkProcessor, CrawlerStorageAdapter adapter) {
        this.linkProcessor = linkProcessor;
        this.adapter = adapter;
        this.currentLink = currentLink;
    }




    @Override
    public Void call() throws Exception {
        processAndUpdateStorageWithLinks(currentLink);
        return null;
    }

    private void processAndUpdateStorageWithLinks(Link currentLink) {
        List<Link> links = linkProcessor.execute(currentLink);
        adapter.addCrawledLink(currentLink);
        adapter.addLinks(links);
    }
}
