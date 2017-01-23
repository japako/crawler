package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import com.bart.crawler.model.LinkType;
import org.jsoup.nodes.Element;

import java.net.URISyntaxException;

/**
 * Created by bart on 23/01/2017.
 */
public class ImgLinkExtractor extends LinkExtractor {
    private final static String ELEMENT_NAME = "img";
    private final static String ELEMENT_ATTR = "src";

    public ImgLinkExtractor() {
        super(ELEMENT_NAME, ELEMENT_ATTR);
    }

    @Override
    public Link buildLink(Element element, String attrValue) throws URISyntaxException {
        return new Link(attrValue, LinkType.IMG);
    }
}
