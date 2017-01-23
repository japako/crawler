package com.bart.crawler;

import com.bart.crawler.storage.*;
import com.bart.crawler.model.Link;
import com.bart.crawler.model.LinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bart on 22/01/2017.
 */
@Service
@Scope("prototype")
public class Crawler {
    private final static Logger logger = LoggerFactory.getLogger(Crawler.class);

    private LinkProcessor linkProcessor;

    @Autowired
    private ApplicationContext applicationContext;

//    @Autowired
//    List<SkipCrawlingMarker> basicMarkers;

    @Value("${crawler.batch.size:10}")
    private int batchSize;
    @Value("${crawler.pool.size:2}")
    private int poolSize ;
    @Value("${crawler.sleep.time:15000}")
    private long sleep;


    public Crawler(LinkProcessor linkProcessor) {
        this.linkProcessor = linkProcessor;
    }

    public void init() {

    }

    public Set<Link> execute(String url) throws Exception {
        Link link = createLink(url);
        CrawlerStorageAdapter storageAdapter = new CrawlerStorageAdapter(new CrawlerLinkStorage(), prepareMarkers(link));

        //add first link
        storageAdapter.addLink(link);
        runIterations(storageAdapter);

        return storageAdapter.getCrawledLinks();
    }

    private void runIterations(CrawlerStorageAdapter storageAdapter) throws InterruptedException {
        logger.info("Running Crawler Iterations");
        logger.info("batchSize: {} poolSize: {} sleep time after iteration: {}", batchSize, poolSize, sleep);

        ExecutorService executorService;
        int i = 0;
        while (storageAdapter.hasAwaitingLinks()) {
            List<CrawlerCallable> batch = createBatch(storageAdapter, batchSize);
            logger.info("Iteration: {} batch size: {}", ++i, batch.size());
            executorService = Executors.newFixedThreadPool(poolSize);
            executorService.invokeAll(batch);
            executorService.shutdown();
            Thread.sleep(sleep);
        }
    }

    private Link createLink(String url) {
        try {
            return new Link(url, LinkType.PAGE);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid url: " + url + " given!");
        }

    }

    private List<SkipCrawlingMarker> prepareMarkers(Link link) {
        List<SkipCrawlingMarker> markers = new ArrayList<>();
        markers.addAll((List<SkipCrawlingMarker>) applicationContext.getBean("basicMarkers"));
        markers.add(new ExternalDomainSkipCrawlingMarker(link.getUri()));
        return  markers;

    }

    private List<CrawlerCallable> createBatch(CrawlerStorageAdapter adapter, int batchSize) {
        List<CrawlerCallable> iterations = new ArrayList<>(batchSize);
        Link currentLink;
        for (int i = 0; i < batchSize; i++) {
            currentLink = adapter.retrieveAwaiting();
            if (currentLink == null) {
                break;
            }
            iterations.add(new CrawlerCallable(currentLink, linkProcessor, adapter));
        }
        return iterations;
    }

}
