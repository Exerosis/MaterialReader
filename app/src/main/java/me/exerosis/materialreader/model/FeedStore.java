package me.exerosis.materialreader.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.nytimes.android.external.fs.FileSystemPersisterFactory;
import com.nytimes.android.external.store.base.impl.Store;
import com.nytimes.android.external.store.base.impl.StoreBuilder;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSource;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ApplySharedPref")
public class FeedStore {
    private static final String PREFS_FEEDS = "feeds";
    private static final BehaviorSubject<List<FeedModel>> FEEDS_SUBJECT = BehaviorSubject.create(new ArrayList<>());
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final SyndFeedInput INPUT = new SyndFeedInput();
    private static Store<SyndFeed, String> store;
    private static List<FeedModel> feeds;

    private FeedStore() {

    }

    public static Store<SyndFeed, String> getStore(Context context) {
        if (store != null)
            return store;
        try {
            store = StoreBuilder.<String, BufferedSource, SyndFeed>parsedWithKey().
                    fetcher(url -> Observable.fromCallable(() ->
                            CLIENT.newCall(new Request.Builder().url(url).get().build()).execute().body().source()).
                            subscribeOn(Schedulers.io())).
                    parser(source -> {
                        try {
                            return INPUT.build(new XmlReader(source.inputStream()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).
                    persister(FileSystemPersisterFactory.create(context.getFilesDir(), url -> prefs(context).getString(url, ""))).open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return store;
    }

    public static void addSource(Context context, String url) {
        Schedulers.io().createWorker().schedule(() -> {
            prefs(context).edit().putString(url, UUID.randomUUID().toString()).commit();
            FeedModel obtain = FeedModel.obtain(getStore(context).getRefreshing(url), url);
            getFeeds(context).add(obtain);
            FEEDS_SUBJECT.onNext(feeds);
        });
    }

    public static void removeSource(Context context, String url) {
        Schedulers.io().createWorker().schedule(() -> {
            prefs(context).edit().remove(url).commit();
            getFeeds(context).add(FeedModel.obtain(getStore(context).getRefreshing(url), url));
            FEEDS_SUBJECT.onNext(feeds);
        });
    }

    public static List<FeedModel> getFeeds(Context context) {
        if (feeds != null)
            return feeds;
        feeds = new ArrayList<>();

        for (String url : prefs(context).getAll().keySet())
            feeds.add(FeedModel.obtain(getStore(context).getRefreshing(url), url));

        return feeds;
    }

    public static Observable<List<FeedModel>> getFeedsSubject(Context context) {
        if (FEEDS_SUBJECT.getValue() == null)
            FEEDS_SUBJECT.onNext(getFeeds(context));
        return FEEDS_SUBJECT;
    }

    public static List<FeedModel> getFeedsUnsafe() {
        return feeds;
    }

    public static void refresh(Context context, String url) {
        getStore(context).clear(url);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_FEEDS, MODE_PRIVATE);
    }
}