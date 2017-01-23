package com.bart.crawler;

import com.bart.crawler.model.Link;
import com.bart.crawler.model.LinkType;
import com.bart.crawler.util.URIUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BuilditCrawlerApplicationTests {
	@Autowired
	Crawler crawler;

	@Test
	public void contextLoads() throws Exception {
		//given
		Link startingLink = new Link("http://wiprodigital.com/", LinkType.PAGE);

		//when
		Set<Link> links = crawler.execute(startingLink.getUri().toString());

		//then
		//count link types
		int imgInternal = 0, imgExternal = 0, pageInternal = 0, pageExternal = 0;
		for(Link link : links) {
			switch (link.getLinkType()) {
				case PAGE:
					if(URIUtil.isTheSameDomain(startingLink.getUri(), link.getUri())) {
						pageInternal++;
					} else {
						pageExternal++;
					}
					break;
				case IMG:
					if(URIUtil.isTheSameDomain(startingLink.getUri(), link.getUri())) {
						imgInternal++;
					} else {
						imgExternal++;
					}
					break;

				default:
					throw new IllegalStateException();
			}

		}
		Assert.assertEquals(0, imgInternal);
		Assert.assertEquals(1, imgExternal);
		Assert.assertEquals(44, pageInternal);
		Assert.assertEquals(9, pageExternal);

	}

}
