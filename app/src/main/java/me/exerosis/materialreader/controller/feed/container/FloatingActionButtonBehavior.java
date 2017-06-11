package me.exerosis.materialreader.controller.feed.container;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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
    public void onNestedScroll(CoordinatorLayout layout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(layout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (home && dyConsumed < 0 && child.getVisibility() != VISIBLE)
            child.show();
        else if (child.getVisibility() != INVISIBLE)
            child.hide();
    }
}