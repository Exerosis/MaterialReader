package me.exerosis.mvc;

public interface ControllerBase <T extends ViewBase> {
    T getRootView();
}
