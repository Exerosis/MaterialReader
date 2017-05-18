package me.exerosis.materialreader.controller.feed.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.materialreader.model.FeedStore;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    private final BiMap<FeedModel, MenuItem> feeds = HashBiMap.create();
    private FeedContainerView view;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());

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
        view.setListener(item -> {
            for (MenuItem menuItem : feeds.values())
                menuItem.setChecked(false);

            item.setChecked(true);
            if (item.getItemId() == R.id.feed_container_view_menu_add)
                return false;
            if (!feeds.inverse().containsKey(item))
                return false;
            display(feeds.inverse().get(item));
            return true;
        });

        for (FeedModel model : FeedStore.getFeeds(this))
            model.getSource().subscribe(feed -> feeds.put(model, view.addFeed(feed)));

        FeedStore.getAddSubject().subscribe(model -> model.getSource().subscribe(feed -> feeds.put(model, view.addFeed(feed))));
        FeedStore.getRemoveSubject().subscribe(model -> view.removeFeed(feeds.get(model)));

        setContentView(view.getRoot());
    }

    private void display(FeedModel model) {
        FeedFragment fragment = FeedFragment.newInstance(model);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), fragment).commit();
    }
}