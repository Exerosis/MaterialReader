package me.exerosis.materialreader.controller.feed.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rometools.rome.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.List;

import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.model.Source;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;
import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    private final List<SyndFeed> feeds = new ArrayList<>();
    private FeedContainerView view;

    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());
        view.setAdapter(new RecyclerView.Adapter<FeedNavigationHolderView>() {
            @Override
            public FeedNavigationHolderView onCreateViewHolder(ViewGroup parent, int type) {
                return new FeedNavigationHolderView(getLayoutInflater(), parent).setListener(FeedContainerActivity.this);
            }

            @Override
            public void onBindViewHolder(FeedNavigationHolderView holder, int position) {
                holder.setFeed(feeds.get(position));
            }

            @Override
            public int getItemCount() {
                return feeds.size();
            }
        });

        setContentView(view.getRoot());
    }

    @Override
    public void onClick(SyndFeed feed) {
        //TODO Continue converting to source.
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(new Source())).commit();
    }
}
