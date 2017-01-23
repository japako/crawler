package com.bart.crawler;

import com.bart.crawler.model.CrawlLinkStorage;
import com.bart.crawler.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.util.Map;

@SpringBootApplication
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

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
        CrawlerStrategy crawlerStrategy = new CrawlerStrategy(url);
        crawlerStrategy.execute();
        CrawlLinkStorage storage = crawlerStrategy.getStorage();
        Writer writer = buildWriter(path);
        printSiteMap(storage, writer);
        writer.close();
        logger.info("Finished crawling domain: {}", url);
    }


    private Writer buildWriter(String path) throws FileNotFoundException {
        OutputStream os = null;
        if (path != null) {
            os = new FileOutputStream(new File(path));
        } else {
            os = System.out;
        }
        return new BufferedWriter(new OutputStreamWriter(os));

    }

    private void printSiteMap(CrawlLinkStorage crawlLinkStorage, Writer w) throws IOException {
        for (Map.Entry<Link, Boolean> entry : crawlLinkStorage.getCrawled().entrySet()) {
            w.write(entry.getKey().getUri().toString());
            w.write("\n");
        }
        w.write("---------------------------------------------------\n");
        for (Map.Entry<Link, Boolean> entry : crawlLinkStorage.getAwaiting().entrySet()) {
            w.write(entry.getKey().getUri().toString());
            w.write("\n");
        }
    }

    private static void printUsage() {
        System.out.println("Please provide domain that should be crawled!");
        System.out.println("<url> [file path]");
    }
}
