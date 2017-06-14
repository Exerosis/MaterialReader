package me.exerosis.materialreader.controller.feed;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nytimes.android.external.store.base.impl.Store;
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
        urls = getArguments().getStringArray(ARG_FEED);
        store = ((MaterialReader) getContext().getApplicationContext()).getStore();
        super.onCreate(in);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);
        view.setListener(this);


        view.setAdapter(new ObservableAdapter<>(Observable.from(urls).
                flatMap(url -> store.get(url)).
                map(SyndFeed::getEntries).
                flatMapIterable(l -> l).
                flatMap(entry -> FeedUtils.getImage(getContext(), entry), Pair::new).
                toSortedList((one, two) -> one.first.getPublishedDate().compareTo(one.first.getPublishedDate())).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).doOnCompleted(() -> view.setLoading(false)), pair -> {
            if (pair.second == null)
                return 0;
            double pixels = pair.second.getWidth() * pair.second.getHeight();
            if (pixels <= 15000)
                return 0;
            if (pixels <= 40000)
                return 1;
            if (pixels <= 220000)
                return 2;
            return 3;
        }, FeedEntryHolderView::setEntry, (parent, type) -> new FeedEntryHolderView(LayoutInflater.from(getContext()), parent, type), (entry, holder) -> {
            new FinestWebView.Builder(getActivity()).
                    titleDefault(entry.first.getTitle()).
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
                    show(entry.first.getLink());
        }));
        return view.getRoot();
    }

    @Override
    public void onRefresh() {
        view.setRefreshing(false);
    }
}