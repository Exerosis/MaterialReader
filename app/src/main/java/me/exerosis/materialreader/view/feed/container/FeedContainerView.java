package me.exerosis.materialreader.view.feed.container;

import android.graphics.drawable.BitmapDrawable;
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

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.mvc.butterknife.ButterKnifeContainerView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public FeedContainerView(@NonNull LayoutInflater inflater) {
        super(inflater, R.layout.feed_container_view, R.id.feed_container_view);

        //Handle hiding and showing FAB.
        behavior = new FloatingActionButtonBehavior(fab);
        ((CoordinatorLayout.LayoutParams) fab.getLayoutParams()).setBehavior(behavior);
        fab.invalidate();

        //Handle nav item selected.
        navigation.setItemIconTintList(null);
        navigation.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            return getListener().onNavigationItemSelected(item);
        });

        toolbar.inflateMenu(R.menu.feed_container_menu);
        toolbar.setOnMenuItemClickListener(item -> getListener().onNavigationItemSelected(item));
    }

    @Override
    public MenuItem getHomeItem() {
        return navigation.getMenu().findItem(R.id.feed_container_view_menu_home);
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

        //Load in a favicon from the URL as menu icon.
        Observable.fromCallable(() -> Picasso.with(getRoot().getContext()).
                load(format(FORMAT_FAVICON, feed.getEntries().get(0).getLink())).
                placeholder(R.drawable.ic_menu_gallery).
                error(R.drawable.ic_menu_gallery).
                resizeDimen(R.dimen.menu_icon_size, R.dimen.menu_icon_size).centerCrop().get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bitmap -> {
            item.setIcon(new BitmapDrawable(getRoot().getResources(), bitmap));
        });
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
        toolbar.getMenu().getItem(0).setVisible(!home);
        return home;
    }

    @Override
    public FeedContainerListener getListener() {
        return behavior.getListener();
    }

    @Override
    public FeedContainerView setListener(FeedContainerListener listener) {
        behavior.setListener(listener);
        return this;
    }
}