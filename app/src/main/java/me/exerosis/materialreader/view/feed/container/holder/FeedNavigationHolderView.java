package me.exerosis.materialreader.view.feed.container.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.rometools.rome.feed.synd.SyndFeed;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import me.exerosis.mvc.butterknife.ButterKnifeHolderView;

public class FeedNavigationHolderView extends ButterKnifeHolderView implements FeedNavigationHolder {
    @BindView(R.id.feed_navigation_view_holder_icon)
    protected ImageView iconView;
    @BindView(R.id.feed_navigation_view_holder_text)
    protected TextView textView;
    private FeedNavigationHolderListener listener;

    public FeedNavigationHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_navigation_view_holder);
    }

    @Override
    public void setFeed(SyndFeed feed) {
        Picasso.with(getRoot().getContext()).load(feed.getImage().getUrl()).centerCrop().into(iconView);
        textView.setText(feed.getTitle());
        iconView.setContentDescription(feed.getTitle());
    }

    @Override
    public FeedNavigationHolderListener getListener() {
        return listener;
    }

    @Override
    public FeedNavigationHolderView setListener(FeedNavigationHolderListener listener) {
        this.listener = listener;
        return this;
    }
}