package com.example.workapp.presentation.screen.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsHolder> {

    private final List<CommentsModel> commentsModelList;

    public CommentsAdapter(List<CommentsModel> commentsModelList) {
        this.commentsModelList = commentsModelList;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_recycler_view, parent, false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        CommentsModel commentsModel = commentsModelList.get(position);
        holder.getBinding().commentText.setText(commentsModel.getText());
        holder.getBinding().commentTime.setText(commentsModel.getTime());
    }

    @Override
    public int getItemCount() {
        return commentsModelList.size();
    }
}