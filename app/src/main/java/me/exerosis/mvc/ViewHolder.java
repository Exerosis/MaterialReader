package me.exerosis.mvc;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder extends RecyclerView.ViewHolder implements ViewBase {
    public ViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @LayoutRes int layout) {
        super(inflater.inflate(layout, container, false));
    }

    @NonNull
    @Override
    public View getRoot() {
        return itemView;
    }

    @Nullable
    @Override
    public Bundle getViewState() {
        return null;
    }
}