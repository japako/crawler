package com.bart.crawler.model;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by bart on 22/01/2017.
 */
public class CrawlLinkStorage {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock read = readWriteLock.readLock();
    private final Lock write = readWriteLock.writeLock();

    private Map<Link, Boolean> crawled = new ConcurrentHashMap<>();
    private Map<Link, Boolean> awaiting = new ConcurrentHashMap<>();

    public void addLink(Link link) {
        write.lock();
        if (!crawled.containsKey(link)) {
            if (!awaiting.containsKey(link) || Boolean.FALSE.equals(awaiting.get(link))) {
                awaiting.put(link, Boolean.FALSE);
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
            if (Boolean.FALSE.equals(entry.getValue())) {
                next = entry.getKey();
                awaiting.put(next, Boolean.TRUE);
                break;
            }
        }
        write.unlock();
        return next;
    }


    public void addLinkToCrawled(Link link) {
        write.lock();
        awaiting.remove(link);
        crawled.put(link, Boolean.FALSE);
        write.unlock();
    }

    public int getAwaitingNumber() {
        read.lock();
        int result = awaiting.size();
        read.unlock();
        return result;
    }

    public Map<Link, Boolean> getAwaiting() {
        return Collections.unmodifiableMap(awaiting);
    }

    public Map<Link, Boolean> getCrawled() {
        return Collections.unmodifiableMap(crawled);
    }

    @Override
    public String toString() {
        return "CrawlLinkStorage{" +
                "crawled=" + crawled +
                ", awaiting=" + awaiting +
                '}';
    }
}
