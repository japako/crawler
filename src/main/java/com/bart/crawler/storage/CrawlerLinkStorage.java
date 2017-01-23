package com.bart.crawler.storage;


import com.bart.crawler.model.Link;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by bart on 22/01/2017.
 */
public class CrawlerLinkStorage {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock read = readWriteLock.readLock();
    private final Lock write = readWriteLock.writeLock();

    private Map<Link, Boolean> crawled = new ConcurrentHashMap<>();
    private Map<Link, Boolean> awaiting = new ConcurrentHashMap<>();

    public void addLink(Link link) {
        write.lock();
        if (!crawled.containsKey(link)) {
            if (!awaiting.containsKey(link) || FALSE.equals(awaiting.get(link))) {
                awaiting.put(link, FALSE);
            }
        }
        write.unlock();
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


    public void addCrawledLink(Link link) {
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
