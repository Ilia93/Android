package com.example.workapp.presentation.screen.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsActionResult;
import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;

import java.util.List;

public class CommentFragment extends Fragment {
    private RecyclerView commentsRecyclerView;
    private CommentsModel commentsModel = new CommentsModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment, container, false);
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        showComments();
        return view;
    }

    private void showComments() {
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments(commentsModel.getText(), new CommentsActionResult() {

            @Override
            public void onSuccess(List<CommentsModel> comments) {
                CommentsAdapter commentsAdapter = new CommentsAdapter(getActivity().getApplicationContext(), comments);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                commentsRecyclerView.setAdapter(commentsAdapter);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}