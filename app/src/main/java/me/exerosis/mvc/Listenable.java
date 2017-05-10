package me.exerosis.mvc;

public interface Listenable<T> {
    T getListener();

    Listenable<T> setListener(T listener);
}
