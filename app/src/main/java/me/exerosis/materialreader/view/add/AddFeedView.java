package me.exerosis.materialreader.view.add;

import android.app.Dialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.EditText;

import butterknife.BindString;
import butterknife.BindView;
import me.exerosis.materialreader.R;
import me.exerosis.mvc.butterknife.ButterKnifeFragmentView;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

public class AddFeedView extends ButterKnifeFragmentView implements AddFeed {
    @BindView(R.id.add_feed_input)
    EditText input;
    @BindString(R.string.add_stock_text)
    String text;
    @BindString(R.string.add_stock_add)
    String add;
    @BindString(R.string.add_stock_cancel)
    String cancel;

    private AddFeedListener listener;


    public AddFeedView(LayoutInflater inflater) {
        super(inflater, null, R.layout.add_feed_view);

    }

    @Override
    public Dialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getRoot().getContext());
        builder.setView(getRoot());

        input.setOnEditorActionListener((view1, id, event) -> {
            if (listener != null)
                listener.onAdd(input.getText().toString());
            return true;
        });
        builder.setMessage(text);
        builder.setPositiveButton(add, null);
        builder.setNegativeButton(cancel, (dialog, id) -> {
            if (listener != null)
                listener.onCancel();
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(button -> {
            if (listener != null)
                listener.onAdd(input.getText().toString());
        }));
        Window window = dialog.getWindow();
        if (window != null)
            window.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void showError(@StringRes int error) {
        input.setError(getRoot().getResources().getString(error));
        input.clearComposingText();
    }

    @Override
    public AddFeedListener getListener() {
        return listener;
    }

    @Override
    public AddFeedView setListener(AddFeedListener listener) {
        this.listener = listener;
        return this;
    }
}