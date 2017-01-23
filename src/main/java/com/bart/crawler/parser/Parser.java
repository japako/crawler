package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bart on 22/01/2017.
 */
public class Parser {

    private List<LinkExtractor> pageLinkExtractor;

    public Parser(List<LinkExtractor> pageLinkExtractor) {
        this.pageLinkExtractor = pageLinkExtractor;
    }

    public List<Link> parse(String content, String baseUri) throws URISyntaxException {
        Document document = Jsoup.parseBodyFragment(content, baseUri);
        List<Link> list = new ArrayList<>();
        for(LinkExtractor extractor : pageLinkExtractor) {
            list.addAll(extractor.extract(document));
        }
        return list;
    }


}
