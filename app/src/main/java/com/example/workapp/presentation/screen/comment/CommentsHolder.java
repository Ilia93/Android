package com.example.workapp.presentation.screen.comment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.databinding.CommentsRecyclerViewBinding;

public class CommentsHolder extends RecyclerView.ViewHolder {

    private CommentsRecyclerViewBinding binding;

    public CommentsHolder(@NonNull View itemView) {
        super(itemView);
        binding = CommentsRecyclerViewBinding.bind(itemView);
    }

    public CommentsRecyclerViewBinding getBinding() {
        return binding;
    }
}
