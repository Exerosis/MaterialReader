package me.exerosis.materialreader.controller.feed;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import me.exerosis.materialreader.FeedUtils;
import me.exerosis.materialreader.MaterialReader;
import me.exerosis.materialreader.view.feed.FeedView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedFragment extends Fragment implements FeedController {
    private static final String ARG_FEED = "FEED";
    private String[] urls;
    private Store<SyndFeed, String> store;
    private FeedView view;

    public static FeedFragment newInstance(String... urls) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_FEED, urls);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle in) {
        urls = getArguments().getStringArray(ARG_FEED);
        store = ((MaterialReader) getContext().getApplicationContext()).getStore();
        super.onCreate(in);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);
        view.setListener(this);


        view.setAdapter(new ObservableAdapter<>(Observable.from(urls).
                concatMap(url -> store.get(url)).
                map(SyndFeed::getEntries).
                flatMapIterable(l -> l).
                flatMap(entry -> FeedUtils.getImage(getContext(), entry), Pair::new).
                toList().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()), pair -> {
            if (pair.second == null)
                return 0;
            double pixels = pair.second.getWidth() * pair.second.getHeight();
            if (pixels <= 40000)
                return 1;
            if (pixels <= 220000)
                return 2;
            return 3;
        }, FeedEntryHolderView::setEntry, (parent, type) -> new FeedEntryHolderView(LayoutInflater.from(getContext()), parent, type).setListener(FeedFragment.this)));
        return view.getRoot();
    }

    @Override
    public void onRefresh() {
        view.setRefreshing(false);
    }

    @Override
    public void onClick(Pair<SyndEntry, Bitmap> entry) {
        //TODO new Activity.
    }
}