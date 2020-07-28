package com.example.workapp.presentation.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.ActivityMainBinding;
import com.example.workapp.presentation.screen.archive.CompletedWorks;
import com.example.workapp.presentation.screen.timer.timer.TimerActivity;
import com.example.workapp.presentation.service.notifications.NotificationService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
    ActivityMainBinding activityMainBinding;

    OnClickListener mainScreen = v -> {
        switch (v.getId()) {
            case R.id.archive:
                moveToArchiveScreen();
                break;
            case R.id.next:
                onNextButtonClicked();
                break;
            case R.id.createWork:
                createWork();
                break;
            case R.id.run:
                insertPredefinedWorkName(R.string.main_run_activity);
                break;
            case R.id.sleep:
                insertPredefinedWorkName(R.string.main_sleep_activity);
                break;
            case R.id.walk:
                insertPredefinedWorkName(R.string.main_walk_activity);
                break;
            case R.id.physical_work:
                insertPredefinedWorkName(R.string.main_physical_activity);
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setClickListeners();
        showWorks();
    }

    public void setClickListeners() {
        activityMainBinding.archive.setOnClickListener(mainScreen);
        activityMainBinding.next.setOnClickListener(mainScreen);
        activityMainBinding.createWork.setOnClickListener(mainScreen);
        activityMainBinding.run.setOnClickListener(mainScreen);
        activityMainBinding.walk.setOnClickListener(mainScreen);
        activityMainBinding.sleep.setOnClickListener(mainScreen);
        activityMainBinding.physicalWork.setOnClickListener(mainScreen);
    }

    public void showWorks() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                switch (works.size()) {
                    case 0:
                        handleEmptyWorks();
                        break;
                    case 1:
                        handleOneWork(works);
                        break;
                    case 2:
                        handleTwoWorks(works);
                        break;
                    case 3:
                        handleThreeWorks(works);
                        break;
                    case 4:
                        handleFourWorks(works);
                        break;
                    default:
                        handleMoreTHenFourWorks(works);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void handleEmptyWorks() {
        activityMainBinding.firstWork.setText(R.string.main_empty_works);
    }

    private void handleOneWork(List<WorkModel> works) {
        displayWorkName(activityMainBinding.firstWork, 1, works);
    }

    private void handleTwoWorks(List<WorkModel> works) {
        displayWorkName(activityMainBinding.firstWork, 1, works);
        displayWorkName(activityMainBinding.secondWork, 2, works);

    }

    private void handleThreeWorks(List<WorkModel> works) {
        displayWorkName(activityMainBinding.firstWork, 1, works);
        displayWorkName(activityMainBinding.secondWork, 2, works);
        displayWorkName(activityMainBinding.thirdWork, 3, works);

    }

    private void handleFourWorks(List<WorkModel> works) {
        displayWorkName(activityMainBinding.firstWork, 1, works);
        displayWorkName(activityMainBinding.secondWork, 2, works);
        displayWorkName(activityMainBinding.thirdWork, 3, works);
        displayWorkName(activityMainBinding.fourthWork, 4, works);
    }

    private void handleMoreTHenFourWorks(List<WorkModel> works) {
        displayWorkName(activityMainBinding.firstWork, 1, works);
        displayWorkName(activityMainBinding.secondWork, 2, works);
        displayWorkName(activityMainBinding.thirdWork, 3, works);
        displayWorkName(activityMainBinding.fourthWork, 4, works);
        displayWorkName(activityMainBinding.fifthWork, 5, works);
    }

    public void displayWorkName(@NonNull TextView textView, Integer index,
                                @NonNull List<WorkModel> works) {

        String text = getString(R.string.main_work_name, works.get(works.size() - index).getName());
        textView.setText(text);
    }

    private void onNextButtonClicked() {
        Intent timerIntent = new Intent(MainActivity.this, TimerActivity.class);
        try {
            if (workModel.getName().equals("")) {
                throw new NullPointerException();
            } else {
                timerIntent.putExtra(WORK_NAME, workModel.getName());
                timerIntent.putExtra(WORK_ID, workModel.getId());
                timerIntent.putExtra(WORK_OBJECT_ID, workModel.getObjectId());
                startActivity(timerIntent);
            }
        } catch (NullPointerException exception) {
            showToastMessage("Work name is empty");
            activityMainBinding.next.setEnabled(false);
        }
    }

    private void moveToArchiveScreen() {
        Intent archiveIntent = new Intent(MainActivity.this, CompletedWorks.class);
        startActivity(archiveIntent);
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
                            activityMainBinding.next.setEnabled(true);
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

    private void insertPredefinedWorkName(Integer preDefinedWork) {
        activityMainBinding.inputWork.setText(preDefinedWork);
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