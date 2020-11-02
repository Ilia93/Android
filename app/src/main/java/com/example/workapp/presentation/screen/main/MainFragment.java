package com.example.workapp.presentation.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.work.WorkActionResult;
import com.example.workapp.data.network.model.work.WorkCloudDataSource;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.MainFragmentBinding;
import com.example.workapp.presentation.screen.main.recyclerview.WorkTemplatesModel;
import com.example.workapp.presentation.screen.timer.timer.TimerFragment;
import com.example.workapp.presentation.service.notifications.NotificationService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

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
    MainFragmentBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    List<Object> activities = new ArrayList<>();
    RecyclerView mainRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mainRecyclerView = view.findViewById(R.id.mainWorksRecyclerView);
        createClickListener();
        showWorks();
        return view;
    }

    private void createClickListener() {
        binding.createWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWork();
            }
        });
    }

    public void showWorks() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWork(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                createActivityTemplates();
                setActivityTemplatesAdapter(works);
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load works");
            }
        });
    }

    private void setActivityTemplatesAdapter(List<WorkModel> works) {
        activities.addAll(works);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainWorkAdapter.OnUserCardClickListener onUserCardClickListener = mainTemplatesModel -> {
            binding.inputWork.setText(mainTemplatesModel.getActivityDescription());
            createWork();
        };
        MainWorkAdapter workAdapter = new MainWorkAdapter(activities, onUserCardClickListener);
        mainRecyclerView.setAdapter(workAdapter);
    }

    private void createActivityTemplates() {
        activities.add("Activity templates");
        activities.add
                (new WorkTemplatesModel(getString(R.string.main_run_activity), R.drawable.run_twice_400_225));
        activities.add
                (new WorkTemplatesModel(getString(R.string.main_walk_activity), R.drawable.walking_activity_400_225));
        activities.add
                (new WorkTemplatesModel(getString(R.string.main_sleep_activity), R.drawable.sand_clocks_400_225));
        activities.add
                (new WorkTemplatesModel(getString(R.string.main_physical_activity), R.drawable.gym_400_225));
        activities.add("Latest works");
    }

    private void createWork() {
        workModel.setName(binding.inputWork.getText().toString());
        try {
            if (workModel.getName().equals("")) {
                throw new NullPointerException();
            } else {
                Call<WorkModel> work = NetworkClient.getWorkApi().createWork(workModel);
                work.enqueue(new Callback<WorkModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WorkModel> call,
                                           @NonNull Response<WorkModel> response) {
                        if (response.isSuccessful()) {
                            showToastMessage("Work created");
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

    private void startWorkService() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent serviceIntent = new Intent(getContext(), NotificationService.class);
            serviceIntent.setAction(Intent.ACTION_ANSWER);
            serviceIntent.putExtra(SERVICE_WORK_NAME, workModel.getName());
            serviceIntent.putExtra(SERVICE_NOTIFICATION_ID, "1");
            serviceIntent.putExtra(SERVICE_WORK_ID, workModel.getId());
            getActivity().getApplicationContext().bindService(
                    serviceIntent,
                    serviceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    private void getWorkObjectIdByName() {
        WorkCloudDataSource workCloudDataSource = new WorkCloudDataSource();
        workCloudDataSource.getWorkObjectId(workModel.getName(), new WorkActionResult() {
            @Override
            public void onSuccess(List<WorkModel> works) {
                setWorkObjectId(works);
                startTimer();
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

    private void startTimer() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            TimerFragment timerFragment = new TimerFragment();
            Bundle timerBundle = new Bundle();
            timerBundle.putString(WORK_ID, workModel.getId());
            timerBundle.putString(WORK_NAME, workModel.getName());
            timerBundle.putString(WORK_OBJECT_ID, workModel.getObjectId());
            timerFragment.setArguments(timerBundle);
            fragmentManager = activity.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction()
                    .add(R.id.navigation_content_frame, timerFragment);
            fragmentTransaction.addToBackStack("timer_fragment");
            fragmentTransaction.commit();
        }
    }

    public void showToastMessage(String text) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}