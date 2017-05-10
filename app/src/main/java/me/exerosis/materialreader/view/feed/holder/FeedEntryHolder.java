package me.exerosis.materialreader.view.feed.holder;

import com.rometools.rome.feed.synd.SyndEntry;

import me.exerosis.mvc.Listenable;

public interface FeedEntryHolder extends Listenable<FeedEntryListener> {
    void setEntry(SyndEntry entry);
}