package com.example.workapp.presentation.screen.archive;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ArchiveRecyclerViewBinding;

import java.util.List;

public class AdapterHolder extends RecyclerView.ViewHolder {

    public ArchiveRecyclerViewBinding binding;

    public AdapterHolder(@NonNull View itemView,
                         RecyclerViewAdapter.OnUserClickListener onUserClickListener,
                         List<WorkModel> listOfWorks) {
        super(itemView);
        binding = ArchiveRecyclerViewBinding.bind(itemView);

        itemView.setOnClickListener(v -> {
            WorkModel workModel = listOfWorks.get(getLayoutPosition());
            onUserClickListener.onUserClick(workModel);
        });
    }
}
