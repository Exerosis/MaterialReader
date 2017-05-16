package me.exerosis.materialreader.controller.feed.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.materialreader.model.FeedStore;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;
import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    private FeedContainerView view;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());

        FeedStore.addSource(this, "http://feeds.gawker.com/lifehacker/full.xml");

        setSupportActionBar(view.getToolbar());
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view.setAdapter(new ObservableAdapter<>(FeedStore.getFeedsSubject(this), FeedNavigationHolderView::setFeed,
                parent -> new FeedNavigationHolderView(getLayoutInflater(), parent).setListener(this), (feed, holder) -> {
            view.getDrawer().closeDrawers();
            display(feed);
        }).selectSingle());

        display(FeedStore.getFeeds(this).get(0));
        setContentView(view.getRoot());
    }

    private void display(FeedModel feed) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(feed)).commit();
    }

    @Override
    public void onClick(FeedModel feed) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(null)).commit();
    }
}