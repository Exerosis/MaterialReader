package me.exerosis.mvc;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentView implements ViewBase {
    private final View view;

    public FragmentView(@NonNull LayoutInflater inflater, ViewGroup container, @LayoutRes int layout) {
        view = inflater.inflate(layout, container, false);
        if (view == null)
            throw new InflateException("View failed to inflate!");
    }

    @NonNull
    @Override
    public View getRoot() {
        return view;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }
}