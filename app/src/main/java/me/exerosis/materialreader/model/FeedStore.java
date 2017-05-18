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
import rx.subjects.PublishSubject;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ApplySharedPref")
public class FeedStore {
    private static final String PREFS_FEEDS = "feeds";
    private static final PublishSubject<FeedModel> ADD_SUBJECT = PublishSubject.create();
    private static final PublishSubject<FeedModel> REMOVE_SUBJECT = PublishSubject.create();
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
            FeedModel feed = FeedModel.obtain(getStore(context).getRefreshing(url), url);
            getFeeds(context).add(feed);
            ADD_SUBJECT.onNext(feed);
        });
    }

    public static void removeSource(Context context, FeedModel feed) {
        Schedulers.io().createWorker().schedule(() -> {
            prefs(context).edit().remove(feed.getUrl()).commit();
            getFeeds(context).remove(feed);
            REMOVE_SUBJECT.onNext(feed);
        });
    }

    public static List<FeedModel> getFeeds(Context context) {
        if (feeds != null)
            return feeds;
        feeds = new ArrayList<>();

        for (String url : prefs(context).getAll().keySet())
            feeds.add(FeedModel.obtain(getStore(context).getRefreshing(url), url));
        if (feeds.size() > 0)
            return feeds;
        String url = "http://feeds.gawker.com/lifehacker/full.xml";
        prefs(context).edit().putString(url, UUID.randomUUID().toString()).commit();
        feeds.add(FeedModel.obtain(getStore(context).getRefreshing(url), url));
        return feeds;
    }

    public static Observable<FeedModel> getAddSubject() {
        return ADD_SUBJECT;
    }

    public static Observable<FeedModel> getRemoveSubject() {
        return REMOVE_SUBJECT;
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