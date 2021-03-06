package me.exerosis.materialreader.view.feed;


import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.butterknife.ButterKnifeFragmentView;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FeedView extends ButterKnifeFragmentView implements Feed {
    @BindView(R.id.feed_view_entries)
    RecyclerView entries;
    @BindView(R.id.feed_view_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.feed_view_loading)
    ProgressBar loading;

    private FeedListener listener;

    public FeedView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_view);
        //Prepare a staggered layout with evenly spaced items.
        entries.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        entries.addItemDecoration(new ItemOffsetDecoration(getRoot().getContext(), R.dimen.item_offset));
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading.setVisibility(loading ? VISIBLE : INVISIBLE);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
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