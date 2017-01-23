package com.bart.crawler;

import com.bart.crawler.model.Link;
import com.bart.crawler.parser.Parser;
import com.bart.crawler.util.LinkUtil;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bart on 21/01/2017.
 */
public class LinkProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LinkProcessor.class);
    public Parser parser = new Parser();

    public List<Link> execute(Link current) {
        String url = current.getUri().toString();
        logger.info("Crawling and parsing url: {}", url);
        List<Link> links = null;
        try {
            String content = retrieve(url);
            links = parse(content, url);
            return removeOtherDomains(links, current);
        } catch (IOException | URISyntaxException e) {
            logger.error("Couldn't retrieve/parse url: {} ", url);
            links = Collections.emptyList();
        }
        return links;
    }

    protected String retrieve(String url) throws IOException {
        Content content = Request.Get(url).execute().returnContent();
        return content.toString();
    }

    protected List<Link> parse(String content, String url) throws URISyntaxException {
        return parser.parse(content, url);
    }

    public List<Link> removeOtherDomains(List<Link> list, Link domainLink) {
        List<Link> result = new ArrayList<>();
        for (Link link : list) {
            if (LinkUtil.isTheSameDomain(domainLink.getUri(), link.getUri())) {
                logger.debug("Adding link {} for domain {}", link.getUri(), domainLink.getUri());
                result.add(link);
            } else {
                logger.debug("Link {} for domain {} is not added.", link.getUri(), domainLink.getUri());
            }
        }
        return result;
    }


}
