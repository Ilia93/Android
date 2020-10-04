package com.example.workapp.presentation.screen.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView
        .Adapter<CommentsAdapter.ViewHolder> {

    private List<CommentsModel> commentsModelList;

    public CommentsAdapter(List<CommentsModel> commentsModelList) {
        this.commentsModelList = commentsModelList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        CommentsModel commentsModel = commentsModelList.get(position);
        holder.commentText.setText(commentsModel.getText());
        holder.commentTime.setText(commentsModel.getTime());
    }

    @Override
    public int getItemCount() {
        return commentsModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView commentText, commentTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
}
