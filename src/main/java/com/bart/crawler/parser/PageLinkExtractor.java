package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import com.bart.crawler.model.PageLink;
import org.jsoup.nodes.Element;

import java.net.URISyntaxException;

/**
 * Created by bart on 22/01/2017.
 */
public class PageLinkExtractor extends LinkExtractor {
    private final static String ELEMENT_NAME = "a";
    private final static String ELEMENT_ATTR = "href";

    public PageLinkExtractor() {
        super(ELEMENT_NAME, ELEMENT_ATTR);
    }

    @Override
    public Link buildLink(Element element, String attrValue) throws URISyntaxException {
        return new PageLink(attrValue);
    }
}
