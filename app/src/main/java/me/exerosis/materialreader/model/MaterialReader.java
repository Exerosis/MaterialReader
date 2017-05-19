package me.exerosis.materialreader.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import com.nytimes.android.external.fs.FileSystemPersisterFactory;
import com.nytimes.android.external.store.base.impl.Store;
import com.nytimes.android.external.store.base.impl.StoreBuilder;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSource;
import rx.Observable;
import rx.schedulers.Schedulers;

public final class MaterialReader extends Application {
    private static final String PREFS_FEEDS = "feeds";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    public static final SyndFeedInput INPUT = new SyndFeedInput();
    private Store<SyndFeed, String> store;

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCreate() {
        super.onCreate();
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
                    persister(FileSystemPersisterFactory.create(getFilesDir(), url -> {
                        if (getSharedPreferences().contains(url))
                            return getSharedPreferences().getString(url, "");
                        String uuid = UUID.randomUUID().toString();
                        getSharedPreferences().edit().putString(url, uuid).commit();
                        return uuid;
                    })).open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Store<SyndFeed, String> getStore() {
        return store;
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREFS_FEEDS, MODE_PRIVATE);
    }
}
