package com.example.workapp.presentation.screen.archive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<AdapterHolder> {
    private final List<WorkModel> listOfWorks;
    private final OnUserClickListener onUserClickListener;

    RecyclerViewAdapter(List<WorkModel> listOfWorks, OnUserClickListener onUserClickListener) {
        this.listOfWorks = listOfWorks;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.archive_recycler_view, parent, false);
        return new AdapterHolder(view, onUserClickListener, listOfWorks);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        WorkModel works = listOfWorks.get(position);
        holder.binding.workInfo.setText(works.getName());
        if (listOfWorks.get(position).isCompleted()) {
            holder.binding.isCompletedFlag.setChecked(true);
        } else {
            holder.binding.isCompletedFlag.setChecked(false);
        }
        holder.binding.workShortInfo.setText(R.string.archive_work_short_information);
        holder.binding.workCompletedInfo.setText(R.string.archive_work_completed_text);
    }

    @Override
    public int getItemCount() {
        return listOfWorks.size();
    }

    public interface OnUserClickListener {
        void onUserClick(WorkModel workModel);
    }
}