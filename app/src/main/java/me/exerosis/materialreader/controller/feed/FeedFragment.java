package me.exerosis.materialreader.controller.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.exerosis.materialreader.model.Source;
import me.exerosis.materialreader.view.feed.FeedView;

public class FeedFragment extends Fragment implements FeedController {
    private static final String ARG_FEED = "FEED";
    private FeedView view;

    @Override
    public void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        Source feed = (Source) getArguments().getSerializable(ARG_FEED);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);
        return view.getRoot();
    }

    public static FeedFragment newInstance(Source feed) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_FEED, feed);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}