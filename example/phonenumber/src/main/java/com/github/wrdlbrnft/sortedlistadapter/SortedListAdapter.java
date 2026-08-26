package com.github.wrdlbrnft.sortedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.SortedList;

import com.github.wrdlbrnft.modularadapter.ModularAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal local stand-in for com.github.wrdlbrnft:sorted-list-adapter (JitPack unavailable).
 */
public abstract class SortedListAdapter<T> extends ModularAdapter<T> {

    public interface Callback {
        void onEditStarted();

        void onEditFinished();
    }

    public interface Editor<T> {
        Editor<T> replaceAll(List<T> items);

        Editor<T> removeAll();

        void commit();
    }

    public static class ComparatorBuilder<T> {
        private final Map<Class<?>, Comparator<?>> order = new HashMap<>();

        public <M extends T> ComparatorBuilder<T> setOrderForModel(Class<M> modelClass, Comparator<M> comparator) {
            order.put(modelClass, comparator);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Comparator<T> build() {
            return (a, b) -> {
                if (a == null && b == null) return 0;
                if (a == null) return -1;
                if (b == null) return 1;
                Comparator<T> cmp = (Comparator<T>) order.get(a.getClass());
                if (cmp != null) {
                    return cmp.compare(a, b);
                }
                return Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
            };
        }
    }

    private final LayoutInflater inflater;
    private final SortedList<T> sortedList;
    private final Comparator<T> comparator;
    private final List<Callback> callbacks = new ArrayList<>();

    public SortedListAdapter(Context context, Class<T> itemClass, Comparator<T> comparator) {
        this.inflater = LayoutInflater.from(context);
        this.comparator = comparator;
        this.sortedList = new SortedList<>(itemClass, new SortedList.Callback<T>() {
            @Override
            public int compare(T a, T b) {
                return comparator.compare(a, b);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return item1 == item2 || (item1 != null && item1.equals(item2));
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
    }

    public void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    protected abstract ViewHolder<? extends T> onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public ViewHolder<? extends T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateViewHolder(inflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<? extends T> holder, int position) {
        //noinspection unchecked
        ((ViewHolder<T>) holder).bind(getItem(position));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<? extends T> holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //noinspection unchecked
            ((ViewHolder<T>) holder).bind(getItem(position));
        }
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public T getItem(int position) {
        return sortedList.get(position);
    }

    public Editor<T> edit() {
        return new EditorImpl();
    }

    private class EditorImpl implements Editor<T> {
        private final List<Runnable> actions = new ArrayList<>();

        @Override
        public Editor<T> replaceAll(List<T> items) {
            actions.add(() -> {
                List<T> copy = new ArrayList<>(items);
                Collections.sort(copy, comparator);
                sortedList.beginBatchedUpdates();
                for (int i = sortedList.size() - 1; i >= 0; i--) {
                    final T current = sortedList.get(i);
                    boolean stillPresent = false;
                    for (T item : copy) {
                        if (areItemsTheSame(current, item)) {
                            stillPresent = true;
                            break;
                        }
                    }
                    if (!stillPresent) {
                        sortedList.remove(current);
                    }
                }
                sortedList.addAll(copy);
                sortedList.endBatchedUpdates();
            });
            return this;
        }

        @Override
        public Editor<T> removeAll() {
            actions.add(() -> {
                sortedList.beginBatchedUpdates();
                while (sortedList.size() > 0) {
                    sortedList.removeItemAt(sortedList.size() - 1);
                }
                sortedList.endBatchedUpdates();
            });
            return this;
        }

        @Override
        public void commit() {
            for (Callback callback : callbacks) {
                callback.onEditStarted();
            }
            for (Runnable action : actions) {
                action.run();
            }
            actions.clear();
            for (Callback callback : callbacks) {
                callback.onEditFinished();
            }
        }

        private boolean areItemsTheSame(T a, T b) {
            return a == b || (a != null && a.equals(b));
        }
    }
}
