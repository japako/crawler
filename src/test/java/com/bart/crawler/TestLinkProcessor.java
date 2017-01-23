package com.bart.crawler;

import com.bart.crawler.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by bart on 23/01/2017.
 */
@Service
@Profile("test")
public class TestLinkProcessor extends LinkProcessor {

    @Autowired
    private ResourceLoader resourceLoader;

    public TestLinkProcessor(Parser parser) {
        super(parser);
    }

    @Override
    protected String retrieve(String url) throws IOException {
        if(url.equals("http://wiprodigital.com")) {
            Resource resource = resourceLoader.getResource("classpath:wiprodigital.html");
//            URL resource = this.getClass().getResource("wiprodigital.html");
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));

        }
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Page Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>This is a Heading</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
