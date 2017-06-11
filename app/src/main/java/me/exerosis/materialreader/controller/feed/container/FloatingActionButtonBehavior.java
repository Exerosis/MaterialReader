package me.exerosis.materialreader.controller.feed.container;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior {
    private boolean home = false;
    private final FloatingActionButton child;

    public FloatingActionButtonBehavior(FloatingActionButton child) {
        this.child = child;
    }

    public void setHome(boolean home) {
        if (this.home != home) {
            this.home = home;
            if (home)
                child.show();
            else
                child.hide();
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        boolean result = super.onLayoutChild(parent, child, layoutDirection);
        if (!home)
            child.hide();
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
            child.show();
        else
            child.hide();
    }
}