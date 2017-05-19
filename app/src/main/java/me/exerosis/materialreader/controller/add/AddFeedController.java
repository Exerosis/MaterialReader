package me.exerosis.materialreader.controller.add;

import android.support.annotation.StringRes;

import me.exerosis.materialreader.view.add.AddFeedListener;
import me.exerosis.mvc.Listenable;

public interface AddFeedController extends Listenable<AddFeedListener> {
    void showError(@StringRes int error);
}