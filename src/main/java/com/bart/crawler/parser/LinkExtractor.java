package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bart on 22/01/2017.
 */
public abstract class LinkExtractor {
    private final static Logger logger = LoggerFactory.getLogger(LinkExtractor.class);

    private String elementName;
    private String elementAttr;

    protected LinkExtractor(String elementName, String elementAttr) {
        this.elementName = elementName;
        this.elementAttr = elementAttr;
    }


    public List<Link> extract(Document document) throws URISyntaxException {
        Elements elements = document.getElementsByTag(elementName);
        List<Link> result = null;
        if (!elements.isEmpty()) {
            result = new ArrayList<>();
            Link link = null;
            String attrValue = null;
            for (Element element : elements) {
                attrValue = element.attr(elementAttr);
                try {
                    link = buildLink(element, attrValue);
                    if (link != null) {
                        result.add(link);
                    }
                } catch (URISyntaxException e) {
                    logger.debug("Invalid link {}", attrValue);


                }

            }
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    protected abstract Link buildLink(Element element, String attrValue) throws URISyntaxException;

}
