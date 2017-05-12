package me.exerosis.materialreader.view.feed.container.holder;

import me.exerosis.materialreader.model.FeedModel;
import me.exerosis.mvc.Listenable;

public interface FeedNavigationHolder extends Listenable<FeedNavigationHolderListener> {
    void setFeed(FeedModel feed);
}
