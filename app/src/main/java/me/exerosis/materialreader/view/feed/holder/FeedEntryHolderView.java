package me.exerosis.materialreader.view.feed.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;

import butterknife.BindView;
import me.exerosis.materialreader.FeedUtils;
import me.exerosis.materialreader.R;
import me.exerosis.mvc.butterknife.ButterKnifeHolderView;

public class FeedEntryHolderView extends ButterKnifeHolderView implements FeedEntryHolder {
    private SyndEntry entry;
    private FeedEntryListener listener;

    @BindView(R.id.feed_entry_title)
    TextView title;

    public FeedEntryHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_entry_holder_view);
        getRoot().setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(entry);
        });
    }

    @Override
    public void setEntry(SyndEntry entry) {
        this.entry = entry;
        title.setText(entry.getTitle());
        FeedUtils.getImage(getRoot().getContext(), entry).subscribe(bitmap -> {
            System.out.println(bitmap.getHeight());
            System.out.println(bitmap.getWidth());
        });
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