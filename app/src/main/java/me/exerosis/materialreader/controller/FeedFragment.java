package me.exerosis.materialreader.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.exerosis.materialreader.view.FeedView;

public class FeedFragment extends Fragment implements FeedController {
    private FeedView view;

    @Override
    public void onCreate(@Nullable Bundle in) {
        super.onCreate(in);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new FeedView(inflater, container);
        return view.getRoot();
    }
}