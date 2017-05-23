package me.exerosis.materialreader;


import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class ExampleUnitTest {
    @Test
    public void canReadRSS() throws Exception {
        // Reading the feed

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(new URL("https://www.hpcwire.com/feed/")));

        for (SyndEntry entry : feed.getEntries()) {
            Image image = FeedUtils.getImage(entry);
            if (image != null)
                assertNotNull(image);
        }
    }
}