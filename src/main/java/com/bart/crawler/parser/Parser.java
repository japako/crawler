package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by bart on 22/01/2017.
 */
public class Parser {

    private LinkExtractor pageLinkExtractor = new PageLinkExtractor();

    public List<Link> parse(String content, String baseUri) throws URISyntaxException {
        Document document = Jsoup.parseBodyFragment(content, baseUri);
        return pageLinkExtractor.extract(document);
    }


}
