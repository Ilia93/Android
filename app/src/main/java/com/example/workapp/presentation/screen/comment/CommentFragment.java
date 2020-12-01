package com.example.workapp.presentation.screen.comment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workapp.data.network.model.comments.CommentsActionResult;
import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.databinding.CommentsFragmentBinding;

import java.util.List;

public class CommentFragment extends Fragment {

    private CommentsFragmentBinding binding;

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CommentsFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        showComments();
        return view;
    }

    private void showComments() {
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments("", new CommentsActionResult() {

            @Override
            public void onSuccess(List<CommentsModel> comments) {
                CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
                binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.commentsRecyclerView.setAdapter(commentsAdapter);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage();
            }
        });
    }

    private void showToastMessage() {
        Toast toast = Toast.makeText(getContext(), "Failed to load comments server data", Toast.LENGTH_SHORT);
        toast.show();
    }
}