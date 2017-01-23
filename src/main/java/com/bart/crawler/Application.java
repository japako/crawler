package com.bart.crawler;

import com.bart.crawler.model.LinkType;
import com.bart.crawler.storage.CrawlerLinkStorage;
import com.bart.crawler.model.Link;
import com.bart.crawler.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    Crawler crawler;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            System.exit(-1);
        }
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        String path = args.length >= 2 ? args[1] : null;
        context.getBean(Application.class).start(args[0], path);
    }


    private void start(String url, String path) throws Exception {
        logger.info("Crawling domain: {}", url);
//        Crawler crawler = new Crawler(url);
        Set<Link> links = crawler.execute(url);
        logger.info("Finished crawling domain: {}", url);

        logger.info("Writing found links to: {}", path == null ? "console" : path);
        printSiteMap(links, new Link(url, LinkType.PAGE), buildWriter(path));
    }


    private Writer buildWriter(String path) throws FileNotFoundException {
        OutputStream os;
        if (path != null) {
            os = new FileOutputStream(new File(path));
        } else {
            os = System.out;
        }
        return new BufferedWriter(new OutputStreamWriter(os));
    }

    private void printSiteMap(Collection<Link> links, Link crawledDomain, Writer w) throws IOException {
        for (Link link : links) {
            w.write(link.getLinkType().toString());
            w.write("|");
            w.write(URIUtil.isTheSameDomain(crawledDomain.getUri(), link.getUri()) ? "I" : "E");
            w.write("|");
            w.write(link.getUri().toString());
            w.write("\n");
        }
    }

    private static void printUsage() {
        System.out.println("Please provide domain that should be crawled!");
        System.out.println("<url> [file path]");
    }
}
