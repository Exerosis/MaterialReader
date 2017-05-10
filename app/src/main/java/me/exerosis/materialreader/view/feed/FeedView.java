package me.exerosis.materialreader.view.feed;


import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xyzreader.R;

import butterknife.BindView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.butterknife.ButterKnifeFragmentView;

public class FeedView extends ButterKnifeFragmentView implements Feed {
    @BindView(R.id.feed_view_entries)
    protected RecyclerView entries;
    @BindView(R.id.feed_view_refresh_layout)
    protected SwipeRefreshLayout refreshLayout;
    private FeedListener listener;

    public FeedView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_view);
        entries.setLayoutManager(new LinearLayoutManager(getRoot().getContext()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.Adapter<FeedEntryHolderView> getAdapter() {
        return entries.getAdapter();
    }

    @Override
    public void setAdapter(@NonNull RecyclerView.Adapter<FeedEntryHolderView> adapter) {
        entries.setAdapter(adapter);
    }

    @Override
    public FeedListener getListener() {
        return listener;
    }

    @Override
    public FeedView setListener(FeedListener listener) {
        this.listener = listener;
        refreshLayout.setOnRefreshListener(listener);
        return this;
    }
}