package me.exerosis.materialreader.view.feed.container;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.example.xyzreader.R;

import butterknife.BindView;
import me.exerosis.materialreader.view.feed.container.holder.FeedNavigationHolderView;
import me.exerosis.mvc.butterknife.ButterKnifeContainerView;

public class FeedContainerView extends ButterKnifeContainerView implements FeedContainer {
    @BindView(R.id.feed_container_view_navigation_list)
    protected RecyclerView navigationList;

    public FeedContainerView(@NonNull LayoutInflater inflater) {
        super(inflater, R.layout.feed_container_view, R.id.feed_view_container);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.Adapter<FeedNavigationHolderView> getAdapter() {
        return navigationList.getAdapter();
    }

    @Override
    public void setAdapter(@NonNull RecyclerView.Adapter<FeedNavigationHolderView> adapter) {
        navigationList.setAdapter(adapter);
    }
}