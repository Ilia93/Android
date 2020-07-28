package com.example.workapp.presentation.screen.timer.timer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.presentation.screen.main.MainActivity;
import com.example.workapp.presentation.screen.timer.comments.AddTimerCommentFragment;
import com.example.workapp.presentation.screen.timer.comments.ShowTimerCommentsFragment;
import com.example.workapp.presentation.screen.timer.operations.TimerOperations;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerActivity extends AppCompatActivity implements StartTimerFragment.TimerClickListener,
        StopTimerFragment.TimerCloseable, AddTimerCommentFragment.CreateCommentsListener {

    public static Date timeOfTimerStart, timeOfTimerPauseStart, timeOfTimerPauseFinish;
    public static boolean isStarted, isPaused, isResumed = false;
    public Date timeOfFinish;
    TimerModel timerModel = new TimerModel();
    WorkModel workModel = new WorkModel();
    CommentsModel commentsModel = new CommentsModel();
    TimerOperations timerOperations = new TimerOperations();
    FragmentManager myFragmentManager;
    FragmentTransaction transaction;
    Fragment startTimerFragment = new StartTimerFragment();
    Fragment stopTimerFragment = new StopTimerFragment();
    Fragment watchTimerComments = new ShowTimerCommentsFragment();
    Fragment addCommentFragment = new AddTimerCommentFragment();
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        if (savedInstanceState == null) {
            myFragmentManager = getSupportFragmentManager();
            replaceFragment(startTimerFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isStarted) {
            startTimerFragment.getView().findViewById(R.id.startTimer).setEnabled(false);
        }
    }

    @Override
    public void onTimerStarted() {
        isStarted = true;
        setButtonsVisibility();
        timeOfTimerStart = timerOperations.getCalendarInstance();
        timerModel.setStartTime(timerOperations.setTime());
        timerModel.setWorkId(getIntent().getStringExtra("workId"));
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> ((TextView) Objects.requireNonNull(startTimerFragment.getView())
                        .findViewById(R.id.timerStartView))
                        .setText(timerOperations.setTime()));
            }
        };
        timer.schedule(timerTask, 0);
        showToastMessage("Timer started");
    }

    @Override
    public void onTimerStopped() {
        isStarted = false;
        isResumed = false;
        isPaused = false;
        startTimerFragment.getView().findViewById(R.id.startTimer).setEnabled(true);
        workModel.setId(getIntent().getStringExtra("workId"));
        workModel.setName(getIntent().getStringExtra("workName"));
        workModel.setCompleted(true);
        workModel.setObjectId(getIntent().getStringExtra("workObjectId"));
        Call<WorkModel> call = NetworkClient.getWorkApi().updateWork(workModel
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
                        Call<TimerModel> elapsedTimeCall = NetworkClient.getTimerAPI()
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
//TODO Race condition, разобраться с логикой, перенести код в successes

            @Override
            public void onFailure(@NonNull Call<WorkModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });

    }

    @Override
    public void onTimerResumed() {
        try {
            isResumed = true;
            timeOfTimerPauseFinish = timerOperations.getCalendarInstance();
            timerModel.setTimeInPause(timerOperations.calculateDifference
                    (timeOfTimerPauseStart, timeOfTimerPauseFinish));
            ((TextView) Objects.requireNonNull(startTimerFragment.getView())
                    .findViewById(R.id.timerEndPauseView))
                    .setText(timerOperations.setTime());
        } catch (NullPointerException exception) {
            showToastMessage("Timer isn't created");
        }
    }


    @Override
    public void onTimerPaused() {
        try {
            if (!isStarted) {
                throw new NullPointerException();
            } else {
                isPaused = true;
                timeOfTimerPauseStart = timerOperations.getCalendarInstance();
                ((TextView) startTimerFragment.getView().findViewById(R.id.timerPauseStartView))
                        .setText(timerOperations.setTime());
            }
        } catch (NullPointerException exception) {
            showToastMessage("Timer isn't created");
        }
    }

    @Override
    public void onTimerAddComment() {
        replaceFragment(addCommentFragment);
    }

    @Override
    public void onTimerWatchComments() {
        replaceFragment(watchTimerComments);
    }

    @Override
    public void onTimerExit() {
        Intent intent = new Intent(TimerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTimerClosed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void createComment() {
        addCommentFragment.getFragmentManager().findFragmentById(R.id.FragmentFrame);
        try {
            if (((EditText) addCommentFragment.getView().findViewById
                    (R.id.inputComment)).getText().toString().equals("")) {
                throw new NullPointerException();
            } else {
                commentsModel.setText(((EditText) addCommentFragment.getView().findViewById
                        (R.id.inputComment)).getText().toString());
                ((EditText) addCommentFragment.getView().findViewById(R.id.inputComment)).setText(null);
                commentsModel.setTime(timerOperations.setTime());
                commentsModel.setWorkId(getIntent().getStringExtra("workId"));
                createCommentOnServer();
            }
        } catch (NullPointerException exception) {
            showToastMessage("Comment can't be empty");
        }
    }

    private void createCommentOnServer() {
        Call<CommentsModel> call = NetworkClient.getCommentAPI().createComment(commentsModel);
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
                        showToastMessage("Shit happened");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentsModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void stopService() {
        try {
            getApplicationContext().unbindService(MainActivity.serviceConnection);
        } catch (IllegalArgumentException exception) {
            showToastMessage("Work isn't existed");
        }
    }

    @Override
    public void exitComments() {
        replaceFragment(startTimerFragment);
    }

    public void replaceFragment(Fragment name) {
        transaction = myFragmentManager.beginTransaction();
        transaction.replace(R.id.FragmentFrame, name);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setButtonsVisibility() {
        startTimerFragment.getView().findViewById(R.id.stopTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.pauseTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.resumeTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.startTimer).setEnabled(false);
    }

    private void createTimer(@NotNull Call<TimerModel> call) {
        call.enqueue(new Callback<TimerModel>() {
            @Override
            public void onResponse(@NonNull Call<TimerModel> call, @NonNull Response<TimerModel> response) {
                if (response.isSuccessful()) {
                    try {
                        timer.cancel();
                    } catch (NullPointerException exception) {
                        showToastMessage("Timer isn't created");
                    }
                    showToastMessage("Timer stopped");
                    replaceFragment(stopTimerFragment);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            showToastMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        showToastMessage("Error happened");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimerModel> call, @NonNull Throwable t) {
                showToastMessage(t.getMessage());
            }
        });
    }

    private void showToastMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
}