package com.example.workapp.presentation.screen.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.presentation.screen.comment.CommentActivity;
import com.example.workapp.presentation.screen.main.MainActivity;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private final String CLICKED_ID_ARG = "NOTE_CLICKED_ID_ARG";
    RecyclerView recyclerView;
    Toolbar toolbar;
    private WorkModel workModel = new WorkModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        setToolbar();
        displayCompletedWorks();
    }

    private void displayCompletedWorks() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setDataAdapter(works);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Error");
            }
        });
    }

    private void setDataAdapter(List<WorkModel> works) {
        recyclerView = findViewById(R.id.recyclerView);
        ArchiveAdapter.OnUserClickListener onUserClickListener = new ArchiveAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(@NotNull WorkModel workModel) {
                Intent intent = new Intent(ArchiveActivity.this, CompletedWorksInformation.class);
                intent.putExtra(CLICKED_ID_ARG, workModel.getId());
                startActivity(intent);
            }
        };
        ArchiveAdapter archiveAdapter = new ArchiveAdapter(this, works, onUserClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(archiveAdapter);
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
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