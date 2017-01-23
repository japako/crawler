package com.bart.crawler;

import com.bart.crawler.parser.ImgLinkExtractor;
import com.bart.crawler.parser.LinkExtractor;
import com.bart.crawler.parser.PageLinkExtractor;
import com.bart.crawler.storage.SkipCrawlingMarker;
import com.bart.crawler.storage.TypeSkipCrawlingMarker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bart on 23/01/2017.
 */
@Configuration
@Profile("test")
public class TestAppConfiguration {
    @Bean
    public List<LinkExtractor> linkExtractors() {
        return Arrays.asList(new PageLinkExtractor(), new ImgLinkExtractor());
    }


    @Bean
    public List<SkipCrawlingMarker> basicMarkers() {
        return Arrays.asList(new TypeSkipCrawlingMarker());
    }


}
