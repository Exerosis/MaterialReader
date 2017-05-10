package me.exerosis.materialreader.view.feed.container.holder;

import com.rometools.rome.feed.synd.SyndFeed;

public interface FeedNavigationHolderListener {
    void onClick(SyndFeed feed);
}