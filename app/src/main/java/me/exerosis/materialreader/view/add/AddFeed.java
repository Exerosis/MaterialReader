package me.exerosis.materialreader.view.add;

import android.app.Dialog;
import android.support.annotation.StringRes;

import me.exerosis.mvc.Listenable;
import me.exerosis.mvc.ViewBase;

public interface AddFeed extends ViewBase, Listenable<AddFeedListener> {
    Dialog getDialog();

    void showError(@StringRes int error);
}
