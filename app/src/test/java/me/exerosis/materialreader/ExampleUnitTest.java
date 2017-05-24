package me.exerosis.materialreader;


import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void canReadRSS() throws Exception {
        // Reading the feed
        double aspectRatio = Math.min(Math.round(((double) 45) / 100), 2);
        System.out.println(aspectRatio);
//
//        SyndFeedInput input = new SyndFeedInput();
//        SyndFeed feed = input.build(new XmlReader(new URL("https://www.hpcwire.com/feed/")));
//
//        for (SyndEntry entry : feed.getEntries()) {
//            Image image = FeedUtils.getImage(entry);
//            if (image != null)
//                assertNotNull(image);
//        }
    }
}