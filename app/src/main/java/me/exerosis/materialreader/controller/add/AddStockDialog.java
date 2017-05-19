package me.exerosis.materialreader.controller.add;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import me.exerosis.materialreader.view.add.AddFeed;
import me.exerosis.materialreader.view.add.AddFeedListener;
import me.exerosis.materialreader.view.add.AddFeedView;

public class AddStockDialog extends DialogFragment implements AddFeedController {
    private AddFeed view;
    private AddFeedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle state) {
        view = new AddFeedView(LayoutInflater.from(getActivity()));
        view.setListener(listener);
        return view.getDialog();
    }

    @Override
    public AddFeedListener getListener() {
        return listener;
    }

    @Override
    public AddStockDialog setListener(AddFeedListener listener) {
        this.listener = listener;
        if (view != null)
            view.setListener(listener);
        return this;
    }

    @Override
    public void showError(@StringRes int error) {
        view.showError(error);
    }
}