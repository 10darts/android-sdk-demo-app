package com.auroralabs.tendarts.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auroralabs.tendarts.app.viewholders.LogViewHolder;
import com.auroralabs.tendarts.domain.entities.LogEntity;

import java.util.List;

/**
 * Created by luismacb on 12/2/18.
 */

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private List<LogEntity> list;
    private LogAdapterInterface listener;

    public LogAdapter(List<LogEntity> list, LogAdapterInterface listener) {
        this.list = list;
        this.listener = listener;
    }

    public interface LogAdapterInterface {
        void onLongClick();
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(LogViewHolder.LAYOUT_RESOURCE_ID, parent, false);
        return new LogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        holder.bind(list.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (listener != null) {
                    listener.onLongClick();
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
