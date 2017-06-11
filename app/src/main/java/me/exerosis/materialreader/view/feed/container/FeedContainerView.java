package me.exerosis.materialreader.view.feed.container;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.rometools.rome.feed.synd.SyndFeed;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.feed.container.FloatingActionButtonBehavior;
import me.exerosis.mvc.butterknife.ButterKnifeContainerView;

import static java.lang.String.format;
import static me.exerosis.materialreader.FeedUtils.FORMAT_FAVICON;

public class FeedContainerView extends ButterKnifeContainerView implements FeedContainer {
    private final FloatingActionButtonBehavior behavior;
    @BindView(R.id.feed_container_view_navigation)
    NavigationView navigation;
    @BindView(R.id.feed_container_view_drawer)
    DrawerLayout drawer;
    @BindView(R.id.feed_container_view_toolbar)
    Toolbar toolbar;
    @BindView(R.id.feed_container_fab)
    FloatingActionButton fab;

    private int menuId = 0;
    private FeedContainerListener listener;

    public FeedContainerView(@NonNull LayoutInflater inflater) {
        super(inflater, R.layout.feed_container_view, R.id.feed_container_view);
        navigation.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            return listener.onNavigationItemSelected(item);
        });
        fab.setOnClickListener(view -> {
            if (listener != null)
                listener.onAddClick();
        });
        behavior = new FloatingActionButtonBehavior(fab);
        ((CoordinatorLayout.LayoutParams) fab.getLayoutParams()).setBehavior(behavior);
        fab.invalidate();
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    public MenuItem addFeed(SyndFeed feed) {
        MenuItem item = navigation.getMenu().add(R.id.feed_container_view_menu, menuId++, Menu.NONE, feed.getTitle());
        Picasso.with(getRoot().getContext()).
                load(format(FORMAT_FAVICON, feed.getLink())).
                placeholder(R.drawable.ic_menu_gallery).
                error(R.drawable.ic_menu_gallery).
                resizeDimen(R.dimen.menu_icon_size, R.dimen.menu_icon_size).
                centerCrop().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                item.setIcon(new BitmapDrawable(getRoot().getResources(), bitmap));
                navigation.invalidate();
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                item.setIcon(drawable);
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
                item.setIcon(drawable);
            }
        });
        if (menuId == 1 && listener != null)
            listener.onNavigationItemSelected(item);
        item.setCheckable(true);
        return item;
    }

    @Override
    public void removeFeed(MenuItem item) {
        navigation.getMenu().removeItem(item.getItemId());
    }

    @Override
    public boolean setHome(boolean home) {
        behavior.setHome(home);
        return home;
    }

    @Override
    public FeedContainerListener getListener() {
        return listener;
    }

    @Override
    public FeedContainerView setListener(FeedContainerListener listener) {
        this.listener = listener;
        return this;
    }
}