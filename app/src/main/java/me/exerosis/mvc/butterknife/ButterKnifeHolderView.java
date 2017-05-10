package me.exerosis.mvc.butterknife;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.exerosis.mvc.ViewHolder;

public class ButterKnifeHolderView extends ViewHolder {
    public ButterKnifeHolderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @LayoutRes int layout) {
        super(inflater, container, layout);
        ButterKnife.bind(this, getRoot());
    }
}