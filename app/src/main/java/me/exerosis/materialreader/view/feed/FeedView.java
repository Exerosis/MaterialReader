package me.exerosis.materialreader.view.feed;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xyzreader.R;

import me.exerosis.mvc.butterknife.ButterKnifeFragmentView;

public class FeedView extends ButterKnifeFragmentView {
    public FeedView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        super(inflater, container, R.layout.feed_view);
    }
}
