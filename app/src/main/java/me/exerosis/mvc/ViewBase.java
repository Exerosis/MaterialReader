package me.exerosis.mvc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface ViewBase {
    @NonNull
    View getRoot();

    @Nullable
    Bundle getViewState();
}
