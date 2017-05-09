package me.exerosis.materialreader;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.exerosis.mvc.FragmentView;

public class ButterKnifeFragmentView extends FragmentView {
    public ButterKnifeFragmentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @LayoutRes int layout) {
        super(inflater, container, layout);
        ButterKnife.bind(this, getRoot());
    }
}
