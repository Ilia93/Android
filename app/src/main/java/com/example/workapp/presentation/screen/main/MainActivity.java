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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ActivityMainBinding;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;
import com.example.workapp.presentation.service.notifications.NotificationService;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    public Toolbar toolbar;
    List<ActivityTemplatesModel> activityTemplates = new ArrayList<>();
    ActivityMainBinding activityMainBinding;
    RecyclerView mainRecyclerView, worksTemplatesRecyclerView;
    OnClickListener mainScreen = v -> {
        if (v.getId() == R.id.createWork) {
            createWork();
        }
    };
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setScreenElements();
        setClickListeners();
        createActivityTemplates();
        showWorks();
    }

    public void setClickListeners() {
        activityMainBinding.createWork.setOnClickListener(mainScreen);
    }

    private void setScreenElements() {
        toolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_archive_fragment) {
            showToastMessage("Archive clicked");
        } else if (id == R.id.nav_home_fragment) {
            showToastMessage("Home clicked");
        } else if (id == R.id.nav_timer_fragment) {
            showToastMessage("Timer clicked");
        } else if (id == R.id.nav_comments_fragment) {
            showToastMessage("Comments clicked");
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_toolbar) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
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