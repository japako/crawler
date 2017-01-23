package com.bart.crawler;

import com.bart.crawler.storage.CrawlerLinkStorage;
import com.bart.crawler.model.Link;
import com.bart.crawler.model.LinkType;
import com.bart.crawler.storage.ExternalDomainSkipCrawlingMarker;
import com.bart.crawler.storage.SkipCrawlingMarker;
import com.bart.crawler.storage.TypeSkipCrawlingMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bart on 22/01/2017.
 */
public class Crawler {
    private final static Logger logger = LoggerFactory.getLogger(Crawler.class);

    private LinkProcessor linkProcessor = new LinkProcessor();
    private AtomicInteger iterationCounter = new AtomicInteger(0);
    private CrawlerLinkStorage storage;

    private int batchSize = 10;
    private int poolSize = 2;
    private long sleep = 15 * 1000;

    public Crawler(String url) {
        Link link;
        try {
            link = new Link(url, LinkType.PAGE);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid url: " + url + " given!");
        }
        storage = new CrawlerLinkStorage(
                Arrays.asList(new ExternalDomainSkipCrawlingMarker(link.getUri()), new TypeSkipCrawlingMarker())
        );
        storage.addLink(link);
    }


    public void execute() throws Exception {
        if (iterationCounter.get() > 0) {
            throw new IllegalStateException("Crawler cannot be reused");
        }
        ExecutorService executorService = null;
        while (storage.getAwaitingNumber() > 0) {
            List<CrawlerCallable> batch = createBatch(storage, batchSize);

            logger.info("Iteration: {} batch size: {}", iterationCounter.incrementAndGet(), batch.size());

            executorService = Executors.newFixedThreadPool(poolSize);
            List<Future<Void>> results = executorService.invokeAll(batch);
            executorService.shutdown();
            Thread.sleep(sleep);
        }
    }

    public Set<Link> getLinks() {
        return Collections.unmodifiableSet(storage.getCrawled().keySet());
    }


    private List<CrawlerCallable> createBatch(CrawlerLinkStorage storage, int batchSize) {
        List<CrawlerCallable> iterations = new ArrayList<>(batchSize);
        Link currentLink = null;
        for (int i = 0; i < batchSize; i++) {
            currentLink = storage.retrieveAwaiting();
            if (currentLink == null) {
                break;
            }
            iterations.add(new CrawlerCallable(currentLink, linkProcessor, storage));
        }
        return iterations;
    }

}
