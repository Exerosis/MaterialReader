package me.exerosis.materialreader.controller.feed.container;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.exerosis.materialreader.view.feed.container.FeedContainerListener;
import me.exerosis.mvc.Listenable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior implements Listenable<FeedContainerListener> {
    private boolean home = false;
    private final FloatingActionButton child;
    private FeedContainerListener listener;

    public FloatingActionButtonBehavior(FloatingActionButton child) {
        this.child = child;
        child.setOnClickListener(view -> {
            if (listener != null && home)
                listener.onAddClick();
        });
    }

    public void setHome(boolean home) {
        if (this.home != home) {
            this.home = home;
            if (home)
                show();
            else
                hide();
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        boolean result = super.onLayoutChild(parent, child, layoutDirection);
        if (!home)
            child.setVisibility(INVISIBLE);
        else
            child.setVisibility(VISIBLE);
        return result;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout layout, FloatingActionButton child, View directTargetChild, View target, int axes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout layout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed <= 0 && home)
            show();
        else
            hide();
    }

    private void show() {
        child.animate().scaleX(1).scaleY(1).setDuration(100);
    }

    private void hide() {
        child.animate().scaleX(0).scaleY(0).setDuration(100);
    }

    @Override
    public FeedContainerListener getListener() {
        return listener;
    }

    @Override
    public FloatingActionButtonBehavior setListener(FeedContainerListener listener) {
        this.listener = listener;
        return this;
    }
}