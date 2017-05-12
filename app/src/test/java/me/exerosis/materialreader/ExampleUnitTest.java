package me.exerosis.materialreader;


import com.google.gson.Gson;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.junit.Test;

import java.net.URL;

import static junit.framework.Assert.assertNotNull;

public class ExampleUnitTest {
    @Test
    public void canReadRSS() throws Exception {
        // Reading the feed

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(new URL("http://feeds.feedburner.com/manishchhabra27")));


        String json = new Gson().toJson(feed);

        SyndFeed syndFeed = new Gson().fromJson(json, SyndFeedImpl.class);

        for (SyndEntry entry : syndFeed.getEntries()) {
            assertNotNull(entry.getTitle());
        }
    }
}