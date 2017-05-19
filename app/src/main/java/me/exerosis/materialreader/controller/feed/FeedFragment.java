package me.exerosis.materialreader.controller.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import me.exerosis.materialreader.model.MaterialReader;
import me.exerosis.materialreader.view.feed.FeedView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;

public class FeedFragment extends Fragment implements FeedController {
    private static final String ARG_FEED = "FEED";
    private String url;
    private Store<SyndFeed, String> store;

    @Override
    public void onCreate(@Nullable Bundle in) {
        url = getArguments().getString(ARG_FEED);
        store = ((MaterialReader) getContext().getApplicationContext()).getStore();
        super.onCreate(in);
    }

    @Override
    public void onRefresh() {
        if (url != null)
            store.clear(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FeedView view = new FeedView(inflater, container);
        view.setListener(this);
        view.setAdapter(new ObservableAdapter<>(store.getRefreshing(url).map(SyndFeed::getEntries), FeedEntryHolderView::setEntry, parent ->
                new FeedEntryHolderView(LayoutInflater.from(getContext()), parent).setListener(this)));

        return view.getRoot();
    }

    public static FeedFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FEED, url);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(SyndEntry entry) {
        //TODO new Activity.
    }
}