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
import android.widget.ToggleButton;

import com.rometools.rome.feed.synd.SyndEntry;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.mvc.butterknife.ButterKnifeHolderView;

public class FeedEntryHolderView extends ButterKnifeHolderView implements FeedEntryHolder {
    public static Format FORMAT_DATE = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private Pair<SyndEntry, Bitmap> pair;
    private FeedEntryListener listener;
    private boolean shown = false;

    @BindView(R.id.feed_entry_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.feed_entry_title)
    TextView title;
    @BindView(R.id.feed_entry_subtitle)
    TextView subtitle;
    @BindView(R.id.feed_entry_description)
    TextView description;
    @BindView(R.id.feed_entry_toggle)
    ToggleButton toggle;
    @BindView(R.id.feed_entry_expandable_ayout)
    ExpandableLayout expandableLayout;

    public FeedEntryHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @LayoutRes int layout) {
        super(inflater, container, layout);
        if (layout == R.layout.feed_entry_holder_view)
            ((StaggeredGridLayoutManager.LayoutParams) getRoot().getLayoutParams()).setFullSpan(true);

        toggle.setOnClickListener(view -> {
            toggle.setChecked(shown ^= true);
            expandableLayout.setExpanded(shown, true);
        });

        getRoot().setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(pair);
        });
    }

    @Override
    public void setEntry(Pair<SyndEntry, Bitmap> pair) {
        this.pair = pair;
        shown = false;
        expandableLayout.collapse(false);
        toggle.setChecked(false);
        title.setText(pair.first.getTitle());
        subtitle.setText(pair.first.getAuthor() + ", " + FORMAT_DATE.format(pair.first.getPublishedDate()));
        thumbnail.setImageBitmap(pair.second);
        description.setText(new HtmlToPlainText().getPlainText(Jsoup.parse(pair.first.getDescription().getValue())));
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