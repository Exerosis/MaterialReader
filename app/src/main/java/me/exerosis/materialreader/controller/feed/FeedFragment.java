package me.exerosis.materialreader.controller.feed;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.thefinestartist.finestwebview.FinestWebView;

import me.exerosis.materialreader.FeedUtils;
import me.exerosis.materialreader.MaterialReader;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.view.feed.FeedView;
import me.exerosis.materialreader.view.feed.holder.FeedEntryHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

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
        //Get urls from arguments and get the store from the application.
        urls = getArguments().getStringArray(ARG_FEED);
        store = ((MaterialReader) getContext().getApplicationContext()).getStore();
        super.onCreate(in);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);
        view.setListener(this);

        //ReactiveX flow into an ObservableAdapter.
        //From urls flat-maps to SyndFeeds, maps to lists of SyndEntries, flattens to a flow of SyndEntries
        //Pairs them with their thumbnail images, sorts them by date. When the flow completes it hides the ProgressBar.
        view.setAdapter(ObservableAdapter.build(Observable.from(urls).
                flatMap(url -> store.get(url)).
                map(SyndFeed::getEntries).
                flatMapIterable(l -> l).
                flatMap(data -> FeedUtils.getImage(getContext(), data), (entry, bitmap) -> new Pair<SyndEntry, Bitmap>(entry, bitmap) {
                    @Override
                    public boolean equals(Object o) {
                        return o instanceof Pair && ((Pair) o).first instanceof SyndEntry && ((SyndEntry) ((Pair) o).first).getLink().equals(first.getLink());
                    }
                }).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).doOnEach(data -> view.setLoading(false)), Pair.class, FeedEntryHolderView::setEntry).
                onClick(data -> new FinestWebView.Builder(getActivity()).
                        titleDefault(data.first.getTitle()).
                        toolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS).
                        toolbarColorRes(R.color.primary).
                        dividerColorRes(R.color.divider).
                        stringResShareVia(R.string.menu_share_via).
                        stringResOpenWith(R.string.menu_open_with).
                        webViewJavaScriptEnabled(true).
                        webViewBlockNetworkLoads(false).
                        statusBarColorRes(R.color.primary_dark).
                        showSwipeRefreshLayout(false).
                        titleColor(Color.WHITE).
                        iconDefaultColor(Color.WHITE).
                        progressBarHeight(applyDimension(COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics())).
                        progressBarColorRes(R.color.accent).
                        backPressToClose(true).
                        setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit).
                        show(data.first.getLink())).
                withTypes(data -> {
                    //Find
                    if (data.second == null)
                        return 0;
                    double pixels = data.second.getWidth() * data.second.getHeight();
                    if (pixels <= 15000)
                        return 0;
                    if (pixels <= 40000)
                        return 1;
                    if (pixels <= 220000)
                        return 2;
                    return 3;
                }, (parent, type) -> new FeedEntryHolderView(LayoutInflater.from(getContext()), parent, type)));
        return view.getRoot();
    }

    @Override
    public void onRefresh() {
        ((ObservableAdapter) view.getAdapter()).refresh();
        view.setRefreshing(false);
    }
}