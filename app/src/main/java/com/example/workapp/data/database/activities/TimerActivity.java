package com.example.workapp.data.database.activities;
/*
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.workapp.R;
import com.example.workapp.data.room_app.fragments.AddTimerCommentFragment;
import com.example.workapp.data.room_app.fragments.ShowTimerCommentsFragment;
import com.example.workapp.data.room_app.fragments.StartTimerFragment;
import com.example.workapp.data.room_app.fragments.StopTimerFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//import com.example.workapp.database.entity.AppTimer;
//import com.example.workapp.database.entity.Comment;
//import com.example.workapp.database.entity.Work;
//import com.example.workapp.database.room_dao.CommentDao;
//import com.example.workapp.database.room_dao.TimerDao;
//import com.example.workapp.database.room_dao.WorkDao;

public class TimerFragment extends AppCompatActivity implements StartTimerFragment.TimerClickListener,
        StopTimerFragment.TimerCloseable, AddTimerCommentFragment.CreateCommentsListener {
    TimerDao timerDao = db.timerDao();
    WorkDao workDao = db.workDao();
    CommentDao commentDao = db.commentDao();
    AppTimer appTimer = new AppTimer();
    Comment comment = new Comment();
    StartTimerFragment date = new StartTimerFragment();
    public Date timeOfFinish;
    public static Date timeOfStart, timeOfPauseStart, timeOfPauseFinish;
    public static boolean isStarted, isPaused, isResumed = false;
    Timer timer;
    TimerTask timerTask;
    FragmentManager myFragmentManager;
    Fragment startTimerFragment, stopTimerFragment, watchTimerComments, addCommentFragment;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_fragment);
        if (savedInstanceState == null) {
            myFragmentManager = getSupportFragmentManager();
            startTimerFragment = new StartTimerFragment();
            replaceFragment(startTimerFragment);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isStarted) {
            Objects.requireNonNull(startTimerFragment.getView()).findViewById(R.id.startTimer).setEnabled(false);
        }
    }

    @Override
    public void onTimerStarted() {
        isStarted = true;
        Objects.requireNonNull(startTimerFragment.getView()).findViewById(R.id.stopTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.pauseTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.resumeTimer).setEnabled(true);
        startTimerFragment.getView().findViewById(R.id.startTimer).setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        //appTimer.setStartTime(date.setTime());
        timeOfStart = calendar.getTime();
        //appTimer.setWorkId(getIntent().getStringExtra("workId"));
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) startTimerFragment.getView().findViewById(R.id.timerStartView))
                                .setText(date.setTime());
                    }
                });
            }
        };
        timer.schedule(timerTask, 0);
        showMessage("Timer started");
    }

    @Override
    public void onTimerStopped() {
        isStarted = false;
        isResumed = false;
        isPaused = false;
        Objects.requireNonNull(startTimerFragment.getView()).findViewById(R.id.startTimer).setEnabled(true);
        Work work = new Work();
        work.setId(getIntent().getStringExtra("workId"));
        work.setName(getIntent().getStringExtra("workName"));
        work.setCompleted(true);
        workDao.update(work);
        appTimer.setFinishTime(date.setTime());
        Calendar calendar = Calendar.getInstance();
        timeOfFinish = calendar.getTime();
       // appTimer.setElapsedTime(date.calculateDifferense(timeOfStart, timeOfFinish));
       // timerDao.insert(appTimer);
        timer.cancel();
        showMessage("Timer stopped");
        stopTimerFragment = new StopTimerFragment();
        replaceFragment(stopTimerFragment);
    }

    @Override
    public void onTimerResumed() {
        isResumed = true;
        Calendar calendar = Calendar.getInstance();
        timeOfPauseFinish = calendar.getTime();
        //appTimer.setTimeInPause(date.calculateDifferense(timeOfPauseStart, timeOfPauseFinish));
        showMessage("Timer resumed");
        ((TextView) Objects.requireNonNull(startTimerFragment.getView()).findViewById(R.id.timerEndPauseView))
                .setText(date.setTime());
    }

    @Override
    public void onTimerPaused() {
        isPaused = true;
        Calendar calendar = Calendar.getInstance();
        timeOfPauseStart = calendar.getTime();
        showMessage("Timer paused");
        ((TextView) Objects.requireNonNull(startTimerFragment.getView()).findViewById(R.id.timerPauseStartView))
                .setText(date.setTime());
    }

    @Override
    public void onTimerAddComment() {
        addCommentFragment = new AddTimerCommentFragment();
        replaceFragment(addCommentFragment);

    }

    @Override
    public void watchComments() {
        watchTimerComments = new ShowTimerCommentsFragment();
        replaceFragment(watchTimerComments);
    }

    @Override
    public void timerExit() {
        Intent intent = new Intent(TimerFragment.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTimerClosed() {
        Fragment mainTimerFragment = new StartTimerFragment();
        replaceFragment(mainTimerFragment);
    }

    @Override
    public void createComment() {
        Objects.requireNonNull(addCommentFragment.getFragmentManager()).findFragmentById(R.id.FragmentFrame);
        //comment.setText(((EditText) Objects.requireNonNull(addCommentFragment.getView()).findViewById
        //        (R.id.inputComment)).getText().toString());
        ((EditText) addCommentFragment.getView().findViewById(R.id.inputComment)).setText(null);
        comment.setTime(date.setTime());
        comment.setWorkId(getIntent().getStringExtra("workId"));
        commentDao.insert(comment);
    }

    @Override
    public void exitComments() {
        replaceFragment(startTimerFragment);
    }

    public void showMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void replaceFragment(Fragment name) {
        transaction = myFragmentManager.beginTransaction();
        transaction.replace(R.id.FragmentFrame, name);
        transaction.addToBackStack("1");
        transaction.commit();
    }
}*/