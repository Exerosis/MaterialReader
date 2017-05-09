package me.exerosis.mvc;

public interface Listenable<T> {
    T getListener();

    void setListener(T listener);
}
