package me.exerosis.materialreader.view.feed.container;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;
import me.exerosis.mvc.Adaptable;
import me.exerosis.mvc.Container;

public interface FeedContainer extends Container, Adaptable<RecyclerView.Adapter<FeedNavigationHolderView>> {
    Toolbar getToolbar();

    DrawerLayout getDrawer();
}
