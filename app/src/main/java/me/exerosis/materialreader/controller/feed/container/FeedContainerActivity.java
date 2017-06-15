package me.exerosis.materialreader.controller.feed.container;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nytimes.android.external.store.base.impl.Store;
import com.rometools.rome.feed.synd.SyndFeed;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.exerosis.materialreader.MaterialReader;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.controller.add.AddStockDialog;
import me.exerosis.materialreader.controller.feed.FeedFragment;
import me.exerosis.materialreader.view.feed.container.FeedContainerView;

import static rx.Observable.from;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class FeedContainerActivity extends AppCompatActivity implements FeedContainerController {
    public static final String TAG_DIALOG = "AddStockDialog";
    private final Map<MenuItem, String> feeds = new HashMap<>();
    private FeedContainerView view;
    private AddStockDialog dialog;
    private Store<SyndFeed, String> store;
    private SharedPreferences preferences;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(@Nullable Bundle in) {
        super.onCreate(in);
        view = new FeedContainerView(getLayoutInflater());

        //Get preferences and sore instance from the application.
        store = ((MaterialReader) getApplicationContext()).getStore();
        preferences = ((MaterialReader) getApplicationContext()).getSharedPreferences();

        //--Setup View--
        view.setListener(this);
        setContentView(view.getRoot());

        //--Dialog--
        dialog = new AddStockDialog();
        dialog.setListener(this);

        //--Toolbar--
        setSupportActionBar(view.getToolbar());

        //--Toggle--
        //Setup the hamburger button for nav drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, view.getDrawer(), view.getToolbar(), R.string.drawer_open, R.string.drawer_close);
        view.getDrawer().addDrawerListener(toggle);
        toggle.syncState();

        //--Feeds--
        //If there are no feeds present, add livescience as a default feed.
        if (preferences.getAll().size() < 1)
            preferences.edit().putString("https://www.livescience.com/home/feed/site.xml", UUID.randomUUID().toString()).commit();

        //ReactiveX flow
        //Flow of feed urls, flat mapped to a flow of SyndEntries paired with urls from the store.
        //At the end of the flow store the url and feed, when flow ends select the home feed.
        from(preferences.getAll().keySet()).flatMap(url -> store.get(url).map(entry -> Pair.create(entry, url))).observeOn(mainThread()).
                subscribe(pair -> feeds.put(view.addFeed(pair.first), pair.second), Throwable::printStackTrace, () -> onNavigationItemSelected(view.getHomeItem().setChecked(true)));
    }

    @Override
    public void onAddClick() {
        //Show the add feed dialog when the FAB is clicked.
        dialog.show(getSupportFragmentManager(), TAG_DIALOG);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        view.getToolbar().setTitle(item.getTitle());
        //If the map of feeds doesn't contain the given ID then it's the home feed, so display all the feeds and enable showing the FAB.
        if (view.setHome(!feeds.containsKey(item))) {
            Set<String> urls = preferences.getAll().keySet();
            String[] s = urls.toArray(new String[urls.size()]);
            display(s);
        } else
            display(feeds.get(item));
        return true;
    }

    @Override
    public void onAdd(String url) {
        //If we already have this feed don't show it.
        if (preferences.contains(url))
            dialog.showError(R.string.error_duplicate);
        else
            //Get the new feed from the store and map it, then display it. Show errors in dialog.
            store.get(url).observeOn(mainThread()).subscribe(feed -> {
                dialog.dismissAllowingStateLoss();
                feeds.put(view.addFeed(feed), url);
                display(url);
            }, throwable -> dialog.showError(throwable instanceof IllegalArgumentException ? R.string.error_url :
                    throwable instanceof UnknownHostException ? R.string.error_network : R.string.error_feed));
    }

    @Override
    public void onCancel() {
        dialog.dismissAllowingStateLoss();
    }

    private void display(String... urls) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(view.getContainerID(), FeedFragment.newInstance(urls)).commit();
    }
}