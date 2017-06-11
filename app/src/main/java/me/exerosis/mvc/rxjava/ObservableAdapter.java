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
    private final List<Action2<Data, ViewHolder>> listeners = new ArrayList<>();
    private final Action2<ViewHolder, Data> binder;
    private final Func2<ViewGroup, Integer, ViewHolder> creator;
    private final Func1<Data, Integer> type;
    private List<Data> data = new ArrayList<>();
    private boolean selectSingle = false;
    private ViewHolder selected;

    public ObservableAdapter(Observable<List<Data>> dataObservable, Action2<ViewHolder, Data> binder, Func1<ViewGroup, ViewHolder> creator) {
        this(dataObservable, entry -> 0, binder, creator, null);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Func1<Data, Integer> type, Action2<ViewHolder, Data> binder, Func1<ViewGroup, ViewHolder> creator) {
        this(dataObservable, type, binder, creator, null);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Action2<ViewHolder, Data> binder, Func1<ViewGroup, ViewHolder> creator, Action2<Data, ViewHolder> clickListener) {
        this(dataObservable, entry -> 0, binder, (parent, type) -> creator.call(parent), clickListener);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Func1<Data, Integer> viewTyper, Action2<ViewHolder, Data> binder, Func1<ViewGroup, ViewHolder> creator, Action2<Data, ViewHolder> clickListener) {
        this(dataObservable, viewTyper, binder, (parent, type) -> creator.call(parent), clickListener);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Action2<ViewHolder, Data> binder, Func2<ViewGroup, Integer, ViewHolder> creator) {
        this(dataObservable, entry -> 0, binder, creator);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Func1<Data, Integer> type, Action2<ViewHolder, Data> binder, Func2<ViewGroup, Integer, ViewHolder> creator) {
        this(dataObservable, type, binder, creator, null);
    }

    public ObservableAdapter(Observable<List<Data>> dataObservable, Func1<Data, Integer> type, Action2<ViewHolder, Data> binder, Func2<ViewGroup, Integer, ViewHolder> creator, Action2<Data, ViewHolder> clickListener) {
        this.type = type;
        this.binder = binder;
        this.creator = creator;
        if (clickListener != null)
            listeners.add(clickListener);
        dataObservable.subscribe(data -> {
            this.data = data;
            notifyDataSetChanged();
        }, Throwable::printStackTrace);
    }


    public ObservableAdapter<ViewHolder, Data> selectSingle() {
        selectSingle ^= true;
        return this;
    }

    public Action2<Data, ViewHolder> onClick(Action2<Data, ViewHolder> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            return null;
        } else
            listeners.add(listener);
        return listener;
    }

    @Override
    public int getItemViewType(int position) {
        return type.call(data.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return creator.call(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            for (Action2<Data, ViewHolder> listener : listeners)
                listener.call(data.get(position), holder);
            if (selectSingle) {
                if (selected != null)
                    selected.itemView.setSelected(false);
                selected = holder;
                selected.itemView.setSelected(true);
            }
        });
        binder.call(holder, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}