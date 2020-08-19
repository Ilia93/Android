package com.example.workapp.presentation.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ActivityMainBinding;
import com.example.workapp.presentation.screen.archive.ArchiveActivity;
import com.example.workapp.presentation.screen.comment.CommentActivity;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;
import com.example.workapp.presentation.service.notifications.NotificationService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static NotificationService notificationService;
    public static ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            notificationService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
    public final String SERVICE_WORK_NAME = "workNameForService";
    public final String SERVICE_NOTIFICATION_ID = "notificationId";
    public final String SERVICE_WORK_ID = "workId";
    public final String WORK_ID = "workId";
    public final String WORK_NAME = "workName";
    public final String WORK_OBJECT_ID = "workObjectId";
    public WorkModel workModel = new WorkModel();
    List<ActivityTemplatesModel> activityTemplates = new ArrayList<>();
    Toolbar toolbar;
    ActivityMainBinding activityMainBinding;
    RecyclerView mainRecyclerView, worksTemplatesRecyclerView;

    OnClickListener mainScreen = v -> {
        if (v.getId() == R.id.createWork) {
            createWork();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setClickListeners();
        setToolbar();
        createActivityTemplates();
        showWorks();
    }

    public void setClickListeners() {
        activityMainBinding.createWork.setOnClickListener(mainScreen);
    }

    public void showWorks() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setWorksDataAdapter(works);
                setActivityTemplatesAdapter();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void createWork() {
        workModel.setName(activityMainBinding.inputWork.getText().toString());
        try {
            if (workModel.getName().equals("")) {
                throw new NullPointerException();
            } else {
                Call<WorkModel> work = NetworkClient.getWorkApi().createWork(workModel);
                work.enqueue(new Callback<WorkModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WorkModel> call, @NonNull Response<WorkModel> response) {
                        if (response.isSuccessful()) {
                            showToastMessage("Work created");
                            activityMainBinding.inputWork.setText(null);
                            getWorkObjectIdByName();
                            startWorkService();
                        } else {
                            try {
                                if (response.errorBody() != null) {
                                    showToastMessage(response.errorBody().string());
                                }
                            } catch (IOException e) {
                                showToastMessage("Error occurred");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WorkModel> call, @NonNull Throwable t) {
                        showToastMessage(t.getMessage());
                    }
                });
            }
        } catch (NullPointerException exception) {
            showToastMessage("Work name can't be empty");
        }
    }

    private void getWorkObjectIdByName() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWorkObjectId(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setWorkObjectId(works);
                startTimerActivity();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setWorkObjectId(@NotNull List<WorkModel> works) {
        for (int i = 0; i < works.size(); i++) {
            if (workModel.getId().equals(works.get(i).getId())) {
                workModel.setObjectId(works.get(i).getObjectId());
            }
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setWorksDataAdapter(List<WorkModel> works) {
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        DataAdapterMain dataAdapterMain = new DataAdapterMain(this, works);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(dataAdapterMain);
    }

    private void createActivityTemplates() {
        activityTemplates.add(new ActivityTemplatesModel("Run activity", R.drawable.run_twice));
        activityTemplates.add(new ActivityTemplatesModel("Walk activity", R.drawable.walking_activity));
        activityTemplates.add(new ActivityTemplatesModel("Sleep activity", R.drawable.sand_clocks));
        activityTemplates.add(new ActivityTemplatesModel("Physical activity", R.drawable.physical));
    }

    private void setActivityTemplatesAdapter() {
        worksTemplatesRecyclerView = findViewById(R.id.predefinedWorksRecyclerView);
        ActivityTemplatesAdapter.OnUserClickListener onUserClickListener =
                new ActivityTemplatesAdapter.OnUserClickListener() {
                    @Override
                    public void onUserClick(@NotNull ActivityTemplatesModel activityTemplatesModel) {
                        activityMainBinding.inputWork.setText(activityTemplatesModel.getActivityDescription());
                    }
                };
        ActivityTemplatesAdapter templates = new ActivityTemplatesAdapter(this,
                activityTemplates, onUserClickListener);
        worksTemplatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        worksTemplatesRecyclerView.setAdapter(templates);
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

    private void startTimerActivity() {
        Intent timerIntent = new Intent(MainActivity.this, TimerActivity.class);
        timerIntent.putExtra(WORK_NAME, workModel.getName());
        timerIntent.putExtra(WORK_ID, workModel.getId());
        timerIntent.putExtra(WORK_OBJECT_ID, workModel.getObjectId());
        startActivity(timerIntent);
    }

    private void startWorkService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.setAction(Intent.ACTION_ANSWER);
        serviceIntent.putExtra(SERVICE_WORK_NAME, workModel.getName());
        serviceIntent.putExtra(SERVICE_NOTIFICATION_ID, "1");
        serviceIntent.putExtra(SERVICE_WORK_ID, workModel.getId());
        getApplicationContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}