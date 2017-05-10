package me.exerosis.mvc.butterknife;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import butterknife.ButterKnife;
import me.exerosis.mvc.ContainerView;

public class ButterKnifeContainerView extends ContainerView {
    public ButterKnifeContainerView(@NonNull LayoutInflater inflater, @LayoutRes int layout, @IdRes int container) {
        super(inflater, layout, container);
        ButterKnife.bind(this, getRoot());
    }
}
