package me.exerosis.mvc.rxjava;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.functions.Func2;

public class ObservableAdapter<ViewHolder extends RecyclerView.ViewHolder, Data> extends RecyclerView.Adapter<ViewHolder> {
    private final Action2<ViewHolder, Data> binder;
    private final Func2<ViewGroup, Integer, ViewHolder> creator;
    private List<Data> data = new ArrayList<>();

    public ObservableAdapter(Observable<List<Data>> dataObservable, Action2<ViewHolder, Data> binder, Func1<ViewGroup, ViewHolder> creator) {
        this(dataObservable, binder, (parent, type) -> creator.call(parent));
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Action2<ViewHolder, Data> binder, Func2<ViewGroup, Integer, ViewHolder> creator) {
        dataObservable.subscribe(data -> {
            this.data = data;
            notifyDataSetChanged();
        });
        this.binder = binder;
        this.creator = creator;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return creator.call(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        binder.call(holder, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}