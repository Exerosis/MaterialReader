package me.exerosis.materialreader.controller.feed.container;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndFeed;

import java.net.UnknownHostException;
import java.util.UUID;

import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.add.AddStockDialog;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.MaterialReader;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;
import rx.android.schedulers.AndroidSchedulers;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    public static final String TAG_DIALOG = "AddStockDialog";
    private final BiMap<String, MenuItem> feeds = HashBiMap.create();
    private FeedContainerView view;
    private AddStockDialog dialog;
    private Store<SyndFeed, String> store;
    private SharedPreferences preferences;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());
        store = ((MaterialReader) getApplicationContext()).getStore();
        preferences = ((MaterialReader) getApplicationContext()).getSharedPreferences();

        //--Dialog--
        dialog = new AddStockDialog();
        dialog.setListener(this);

        //--Toolbar--
        setSupportActionBar(view.getToolbar());

        //--Toggle--
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, view.getDrawer(), view.getToolbar(), R.string.drawer_open, R.string.drawer_close);
        view.getDrawer().addDrawerListener(toggle);
        toggle.syncState();

        //--Feeds--
        if (preferences.getAll().size() < 1)
            preferences.edit().putString("https://www.wired.com/feed", UUID.randomUUID().toString()).commit();
        for (String url : preferences.getAll().keySet())
            store.getRefreshing(url).observeOn(AndroidSchedulers.mainThread()).subscribe(feed -> feeds.put(url, view.addFeed(feed)), Throwable::printStackTrace);
        display((String) preferences.getAll().keySet().toArray()[0]);

        view.setListener(item -> {
            if (!feeds.inverse().containsKey(item))
                if (item.getItemId() == R.id.feed_container_view_menu_add) {
                    dialog.show(getSupportFragmentManager(), TAG_DIALOG);
                    return true;
                } else
                    return false;
            display(feeds.inverse().get(item));
            return true;
        });

        setContentView(view.getRoot());
    }

    @Override
    public void onAdd(String url) {
        store.getRefreshing(url).observeOn(AndroidSchedulers.mainThread()).subscribe(feed -> {
            if (preferences.contains(url))
                dialog.showError(R.string.error_duplicate);
            else {
                dialog.dismissAllowingStateLoss();
                feeds.put(url, view.addFeed(feed));
            }
        }, throwable -> dialog.showError(throwable instanceof IllegalArgumentException ? R.string.error_url :
                throwable instanceof UnknownHostException ? R.string.error_network : R.string.error_feed));
    }

    @Override
    public void onCancel() {
        dialog.dismissAllowingStateLoss();
    }

    private void display(String url) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(url)).commit();
    }
}