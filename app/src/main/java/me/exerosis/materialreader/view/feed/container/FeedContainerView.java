package me.exerosis.materialreader.view.feed.container;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;
import me.exerosis.mvc.butterknife.ButterKnifeContainerView;

public class FeedContainerView extends ButterKnifeContainerView implements FeedContainer {
    @BindView(R.id.feed_container_view_navigation_list)
    protected RecyclerView navigationList;
    @BindView(R.id.feed_container_view_drawer)
    protected DrawerLayout drawer;
    @BindView(R.id.feed_container_view_toolbar)
    protected Toolbar toolbar;

    public FeedContainerView(@NonNull LayoutInflater inflater) {
        super(inflater, R.layout.feed_container_view, R.id.feed_view_container);
        navigationList.setLayoutManager(new LinearLayoutManager(getRoot().getContext()));
        navigationList.setHasFixedSize(true);
        // Set the drawer toggle as the DrawerListener
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.Adapter<FeedNavigationHolderView> getAdapter() {
        return navigationList.getAdapter();
    }

    @Override
    public void setAdapter(@NonNull RecyclerView.Adapter<FeedNavigationHolderView> adapter) {
        navigationList.setAdapter(adapter);
    }


    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public DrawerLayout getDrawer() {
        return drawer;
    }
}