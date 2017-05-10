package me.exerosis.materialreader.controller.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.List;

import me.exerosis.materialreader.model.Source;
import me.exerosis.materialreader.model.featcher.FeedStore;
import me.exerosis.materialreader.view.feed.FeedView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;

public class FeedFragment extends Fragment implements FeedController {
    private static final String ARG_FEED = "FEED";
    private FeedView view;
    private List<SyndEntry> entries = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle in) {
        Source feed = (Source) getArguments().getSerializable(ARG_FEED);
        entries = ((SyndFeed) feed).getEntries();
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
        view.setAdapter();

        return view.getRoot();
    }

    public static FeedFragment newInstance(Source feed) {
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