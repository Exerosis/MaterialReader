package me.exerosis.materialreader.view.feed.holder;

import com.rometools.rome.feed.synd.SyndEntry;

public interface FeedEntryListener {
    void onClick(SyndEntry entry);
}