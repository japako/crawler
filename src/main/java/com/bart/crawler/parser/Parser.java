package com.bart.crawler.parser;

import com.bart.crawler.model.Link;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bart on 22/01/2017.
 */
@Service
public class Parser {

    private List<LinkExtractor> linkExtractors;

    public Parser(List<LinkExtractor> linkExtractors) {
        this.linkExtractors = linkExtractors;
    }

    public List<Link> parse(String content, String baseUri) throws URISyntaxException {
        Document document = Jsoup.parseBodyFragment(content, baseUri);
        List<Link> list = new ArrayList<>();
        for(LinkExtractor extractor : linkExtractors) {
            list.addAll(extractor.extract(document));
        }
        return list;
    }


}
