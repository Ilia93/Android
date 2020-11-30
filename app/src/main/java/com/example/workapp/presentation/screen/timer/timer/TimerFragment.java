package com.example.workapp.presentation.screen.timer.timer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.databinding.TimerFragmentBinding;
import com.example.workapp.presentation.screen.comment.CommentFragment;
import com.example.workapp.presentation.screen.main.MainFragment;
import com.example.workapp.presentation.screen.timer.operations.TimerOperations;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerFragment extends Fragment implements CommentDialog.DialogListener {

    public static Date timeOfTimerStart, timeOfTimerPauseStart, timeOfTimerPauseFinish;
    public static boolean isStarted = false;
    public static boolean isPaused = false;
    public static boolean isResumed = false;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public Date timeOfFinish;
    TimerModel timerModel = new TimerModel();
    WorkModel workModel = new WorkModel();
    CommentsModel commentsModel = new CommentsModel();
    TimerOperations timerOperations = new TimerOperations();
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment stopTimerFragment = new StopTimerFragment();
    Timer timer;
    TimerTask timerTask;
    Fragment commentFragment = new CommentFragment();
    TimerFragmentBinding binding;
    Handler uiHandler = new Handler();
    Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TimerFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setClickListeners();
        initializeMenuModel();
        setMenuAdapter();
        return view;
    }

    public void onResume() {
        super.onResume();
        saveData();
        onTimerStarted();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setClickListeners() {
        binding.stopTimer.setOnClickListener(v -> onTimerStopped());
        binding.pauseTimer.setOnClickListener(v -> onTimerPaused());
        binding.resumeTimer.setOnClickListener(v -> onTimerResumed());
    }

    @NotNull
    private List<TimerMenuModel> initializeMenuModel() {
        List<TimerMenuModel> menuModelList = new ArrayList<>();
        menuModelList.add
                (new TimerMenuModel("Watch comments", R.drawable.ic_watch_comments_48dp));
        menuModelList.add
                (new TimerMenuModel("Add comment", R.drawable.ic_add_commen_48dp));
        return menuModelList;
    }

    private void setMenuAdapter() {
        TimerMenuAdapter.OnUserClickListener onUserClickListener = timerMenuModel -> {
            if (timerMenuModel.getText().equals("Watch comments")) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    fragmentManager = activity.getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction()
                            .replace(R.id.navigation_content_frame, commentFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            } else if (timerMenuModel.getText().equals("Add comment")) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    fragmentManager = activity.getSupportFragmentManager();
                    CommentDialog commentDialog = new CommentDialog();
                    commentDialog.setTargetFragment(TimerFragment.this, 300);
                    commentDialog.show(fragmentManager, "dialog");
                }
            }
        };
        TimerMenuAdapter timerMenuAdapter = new TimerMenuAdapter
                (getActivity(), initializeMenuModel(), onUserClickListener);
        binding.timerRecyclerView.setLayoutManager
                (new GridLayoutManager(getContext(), 2));
        binding.timerRecyclerView.setAdapter(timerMenuAdapter);
    }

    public void onTimerStarted() {
        isStarted = true;
        timeOfTimerStart = timerOperations.getCalendarInstance();
        timerModel.setStartTime(timerOperations.setTime());
        if (getArguments() != null) {
            timerModel.setWorkId(getArguments().getString("workId"));
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runnable = () -> binding.timerStartView.setText(timerOperations.setTime());
                    uiHandler.postDelayed(runnable, 1000);
                }
            };
            timer.schedule(timerTask, 0);
            showToastMessage("Timer started");
        }
    }

    public void onTimerStopped() {
        isStarted = false;
        isResumed = false;
        isPaused = false;
        if (getArguments() != null) {
            workModel.setId(getArguments().getString("workId"));
            workModel.setName(getArguments().getString("workName"));
            workModel.setCompleted(true);
            workModel.setObjectId(getArguments().getString("workObjectId"));
            Call<WorkModel> call = NetworkClient.getInstance().getWorkApi().updateWork(workModel
                    .getObjectId(), workModel);
            call.enqueue(new Callback<WorkModel>() {
                @Override
                public void onResponse(@NonNull Call<WorkModel> call,
                                       @NonNull Response<WorkModel> response) {
                    if (response.isSuccessful()) {
                        try {
                            timerModel.setFinishTime(timerOperations.setTime());
                            timeOfFinish = timerOperations.getCalendarInstance();
                            timerModel.setElapsedTime(timerOperations.calculateDifference
                                    (timeOfTimerStart, timeOfFinish));
                            Call<TimerModel> elapsedTimeCall = NetworkClient.getInstance().getTimerAPI()
                                    .createTimer(timerModel);
                            createTimer(elapsedTimeCall);
                            stopService();
                        } catch (NullPointerException exception) {
                            showToastMessage("Timer isn't created");
                        }

                    } else {
                        try {
                            if (response.errorBody() != null) {
                                showToastMessage(response.errorBody().string());
                            }
                        } catch (IOException e) {
                            showToastMessage("IO exception");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<WorkModel> call, @NonNull Throwable t) {
                    showToastMessage("Failed to update work" );
                }
            });
        }
    }

    private void createTimer(@NotNull Call<TimerModel> call) {
        call.enqueue(new Callback<TimerModel>() {
            @Override
            public void onResponse(@NonNull Call<TimerModel> call,
                                   @NonNull Response<TimerModel> response) {
                if (response.isSuccessful()) {
                    try {
                        timer.cancel();
                    } catch (NullPointerException exception) {
                        showToastMessage("Timer didn't created");
                    }
                    showToastMessage("Timer stopped");
                    replaceFragment(stopTimerFragment);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("Failed to cancel timer");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimerModel> call, @NonNull Throwable t) {
                showToastMessage("Failed to create timer");
            }
        });
    }

    public void onTimerResumed() {
        try {
            if (!isResumed) {
                throw new NullPointerException();
            } else {
                isResumed = true;
                timeOfTimerPauseFinish = timerOperations.getCalendarInstance();
                timerModel.setTimeInPause(timerOperations.calculateDifference
                        (timeOfTimerPauseStart, timeOfTimerPauseFinish));
                binding.timerEndPauseView.setText(timerOperations.setTime());
            }
        } catch (NullPointerException exception) {
            showToastMessage("Timer didn't created");
        }
    }

    public void onTimerPaused() {
        try {
            if (!isPaused) {
                throw new NullPointerException();
            } else {
                isPaused = true;
                timeOfTimerPauseStart = timerOperations.getCalendarInstance();
                binding.timerPauseStartView.setText(timerOperations.setTime());
            }
        } catch (NullPointerException exception) {
            showToastMessage("Timer didn't created");
        }
    }

    private void createCommentOnServer() {
        Call<CommentsModel> call = NetworkClient.getInstance().getCommentAPI().createComment(commentsModel);
        call.enqueue(new Callback<CommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentsModel> call,
                                   @NonNull Response<CommentsModel> response) {
                if (response.isSuccessful()) {
                    showToastMessage("Comment created");
                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("Failed to create comment");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsModel> call, @NonNull Throwable t) {
                showToastMessage("Failed to create comment");
            }
        });
    }

    private void replaceFragment(Fragment fragmentName) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction()
                    .add(R.id.navigation_content_frame, fragmentName);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void stopService() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            try {
                getActivity().getApplicationContext().unbindService(MainFragment.serviceConnection);
            } catch (IllegalArgumentException exception) {
                showToastMessage("Work don't exist");
            }
        }
    }

    @Override
    public void onPositiveClicked(String inputText) {
        if (getArguments() != null) {
            commentsModel.setText(inputText);
            commentsModel.setTime(timerOperations.setTime());
            commentsModel.setWorkId(getArguments().getString("workId"));
            createCommentOnServer();
        } else {
            showToastMessage("Work don't exist");
        }
    }

    private void saveData() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (isStarted) {
                setData(R.id.timerStartView, timeOfTimerStart);
            }
            if (isPaused) {
                setData(R.id.timerPauseStartView, timeOfTimerPauseStart);
            }
            if (isResumed) {
                setData(R.id.timerEndPauseView, timeOfTimerPauseFinish);
            }
        }
    }

    private void setData(Integer id, Date date) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((TextView) getActivity().findViewById(id)).setText(simpleDateFormat.format(date));
        }
    }
}