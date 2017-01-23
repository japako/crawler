package com.bart.crawler;

import com.bart.crawler.model.CrawlLinkStorage;
import com.bart.crawler.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bart on 22/01/2017.
 */
public class CrawlerStrategy {
    private final static Logger logger = LoggerFactory.getLogger(CrawlerStrategy.class);
    private LinkProcessor linkProcessor = new LinkProcessor();
    private AtomicInteger iterationCounter = new AtomicInteger(0);

    private int batchSize = 10;
    private int poolSize = 2;
    private long sleep = 15 * 1000;

    CrawlLinkStorage storage = new CrawlLinkStorage();

    public CrawlerStrategy(String url) {
        try {
            storage.addLink(new Link(url));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid url: " + url + " given!");
        }

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

    public CrawlLinkStorage getStorage() {
        return storage;
    }


    private List<CrawlerCallable> createBatch(CrawlLinkStorage storage, int batchSize) {
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
