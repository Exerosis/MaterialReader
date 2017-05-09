package me.exerosis.mvc;

import android.support.annotation.NonNull;

public interface Adaptable<T> {
    T getAdapter();

    void setAdapter(@NonNull T adapter);
}
