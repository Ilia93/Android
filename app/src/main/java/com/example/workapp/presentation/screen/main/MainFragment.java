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
    List<MainTemplatesModel> activityTemplates = new ArrayList<>();
    RecyclerView mainRecyclerView, worksTemplatesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        worksTemplatesRecyclerView = view.findViewById(R.id.predefinedWorksRecyclerView);
        mainRecyclerView = view.findViewById(R.id.mainRecyclerView);
        createClickListener();
        showWorks();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
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
                setWorksDataAdapter(works);
                setActivityTemplatesAdapter();
            }

            @Override
            public void onFailure(String message) {
                showToastMessage("Failed to load works");
            }
        });
    }

    private void setWorksDataAdapter(List<WorkModel> works) {
        MainWorksAdapter mainWorksAdapter = new MainWorksAdapter(getContext(), works);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRecyclerView.setAdapter(mainWorksAdapter);
    }

    private void createActivityTemplates() {
        activityTemplates.add(new MainTemplatesModel("Run activity", R.drawable.run_twice));
        activityTemplates.add(new MainTemplatesModel("Walk activity", R.drawable.walking_activity));
        activityTemplates.add(new MainTemplatesModel("Sleep activity", R.drawable.sand_clocks));
        activityTemplates.add(new MainTemplatesModel("Physical activity", R.drawable.physical));
    }

    private void setActivityTemplatesAdapter() {
        createActivityTemplates();
        MainWorkTemplatesAdapter.OnUserClickListener onUserClickListener =
                new MainWorkTemplatesAdapter.OnUserClickListener() {
                    @Override
                    public void onUserClick(@NotNull MainTemplatesModel activityTemplatesModel) {
                        binding.inputWork.setText(activityTemplatesModel.getActivityDescription());
                    }
                };
        MainWorkTemplatesAdapter templates = new MainWorkTemplatesAdapter(getContext(),
                activityTemplates, onUserClickListener);
        worksTemplatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        worksTemplatesRecyclerView.setAdapter(templates);
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
                    public void onResponse(@NonNull Call<WorkModel> call, @NonNull Response<WorkModel> response) {
                        if (response.isSuccessful()) {
                            showToastMessage("Work created");
                            binding.inputWork.setText(null);
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
        Intent serviceIntent = new Intent(getContext(), NotificationService.class);
        serviceIntent.setAction(Intent.ACTION_ANSWER);
        serviceIntent.putExtra(SERVICE_WORK_NAME, workModel.getName());
        serviceIntent.putExtra(SERVICE_NOTIFICATION_ID, "1");
        serviceIntent.putExtra(SERVICE_WORK_ID, workModel.getId());
        getActivity().getApplicationContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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
//TODO fix this place
    private void startTimerActivity() {
        TimerFragment timerFragment = new TimerFragment();
        Bundle timerBundle = new Bundle();
        timerBundle.putString(WORK_ID, workModel.getId());
        timerBundle.putString(WORK_NAME, workModel.getName());
        timerBundle.putString(WORK_OBJECT_ID, workModel.getObjectId());
        timerFragment.setArguments(timerBundle);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.navigation_content_frame, timerFragment);
        fragmentTransaction.addToBackStack("timer_fragment");
        fragmentTransaction.commit();

    }

    public void showToastMessage(String text) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
