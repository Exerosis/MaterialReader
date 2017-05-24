package me.exerosis.materialreader.view.feed.holder;

import android.graphics.Bitmap;
import android.support.v4.util.Pair;

import com.rometools.rome.feed.synd.SyndEntry;

public interface FeedEntryListener {
    void onClick(Pair<SyndEntry, Bitmap> entry);
}