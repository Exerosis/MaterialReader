package me.exerosis.materialreader.controller.feed.container;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndFeed;

import java.net.MalformedURLException;

import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.add.AddStockDialog;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.MaterialReader;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    public static final String TAG_DIALOG = "AddStockDialog";
    private final BiMap<String, MenuItem> feeds = HashBiMap.create();
    private FeedContainerView view;
    private AddStockDialog dialog;
    private Store<SyndFeed, String> store;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());
        store = ((MaterialReader) getApplicationContext()).getStore();
        prefs = ((MaterialReader) getApplicationContext()).getSharedPreferences();

        //--Dialog--
        dialog = new AddStockDialog();
        dialog.setListener(this);

        //--Toolbar--
        setSupportActionBar(view.getToolbar());

        //--Toggle--
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, view.getDrawer(), view.getToolbar(), R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        view.getDrawer().addDrawerListener(toggle);
        toggle.syncState();

        //--Feeds--
        for (String url : prefs.getAll().keySet())
            store.getRefreshing(url).subscribe(feed -> feeds.put(url, view.addFeed(feed)));

        view.setListener(item -> {
            if (item.getItemId() == R.id.feed_container_view_menu_add) {
                dialog.show(getSupportFragmentManager(), TAG_DIALOG);
                return true;
            }

            if (feeds.inverse().containsKey(item))
                return false;
            display(feeds.inverse().get(item));
            return true;
        });

        setContentView(view.getRoot());
    }


    private void display(String url) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(url)).commit();
    }

    @Override
    public void onCancel() {
        dialog.dismissAllowingStateLoss();
    }

    @Override
    public void onAdd(String url) {
        store.getRefreshing(url).subscribe(feed -> {
            feeds.put(url, view.addFeed(feed));
            dialog.dismissAllowingStateLoss();
        }, throwable -> dialog.showError(throwable instanceof MalformedURLException ? R.string.error_url : R.string.error_feed));
    }
}