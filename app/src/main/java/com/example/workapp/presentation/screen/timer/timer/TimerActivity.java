package com.example.workapp.presentation.screen.timer.timer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.network.NetworkClient;
import com.example.workapp.data.network.model.comments.CommentsModel;
import com.example.workapp.data.network.model.timer.TimerModel;
import com.example.workapp.data.network.model.work.WorkModel;
import com.example.workapp.presentation.screen.archive.ArchiveActivity;
import com.example.workapp.presentation.screen.comment.CommentActivity;
import com.example.workapp.presentation.screen.comment.CommentDialog;
import com.example.workapp.presentation.screen.main.MainActivity;
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
        StopTimerFragment.TimerCloseable, CommentDialog.EditNameDialogListener {

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
    Timer timer;
    TimerTask timerTask;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setToolbar();
        if (savedInstanceState == null) {
            myFragmentManager = getSupportFragmentManager();
            replaceFragment(startTimerFragment);
        }
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

    @Override
    public void onTimerStarted() {
        isStarted = true;
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
    public void onTimerClosed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createCommentOnServer(DialogFragment dialog) {
        Call<CommentsModel> call = NetworkClient.getCommentAPI().createComment(commentsModel);
        call.enqueue(new Callback<CommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<CommentsModel> call,
                                   @NonNull Response<CommentsModel> response) {
                if (response.isSuccessful()) {
                    commentsModel.setTime(timerOperations.setTime());
                    commentsModel.setWorkId(getIntent().getStringExtra("workId"));
                    commentsModel.setText(((EditText) dialog.getDialog().findViewById
                            (R.id.inputDialogComment)).getText().toString());
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

    public void replaceFragment(Fragment name) {
        transaction = myFragmentManager.beginTransaction();
        transaction.replace(R.id.FragmentFrame, name);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showToastMessage(String text) {
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

    private void stopService() {
        try {
            getApplicationContext().unbindService(MainActivity.serviceConnection);
        } catch (IllegalArgumentException exception) {
            showToastMessage("Work isn't existed");
        }
    }

   /* @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Fragment dialog1 = getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog1 instanceof CommentDialog){
            CommentDialog commentDialog = (CommentDialog) dialog1;
            commentDialog.getText()

        }
        //try {
            if (((EditText) dialog.getDialog().findViewById(R.id.inputDialogComment))
                    .getText().equals("")) {
          //      throw new NullPointerException();
          //  } else {
                createCommentOnServer(dialog);
            }
       // } catch (NullPointerException exception) {
       //     showToastMessage("Comment can't be empty");
       // }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Fragment dialog1 = getSupportFragmentManager().findFragmentByTag("dialog");
        if (dialog1 instanceof CommentDialog){
            CommentDialog commentDialog = (CommentDialog) dialog1;
            commentDialog.dismiss();
        }
    }*/

    @Override
    public void onFinishEditDialog(String inputText) {
        createCommentOnServer();

    }
}