package me.exerosis.materialreader.view.feed.container.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.materialreader.model.FeedModel;
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
    public void setFeed(FeedModel feed) {
        feed.getSource().subscribe(source -> {
            if (source.getImage() != null)
                Picasso.with(getRoot().getContext()).load(source.getImage().getUrl()).centerCrop().into(iconView);
            textView.setText(source.getTitle());
            iconView.setContentDescription(source.getTitle());
        });
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