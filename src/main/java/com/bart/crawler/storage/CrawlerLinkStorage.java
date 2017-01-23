package com.bart.crawler.storage;


import com.bart.crawler.model.Link;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import static java.lang.Boolean.*;

/**
 * Created by bart on 22/01/2017.
 */
public class CrawlerLinkStorage {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock read = readWriteLock.readLock();
    private final Lock write = readWriteLock.writeLock();

    private Map<Link, Boolean> crawled = new ConcurrentHashMap<>();
    private Map<Link, Boolean> awaiting = new ConcurrentHashMap<>();

    private List<SkipCrawlingMarker> markers;

    public CrawlerLinkStorage(List<SkipCrawlingMarker> markers) {
        this.markers = markers;
    }

    public void addLink(Link link) {
        boolean skipCrawling = skipCrawl(link);

        write.lock();
        if (!crawled.containsKey(link)) {
            if(skipCrawling) {
                crawled.put(link, FALSE);
            } else if (!awaiting.containsKey(link) || FALSE.equals(awaiting.get(link))) {
                awaiting.put(link, FALSE);
            }
        }
        write.unlock();

    }

    public void addLinks(List<Link> links) {
        for (Link link : links) {
            addLink(link);
        }
    }


    public Link retrieveAwaiting() {
        write.lock();
        Iterator<Map.Entry<Link, Boolean>> iterator = awaiting.entrySet().iterator();
        Map.Entry<Link, Boolean> entry = null;
        Link next = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (FALSE.equals(entry.getValue())) {
                next = entry.getKey();
                awaiting.put(next, TRUE);
                break;
            }
        }
        write.unlock();
        return next;
    }


    public void addLinkToCrawled(Link link) {
        write.lock();
        awaiting.remove(link);
        crawled.put(link, FALSE);
        write.unlock();
    }

    public int getAwaitingNumber() {
        read.lock();
        int result = awaiting.size();
        read.unlock();
        return result;
    }

    private boolean skipCrawl(Link link) {
        for(SkipCrawlingMarker marker: markers) {
            if(marker.skipCrawling(link)) {
                return true;
            }
        }
        return false;
    }

    public Map<Link, Boolean> getAwaiting() {
        return Collections.unmodifiableMap(awaiting);
    }

    public Map<Link, Boolean> getCrawled() {
        return Collections.unmodifiableMap(crawled);
    }

    @Override
    public String toString() {
        return "CrawlerLinkStorage{" +
                "crawled=" + crawled +
                ", awaiting=" + awaiting +
                '}';
    }
}
