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
        setToolbar();
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

    private void setToolbar() {
        toolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.archive_activity_toolbar) {
            Intent intent = new Intent(this, ArchiveActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.timer_activity_toolbar) {
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.home_activity_toolbar) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.comments_activity_toolbar) {
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
