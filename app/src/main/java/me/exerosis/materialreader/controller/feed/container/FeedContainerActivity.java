package me.exerosis.materialreader.controller.feed.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rometools.rome.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.List;

import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.materialreader.model.FeedStore;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;
import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;
import me.exerosis.mvc.rxjava.ObservableAdapter;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    private final List<SyndFeed> feeds = new ArrayList<>();
    private FeedContainerView view;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());

        view.setAdapter(new ObservableAdapter<>(FeedStore.getFeeds(this), FeedNavigationHolderView::setFeed,
                parent -> new FeedNavigationHolderView(getLayoutInflater(), parent).setListener(this)));
        setContentView(view.getRoot());
    }

    @Override
    public void onClick(FeedModel feed) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(null)).commit();
    }
}
