package me.exerosis.materialreader.view.feed;

import android.support.v7.widget.RecyclerView;

import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.Adaptable;
import me.exerosis.mvc.Listenable;
import me.exerosis.mvc.ViewBase;

public interface Feed extends ViewBase, Adaptable<RecyclerView.Adapter<FeedEntryHolderView>>, Listenable<FeedListener> {
    void setLoading(boolean loading);

    void setRefreshing(boolean refreshing);
}
