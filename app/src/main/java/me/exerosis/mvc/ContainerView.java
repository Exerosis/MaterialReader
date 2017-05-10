package me.exerosis.mvc;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

public class ContainerView extends FragmentView implements Container {
    @IdRes
    private final int container;

    public ContainerView(@NonNull LayoutInflater inflater, @LayoutRes int layout, @IdRes int container) {
        super(inflater, null, layout);
        this.container = container;
    }

    @Override
    @IdRes
    public int getContainerID() {
        return container;
    }
}