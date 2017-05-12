package me.exerosis.materialreader.controller.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.List;

import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.materialreader.view.feed.FeedView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;

public class FeedFragment extends Fragment implements FeedController {
    private static final String ARG_FEED = "FEED";
    private FeedView view;
    private List<SyndEntry> entries;
    private FeedModel feed;

    @Override
    public void onCreate(@Nullable Bundle in) {
        feed = (FeedModel) getArguments().getSerializable(ARG_FEED);
        super.onCreate(in);
    }

    @Override
    public void onRefresh() {
        //TODO make this work somehow
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);

        view.setListener(this);

        view.setAdapter(new ObservableAdapter<>(feed.getSource().map(SyndFeed::getEntries), FeedEntryHolderView::setEntry, parent ->
                new FeedEntryHolderView(LayoutInflater.from(getContext()), parent).setListener(this)));

        return view.getRoot();
    }

    public static FeedFragment newInstance(FeedModel feed) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_FEED, feed);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(SyndEntry entry) {
        //TODO new Activity.
    }
}