package me.exerosis.materialreader.view.feed.holder;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.mvc.butterknife.ButterKnifeHolderView;

public class FeedEntryHolderView extends ButterKnifeHolderView implements FeedEntryHolder {
    private Pair<SyndEntry, Bitmap> pair;
    private FeedEntryListener listener;

    @BindView(R.id.feed_entry_title)
    TextView title;
    @BindView(R.id.feed_entry_thumbnail)
    ImageView thumbnail;

    public FeedEntryHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @LayoutRes int layout) {
        super(inflater, container, layout);
        if (layout == R.layout.feed_entry_holder_view)
            ((StaggeredGridLayoutManager.LayoutParams) getRoot().getLayoutParams()).setFullSpan(true);

        getRoot().setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(pair);
        });
    }

    @Override
    public void setEntry(Pair<SyndEntry, Bitmap> pair) {
        this.pair = pair;
        title.setText(pair.first.getTitle());

        thumbnail.setImageBitmap(pair.second);
    }

    @Override
    public FeedEntryListener getListener() {
        return listener;
    }

    @Override
    public FeedEntryHolderView setListener(FeedEntryListener listener) {
        this.listener = listener;
        return this;
    }
}