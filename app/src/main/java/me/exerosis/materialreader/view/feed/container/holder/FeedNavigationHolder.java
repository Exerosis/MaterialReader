package me.exerosis.materialreader.view.feed.container.holder;

import com.rometools.rome.feed.synd.SyndFeed;

import me.exerosis.mvc.Listenable;

public interface FeedNavigationHolder extends Listenable<FeedNavigationHolderListener> {
    void setFeed(SyndFeed feed);
}
