package com.example.workapp.presentation.screen.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.comments.CommentsActionResult;
import com.example.workapp.data.network.model.comments.CommentsCloudDataSource;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.presentation.screen.archive.ArchiveActivity;
import com.example.workapp.presentation.screen.main.MainActivity;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;

import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView commentsRecyclerView;
    private CommentsModel commentsModel = new CommentsModel();
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        showComments();
    }

    private void showComments() {
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        CommentsCloudDataSource commentsCloudDataSource = new CommentsCloudDataSource();
        commentsCloudDataSource.getComments(commentsModel.getText(), new CommentsActionResult() {

            @Override
            public void onSuccess(List<CommentsModel> comments) {
                CommentsAdapter commentsAdapter = new CommentsAdapter(CommentActivity.this, comments);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                commentsRecyclerView.setAdapter(commentsAdapter);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}