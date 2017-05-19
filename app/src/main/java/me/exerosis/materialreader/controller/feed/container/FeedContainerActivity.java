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
import me.exerosis.materialreader.controller.add.AddStockDialog;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.materialreader.model.FeedStore;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    public static final String TAG_DIALOG = "AddStockDialog";
    private final BiMap<FeedModel, MenuItem> feeds = HashBiMap.create();
    private FeedContainerView view;
    private AddStockDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());
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
        view.setListener(item -> {
            if (item.getItemId() == R.id.feed_container_view_menu_add) {
                dialog.show(getSupportFragmentManager(), TAG_DIALOG);
                return false;
            }

            for (MenuItem menuItem : feeds.values())
                menuItem.setChecked(false);

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

    @Override
    public void onCancel() {
        dialog.dismissAllowingStateLoss();
    }

    @Override
    public void onAdd(String url) {
        FeedStore.addSource(this, url);
    }
}