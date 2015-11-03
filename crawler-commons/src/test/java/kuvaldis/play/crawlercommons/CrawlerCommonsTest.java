package kuvaldis.play.crawlercommons;

import crawlercommons.sitemaps.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class CrawlerCommonsTest {

    @Test
    public void testSiteMap() throws Exception {
        final SiteMapParser parser = new SiteMapParser();
        final AbstractSiteMap indexSiteMap = parser.parseSiteMap(new URL("http://www.am-autopartsqa.com/sitemap.xml.gz"));
        assertTrue(indexSiteMap.isIndex());
        parse(indexSiteMap);
    }

    private void parse(final AbstractSiteMap abstractSiteMap) {
        if (abstractSiteMap.isIndex()) {
            ((SiteMapIndex) abstractSiteMap).getSitemaps().forEach(this::parse);
        } else {
            final SiteMap siteMap = ((SiteMap) abstractSiteMap);
            if (siteMap.getSiteMapUrls().isEmpty()) {
                try {
                    parse(new SiteMapParser().parseSiteMap(siteMap.getUrl()));
                } catch (UnknownFormatException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (final SiteMapURL siteMapURL : siteMap.getSiteMapUrls()) {
                    System.out.println(siteMapURL.getUrl());
                }
            }
        }
    }
}
