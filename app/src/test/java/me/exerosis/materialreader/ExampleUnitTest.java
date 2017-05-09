package me.exerosis.materialreader;


import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.junit.Test;

import java.net.URL;

import static junit.framework.Assert.assertNotNull;

public class ExampleUnitTest {
    @Test
    public void canReadRSS() throws Exception {
        // Reading the feed
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL("http://feeds.feedburner.com/manishchhabra27")));
        for (SyndEntry entry : feed.getEntries()) {
            assertNotNull(entry.getTitle());
        }
    }
}