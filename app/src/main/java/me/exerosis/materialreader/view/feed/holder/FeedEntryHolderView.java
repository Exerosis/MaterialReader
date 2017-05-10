package me.exerosis.materialreader.view.feed.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rometools.rome.feed.synd.SyndEntry;

import me.exerosis.materialreader.R;
import me.exerosis.mvc.ViewHolder;

public class FeedEntryHolderView extends ViewHolder implements FeedEntryHolder {
    private SyndEntry entry;
    private FeedEntryListener listener;

    public FeedEntryHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_entry_holder_view);
        getRoot().setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(entry);
        });
    }

    @Override
    public void setEntry(SyndEntry entry) {
        this.entry = entry;
    }

    @Override
    public FeedEntryListener getListener() {
        return listener;
    }

    @Override
    public FeedEntryHolderView setListener(FeedEntryListener listener) {
        this.listener = listener;
        return this;
    }
}