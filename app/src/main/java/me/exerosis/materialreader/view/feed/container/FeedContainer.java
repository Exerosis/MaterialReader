package me.exerosis.materialreader.view.feed.container;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rometools.rome.feed.synd.SyndFeed;

import me.exerosis.mvc.Container;
import me.exerosis.mvc.Listenable;

public interface FeedContainer extends Container, Listenable<NavigationView.OnNavigationItemSelectedListener> {
    Toolbar getToolbar();

    DrawerLayout getDrawer();

    MenuItem addFeed(SyndFeed feed);

    void removeFeed(MenuItem item);
}