package me.exerosis.mvc.rxjava;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.functions.Func2;

public class ObservableAdapter<ViewHolder extends RecyclerView.ViewHolder, Data> extends RecyclerView.Adapter<ViewHolder> {
    private final List<Action2<ViewHolder, Data>> listeners = new ArrayList<>();
    private final Action2<ViewHolder, Data> binder;
    private final Func2<ViewGroup, Integer, ViewHolder> creator;
    private final Observable<Data> data;
    private final Func1<Data, Integer> typer;
    private List<Data> allData = new ArrayList<>();
    private final SortedList<Data> dataList;

    public static <ViewHolder extends RecyclerView.ViewHolder, Data> ObservableAdapterBuilder<ViewHolder, Data> build(Observable<Data> data, Class<?> clazz, Action2<ViewHolder, Data> binder) {
        return new ObservableAdapterBuilder<>(data, clazz, binder);
    }

    public static class ObservableAdapterBuilder<ViewHolder extends RecyclerView.ViewHolder, Data> {
        private final Observable<Data> data;
        private final Class<?> clazz;
        private final Action2<ViewHolder, Data> binder;
        private Action2<ViewHolder, Data> clickListener;
        private Comparator<Data> sorter;

        public ObservableAdapterBuilder(Observable<Data> data, Class<?> clazz, Action2<ViewHolder, Data> binder) {
            this.data = data;
            this.clazz = clazz;
            this.binder = binder;
        }

        public ObservableAdapterBuilder<ViewHolder, Data> sortWith(Comparator<Data> sorter) {
            this.sorter = sorter;
            return this;
        }

        public ObservableAdapterBuilder<ViewHolder, Data> onClick(Action1<Data> clickListener) {
            return onClick(((viewHolder, data) -> clickListener.call(data)));
        }

        public ObservableAdapterBuilder<ViewHolder, Data> onClick(Action2<ViewHolder, Data> clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public ObservableAdapter<ViewHolder, Data> withTypes(Func1<Data, Integer> typer, Func2<ViewGroup, Integer, ViewHolder> creator) {
            return new ObservableAdapter<>(data, clazz, typer, binder, creator, clickListener, sorter);
        }

        public ObservableAdapter<ViewHolder, Data> withoutTypes(Func1<ViewGroup, ViewHolder> creator) {
            return withTypes(data -> 0, (viewGroup, type) -> creator.call(viewGroup));
        }
    }

    @SuppressWarnings("unchecked")
    private ObservableAdapter(Observable<Data> data, Class<?> clazz, Func1<Data, Integer> typer, Action2<ViewHolder, Data> binder, Func2<ViewGroup, Integer, ViewHolder> creator, Action2<ViewHolder, Data> clickListener, Comparator<Data> sorter) {
        this.data = data;
        this.typer = typer;
        this.binder = binder;
        this.creator = creator;

        dataList = new SortedList<>((Class<Data>) clazz, new SortedList.Callback<Data>() {
            @Override
            public int compare(Data one, Data two) {
                if (sorter == null)
                    return 0;
                return sorter.compare(one, two);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Data oldItem, Data newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Data item1, Data item2) {
                return item1.equals(item2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });

        if (clickListener != null)
            listeners.add(clickListener);

        data.subscribe(newData -> {
            allData.add(newData);
            dataList.add(newData);
        }, Throwable::printStackTrace);
    }

    public void refresh() {
        data.toList().subscribe(newData -> {
            for (Data data : allData)
                if (!newData.contains(data))
                    dataList.remove(data);
            for (Data data : newData)
                if (!allData.contains(data))
                    dataList.add(data);
            allData = newData;
        }, Throwable::printStackTrace);
    }


    public Action2<ViewHolder, Data> onClick(Action2<ViewHolder, Data> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            return null;
        } else
            listeners.add(listener);
        return listener;
    }

    @Override
    public int getItemViewType(int position) {
        return typer.call(dataList.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return creator.call(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            for (Action2<ViewHolder, Data> listener : listeners)
                listener.call(holder, dataList.get(position));
        });
        binder.call(holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}